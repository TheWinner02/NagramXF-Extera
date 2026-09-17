package tw.nekomimi.nekogram.helpers;

import static org.telegram.messenger.LocaleController.getString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CheckBoxSquare;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Components.TextViewSwitcher;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import tw.nekomimi.nekogram.utils.GsonUtil;

public class CloudSettingsHelper {
    public static final SharedPreferences.OnSharedPreferenceChangeListener listener = (preferences, key) -> CloudSettingsHelper.getInstance().doAutoSync();
    private static final String[] AUTO_SYNC_PREFERENCES = {
            "nkmrcfg", "nekox_config", "pillstackconfig", "aichatconfig"
    };
    private static final SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekocloud", Context.MODE_PRIVATE);
    private final SparseArray<Long> cloudSyncedDate = new SparseArray<>();
    private final Handler handler = new Handler();
    private final SparseArray<Long> localSyncedDate = new SparseArray<>();
    private final ArrayDeque<SyncRequest> syncQueue = new ArrayDeque<>();
    private boolean syncInProgress;
    private boolean autoSync = preferences.getBoolean("auto_sync", false);

    private static final String SETTINGS_CHUNKS_COUNT_KEY = "neko_settings";
    private static final String SETTINGS_CHUNK_KEY_PREFIX = "neko_settings_";
    private static final String SETTINGS_UPDATED_AT_KEY = "neko_settings_updated_at";
    private static final String SETTINGS_ENCODING_KEY = "neko_settings_encoding";
    private static final String SETTINGS_ENCODING_GZIP_BASE64_V1 = "gzip_base64_v1";
    private static final String SETTINGS_MANIFEST_KEY = "neko_settings_manifest";
    private static final String SETTINGS_DATA_KEY_PREFIX = "neko_settings_data_";
    private static final int MAX_CHUNK_CHARS = 3000;
    private static final int MAX_CHUNKS = 256;
    private static final int MAX_RESTORED_BYTES = 10 * 1024 * 1024;
    private static final int RESTORE_BATCH_SIZE = 50;

    private final Runnable cloudSyncRunnable = () -> {
        int account = UserConfig.selectedAccount;
        CloudSettingsHelper.getInstance().syncToCloud(account, (success, error) -> {
            if (!success) {
                var global = BulletinFactory.global();
                if (error == null) {
                    global.createSimpleBulletin(R.raw.error, getString(R.string.CloudConfigSyncFailed)).show();
                } else {
                    global.createSimpleBulletin(R.raw.error, getString(R.string.CloudConfigSyncFailed), error).show();
                }
            }
        });
    };

    private CloudSettingsHelper() {
        for (String name : AUTO_SYNC_PREFERENCES) {
            ApplicationLoader.applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)
                    .registerOnSharedPreferenceChangeListener(listener);
        }
    }

    public static CloudSettingsHelper getInstance() {
        return InstanceHolder.instance;
    }

    private static String formatDateUntil(long date) {
        try {
            Calendar rightNow = Calendar.getInstance();
            int year = rightNow.get(Calendar.YEAR);
            rightNow.setTimeInMillis(date);
            int dateYear = rightNow.get(Calendar.YEAR);

            if (year == dateYear) {
                return LocaleController.getInstance().getFormatterBannedUntilThisYear().format(new Date(date));
            } else {
                return LocaleController.getInstance().getFormatterBannedUntil().format(new Date(date));
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return "LOC_ERR";
    }

    public void showDialog(BaseFragment parentFragment) {
        if (parentFragment == null) {
            return;
        }

        Context context = parentFragment.getParentActivity();
        Theme.ResourcesProvider resourcesProvider = parentFragment.getResourceProvider();
        int selectedAccount = UserConfig.selectedAccount;

        AlertDialog.Builder builder = new AlertDialog.Builder(context, resourcesProvider);
        builder.setTitle(getString(R.string.CloudConfig));
        builder.setMessage(AndroidUtilities.replaceTags(getString(R.string.CloudConfigDesc)));
        builder.setTopImage(R.drawable.cloud, Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider));

        TextViewSwitcher syncedDate = new TextViewSwitcher(context);
        syncedDate.setFactory(() -> {
            TextView tv = new TextView(context);
            tv.setGravity(Gravity.START);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            tv.setTextColor(Theme.getColor(Theme.key_dialogTextGray3, resourcesProvider));
            return tv;
        });
        syncedDate.setInAnimation(context, R.anim.alpha_in);
        syncedDate.setOutAnimation(context, R.anim.alpha_out);
        syncedDate.setText(formatSyncedDate(selectedAccount), false);

        ButtonWithCounterView restoreButton = new ButtonWithCounterView(context, false, resourcesProvider).setRound();
        restoreButton.setText(getString(R.string.CloudConfigRestore), false);
        restoreButton.setEnabled(false);
        restoreButton.setClickable(false);

        var storageHelper = getCloudStorageHelper();
        storageHelper.getItems(new String[]{SETTINGS_MANIFEST_KEY, SETTINGS_UPDATED_AT_KEY}, (res, error) -> {
            long updatedAt = getCloudUpdatedAt(res);
            if (error == null && updatedAt > 0) {
                cloudSyncedDate.put(selectedAccount, updatedAt);
                restoreButton.setEnabled(true);
                restoreButton.setClickable(true);
            } else {
                cloudSyncedDate.put(selectedAccount, -1L);
                restoreButton.setEnabled(false);
                restoreButton.setClickable(false);
            }
            syncedDate.setText(formatSyncedDate(selectedAccount));
        });

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ButtonWithCounterView buttonTextView = new ButtonWithCounterView(context, true, resourcesProvider).setRound();
        buttonTextView.setText(getString(R.string.CloudConfigSync), false);
        linearLayout.addView(buttonTextView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48, 16, 0, 16, 0));
        buttonTextView.setOnClickListener(view -> {
            syncedDate.setText(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.CloudConfigSyncing)));
            syncToCloud(selectedAccount, (success, error) -> {
                syncedDate.setText(formatSyncedDate(selectedAccount));
                if (!success) {
                    if (error == null) {
                        BulletinFactory.of(Bulletin.BulletinWindow.make(context), resourcesProvider).createSimpleBulletin(R.raw.error, getString(R.string.CloudConfigSyncFailed)).show();
                    } else {
                        BulletinFactory.of(Bulletin.BulletinWindow.make(context), resourcesProvider).createSimpleBulletin(R.raw.error, getString(R.string.CloudConfigSyncFailed), error).show();
                    }
                }
                boolean hasCloudData = cloudSyncedDate.get(selectedAccount, 0L) > 0;
                restoreButton.setEnabled(hasCloudData);
                restoreButton.setClickable(hasCloudData);
            });
        });

        linearLayout.addView(restoreButton, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48, 16, 8, 16, 0));
        restoreButton.setOnClickListener(view -> {
            if (!restoreButton.isEnabled()) return;
            syncedDate.setText(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.CloudConfigSyncing)));
            restoreFromCloud(selectedAccount, (success, error) -> {
                syncedDate.setText(formatSyncedDate(selectedAccount));
                if (!success) {
                    if (error == null) {
                        BulletinFactory.of(Bulletin.BulletinWindow.make(context), resourcesProvider).createSimpleBulletin(R.raw.error, getString(R.string.CloudConfigRestoreFailed)).show();
                    } else {
                        BulletinFactory.of(Bulletin.BulletinWindow.make(context), resourcesProvider).createSimpleBulletin(R.raw.error, getString(R.string.CloudConfigRestoreFailed), error).show();
                    }
                } else {
                    AlertDialog restart = new AlertDialog(context, 0);
                    restart.setTitle(getString(R.string.NagramX));
                    restart.setMessage(getString(R.string.RestartAppToTakeEffect));
                    restart.setPositiveButton(getString(R.string.OK), (__, ___) -> AppRestartHelper.triggerRebirth(context, new Intent(context, LaunchActivity.class)));
                    restart.show();
                }
            });
        });

        ButtonWithCounterView deleteButton = new ButtonWithCounterView(context, false, resourcesProvider).setRound();
        deleteButton.setText(getString(R.string.DeleteCloudBackup), false);
        deleteButton.setTextColor(Theme.getColor(Theme.key_dialogTextRed));
        linearLayout.addView(deleteButton, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48, 16, 8, 16, 0));
        deleteButton.setOnClickListener(view -> {
            syncedDate.setText(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.CloudConfigSyncing)));
            deleteCloudBackup(selectedAccount, (success, error) -> {
                syncedDate.setText(formatSyncedDate(selectedAccount));
                if (!success) {
                    if (error == null) {
                        BulletinFactory.of(Bulletin.BulletinWindow.make(context), resourcesProvider).createSimpleBulletin(R.raw.info, getString(R.string.CloudConfigNoBackupToDelete)).show();
                    } else {
                        BulletinFactory.of(Bulletin.BulletinWindow.make(context), resourcesProvider).createSimpleBulletin(R.raw.error, getString(R.string.DeleteCloudBackupFailed), error).show();
                    }
                } else {
                    BulletinFactory.of(Bulletin.BulletinWindow.make(context), resourcesProvider).createSimpleBulletin(R.raw.done, getString(R.string.DeleteCloudBackupSuccess)).show();
                    restoreButton.setEnabled(false);
                    restoreButton.setClickable(false);
                }
            });
        });

        MiniCheckBoxCell autoSyncCheck = new MiniCheckBoxCell(context, 8, resourcesProvider);
        autoSyncCheck.setTextAndValueAndCheck(getString(R.string.CloudConfigAutoSync), getString(R.string.CloudConfigAutoSyncDesc), autoSync);
        autoSyncCheck.setOnClickListener(view13 -> {
            autoSync = !autoSync;
            preferences.edit().putBoolean("auto_sync", autoSync).apply();
            autoSyncCheck.setChecked(autoSync);
        });
        linearLayout.addView(autoSyncCheck, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 8, 8, 8, 0));

        linearLayout.addView(syncedDate, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 16, 8, 16, 0));

        builder.setView(linearLayout);
        parentFragment.showDialog(builder.create());
    }

    public void doAutoSync() {
        if (!autoSync) {
            return;
        }
        handler.removeCallbacks(cloudSyncRunnable);
        handler.postDelayed(cloudSyncRunnable, 1200);
    }

    private void syncToCloud(int account, Utilities.Callback2<Boolean, String> callback) {
        syncQueue.add(new SyncRequest(account, callback));
        runNextCloudSync();
    }

    private void runNextCloudSync() {
        if (syncInProgress || syncQueue.isEmpty()) {
            return;
        }
        syncInProgress = true;
        SyncRequest request = syncQueue.peek();
        performCloudSync(request.account, (success, error) -> {
            syncQueue.poll();
            syncInProgress = false;
            request.callback.run(success, error);
            runNextCloudSync();
        });
    }

    private void performCloudSync(int account, Utilities.Callback2<Boolean, String> callback) {
        try {
            String settingsJson = SettingsBackupHelper.backupSettingsJson(true, 0, false);
            String payload = gzipBase64Encode(settingsJson);
            int numChunks = (int) Math.ceil((double) payload.length() / MAX_CHUNK_CHARS);
            if (numChunks <= 0 || numChunks > MAX_CHUNKS) {
                callback.run(false, "Cloud backup is too large");
                return;
            }
            String generation = UUID.randomUUID().toString().replace("-", "");
            CloudStorageHelper storage = CloudStorageHelper.getInstance(account);
            storage.getItems(new String[]{SETTINGS_MANIFEST_KEY, SETTINGS_CHUNKS_COUNT_KEY}, (values, error) -> {
                if (error != null) {
                    callback.run(false, error);
                    return;
                }
                PreviousBackup previous = PreviousBackup.from(values);
                syncChunk(account, storage, generation, payload, 0, numChunks, previous, callback);
            });
        } catch (Exception error) {
            callback.run(false, error.toString());
        }
    }

    private void syncChunk(int account, CloudStorageHelper storage, String generation, String payload, int index,
                           int numChunks, PreviousBackup previous, Utilities.Callback2<Boolean, String> callback) {
        if (index >= numChunks) {
            long updatedAt = System.currentTimeMillis();
            JSONObject manifest = new JSONObject();
            try {
                manifest.put("generation", generation);
                manifest.put("count", numChunks);
                manifest.put("encoding", SETTINGS_ENCODING_GZIP_BASE64_V1);
                manifest.put("updatedAt", updatedAt);
            } catch (Exception e) {
                cleanupGeneration(storage, generation, numChunks);
                callback.run(false, e.getLocalizedMessage());
                return;
            }
            storage.setItem(SETTINGS_MANIFEST_KEY, manifest.toString(), (res, error) -> {
                if (error == null) {
                    setLocalSyncedDate(account, updatedAt);
                    cloudSyncedDate.put(account, updatedAt);
                    cleanupPreviousCloudData(storage, previous);
                    callback.run(true, null);
                } else {
                    cleanupGeneration(storage, generation, numChunks);
                    callback.run(false, error);
                }
            });
            return;
        }

        int startIndex = index * MAX_CHUNK_CHARS;
        int endIndex = Math.min(startIndex + MAX_CHUNK_CHARS, payload.length());
        String chunk = payload.substring(startIndex, endIndex);
        String storageKey = getDataKey(generation, index);

        storage.setItem(storageKey, chunk, (res, error) -> {
            if (error != null) {
                cleanupGeneration(storage, generation, index);
                callback.run(false, error);
            } else {
                syncChunk(account, storage, generation, payload, index + 1, numChunks, previous, callback);
            }
        });
    }

    private void restoreFromCloud(int account, Utilities.Callback2<Boolean, String> callback) {
        CloudStorageHelper storage = CloudStorageHelper.getInstance(account);
        storage.getItems(new String[]{SETTINGS_MANIFEST_KEY, SETTINGS_CHUNKS_COUNT_KEY, SETTINGS_ENCODING_KEY}, (meta, metaError) -> {
            if (metaError != null || meta == null) {
                callback.run(false, metaError);
                return;
            }
            int numChunks;
            String encoding;
            String generationValue;
            try {
                String manifestValue = meta.get(SETTINGS_MANIFEST_KEY);
                if (!TextUtils.isEmpty(manifestValue)) {
                    JSONObject manifest = new JSONObject(manifestValue);
                    generationValue = manifest.getString("generation");
                    numChunks = manifest.getInt("count");
                    encoding = manifest.getString("encoding");
                } else {
                    String countStr = meta.get(SETTINGS_CHUNKS_COUNT_KEY);
                    if (!AndroidUtilities.isNumeric(countStr)) {
                        callback.run(false, null);
                        return;
                    }
                    numChunks = Integer.parseInt(countStr);
                    encoding = meta.get(SETTINGS_ENCODING_KEY);
                    generationValue = null;
                }
            } catch (Exception e) {
                FileLog.e(e);
                callback.run(false, e.getLocalizedMessage());
                return;
            }
            if (numChunks <= 0 || numChunks > MAX_CHUNKS) {
                callback.run(false, "Invalid cloud backup size");
                return;
            }
            final String generation = generationValue;
            fetchChunksFromCloud(storage, generation, numChunks, 0, new StringBuilder(), (payload, chunksError) -> {
                if (chunksError != null) {
                    callback.run(false, chunksError);
                    return;
                }
                try {
                    String json;
                    if (SETTINGS_ENCODING_GZIP_BASE64_V1.equals(encoding)) {
                        json = gzipBase64Decode(payload, MAX_RESTORED_BYTES);
                    } else {
                        json = payload;
                    }
                    SettingsBackupHelper.importCloudSettings(GsonUtil.toJsonObject(json));
                    setLocalSyncedDate(account, System.currentTimeMillis());
                    callback.run(true, null);
                } catch (Exception e) {
                    FileLog.e(e);
                    callback.run(false, e.getLocalizedMessage());
                }
            });
        });
    }

    private void fetchChunksFromCloud(CloudStorageHelper storage, String generation, int numChunks, int offset,
                                      StringBuilder sb, Utilities.Callback2<String, String> callback) {
        if (offset >= numChunks) {
            callback.run(sb.toString(), null);
            return;
        }
        int end = Math.min(offset + RESTORE_BATCH_SIZE, numChunks);
        String[] keys = new String[end - offset];
        for (int i = offset; i < end; i++) {
            keys[i - offset] = generation == null ? SETTINGS_CHUNK_KEY_PREFIX + i : getDataKey(generation, i);
        }
        storage.getItems(keys, (res, error) -> {
            if (error != null || res == null) {
                callback.run(null, error);
                return;
            }
            for (int i = offset; i < end; i++) {
                String chunk = res.get(keys[i - offset]);
                if (chunk == null) {
                    callback.run(null, "Chunk " + i + " is missing");
                    return;
                }
                sb.append(chunk);
            }
            fetchChunksFromCloud(storage, generation, numChunks, end, sb, callback);
        });
    }

    private static String gzipBase64Encode(String input) throws Exception {
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(baos)) {
            gzip.write(inputBytes);
        }
        return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
    }

    @SuppressWarnings("StringOperationCanBeSimplified") // API 33
    private static String gzipBase64Decode(String input, int maxBytes) throws Exception {
        byte[] compressed = Base64.decode(input, Base64.DEFAULT);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = gzip.read(buffer)) != -1) {
                if (baos.size() + read > maxBytes) {
                    throw new IllegalArgumentException("Cloud backup is too large");
                }
                baos.write(buffer, 0, read);
            }
        }
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    private void deleteCloudBackup(int account, Utilities.Callback2<Boolean, String> callback) {
        if (syncInProgress) {
            handler.postDelayed(() -> deleteCloudBackup(account, callback), 100);
            return;
        }
        syncInProgress = true;
        performDeleteCloudBackup(account, (success, error) -> {
            syncInProgress = false;
            callback.run(success, error);
            runNextCloudSync();
        });
    }

    private void performDeleteCloudBackup(int account, Utilities.Callback2<Boolean, String> callback) {
        CloudStorageHelper storage = CloudStorageHelper.getInstance(account);
        storage.getKeys((keys, error) -> {
            if (error != null) {
                callback.run(false, error);
                return;
            }
            if (keys == null || keys.length == 0) {
                callback.run(false, null);
                return;
            }

            ArrayList<String> nekoKeys = new ArrayList<>();
            for (String key : keys) {
                if (key.startsWith("neko_settings")) {
                    nekoKeys.add(key);
                }
            }

            if (nekoKeys.isEmpty()) {
                callback.run(false, null);
                return;
            }

            storage.removeItems(nekoKeys.toArray(new String[0]), (res_, error_) -> {
                if (error_ == null) {
                    cloudSyncedDate.put(account, -1L);
                    callback.run(true, null);
                } else {
                    callback.run(false, error_);
                }
            });
        });
    }

    private static String getDataKey(String generation, int index) {
        return SETTINGS_DATA_KEY_PREFIX + generation + "_" + index;
    }

    private static long getCloudUpdatedAt(HashMap<String, String> values) {
        if (values == null) {
            return -1;
        }
        String manifestValue = values.get(SETTINGS_MANIFEST_KEY);
        if (!TextUtils.isEmpty(manifestValue)) {
            try {
                long updatedAt = new JSONObject(manifestValue).getLong("updatedAt");
                return updatedAt > 0 ? updatedAt : -1;
            } catch (Exception e) {
                FileLog.e(e);
                return -1;
            }
        }
        String legacyUpdatedAt = values.get(SETTINGS_UPDATED_AT_KEY);
        if (AndroidUtilities.isNumeric(legacyUpdatedAt)) {
            try {
                return Long.parseLong(legacyUpdatedAt);
            } catch (NumberFormatException e) {
                FileLog.e(e);
            }
        }
        return -1;
    }

    private long getLocalSyncedDate(int account) {
        Long cached = localSyncedDate.get(account);
        if (cached != null) {
            return cached;
        }
        long value = preferences.getLong("updated_at_" + account,
                account == 0 ? preferences.getLong("updated_at", -1) : -1);
        localSyncedDate.put(account, value);
        return value;
    }

    private void setLocalSyncedDate(int account, long value) {
        localSyncedDate.put(account, value);
        preferences.edit().putLong("updated_at_" + account, value).apply();
    }

    private void cleanupPreviousCloudData(CloudStorageHelper storage, PreviousBackup previous) {
        if (previous == null) {
            return;
        }
        ArrayList<String> oldKeys = new ArrayList<>();
        if (previous.generation == null) {
            oldKeys.add(SETTINGS_CHUNKS_COUNT_KEY);
            oldKeys.add(SETTINGS_UPDATED_AT_KEY);
            oldKeys.add(SETTINGS_ENCODING_KEY);
            for (int i = 0; i < previous.count; i++) {
                oldKeys.add(SETTINGS_CHUNK_KEY_PREFIX + i);
            }
        } else {
            for (int i = 0; i < previous.count; i++) {
                oldKeys.add(getDataKey(previous.generation, i));
            }
        }
        if (!oldKeys.isEmpty()) {
            storage.removeItems(oldKeys.toArray(new String[0]), null);
        }
    }

    private void cleanupGeneration(CloudStorageHelper storage, String generation, int count) {
        if (count <= 0) {
            return;
        }
        String[] keys = new String[count];
        for (int i = 0; i < count; i++) {
            keys[i] = getDataKey(generation, i);
        }
        storage.removeItems(keys, null);
    }

    private CloudStorageHelper getCloudStorageHelper() {
        return CloudStorageHelper.getInstance(UserConfig.selectedAccount);
    }

    private String formatSyncedDate(int account) {
        long localDate = getLocalSyncedDate(account);
        return LocaleController.formatString(R.string.CloudConfigSyncDate, localDate > 0 ? formatDateUntil(localDate) : getString(R.string.CloudConfigSyncDateNever), cloudSyncedDate.get(account, 0L) > 0 ? formatDateUntil(cloudSyncedDate.get(account, 0L)) : getString(R.string.CloudConfigSyncDateNever));
    }

    private static final class InstanceHolder {
        private static final CloudSettingsHelper instance = new CloudSettingsHelper();
    }

    private static final class SyncRequest {
        final int account;
        final Utilities.Callback2<Boolean, String> callback;

        SyncRequest(int account, Utilities.Callback2<Boolean, String> callback) {
            this.account = account;
            this.callback = callback;
        }
    }

    private static final class PreviousBackup {
        final String generation;
        final int count;

        PreviousBackup(String generation, int count) {
            this.generation = generation;
            this.count = count;
        }

        static PreviousBackup from(HashMap<String, String> values) {
            if (values == null) {
                return null;
            }
            String manifestValue = values.get(SETTINGS_MANIFEST_KEY);
            if (!TextUtils.isEmpty(manifestValue)) {
                try {
                    JSONObject manifest = new JSONObject(manifestValue);
                    int count = manifest.getInt("count");
                    if (count > 0 && count <= MAX_CHUNKS) {
                        return new PreviousBackup(manifest.getString("generation"), count);
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
                return null;
            }
            String countValue = values.get(SETTINGS_CHUNKS_COUNT_KEY);
            if (AndroidUtilities.isNumeric(countValue)) {
                try {
                    int count = Integer.parseInt(countValue);
                    if (count > 0 && count <= MAX_CHUNKS) {
                        return new PreviousBackup(null, count);
                    }
                } catch (NumberFormatException e) {
                    FileLog.e(e);
                }
            }
            return null;
        }
    }

    @SuppressLint("ViewConstructor")
    private static class MiniCheckBoxCell extends FrameLayout {

        private final TextView textView;
        private final TextView valueTextView;
        private final CheckBoxSquare checkBox;

        public MiniCheckBoxCell(Context context, int padding, Theme.ResourcesProvider resourcesProvider) {
            super(context);

            ScaleStateListAnimator.apply(this, .02f, 1.2f);

            setForeground(Theme.createRadSelectorDrawable(Theme.multAlpha(Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider), .10f), 16, 16));

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            textView = new TextView(context);
            textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, resourcesProvider));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            textView.setLines(1);
            textView.setMaxLines(1);
            textView.setSingleLine(true);
            textView.setGravity((LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT) | Gravity.CENTER_VERTICAL);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            linearLayout.addView(textView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

            valueTextView = new TextView(context);
            valueTextView.setTextColor(Theme.getColor(Theme.key_dialogIcon, resourcesProvider));
            valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
            valueTextView.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
            valueTextView.setEllipsize(TextUtils.TruncateAt.END);
            linearLayout.addView(valueTextView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 0));

            addView(linearLayout, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.TOP, LocaleController.isRTL ? 22 + padding : padding, 4, LocaleController.isRTL ? padding : 22 + padding, 4));

            checkBox = new CheckBoxSquare(context, true, resourcesProvider);
            addView(checkBox, LayoutHelper.createFrame(18, 18, (LocaleController.isRTL ? Gravity.LEFT : Gravity.RIGHT) | Gravity.CENTER_VERTICAL, LocaleController.isRTL ? padding : 4, 0, LocaleController.isRTL ? 4 : padding, 0));
        }

        public void setTextAndValueAndCheck(String text, String value, boolean checked) {
            textView.setText(text);
            valueTextView.setText(value);
            checkBox.setChecked(checked, false);
        }

        public boolean isChecked() {
            return checkBox.isChecked();
        }

        public void setChecked(boolean checked) {
            checkBox.setChecked(checked, true);
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setClassName("android.widget.CheckBox");
            info.setCheckable(true);
            info.setChecked(checkBox.isChecked());
            StringBuilder sb = new StringBuilder();
            sb.append(textView.getText());
            if (!TextUtils.isEmpty(valueTextView.getText())) {
                sb.append('\n');
                sb.append(valueTextView.getText());
            }
            info.setContentDescription(sb);
        }
    }
}

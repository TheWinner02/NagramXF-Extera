package tw.nekomimi.nekogram.helpers;

import static org.telegram.messenger.AndroidUtilities.dp;
import static org.telegram.messenger.LocaleController.getString;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.radolyn.ayugram.controllers.AyuSavePreferences;
import com.radolyn.ayugram.AyuGhostPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.PushListenerController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.LaunchActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.function.Function;

import tw.nekomimi.nekogram.DialogConfig;
import tw.nekomimi.nekogram.NekoConfig;
import tw.nekomimi.nekogram.config.ConfigItem;
import tw.nekomimi.nekogram.utils.AlertUtil;
import tw.nekomimi.nekogram.utils.FileUtil;
import tw.nekomimi.nekogram.utils.GsonUtil;
import tw.nekomimi.nekogram.utils.ShareUtil;
import xyz.nextalone.nagram.NaConfig;
import xyz.nextalone.nagram.helper.BookmarksHelper;
import xyz.nextalone.nagram.helper.LocalPeerColorHelper;
import xyz.nextalone.nagram.helper.LocalPremiumStatusHelper;

public final class SettingsBackupHelper {
    private static final String META_SECTION = "_meta";
    private static final String META_VERSION = "version";
    private static final String META_INCLUDE_API_KEYS = "includeApiKeys";
    private static final String META_CLOUD = "cloud";
    private static final int BACKUP_VERSION = 2;

    private static final Set<String> SUPPORTED_PREFERENCES = new HashSet<>();

    static {
        Collections.addAll(SUPPORTED_PREFERENCES,
                "userconfing", "mainconfig", "themeconfig", "nkmrcfg",
                "nekox_config", "pillstackconfig", "aichatconfig");
    }

    public static String backupSettingsJson(boolean isCloud, int indentSpaces) throws JSONException {
        return backupSettingsJson(isCloud, indentSpaces, true);
    }

    public static String backupSettingsJson(boolean isCloud, int indentSpaces, boolean includeApiKeys) throws JSONException {

        JSONObject configJson = new JSONObject();

        JSONObject metadata = new JSONObject();
        metadata.put(META_VERSION, BACKUP_VERSION);
        metadata.put(META_INCLUDE_API_KEYS, includeApiKeys);
        metadata.put(META_CLOUD, isCloud);
        configJson.put(META_SECTION, metadata);

        Set<String> userconfig = getUserConfigKeys();
        spToJSON("userconfing", configJson, userconfig::contains, isCloud);

        Set<String> mainconfig = getMainConfigKeys(isCloud);
        spToJSON("mainconfig", configJson, mainconfig::contains);
        if (!isCloud) spToJSON("themeconfig", configJson, null);
        spToJSON("nkmrcfg", configJson, null, includeApiKeys);
        spToJSON("nekox_config", configJson, null, includeApiKeys);
        spToJSON("pillstackconfig", configJson, null, includeApiKeys);
        spToJSON("aichatconfig", configJson, includeApiKeys ? null : key -> !"services".equals(key), includeApiKeys);

        return configJson.toString(indentSpaces);
    }

    private static Set<String> getUserConfigKeys() {
        Set<String> keys = new HashSet<>();
        keys.add("saveIncomingPhotos");
        return keys;
    }

    private static Set<String> getMainConfigKeys(boolean isCloud) {
        Set<String> mainconfig = new HashSet<>();
        mainconfig.add("saveToGallery");
        mainconfig.add("autoplayGifs");
        mainconfig.add("autoplayVideo");
        mainconfig.add("mapPreviewType");
        mainconfig.add("raiseToSpeak");
        mainconfig.add("customTabs");
        mainconfig.add("directShare");
        mainconfig.add("shuffleMusic");
        mainconfig.add("playOrderReversed");
        mainconfig.add("inappCamera");
        mainconfig.add("repeatMode");
        mainconfig.add("fontSize");
        mainconfig.add("bubbleRadius");
        mainconfig.add("ivFontSize");
        mainconfig.add("allowBigEmoji");
        mainconfig.add("streamMedia");
        mainconfig.add("saveStreamMedia");
        mainconfig.add("smoothKeyboard");
        mainconfig.add("pauseMusicOnRecord");
        mainconfig.add("streamAllVideo");
        mainconfig.add("streamMkv");
        mainconfig.add("suggestStickers");
        mainconfig.add("sortContactsByName");
        mainconfig.add("sortFilesByName");
        mainconfig.add("noSoundHintShowed");
        mainconfig.add("directShareHash");
        mainconfig.add("useThreeLinesLayout");
        mainconfig.add("archiveHidden");
        mainconfig.add("distanceSystemType");
        mainconfig.add("loopStickers");
        mainconfig.add("keepMedia");
        mainconfig.add("noStatusBar");
        mainconfig.add("lastKeepMediaCheckTime");
        mainconfig.add("searchMessagesAsListHintShows");
        mainconfig.add("searchMessagesAsListUsed");
        mainconfig.add("stickersReorderingHintUsed");
        mainconfig.add("textSelectionHintShows");
        mainconfig.add("scheduledOrNoSoundHintShows");
        mainconfig.add("lockRecordAudioVideoHint");
        mainconfig.add("disableVoiceAudioEffects");
        mainconfig.add("chatSwipeAction");

        if (!isCloud) mainconfig.add("theme");
        mainconfig.add("selectedAutoNightType");
        mainconfig.add("autoNightScheduleByLocation");
        mainconfig.add("autoNightBrighnessThreshold");
        mainconfig.add("autoNightDayStartTime");
        mainconfig.add("autoNightDayEndTime");
        mainconfig.add("autoNightSunriseTime");
        mainconfig.add("autoNightCityName");
        mainconfig.add("autoNightSunsetTime");
        mainconfig.add("autoNightLocationLatitude3");
        mainconfig.add("autoNightLocationLongitude3");
        mainconfig.add("autoNightLastSunCheckDay");

        mainconfig.add("lang_code");

        mainconfig.add("web_restricted_domains2");

        return mainconfig;
    }

    private static void spToJSON(String sp, JSONObject object, Function<String, Boolean> filter) throws JSONException {
        spToJSON(sp, object, filter, true);
    }

    private static void spToJSON(String sp, JSONObject object, Function<String, Boolean> filter, boolean includeApiKeys) throws JSONException {
        SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences(sp, Activity.MODE_PRIVATE);
        JSONObject jsonConfig = new JSONObject();
        for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
            String key = entry.getKey();
            if ("nkmrcfg".equals(sp) && isDeviceSpecificPushKey(key)) {
                continue;
            }
            if (!includeApiKeys && isSensitiveKey(key)) {
                continue;
            }
            if (filter != null && !filter.apply(key)) {
                continue;
            }
            if (entry.getValue() instanceof Long) {
                key = key + "_long";
            } else if (entry.getValue() instanceof Float) {
                key = key + "_float";
            }
            Object value = entry.getValue();
            if (value instanceof Set) {
                value = new JSONArray(new ArrayList<>((Set<?>) value));
            }
            jsonConfig.put(key, value);
        }
        object.put(sp, jsonConfig);
    }

    public static void importSettings(Context context, File settingsFile) {
        try {
            if (settingsFile == null || !settingsFile.isFile()) {
                throw new IllegalArgumentException("Settings backup not found");
            }
            if (settingsFile.length() > 10L * 1024L * 1024L) {
                throw new IllegalArgumentException("Settings backup is too large");
            }
            JsonObject configJson = GsonUtil.toJsonObject(FileUtil.readUtf8String(settingsFile));
            prepareImport(configJson);
            AlertUtil.showConfirm(context,
                    getString(R.string.ImportSettingsAlertSafe),
                    R.drawable.msg_photo_settings_solar,
                    getString(R.string.Import),
                    true,
                    () -> importSettingsConfirmed(context, configJson));
        } catch (Exception e) {
            AlertUtil.showSimpleAlert(context, e);
        }
    }

    private static void importSettingsConfirmed(Context context, JsonObject configJson) {
        try {
            importSettings(configJson);

            AlertDialog restart = new AlertDialog(context, 0);
            restart.setTitle(getString(R.string.NagramX));
            restart.setMessage(getString(R.string.RestartAppToTakeEffect));
            restart.setPositiveButton(getString(R.string.OK), (__, ___) -> AppRestartHelper.triggerRebirth(context, new Intent(context, LaunchActivity.class)));
            restart.show();
        } catch (Exception e) {
            AlertUtil.showSimpleAlert(context, e);
        }
    }

    @SuppressLint("ApplySharedPref")
    public static void importSettings(JsonObject configJson) throws JSONException {
        importSettings(configJson, true);
    }

    public static void importCloudSettings(JsonObject configJson) throws JSONException {
        importSettings(configJson, false);
    }

    @SuppressLint("ApplySharedPref")
    private static void importSettings(JsonObject configJson, boolean allowApiKeys) throws JSONException {
        List<PreparedSection> sections = prepareImport(configJson, allowApiKeys);
        ArrayList<PreparedSection> committed = new ArrayList<>();
        try {
            for (PreparedSection section : sections) {
                SharedPreferences.Editor editor = section.preferences.edit();
                for (String key : section.keysToRemove) {
                    editor.remove(key);
                }
                for (Map.Entry<String, Object> value : section.values.entrySet()) {
                    putPreference(editor, value.getKey(), value.getValue());
                }
                committed.add(section);
                if (!editor.commit()) {
                    throw new IllegalStateException("Unable to save imported settings");
                }
            }
        } catch (Exception e) {
            for (PreparedSection section : committed) {
                restorePreferences(section.preferences, section.snapshot);
            }
            if (e instanceof JSONException) {
                throw (JSONException) e;
            }
            throw new JSONException(e.getMessage());
        }
        PushListenerController.reconcilePushRegistration();
    }

    private static List<PreparedSection> prepareImport(JsonObject configJson) throws JSONException {
        return prepareImport(configJson, true);
    }

    private static List<PreparedSection> prepareImport(JsonObject configJson, boolean allowApiKeys) throws JSONException {
        if (configJson == null) {
            throw new JSONException("Invalid settings backup");
        }

        boolean includeApiKeys = false;
        boolean cloudBackup = !configJson.has("themeconfig");
        JsonElement metadataElement = configJson.get(META_SECTION);
        if (metadataElement != null) {
            if (!metadataElement.isJsonObject()) {
                throw new JSONException("Invalid backup metadata");
            }
            JsonObject metadata = metadataElement.getAsJsonObject();
            if (metadata.has(META_VERSION) && metadata.get(META_VERSION).getAsInt() > BACKUP_VERSION) {
                throw new JSONException("This settings backup was created by a newer app version");
            }
            if (metadata.has(META_INCLUDE_API_KEYS)) {
                includeApiKeys = metadata.get(META_INCLUDE_API_KEYS).getAsBoolean();
            }
            if (metadata.has(META_CLOUD)) {
                cloudBackup = metadata.get(META_CLOUD).getAsBoolean();
            }
        }
        includeApiKeys &= allowApiKeys;

        Map<String, tw.nekomimi.nekogram.config.ConfigItem> configTypes = new HashMap<>();
        try {
            configTypes.putAll(NekoConfig.getConfigTypes());
            configTypes.putAll(NaConfig.INSTANCE.getConfigTypes());
        } catch (Throwable ignore) {
        }
        String[] preservePrefixes = {
                AyuGhostPreferences.ghostReadExclusionPrefix,
                AyuGhostPreferences.ghostTypingExclusionPrefix,
                AyuSavePreferences.saveExclusionPrefix,
                LocalNameHelper.chatNameOverridePrefix,
                LocalNameHelper.userNameOverridePrefix,
                DialogConfig.customForumTabPrefix,
                LocalPeerColorHelper.KEY_PREFIX,
                LocalPremiumStatusHelper.KEY_PREFIX,
                BookmarksHelper.KEY_PREFIX
        };

        ArrayList<PreparedSection> sections = new ArrayList<>();
        for (Map.Entry<String, JsonElement> element : configJson.entrySet()) {
            String spName = element.getKey();
            if (META_SECTION.equals(spName)) {
                continue;
            }
            if (!SUPPORTED_PREFERENCES.contains(spName)) {
                throw new JSONException("Unsupported settings section: " + spName);
            }
            if (!element.getValue().isJsonObject()) {
                throw new JSONException("Invalid settings section: " + spName);
            }
            SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences(spName, Activity.MODE_PRIVATE);
            Map<String, Object> values = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> config : element.getValue().getAsJsonObject().entrySet()) {
                String encodedKey = config.getKey();
                String key = decodePreferenceKey(encodedKey);
                if ("nkmrcfg".equals(spName) && isDeviceSpecificPushKey(key)) {
                    continue;
                }
                if (!includeApiKeys && (isSensitivePreference(spName, key)
                        || ("aichatconfig".equals(spName) && "services".equals(key)))) {
                    continue;
                }
                if ("nkmrcfg".equals(spName)) {
                    tw.nekomimi.nekogram.config.ConfigItem item = configTypes.get(key);
                    if (item == null && !startsWithAny(key, preservePrefixes)) {
                        continue;
                    }
                    if (item != null && !isCompatibleConfigValue(encodedKey, config.getValue(), item.type)) {
                        throw new JSONException("Invalid value for setting: " + key);
                    }
                } else if ("userconfing".equals(spName) && !getUserConfigKeys().contains(key)) {
                    continue;
                } else if ("mainconfig".equals(spName) && !getMainConfigKeys(cloudBackup).contains(key)) {
                    continue;
                }
                if (values.containsKey(key)) {
                    throw new JSONException("Duplicate setting: " + key);
                }
                values.put(key, parsePreferenceValue(encodedKey, config.getValue()));
            }
            Set<String> keysToRemove = getKeysToRemove(spName, preferences, configTypes, preservePrefixes, includeApiKeys, cloudBackup);
            sections.add(new PreparedSection(preferences, values, keysToRemove, new HashMap<>(preferences.getAll())));
        }
        if (sections.isEmpty()) {
            throw new JSONException("The file does not contain any supported settings");
        }
        return sections;
    }

    private static boolean isCompatibleConfigValue(String encodedKey, JsonElement value, int type) {
        String key = decodePreferenceKey(encodedKey);
        if (key.equals(NaConfig.INSTANCE.getPushServiceType().getKey())) {
            return value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber() && value.getAsInt() >= 0 && value.getAsInt() <= 3;
        }
        if (type == ConfigItem.configTypeBool || type == ConfigItem.configTypeBoolLinkInt) {
            return value.isJsonPrimitive() && value.getAsJsonPrimitive().isBoolean();
        }
        if (type == ConfigItem.configTypeInt) {
            return value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber() && !encodedKey.endsWith("_long") && !encodedKey.endsWith("_float");
        }
        if (type == ConfigItem.configTypeLong) {
            return value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber() && encodedKey.endsWith("_long");
        }
        if (type == ConfigItem.configTypeFloat) {
            return value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber() && encodedKey.endsWith("_float");
        }
        if (type == ConfigItem.configTypeSetInt) {
            return value.isJsonArray();
        }
        return value.isJsonPrimitive() && value.getAsJsonPrimitive().isString();
    }

    private static Object parsePreferenceValue(String encodedKey, JsonElement element) throws JSONException {
        if (element.isJsonArray()) {
            Set<String> values = new HashSet<>();
            JsonArray array = element.getAsJsonArray();
            for (JsonElement item : array) {
                if (!item.isJsonPrimitive() || !item.getAsJsonPrimitive().isString()) {
                    throw new JSONException("Invalid string set: " + decodePreferenceKey(encodedKey));
                }
                values.add(item.getAsString());
            }
            return values;
        }
        if (!element.isJsonPrimitive()) {
            throw new JSONException("Unsupported value for setting: " + decodePreferenceKey(encodedKey));
        }
        JsonPrimitive value = element.getAsJsonPrimitive();
        if (value.isBoolean()) {
            return value.getAsBoolean();
        }
        if (value.isNumber()) {
            if (encodedKey.endsWith("_long")) {
                return value.getAsLong();
            }
            if (encodedKey.endsWith("_float")) {
                return value.getAsFloat();
            }
            return value.getAsInt();
        }
        if (value.isString()) {
            return value.getAsString();
        }
        throw new JSONException("Unsupported value for setting: " + decodePreferenceKey(encodedKey));
    }

    private static String decodePreferenceKey(String key) {
        if (key.endsWith("_long")) {
            return key.substring(0, key.length() - "_long".length());
        }
        if (key.endsWith("_float")) {
            return key.substring(0, key.length() - "_float".length());
        }
        return key;
    }

    private static Set<String> getKeysToRemove(String spName, SharedPreferences preferences,
                                                Map<String, tw.nekomimi.nekogram.config.ConfigItem> configTypes,
                                                String[] preservePrefixes, boolean includeApiKeys,
                                                boolean cloudBackup) {
        Set<String> keys = new HashSet<>();
        if ("userconfing".equals(spName)) {
            keys.addAll(getUserConfigKeys());
        } else if ("mainconfig".equals(spName)) {
            keys.addAll(getMainConfigKeys(cloudBackup));
        } else {
            for (String key : preferences.getAll().keySet()) {
                if (!"nkmrcfg".equals(spName) || configTypes.containsKey(key) || startsWithAny(key, preservePrefixes)) {
                    keys.add(key);
                }
            }
        }
        keys.removeIf(key -> isDeviceSpecificPushKey(key)
                || (!includeApiKeys && isSensitivePreference(spName, key))
                || ("aichatconfig".equals(spName) && !includeApiKeys && "services".equals(key)));
        return keys;
    }

    private static boolean startsWithAny(String key, String[] prefixes) {
        for (String prefix : prefixes) {
            if (key.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSensitivePreference(String spName, String key) {
        return ("nkmrcfg".equals(spName) || "nekox_config".equals(spName)
                || "pillstackconfig".equals(spName) || "aichatconfig".equals(spName))
                && isSensitiveKey(key);
    }

    private static boolean isSensitiveKey(String key) {
        String normalized = key.toLowerCase(Locale.ROOT);
        return normalized.endsWith("key") || normalized.contains("apikey") || normalized.contains("api_key")
                || normalized.contains("token") || normalized.contains("secret")
                || normalized.contains("accountid") || normalized.contains("password");
    }

    @SuppressWarnings("unchecked")
    private static void putPreference(SharedPreferences.Editor editor, String key, Object value) {
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, new HashSet<>((Set<String>) value));
        } else {
            editor.putString(key, String.valueOf(value));
        }
    }

    private static void restorePreferences(SharedPreferences preferences, Map<String, ?> snapshot) {
        SharedPreferences.Editor editor = preferences.edit().clear();
        for (Map.Entry<String, ?> value : snapshot.entrySet()) {
            putPreference(editor, value.getKey(), value.getValue());
        }
        editor.commit();
    }

    private static final class PreparedSection {
        final SharedPreferences preferences;
        final Map<String, Object> values;
        final Set<String> keysToRemove;
        final Map<String, ?> snapshot;

        PreparedSection(SharedPreferences preferences, Map<String, Object> values,
                        Set<String> keysToRemove, Map<String, ?> snapshot) {
            this.preferences = preferences;
            this.values = values;
            this.keysToRemove = keysToRemove;
            this.snapshot = snapshot;
        }
    }

    public static void backupSettings(Context context, Theme.ResourcesProvider resourceProvider) {
        if (context == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.BackupSettings));

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        CheckBoxCell checkBoxCell = new CheckBoxCell(context, CheckBoxCell.TYPE_CHECK_BOX_DEFAULT, resourceProvider);
        checkBoxCell.setBackground(Theme.getSelectorDrawable(false));
        checkBoxCell.setText(getString(R.string.ExportSettingsIncludeApiKeys), "", true, false);
        checkBoxCell.setPadding(LocaleController.isRTL ? dp(16) : dp(8), 0, LocaleController.isRTL ? dp(8) : dp(16), 0);
        checkBoxCell.setChecked(false, false);
        checkBoxCell.setOnClickListener(v -> {
            CheckBoxCell cell = (CheckBoxCell) v;
            cell.setChecked(!cell.isChecked(), true);
        });
        linearLayout.addView(checkBoxCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        builder.setView(linearLayout);
        builder.setPositiveButton(getString(R.string.ExportTheme), (dialog, which) -> {
            boolean includeApiKeys = checkBoxCell.isChecked();
            try {
                String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(new Date());
                File cacheFile = new File(AndroidUtilities.getCacheDir(), timestamp + ".nekox-settings.json");
                FileUtil.writeUtf8String(SettingsBackupHelper.backupSettingsJson(false, 4, includeApiKeys), cacheFile);
                ShareUtil.shareFile(context, cacheFile, "", "application/json");
            } catch (Exception e) {
                AlertUtil.showSimpleAlert(context, e);
            }
        });
        builder.setNegativeButton(getString(R.string.Cancel), null);
        builder.show();
    }

    private static boolean isDeviceSpecificPushKey(String key) {
        return key.equals(NaConfig.INSTANCE.getPushServiceTypeUnifiedSimple().getKey())
                || key.equals(NaConfig.INSTANCE.getPushServiceTypeUnifiedWebPushPrivateKey().getKey())
                || key.equals(NaConfig.INSTANCE.getPushServiceTypeUnifiedWebPushPublicKey().getKey())
                || key.equals(NaConfig.INSTANCE.getPushServiceTypeUnifiedWebPushAuthSecret().getKey());
    }
}

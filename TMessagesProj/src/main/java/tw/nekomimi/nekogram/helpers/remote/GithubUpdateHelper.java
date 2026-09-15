package tw.nekomimi.nekogram.helpers.remote;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tw.nekomimi.nekogram.utils.HttpClient;

public class GithubUpdateHelper {

    private static final String GITHUB_REPO = "TheWinner02/NagramXF-Extera";
    private static final String USER_AGENT = "NegramXFE-Updater";
    private static final String LATEST_RELEASE_URL = "https://api.github.com/repos/" + GITHUB_REPO + "/releases/latest";

    public interface Delegate {
        void onResult(TLRPC.TL_help_appUpdate update, String error);
    }

    public interface DownloadDelegate {
        void onDone(boolean installed, String error);
    }

    public static void check(boolean updateAlways, Delegate delegate) {
        if (delegate == null) {
            return;
        }
        Utilities.globalQueue.postRunnable(() -> {
            try {
                FileLog.d("GithubUpdateHelper: checking updates, updateAlways=" + updateAlways);
                TLRPC.TL_help_appUpdate update = findUpdate(updateAlways);
                FileLog.d("GithubUpdateHelper: check finished, update=" + (update != null ? update.version : "null"));
                AndroidUtilitiesBridge.runOnUIThread(() -> delegate.onResult(update, null));
            } catch (Exception e) {
                FileLog.e(e);
                AndroidUtilitiesBridge.runOnUIThread(() -> delegate.onResult(null, e.getMessage()));
            }
        });
    }

    private static TLRPC.TL_help_appUpdate findUpdate(boolean updateAlways) throws IOException, JSONException {
        JSONObject release = loadRelease();
        if (release == null) {
            FileLog.d("GithubUpdateHelper: no release response");
            return null;
        }
        String tagName = release.optString("tag_name", "");
        FileLog.d("GithubUpdateHelper: latest release tag=" + tagName + ", current=" + getCurrentVersion());
        if (!updateAlways && !isNewerVersion(getCurrentVersion(), tagName)) {
            FileLog.d("GithubUpdateHelper: release is not newer");
            return null;
        }
        JSONObject asset = findBestApkAsset(release.optJSONArray("assets"));
        if (asset == null) {
            FileLog.d("GithubUpdateHelper: release has no apk asset");
            return null;
        }

        TLRPC.TL_help_appUpdate update = new TLRPC.TL_help_appUpdate();
        update.version = tagName;
        update.text = release.optString("body", "");
        update.url = asset.optString("browser_download_url", "");
        FileLog.d("GithubUpdateHelper: selected asset=" + asset.optString("name", "") + ", url=" + update.url);
        update.flags |= 4;
        update.can_not_skip = false;
        return update;
    }

    private static JSONObject loadRelease() throws IOException, JSONException {
        OkHttpClient client = HttpClient.INSTANCE.getInstance();
        Request request = new Request.Builder()
                .url(LATEST_RELEASE_URL)
                .header("User-Agent", USER_AGENT)
                .build();
        FileLog.d("GithubUpdateHelper: request " + LATEST_RELEASE_URL);
        try (Response response = client.newCall(request).execute()) {
            FileLog.d("GithubUpdateHelper: response code=" + response.code());
            if (!response.isSuccessful()) {
                throw new IOException("GitHub update check failed: HTTP " + response.code());
            }
            if (response.body() == null) {
                throw new IOException("GitHub update check failed: empty response");
            }
            String body = response.body().string();
            return new JSONObject(body);
        }
    }

    public static void downloadAndInstall(Activity activity, String url, DownloadDelegate delegate) {
        Utilities.globalQueue.postRunnable(() -> {
            File file = null;
            try {
                file = downloadApk(activity, url);
                File finalFile = file;
                AndroidUtilitiesBridge.runOnUIThread(() -> {
                    boolean installed = openApkInstall(activity, finalFile);
                    delegate.onDone(installed, installed ? null : "Unable to open installer");
                });
            } catch (Exception e) {
                FileLog.e(e);
                if (file != null && file.exists()) {
                    try {
                        file.delete();
                    } catch (Exception ignored) {
                    }
                }
                AndroidUtilitiesBridge.runOnUIThread(() -> delegate.onDone(false, e.getMessage()));
            }
        });
    }

    private static File downloadApk(Activity activity, String url) throws IOException {
        OkHttpClient client = HttpClient.INSTANCE.getInstance();
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", USER_AGENT)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to download update: HTTP " + response.code());
            }
            File dir = new File(activity.getCacheDir(), "apks");
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Unable to create update cache directory");
            }
            File file = File.createTempFile("negramxfe-update", ".apk", dir);
            try (InputStream input = response.body().byteStream(); FileOutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[32 * 1024];
                int read;
                while ((read = input.read(buffer)) >= 0) {
                    output.write(buffer, 0, read);
                }
            }
            return file;
        }
    }

    private static boolean openApkInstall(Activity activity, File file) {
        try {
            if (activity == null || file == null || !file.exists()) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.setDataAndType(FileProvider.getUriForFile(activity, ApplicationLoader.getApplicationId() + ".provider", file), "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            activity.startActivityForResult(intent, 500);
            return true;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    private static JSONObject findBestApkAsset(JSONArray assets) {
        if (assets == null) {
            return null;
        }
        JSONObject fallback = null;
        for (int i = 0; i < assets.length(); i++) {
            JSONObject asset = assets.optJSONObject(i);
            if (asset == null) {
                continue;
            }
            String name = asset.optString("name", "").toLowerCase(Locale.ROOT);
            if (!name.endsWith(".apk")) {
                continue;
            }
            if (fallback == null || name.contains("universal")) {
                fallback = asset;
            }
            for (String abi : Build.SUPPORTED_ABIS) {
                if (name.contains(abi.toLowerCase(Locale.ROOT))) {
                    return asset;
                }
            }
        }
        return fallback;
    }

    private static String getCurrentVersion() {
        String version = BuildConfig.VERSION_NAME;
        int suffix = version.indexOf('-');
        return suffix >= 0 ? version.substring(0, suffix) : version;
    }

    private static boolean isNewerVersion(String currentVersion, String latestVersion) {
        int[] currentParts = versionParts(currentVersion);
        int[] latestParts = versionParts(latestVersion);
        if (currentParts != null && latestParts != null) {
            int size = Math.max(currentParts.length, latestParts.length);
            for (int i = 0; i < size; i++) {
                int latest = i < latestParts.length ? latestParts[i] : 0;
                int current = i < currentParts.length ? currentParts[i] : 0;
                if (latest != current) {
                    return latest > current;
                }
            }
        }

        Integer currentBuild = buildNumber(currentVersion);
        Integer latestBuild = buildNumber(latestVersion);
        return currentBuild != null && latestBuild != null && latestBuild > currentBuild;
    }

    private static Integer buildNumber(String version) {
        String clean = cleanVersion(version);
        String[] parts = clean.split("\\.");
        if (parts.length >= 3) {
            try {
                return Integer.parseInt(parts[2]);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private static int[] versionParts(String version) {
        String clean = cleanVersion(version);
        if (clean.isEmpty()) {
            return null;
        }
        String[] parts = clean.split("\\.");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                result[i] = Integer.parseInt(parts[i]);
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }

    private static String cleanVersion(String version) {
        if (version == null) {
            return "";
        }
        String clean = version.trim();
        if (clean.startsWith("v") || clean.startsWith("V")) {
            clean = clean.substring(1);
        }
        int suffix = clean.indexOf('_');
        if (suffix >= 0) {
            clean = clean.substring(0, suffix);
        }
        suffix = clean.indexOf('-');
        if (suffix >= 0) {
            clean = clean.substring(0, suffix);
        }
        return clean;
    }

    private static class AndroidUtilitiesBridge {
        static void runOnUIThread(Runnable runnable) {
            org.telegram.messenger.AndroidUtilities.runOnUIThread(runnable);
        }
    }
}

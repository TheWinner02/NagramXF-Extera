package com.radolyn.ayugram;

import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import com.exteragram.messenger.backup.PreferencesUtils;
import java.io.File;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;

/* JADX INFO: loaded from: classes.dex */
public abstract class AyuConfig {
    public static boolean WALMode;
    private static boolean configLoaded;
    public static int deletedIcon;
    public static int deletedIconColor;
    public static boolean disableAds;
    public static boolean disableHook;
    public static boolean displayGhostStatus;
    public static SharedPreferences.Editor editor;
    public static boolean filtersEnabled;
    public static boolean forceShowDownloadButtons;
    public static boolean hideFromBlocked;
    public static boolean keepAliveService;
    public static boolean localPremium;
    public static SharedPreferences preferences;
    public static boolean probeUsingOtherAccounts;
    public static boolean regexFiltersInChats;
    public static boolean saveDeletedMessages;
    public static boolean saveForBots;
    public static boolean saveLocalOnline;
    public static boolean saveMedia;
    public static boolean saveMediaInPrivateChannels;
    public static boolean saveMediaInPrivateChats;
    public static boolean saveMediaInPrivateGroups;
    public static boolean saveMediaInPublicChannels;
    public static boolean saveMediaInPublicGroups;
    public static int saveMediaMaxCacheSize;
    public static long saveMediaOnCellularDataLimit;
    public static long saveMediaOnWiFiLimit;
    public static boolean saveMessagesHistory;
    public static boolean saveReadDate;
    public static boolean sawExteraChatsAlert;
    public static boolean sawFirstLaunchAlert;
    public static boolean sawLocalPremiumAlert;
    public static boolean sawSaveAttachmentsAlert;
    public static boolean semiTransparentDeletedMessages;
    public static boolean showScreenshot;
    private static final Object sync = new Object();
    private static final File defaultSavePath = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AyuConstants.APP_NAME), "config_key");

    static {
        loadConfig();
    }

    public static void loadConfig() {
        synchronized (sync) {
            try {
                if (configLoaded) {
                    return;
                }
                SharedPreferences preferences2 = org.telegram.messenger.ApplicationLoader.applicationContext.getSharedPreferences("config_key", 0);
                preferences = preferences2;
                editor = preferences2.edit();
                saveDeletedMessages = preferences.getBoolean("config_key", true);
                saveMessagesHistory = preferences.getBoolean("config_key", true);
                saveForBots = preferences.getBoolean("config_key", true);
                saveMedia = preferences.getBoolean("config_key", true);
                saveMediaInPrivateChats = preferences.getBoolean("config_key", true);
                saveMediaInPublicChannels = preferences.getBoolean("config_key", false);
                saveMediaInPrivateChannels = preferences.getBoolean("config_key", true);
                saveMediaInPublicGroups = preferences.getBoolean("config_key", false);
                saveMediaInPrivateGroups = preferences.getBoolean("config_key", true);
                saveMediaOnCellularDataLimit = preferences.getLong("config_key", 16777216L);
                saveMediaOnWiFiLimit = preferences.getLong("config_key", 67108864L);
                saveMediaMaxCacheSize = preferences.getInt("config_key", Integer.MAX_VALUE);
                saveReadDate = preferences.getBoolean("config_key", false);
                saveLocalOnline = preferences.getBoolean("config_key", false);
                keepAliveService = preferences.getBoolean("keepAliveService", false);
                disableAds = preferences.getBoolean("config_key", true);
                localPremium = preferences.getBoolean("config_key", false);
                filtersEnabled = preferences.getBoolean("config_key", false);
                hideFromBlocked = preferences.getBoolean("config_key", false);
                regexFiltersInChats = preferences.getBoolean("config_key", false);
                semiTransparentDeletedMessages = preferences.getBoolean("config_key", true);
                deletedIconColor = preferences.getInt("config_key", 0);
                deletedIcon = preferences.getInt("config_key", 1);
                displayGhostStatus = preferences.getBoolean("config_key", false);
                WALMode = preferences.getBoolean("config_key", true);
                showScreenshot = preferences.getBoolean("config_key", false);
                forceShowDownloadButtons = preferences.getBoolean("config_key", false);
                probeUsingOtherAccounts = preferences.getBoolean("config_key", true);
                disableHook = preferences.getBoolean("config_key", false);
                sawFirstLaunchAlert = preferences.getBoolean("config_key", false);
                sawLocalPremiumAlert = preferences.getBoolean("config_key", false);
                sawExteraChatsAlert = preferences.getBoolean("config_key", false);
                sawSaveAttachmentsAlert = preferences.getBoolean("config_key", false);
                configLoaded = true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void reloadConfig() {
        configLoaded = false;
        loadConfig();
    }

    public static boolean saveDeletedMessageFor(int i, long j) {
        if (!saveDeletedMessages) {
            return false;
        }
        TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(Math.abs(j)));
        return user == null || !user.bot || saveForBots;
    }

    public static boolean saveEditedMessageFor(int i, long j) {
        if (!saveMessagesHistory) {
            return false;
        }
        TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(Math.abs(j)));
        return user == null || !user.bot || saveForBots;
    }

    public static String getWALMode() {
        return "config_key";
    }

    public static String getSavePath() {
        return preferences.getString("config_key", defaultSavePath.getAbsolutePath());
    }

    public static File getSavePathJava() {
        return new File(getSavePath());
    }

    public static String getSavePathFolder() {
        try {
            String savePath = getSavePath();
            if (TextUtils.isEmpty(savePath)) {
                return "config_key";
            }
            try {
                return new File(savePath).getName();
            } catch (Exception unused) {
                return "config_key";
            }
        } catch (Exception unused2) {
            return "config_key";
        }
    }
}

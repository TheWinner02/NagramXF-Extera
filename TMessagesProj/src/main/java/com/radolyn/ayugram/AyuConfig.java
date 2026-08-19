package com.radolyn.ayugram;

import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import java.io.File;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;
import xyz.nextalone.nagram.NaConfig;

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
    private static final File defaultSavePath = new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), AyuConstants.APP_NAME), "Media");

    static {
        loadConfig();
    }

    public static void loadConfig() {
        synchronized (sync) {
            try {
                if (configLoaded) {
                    return;
                }
                SharedPreferences preferences2 = ApplicationLoader.applicationContext.getSharedPreferences("ayuconfig", 0);
                preferences = preferences2;
                editor = preferences2.edit();
                saveDeletedMessages = preferences.getBoolean("saveDeletedMessages", true);
                saveMessagesHistory = preferences.getBoolean("saveMessagesHistory", true);
                saveForBots = preferences.getBoolean("saveForBots", true);
                saveMedia = preferences.getBoolean("saveMedia", true);
                saveMediaInPrivateChats = preferences.getBoolean("saveMediaInPrivateChats", true);
                saveMediaInPublicChannels = preferences.getBoolean("saveMediaInPublicChannels", false);
                saveMediaInPrivateChannels = preferences.getBoolean("saveMediaInPrivateChannels", true);
                saveMediaInPublicGroups = preferences.getBoolean("saveMediaInPublicGroups", false);
                saveMediaInPrivateGroups = preferences.getBoolean("saveMediaInPrivateGroups", true);
                saveMediaOnCellularDataLimit = preferences.getLong("saveMediaOnCellularDataLimit", 16777216L);
                saveMediaOnWiFiLimit = preferences.getLong("saveMediaOnWiFiLimit", 67108864L);
                saveMediaMaxCacheSize = preferences.getInt("saveMediaMaxCacheSize", Integer.MAX_VALUE);
                saveReadDate = preferences.getBoolean("saveReadDate", false);
                saveLocalOnline = preferences.getBoolean("saveLocalOnline", false);
                keepAliveService = preferences.getBoolean("keepAliveService", false);
                disableAds = preferences.getBoolean("disableAds", true);
                localPremium = preferences.getBoolean("localPremium", false);
                filtersEnabled = preferences.getBoolean("filtersEnabled", false);
                hideFromBlocked = preferences.getBoolean("hideFromBlocked", false);
                regexFiltersInChats = preferences.getBoolean("regexFiltersInChats", false);
                semiTransparentDeletedMessages = preferences.getBoolean("semiTransparentDeletedMessages", true);
                deletedIconColor = preferences.getInt("deletedIconColor", 0);
                deletedIcon = preferences.getInt("deletedIcon", 1);
                displayGhostStatus = preferences.getBoolean("displayGhostStatus", false);
                WALMode = preferences.getBoolean("WALMode", true);
                showScreenshot = preferences.getBoolean("showScreenshot", false);
                forceShowDownloadButtons = preferences.getBoolean("forceShowDownloadButtons", false);
                probeUsingOtherAccounts = preferences.getBoolean("probeUsingOtherAccounts", true);
                disableHook = preferences.getBoolean("disableHook", false);
                sawFirstLaunchAlert = preferences.getBoolean("sawFirstLaunchAlert", false);
                sawLocalPremiumAlert = preferences.getBoolean("sawLocalPremiumAlert", false);
                sawExteraChatsAlert = preferences.getBoolean("sawExteraChatsAlert", false);
                sawSaveAttachmentsAlert = preferences.getBoolean("sawSaveAttachmentsAlert", false);
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
        if (!saveDeletedMessages && !NaConfig.INSTANCE.getEnableSaveDeletedMessages().Bool()) {
            return false;
        }
        TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(Math.abs(j)));
        return user == null || !user.bot || saveForBots;
    }

    public static boolean saveEditedMessageFor(int i, long j) {
        if (!saveMessagesHistory && !NaConfig.INSTANCE.getEnableSaveEditsHistory().Bool()) {
            return false;
        }
        TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(Math.abs(j)));
        return user == null || !user.bot || saveForBots;
    }

    public static boolean isSaveLocalOnline() {
        return saveLocalOnline || NaConfig.INSTANCE.getSaveLocalLastSeen().Bool();
    }

    public static String getWALMode() {
        return WALMode ? "WAL" : "TRUNCATE";
    }

    public static String getSavePath() {
        return preferences.getString("savePath", defaultSavePath.getAbsolutePath());
    }

    public static File getSavePathJava() {
        return new File(getSavePath());
    }

    public static String getSavePathFolder() {
        try {
            String savePath = getSavePath();
            if (TextUtils.isEmpty(savePath)) {
                return AyuConstants.APP_NAME;
            }
            try {
                return new File(savePath).getName();
            } catch (Exception unused) {
                return AyuConstants.APP_NAME;
            }
        } catch (Exception unused2) {
            return AyuConstants.APP_NAME;
        }
    }
}

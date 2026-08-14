package com.radolyn.ayugram;

import android.content.SharedPreferences;
import com.exteragram.messenger.backup.PreferencesUtils;
import java.util.HashMap;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: loaded from: classes.dex */
public abstract class AyuGhostConfig {
    private static boolean configLoaded;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences preferences;
    private static HashMap settings;
    private static final Object sync = new Object();
    private static boolean useGlobalConfig;

    static {
        loadConfig();
    }

    public static void loadConfig() {
        synchronized (sync) {
            try {
                if (configLoaded) {
                    return;
                }
                SharedPreferences preferences2 = org.telegram.messenger.ApplicationLoader.applicationContext.getSharedPreferences("ayughostconfig", 0);
                preferences = preferences2;
                editor = preferences2.edit();
                settings = new HashMap();
                for (int i = 0; i < 16; i++) {
                    if (UserConfig.isValidAccount(i)) {
                        settings.put(Long.valueOf(UserConfig.getInstance(i).getClientUserId()), new GhostModeSettings(UserConfig.getInstance(i).getClientUserId()));
                    }
                }
                settings.put(-1L, new GhostModeSettings(-1L));
                useGlobalConfig = preferences.getBoolean("useGlobalConfig", true);
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

    public static class GhostModeSettings {
        public boolean markReadAfterAction;
        public boolean sendOfflinePacketAfterOnline;
        public boolean sendOfflinePacketAfterOnlineLocked;
        public boolean sendOnlinePackets;
        public boolean sendOnlinePacketsLocked;
        public boolean sendReadMessagePackets;
        public boolean sendReadMessagePacketsLocked;
        public boolean sendReadStoryPackets;
        public boolean sendReadStoryPacketsLocked;
        public boolean sendUploadProgress;
        public boolean sendUploadProgressLocked;
        public int sendWithoutSound;
        public boolean suggestGhostModeBeforeViewingStory;
        public boolean useScheduledMessages;
        public long userId;

        public GhostModeSettings(long j) {
            String str;
            if (j == -1) {
                str = _UrlKt.FRAGMENT_ENCODE_SET;
            } else {
                str = "_" + j;
            }
            this.userId = j;
            this.sendReadMessagePackets = AyuGhostConfig.preferences.getBoolean("sendReadMessagePackets" + str, true);
            this.sendReadMessagePacketsLocked = AyuGhostConfig.preferences.getBoolean("sendReadMessagePacketsLocked" + str, false);
            this.sendReadStoryPackets = AyuGhostConfig.preferences.getBoolean("sendReadStoryPackets" + str, true);
            this.sendReadStoryPacketsLocked = AyuGhostConfig.preferences.getBoolean("sendReadStoryPacketsLocked" + str, false);
            this.sendOnlinePackets = AyuGhostConfig.preferences.getBoolean("sendOnlinePackets" + str, true);
            this.sendOnlinePacketsLocked = AyuGhostConfig.preferences.getBoolean("sendOnlinePacketsLocked" + str, false);
            this.sendUploadProgress = AyuGhostConfig.preferences.getBoolean("sendUploadProgress" + str, true);
            this.sendUploadProgressLocked = AyuGhostConfig.preferences.getBoolean("sendUploadProgressLocked" + str, false);
            this.sendOfflinePacketAfterOnline = AyuGhostConfig.preferences.getBoolean("sendOfflinePacketAfterOnline" + str, false);
            this.sendOfflinePacketAfterOnlineLocked = AyuGhostConfig.preferences.getBoolean("sendOfflinePacketAfterOnlineLocked" + str, false);
            this.markReadAfterAction = AyuGhostConfig.preferences.getBoolean("markReadAfterAction" + str, true);
            this.useScheduledMessages = AyuGhostConfig.preferences.getBoolean("useScheduledMessages" + str, false);
            this.sendWithoutSound = AyuGhostConfig.preferences.getInt("sendWithoutSound2" + str, 0);
            this.suggestGhostModeBeforeViewingStory = AyuGhostConfig.preferences.getBoolean("suggestGhostModeBeforeViewingStory" + str, true);
        }

        public void save() {
            String str;
            if (this.userId == -1) {
                str = _UrlKt.FRAGMENT_ENCODE_SET;
            } else {
                str = "_" + this.userId;
            }
            AyuGhostConfig.editor.putBoolean("sendReadMessagePackets" + str, this.sendReadMessagePackets).apply();
            AyuGhostConfig.editor.putBoolean("sendReadMessagePacketsLocked" + str, this.sendReadMessagePacketsLocked).apply();
            AyuGhostConfig.editor.putBoolean("sendReadStoryPackets" + str, this.sendReadStoryPackets).apply();
            AyuGhostConfig.editor.putBoolean("sendReadStoryPacketsLocked" + str, this.sendReadStoryPacketsLocked).apply();
            AyuGhostConfig.editor.putBoolean("sendOnlinePackets" + str, this.sendOnlinePackets).apply();
            AyuGhostConfig.editor.putBoolean("sendOnlinePacketsLocked" + str, this.sendOnlinePacketsLocked).apply();
            AyuGhostConfig.editor.putBoolean("sendUploadProgress" + str, this.sendUploadProgress).apply();
            AyuGhostConfig.editor.putBoolean("sendUploadProgressLocked" + str, this.sendUploadProgressLocked).apply();
            AyuGhostConfig.editor.putBoolean("sendOfflinePacketAfterOnline" + str, this.sendOfflinePacketAfterOnline).apply();
            AyuGhostConfig.editor.putBoolean("sendOfflinePacketAfterOnlineLocked" + str, this.sendOfflinePacketAfterOnlineLocked).apply();
            AyuGhostConfig.editor.putBoolean("markReadAfterAction" + str, this.markReadAfterAction).apply();
            AyuGhostConfig.editor.putBoolean("useScheduledMessages" + str, this.useScheduledMessages).apply();
            AyuGhostConfig.editor.putInt("sendWithoutSound2" + str, this.sendWithoutSound).apply();
            AyuGhostConfig.editor.putBoolean("suggestGhostModeBeforeViewingStory" + str, this.suggestGhostModeBeforeViewingStory).apply();
        }
    }

    public static void setGlobalOverride(boolean z) {
        setGlobalOverride(z, BulletinFactory.global());
    }

    public static void setGlobalOverride(boolean z, BulletinFactory bulletinFactory) {
        boolean z2 = useGlobalConfig;
        SharedPreferences.Editor editor2 = editor;
        useGlobalConfig = z;
        editor2.putBoolean("useGlobalConfig", z).apply();
        if (z2 != z) {
            if (z) {
                bulletinFactory.createSimpleBulletin(R.raw.info, LocaleController.getString(R.string.GhostMode)).show();
            } else {
                bulletinFactory.createSimpleBulletin(R.raw.info, LocaleController.getString(R.string.GhostMode)).show();
            }
        }
    }

    public static boolean isGlobalOverride() {
        return useGlobalConfig;
    }

    public static GhostModeSettings getGhostModeSettings(long j) {
        if (isGlobalOverride()) {
            j = -1;
        }
        if (settings.containsKey(Long.valueOf(j))) {
            return (GhostModeSettings) settings.get(Long.valueOf(j));
        }
        settings.put(Long.valueOf(j), new GhostModeSettings(j));
        return (GhostModeSettings) settings.get(Long.valueOf(j));
    }

    public static boolean isGhostModeActive(long j) {
        GhostModeSettings ghostModeSettings = getGhostModeSettings(j);
        if (ghostModeSettings.sendReadMessagePackets && !ghostModeSettings.sendReadMessagePacketsLocked) {
            return false;
        }
        if (ghostModeSettings.sendReadStoryPackets && !ghostModeSettings.sendReadStoryPacketsLocked) {
            return false;
        }
        if (ghostModeSettings.sendOnlinePackets && !ghostModeSettings.sendOnlinePacketsLocked) {
            return false;
        }
        if (!ghostModeSettings.sendUploadProgress || ghostModeSettings.sendUploadProgressLocked) {
            return ghostModeSettings.sendOfflinePacketAfterOnline || ghostModeSettings.sendOfflinePacketAfterOnlineLocked;
        }
        return false;
    }

    public static void setGhostMode(final long j, boolean z, final BulletinFactory bulletinFactory) {
        GhostModeSettings ghostModeSettings = getGhostModeSettings(j);
        if (!ghostModeSettings.sendReadMessagePacketsLocked) {
            ghostModeSettings.sendReadMessagePackets = !z;
        }
        if (!ghostModeSettings.sendReadStoryPacketsLocked) {
            ghostModeSettings.sendReadStoryPackets = !z;
        }
        if (!ghostModeSettings.sendOnlinePacketsLocked) {
            ghostModeSettings.sendOnlinePackets = !z;
        }
        if (!ghostModeSettings.sendUploadProgressLocked) {
            ghostModeSettings.sendUploadProgress = !z;
        }
        if (!ghostModeSettings.sendOfflinePacketAfterOnlineLocked) {
            ghostModeSettings.sendOfflinePacketAfterOnline = z;
        }
        ghostModeSettings.save();
        AyuState.resetGhostMode();
        if (z && ghostModeSettings.sendOfflinePacketAfterOnline) {
            AyuWorker.setOnline(UserConfig.selectedAccount, true);
        } else if (!z && ghostModeSettings.sendOnlinePackets) {
            
            if (ghostModeSettings.sendOfflinePacketAfterOnline) {
                AyuWorker.setOnline(UserConfig.selectedAccount, true);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.radolyn.ayugram.AyuGhostConfig$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AyuGhostConfig.$r8$lambda$Kr4SHz1TRRbhgUv0kxKKSPNwJdU(bulletinFactory, j);
            }
        });
    }

    public static /* synthetic */ void $r8$lambda$Kr4SHz1TRRbhgUv0kxKKSPNwJdU(BulletinFactory bulletinFactory, long j) {
        NotificationCenter.getInstance(UserConfig.selectedAccount).postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
        NotificationCenter.getInstance(UserConfig.selectedAccount).postNotificationName(NotificationCenter.currentUserPremiumStatusChanged, new Object[0]);
        if (bulletinFactory == null || !LaunchActivity.isActive || LaunchActivity.getSafeLastFragment() == null) {
            return;
        }
        try {
            if (isGhostModeActive(j)) {
                bulletinFactory.createSuccessBulletin(LocaleController.getString(R.string.GhostModeEnabled)).show();
            } else {
                bulletinFactory.createErrorBulletin(LocaleController.getString(R.string.GhostModeDisabled)).show();
            }
        } catch (Throwable unused) {
        }
    }

    public static void toggleGhostMode(long j, BulletinFactory bulletinFactory) {
        setGhostMode(j, !isGhostModeActive(j), bulletinFactory);
    }

    public static boolean isUseScheduledMessages(long j) {
        return getGhostModeSettings(j).useScheduledMessages && isGhostModeActive(j);
    }

    public static boolean isSendReadMessagePackets(long j) {
        return getGhostModeSettings(j).sendReadMessagePackets;
    }

    public static boolean isSendReadStoryPackets(long j) {
        return getGhostModeSettings(j).sendReadStoryPackets;
    }

    public static boolean isSendOnlinePackets(long j) {
        return getGhostModeSettings(j).sendOnlinePackets;
    }

    public static boolean isSendUploadProgress(long j) {
        return getGhostModeSettings(j).sendUploadProgress;
    }

    public static boolean isSendOfflinePacketAfterOnline(long j) {
        return getGhostModeSettings(j).sendOfflinePacketAfterOnline;
    }

    public static boolean isMarkReadAfterAction(long j) {
        return getGhostModeSettings(j).markReadAfterAction;
    }

    public static boolean isSendWithoutSound(long j) {
        int i = getGhostModeSettings(j).sendWithoutSound;
        return i == 2 || (i == 1 && isGhostModeActive(j));
    }

    public static boolean isSuggestGhostModeBeforeViewingStory(long j) {
        return getGhostModeSettings(j).suggestGhostModeBeforeViewingStory;
    }
}

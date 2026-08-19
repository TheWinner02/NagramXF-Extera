package com.radolyn.ayugram.controllers;

import com.radolyn.ayugram.AyuGhostConfig;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.Components.BulletinFactory;
import tw.nekomimi.nekogram.NekoConfig;
import xyz.nextalone.nagram.NaConfig;

public class AyuGhostController extends BaseController {
    private static final AyuGhostController[] Instance = new AyuGhostController[16];

    public AyuGhostController(int account) {
        super(account);
    }

    public static AyuGhostController getInstance() {
        return getInstance(0);
    }

    public static AyuGhostController getInstance(int account) {
        AyuGhostController ayuGhostController;
        AyuGhostController[] ayuGhostControllerArr = Instance;
        AyuGhostController ayuGhostController2 = ayuGhostControllerArr[account];
        if (ayuGhostController2 != null) {
            return ayuGhostController2;
        }
        synchronized (AyuGhostController.class) {
            try {
                ayuGhostController = ayuGhostControllerArr[account];
                if (ayuGhostController == null) {
                    ayuGhostController = new AyuGhostController(account);
                    ayuGhostControllerArr[account] = ayuGhostController;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return ayuGhostController;
    }

    public boolean isGhostModeActive() {
        return NekoConfig.isGhostModeActive() || AyuGhostConfig.isGhostModeActive(getUserConfig().getClientUserId());
    }

    public void setGhostMode(boolean active, BulletinFactory bulletinFactory) {
        AyuGhostConfig.setGhostMode(getUserConfig().getClientUserId(), active, bulletinFactory);
    }

    public void toggleGhostMode(BulletinFactory bulletinFactory) {
        AyuGhostConfig.toggleGhostMode(getUserConfig().getClientUserId(), bulletinFactory);
    }

    public boolean isUseScheduledMessages() {
        return NekoConfig.useScheduledMessages.Bool() || AyuGhostConfig.isUseScheduledMessages(getUserConfig().getClientUserId());
    }

    public boolean isSendWithoutSound() {
        return NaConfig.INSTANCE.getSilentMessageByDefault().Bool() || AyuGhostConfig.isSendWithoutSound(getUserConfig().getClientUserId());
    }

    public boolean isSendReadMessagePackets() {
        return NekoConfig.sendReadMessagePackets.Bool() && AyuGhostConfig.isSendReadMessagePackets(getUserConfig().getClientUserId());
    }

    public boolean isSendReadStoryPackets() {
        return NekoConfig.sendReadStoriesPackets.Bool() && AyuGhostConfig.isSendReadStoryPackets(getUserConfig().getClientUserId());
    }

    public boolean isSendOnlinePackets() {
        return NekoConfig.sendOnlinePackets.Bool() && AyuGhostConfig.isSendOnlinePackets(getUserConfig().getClientUserId());
    }

    public boolean isSendUploadProgress() {
        return NekoConfig.sendUploadProgress.Bool() && AyuGhostConfig.isSendUploadProgress(getUserConfig().getClientUserId());
    }

    public boolean isSendOfflinePacketAfterOnline() {
        return NekoConfig.sendOfflinePacketAfterOnline.Bool() || AyuGhostConfig.isSendOfflinePacketAfterOnline(getUserConfig().getClientUserId());
    }

    public boolean isMarkReadAfterAction() {
        return NekoConfig.markReadAfterSend.Bool() || AyuGhostConfig.isMarkReadAfterAction(getUserConfig().getClientUserId());
    }

    public String getSendWithoutSoundString() {
        return LocaleController.getString(isSendWithoutSound() ? R.string.SendWithSound : R.string.SendWithoutSound);
    }

    public int getSendWithoutSoundIcon() {
        return isSendWithoutSound() ? R.drawable.input_notify_on : R.drawable.input_notify_off;
    }

    public boolean shouldSuppressOnlinePackets() {
        return !isSendOnlinePackets() || isSendOfflinePacketAfterOnline() || isGhostModeActive();
    }
}

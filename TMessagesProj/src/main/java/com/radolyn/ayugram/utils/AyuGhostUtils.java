package com.radolyn.ayugram.utils;

import com.radolyn.ayugram.AyuGhostConfig;
import com.radolyn.ayugram.AyuState;
import com.radolyn.ayugram.controllers.AyuGhostController;
import com.radolyn.ayugram.utils.network.AyuRequestUtils;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;

public class AyuGhostUtils {

    public static boolean isGhostModeActive(long userId) {
        return AyuGhostConfig.isGhostModeActive(userId);
    }

    public static long getDialogId(TLRPC.InputPeer peer) {
        return DialogObject.getPeerDialogId(peer);
    }

    public static void markReadOnServer(int currentAccount, TLRPC.InputPeer peer, boolean readLocal) {
    }

    public static void markReadOnServer(Object obj, boolean force) {
    }

    public static void performStatusRequest(boolean sendOnlineNow) {
        int account = UserConfig.selectedAccount;
        if (sendOnlineNow) {
            AyuRequestUtils.sendOnline(account);
        } else {
            AyuRequestUtils.sendOffline(account);
        }
    }

    private static double getEstimatedUploadSpeedBytesPerSec(int account) {
        boolean isPremium = UserConfig.getInstance(account).isPremium();
        int networkType = org.telegram.messenger.ApplicationLoader.getCurrentNetworkType();
        boolean isSlow = org.telegram.messenger.ApplicationLoader.isConnectionSlow();

        if (isSlow || networkType == org.telegram.messenger.StatsController.TYPE_ROAMING) {
            // Slow connection / 2G / 3G / Roaming
            return isPremium ? (600.0 * 1024) : (350.0 * 1024);
        } else if (networkType == org.telegram.messenger.StatsController.TYPE_WIFI) {
            // Wi-Fi: Telegram Free server upload cap is ~1.5 MB/s, Premium is ~8 MB/s
            return isPremium ? (8.0 * 1024 * 1024) : (1.5 * 1024 * 1024);
        } else {
            // Mobile 4G/5G: Telegram Free ~1.0 MB/s, Premium ~4 MB/s
            return isPremium ? (4.0 * 1024 * 1024) : (1.0 * 1024 * 1024);
        }
    }

    public static int calculateAutoScheduleDelay(int account, org.telegram.messenger.SendMessagesHelper.SendMessageParams params) {
        int baseDelay = 12; // Minimum MTProto safety buffer
        int additionalDelay = 0;

        if (params == null) {
            return baseDelay;
        }

        double uploadSpeed = getEstimatedUploadSpeedBytesPerSec(account);
        boolean isSlow = org.telegram.messenger.ApplicationLoader.isConnectionSlow();

        // 1. Text / Caption length (simulating typing speed ~25-30 chars/s)
        String text = params.message;
        if (text == null || text.isEmpty()) {
            text = params.caption;
        }
        if (text != null && !text.isEmpty()) {
            int typingSpeed = isSlow ? 20 : 30;
            additionalDelay += Math.min(40, text.length() / typingSpeed);
        }

        // 2. Documents / Audio / Voice / Video
        if (params.document != null) {
            long size = params.document.size;
            int duration = 0;
            boolean isVoiceOrRound = false;

            if (params.document.attributes != null) {
                for (int i = 0; i < params.document.attributes.size(); i++) {
                    TLRPC.DocumentAttribute attr = params.document.attributes.get(i);
                    if (attr instanceof TLRPC.TL_documentAttributeAudio) {
                        duration = (int) attr.duration;
                        if (attr.voice) {
                            isVoiceOrRound = true;
                        }
                    } else if (attr instanceof TLRPC.TL_documentAttributeVideo) {
                        duration = (int) attr.duration;
                        if (attr.round_message) {
                            isVoiceOrRound = true;
                        }
                    }
                }
            }

            if (isVoiceOrRound && duration > 0) {
                // Voice or Video note recording time + upload overhead
                int uploadTime = (int) Math.ceil(size > 0 ? (size / uploadSpeed) : 2);
                additionalDelay += Math.min(90, duration + uploadTime);
            } else if (size > 0) {
                // Upload simulation based on network quality and Telegram Free bandwidth limit
                int uploadTime = (int) Math.ceil(size / uploadSpeed);
                additionalDelay += Math.min(120, uploadTime);
            }
        } else if (params.videoEditedInfo != null) {
            long size = params.videoEditedInfo.estimatedSize;
            int uploadTime = size > 0 ? (int) Math.ceil(size / uploadSpeed) : 5;
            if (params.videoEditedInfo.estimatedDuration > 0) {
                additionalDelay += Math.min(30, (int) (params.videoEditedInfo.estimatedDuration / 1000) / 2);
            }
            additionalDelay += Math.min(120, uploadTime);
        } else if (params.photo != null) {
            // Photo upload time estimation with network bandwidth (~800 KB)
            int photoUploadTime = (int) Math.max(3, Math.ceil((800.0 * 1024) / uploadSpeed));
            additionalDelay += Math.min(25, photoUploadTime);
        } else if (params.path != null) {
            try {
                java.io.File f = new java.io.File(params.path);
                if (f.exists()) {
                    long size = f.length();
                    if (size > 0) {
                        int uploadTime = (int) Math.ceil(size / uploadSpeed);
                        additionalDelay += Math.min(120, uploadTime);
                    }
                }
            } catch (Throwable ignored) {
            }
        }

        if (isSlow) {
            additionalDelay += 3; // Extra network latency overhead on poor connections
        }

        return baseDelay + additionalDelay;
    }

    public static class InterceptResult {
        private final boolean block;
        private final RequestDelegate onComplete;

        public InterceptResult(boolean block, RequestDelegate onComplete) {
            this.block = block;
            this.onComplete = onComplete;
        }

        public boolean blockRequest() {
            return block;
        }

        public RequestDelegate effectiveOnComplete() {
            return onComplete;
        }
    }

    public static InterceptResult interceptRequest(TLObject object, RequestDelegate onCompleteOrig) {
        return interceptRequest(UserConfig.selectedAccount, object, onCompleteOrig);
    }

    public static InterceptResult interceptRequest(int account, TLObject object, RequestDelegate onCompleteOrig) {
        if (object == null) {
            return new InterceptResult(false, onCompleteOrig);
        }

        AyuGhostController controller = AyuGhostController.getInstance(account);

        // 1. Don't send upload / typing progress
        if (AyuRequestUtils.isSetTypingRequest(object)) {
            if (!controller.isSendUploadProgress()) {
                return new InterceptResult(true, null);
            }
        }

        // 2. Don't send online packets
        if (object instanceof TL_account.updateStatus) {
            TL_account.updateStatus updateStatus = (TL_account.updateStatus) object;
            if (!controller.isSendOnlinePackets() && !updateStatus.offline) {
                updateStatus.offline = true;
            }
        }

        // 3. Don't send read message packets
        if (AyuRequestUtils.isReadMessageRequest(object)) {
            if (!controller.isSendReadMessagePackets()) {
                return new InterceptResult(true, null);
            }
        }

        // 4. Don't send read story packets
        if (AyuRequestUtils.isReadStoryRequest(object)) {
            if (!controller.isSendReadStoryPackets()) {
                return new InterceptResult(true, null);
            }
        }

        // 5. Send offline packet right after online
        RequestDelegate effectiveDelegate = onCompleteOrig;
        if (controller.isSendOfflinePacketAfterOnline() && AyuRequestUtils.isOnlineRequest(account, object)) {
            effectiveDelegate = (response, error) -> {
                if (onCompleteOrig != null) {
                    onCompleteOrig.run(response, error);
                }
                if (error == null) {
                    AyuRequestUtils.sendOffline(account);
                }
            };
        }

        return new InterceptResult(false, effectiveDelegate);
    }
}

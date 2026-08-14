package com.radolyn.ayugram.utils;

import com.radolyn.ayugram.AyuGhostConfig;
import org.telegram.messenger.DialogObject;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

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
        return new InterceptResult(false, onCompleteOrig);
    }
}

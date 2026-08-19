package com.radolyn.ayugram;

import com.radolyn.ayugram.controllers.AyuGhostController;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;

public class AyuWorker {
    private static final long LAST_SEEN_FETCH_DELAY_MS = 100;
    private static ScheduledFuture<?> scheduledTask;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final ConcurrentHashMap<Integer, AtomicBoolean> needOffline = new ConcurrentHashMap<>();

    static {
        for (int i = 0; i < 16; i++) {
            needOffline.put(i, new AtomicBoolean(false));
        }
    }

    public static synchronized void run() {
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(false);
        }
        scheduledTask = scheduler.scheduleWithFixedDelay(AyuWorker::runOnce, 1500L, 3000L, TimeUnit.MILLISECONDS);
    }

    private static void runOnce() {
        for (int i = 0; i < 16; i++) {
            final int account = i;
            if (UserConfig.getInstance(account).isClientActivated() && shouldSendOffline(account)) {
                sendOffline(account, () -> notifyLastSeenPillFetch(account));
            }
        }
    }

    private static boolean shouldSendOffline(int account) {
        AtomicBoolean flag = needOffline.get(account);
        return AyuGhostController.getInstance(account).isSendOfflinePacketAfterOnline() && flag != null && flag.getAndSet(false);
    }

    private static void sendOffline(int account, Runnable callback) {
        TL_account.updateStatus req = new TL_account.updateStatus();
        req.offline = true;
        ConnectionsManager.getInstance(account).sendRequest(req, (response, error) -> {
            if (callback != null) {
                callback.run();
            }
        });
    }

    private static void notifyLastSeenPillUpdate(int account) {
        NotificationCenter.getGlobalInstance().postNotificationNameOnUIThread(AyuConstants.LAST_SEEN_PILL_UPDATE, account, Boolean.FALSE);
    }

    private static void notifyLastSeenPillFetch(int account) {
        AndroidUtilities.runOnUIThread(() -> {
            NotificationCenter.getGlobalInstance().postNotificationNameOnUIThread(AyuConstants.LAST_SEEN_PILL_FETCH, account);
        }, LAST_SEEN_FETCH_DELAY_MS);
    }

    public static void requestLastSeenUpdate(int account) {
        if (!AyuGhostController.getInstance(account).isSendOfflinePacketAfterOnline()) {
            notifyLastSeenPillFetch(account);
        } else if (shouldSendOffline(account)) {
            sendOffline(account, () -> notifyLastSeenPillFetch(account));
        } else {
            notifyLastSeenPillFetch(account);
        }
    }

    public static synchronized void setOnline(int account, boolean online) {
        AtomicBoolean flag = needOffline.get(account);
        if (flag != null) {
            flag.set(online);
        }
        if (AyuGhostController.getInstance(account).isSendOfflinePacketAfterOnline()) {
            run();
        }
        notifyLastSeenPillUpdate(account);
    }

    public static void shutdown() {
        scheduler.shutdown();
    }
}

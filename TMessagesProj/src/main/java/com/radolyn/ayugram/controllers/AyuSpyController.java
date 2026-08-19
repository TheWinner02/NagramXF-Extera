package com.radolyn.ayugram.controllers;

import android.util.LruCache;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.SpyLastSeen;
import com.radolyn.ayugram.database.entities.SpyMessageContentsRead;
import com.radolyn.ayugram.database.entities.SpyMessageRead;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;

public class AyuSpyController extends BaseController {
    private static final AyuSpyController[] Instance = new AyuSpyController[16];
    private final LruCache<Long, Integer> onlineCache;

    public AyuSpyController(int account) {
        super(account);
        this.onlineCache = new LruCache<>(500);
    }

    public static AyuSpyController getInstance() {
        return getInstance(0);
    }

    public static AyuSpyController getInstance(int account) {
        AyuSpyController controller = Instance[account];
        if (controller != null) {
            return controller;
        }
        synchronized (AyuSpyController.class) {
            controller = Instance[account];
            if (controller == null) {
                controller = new AyuSpyController(account);
                Instance[account] = controller;
            }
        }
        return controller;
    }

    public boolean saveOnlineActivity(long userId, int catchTime) {
        if (!AyuConfig.isSaveLocalOnline() || getUserConfig().getClientUserId() == userId || catchTime < 1397411401) {
            return false;
        }
        Integer lastSeen = getLastSeen(userId);
        if (lastSeen != null && lastSeen >= catchTime) {
            return false;
        }
        SpyLastSeen spyLastSeen = new SpyLastSeen();
        spyLastSeen.userId = userId;
        spyLastSeen.lastSeenDate = catchTime;
        AyuData.getSpyDao().insert(spyLastSeen);
        
        this.onlineCache.put(userId, catchTime);
        TLRPC.User user = getMessagesController().getUser(userId);
        return user != null && !user.bot && isBadStatus(user.status);
    }

    public Integer getLastSeenCached(long userId) {
        if (AyuConfig.isSaveLocalOnline()) {
            return this.onlineCache.get(userId);
        }
        return null;
    }

    public Integer getLastSeen(long userId) {
        if (!AyuConfig.isSaveLocalOnline()) {
            return null;
        }
        Integer num = this.onlineCache.get(userId);
        if (num != null && num >= 1397411401) {
            return num;
        }
        SpyLastSeen lastSeen = AyuData.getSpyDao().getLastSeen(userId);
        if (lastSeen == null || lastSeen.lastSeenDate < 1397411401) {
            return null;
        }
        this.onlineCache.put(userId, lastSeen.lastSeenDate);
        return lastSeen.lastSeenDate;
    }

    public void loadLastSeen(final long userId) {
        if (AyuConfig.isSaveLocalOnline()) {
            Integer num = this.onlineCache.get(userId);
            if (num == null || num <= 1397411401) {
                Utilities.globalQueue.postRunnable(() -> {
                    if (getLastSeen(userId) != null) {
                        updateInterfaces();
                    }
                });
            }
        }
    }

    public void updateInterfaces() {
        AndroidUtilities.runOnUIThread(() -> {
            getNotificationCenter().postNotificationName(NotificationCenter.updateInterfaces, MessagesController.UPDATE_MASK_STATUS);
        });
    }

    public static boolean isBadStatus(TLRPC.UserStatus userStatus) {
        if (userStatus == null) return true;
        if (userStatus instanceof TLRPC.TL_userStatusRecently || userStatus instanceof TLRPC.TL_userStatusLastWeek || userStatus instanceof TLRPC.TL_userStatusLastMonth) {
            return true;
        }
        int expires = userStatus.expires;
        return expires == -1 || expires == -100 || expires == -101 || expires == -102 || expires == -1000 || expires == -1001 || expires == -1002;
    }

    public void saveLastSeenFromLoadedMessages(long userId, long clientUserId, ArrayList<Object> messages, Object chatAdapter) {
    }

    public void saveLastSeenFromPeerReactions(ArrayList<TLRPC.MessagePeerReaction> reactions, long clientUserId) {
    }
}

package com.radolyn.ayugram.controllers;

import android.text.TextUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import com.radolyn.ayugram.utils.filters.AyuFilterUtils;
import com.radolyn.ayugram.utils.filters.HashablePattern;
import com.radolyn.ayugram.utils.filters.ReversiblePattern;
import java.util.ArrayList;
import java.util.HashSet;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

public class AyuFilterController extends BaseController {
    private static final AyuFilterController[] Instance = new AyuFilterController[16];
    private final AyuFilterCacheController cacheController = new AyuFilterCacheController();

    public AyuFilterController(int account) {
        super(account);
    }

    public static AyuFilterController getInstance() {
        return getInstance(0);
    }

    public static AyuFilterController getInstance(int account) {
        AyuFilterController controller = Instance[account];
        if (controller != null) {
            return controller;
        }
        synchronized (AyuFilterController.class) {
            controller = Instance[account];
            if (controller == null) {
                controller = new AyuFilterController(account);
                Instance[account] = controller;
            }
        }
        return controller;
    }

    public AyuFilterCacheController getAyuFilterCacheController() {
        return cacheController;
    }

    public static boolean isEnabled(TLRPC.Chat chat) {
        if (AyuConfig.filtersEnabled) {
            return AyuConfig.regexFiltersInChats || ChatObject.isChannel(chat);
        }
        return false;
    }

    public boolean isBlocked(long userId) {
        if (!AyuConfig.filtersEnabled) {
            return false;
        }
        if (getUserConfig().getClientUserId() == userId || !AyuFilterUtils.isShadowBanned(userId)) {
            return AyuConfig.hideFromBlocked && getMessagesController().blockePeers.indexOfKey(userId) >= 0;
        }
        return true;
    }

    public void invalidate(MessageObject messageObject) {
        if (messageObject == null) {
            return;
        }
        getAyuFilterCacheController().invalidate(messageObject);
    }

    public boolean isFilteredWithoutCaching(MessageObject messageObject) {
        if (!AyuConfig.filtersEnabled || messageObject == null || messageObject.isOut() || messageObject.isOutOwner()) {
            return false;
        }
        if (isFilterBlocked(messageObject)) {
            return true;
        }
        TLRPC.Chat chat = messageObject.getChatId() != 0 ? getMessagesController().getChat(messageObject.getChatId()) : null;
        if ((chat == null && !AyuConfig.regexFiltersInChats) || !isEnabled(chat)) {
            return false;
        }
        Boolean boolIsFiltered = getAyuFilterCacheController().isFiltered(messageObject, null);
        if (boolIsFiltered != null) {
            return boolIsFiltered;
        }
        return isFiltered(AyuMessageUtils.extractAllText(messageObject, null), messageObject.getDialogId());
    }

    public boolean isFiltered(TLRPC.Chat chat, MessageObject messageObject, MessageObject.GroupedMessages groupedMessages) {
        if (!AyuConfig.filtersEnabled) {
            return false;
        }
        if ((messageObject == null && (groupedMessages == null || (messageObject = groupedMessages.findPrimaryMessageObject()) == null)) || messageObject.isOut() || messageObject.isOutOwner()) {
            return false;
        }
        if (isFilterBlocked(messageObject)) {
            return true;
        }
        if (chat == null && messageObject.getChatId() != 0) {
            chat = getMessagesController().getChat(messageObject.getChatId());
        }
        if ((chat == null && !AyuConfig.regexFiltersInChats) || !isEnabled(chat)) {
            return false;
        }
        AyuFilterCacheController ayuFilterCacheController = getAyuFilterCacheController();
        Boolean boolIsFiltered = ayuFilterCacheController.isFiltered(messageObject, groupedMessages);
        if (boolIsFiltered != null) {
            return boolIsFiltered;
        }
        boolean zIsFiltered = isFiltered(AyuMessageUtils.extractAllText(messageObject, groupedMessages), messageObject.getDialogId());
        ayuFilterCacheController.putFiltered(messageObject, groupedMessages, zIsFiltered);
        return zIsFiltered;
    }

    private boolean isFilterBlocked(MessageObject messageObject) {
        if (messageObject == null || messageObject.messageOwner == null) return false;
        TLRPC.Peer peer;
        if (MessageObject.getPeerId(messageObject.messageOwner.from_id) != messageObject.getDialogId()) {
            if (isBlocked(MessageObject.getPeerId(messageObject.messageOwner.from_id))) {
                return true;
            }
            TLRPC.MessageFwdHeader messageFwdHeader = messageObject.messageOwner.fwd_from;
            if (messageFwdHeader != null && (peer = messageFwdHeader.from_id) != null && isBlocked(MessageObject.getPeerId(peer))) {
                return true;
            }
        }
        return false;
    }

    private boolean isFiltered(CharSequence charSequence, long dialogId) {
        if (TextUtils.isEmpty(charSequence)) {
            return false;
        }
        ArrayList<ReversiblePattern> patternsByDialogId = getAyuFilterCacheController().getPatternsByDialogId(dialogId);
        if (patternsByDialogId != null) {
            int size = patternsByDialogId.size();
            for (int i = 0; i < size; i++) {
                ReversiblePattern reversiblePattern = patternsByDialogId.get(i);
                boolean zFind = reversiblePattern.pattern().matcher(charSequence).find();
                boolean zReversed = reversiblePattern.reversed();
                if ((zReversed && !zFind) || (!zReversed && zFind)) {
                    return true;
                }
            }
        }
        HashSet<HashablePattern> exclusionsByDialogId = getAyuFilterCacheController().getExclusionsByDialogId(dialogId);
        ArrayList<HashablePattern> sharedPatterns = getAyuFilterCacheController().getSharedPatterns();
        if (sharedPatterns != null) {
            int size2 = sharedPatterns.size();
            for (int i2 = 0; i2 < size2; i2++) {
                HashablePattern hashablePattern = sharedPatterns.get(i2);
                if (exclusionsByDialogId == null || !exclusionsByDialogId.contains(hashablePattern)) {
                    boolean zFind2 = hashablePattern.getPattern().matcher(charSequence).find();
                    boolean zIsReversed = hashablePattern.isReversed();
                    if ((zIsReversed && !zFind2) || (!zIsReversed && zFind2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

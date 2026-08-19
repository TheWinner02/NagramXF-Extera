package com.radolyn.ayugram.utils.network;

import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.tgnet.tl.TL_forum;
import org.telegram.tgnet.tl.TL_phone;
import org.telegram.tgnet.tl.TL_stories;

public class AyuRequestUtils {

    public static boolean isSetTypingRequest(TLObject object) {
        return (object instanceof TLRPC.TL_messages_setTyping) || (object instanceof TLRPC.TL_messages_setEncryptedTyping);
    }

    public static boolean isSendMessageRequest(TLObject object) {
        return (object instanceof TLRPC.TL_messages_sendMessage)
                || (object instanceof TLRPC.TL_messages_sendMedia)
                || (object instanceof TLRPC.TL_messages_sendMultiMedia)
                || (object instanceof TLRPC.TL_messages_forwardMessages)
                || (object instanceof TLRPC.TL_messages_sendInlineBotResult)
                || (object instanceof TLRPC.TL_messages_sendEncrypted)
                || (object instanceof TLRPC.TL_messages_sendEncryptedFile)
                || (object instanceof TLRPC.TL_messages_sendEncryptedMultiMedia)
                || (object instanceof TLRPC.TL_messages_sendEncryptedService);
    }

    public static boolean isEditMessageRequest(TLObject object) {
        return object instanceof TLRPC.TL_messages_editMessage;
    }

    public static boolean isSendReactionRequest(TLObject object) {
        return object instanceof TLRPC.TL_messages_sendReaction;
    }

    public static boolean isSendPollRequest(TLObject object) {
        return object instanceof TLRPC.TL_messages_sendVote;
    }

    public static boolean isReadMessageRequest(TLObject object) {
        return (object instanceof TLRPC.TL_messages_readHistory)
                || (object instanceof TLRPC.TL_messages_readEncryptedHistory)
                || (object instanceof TLRPC.TL_messages_readDiscussion)
                || (object instanceof TLRPC.TL_messages_readMessageContents)
                || (object instanceof TLRPC.TL_messages_readSavedHistory)
                || (object instanceof TLRPC.TL_channels_readHistory)
                || (object instanceof TLRPC.TL_channels_readMessageContents)
                || (object instanceof TLRPC.TL_messages_markDialogUnread);
    }

    public static boolean isReadStoryRequest(TLObject object) {
        return (object instanceof TL_stories.TL_stories_readStories)
                || (object instanceof TL_stories.TL_stories_incrementStoryViews);
    }

    public static boolean isOnlineRequest(int account, TLObject object) {
        if (isSendMessageRequest(object) || isReadMessageRequest(object) || isEditMessageRequest(object)) {
            return true;
        }
        if (isSendReactionRequest(object) || isSendPollRequest(object)) {
            return true;
        }
        return (object instanceof TLRPC.TL_messages_createChat)
                || (object instanceof TLRPC.TL_channels_createChannel)
                || (object instanceof TL_forum.TL_messages_createForumTopic)
                || (object instanceof TLRPC.TL_channels_leaveChannel)
                || (object instanceof TL_forum.TL_messages_deleteTopicHistory)
                || (object instanceof TL_forum.TL_messages_editForumTopic)
                || (object instanceof TLRPC.TL_messages_updatePinnedMessage)
                || (object instanceof TL_phone.requestCall)
                || (object instanceof TL_phone.acceptCall)
                || (object instanceof TL_phone.confirmCall)
                || (object instanceof TL_stories.TL_stories_sendStory)
                || (object instanceof TL_stories.TL_stories_sendReaction)
                || (object instanceof TL_stories.TL_stories_readStories);
    }

    public static void sendOffline(int account) {
        TL_account.updateStatus req = new TL_account.updateStatus();
        req.offline = true;
        ConnectionsManager.getInstance(account).sendRequest(req, null);
    }

    public static void sendOnline(int account) {
        TL_account.updateStatus req = new TL_account.updateStatus();
        req.offline = false;
        ConnectionsManager.getInstance(account).sendRequest(req, null);
    }
}

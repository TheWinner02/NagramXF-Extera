package com.radolyn.ayugram.controllers.messages;

import android.text.TextUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.DeletedMessage;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.DeletedMessageReaction;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.TLRPC;

public class DeletedMessageService {
    private final AyuMessagesController controller;

    public DeletedMessageService(AyuMessagesController controller) {
        this.controller = controller;
    }

    public void onMessageDeleted(final SaveMessageRequest request) {
        if (validateForSave(request)) {
            this.controller.executeAsync(() -> {
                if (messageExists(request)) {
                    FileLog.e("onMessageDeleted: message exists");
                } else {
                    saveDeletedMessage(request);
                }
            }, "saveDeletedMessage");
        }
    }

    private boolean validateForSave(SaveMessageRequest request) {
        try {
            if (request == null || request.getMessage() == null) {
                FileLog.e("onMessageDeleted: message is null");
                return false;
            }
            TLRPC.Message message = request.getMessage();
            if (message.send_state == 1 && message.id < 0) {
                FileLog.e("onMessageDeleted: message is sending");
                return false;
            }
            if (!AyuConfig.saveDeletedMessageFor(this.controller.getCurrentAccount(), request.getDialogId())) {
                FileLog.e("onMessageDeleted: saveDeletedMessageFor is false");
                return false;
            }
            if (!(message instanceof TLRPC.TL_messageService) && !(message instanceof TLRPC.TL_messageEmpty)) {
                return true;
            }
            FileLog.e("onMessageDeleted: message is empty");
            return false;
        } catch (Throwable th) {
            FileLog.e("onMessageDeleted error", th);
            return false;
        }
    }

    private boolean messageExists(SaveMessageRequest request) {
        return AyuData.getDeletedMessageDao().exists(this.controller.getUserId(), request.getDialogId(), request.getTopicId(), request.getMessageId());
    }

    public void saveDeletedMessage(SaveMessageRequest request) {
        DeletedMessage deletedMessage = new DeletedMessage();
        deletedMessage.userId = this.controller.getUserId();
        deletedMessage.dialogId = request.getDialogId();
        deletedMessage.messageId = request.getMessageId();
        deletedMessage.entityCreateDate = request.getRequestCatchTime();
        TLRPC.Message message = request.getMessage();
        this.controller.getAyuMapperInternal().map(request, deletedMessage);
        try {
            this.controller.getAyuMapperInternal().mapMedia(request, deletedMessage, true);
        } catch (Throwable th) {
            FileLog.e("saveDeletedMessage.mapMedia", th);
        }
        long insertedId = AyuData.getDeletedMessageDao().insert(deletedMessage);
        this.controller.updateLastMessage(request.getDialogId(), message);
        if (message.reactions != null && message.reactions.results != null) {
            processDeletedReactions(insertedId, message.reactions.results);
        }
    }

    private void processDeletedReactions(long deletedMessageId, ArrayList<TLRPC.ReactionCount> results) {
        if (results == null) return;
        for (int i = 0; i < results.size(); i++) {
            TLRPC.ReactionCount reactionCount = results.get(i);
            if (reactionCount != null && !(reactionCount.reaction instanceof TLRPC.TL_reactionEmpty)) {
                DeletedMessageReaction deletedMessageReaction = new DeletedMessageReaction();
                deletedMessageReaction.deletedMessageId = deletedMessageId;
                deletedMessageReaction.count = reactionCount.count;
                deletedMessageReaction.selfSelected = reactionCount.chosen;
                TLRPC.Reaction reaction = reactionCount.reaction;
                if (reaction instanceof TLRPC.TL_reactionEmoji) {
                    deletedMessageReaction.emoticon = ((TLRPC.TL_reactionEmoji) reaction).emoticon;
                } else if (reaction instanceof TLRPC.TL_reactionCustomEmoji) {
                    deletedMessageReaction.documentId = ((TLRPC.TL_reactionCustomEmoji) reaction).document_id;
                    deletedMessageReaction.isCustom = true;
                } else if (reaction instanceof TLRPC.TL_reactionPaid) {
                    deletedMessageReaction.isPaid = true;
                }
                AyuData.getDeletedMessageDao().insertReaction(deletedMessageReaction);
            }
        }
    }

    public void onHistoryFlushed(final TLRPC.Dialog dialog, final Runnable onDone) {
        if (dialog == null) return;
        this.controller.executeAsync(() -> {
            onDone.run();
        }, "saveHistory");
    }

    public DeletedMessageFull getMessage(long dialogId, int messageId) {
        return AyuData.getDeletedMessageDao().getMessage(this.controller.getUserId(), dialogId, messageId);
    }

    public List<DeletedMessageFull> getMessages(long dialogId, long topicId, int offset, int limit) {
        if (topicId == 0) {
            return AyuData.getDeletedMessageDao().getMessagesTopicless(this.controller.getUserId(), dialogId, offset, limit);
        }
        return AyuData.getDeletedMessageDao().getMessagesForTopic(this.controller.getUserId(), dialogId, topicId, offset, limit);
    }

    public List<DeletedMessageFull> getMessagesForScroll(long dialogId, long topicId, String query, int minId, int limit) {
        return AyuData.getDeletedMessageDao().getMessagesForScroll(this.controller.getUserId(), dialogId, topicId, TextUtils.isEmpty(query) ? "" : query, minId, limit);
    }

    public int getDeletedCount(long dialogId, long topicId, String query) {
        return AyuData.getDeletedMessageDao().getDeletedCount(this.controller.getUserId(), dialogId, topicId, query);
    }

    public boolean deleteMessage(long dialogId, int messageId) {
        DeletedMessageFull message = getMessage(dialogId, messageId);
        if (message == null) {
            return false;
        }
        AyuData.getDeletedMessageDao().delete(this.controller.getUserId(), dialogId, messageId);
        if (message.message != null && !TextUtils.isEmpty(message.message.mediaPath) && message.message.mediaPath.contains(AyuConfig.getSavePath())) {
            File file = new File(message.message.mediaPath);
            if (file.exists()) {
                try {
                    if (!file.delete()) {
                        file.deleteOnExit();
                    }
                } catch (Throwable unused) {
                    file.deleteOnExit();
                }
            }
        }
        return true;
    }

    public void clearDeletedFromDialog(long dialogId, long topicId, Long messageId) {
        AyuData.getDeletedMessageDao().clearForDialog(this.controller.getUserId(), dialogId, messageId);
        this.controller.getLastMessages().remove(dialogId);
        if (topicId != 0 && topicId != dialogId) {
            AyuData.getDeletedMessageDao().clearForDialog(this.controller.getUserId(), topicId, messageId);
            AyuData.getDeletedMessageDao().clearForDialog(this.controller.getUserId(), topicId, null);
            this.controller.getLastMessages().remove(topicId);
        }
    }
}

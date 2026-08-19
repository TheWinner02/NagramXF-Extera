package com.radolyn.ayugram.controllers.messages;

import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;

public class SaveMessageRequest {
    private long dialogId;
    private boolean force = false;
    private TLRPC.Message message;
    private int messageId;
    private long monoForumTopicId;
    private int requestCatchTime;
    private long topicId;

    public SaveMessageRequest(TLRPC.Message message, long dialogId, long topicId, int messageId, int requestCatchTime) {
        this.monoForumTopicId = -1L;
        this.requestCatchTime = -1;
        this.message = message;
        this.dialogId = dialogId;
        this.topicId = topicId;
        this.messageId = messageId;
        this.requestCatchTime = requestCatchTime < 1397411401 ? (int) (System.currentTimeMillis() / 1000) : requestCatchTime;
        this.monoForumTopicId = MessageObject.getMonoForumTopicId(message);
    }

    public SaveMessageRequest(int account, TLRPC.Message message) {
        this.dialogId = -1L;
        this.topicId = -1L;
        this.monoForumTopicId = -1L;
        this.messageId = -1;
        this.requestCatchTime = -1;
        if (message == null) {
            return;
        }
        this.message = message;
        this.dialogId = MessageObject.getDialogId(message);
        this.topicId = MessageObject.getDialogId(this.message) == UserConfig.getInstance(account).getClientUserId() ? 0L : MessageObject.getTopicId(account, message, MessagesController.getInstance(account).isForum(message));
        this.messageId = message.id;
        this.requestCatchTime = (int) (System.currentTimeMillis() / 1000);
        this.monoForumTopicId = MessageObject.getMonoForumTopicId(message);
    }

    public TLRPC.Message getMessage() {
        return this.message;
    }

    public long getDialogId() {
        return this.dialogId;
    }

    public void setDialogId(long dialogId) {
        if (dialogId == 0) {
            return;
        }
        this.dialogId = dialogId;
    }

    public long getTopicId() {
        return this.topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public int getRequestCatchTime() {
        return this.requestCatchTime;
    }

    public long getMonoForumTopicId() {
        return this.monoForumTopicId;
    }

    public boolean isForce() {
        return this.force;
    }

    public void forceSaveEdited() {
        this.force = true;
    }
}

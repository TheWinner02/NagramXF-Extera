package com.radolyn.ayugram.controllers.messages;

import com.radolyn.ayugram.controllers.AyuMessagesController;

public class MessageDeleteWrapper {
    private final AyuMessagesController controller;
    private final DeletedMessageService deletedMessageService;
    private boolean deletedSmth = false;

    public MessageDeleteWrapper(AyuMessagesController controller, DeletedMessageService deletedMessageService) {
        this.controller = controller;
        this.deletedMessageService = deletedMessageService;
    }

    public MessageDeleteWrapper deleteMessage(long dialogId, int messageId) {
        this.deletedSmth = this.deletedMessageService.deleteMessage(dialogId, messageId) | this.deletedSmth;
        return this;
    }

    public void commit() {
        if (this.deletedSmth) {
            this.controller.executeAsync(() -> this.controller.getDeletedDialogServiceInternal().loadLastMessages(), "commitDeleteWrapper");
        }
    }
}

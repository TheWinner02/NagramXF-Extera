package com.radolyn.ayugram.controllers.messages;

import android.text.TextUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.EditedMessage;
import java.util.List;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.TLRPC;

public class EditedMessageService {
    private final AyuMessagesController controller;

    public EditedMessageService(AyuMessagesController controller) {
        this.controller = controller;
    }

    public void onMessageEdited(final SaveMessageRequest request, TLRPC.Message oldMessage) {
        try {
            if (AyuConfig.saveEditedMessageFor(this.controller.getCurrentAccount(), request.getDialogId()) || request.isForce()) {
                TLRPC.Message newMessage = request.getMessage();
                final boolean sameMedia = !request.isForce() && isSameMedia(newMessage, oldMessage);
                if (sameMedia && TextUtils.equals(newMessage.message, oldMessage.message)) {
                    return;
                }
                if (!request.isForce() && oldMessage != null && oldMessage.from_id != null && oldMessage.from_id.user_id != 0 && !oldMessage.edit_hide) {
                    this.controller.getAyuSpyControllerInternal().saveOnlineActivity(oldMessage.from_id.user_id, request.getRequestCatchTime());
                }
                this.controller.executeAsync(() -> saveEditedMessage(request, sameMedia), "saveEditedMessage");
            }
        } catch (Throwable th) {
            FileLog.e("onMessageEdited error", th);
        }
    }

    private static boolean isSameMedia(TLRPC.Message m1, TLRPC.Message m2) {
        if (m1 == null || m2 == null) return false;
        TLRPC.MessageMedia media1 = m1.media;
        TLRPC.MessageMedia media2 = m2.media;
        if (media1 == media2) return true;
        if (media1 == null || media2 == null || media1.getClass() != media2.getClass()) return false;
        if (media1 instanceof TLRPC.TL_messageMediaPhoto) {
            if (media1.photo != null && media2.photo != null) {
                return media1.photo.id == media2.photo.id;
            }
        } else if (media1 instanceof TLRPC.TL_messageMediaDocument) {
            if (media1.document != null && media2.document != null) {
                return media1.document.id == media2.document.id;
            }
        }
        return true;
    }

    private void saveEditedMessage(SaveMessageRequest request, boolean sameMedia) {
        EditedMessage editedMessage = new EditedMessage();
        this.controller.getAyuMapperInternal().map(request, editedMessage);
        try {
            this.controller.getAyuMapperInternal().mapMedia(request, editedMessage, !sameMedia);
        } catch (Exception e) {
            FileLog.e("saveEditedMessage mapMedia error: " + request.getMessageId(), e);
        }
        if (!sameMedia && !TextUtils.isEmpty(editedMessage.mediaPath)) {
            EditedMessage lastRevision = AyuData.getEditedMessageDao().getLastRevision(this.controller.getUserId(), request.getDialogId(), request.getMessageId());
            if (lastRevision != null && !TextUtils.equals(editedMessage.mediaPath, lastRevision.mediaPath) && lastRevision.mediaPath != null && !lastRevision.mediaPath.contains(AyuConfig.getSavePathFolder())) {
                AyuData.getEditedMessageDao().updateMediaPathForRevisionsBetweenDates(this.controller.getUserId(), request.getDialogId(), request.getMessageId(), lastRevision.mediaPath, editedMessage.mediaPath);
            }
        }
        AyuData.getEditedMessageDao().insert(editedMessage);
    }

    public boolean hasAnyRevisions(long dialogId, int messageId) {
        return AyuData.getEditedMessageDao().hasAnyRevisions(this.controller.getUserId(), dialogId, messageId);
    }

    public List<EditedMessage> getRevisions(long dialogId, int messageId, int offset, int limit) {
        return AyuData.getEditedMessageDao().getAllRevisions(this.controller.getUserId(), dialogId, messageId, offset, limit);
    }
}

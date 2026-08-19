package com.radolyn.ayugram.controllers.messages;

import android.text.TextUtils;
import android.util.LongSparseArray;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.DeletedDialog;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

public class DeletedDialogService {
    private final AyuMessagesController controller;

    public DeletedDialogService(AyuMessagesController controller) {
        this.controller = controller;
    }

    public void loadLastMessages() {
        List<DeletedMessageFull> lastMessages = AyuData.getDeletedMessageDao().getLastMessages(this.controller.getUserId());
        this.controller.getLastMessages().clear();
        for (DeletedMessageFull deletedMessageFull : lastMessages) {
            if (deletedMessageFull != null && deletedMessageFull.message != null) {
                TLRPC.TL_message message = new TLRPC.TL_message();
                this.controller.getAyuMapperInternal().map(deletedMessageFull.message, message);
                try {
                    this.controller.getAyuMapperInternal().mapMedia(deletedMessageFull.message, message);
                } catch (Exception e) {
                    FileLog.e("loadLastMessages mapMedia error: " + deletedMessageFull.message.messageId, e);
                }
                MessageObject messageObject = new MessageObject(this.controller.getCurrentAccount(), message, false, true);
                if (!TextUtils.isEmpty(messageObject.messageText)) {
                    this.controller.getLastMessages().put(deletedMessageFull.message.dialogId, messageObject);
                }
            }
        }
    }

    public void onDialogDeleted(long dialogId) {
        this.controller.executeAsync(() -> {
            DeletedDialog deletedDialog = new DeletedDialog();
            deletedDialog.userId = this.controller.getUserId();
            deletedDialog.dialogId = dialogId;
            deletedDialog.entityCreateDate = (int) (System.currentTimeMillis() / 1000);
            AyuData.getDeletedDialogDao().insert(deletedDialog);
        }, "onDialogDeleted");
    }

    public void updateDeletedDialogsFolder(ArrayList<DeletedDialog> dialogs, int folderId) {
        if (dialogs == null || dialogs.isEmpty()) return;
        for (int i = 0; i < dialogs.size(); i++) {
            DeletedDialog dialog = dialogs.get(i);
            if (dialog != null) {
                dialog.folderId = folderId;
                AyuData.getDeletedDialogDao().insert(dialog);
            }
        }
    }

    public void deleteExistingDialogs(ArrayList<DeletedDialog> dialogs) {
        if (dialogs == null || dialogs.isEmpty()) return;
        for (int i = 0; i < dialogs.size(); i++) {
            DeletedDialog dialog = dialogs.get(i);
            if (dialog != null) {
                AyuData.getDeletedDialogDao().delete(this.controller.getUserId(), dialog.dialogId);
            }
        }
    }

    public void deleteDialog(long dialogId) {
        AyuData.getDeletedDialogDao().delete(this.controller.getUserId(), dialogId);
    }

    public void updateLastMessage(long dialogId, TLRPC.Message message) {
        if (message == null) return;
        MessageObject messageObject = new MessageObject(this.controller.getCurrentAccount(), message, false, true);
        this.controller.getLastMessages().put(dialogId, messageObject);
    }
}

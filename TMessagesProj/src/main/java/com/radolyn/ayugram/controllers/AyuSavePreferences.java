package com.radolyn.ayugram.controllers;

import com.radolyn.ayugram.controllers.messages.SaveMessageRequest;
import org.telegram.tgnet.TLRPC;

public class AyuSavePreferences extends SaveMessageRequest {
    public static String saveExclusionPrefix = "save_excl_";

    public AyuSavePreferences(TLRPC.Message message, int account) {
        super(account, message);
    }

    public AyuSavePreferences(TLRPC.Message message, long dialogId, long topicId, int messageId, int catchTime) {
        super(message, dialogId, topicId, messageId, catchTime);
    }

    public AyuSavePreferences(TLRPC.Message message, int account, long dialogId, long topicId, int messageId, int catchTime) {
        super(message, dialogId, topicId, messageId, catchTime);
    }

    public static boolean saveDeletedMessageFor(int account, long dialogId, Object obj) {
        return com.radolyn.ayugram.AyuConfig.saveDeletedMessageFor(account, dialogId);
    }

    public static boolean getSaveDeletedExclusion(long chatId) {
        return false;
    }

    public static void setSaveDeletedExclusion(long chatId, boolean exclusion) {
    }
}

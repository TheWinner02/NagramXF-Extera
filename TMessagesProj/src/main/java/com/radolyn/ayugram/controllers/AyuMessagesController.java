package com.radolyn.ayugram.controllers;

import org.telegram.tgnet.TLRPC;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AyuMessagesController {
    public static File attachmentsPath;
    public static String attachmentsSubfolder = "attachments";

    public static AyuMessagesController getInstance() {
        return new AyuMessagesController();
    }

    public static AyuMessagesController getInstance(int account) {
        return new AyuMessagesController();
    }

    public void onMessageDeleted(Object prefs) {
    }

    public void onMessageDeleted(Object prefs, boolean b) {
    }

    public boolean isAyuDeletedMessageId(long u, long d, int m) {
        return false;
    }

    public boolean hasAnyRevisions(long u, long d, int m) {
        return false;
    }

    public void deleteMessages(long u, long d, ArrayList<Integer> ids) {
    }

    public void deleteCurrent(long d1, long d2, Runnable r) {
        if (r != null) r.run();
    }

    public void onMessageEditedForce(Object prefs) {
    }

    public void onMessageEdited(Object prefs, Object msg) {
    }

    public ArrayList<Integer> getExistingMessageIds(long u, long d, ArrayList<Integer> p) {
        return new ArrayList<>();
    }

    public DeletedMessageFull getMessage(long u, long d, int m) {
        return null;
    }

    public static void onAttachmentsCleanUp() {
    }

    public static void setAttachmentFolderPath(File folder) {
        attachmentsPath = folder;
    }

    public static void clearAttachments() {
    }

    public static void clearDatabase() {
    }

    public static int clampAttachmentSizeLimitPreset(int preset) {
        return preset;
    }

    public static void syncAttachmentsPathWithConfig() {
    }

    public static void trimAttachmentsFolderToLimit() {
    }

    public List getMessages(long j, long j2, int i2, int i3) {
        return new ArrayList();
    }

    public TLRPC.Message getMessage(long dialogId, int msgId) {
        return null;
    }
}

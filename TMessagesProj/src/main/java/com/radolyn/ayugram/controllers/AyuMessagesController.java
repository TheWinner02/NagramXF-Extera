package com.radolyn.ayugram.controllers;

import com.radolyn.ayugram.AyuUtils;
import com.radolyn.ayugram.controllers.messages.DeletedDialogService;
import com.radolyn.ayugram.controllers.messages.DeletedMessageService;
import com.radolyn.ayugram.controllers.messages.EditedMessageService;
import com.radolyn.ayugram.controllers.messages.MessageDeleteWrapper;
import com.radolyn.ayugram.controllers.messages.SaveMessageRequest;
import com.radolyn.ayugram.database.AyuData;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.EditedMessage;
import com.radolyn.ayugram.utils.ThreadSafeLongSparseArray;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;

public class AyuMessagesController extends BaseController {
    private static volatile AyuMessagesController[] Instance = new AyuMessagesController[16];
    private final DeletedDialogService deletedDialogService;
    private final DeletedMessageService deletedMessageService;
    private final EditedMessageService editedMessageService;
    private final ExecutorService executor;
    private final ThreadSafeLongSparseArray lastMessages;

    public AyuMessagesController(final int account) {
        super(account);
        this.executor = Executors.newFixedThreadPool(3, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("AyuMessagesPool_" + account);
            return thread;
        });
        this.lastMessages = new ThreadSafeLongSparseArray();
        this.deletedMessageService = new DeletedMessageService(this);
        this.editedMessageService = new EditedMessageService(this);
        this.deletedDialogService = new DeletedDialogService(this);
        executeAsync(() -> this.deletedDialogService.loadLastMessages(), "loadLastMessages");
    }

    public static AyuMessagesController getInstance() {
        return getInstance(UserConfig.selectedAccount);
    }

    public static AyuMessagesController getInstance(int account) {
        AyuMessagesController controller = Instance[account];
        if (controller != null) {
            return controller;
        }
        synchronized (AyuMessagesController.class) {
            controller = Instance[account];
            if (controller == null) {
                AyuMessagesController[] array = Instance;
                AyuMessagesController newController = new AyuMessagesController(account);
                array[account] = newController;
                controller = newController;
            }
        }
        return controller;
    }

    public static void clear() {
        AyuData.clearMessageDatabase();
        for (int i = 0; i < UserConfig.getActivatedAccountsCount(); i++) {
            if (Instance[i] != null && Instance[i].executor != null) {
                Instance[i].executor.shutdown();
            }
        }
        Instance = new AyuMessagesController[16];
    }

    public void onMessageEdited(SaveMessageRequest request, TLRPC.Message oldMessage) {
        this.editedMessageService.onMessageEdited(request, oldMessage);
    }

    public void onMessageEdited(SaveMessageRequest request) {
        this.editedMessageService.onMessageEdited(request, null);
    }

    public void onMessageEditedForce(SaveMessageRequest request) {
        if (request != null) {
            request.forceSaveEdited();
        }
        this.editedMessageService.onMessageEdited(request, null);
    }

    public void onMessageDeleted(SaveMessageRequest request) {
        this.deletedMessageService.onMessageDeleted(request);
    }

    public void onHistoryFlushed(TLRPC.Dialog dialog, Runnable onDone) {
        this.deletedMessageService.onHistoryFlushed(dialog, onDone);
    }

    public void onDialogDeleted(long dialogId) {
        this.deletedDialogService.onDialogDeleted(dialogId);
    }

    public void updateDeletedDialogsFolder(ArrayList dialogs, int folderId) {
        this.deletedDialogService.updateDeletedDialogsFolder(dialogs, folderId);
    }

    public void deleteExistingDialogs(ArrayList dialogs) {
        this.deletedDialogService.deleteExistingDialogs(dialogs);
    }

    public boolean hasAnyRevisions(long dialogId, int messageId) {
        return this.editedMessageService.hasAnyRevisions(dialogId, messageId);
    }

    public List<EditedMessage> getRevisions(long dialogId, int messageId, int offset, int limit) {
        return this.editedMessageService.getRevisions(dialogId, messageId, offset, limit);
    }

    public static java.io.File attachmentsPath = com.radolyn.ayugram.AyuConfig.getSavePathJava();
    public static String attachmentsSubfolder = "media";

    public static void setAttachmentFolderPath(java.io.File folder) {
        if (folder != null) attachmentsPath = folder;
    }

    public static void clearAttachments() {
        com.radolyn.ayugram.controllers.messages.AttachmentsCacheManager.clearAll();
    }

    public static void clearDatabase() {
        clear();
    }

    public static int clampAttachmentSizeLimitPreset(int preset) {
        return Math.max(0, preset);
    }

    public static void syncAttachmentsPathWithConfig() {
        attachmentsPath = com.radolyn.ayugram.AyuConfig.getSavePathJava();
    }

    public static void trimAttachmentsFolderToLimit() {
        com.radolyn.ayugram.controllers.messages.AttachmentsCacheManager.cleanUp();
    }

    public void onMessageDeleted(SaveMessageRequest request, boolean force) {
        if (request != null && force) {
            request.forceSaveEdited();
        }
        onMessageDeleted(request);
    }

    public DeletedMessageFull getMessage(long userId, long dialogId, int messageId) {
        return getMessage(dialogId, messageId);
    }

    public DeletedMessageFull getMessage(long dialogId, int messageId) {
        return this.deletedMessageService.getMessage(dialogId, messageId);
    }

    public List<DeletedMessageFull> getMessages(long dialogId, long topicId, int offset, int limit) {
        return this.deletedMessageService.getMessages(dialogId, topicId, offset, limit);
    }

    public List<DeletedMessageFull> getMessagesForScroll(long dialogId, long topicId, String query, int minId, int limit) {
        return this.deletedMessageService.getMessagesForScroll(dialogId, topicId, query, minId, limit);
    }

    public int getDeletedCount(long dialogId, long topicId, String query) {
        return this.deletedMessageService.getDeletedCount(dialogId, topicId, query);
    }

    public void clearDeletedFromDialog(long dialogId, long topicId, Long messageId) {
        this.deletedMessageService.clearDeletedFromDialog(dialogId, topicId, messageId);
    }

    public void deleteDialog(long dialogId) {
        this.deletedDialogService.deleteDialog(dialogId);
    }

    public MessageObject getLastMessageCached(long dialogId) {
        return (MessageObject) this.lastMessages.get(dialogId);
    }

    public int getLastMessagesCount() {
        return this.lastMessages.size();
    }

    public MessageDeleteWrapper wrapDelete() {
        return new MessageDeleteWrapper(this, this.deletedMessageService);
    }

    public ExecutorService getExecutor() {
        return this.executor;
    }

    public void executeAsync(final Runnable runnable, final String tag) {
        if (this.executor.isShutdown() || this.executor.isTerminated()) {
            FileLog.w("Executor shutdown, tag: " + tag);
            return;
        }
        this.executor.execute(() -> {
            try {
                runnable.run();
            } catch (Throwable th) {
                AyuUtils.logError(tag, th);
            }
        });
    }

    public long getUserId() {
        return getUserConfig().getClientUserId();
    }

    public int getCurrentAccount() {
        return this.currentAccount;
    }

    public ThreadSafeLongSparseArray getLastMessages() {
        return this.lastMessages;
    }

    public void updateLastMessage(long dialogId, TLRPC.Message message) {
        this.deletedDialogService.updateLastMessage(dialogId, message);
    }

    public static void onAttachmentsCleanUp() {
        com.radolyn.ayugram.controllers.messages.AttachmentsCacheManager.cleanUp();
    }

    public boolean hasAnyRevisions(long userId, long dialogId, int messageId) {
        return hasAnyRevisions(dialogId, messageId);
    }

    public ArrayList<Integer> getExistingMessageIds(long userId, long dialogId, ArrayList<Integer> permittedIds) {
        ArrayList<Integer> result = new ArrayList<>();
        if (permittedIds != null) {
            for (int i = 0; i < permittedIds.size(); i++) {
                int id = permittedIds.get(i);
                if (getMessage(dialogId, id) != null) {
                    result.add(id);
                }
            }
        }
        return result;
    }

    public boolean isAyuDeletedMessageId(long userId, long dialogId, int messageId) {
        return getMessage(dialogId, messageId) != null;
    }

    public void deleteCurrent(long dialogId, long mergeDialogId, Runnable callback) {
        clearDeletedFromDialog(dialogId, mergeDialogId, null);
        if (callback != null) {
            callback.run();
        }
    }

    public void deleteMessages(long userId, long dialogId, ArrayList<Integer> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) return;
        MessageDeleteWrapper wrapper = wrapDelete();
        for (int i = 0; i < messageIds.size(); i++) {
            wrapper.deleteMessage(dialogId, messageIds.get(i));
        }
        wrapper.commit();
    }

    public AyuMapper getAyuMapperInternal() {
        return AyuMapper.getInstance(this.currentAccount);
    }

    public MessagesController getMessagesControllerInternal() {
        return getMessagesController();
    }

    public MessagesStorage getMessagesStorageInternal() {
        return getMessagesStorage();
    }

    public NotificationCenter getNotificationCenterInternal() {
        return getNotificationCenter();
    }

    public AyuSpyController getAyuSpyControllerInternal() {
        return AyuSpyController.getInstance(this.currentAccount);
    }

    public DeletedDialogService getDeletedDialogServiceInternal() {
        return this.deletedDialogService;
    }
}

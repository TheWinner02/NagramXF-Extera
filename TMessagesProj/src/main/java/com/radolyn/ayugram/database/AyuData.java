package com.radolyn.ayugram.database;

import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.dao.DeletedDialogDao;
import com.radolyn.ayugram.database.dao.DeletedMessageDao;
import com.radolyn.ayugram.database.dao.EditedMessageDao;
import com.radolyn.ayugram.database.dao.RegexFilterDao;
import com.radolyn.ayugram.database.dao.SpyDao;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.ui.LaunchActivity;

public class AyuData {
    public static long totalSize = 0L;
    public static void exportDatabase(Object os) {}
    public static void importDatabase(Object is) {}
    private static AyuSQLiteHelper sqLiteHelper;
    private static DeletedDialogDao deletedDialogDao;
    private static DeletedMessageDao deletedMessageDao;
    private static EditedMessageDao editedMessageDao;
    private static Runnable nextTidyUpAttachmentsRunnable;
    private static Runnable nextTidyUpDBRunnable;
    private static RegexFilterDao regexFilterDao;
    private static SpyDao spyDao;
    private static final Object sync = new Object();

    static {
        create();
    }

    public static void create() {
        synchronized (sync) {
            try {
                if (sqLiteHelper == null && ApplicationLoader.applicationContext != null) {
                    sqLiteHelper = new AyuSQLiteHelper(ApplicationLoader.applicationContext);
                }
                initDaos();
                tidyUpDB();
                tidyUpAttachments();
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    private static AyuSQLiteHelper getHelper() {
        if (sqLiteHelper == null && ApplicationLoader.applicationContext != null) {
            sqLiteHelper = new AyuSQLiteHelper(ApplicationLoader.applicationContext);
        }
        return sqLiteHelper;
    }

    private static void initDaos() {
        if (editedMessageDao == null) {
            editedMessageDao = new EditedMessageDao() {
                @Override public void deleteAll() {
                    AyuSQLiteHelper helper = getHelper();
                    if (helper != null) helper.deleteAllEditedMessages();
                }
                @Override public void deleteMedia(long j) {}
                @Override public List<com.radolyn.ayugram.database.entities.EditedMessage> getAllRevisions(long j, long j2, long j3, int i, int i2) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getAllRevisions(j, j2, j3, i, i2) : new ArrayList<>();
                }
                @Override public com.radolyn.ayugram.database.entities.EditedMessage getLastRevision(long j, long j2, long j3) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getLastRevision(j, j2, j3) : null;
                }
                @Override public boolean hasAnyRevisions(long j, long j2, long j3) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null && helper.hasAnyRevisions(j, j2, j3);
                }
                @Override public void insert(com.radolyn.ayugram.database.entities.EditedMessage editedMessage) {
                    AyuSQLiteHelper helper = getHelper();
                    if (helper != null) helper.insertEditedMessage(editedMessage);
                }
                @Override public void updateMediaPathForRevisionsBetweenDates(long j, long j2, long j3, String str, String str2) {}
            };
        }
        if (deletedMessageDao == null) {
            deletedMessageDao = new DeletedMessageDao() {
                @Override public void clearForDialog(long j, long j2, Long l) {
                    AyuSQLiteHelper helper = getHelper();
                    if (helper != null) helper.clearDeletedMessagesForDialog(j, j2, l);
                }
                @Override public void delete(long j, long j2, int i) {
                    AyuSQLiteHelper helper = getHelper();
                    if (helper != null) helper.deleteDeletedMessage(j, j2, i);
                }
                @Override public void deleteAll() {
                    AyuSQLiteHelper helper = getHelper();
                    if (helper != null) helper.deleteAllDeletedMessages();
                }
                @Override public void deleteMedia(long j) {}
                @Override public boolean exists(long j, long j2, long j3, int i) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null && helper.existsDeletedMessage(j, j2, j3, i);
                }
                @Override public boolean existsWithoutMedia(long j, long j2, int i) { return false; }
                @Override public int getDeletedCount() {
                    return getDeletedCount(0, 0, 0, "");
                }
                @Override public int getDeletedCount(long j, long j2, long j3, String str) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getDeletedMessagesCount(j, j2, j3, str) : 0;
                }
                @Override public List<DeletedMessageFull> getLastMessages(long j) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getAllDeletedMessagesForDialog(j, 0, 0, 50) : new ArrayList<>();
                }
                @Override public DeletedMessageFull getMessage(long j, long j2, int i) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getDeletedMessage(j, j2, i) : null;
                }
                @Override public List<com.radolyn.ayugram.database.other.CleanUpUnion> getMessagesForCleanUp() { return new ArrayList<>(); }
                @Override public List<DeletedMessageFull> getMessagesForScroll(long j, long j2, long j3, String str, int i, int i2) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getAllDeletedMessagesForDialog(j, j2, 0, i2) : new ArrayList<>();
                }
                @Override public List<DeletedMessageFull> getMessagesForTopic(long j, long j2, long j3, int i, int i2) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getDeletedMessages(j, j2, j3, i, i2) : new ArrayList<>();
                }
                @Override public List<DeletedMessageFull> getMessagesTopicless(long j, long j2, int i, int i2) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getDeletedMessages(j, j2, 0, i, i2) : new ArrayList<>();
                }
                @Override public List<DeletedMessageFull> getMessagesPaginated(long j, long j2, long j3, int i, int i2) {
                    AyuSQLiteHelper helper = getHelper();
                    if (helper == null) return new ArrayList<>();
                    return j3 == 0 ? helper.getAllDeletedMessagesForDialog(j, j2, i, i2) : helper.getDeletedMessagesForTopic(j, j2, j3, i, i2);
                }
                @Override public long insert(com.radolyn.ayugram.database.entities.DeletedMessage deletedMessage) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.insertDeletedMessage(deletedMessage) : 0;
                }
                @Override public void insertReaction(com.radolyn.ayugram.database.entities.DeletedMessageReaction deletedMessageReaction) {
                    AyuSQLiteHelper helper = getHelper();
                    if (helper != null) helper.insertReaction(deletedMessageReaction);
                }
                @Override public void updateMediaPath(long j, long j2, long j3, String str) {}
            };
        }
        if (deletedDialogDao == null) {
            deletedDialogDao = new DeletedDialogDao() {
                @Override public void delete(long j, long j2) {
                    AyuSQLiteHelper helper = getHelper();
                    if (helper != null) helper.deleteDeletedDialog(j, j2);
                }
                @Override public void delete(com.radolyn.ayugram.database.entities.DeletedDialog deletedDialog) {
                    if (deletedDialog != null) {
                        AyuSQLiteHelper helper = getHelper();
                        if (helper != null) helper.deleteDeletedDialog(deletedDialog.userId, deletedDialog.dialogId);
                    }
                }
                @Override public void deleteAll() {
                    AyuSQLiteHelper helper = getHelper();
                    if (helper != null) helper.deleteAllDeletedDialogs();
                }
                @Override public void deleteExisting(long j, List<Long> list) {
                    if (list != null) {
                        AyuSQLiteHelper helper = getHelper();
                        if (helper != null) {
                            for (Long dialogId : list) {
                                if (dialogId != null) helper.deleteDeletedDialog(j, dialogId);
                            }
                        }
                    }
                }
                @Override public com.radolyn.ayugram.database.entities.DeletedDialog get(long j, long j2) { return null; }
                @Override public List<com.radolyn.ayugram.database.entities.DeletedDialog> getAll(long j) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getAllDeletedDialogs(j) : new ArrayList<>();
                }
                @Override public int getDeletedCount() { return 0; }
                @Override public long insert(com.radolyn.ayugram.database.entities.DeletedDialog deletedDialog) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.insertDeletedDialog(deletedDialog) : 0;
                }
                @Override public void updateDialogsFolder(long j, List<Long> list, int i) {}
            };
        }
        if (regexFilterDao == null) {
            regexFilterDao = new RegexFilterDao() {
                @Override public void delete(java.util.UUID uuid) {}
                @Override public void deleteAllExclusions() {}
                @Override public void deleteAllFilters() {}
                @Override public void deleteExclusion(long j, java.util.UUID uuid) {}
                @Override public void deleteExclusionsByFilterId(java.util.UUID uuid) {}
                @Override public List<com.radolyn.ayugram.database.entities.RegexFilter> getAll() { return new ArrayList<>(); }
                @Override public List<com.radolyn.ayugram.database.entities.RegexFilterGlobalExclusion> getAllExclusions() { return new ArrayList<>(); }
                @Override public List<com.radolyn.ayugram.database.entities.RegexFilter> getByDialogId(long j) { return new ArrayList<>(); }
                @Override public com.radolyn.ayugram.database.entities.RegexFilter getById(java.util.UUID uuid) { return null; }
                @Override public int getCount() { return 0; }
                @Override public List<com.radolyn.ayugram.database.entities.RegexFilter> getExcludedByDialogId(long j) { return new ArrayList<>(); }
                @Override public List<com.radolyn.ayugram.database.entities.RegexFilter> getShared() { return new ArrayList<>(); }
                @Override public void insert(com.radolyn.ayugram.database.entities.RegexFilter regexFilter) {}
                @Override public void insertExclusion(com.radolyn.ayugram.database.entities.RegexFilterGlobalExclusion regexFilterGlobalExclusion) {}
                @Override public void update(com.radolyn.ayugram.database.entities.RegexFilter regexFilter) {}
            };
        }
        if (spyDao == null) {
            spyDao = new SpyDao() {
                @Override public void deleteOldContentsRead() {}
                @Override public void deleteOldLastSeen() {}
                @Override public void deleteOldReads() {}
                @Override public com.radolyn.ayugram.database.entities.SpyLastSeen getLastSeen(long j) {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getSpyLastSeen(j) : null;
                }
                @Override public int getLastSeenCount() {
                    AyuSQLiteHelper helper = getHelper();
                    return helper != null ? helper.getSpyLastSeenCount() : 0;
                }
                @Override public com.radolyn.ayugram.database.entities.SpyMessageContentsRead getMessageContentsRead(long j, long j2, int i) { return null; }
                @Override public com.radolyn.ayugram.database.entities.SpyMessageRead getMessageRead(long j, long j2, int i) { return null; }
                @Override public void insert(com.radolyn.ayugram.database.entities.SpyLastSeen spyLastSeen) {
                    AyuSQLiteHelper helper = getHelper();
                    if (helper != null) helper.insertSpyLastSeen(spyLastSeen);
                }
                @Override public void insert(com.radolyn.ayugram.database.entities.SpyMessageContentsRead spyMessageContentsRead) {}
                @Override public void insert(com.radolyn.ayugram.database.entities.SpyMessageRead spyMessageRead) {}
                @Override public int vacuum(androidx.sqlite.db.SupportSQLiteQuery supportSQLiteQuery) { return 0; }
            };
        }
    }

    public static EditedMessageDao getEditedMessageDao() {
        if (editedMessageDao == null) {
            create();
        }
        return editedMessageDao;
    }

    public static DeletedMessageDao getDeletedMessageDao() {
        if (deletedMessageDao == null) {
            create();
        }
        return deletedMessageDao;
    }

    public static DeletedDialogDao getDeletedDialogDao() {
        if (deletedDialogDao == null) {
            create();
        }
        return deletedDialogDao;
    }

    public static RegexFilterDao getRegexFilterDao() {
        if (regexFilterDao == null) {
            create();
        }
        return regexFilterDao;
    }

    public static SpyDao getSpyDao() {
        if (spyDao == null) {
            create();
        }
        return spyDao;
    }

    public static long getAyuDatabaseSize() {
        if (ApplicationLoader.applicationContext == null) return 0L;
        File databasePath = ApplicationLoader.applicationContext.getDatabasePath("ayu.db");
        return databasePath != null && databasePath.exists() ? databasePath.length() : 0L;
    }

    public static void loadSizes(Runnable callback) {
        org.telegram.messenger.Utilities.globalQueue.postRunnable(() -> {
            if (callback != null) {
                AndroidUtilities.runOnUIThread(callback);
            }
        });
    }

    public static long getAttachmentsDirSize() {
        return getAyuDatabaseSize();
    }

    public static void importAyuDatabase(Object fragment, File file) {
    }

    public static void clearMessageDatabase() {
        if (deletedDialogDao != null) deletedDialogDao.deleteAll();
        if (deletedMessageDao != null) deletedMessageDao.deleteAll();
        if (editedMessageDao != null) editedMessageDao.deleteAll();
        tidyUpDB(true);
    }

    public static void clearRegexFilterDatabase() {
        if (regexFilterDao != null) {
            regexFilterDao.deleteAllFilters();
            regexFilterDao.deleteAllExclusions();
        }
    }

    public static boolean exportDatabase() {
        return false;
    }

    public static boolean importDatabase() {
        return false;
    }

    public static boolean canImportDatabase() {
        return false;
    }

    public static void tidyUpDB() {
        tidyUpDB(false);
    }

    public static void tidyUpDB(boolean z) {
    }

    public static void tidyUpAttachments() {
    }
}

package com.radolyn.ayugram.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.radolyn.ayugram.database.entities.DeletedDialog;
import com.radolyn.ayugram.database.entities.DeletedMessage;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.DeletedMessageReaction;
import com.radolyn.ayugram.database.entities.EditedMessage;
import com.radolyn.ayugram.database.entities.RegexFilter;
import com.radolyn.ayugram.database.entities.RegexFilterGlobalExclusion;
import com.radolyn.ayugram.database.entities.SpyLastSeen;
import com.radolyn.ayugram.database.entities.SpyMessageContentsRead;
import com.radolyn.ayugram.database.entities.SpyMessageRead;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.telegram.messenger.FileLog;

public class AyuSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ayu.db";
    private static final int DATABASE_VERSION = 1;

    public AyuSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS `DeletedMessage` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `groupedId` INTEGER NOT NULL, `peerId` INTEGER NOT NULL, `fromId` INTEGER NOT NULL, `topicId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `date` INTEGER NOT NULL, `flags` INTEGER NOT NULL, `editDate` INTEGER NOT NULL, `views` INTEGER NOT NULL, `fwdFlags` INTEGER NOT NULL, `fwdFromId` INTEGER NOT NULL, `fwdName` TEXT, `fwdDate` INTEGER NOT NULL, `fwdPostAuthor` TEXT, `postAuthor` TEXT, `replyFlags` INTEGER NOT NULL, `replyMessageId` INTEGER NOT NULL, `replyPeerId` INTEGER NOT NULL, `replyTopId` INTEGER NOT NULL, `replyForumTopic` INTEGER NOT NULL, `replySerialized` BLOB, `replyMarkupSerialized` BLOB, `entityCreateDate` INTEGER NOT NULL, `text` TEXT, `textEntities` BLOB, `mediaPath` TEXT, `hqThumbPath` TEXT, `documentType` INTEGER NOT NULL, `documentSerialized` BLOB, `thumbsSerialized` BLOB, `documentAttributesSerialized` BLOB, `mimeType` TEXT)");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_DeletedMessage_userId_dialogId_topicId_messageId` ON `DeletedMessage` (`userId`, `dialogId`, `topicId`, `messageId`)");
            db.execSQL("CREATE TABLE IF NOT EXISTS `DeletedMessageReaction` (`fakeReactionId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deletedMessageId` INTEGER NOT NULL, `emoticon` TEXT, `documentId` INTEGER NOT NULL, `isCustom` INTEGER NOT NULL, `isPaid` INTEGER NOT NULL DEFAULT 0, `count` INTEGER NOT NULL, `selfSelected` INTEGER NOT NULL)");
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_DeletedMessageReaction_deletedMessageId` ON `DeletedMessageReaction` (`deletedMessageId`)");
            db.execSQL("CREATE TABLE IF NOT EXISTS `DeletedDialog` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `peerId` INTEGER NOT NULL, `folderId` INTEGER, `topMessage` INTEGER NOT NULL, `lastMessageDate` INTEGER NOT NULL, `flags` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS `EditedMessage` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `groupedId` INTEGER NOT NULL, `peerId` INTEGER NOT NULL, `fromId` INTEGER NOT NULL, `topicId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `date` INTEGER NOT NULL, `flags` INTEGER NOT NULL, `editDate` INTEGER NOT NULL, `views` INTEGER NOT NULL, `fwdFlags` INTEGER NOT NULL, `fwdFromId` INTEGER NOT NULL, `fwdName` TEXT, `fwdDate` INTEGER NOT NULL, `fwdPostAuthor` TEXT, `postAuthor` TEXT, `replyFlags` INTEGER NOT NULL, `replyMessageId` INTEGER NOT NULL, `replyPeerId` INTEGER NOT NULL, `replyTopId` INTEGER NOT NULL, `replyForumTopic` INTEGER NOT NULL, `replySerialized` BLOB, `replyMarkupSerialized` BLOB, `entityCreateDate` INTEGER NOT NULL, `text` TEXT, `textEntities` BLOB, `mediaPath` TEXT, `hqThumbPath` TEXT, `documentType` INTEGER NOT NULL, `documentSerialized` BLOB, `thumbsSerialized` BLOB, `documentAttributesSerialized` BLOB, `mimeType` TEXT)");
            db.execSQL("CREATE TABLE IF NOT EXISTS `SpyLastSeen` (`userId` INTEGER PRIMARY KEY NOT NULL, `lastSeenDate` INTEGER NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS `SpyMessageRead` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS `SpyMessageContentsRead` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `dialogId` INTEGER NOT NULL, `messageId` INTEGER NOT NULL, `entityCreateDate` INTEGER NOT NULL)");
            db.execSQL("CREATE TABLE IF NOT EXISTS `RegexFilter` (`id` TEXT PRIMARY KEY, `text` TEXT, `enabled` INTEGER NOT NULL, `reversed` INTEGER NOT NULL DEFAULT 0, `caseInsensitive` INTEGER NOT NULL, `dialogId` INTEGER)");
            db.execSQL("CREATE TABLE IF NOT EXISTS `RegexFilterGlobalExclusion` (`fakeId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dialogId` INTEGER NOT NULL, `filterId` TEXT)");
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // --- DeletedMessage Operations ---
    public synchronized long insertDeletedMessage(DeletedMessage msg) {
        if (msg == null) return 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("userId", msg.userId);
            cv.put("dialogId", msg.dialogId);
            cv.put("groupedId", msg.groupedId);
            cv.put("peerId", msg.peerId);
            cv.put("fromId", msg.fromId);
            cv.put("topicId", msg.topicId);
            cv.put("messageId", msg.messageId);
            cv.put("date", msg.date);
            cv.put("flags", msg.flags);
            cv.put("editDate", msg.editDate);
            cv.put("views", msg.views);
            cv.put("fwdFlags", msg.fwdFlags);
            cv.put("fwdFromId", msg.fwdFromId);
            cv.put("fwdName", msg.fwdName);
            cv.put("fwdDate", msg.fwdDate);
            cv.put("fwdPostAuthor", msg.fwdPostAuthor);
            cv.put("postAuthor", msg.postAuthor);
            cv.put("replyFlags", msg.replyFlags);
            cv.put("replyMessageId", msg.replyMessageId);
            cv.put("replyPeerId", msg.replyPeerId);
            cv.put("replyTopId", msg.replyTopId);
            cv.put("replyForumTopic", msg.replyForumTopic ? 1 : 0);
            cv.put("replySerialized", msg.replySerialized);
            cv.put("replyMarkupSerialized", msg.replyMarkupSerialized);
            cv.put("entityCreateDate", msg.entityCreateDate);
            cv.put("text", msg.text);
            cv.put("textEntities", msg.textEntities);
            cv.put("mediaPath", msg.mediaPath);
            cv.put("hqThumbPath", msg.hqThumbPath);
            cv.put("documentType", msg.documentType);
            cv.put("documentSerialized", msg.documentSerialized);
            cv.put("thumbsSerialized", msg.thumbsSerialized);
            cv.put("documentAttributesSerialized", msg.documentAttributesSerialized);
            cv.put("mimeType", msg.mimeType);
            return db.insertWithOnConflict("DeletedMessage", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Throwable th) {
            FileLog.e(th);
            return 0;
        }
    }

    public synchronized void insertReaction(DeletedMessageReaction reaction) {
        if (reaction == null) return;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("deletedMessageId", reaction.deletedMessageId);
            cv.put("emoticon", reaction.emoticon);
            cv.put("documentId", reaction.documentId);
            cv.put("isCustom", reaction.isCustom ? 1 : 0);
            cv.put("isPaid", reaction.isPaid ? 1 : 0);
            cv.put("count", reaction.count);
            cv.put("selfSelected", reaction.selfSelected ? 1 : 0);
            db.insert("DeletedMessageReaction", null, cv);
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public synchronized boolean existsDeletedMessage(long userId, long dialogId, long topicId, int messageId) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT 1 FROM DeletedMessage WHERE userId = ? AND dialogId = ? AND messageId = ? LIMIT 1",
                    new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(messageId)});
            boolean exists = c.moveToFirst();
            c.close();
            return exists;
        } catch (Throwable th) {
            FileLog.e(th);
            return false;
        }
    }

    public synchronized List<DeletedMessageFull> getDeletedMessages(long userId, long dialogId, long topicId, int minId, int maxId) {
        List<DeletedMessageFull> list = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            String query;
            String[] args;
            if (topicId == 0) {
                if (minId == 0 && maxId == Integer.MAX_VALUE) {
                    query = "SELECT * FROM DeletedMessage WHERE userId = ? AND dialogId = ? ORDER BY messageId DESC";
                    args = new String[]{String.valueOf(userId), String.valueOf(dialogId)};
                } else {
                    query = "SELECT * FROM DeletedMessage WHERE userId = ? AND dialogId = ? AND messageId >= ? AND messageId <= ? ORDER BY messageId DESC";
                    args = new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(minId), String.valueOf(maxId)};
                }
            } else {
                if (minId == 0 && maxId == Integer.MAX_VALUE) {
                    query = "SELECT * FROM DeletedMessage WHERE userId = ? AND dialogId = ? AND topicId = ? ORDER BY messageId DESC";
                    args = new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(topicId)};
                } else {
                    query = "SELECT * FROM DeletedMessage WHERE userId = ? AND dialogId = ? AND topicId = ? AND messageId >= ? AND messageId <= ? ORDER BY messageId DESC";
                    args = new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(topicId), String.valueOf(minId), String.valueOf(maxId)};
                }
            }
            Cursor c = db.rawQuery(query, args);
            while (c.moveToNext()) {
                DeletedMessageFull full = parseDeletedMessage(c);
                list.add(full);
            }
            c.close();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        return list;
    }

    public synchronized List<DeletedMessageFull> getAllDeletedMessagesForDialog(long userId, long dialogId, int offset, int limit) {
        List<DeletedMessageFull> list = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM DeletedMessage WHERE userId = ? AND dialogId = ? ORDER BY messageId DESC LIMIT ? OFFSET ?",
                    new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(limit), String.valueOf(offset)});
            while (c.moveToNext()) {
                DeletedMessageFull full = parseDeletedMessage(c);
                list.add(full);
            }
            c.close();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        return list;
    }

    public synchronized List<DeletedMessageFull> getDeletedMessagesForTopic(long userId, long dialogId, long topicId, int offset, int limit) {
        List<DeletedMessageFull> list = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM DeletedMessage WHERE userId = ? AND dialogId = ? AND topicId = ? ORDER BY messageId DESC LIMIT ? OFFSET ?",
                    new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(topicId), String.valueOf(limit), String.valueOf(offset)});
            while (c.moveToNext()) {
                DeletedMessageFull full = parseDeletedMessage(c);
                list.add(full);
            }
            c.close();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        return list;
    }

    public synchronized DeletedMessageFull getDeletedMessage(long userId, long dialogId, int messageId) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM DeletedMessage WHERE userId = ? AND dialogId = ? AND messageId = ? LIMIT 1",
                    new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(messageId)});
            DeletedMessageFull full = null;
            if (c.moveToFirst()) {
                full = parseDeletedMessage(c);
            }
            c.close();
            return full;
        } catch (Throwable th) {
            FileLog.e(th);
            return null;
        }
    }

    public synchronized void deleteDeletedMessage(long userId, long dialogId, int messageId) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete("DeletedMessageReaction", "deletedMessageId = ?", new String[]{String.valueOf(messageId)});
            db.delete("DeletedMessage", "userId = ? AND dialogId = ? AND messageId = ?",
                    new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(messageId)});
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public synchronized void clearDeletedMessagesForDialog(long userId, long dialogId, Long messageId) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            if (messageId == null) {
                db.execSQL("DELETE FROM DeletedMessageReaction WHERE deletedMessageId IN (SELECT messageId FROM DeletedMessage WHERE userId = " + userId + " AND dialogId = " + dialogId + ")");
                db.delete("DeletedMessage", "userId = ? AND dialogId = ?",
                        new String[]{String.valueOf(userId), String.valueOf(dialogId)});
                db.delete("DeletedDialog", "userId = ? AND dialogId = ?",
                        new String[]{String.valueOf(userId), String.valueOf(dialogId)});
            } else {
                db.delete("DeletedMessageReaction", "deletedMessageId = ?",
                        new String[]{String.valueOf(messageId)});
                db.delete("DeletedMessage", "userId = ? AND dialogId = ? AND messageId = ?",
                        new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(messageId)});
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public synchronized void deleteAllDeletedMessages() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete("DeletedMessage", null, null);
            db.delete("DeletedMessageReaction", null, null);
            db.delete("DeletedDialog", null, null);
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public synchronized void deleteDeletedDialog(long userId, long dialogId) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete("DeletedDialog", "userId = ? AND dialogId = ?",
                    new String[]{String.valueOf(userId), String.valueOf(dialogId)});
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public synchronized void deleteAllDeletedDialogs() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete("DeletedDialog", null, null);
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public synchronized long insertDeletedDialog(DeletedDialog dialog) {
        if (dialog == null) return 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("userId", dialog.userId);
            cv.put("dialogId", dialog.dialogId);
            cv.put("peerId", dialog.peerId);
            if (dialog.folderId != null) cv.put("folderId", dialog.folderId);
            cv.put("topMessage", dialog.topMessage);
            cv.put("lastMessageDate", dialog.lastMessageDate);
            cv.put("flags", dialog.flags);
            cv.put("entityCreateDate", dialog.entityCreateDate);
            return db.insertWithOnConflict("DeletedDialog", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Throwable th) {
            FileLog.e(th);
            return 0;
        }
    }

    public synchronized List<DeletedDialog> getAllDeletedDialogs(long userId) {
        List<DeletedDialog> list = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM DeletedDialog WHERE userId = ?",
                    new String[]{String.valueOf(userId)});
            while (c.moveToNext()) {
                DeletedDialog d = new DeletedDialog();
                d.fakeId = c.getLong(c.getColumnIndexOrThrow("fakeId"));
                d.userId = c.getLong(c.getColumnIndexOrThrow("userId"));
                d.dialogId = c.getLong(c.getColumnIndexOrThrow("dialogId"));
                d.peerId = c.getLong(c.getColumnIndexOrThrow("peerId"));
                int folderIdx = c.getColumnIndexOrThrow("folderId");
                if (!c.isNull(folderIdx)) d.folderId = c.getInt(folderIdx);
                d.topMessage = c.getInt(c.getColumnIndexOrThrow("topMessage"));
                d.lastMessageDate = c.getInt(c.getColumnIndexOrThrow("lastMessageDate"));
                d.flags = c.getInt(c.getColumnIndexOrThrow("flags"));
                d.entityCreateDate = c.getInt(c.getColumnIndexOrThrow("entityCreateDate"));
                list.add(d);
            }
            c.close();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        return list;
    }

    public synchronized int getDeletedMessagesCount(long userId, long dialogId, long topicId, String query) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM DeletedMessage WHERE userId = ? AND dialogId = ?",
                    new String[]{String.valueOf(userId), String.valueOf(dialogId)});
            int count = 0;
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            c.close();
            return count;
        } catch (Throwable th) {
            FileLog.e(th);
            return 0;
        }
    }

    private DeletedMessageFull parseDeletedMessage(Cursor c) {
        DeletedMessage msg = new DeletedMessage();
        msg.fakeId = c.getLong(c.getColumnIndexOrThrow("fakeId"));
        msg.userId = c.getLong(c.getColumnIndexOrThrow("userId"));
        msg.dialogId = c.getLong(c.getColumnIndexOrThrow("dialogId"));
        msg.groupedId = c.getLong(c.getColumnIndexOrThrow("groupedId"));
        msg.peerId = c.getLong(c.getColumnIndexOrThrow("peerId"));
        msg.fromId = c.getLong(c.getColumnIndexOrThrow("fromId"));
        msg.topicId = c.getLong(c.getColumnIndexOrThrow("topicId"));
        msg.messageId = c.getInt(c.getColumnIndexOrThrow("messageId"));
        msg.date = c.getInt(c.getColumnIndexOrThrow("date"));
        msg.flags = c.getInt(c.getColumnIndexOrThrow("flags"));
        msg.editDate = c.getInt(c.getColumnIndexOrThrow("editDate"));
        msg.views = c.getInt(c.getColumnIndexOrThrow("views"));
        msg.fwdFlags = c.getInt(c.getColumnIndexOrThrow("fwdFlags"));
        msg.fwdFromId = c.getLong(c.getColumnIndexOrThrow("fwdFromId"));
        msg.fwdName = c.getString(c.getColumnIndexOrThrow("fwdName"));
        msg.fwdDate = c.getInt(c.getColumnIndexOrThrow("fwdDate"));
        msg.fwdPostAuthor = c.getString(c.getColumnIndexOrThrow("fwdPostAuthor"));
        msg.postAuthor = c.getString(c.getColumnIndexOrThrow("postAuthor"));
        msg.replyFlags = c.getInt(c.getColumnIndexOrThrow("replyFlags"));
        msg.replyMessageId = c.getInt(c.getColumnIndexOrThrow("replyMessageId"));
        msg.replyPeerId = c.getLong(c.getColumnIndexOrThrow("replyPeerId"));
        msg.replyTopId = c.getInt(c.getColumnIndexOrThrow("replyTopId"));
        msg.replyForumTopic = c.getInt(c.getColumnIndexOrThrow("replyForumTopic")) == 1;
        msg.replySerialized = c.getBlob(c.getColumnIndexOrThrow("replySerialized"));
        msg.replyMarkupSerialized = c.getBlob(c.getColumnIndexOrThrow("replyMarkupSerialized"));
        msg.entityCreateDate = c.getInt(c.getColumnIndexOrThrow("entityCreateDate"));
        msg.text = c.getString(c.getColumnIndexOrThrow("text"));
        msg.textEntities = c.getBlob(c.getColumnIndexOrThrow("textEntities"));
        msg.mediaPath = c.getString(c.getColumnIndexOrThrow("mediaPath"));
        msg.hqThumbPath = c.getString(c.getColumnIndexOrThrow("hqThumbPath"));
        msg.documentType = c.getInt(c.getColumnIndexOrThrow("documentType"));
        msg.documentSerialized = c.getBlob(c.getColumnIndexOrThrow("documentSerialized"));
        msg.thumbsSerialized = c.getBlob(c.getColumnIndexOrThrow("thumbsSerialized"));
        msg.documentAttributesSerialized = c.getBlob(c.getColumnIndexOrThrow("documentAttributesSerialized"));
        msg.mimeType = c.getString(c.getColumnIndexOrThrow("mimeType"));

        DeletedMessageFull full = new DeletedMessageFull();
        full.message = msg;
        return full;
    }

    // --- Spy LastSeen Operations ---
    public synchronized void insertSpyLastSeen(SpyLastSeen item) {
        if (item == null) return;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("userId", item.userId);
            cv.put("lastSeenDate", item.lastSeenDate);
            db.insertWithOnConflict("SpyLastSeen", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public synchronized SpyLastSeen getSpyLastSeen(long userId) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT userId, lastSeenDate FROM SpyLastSeen WHERE userId = ? LIMIT 1",
                    new String[]{String.valueOf(userId)});
            SpyLastSeen item = null;
            if (c.moveToFirst()) {
                item = new SpyLastSeen();
                item.userId = c.getLong(0);
                item.lastSeenDate = c.getInt(1);
            }
            c.close();
            return item;
        } catch (Throwable th) {
            FileLog.e(th);
            return null;
        }
    }

    public synchronized int getSpyLastSeenCount() {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT COUNT(*) FROM SpyLastSeen", null);
            int count = 0;
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            c.close();
            return count;
        } catch (Throwable th) {
            FileLog.e(th);
            return 0;
        }
    }

    // --- EditedMessage Operations ---
    public synchronized long insertEditedMessage(EditedMessage msg) {
        if (msg == null) return 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("userId", msg.userId);
            cv.put("dialogId", msg.dialogId);
            cv.put("groupedId", msg.groupedId);
            cv.put("peerId", msg.peerId);
            cv.put("fromId", msg.fromId);
            cv.put("topicId", msg.topicId);
            cv.put("messageId", msg.messageId);
            cv.put("date", msg.date);
            cv.put("flags", msg.flags);
            cv.put("editDate", msg.editDate);
            cv.put("views", msg.views);
            cv.put("fwdFlags", msg.fwdFlags);
            cv.put("fwdFromId", msg.fwdFromId);
            cv.put("fwdName", msg.fwdName);
            cv.put("fwdDate", msg.fwdDate);
            cv.put("fwdPostAuthor", msg.fwdPostAuthor);
            cv.put("postAuthor", msg.postAuthor);
            cv.put("replyFlags", msg.replyFlags);
            cv.put("replyMessageId", msg.replyMessageId);
            cv.put("replyPeerId", msg.replyPeerId);
            cv.put("replyTopId", msg.replyTopId);
            cv.put("replyForumTopic", msg.replyForumTopic ? 1 : 0);
            cv.put("replySerialized", msg.replySerialized);
            cv.put("replyMarkupSerialized", msg.replyMarkupSerialized);
            cv.put("entityCreateDate", msg.entityCreateDate);
            cv.put("text", msg.text);
            cv.put("textEntities", msg.textEntities);
            cv.put("mediaPath", msg.mediaPath);
            cv.put("hqThumbPath", msg.hqThumbPath);
            cv.put("documentType", msg.documentType);
            cv.put("documentSerialized", msg.documentSerialized);
            cv.put("thumbsSerialized", msg.thumbsSerialized);
            cv.put("documentAttributesSerialized", msg.documentAttributesSerialized);
            cv.put("mimeType", msg.mimeType);
            return db.insertWithOnConflict("EditedMessage", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (Throwable th) {
            FileLog.e(th);
            return 0;
        }
    }

    public synchronized List<EditedMessage> getAllRevisions(long userId, long dialogId, long messageId, int offset, int limit) {
        List<EditedMessage> list = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM EditedMessage WHERE userId = ? AND dialogId = ? AND messageId = ? ORDER BY editDate DESC LIMIT ? OFFSET ?",
                    new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(messageId), String.valueOf(limit), String.valueOf(offset)});
            while (c.moveToNext()) {
                EditedMessage msg = parseEditedMessage(c);
                list.add(msg);
            }
            c.close();
        } catch (Throwable th) {
            FileLog.e(th);
        }
        return list;
    }

    public synchronized EditedMessage getLastRevision(long userId, long dialogId, long messageId) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM EditedMessage WHERE userId = ? AND dialogId = ? AND messageId = ? ORDER BY editDate DESC LIMIT 1",
                    new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(messageId)});
            EditedMessage msg = null;
            if (c.moveToFirst()) {
                msg = parseEditedMessage(c);
            }
            c.close();
            return msg;
        } catch (Throwable th) {
            FileLog.e(th);
            return null;
        }
    }

    public synchronized boolean hasAnyRevisions(long userId, long dialogId, long messageId) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT 1 FROM EditedMessage WHERE userId = ? AND dialogId = ? AND messageId = ? LIMIT 1",
                    new String[]{String.valueOf(userId), String.valueOf(dialogId), String.valueOf(messageId)});
            boolean exists = c.moveToFirst();
            c.close();
            return exists;
        } catch (Throwable th) {
            FileLog.e(th);
            return false;
        }
    }

    public synchronized void deleteAllEditedMessages() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete("EditedMessage", null, null);
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    private EditedMessage parseEditedMessage(Cursor c) {
        EditedMessage msg = new EditedMessage();
        msg.fakeId = c.getLong(c.getColumnIndexOrThrow("fakeId"));
        msg.userId = c.getLong(c.getColumnIndexOrThrow("userId"));
        msg.dialogId = c.getLong(c.getColumnIndexOrThrow("dialogId"));
        msg.groupedId = c.getLong(c.getColumnIndexOrThrow("groupedId"));
        msg.peerId = c.getLong(c.getColumnIndexOrThrow("peerId"));
        msg.fromId = c.getLong(c.getColumnIndexOrThrow("fromId"));
        msg.topicId = c.getLong(c.getColumnIndexOrThrow("topicId"));
        msg.messageId = c.getInt(c.getColumnIndexOrThrow("messageId"));
        msg.date = c.getInt(c.getColumnIndexOrThrow("date"));
        msg.flags = c.getInt(c.getColumnIndexOrThrow("flags"));
        msg.editDate = c.getInt(c.getColumnIndexOrThrow("editDate"));
        msg.views = c.getInt(c.getColumnIndexOrThrow("views"));
        msg.fwdFlags = c.getInt(c.getColumnIndexOrThrow("fwdFlags"));
        msg.fwdFromId = c.getLong(c.getColumnIndexOrThrow("fwdFromId"));
        msg.fwdName = c.getString(c.getColumnIndexOrThrow("fwdName"));
        msg.fwdDate = c.getInt(c.getColumnIndexOrThrow("fwdDate"));
        msg.fwdPostAuthor = c.getString(c.getColumnIndexOrThrow("fwdPostAuthor"));
        msg.postAuthor = c.getString(c.getColumnIndexOrThrow("postAuthor"));
        msg.replyFlags = c.getInt(c.getColumnIndexOrThrow("replyFlags"));
        msg.replyMessageId = c.getInt(c.getColumnIndexOrThrow("replyMessageId"));
        msg.replyPeerId = c.getLong(c.getColumnIndexOrThrow("replyPeerId"));
        msg.replyTopId = c.getInt(c.getColumnIndexOrThrow("replyTopId"));
        msg.replyForumTopic = c.getInt(c.getColumnIndexOrThrow("replyForumTopic")) == 1;
        msg.replySerialized = c.getBlob(c.getColumnIndexOrThrow("replySerialized"));
        msg.replyMarkupSerialized = c.getBlob(c.getColumnIndexOrThrow("replyMarkupSerialized"));
        msg.entityCreateDate = c.getInt(c.getColumnIndexOrThrow("entityCreateDate"));
        msg.text = c.getString(c.getColumnIndexOrThrow("text"));
        msg.textEntities = c.getBlob(c.getColumnIndexOrThrow("textEntities"));
        msg.mediaPath = c.getString(c.getColumnIndexOrThrow("mediaPath"));
        msg.hqThumbPath = c.getString(c.getColumnIndexOrThrow("hqThumbPath"));
        msg.documentType = c.getInt(c.getColumnIndexOrThrow("documentType"));
        msg.documentSerialized = c.getBlob(c.getColumnIndexOrThrow("documentSerialized"));
        msg.thumbsSerialized = c.getBlob(c.getColumnIndexOrThrow("thumbsSerialized"));
        msg.documentAttributesSerialized = c.getBlob(c.getColumnIndexOrThrow("documentAttributesSerialized"));
        msg.mimeType = c.getString(c.getColumnIndexOrThrow("mimeType"));
        return msg;
    }
}

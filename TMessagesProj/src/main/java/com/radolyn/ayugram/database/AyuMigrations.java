package com.radolyn.ayugram.database;

import android.annotation.SuppressLint;
import android.database.Cursor;
import androidx.room.migration.Migration;
import androidx.room.util.UUIDUtil;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class AyuMigrations {
    static final Migration MIGRATION_25_26 = new Migration(25, 26) { // from class: com.radolyn.ayugram.database.AyuMigrations.1
        @Override // androidx.room.migration.Migration
        @SuppressLint({"Range"})
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            Long lValueOf;
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS RegexFilter_new (id BLOB PRIMARY KEY NOT NULL, text TEXT, enabled INTEGER NOT NULL, caseInsensitive INTEGER NOT NULL, dialogId INTEGER)");
            Cursor cursorQuery = supportSQLiteDatabase.query("SELECT fakeId, text, enabled, caseInsensitive, dialogId FROM RegexFilter");
            while (cursorQuery.moveToNext()) {
                String string = cursorQuery.getString(cursorQuery.getColumnIndex("text"));
                boolean z = cursorQuery.getInt(cursorQuery.getColumnIndex("enabled")) != 0;
                boolean z2 = cursorQuery.getInt(cursorQuery.getColumnIndex("caseInsensitive")) != 0;
                Object obj = null;
                try {
                    lValueOf = Long.valueOf(cursorQuery.getLong(cursorQuery.getColumnIndex("dialogId")));
                } catch (Exception unused) {
                    lValueOf = null;
                }
                if (lValueOf == null || lValueOf.longValue() != 0) {
                    obj = lValueOf;
                }
                supportSQLiteDatabase.execSQL("INSERT INTO RegexFilter_new (id, text, enabled, caseInsensitive, dialogId) VALUES (?, ?, ?, ?, ?)", new Object[]{UUIDUtil.convertUUIDToByte(UUID.randomUUID()), string, Boolean.valueOf(z), Boolean.valueOf(z2), obj});
            }
            cursorQuery.close();
            supportSQLiteDatabase.execSQL("DROP TABLE RegexFilter");
            supportSQLiteDatabase.execSQL("ALTER TABLE RegexFilter_new RENAME TO RegexFilter");
            supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS RegexFilterGlobalExclusion (fakeId INTEGER PRIMARY KEY NOT NULL, dialogId INTEGER NOT NULL, filterId BLOB)");
        }
    };
    static final Migration MIGRATION_33_34 = new Migration(33, 34) { // from class: com.radolyn.ayugram.database.AyuMigrations.2
        @Override // androidx.room.migration.Migration
        @SuppressLint({"Range"})
        public void migrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            supportSQLiteDatabase.execSQL("DROP INDEX IF EXISTS index_DeletedMessage_groupedId");
            supportSQLiteDatabase.execSQL("CREATE INDEX IF NOT EXISTS index_DeletedMessage_dialogId_groupedId ON DeletedMessage (dialogId, groupedId)");
        }
    };
}

package com.radolyn.ayugram.database;

import androidx.room.RoomDatabase;
import com.radolyn.ayugram.database.dao.DeletedDialogDao;
import com.radolyn.ayugram.database.dao.DeletedMessageDao;
import com.radolyn.ayugram.database.dao.EditedMessageDao;
import com.radolyn.ayugram.database.dao.RegexFilterDao;
import com.radolyn.ayugram.database.dao.SpyDao;

/* JADX INFO: loaded from: classes.dex */
public abstract class AyuDatabase extends RoomDatabase {
    public abstract DeletedDialogDao deletedDialogDao();

    public abstract DeletedMessageDao deletedMessageDao();

    public abstract EditedMessageDao editedMessageDao();

    public abstract RegexFilterDao regexFilterDao();

    public abstract SpyDao spyDao();
}

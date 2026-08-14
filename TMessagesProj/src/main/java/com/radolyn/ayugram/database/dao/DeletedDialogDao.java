package com.radolyn.ayugram.database.dao;

import com.radolyn.ayugram.database.entities.DeletedDialog;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface DeletedDialogDao {
    void delete(long j, long j2);

    void delete(DeletedDialog deletedDialog);

    void deleteAll();

    void deleteExisting(long j, List<Long> list);

    DeletedDialog get(long j, long j2);

    List<DeletedDialog> getAll(long j);

    int getDeletedCount();

    long insert(DeletedDialog deletedDialog);

    void updateDialogsFolder(long j, List<Long> list, int i);
}

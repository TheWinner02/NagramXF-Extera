package com.radolyn.ayugram.database.dao;

import com.radolyn.ayugram.database.entities.EditedMessage;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface EditedMessageDao {
    void deleteAll();

    void deleteMedia(long j);

    List<EditedMessage> getAllRevisions(long j, long j2, long j3, int i, int i2);

    EditedMessage getLastRevision(long j, long j2, long j3);

    boolean hasAnyRevisions(long j, long j2, long j3);

    void insert(EditedMessage editedMessage);

    void updateMediaPathForRevisionsBetweenDates(long j, long j2, long j3, String str, String str2);
}

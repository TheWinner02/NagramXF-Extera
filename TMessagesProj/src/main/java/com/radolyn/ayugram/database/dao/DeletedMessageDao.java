package com.radolyn.ayugram.database.dao;

import com.radolyn.ayugram.database.entities.DeletedMessage;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.DeletedMessageReaction;
import com.radolyn.ayugram.database.other.CleanUpUnion;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface DeletedMessageDao {
    void clearForDialog(long j, long j2, Long l);

    void delete(long j, long j2, int i);

    void deleteAll();

    void deleteMedia(long j);

    boolean exists(long j, long j2, long j3, int i);

    boolean existsWithoutMedia(long j, long j2, int i);

    int getDeletedCount();

    int getDeletedCount(long j, long j2, long j3, String str);

    List<DeletedMessageFull> getLastMessages(long j);

    DeletedMessageFull getMessage(long j, long j2, int i);

    List<CleanUpUnion> getMessagesForCleanUp();

    List<DeletedMessageFull> getMessagesForScroll(long j, long j2, long j3, String str, int i, int i2);

    List<DeletedMessageFull> getMessagesForTopic(long j, long j2, long j3, int i, int i2);

    List<DeletedMessageFull> getMessagesTopicless(long j, long j2, int i, int i2);

    List<DeletedMessageFull> getMessagesPaginated(long j, long j2, long j3, int i, int i2);

    long insert(DeletedMessage deletedMessage);

    void insertReaction(DeletedMessageReaction deletedMessageReaction);

    void updateMediaPath(long j, long j2, long j3, String str);
}

package com.radolyn.ayugram.database.dao;

import androidx.sqlite.db.SupportSQLiteQuery;
import com.radolyn.ayugram.database.entities.SpyLastSeen;
import com.radolyn.ayugram.database.entities.SpyMessageContentsRead;
import com.radolyn.ayugram.database.entities.SpyMessageRead;

/* JADX INFO: loaded from: classes.dex */
public interface SpyDao {
    void deleteOldContentsRead();

    void deleteOldLastSeen();

    void deleteOldReads();

    SpyLastSeen getLastSeen(long j);

    int getLastSeenCount();

    SpyMessageContentsRead getMessageContentsRead(long j, long j2, int i);

    SpyMessageRead getMessageRead(long j, long j2, int i);

    void insert(SpyLastSeen spyLastSeen);

    void insert(SpyMessageContentsRead spyMessageContentsRead);

    void insert(SpyMessageRead spyMessageRead);

    int vacuum(SupportSQLiteQuery supportSQLiteQuery);
}

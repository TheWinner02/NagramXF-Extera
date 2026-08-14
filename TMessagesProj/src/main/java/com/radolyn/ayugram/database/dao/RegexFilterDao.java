package com.radolyn.ayugram.database.dao;

import com.radolyn.ayugram.database.entities.RegexFilter;
import com.radolyn.ayugram.database.entities.RegexFilterGlobalExclusion;
import java.util.List;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public interface RegexFilterDao {
    void delete(UUID uuid);

    void deleteAllExclusions();

    void deleteAllFilters();

    void deleteExclusion(long j, UUID uuid);

    void deleteExclusionsByFilterId(UUID uuid);

    List<RegexFilter> getAll();

    List<RegexFilterGlobalExclusion> getAllExclusions();

    List<RegexFilter> getByDialogId(long j);

    RegexFilter getById(UUID uuid);

    int getCount();

    List<RegexFilter> getExcludedByDialogId(long j);

    List<RegexFilter> getShared();

    void insert(RegexFilter regexFilter);

    void insertExclusion(RegexFilterGlobalExclusion regexFilterGlobalExclusion);

    void update(RegexFilter regexFilter);
}

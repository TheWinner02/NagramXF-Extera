package com.exteragram.messenger.config;

import com.exteragram.messenger.ExteraConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public final class LongPref extends BasePref<Long> {
    public LongPref(long j, String str) {
        super(Long.valueOf(j), str);
    }

    public /* synthetic */ LongPref(long j, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(j, (i & 2) != 0 ? null : str);
    }

    @Override // com.exteragram.messenger.config.BasePref
    public /* bridge */ /* synthetic */ Long fetch(String str, Long l) {
        return fetch(str, l.longValue());
    }

    @Override // com.exteragram.messenger.config.BasePref
    public /* bridge */ /* synthetic */ void save(String str, Long l) {
        save(str, l.longValue());
    }

    public Long fetch(String key, long j) {
        return Long.valueOf(ExteraConfig.getPreferences().getLong(key, j));
    }

    public void save(String key, long value) {
        ExteraConfig.getEditor().putLong(key, value).apply();
    }
}

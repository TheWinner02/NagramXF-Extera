package com.exteragram.messenger.config;

import com.exteragram.messenger.ExteraConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public final class IntegerPref extends BasePref<Integer> {
    public IntegerPref(int i, String str) {
        super(Integer.valueOf(i), str);
    }

    public /* synthetic */ IntegerPref(int i, String str, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? null : str);
    }

    @Override // com.exteragram.messenger.config.BasePref
    public /* bridge */ /* synthetic */ Integer fetch(String str, Integer num) {
        return fetch(str, num.intValue());
    }

    @Override // com.exteragram.messenger.config.BasePref
    public /* bridge */ /* synthetic */ void save(String str, Integer num) {
        save(str, num.intValue());
    }

    public Integer fetch(String key, int i) {
        return Integer.valueOf(ExteraConfig.getPreferences().getInt(key, i));
    }

    public void save(String key, int value) {
        ExteraConfig.getEditor().putInt(key, value).apply();
    }
}

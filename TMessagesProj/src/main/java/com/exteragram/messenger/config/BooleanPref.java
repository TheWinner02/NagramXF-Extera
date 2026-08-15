package com.exteragram.messenger.config;

import com.exteragram.messenger.ExteraConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public final class BooleanPref extends BasePref<Boolean> {
    public BooleanPref(boolean z, String str) {
        super(Boolean.valueOf(z), str);
    }

    public /* synthetic */ BooleanPref(boolean z, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(z, (i & 2) != 0 ? null : str);
    }

    @Override // com.exteragram.messenger.config.BasePref
    public /* bridge */ /* synthetic */ Boolean fetch(String str, Boolean bool) {
        return fetch(str, bool.booleanValue());
    }

    @Override // com.exteragram.messenger.config.BasePref
    public /* bridge */ /* synthetic */ void save(String str, Boolean bool) {
        save(str, bool.booleanValue());
    }

    public Boolean fetch(String key, boolean z) {
        return Boolean.valueOf(ExteraConfig.getPreferences().getBoolean(key, z));
    }

    public void save(String key, boolean value) {
        ExteraConfig.getEditor().putBoolean(key, value).apply();
    }
}

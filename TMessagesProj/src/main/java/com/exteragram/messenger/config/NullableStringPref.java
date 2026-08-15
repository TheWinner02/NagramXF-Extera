package com.exteragram.messenger.config;

import com.exteragram.messenger.ExteraConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public final class NullableStringPref extends BasePref<String> {
    public NullableStringPref(String str, String str2) {
        super(str, str2);
    }

    public /* synthetic */ NullableStringPref(String str, String str2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i & 2) != 0 ? null : str2);
    }

    @Override // com.exteragram.messenger.config.BasePref
    public String fetch(String key, String str) {
        return ExteraConfig.getPreferences().getString(key, str);
    }

    @Override // com.exteragram.messenger.config.BasePref
    public void save(String key, String value) {
        ExteraConfig.getEditor().putString(key, value).apply();
    }
}

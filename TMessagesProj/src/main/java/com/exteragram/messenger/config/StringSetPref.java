package com.exteragram.messenger.config;

import com.exteragram.messenger.ExteraConfig;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public final class StringSetPref extends BasePref<Set<? extends String>> {
    public StringSetPref(Set<String> set, String str) {
        super(set, str);
    }

    public /* synthetic */ StringSetPref(Set set, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(set, (i & 2) != 0 ? null : str);
    }

    @Override // com.exteragram.messenger.config.BasePref
    public /* bridge */ /* synthetic */ Set<? extends String> fetch(String str, Set<? extends String> set) {
        return fetch2(str, (Set<String>) set);
    }

    @Override // com.exteragram.messenger.config.BasePref
    public /* bridge */ /* synthetic */ void save(String str, Set<? extends String> set) {
        save2(str, (Set<String>) set);
    }

    /* JADX INFO: renamed from: fetch, reason: avoid collision after fix types in other method */
    public Set<String> fetch2(String key, Set<String> set) {
        Set<String> stringSet = ExteraConfig.getPreferences().getStringSet(key, set);
        return stringSet == null ? set : stringSet;
    }

    /* JADX INFO: renamed from: save, reason: avoid collision after fix types in other method */
    public void save2(String key, Set<String> value) {
        ExteraConfig.getEditor().putStringSet(key, value).apply();
    }
}

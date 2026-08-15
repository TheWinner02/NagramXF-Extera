package com.exteragram.messenger.config;

import com.exteragram.messenger.ExteraConfig;
import java.lang.Enum;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public final class EnumPref<E extends Enum<E>> extends BasePref<E> {
    public EnumPref(E e, String str) {
        super(e, str);
    }

    public /* synthetic */ EnumPref(Enum r1, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((E) r1, (i & 2) != 0 ? null : str);
    }

    @Override // com.exteragram.messenger.config.BasePref
    public E fetch(String key, E e) {
        E e2;
        int i = ExteraConfig.getPreferences().getInt(key, e.ordinal());
        try {
            Enum[] enumArr = (Enum[]) e.getClass().getEnumConstants();
            return (enumArr == null || (e2 = (E) ArraysKt.getOrNull(enumArr, i)) == null) ? e : e2;
        } catch (Exception unused) {
            return e;
        }
    }

    @Override // com.exteragram.messenger.config.BasePref
    public void save(String key, E value) {
        ExteraConfig.getEditor().putInt(key, value.ordinal()).apply();
    }
}

package com.exteragram.messenger.config;

import com.exteragram.messenger.ExteraConfig;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public final class FloatPref extends BasePref<Float> {
    public FloatPref(float f, String str) {
        super(Float.valueOf(f), str);
    }

    public /* synthetic */ FloatPref(float f, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(f, (i & 2) != 0 ? null : str);
    }

    @Override // com.exteragram.messenger.config.BasePref
    public /* bridge */ /* synthetic */ Float fetch(String str, Float f) {
        return fetch(str, f.floatValue());
    }

    @Override // com.exteragram.messenger.config.BasePref
    public /* bridge */ /* synthetic */ void save(String str, Float f) {
        save(str, f.floatValue());
    }

    public Float fetch(String key, float f) {
        float f2;
        try {
            try {
                f2 = ExteraConfig.getPreferences().getFloat(key, f);
            } catch (Exception unused) {
                f2 = f;
            }
        } catch (ClassCastException unused2) {
            f = ExteraConfig.getPreferences().getInt(key, (int) f);
            f2 = f;
            return Float.valueOf(f2);
        }
        return Float.valueOf(f2);
    }

    public void save(String key, float value) {
        ExteraConfig.getEditor().putFloat(key, value).apply();
    }
}

package com.exteragram.messenger.config;

import com.exteragram.messenger.backup.PreferencesUtils;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.reflect.KProperty;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
@SourceDebugExtension({"SMAP\nPrefClasses.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PrefClasses.kt\ncom/exteragram/messenger/config/BasePref\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,105:1\n1#2:106\n*E\n"})
public abstract class BasePref<T> {
    private final String backupKey;
    private T cache;
    private final T defaultValue;
    public String key;

    public abstract T fetch(String key, T t);

    public abstract void save(String key, T value);

    public BasePref(T t, String str) {
        this.defaultValue = t;
        this.backupKey = str;
    }

    public final String getKey() {
        String str = this.key;
        if (str != null) {
            return str;
        }
        return null;
    }

    public final void setKey(String str) {
        this.key = str;
    }

    public final BasePref<T> provideDelegate(Object thisRef, KProperty<?> property) {
        String name = this.backupKey;
        if (name == null) {
            name = property.getName();
        }
        setKey(name);
        List<PreferencesUtils.BackupItem> registeredKeys = PrefClassesKt.getRegisteredKeys();
        String key = getKey();
        T t = this.defaultValue;
        registeredKeys.add(new PreferencesUtils.BackupItem(key, t != null ? t.getClass() : String.class));
        PrefClassesKt.getAllDelegates().add(this);
        return this;
    }

    public final T getValue(Object thisRef, KProperty<?> property) {
        T t = this.cache;
        if (t != null) {
            return t;
        }
        T tFetch = fetch(getKey(), this.defaultValue);
        this.cache = tFetch;
        return tFetch;
    }

    public final void setValue(Object thisRef, KProperty<?> property, T value) {
        if (Intrinsics.areEqual(this.cache, value)) {
            return;
        }
        String key = getKey();
        this.cache = value;
        Unit unit = Unit.INSTANCE;
        save(key, value);
    }

    public final void invalidate() {
        this.cache = null;
    }
}

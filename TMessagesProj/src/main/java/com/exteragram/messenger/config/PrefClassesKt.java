package com.exteragram.messenger.config;

import com.exteragram.messenger.backup.PreferencesUtils;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public abstract class PrefClassesKt {
    private static final List<BasePref<?>> allDelegates = new ArrayList();
    private static final List<PreferencesUtils.BackupItem> registeredKeys = new ArrayList();

    public static final List<BasePref<?>> getAllDelegates() {
        return allDelegates;
    }

    public static final List<PreferencesUtils.BackupItem> getRegisteredKeys() {
        return registeredKeys;
    }
}

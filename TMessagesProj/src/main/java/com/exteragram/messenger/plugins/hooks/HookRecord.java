package com.exteragram.messenger.plugins.hooks;

import kotlin.Metadata;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public interface HookRecord {
    void cleanup();

    boolean matches(Object criteria);
}

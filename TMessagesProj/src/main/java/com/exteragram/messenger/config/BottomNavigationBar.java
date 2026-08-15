package com.exteragram.messenger.config;

import kotlin.Metadata;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public abstract class BottomNavigationBar {
    private static int mode;

    public static final int getMode() {
        int iCoerceIn = RangesKt.coerceIn(mode, 0, 2);
        mode = iCoerceIn;
        return iCoerceIn;
    }

    public static final void setMode(int i) {
        mode = i;
        getMode();
    }

    public static final boolean hidden() {
        return getMode() == 1;
    }

    public static final boolean visible() {
        return getMode() != 1;
    }

    public static final boolean floating() {
        return getMode() == 2;
    }
}

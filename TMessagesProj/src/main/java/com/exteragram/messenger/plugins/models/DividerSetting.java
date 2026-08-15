package com.exteragram.messenger.plugins.models;

import kotlin.Metadata;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public final class DividerSetting extends SettingItem {
    private String text;

    public DividerSetting(String str) {
        super("divider", null, null, null, 14, null);
        this.text = str;
    }

    public final String getText() {
        return this.text;
    }

    public final void setText(String str) {
        this.text = str;
    }
}

package com.exteragram.messenger.plugins.ui.components;

import android.view.View;
import kotlin.Metadata;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public interface PluginCellDelegate {
    boolean canOpenInExternalApp();

    void deletePlugin();

    void openInExternalApp();

    void openPluginSettings();

    void pinPlugin(View view);

    void sharePlugin();

    void togglePlugin(View view);
}

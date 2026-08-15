package com.exteragram.messenger.plugins.models;

import com.chaquo.python.PyObject;
import kotlin.Metadata;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public final class SwitchSetting extends SettingItem {
    private boolean defaultValue;
    private String key;
    private PyObject onChangeCallback;
    private String subtext;
    private String text;

    public final String getKey() {
        return this.key;
    }

    public final void setKey(String str) {
        this.key = str;
    }

    public final String getText() {
        return this.text;
    }

    public final void setText(String str) {
        this.text = str;
    }

    public final boolean getDefaultValue() {
        return this.defaultValue;
    }

    public final void setDefaultValue(boolean z) {
        this.defaultValue = z;
    }

    public final String getSubtext() {
        return this.subtext;
    }

    public final void setSubtext(String str) {
        this.subtext = str;
    }

    public final PyObject getOnChangeCallback() {
        return this.onChangeCallback;
    }

    public final void setOnChangeCallback(PyObject pyObject) {
        this.onChangeCallback = pyObject;
    }

    public SwitchSetting(String str, String str2, boolean z, String str3, String str4, PyObject pyObject, PyObject pyObject2, String str5) {
        super("switch", str4, pyObject2, str5);
        this.key = str;
        this.text = str2;
        this.defaultValue = z;
        this.subtext = str3;
        this.onChangeCallback = pyObject;
    }

    @Override // com.exteragram.messenger.plugins.models.SettingItem
    public void cleanup() {
        super.cleanup();
        closeCallback(this.onChangeCallback);
        this.onChangeCallback = null;
    }
}

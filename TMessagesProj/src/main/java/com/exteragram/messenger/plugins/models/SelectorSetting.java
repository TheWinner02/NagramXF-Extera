package com.exteragram.messenger.plugins.models;

import com.chaquo.python.PyObject;
import kotlin.Metadata;
import okhttp3.internal.url._UrlKt;

/* JADX INFO: loaded from: classes.dex */
public final class SelectorSetting extends SettingItem {
    private int defaultValue;
    private String[] items;
    private String key;
    private PyObject onChangeCallback;
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

    public final int getDefaultValue() {
        return this.defaultValue;
    }

    public final void setDefaultValue(int i) {
        this.defaultValue = i;
    }

    public final String[] getItems() {
        return this.items;
    }

    public final void setItems(String[] strArr) {
        this.items = strArr;
    }

    public final PyObject getOnChangeCallback() {
        return this.onChangeCallback;
    }

    public final void setOnChangeCallback(PyObject pyObject) {
        this.onChangeCallback = pyObject;
    }

    public SelectorSetting(String str, String str2, int i, String[] strArr, String str3, PyObject pyObject, PyObject pyObject2, String str4) {
        super("selector", str3, pyObject2, str4);
        this.key = str;
        this.text = str2;
        this.defaultValue = i;
        this.items = strArr;
        this.onChangeCallback = pyObject;
    }

    @Override // com.exteragram.messenger.plugins.models.SettingItem
    public void cleanup() {
        super.cleanup();
        closeCallback(this.onChangeCallback);
        this.onChangeCallback = null;
    }
}

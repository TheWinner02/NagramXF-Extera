package com.exteragram.messenger.plugins.models;

import com.chaquo.python.PyObject;
import com.sun.jna.Callback;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.scilab.forge.jlatexmath.TeXSymbolParser;

/* JADX INFO: loaded from: classes.dex */
public abstract class SettingItem {
    private String icon;
    private String linkAlias;
    private PyObject onLongClickCallback;
    private String type;

    public SettingItem(String str, String str2, PyObject pyObject, String str3) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-100783424292399L);
        this.type = str;
        this.icon = str2;
        this.onLongClickCallback = pyObject;
        this.linkAlias = str3;
    }

    public /* synthetic */ SettingItem(String str, String str2, PyObject pyObject, String str3, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : pyObject, (i & 8) != 0 ? null : str3);
    }

    public final String getType() {
        return this.type;
    }

    public final void setType(String str) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-100796309194287L);
        this.type = str;
    }

    public final String getIcon() {
        return this.icon;
    }

    public final void setIcon(String str) {
        this.icon = str;
    }

    public final PyObject getOnLongClickCallback() {
        return this.onLongClickCallback;
    }

    public final void setOnLongClickCallback(PyObject pyObject) {
        this.onLongClickCallback = pyObject;
    }

    public final String getLinkAlias() {
        return this.linkAlias;
    }

    public final void setLinkAlias(String str) {
        this.linkAlias = str;
    }

    public final void closeCallback(PyObject callback) {
        if (callback != null) {
            try {
                callback.close();
            } catch (Exception unused) {
            }
        }
    }

    public void cleanup() {
        closeCallback(this.onLongClickCallback);
        this.onLongClickCallback = null;
    }

    public final String getLink(String pluginId, String prefix) {
        String str;
        String str2 = this.linkAlias;
        if (str2 == null || str2.length() == 0 || pluginId == null || pluginId.length() == 0) {
            return null;
        }
        if (prefix == null) {
            str = this.linkAlias;
        } else {
            str = prefix + ':' + this.linkAlias;
        }
        return Deobfuscator$exteraGramDev$TMessagesProj.getString(-100761949455919L) + pluginId + Deobfuscator$exteraGramDev$TMessagesProj.getString(-99254415935023L) + str;
    }
}

package com.radolyn.ayugram.utils;

import org.telegram.tgnet.TLRPC;

/* JADX INFO: loaded from: classes.dex */
public class AyuFileLocation extends TLRPC.FileLocation {
    public String path;

    public AyuFileLocation(String str) {
        this.path = str;
    }
}

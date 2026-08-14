package com.radolyn.ayugram.utils.network;

import org.telegram.tgnet.TLObject;

/* JADX INFO: loaded from: classes.dex */
public class TLRPCWrappedBypass extends TLObject {
    private final TLObject wrappedObject;

    private TLRPCWrappedBypass(TLObject tLObject) {
        this.wrappedObject = tLObject;
    }

    public TLObject getWrappedObject() {
        return this.wrappedObject;
    }

    public static TLRPCWrappedBypass wrap(TLObject tLObject) {
        return new TLRPCWrappedBypass(tLObject);
    }
}

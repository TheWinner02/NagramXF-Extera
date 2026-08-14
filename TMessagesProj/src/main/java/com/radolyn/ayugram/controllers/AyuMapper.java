package com.radolyn.ayugram.controllers;

import org.telegram.tgnet.TLRPC;

public class AyuMapper {
    public static AyuMapper getInstance(int account) {
        return new AyuMapper();
    }

    public void map(Object deletedMessage, TLRPC.TL_message message) {
    }

    public void mapMedia(Object deletedMessage, TLRPC.TL_message message) {
    }
}

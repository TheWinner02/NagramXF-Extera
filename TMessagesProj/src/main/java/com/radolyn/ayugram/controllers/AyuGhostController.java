package com.radolyn.ayugram.controllers;

public class AyuGhostController {
    public static AyuGhostController getInstance() {
        return new AyuGhostController();
    }

    public static AyuGhostController getInstance(int account) {
        return new AyuGhostController();
    }

    public boolean isSendOfflinePacketAfterOnline() {
        return false;
    }
}

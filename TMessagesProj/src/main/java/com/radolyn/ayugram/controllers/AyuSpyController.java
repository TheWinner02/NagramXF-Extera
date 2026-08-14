package com.radolyn.ayugram.controllers;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

public class AyuSpyController {
    private static final AyuSpyController INSTANCE = new AyuSpyController();

    public static AyuSpyController getInstance() {
        return INSTANCE;
    }

    public void saveLastSeenFromLoadedMessages(long userId, long clientUserId, ArrayList<Object> messages, Object chatAdapter) {
    }

    public void saveLastSeenFromPeerReactions(ArrayList<TLRPC.MessagePeerReaction> reactions, long clientUserId) {
    }
}

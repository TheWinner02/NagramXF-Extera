package com.radolyn.ayugram.utils;

import com.radolyn.ayugram.controllers.AyuSpyController;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;

public class LastSeenHelper {

    public static void preload() {
    }

    public static void saveLastSeen(long userId, int date) {
    }

    public static String getFormattedLastSeenOrDefault(TLRPC.User user, boolean[] madeShorter, String defaultText) {
        return defaultText;
    }

    public static void saveLastSeenFromMessageReactions(TLRPC.TL_messageReactions reactions, long clientUserId) {
    }

    public static void saveLastSeenFromLoadedMessages(long userId, long clientUserId, ArrayList messages, Object chatAdapter) {
    }

    public static void saveLastSeenFromPeerReactions(ArrayList<TLRPC.MessagePeerReaction> reactions, long clientUserId) {
    }
}

package com.radolyn.ayugram.utils;

import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.controllers.AyuSpyController;
import java.util.ArrayList;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;

public class LastSeenHelper {

    public static void preload() {
    }

    public static void saveLastSeen(long userId, int date) {
        if (!AyuConfig.saveLocalOnline) return;
        AyuSpyController.getInstance(UserConfig.selectedAccount).saveOnlineActivity(userId, date);
    }

    public static String getFormattedLastSeenOrDefault(TLRPC.User user, boolean[] madeShorter, String defaultText) {
        if (user == null || !AyuConfig.saveLocalOnline) {
            return defaultText;
        }
        Integer lastSeen = AyuSpyController.getInstance(UserConfig.selectedAccount).getLastSeen(user.id);
        if (lastSeen != null && lastSeen > 0) {
            String formatted = LocaleController.formatDateOnline(lastSeen, madeShorter);
            return formatted + " (~)";
        }
        return defaultText;
    }

    public static void saveLastSeenFromMessageReactions(TLRPC.TL_messageReactions reactions, long clientUserId) {
        if (reactions == null || reactions.recent_reactions == null) return;
        for (int i = 0; i < reactions.recent_reactions.size(); i++) {
            TLRPC.MessagePeerReaction reaction = reactions.recent_reactions.get(i);
            if (reaction != null && reaction.peer_id != null) {
                long uid = reaction.peer_id.user_id;
                if (uid != 0 && uid != clientUserId) {
                    saveLastSeen(uid, (int) (System.currentTimeMillis() / 1000));
                }
            }
        }
    }

    public static void saveLastSeenFromLoadedMessages(long userId, long clientUserId, ArrayList messages, Object chatAdapter) {
    }

    public static void saveLastSeenFromPeerReactions(ArrayList<TLRPC.MessagePeerReaction> reactions, long clientUserId) {
        if (reactions == null) return;
        for (int i = 0; i < reactions.size(); i++) {
            TLRPC.MessagePeerReaction reaction = reactions.get(i);
            if (reaction != null && reaction.peer_id != null) {
                long uid = reaction.peer_id.user_id;
                if (uid != 0 && uid != clientUserId) {
                    saveLastSeen(uid, (int) (System.currentTimeMillis() / 1000));
                }
            }
        }
    }
}

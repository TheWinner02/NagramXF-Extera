package com.radolyn.ayugram;

public class AyuGhostPreferences {
    public static String ghostReadExclusionPrefix = "ghost_read_";
    public static String ghostTypingExclusionPrefix = "ghost_typing_";

    public static boolean getGhostModeReadExclusion(long chatId) {
        return false;
    }

    public static void setGhostModeReadExclusion(long chatId, boolean value) {
    }

    public static boolean getGhostModeTypingExclusion(long chatId) {
        return false;
    }

    public static void setGhostModeTypingExclusion(long chatId, boolean value) {
    }
}

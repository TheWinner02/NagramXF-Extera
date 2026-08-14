package com.radolyn.ayugram.controllers;

public class AyuSavePreferences {
    public static String saveExclusionPrefix = "save_excl_";

    public AyuSavePreferences(Object... args) {
    }

    public void setDialogId(long dialogId) {
    }

    public static boolean saveDeletedMessageFor(int account, long dialogId, Object obj) {
        return true;
    }

    public static boolean getSaveDeletedExclusion(long chatId) {
        return false;
    }

    public static void setSaveDeletedExclusion(long chatId, boolean exclusion) {
    }
}

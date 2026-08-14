package com.exteragram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import org.telegram.messenger.ApplicationLoader;
import java.util.ArrayList;

public class ExteraConfig {
    private static String editingIconPackId;
    private static IconPackType iconPack = IconPackType.SYSTEM;
    private static final ArrayList<String> iconPacksLayout = new ArrayList<>();
    private static final ArrayList<String> iconPacksHidden = new ArrayList<>();

    public static SharedPreferences getPreferences() {
        return ApplicationLoader.applicationContext.getSharedPreferences("exteraconfig", Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor() {
        return getPreferences().edit();
    }

    public static ArrayList<String> getIconPacksLayout() {
        return iconPacksLayout;
    }

    public static ArrayList<String> getIconPacksHidden() {
        return iconPacksHidden;
    }

    public static void saveIconPacksLayout() {
    }

    public static void loadConfig() {
    }

    public static String getEditingIconPackId() {
        return editingIconPackId;
    }

    public static void setEditingIconPackId(String id) {
        editingIconPackId = id;
    }

    public static IconPackType getIconPack() {
        return iconPack;
    }

    public static void setIconPack(IconPackType pack) {
        iconPack = pack;
    }
}

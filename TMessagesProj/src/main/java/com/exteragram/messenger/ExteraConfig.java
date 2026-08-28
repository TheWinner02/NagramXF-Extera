package com.exteragram.messenger;

import android.content.Context;
import android.content.SharedPreferences;

import com.exteragram.messenger.badges.BadgesController;
import com.exteragram.messenger.plugins.PluginsConstants;
import com.exteragram.messenger.plugins.PluginsController;

import org.telegram.messenger.ApplicationLoader;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class ExteraConfig {
    private static final Object sync = new Object();
    private static boolean configLoaded;
    private static boolean initialized;

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    public static boolean pluginsEngine;
    public static boolean pluginsSafeMode;
    public static boolean pluginsDevMode;
    public static boolean pluginsCompactView;
    public static boolean pluginsDisableArtOpts;
    public static boolean pluginsPySdkAutoUpdate;
    public static boolean pluginsPySdkBetaVersions;
    public static long sdkUpdateScheduleTimestamp;
    public static Set<String> pinnedPlugins = Collections.emptySet();

    public static String recognitionLanguage = "auto";
    private static String editingIconPackId;
    private static IconPackType iconPack = IconPackType.SYSTEM;
    private static final java.util.ArrayList<String> iconPacksLayout = new java.util.ArrayList<>();
    private static final java.util.ArrayList<String> iconPacksHidden = new java.util.ArrayList<>();

    public static SharedPreferences getPreferences() {
        if (preferences == null) {
            loadConfig();
        }
        if (preferences != null) {
            return preferences;
        }
        return ApplicationLoader.applicationContext.getSharedPreferences("exteraconfig", Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor() {
        if (editor == null) {
            loadConfig();
        }
        if (editor != null) {
            return editor;
        }
        return getPreferences().edit();
    }

    public static java.util.ArrayList<String> getIconPacksLayout() {
        return iconPacksLayout;
    }

    public static java.util.ArrayList<String> getIconPacksHidden() {
        return iconPacksHidden;
    }

    public static void saveIconPacksLayout() {
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

    public static boolean getInAppVibration() {
        return getPreferences().getBoolean("in_app_vibration", true);
    }

    public static int getSectionRadiusDp() {
        return 12;
    }

    public static android.util.Pair<Long, String> getApiBotInfo() {
        return new android.util.Pair<>(0L, "");
    }

    private ExteraConfig() {
    }

    public enum DrawerItem {
        PLUGINS(102);

        public final int id;

        DrawerItem(int id) {
            this.id = id;
        }

        public static DrawerItem getById(int id) {
            for (DrawerItem item : values()) {
                if (item.id == id) {
                    return item;
                }
            }
            return null;
        }
    }

    public static void loadConfig() {
        synchronized (sync) {
            if (configLoaded) {
                return;
            }
            Context context = ApplicationLoader.applicationContext;
            if (context == null) {
                return;
            }
            preferences = context.getSharedPreferences("exteraconfig", Context.MODE_PRIVATE);
            editor = preferences.edit();
            pluginsEngine = PluginsController.isPluginEngineSupported() && preferences.getBoolean("pluginsEngine", false);
            pluginsSafeMode = preferences.getBoolean("pluginsSafeMode", false);
            pluginsDevMode = preferences.getBoolean("pluginsDevMode", false);
            pluginsCompactView = preferences.getBoolean("pluginsCompactView", false);
            pluginsDisableArtOpts = preferences.getBoolean("pluginsDisableArtOpts", false);
            pluginsPySdkAutoUpdate = preferences.getBoolean("pluginsPySdkAutoUpdate", false);
            pluginsPySdkBetaVersions = preferences.getBoolean("pluginsPySdkBetaVersions", false);
            sdkUpdateScheduleTimestamp = preferences.getLong("sdkUpdateScheduleTimestamp", 0L);
            pinnedPlugins = new HashSet<>(preferences.getStringSet("pinnedPlugins", Collections.emptySet()));
            configLoaded = true;
        }
    }

    public static void reloadConfig() {
        synchronized (sync) {
            configLoaded = false;
        }
        loadConfig();
    }

    public static void init() {
        synchronized (sync) {
            if (initialized) {
                return;
            }
            initialized = true;
        }
        loadConfig();
        BadgesController.INSTANCE.init();
        PluginsController.getInstance().init(() -> PluginsController.getInstance().executeOnAppEvent(PluginsConstants.APP_START));
    }
}

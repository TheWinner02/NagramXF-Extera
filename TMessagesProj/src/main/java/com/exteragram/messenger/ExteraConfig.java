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

    public static boolean getInAppVibration() {
        return getPreferences().getBoolean("in_app_vibration", true);
    }

    public static boolean getPluginsEngine() {
        return getPreferences().getBoolean("plugins_engine", true);
    }

    public static void setPluginsEngine(boolean enabled) {
        getEditor().putBoolean("plugins_engine", enabled).apply();
    }

    public static boolean getPluginsSafeMode() {
        return getPreferences().getBoolean("plugins_safe_mode", false);
    }

    public static boolean getPluginsDevMode() {
        return getPreferences().getBoolean("plugins_dev_mode", false);
    }

    public static void setPluginsDevMode(boolean enabled) {
        getEditor().putBoolean("plugins_dev_mode", enabled).apply();
    }

    public static boolean getPluginsCompactView() {
        return getPreferences().getBoolean("plugins_compact_view", false);
    }

    public static void setPluginsCompactView(boolean enabled) {
        getEditor().putBoolean("plugins_compact_view", enabled).apply();
    }

    public static boolean getPluginsDisableArtOpts() {
        return getPreferences().getBoolean("plugins_disable_art_opts", false);
    }

    public static void setPluginsDisableArtOpts(boolean enabled) {
        getEditor().putBoolean("plugins_disable_art_opts", enabled).apply();
    }

    public static boolean getPluginsPySdkBetaVersions() {
        return getPreferences().getBoolean("plugins_pysdk_beta", false);
    }

    public static void setPluginsPySdkBetaVersions(boolean enabled) {
        getEditor().putBoolean("plugins_pysdk_beta", enabled).apply();
    }

    public static void setPluginsSafeMode(boolean enabled) {
        getEditor().putBoolean("plugins_safe_mode", enabled).apply();
    }

    public static java.util.Set<String> getPinnedPlugins() {
        return getPreferences().getStringSet("pinned_plugins", new java.util.HashSet<>());
    }

    public static void setPinnedPlugins(java.util.Set<String> plugins) {
        getEditor().putStringSet("pinned_plugins", plugins).apply();
    }

    public static long getSdkUpdateScheduleTimestamp() {
        return getPreferences().getLong("sdk_update_schedule_ts", 0L);
    }

    public static void setSdkUpdateScheduleTimestamp(long ts) {
        getEditor().putLong("sdk_update_schedule_ts", ts).apply();
    }

    public static boolean getPluginsPySdkAutoUpdate() {
        return getPreferences().getBoolean("plugins_pysdk_auto_update", true);
    }

    public static void setPluginsPySdkAutoUpdate(boolean enabled) {
        getEditor().putBoolean("plugins_pysdk_auto_update", enabled).apply();
    }

    public static int getSectionRadiusDp() {
        return 12;
    }

    public static android.util.Pair<Long, String> getApiBotInfo() {
        return new android.util.Pair<>(0L, "");
    }
}

package com.radolyn.ayugram;

import android.content.SharedPreferences;
import com.exteragram.messenger.backup.PreferencesUtils;
import org.telegram.tgnet.TLObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class AyuGhostExclusions {
    private static boolean configLoaded;
    public static SharedPreferences.Editor editor;
    public static SharedPreferences preferences;
    private static final Object sync = new Object();

    static {
        loadConfig();
    }

    public static void loadConfig() {
        synchronized (sync) {
            try {
                if (configLoaded) {
                    return;
                }
                SharedPreferences preferences2 = org.telegram.messenger.ApplicationLoader.applicationContext.getSharedPreferences("ayughostexclusionsconfig", 0);
                preferences = preferences2;
                editor = preferences2.edit();
                configLoaded = true;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static void reloadConfig() {
        configLoaded = false;
        loadConfig();
    }

    public static void saveReadException(Long l, Integer num) {
        editor.putInt("read_" + l, num.intValue()).apply();
    }

    public static void saveTypingException(Long l, Integer num) {
        editor.putInt("typing_" + l, num.intValue()).apply();
    }

    public static int getReadSettingsType(Long l) {
        return preferences.getInt("read_" + l, 0);
    }

    public static int getTypingSettingsType(Long l) {
        return preferences.getInt("typing_" + l, 0);
    }

    public static int getReadException(TLObject tLObject) {
        return getReadSettingsType(Long.valueOf(0L));
    }

    public static int getTypingException(TLObject tLObject) {
        return getTypingSettingsType(Long.valueOf(0L));
    }
}

package com.radolyn.ayugram;

import java.util.ArrayList;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class AyuConstants {
    public static final int FILTERS_UPDATED;
    public static final int FIX_FORWARD;
    public static final int FORCE_MESSAGES_UPDATE;
    public static final int HISTORY_FLUSHED_NOTIFICATION;
    public static final int LAST_SEEN_PILL_FETCH;
    public static final int LAST_SEEN_PILL_UPDATE;
    public static final int MESSAGES_DELETED_NOTIFICATION;
    private static int OPTIONS;
    public static final int OPTION_DEBUG_SEND_SCREENSHOT;
    public static final int OPTION_DELETED_HISTORY;
    public static final int OPTION_GHOST_READ_EXCLUSION;
    public static final int OPTION_GHOST_TYPING_EXCLUSION;
    public static final int OPTION_SWITCH_FILTERING;
    public static final int OPTION_VIEW_FILTERS;
    public static final int PEEK_ONLINE_ITEM;
    public static final int PEER_RESOLVED_NOTIFICATION;
    public static final int SHADOW_BAN_ITEM;
    public static final int UPDATE_CHAT_RESTRICTION;
    private static int notificationId;
    public static final String DEFAULT_JUMPSCARES_CHANNEL = "default";
    public static final String AYU_DATABASE = "ayugram.db";
    public static final String AYU_DATABASE_EXPORT = "ayugram.db";
    public static final int DELETED_MEDIA_LOADED_NOTIFICATION = 29481;
    public static String APP_NAME = "NegramXFE";
    public static final String UPDATES_CHANNEL_USERNAME = "default";
    public static final int OPTION_CLEAR_DELETED = 80;
    public static String BUILD_STORE_PACKAGE = "default";
    public static int MAX_CACHE_SIZE_300_MB = -10;
    public static final ArrayList DEFAULT_JUMPSCARES_KEYS = new ArrayList() { // from class: com.radolyn.ayugram.AyuConstants.1
        {
            add("default");
            add("default");
            add("default");
            add("default");
            add("default");
            add("default");
            add("default");
        }
    };
    public static final ArrayList DEFAULT_JUMPSCARES_VALUES = new ArrayList() { // from class: com.radolyn.ayugram.AyuConstants.2
        {
            add(String.valueOf(5));
            add(String.valueOf(6));
            add(String.valueOf(7));
            add(String.valueOf(8));
            add(String.valueOf(9));
            add(String.valueOf(10));
            add(String.valueOf(11));
        }
    };
    public static final Set ALLOWED_PASTE_SERVICES = new java.util.HashSet();
    public static final int FIX_SCHEDULED_BAR = 6969;

    static {
        int i = 80 + 1;
        OPTION_VIEW_FILTERS = i;
        OPTION_SWITCH_FILTERING = i + 1;
        PEEK_ONLINE_ITEM = i + 2;
        SHADOW_BAN_ITEM = i + 3;
        OPTION_DELETED_HISTORY = i + 4;
        OPTION_GHOST_READ_EXCLUSION = i + 5;
        OPTION_GHOST_TYPING_EXCLUSION = i + 6;
        OPTIONS = i + 8;
        OPTION_DEBUG_SEND_SCREENSHOT = i + 7;
        int i2 = 6969 + 1;
        FIX_FORWARD = i2;
        MESSAGES_DELETED_NOTIFICATION = i2 + 1;
        HISTORY_FLUSHED_NOTIFICATION = i2 + 2;
        PEER_RESOLVED_NOTIFICATION = i2 + 3;
        UPDATE_CHAT_RESTRICTION = i2 + 4;
        FORCE_MESSAGES_UPDATE = i2 + 5;
        LAST_SEEN_PILL_UPDATE = i2 + 6;
        LAST_SEEN_PILL_FETCH = i2 + 7;
        notificationId = i2 + 9;
        FILTERS_UPDATED = i2 + 8;
    }
}

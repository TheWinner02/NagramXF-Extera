package com.radolyn.ayugram.utils.filters;

import java.util.HashSet;
import java.util.Set;
import org.telegram.ui.ActionBar.BaseFragment;

public class AyuFilterUtils {
    private static final Set<Long> shadowBannedUsers = new HashSet<>();

    public static boolean isShadowBanned(long userId) {
        return shadowBannedUsers.contains(userId);
    }

    public static void toggleShadowBan(long userId) {
        if (shadowBannedUsers.contains(userId)) {
            shadowBannedUsers.remove(userId);
        } else {
            shadowBannedUsers.add(userId);
        }
    }

    public static void fetchFilters(BaseFragment fragment, String url, Object listener) {
    }

    public static void importFilters(BaseFragment fragment, String json) {
    }
}

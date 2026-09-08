package xyz.nextalone.nagram.ui;

import android.os.Build;

import androidx.annotation.ColorInt;

import org.telegram.ui.ActionBar.Theme;

import tw.nekomimi.nekogram.helpers.MonetHelper;

public class M3ColorRoles {

    public enum Role {
        PRIMARY,
        ON_PRIMARY,
        PRIMARY_CONTAINER,
        ON_PRIMARY_CONTAINER,
        SECONDARY,
        ON_SECONDARY,
        SECONDARY_CONTAINER,
        ON_SECONDARY_CONTAINER,
        TERTIARY,
        ON_TERTIARY,
        TERTIARY_CONTAINER,
        ON_TERTIARY_CONTAINER,
        ERROR,
        ON_ERROR,
        ERROR_CONTAINER,
        ON_ERROR_CONTAINER,
        BACKGROUND,
        ON_BACKGROUND,
        SURFACE,
        ON_SURFACE,
        SURFACE_VARIANT,
        ON_SURFACE_VARIANT,
        SURFACE_CONTAINER_LOWEST,
        SURFACE_CONTAINER_LOW,
        SURFACE_CONTAINER,
        SURFACE_CONTAINER_HIGH,
        SURFACE_CONTAINER_HIGHEST,
        OUTLINE,
        OUTLINE_VARIANT,
        INVERSE_SURFACE,
        INVERSE_ON_SURFACE,
        INVERSE_PRIMARY
    }

    @ColorInt
    public static int get(Role role, @ColorInt int fallbackColor) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || !MonetHelper.useMonetMd3Colors()) {
            return fallbackColor;
        }
        return MonetHelper.getColor(getToken(role));
    }

    @ColorInt
    public static int surfaceContainer(@ColorInt int fallbackColor) {
        return get(Role.SURFACE_CONTAINER, fallbackColor);
    }

    @ColorInt
    public static int surfaceContainerHigh(@ColorInt int fallbackColor) {
        return get(Role.SURFACE_CONTAINER_HIGH, fallbackColor);
    }

    @ColorInt
    public static int primary(@ColorInt int fallbackColor) {
        return get(Role.PRIMARY, fallbackColor);
    }

    @ColorInt
    public static int primaryContainer(@ColorInt int fallbackColor) {
        return get(Role.PRIMARY_CONTAINER, fallbackColor);
    }

    @ColorInt
    public static int onSurface(@ColorInt int fallbackColor) {
        return get(Role.ON_SURFACE, fallbackColor);
    }

    private static String getToken(Role role) {
        Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
        boolean dark = activeTheme != null && activeTheme.isMonetNight();
        boolean amoled = activeTheme != null && activeTheme.isMonetAmoled();
        if (amoled) {
            return getAmoledToken(role);
        }
        return dark ? getDarkToken(role) : getLightToken(role);
    }

    private static String getLightToken(Role role) {
        switch (role) {
            case PRIMARY: return "a1_600";
            case ON_PRIMARY: return "a1_0";
            case PRIMARY_CONTAINER: return "a1_100";
            case ON_PRIMARY_CONTAINER: return "a1_900";
            case SECONDARY: return "a2_600";
            case ON_SECONDARY: return "a2_0";
            case SECONDARY_CONTAINER: return "a2_100";
            case ON_SECONDARY_CONTAINER: return "a2_900";
            case TERTIARY: return "a3_600";
            case ON_TERTIARY: return "a3_0";
            case TERTIARY_CONTAINER: return "a3_100";
            case ON_TERTIARY_CONTAINER: return "a3_900";
            case ERROR: return "monetRedLight";
            case ON_ERROR: return "n1_0";
            case ERROR_CONTAINER: return "monetRedLight (a=24)";
            case ON_ERROR_CONTAINER: return "monetRedLight";
            case BACKGROUND:
            case SURFACE: return "n1_10";
            case ON_BACKGROUND:
            case ON_SURFACE: return "n1_900";
            case SURFACE_VARIANT: return "n2_100";
            case ON_SURFACE_VARIANT: return "n2_700";
            case SURFACE_CONTAINER_LOWEST: return "n1_0";
            case SURFACE_CONTAINER_LOW: return "n1_50";
            case SURFACE_CONTAINER: return "n1_100";
            case SURFACE_CONTAINER_HIGH: return "n1_200";
            case SURFACE_CONTAINER_HIGHEST: return "n1_300";
            case OUTLINE: return "n2_500";
            case OUTLINE_VARIANT: return "n2_200";
            case INVERSE_SURFACE: return "n1_800";
            case INVERSE_ON_SURFACE: return "n1_50";
            case INVERSE_PRIMARY: return "a1_200";
        }
        return "n1_10";
    }

    private static String getDarkToken(Role role) {
        switch (role) {
            case PRIMARY: return "a1_200";
            case ON_PRIMARY: return "a1_800";
            case PRIMARY_CONTAINER: return "a1_700";
            case ON_PRIMARY_CONTAINER: return "a1_100";
            case SECONDARY: return "a2_200";
            case ON_SECONDARY: return "a2_800";
            case SECONDARY_CONTAINER: return "a2_700";
            case ON_SECONDARY_CONTAINER: return "a2_100";
            case TERTIARY: return "a3_200";
            case ON_TERTIARY: return "a3_800";
            case TERTIARY_CONTAINER: return "a3_700";
            case ON_TERTIARY_CONTAINER: return "a3_100";
            case ERROR: return "monetRedDark";
            case ON_ERROR: return "n1_1000";
            case ERROR_CONTAINER: return "monetRedDark (a=36)";
            case ON_ERROR_CONTAINER: return "monetRedDark";
            case BACKGROUND:
            case SURFACE: return "n1_900";
            case ON_BACKGROUND:
            case ON_SURFACE: return "n1_100";
            case SURFACE_VARIANT: return "n2_700";
            case ON_SURFACE_VARIANT: return "n2_200";
            case SURFACE_CONTAINER_LOWEST: return "n1_1000";
            case SURFACE_CONTAINER_LOW: return "n1_900";
            case SURFACE_CONTAINER: return "n1_800";
            case SURFACE_CONTAINER_HIGH: return "n1_700";
            case SURFACE_CONTAINER_HIGHEST: return "n1_600";
            case OUTLINE: return "n2_400";
            case OUTLINE_VARIANT: return "n2_700";
            case INVERSE_SURFACE: return "n1_100";
            case INVERSE_ON_SURFACE: return "n1_800";
            case INVERSE_PRIMARY: return "a1_600";
        }
        return "n1_900";
    }

    private static String getAmoledToken(Role role) {
        switch (role) {
            case BACKGROUND:
            case SURFACE:
            case SURFACE_CONTAINER_LOWEST: return "n1_1000";
            case SURFACE_CONTAINER_LOW: return "n1_1000";
            case SURFACE_CONTAINER: return "n1_900";
            case SURFACE_CONTAINER_HIGH: return "n1_800";
            case SURFACE_CONTAINER_HIGHEST: return "n1_700";
            default: return getDarkToken(role);
        }
    }
}

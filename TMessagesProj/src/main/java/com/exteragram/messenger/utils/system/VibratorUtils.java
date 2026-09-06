package com.exteragram.messenger.utils.system;

import android.os.Build;
import android.view.HapticFeedbackConstants;
import android.view.View;
import tw.nekomimi.nekogram.NekoConfig;

public final class VibratorUtils {
    private VibratorUtils() {
    }

    public static int getType(int type) {
        return type;
    }

    public static void vibrateClick(View view) {
        if (view == null || NekoConfig.disableVibration.Bool()) return;
        try {
            int feedback = (Build.VERSION.SDK_INT >= 30) ? 16 /* HapticFeedbackConstants.CONFIRM */ : HapticFeedbackConstants.KEYBOARD_TAP;
            view.performHapticFeedback(feedback, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
        } catch (Exception ignore) {}
    }

    public static void vibrateToggle(View view, boolean on) {
        if (view == null || NekoConfig.disableVibration.Bool()) return;
        try {
            int feedback;
            if (Build.VERSION.SDK_INT >= 34) {
                feedback = on ? 21 /* TOGGLE_ON */ : 22 /* TOGGLE_OFF */;
            } else if (Build.VERSION.SDK_INT >= 30) {
                feedback = 16 /* CONFIRM */;
            } else {
                feedback = HapticFeedbackConstants.KEYBOARD_TAP;
            }
            view.performHapticFeedback(feedback, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
        } catch (Exception ignore) {}
    }

    public static void vibrateSegment(View view) {
        if (view == null || NekoConfig.disableVibration.Bool()) return;
        try {
            int feedback;
            if (Build.VERSION.SDK_INT >= 34) {
                feedback = 26 /* SEGMENT_TICK */;
            } else if (Build.VERSION.SDK_INT >= 21) {
                feedback = HapticFeedbackConstants.CLOCK_TICK;
            } else {
                feedback = HapticFeedbackConstants.KEYBOARD_TAP;
            }
            view.performHapticFeedback(feedback, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
        } catch (Exception ignore) {}
    }

    public static void vibrateGesture(View view, boolean start) {
        if (view == null || NekoConfig.disableVibration.Bool()) return;
        try {
            int feedback;
            if (Build.VERSION.SDK_INT >= 30) {
                feedback = start ? 12 /* GESTURE_START */ : 13 /* GESTURE_END */;
            } else {
                feedback = HapticFeedbackConstants.KEYBOARD_TAP;
            }
            view.performHapticFeedback(feedback, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
        } catch (Exception ignore) {}
    }

    public static void vibrateLongPress(View view) {
        if (view == null || NekoConfig.disableVibration.Bool()) return;
        try {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
        } catch (Exception ignore) {}
    }
}

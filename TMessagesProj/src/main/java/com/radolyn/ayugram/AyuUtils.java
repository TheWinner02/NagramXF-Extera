package com.radolyn.ayugram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Process;
import android.text.Html;
import android.text.SpannableString;
import android.util.LongSparseArray;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: loaded from: classes.dex */
public class AyuUtils {
    public static int getMinRealId(ArrayList<org.telegram.messenger.MessageObject> messages) {
        if (messages == null || messages.isEmpty()) return 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < messages.size(); i++) {
            org.telegram.messenger.MessageObject obj = messages.get(i);
            if (obj != null && obj.getId() < min) {
                min = obj.getId();
            }
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    public static String generateRandomString(int i) {
        return Utilities.generateRandomString(i).toLowerCase();
    }

    public static String getPackageName() {
        return ApplicationLoader.applicationContext.getPackageName();
    }

    public static void restartApp(Context context) {
        if (context == null) {
            context = ApplicationLoader.applicationContext;
        }
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(getPackageName());
        if (launchIntentForPackage != null) {
            launchIntentForPackage.addFlags(268468224);
            context.startActivity(launchIntentForPackage);
        }
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    public static String getFormattedBuildDate() {
        try {
            return LocalDate.parse("20260814", DateTimeFormatter.ofPattern("yyyyMMdd")).format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        } catch (Exception e) {
            return "2026.08.14";
        }
    }

    public static CharSequence fromHtml(String str) {
        SpannableString spannableString = new SpannableString(Html.fromHtml(str));
        return spannableString;
    }

    public static void showRestartNotification(BaseFragment baseFragment) {
    }

    public static void logError(String str, Throwable th) {
        FileLog.e(str, th);
    }
}

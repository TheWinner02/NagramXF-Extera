package com.radolyn.ayugram.utils;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.util.Pair;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuState;
import java.io.File;
import java.util.ArrayList;
import kotlin.Triple;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessageObject;

public abstract class AyuMessageUtils {
    public static int getMinRealId(ArrayList<MessageObject> messages) {
        if (messages == null || messages.isEmpty()) return 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < messages.size(); i++) {
            MessageObject obj = messages.get(i);
            if (obj != null && obj.getId() < min) {
                min = obj.getId();
            }
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }
    public static int[] deletedColors = {-16777216, -2349530, -2414729, -4184365, -7130134, -11581723, -14326805};

    public static void saveDownloadedMedia(File file) {
    }

    public static boolean isExpiredDocument(MessageObject message) {
        return false;
    }

    public static boolean isExpiredPhoto(MessageObject message) {
        return false;
    }

    public static File decryptAndSaveMedia(String name, File file, MessageObject message) {
        return file;
    }

    public static boolean shouldSaveMedia(int currentAccount, long dialogId) {
        return false;
    }

    public static File findExistingFileByBaseNameFast(String fileName) {
        return null;
    }

    public static void map(Object base, Object message, int accountId) {
    }

    public static void mapMedia(Object base, Object message, int accountId) {
    }

    public static CharSequence getDeletedIcon() {
        return new SpannableStringBuilder();
    }

    public static int getDeletedIconWidth() {
        return 0;
    }

    public static Drawable getDeletedIconPreviewDrawable() {
        return null;
    }
}

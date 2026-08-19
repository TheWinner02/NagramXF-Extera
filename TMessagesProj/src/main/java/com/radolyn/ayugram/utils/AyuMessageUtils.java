package com.radolyn.ayugram.utils;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Pair;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.AyuState;
import java.io.File;
import java.util.ArrayList;
import kotlin.Triple;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC;

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

    public static boolean isMediaDownloadable(MessageObject messageObject, boolean onlyVisual) {
        if (messageObject == null || messageObject.messageOwner == null || messageObject.messageOwner.media == null || (messageObject.messageOwner.media.photo instanceof TLRPC.TL_photoEmpty) || (messageObject.messageOwner.media.document instanceof TLRPC.TL_documentEmpty) || MessageObject.isMediaEmpty(messageObject.messageOwner)) {
            return false;
        }
        TLRPC.MessageMedia messageMedia = messageObject.messageOwner.media;
        if ((messageMedia instanceof TLRPC.TL_messageMediaPaidMedia) && ((TLRPC.TL_messageMediaPaidMedia) messageMedia).stars_amount != 0) {
            return true;
        }
        boolean visual = (messageObject.isSecretMedia() && !messageObject.isVoice()) || messageObject.isGif() || messageObject.isNewGif() || messageObject.isRoundVideo() || messageObject.isVideo() || messageObject.isPhoto() || messageObject.isSticker() || messageObject.isAnimatedSticker();
        if (onlyVisual || visual) {
            return visual;
        }
        return messageObject.isDocument() || messageObject.isMusic() || messageObject.isVoice();
    }

    public static long getMessageSize(MessageObject messageObject) {
        if (messageObject == null) return 0;
        if (messageObject.getDocument() != null) {
            return messageObject.getDocument().size;
        }
        if (messageObject.photoThumbs != null && !messageObject.photoThumbs.isEmpty()) {
            TLRPC.PhotoSize size = messageObject.photoThumbs.get(messageObject.photoThumbs.size() - 1);
            if (size != null) return size.size;
        }
        return 0;
    }

    public static CharSequence extractAllText(MessageObject messageObject, MessageObject.GroupedMessages groupedMessages) {
        if (messageObject == null || messageObject.messageOwner == null) return "";
        StringBuilder sb = new StringBuilder();
        if (messageObject.messageText != null) {
            sb.append(messageObject.messageText);
        } else if (messageObject.caption != null) {
            sb.append(messageObject.caption);
        }
        if (messageObject.messageOwner.entities != null) {
            for (int i = 0; i < messageObject.messageOwner.entities.size(); i++) {
                TLRPC.MessageEntity entity = messageObject.messageOwner.entities.get(i);
                if (entity instanceof TLRPC.TL_messageEntityTextUrl && entity.url != null) {
                    sb.append("\n").append(entity.url);
                }
            }
        }
        return sb.toString();
    }

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

package com.radolyn.ayugram.utils;

import android.graphics.BitmapFactory;
import android.text.TextUtils;
import androidx.core.util.Pair;
import com.radolyn.ayugram.AyuUtils;
import java.io.File;
import java.util.ArrayList;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

/* JADX INFO: loaded from: classes.dex */
public abstract class AyuFileUtils {
    public static boolean moveOrCopyFile(File file, File file2, boolean z) {
        boolean zRenameTo;
        if (z) {
            zRenameTo = false;
        } else {
            try {
                zRenameTo = file.renameTo(file2);
            } catch (Throwable th) {
                FileLog.e(th);
                zRenameTo = false;
            }
        }
        if (zRenameTo) {
            return zRenameTo;
        }
        try {
            return AndroidUtilities.copyFile(file, file2);
        } catch (Throwable th2) {
            FileLog.e(th2);
            return zRenameTo;
        }
    }

    public static String removeExtension(String str) {
        int iLastIndexOf;
        return (TextUtils.isEmpty(str) || (iLastIndexOf = str.lastIndexOf(46)) == -1) ? str : str.substring(0, iLastIndexOf);
    }

    public static String getExtension(String str) {
        int iLastIndexOf;
        return (TextUtils.isEmpty(str) || (iLastIndexOf = str.lastIndexOf(46)) == -1) ? _UrlKt.FRAGMENT_ENCODE_SET : str.substring(iLastIndexOf);
    }

    public static String getFilename(TLObject tLObject) {
        ArrayList arrayList;
        TLRPC.PhotoSize closestPhotoSizeWithSize;
        TLRPC.MessageMedia messageMedia;
        boolean z = tLObject instanceof TLRPC.Message;
        String documentFileName = (!z || (messageMedia = ((TLRPC.Message) tLObject).media) == null) ? null : FileLoader.getDocumentFileName(messageMedia.document);
        if (tLObject instanceof TLRPC.Document) {
            documentFileName = FileLoader.getDocumentFileName((TLRPC.Document) tLObject);
        }
        if (tLObject instanceof TLRPC.PhotoSize) {
            documentFileName = FileLoader.getAttachFileName(tLObject);
        }
        if (TextUtils.isEmpty(documentFileName) && z) {
            documentFileName = FileLoader.getMessageFileName((TLRPC.Message) tLObject);
        }
        if (TextUtils.isEmpty(documentFileName)) {
            documentFileName = "unnamed";
        }
        String strRemoveExtension = removeExtension(documentFileName);
        if (z) {
            TLRPC.Message message = (TLRPC.Message) tLObject;
            TLRPC.MessageMedia messageMedia2 = message.media;
            if ((messageMedia2 instanceof TLRPC.TL_messageMediaPhoto) && (arrayList = messageMedia2.photo.sizes) != null && !arrayList.isEmpty() && (closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(message.media.photo.sizes, AndroidUtilities.getPhotoSize(true))) != null) {
                strRemoveExtension = strRemoveExtension + "#" + closestPhotoSizeWithSize.w + "x" + closestPhotoSizeWithSize.h;
            }
        }
        return (strRemoveExtension + "@" + AyuUtils.generateRandomString(6)) + getExtension(documentFileName);
    }

    public static String getReadableFilename(String str) {
        String extension = getExtension(str);
        int iLastIndexOf = str.lastIndexOf("@");
        if (iLastIndexOf == -1) {
            return str;
        }
        return str.substring(0, iLastIndexOf) + extension;
    }

    public static Pair extractImageSizeFromName(String str) {
        int iLastIndexOf;
        int iLastIndexOf2 = str.lastIndexOf("#") + 1;
        if (iLastIndexOf2 == 0 || (iLastIndexOf = str.lastIndexOf("@")) == -1) {
            return null;
        }
        try {
            String[] strArrSplit = str.substring(iLastIndexOf2, iLastIndexOf).split("x");
            return new Pair(Integer.valueOf(Integer.parseInt(strArrSplit[0])), Integer.valueOf(Integer.parseInt(strArrSplit[1])));
        } catch (Exception unused) {
            return null;
        }
    }

    public static Pair extractImageSizeFromFile(String str) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            return new Pair(Integer.valueOf(options.outWidth), Integer.valueOf(options.outHeight));
        } catch (Exception unused) {
            return null;
        }
    }
}

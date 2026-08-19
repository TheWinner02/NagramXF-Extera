package com.radolyn.ayugram.controllers;

import android.text.TextUtils;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.controllers.messages.SaveMessageRequest;
import com.radolyn.ayugram.utils.AyuFileUtils;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import java.io.File;
import java.io.FileOutputStream;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.secretmedia.EncryptedFileInputStream;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class AyuAttachments extends BaseController {
    private static final AyuAttachments[] Instance = new AyuAttachments[16];

    public AyuAttachments(int account) {
        super(account);
    }

    public static AyuAttachments getInstance(int account) {
        AyuAttachments ayuAttachments;
        AyuAttachments[] ayuAttachmentsArr = Instance;
        AyuAttachments ayuAttachments2 = ayuAttachmentsArr[account];
        if (ayuAttachments2 != null) {
            return ayuAttachments2;
        }
        synchronized (AyuAttachments.class) {
            ayuAttachments = ayuAttachmentsArr[account];
            if (ayuAttachments == null) {
                ayuAttachments = new AyuAttachments(account);
                ayuAttachmentsArr[account] = ayuAttachments;
            }
        }
        return ayuAttachments;
    }

    public String getExistingPath(TLRPC.Message message, boolean downloadIfMissing) {
        return getExistingPath(new MessageObject(this.currentAccount, message, false, true), downloadIfMissing);
    }

    public String getExistingPath(MessageObject messageObject, boolean downloadIfMissing) {
        if (messageObject == null || messageObject.messageOwner == null) {
            return "/";
        }
        FileLoader fileLoader = FileLoader.getInstance(messageObject.currentAccount);
        long messageSize = AyuMessageUtils.getMessageSize(messageObject);
        String path = messageObject.messageOwner.attachPath;

        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (!file.exists() || (messageSize > 0 && file.length() != messageSize) || file.isDirectory()) {
                path = null;
            }
        }
        if (TextUtils.isEmpty(path)) {
            File msgFile = fileLoader.getPathToMessage(messageObject.messageOwner);
            if (msgFile != null) {
                String strPath = msgFile.toString();
                File f = new File(strPath);
                if (f.exists() && (messageSize == 0 || f.length() == messageSize) && !f.isDirectory()) {
                    path = strPath;
                }
                if (!TextUtils.isEmpty(strPath)) {
                    File encryptedFile = tryEncrypted(f, new File(strPath), !downloadIfMissing);
                    if (encryptedFile != null && !encryptedFile.getAbsolutePath().equals("/")) {
                        return encryptedFile.getAbsolutePath();
                    }
                }
            }
        }
        if (TextUtils.isEmpty(path) && messageObject.getDocument() != null) {
            File docFile = fileLoader.getPathToAttach(messageObject.getDocument(), null, false);
            if (docFile != null) {
                String docPath = docFile.toString();
                File f = new File(docPath);
                if (f.exists() && (messageSize == 0 || f.length() == messageSize) && !f.isDirectory()) {
                    path = docPath;
                }
            }
        }
        if (TextUtils.isEmpty(path) && messageObject.getDocument() != null) {
            File docFileEncrypted = fileLoader.getPathToAttach(messageObject.getDocument(), null, true);
            if (docFileEncrypted != null) {
                String docPathEncrypted = docFileEncrypted.toString();
                File f = new File(docPathEncrypted);
                if (f.exists() && (messageSize == 0 || f.length() == messageSize) && !f.isDirectory()) {
                    path = docPathEncrypted;
                }
            }
        }
        if (TextUtils.isEmpty(path) && messageObject.messageOwner.media != null && messageObject.messageOwner.media.photo != null) {
            File photoFile = fileLoader.getPathToAttach(messageObject.messageOwner.media.photo, null, false);
            if (photoFile != null) {
                String photoPath = photoFile.toString();
                File f = new File(photoPath);
                if (f.exists() && !f.isDirectory()) {
                    path = photoPath;
                }
            }
        }
        if (TextUtils.isEmpty(path) && messageObject.messageOwner.media != null && messageObject.messageOwner.media.photo != null) {
            File photoFileEnc = fileLoader.getPathToAttach(messageObject.messageOwner.media.photo, null, true);
            if (photoFileEnc != null) {
                String photoPathEnc = photoFileEnc.toString();
                File f = new File(photoPathEnc);
                if (f.exists() && !f.isDirectory()) {
                    path = photoPathEnc;
                }
            }
        }
        if (TextUtils.isEmpty(path) || new File(path).isDirectory()) {
            File fallbackMsgFile = fileLoader.getPathToMessage(messageObject.messageOwner);
            if (fallbackMsgFile != null) {
                path = fallbackMsgFile.toString();
            }
        }
        if (TextUtils.isEmpty(path) || new File(path).isDirectory()) {
            File fallbackDocFile = fileLoader.getPathToAttach(messageObject.getDocument(), null, false);
            if (fallbackDocFile != null) {
                path = fallbackDocFile.toString();
            }
        }
        return (TextUtils.isEmpty(path) || new File(path).isDirectory()) ? "/" : path;
    }

    public String getExistingPath(MessageObject messageObject) {
        return getExistingPath(messageObject, true);
    }

    public String getExistingPathPhoto(TLRPC.TL_photoSize photoSize) {
        if (photoSize == null) return "/";
        FileLoader fileLoader = FileLoader.getInstance(this.currentAccount);
        File file = fileLoader.getPathToAttach(photoSize, null, false, false);
        if (file != null && file.exists()) {
            return file.getAbsolutePath();
        }
        File fileEncrypted = fileLoader.getPathToAttach(photoSize, null, true, false);
        return fileEncrypted != null ? fileEncrypted.getAbsolutePath() : "/";
    }

    public File processThumb(TLRPC.TL_photoSize photoSize) {
        if (photoSize == null) return new File("/");
        File source = new File(getExistingPathPhoto(photoSize));
        File target = new File(AyuConfig.getSavePathJava(), AyuFileUtils.getFilename(photoSize));
        return copyFile(source, target, false);
    }

    public File processAttachment(SaveMessageRequest request) {
        if (request == null || request.getMessage() == null) return new File("/");
        TLRPC.Message message = request.getMessage();
        MessageObject messageObject = new MessageObject(this.currentAccount, message, false, true);
        File source = new File(getExistingPath(messageObject));
        File target = new File(AyuConfig.getSavePathJava(), AyuFileUtils.getFilename(message));
        return copyFile(source, target, request.isForce());
    }

    private File copyFile(File source, File target, boolean force) {
        File emptyMarker = new File("/");
        if (source.exists() && !source.isDirectory()) {
            boolean success = false;
            try {
                success = AyuFileUtils.moveOrCopyFile(source, target, force);
            } catch (Throwable unused) {
            }
            return success ? target : emptyMarker;
        }
        return tryEncrypted(source, target, false);
    }

    private File tryEncrypted(File source, File target, boolean limit) {
        File emptyMarker = new File("/");
        if (source == null) return emptyMarker;
        File encFile = new File(FileLoader.getDirectory(4), source.getName() + ".enc");
        if (!encFile.exists() || (limit && encFile.length() > 8388608)) {
            return emptyMarker;
        }
        File keyFile = new File(FileLoader.getInternalCacheDir(), encFile.getName() + ".key");
        if (keyFile.exists() && encFile.length() > 0 && keyFile.length() > 0) {
            try (EncryptedFileInputStream encryptedFileInputStream = new EncryptedFileInputStream(encFile, keyFile);
                 FileOutputStream fileOutputStream = new FileOutputStream(target)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = encryptedFileInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }
                return target;
            } catch (Exception unused) {
            }
        }
        return emptyMarker;
    }

    public boolean shouldSaveMedia(SaveMessageRequest request) {
        if (!AyuConfig.saveMedia || request == null || request.getMessage() == null || request.getMessage().media == null) {
            return false;
        }
        long dialogId = request.getDialogId();
        if (DialogObject.isUserDialog(dialogId)) {
            return AyuConfig.saveMediaInPrivateChats;
        }
        TLRPC.Chat chat = getMessagesController().getChat(Long.valueOf(Math.abs(dialogId)));
        if (chat == null) {
            return true;
        }
        boolean isPublic = ChatObject.isPublic(chat);
        if (ChatObject.isChannel(chat)) {
            if (isPublic && AyuConfig.saveMediaInPublicChannels) {
                return true;
            }
            return !isPublic && AyuConfig.saveMediaInPrivateChannels;
        }
        if (isPublic && AyuConfig.saveMediaInPublicGroups) {
            return true;
        }
        return !isPublic && AyuConfig.saveMediaInPrivateGroups;
    }
}

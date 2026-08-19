package com.radolyn.ayugram.controllers;

import android.text.TextUtils;
import androidx.core.util.Pair;
import com.radolyn.ayugram.controllers.messages.SaveMessageRequest;
import com.radolyn.ayugram.database.entities.AyuMessageBase;
import com.radolyn.ayugram.database.entities.DeletedDialog;
import com.radolyn.ayugram.utils.AyuFileLocation;
import com.radolyn.ayugram.utils.AyuFileUtils;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.telegram.messenger.BaseController;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public class AyuMapper extends BaseController {
    private static final AyuMapper[] Instance = new AyuMapper[16];

    public interface TLDeserializer<T> {
        T deserialize(NativeByteBuffer buffer);
    }

    public AyuMapper(int account) {
        super(account);
    }

    public static AyuMapper getInstance(int account) {
        AyuMapper ayuMapper;
        AyuMapper[] ayuMapperArr = Instance;
        AyuMapper ayuMapper2 = ayuMapperArr[account];
        if (ayuMapper2 != null) {
            return ayuMapper2;
        }
        synchronized (AyuMapper.class) {
            ayuMapper = ayuMapperArr[account];
            if (ayuMapper == null) {
                ayuMapper = new AyuMapper(account);
                ayuMapperArr[account] = ayuMapper;
            }
        }
        return ayuMapper;
    }

    public static <T extends TLObject> ArrayList<T> deserializeMultiple(byte[] bytes, TLDeserializer<T> deserializer) {
        if (bytes == null || bytes.length == 0) {
            return new ArrayList<>();
        }
        NativeByteBuffer nativeByteBuffer = null;
        try {
            nativeByteBuffer = new NativeByteBuffer(bytes.length);
            nativeByteBuffer.buffer.put(bytes);
            nativeByteBuffer.rewind();
            ArrayList<T> list = new ArrayList<>();
            while (nativeByteBuffer.buffer.position() < nativeByteBuffer.buffer.limit()) {
                T obj = deserializer.deserialize(nativeByteBuffer);
                if (obj != null) {
                    list.add(obj);
                }
            }
            nativeByteBuffer.reuse();
            return list;
        } catch (Throwable unused) {
            if (nativeByteBuffer != null) {
                nativeByteBuffer.reuse();
            }
            return new ArrayList<>();
        }
    }

    public static <T extends TLObject> T deserializeSingle(byte[] bytes, TLDeserializer<T> deserializer) {
        ArrayList<T> list = deserializeMultiple(bytes, deserializer);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static byte[] serializeMultiple(ArrayList<? extends TLObject> list) {
        if (list == null || list.isEmpty()) {
            return new byte[0];
        }
        int totalSize = 0;
        for (int i = 0; i < list.size(); i++) {
            TLObject obj = list.get(i);
            if (obj != null) {
                totalSize += obj.getObjectSize();
            }
        }
        NativeByteBuffer nativeByteBuffer = null;
        try {
            nativeByteBuffer = new NativeByteBuffer(totalSize);
            for (int i = 0; i < list.size(); i++) {
                TLObject obj = list.get(i);
                if (obj != null) {
                    obj.serializeToStream(nativeByteBuffer);
                }
            }
            nativeByteBuffer.rewind();
            byte[] bytes = new byte[nativeByteBuffer.remaining()];
            nativeByteBuffer.buffer.get(bytes);
            nativeByteBuffer.reuse();
            return bytes;
        } catch (Throwable unused) {
            if (nativeByteBuffer != null) {
                nativeByteBuffer.reuse();
            }
            return null;
        }
    }

    public static byte[] serializeSingle(TLObject entity) {
        if (entity == null) {
            return new byte[0];
        }
        ArrayList<TLObject> list = new ArrayList<>();
        list.add(entity);
        return serializeMultiple(list);
    }

    public void map(SaveMessageRequest request, AyuMessageBase entity) {
        TLRPC.Message message = request.getMessage();
        entity.userId = getUserConfig().getClientUserId();
        entity.dialogId = request.getDialogId();
        entity.groupedId = message.grouped_id;
        entity.peerId = MessageObject.getPeerId(message.peer_id);
        entity.fromId = MessageObject.getPeerId(message.from_id);
        long topicId = request.getTopicId();
        entity.topicId = topicId;
        if (topicId == 0 && getMessagesController().isMonoForum(request.getDialogId())) {
            entity.topicId = request.getMonoForumTopicId();
        }
        entity.messageId = message.id;
        entity.date = message.date;
        int flags = message.flags;
        entity.flags = flags;
        if (message.unread) {
            entity.flags = flags | 1;
        }
        entity.editDate = message.edit_date;
        entity.views = message.views;
        TLRPC.MessageFwdHeader fwdHeader = message.fwd_from;
        if (fwdHeader != null) {
            entity.fwdFlags = fwdHeader.flags;
            entity.fwdFromId = MessageObject.getPeerId(fwdHeader.from_id);
            entity.fwdName = fwdHeader.from_name;
            entity.fwdDate = fwdHeader.date;
            entity.fwdPostAuthor = fwdHeader.post_author;
        }
        TLRPC.MessageReplyHeader replyHeader = message.reply_to;
        if (replyHeader != null) {
            entity.replySerialized = serializeSingle(replyHeader);
        }
        TLRPC.ReplyMarkup replyMarkup = message.reply_markup;
        if (replyMarkup != null) {
            entity.replyMarkupSerialized = serializeSingle(replyMarkup);
        }
        entity.postAuthor = message.post_author;
        entity.entityCreateDate = request.getRequestCatchTime();
        entity.text = message.message;
        entity.textEntities = serializeMultiple(message.entities);
    }

    public void mapMedia(SaveMessageRequest request, AyuMessageBase entity, boolean isNew) {
        TLRPC.Message message = request.getMessage();
        if (AyuMessageUtils.isMediaDownloadable(new MessageObject(this.currentAccount, message, false, true), false) && AyuAttachments.getInstance(this.currentAccount).shouldSaveMedia(request)) {
            TLRPC.MessageMedia media = message.media;
            String path = null;
            if (media == null) {
                entity.documentType = 0;
            } else if ((media instanceof TLRPC.TL_messageMediaPhoto) && media.photo != null) {
                entity.documentType = 1;
            } else if ((media instanceof TLRPC.TL_messageMediaDocument) && media.document != null && (MessageObject.isStickerMessage(message) || "application/x-tgsticker".equals(media.document.mime_type))) {
                entity.documentType = 2;
                entity.mimeType = media.document.mime_type;
                NativeByteBuffer nativeByteBuffer = null;
                try {
                    nativeByteBuffer = new NativeByteBuffer(media.getObjectSize());
                    media.serializeToStream(nativeByteBuffer);
                    nativeByteBuffer.rewind();
                    byte[] bytes = new byte[nativeByteBuffer.remaining()];
                    nativeByteBuffer.readBytes(bytes, false);
                    entity.documentSerialized = bytes;
                    nativeByteBuffer.reuse();
                } catch (Throwable unused) {
                    if (nativeByteBuffer != null) {
                        nativeByteBuffer.reuse();
                    }
                }
            } else {
                entity.documentType = 3;
            }
            int docType = entity.documentType;
            if (docType == 1 || docType == 3) {
                File file = new File("/");
                if (isNew) {
                    file = AyuAttachments.getInstance(this.currentAccount).processAttachment(request);
                    TLRPC.MessageMedia messageMedia = MessageObject.getMedia(request.getMessage());
                    if (messageMedia != null && MessageObject.isVideoDocument(messageMedia.document)) {
                        ArrayList<TLRPC.PhotoSize> thumbs = messageMedia.document.thumbs;
                        if (thumbs != null) {
                            for (int i = 0; i < thumbs.size(); i++) {
                                TLRPC.PhotoSize photoSize = thumbs.get(i);
                                if (photoSize instanceof TLRPC.TL_photoSize) {
                                    File thumbFile = AyuAttachments.getInstance(this.currentAccount).processThumb((TLRPC.TL_photoSize) photoSize);
                                    if (thumbFile != null && !thumbFile.getAbsolutePath().equals("/")) {
                                        entity.hqThumbPath = thumbFile.getAbsolutePath();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    file = new File(AyuAttachments.getInstance(this.currentAccount).getExistingPath(request.getMessage(), false));
                }
                TLRPC.Document document = message.media.document;
                if (document != null) {
                    entity.documentAttributesSerialized = serializeMultiple(document.attributes);
                    entity.thumbsSerialized = serializeMultiple(document.thumbs);
                    entity.mimeType = document.mime_type;
                }
                if (file != null) {
                    String absPath = file.getAbsolutePath();
                    if (!TextUtils.isEmpty(absPath) && !absPath.equals("/")) {
                        path = absPath;
                    }
                }
                entity.mediaPath = path;
            }
        }
    }

    public void map(AyuMessageBase entity, TLRPC.Message message) {
        int flags = entity.flags;
        message.dialog_id = entity.dialogId;
        message.grouped_id = entity.groupedId;
        message.peer_id = getMessagesController().getPeer(entity.peerId);
        message.from_id = getMessagesController().getPeer(entity.fromId);
        int messageId = entity.messageId;
        message.id = messageId;
        message.realId = messageId;
        message.date = entity.date;
        message.flags = flags;
        message.unread = (flags & 1) != 0;
        message.out = (flags & 2) != 0;
        message.mentioned = (flags & 16) != 0;
        message.media_unread = (flags & 32) != 0;
        message.silent = (flags & 8192) != 0;
        message.post = (flags & 16384) != 0;
        message.from_scheduled = (262144 & flags) != 0;
        message.legacy = (524288 & flags) != 0;
        message.edit_hide = (2097152 & flags) != 0;
        message.pinned = (16777216 & flags) != 0;
        message.noforwards = false;
        message.ayuNoforwards = (67108864 & flags) != 0;
        message.invert_media = (134217728 & flags) != 0;
        message.edit_date = entity.editDate;
        message.views = entity.views;
        if ((flags & 4) != 0) {
            TLRPC.TL_messageFwdHeader fwdHeader = new TLRPC.TL_messageFwdHeader();
            message.fwd_from = fwdHeader;
            fwdHeader.flags = entity.fwdFlags;
            if (entity.fwdFromId != 0) {
                fwdHeader.from_id = getMessagesController().getPeer(entity.fwdFromId);
            }
            fwdHeader.from_name = entity.fwdName;
            fwdHeader.date = entity.fwdDate;
            fwdHeader.post_author = entity.fwdPostAuthor;
        }
        if ((flags & 8) != 0) {
            if (entity.replyFlags != 0) {
                TLRPC.TL_messageReplyHeader replyHeader = new TLRPC.TL_messageReplyHeader();
                message.reply_to = replyHeader;
                replyHeader.flags = entity.replyFlags;
                replyHeader.reply_to_msg_id = entity.replyMessageId;
                replyHeader.reply_to_peer_id = getMessagesController().getPeer(entity.replyPeerId);
                replyHeader.reply_to_top_id = entity.replyTopId;
                replyHeader.forum_topic = entity.replyForumTopic;
            } else {
                message.reply_to = deserializeSingle(entity.replySerialized, buffer -> TLRPC.MessageReplyHeader.TLdeserialize(buffer, buffer.readInt32(false), false));
            }
        }
        if ((flags & 64) != 0) {
            message.reply_markup = deserializeSingle(entity.replyMarkupSerialized, buffer -> TLRPC.ReplyMarkup.TLdeserialize(buffer, buffer.readInt32(false), false));
        }
        message.post_author = entity.postAuthor;
        message.message = entity.text;
        message.entities = deserializeMultiple(entity.textEntities, buffer -> TLRPC.MessageEntity.TLdeserialize(buffer, buffer.readInt32(false), false));
    }

    public void mapMedia(AyuMessageBase entity, TLRPC.Message message) {
        int docType = entity.documentType;
        byte[] docBytes = entity.documentSerialized;
        String mediaPath = entity.mediaPath;
        int date = entity.date;
        if (docType == 0) {
            return;
        }
        if (docType != 2 && TextUtils.isEmpty(mediaPath)) {
            return;
        }
        if (docType == 2) {
            if (docBytes == null || docBytes.length == 0) return;
            NativeByteBuffer buffer = null;
            try {
                buffer = new NativeByteBuffer(docBytes.length);
                buffer.put(ByteBuffer.wrap(docBytes));
                buffer.rewind();
                message.media = TLRPC.MessageMedia.TLdeserialize(buffer, buffer.readInt32(false), false);
                message.stickerVerified = 1;
                buffer.reuse();
            } catch (Throwable unused) {
                if (buffer != null) {
                    buffer.reuse();
                }
            }
        } else {
            message.attachPath = mediaPath;
            File file = new File(mediaPath);
            if (docType == 1) {
                Pair<Integer, Integer> sizePair = AyuFileUtils.extractImageSizeFromName(file.getName());
                if (sizePair == null) {
                    sizePair = AyuFileUtils.extractImageSizeFromFile(file.getAbsolutePath());
                }
                if (sizePair == null) {
                    sizePair = new Pair<>(500, 302);
                }
                int width = sizePair.first != null ? sizePair.first : 500;
                int height = sizePair.second != null ? sizePair.second : 302;
                TLRPC.TL_messageMediaPhoto mediaPhoto = new TLRPC.TL_messageMediaPhoto();
                message.media = mediaPhoto;
                mediaPhoto.flags = 1;
                mediaPhoto.photo = new TLRPC.TL_photo();
                mediaPhoto.photo.has_stickers = false;
                mediaPhoto.photo.date = date;
                TLRPC.TL_photoSize photoSize = new TLRPC.TL_photoSize();
                photoSize.size = (int) file.length();
                photoSize.w = width;
                photoSize.h = height;
                photoSize.type = "y";
                photoSize.location = new AyuFileLocation(mediaPath);
                mediaPhoto.photo.sizes.add(photoSize);
            } else if (docType == 3) {
                TLRPC.TL_messageMediaDocument mediaDoc = new TLRPC.TL_messageMediaDocument();
                message.media = mediaDoc;
                mediaDoc.flags = 1;
                String readableFilename = AyuFileUtils.getReadableFilename(file.getName());
                mediaDoc.document = new TLRPC.TL_document();
                TLRPC.Document document = mediaDoc.document;
                document.date = date;
                document.localPath = mediaPath;
                document.file_name = readableFilename;
                document.file_name_fixed = readableFilename;
                document.size = file.length();
                document.mime_type = entity.mimeType;
                document.attributes = deserializeMultiple(entity.documentAttributesSerialized, buffer -> TLRPC.DocumentAttribute.TLdeserialize(buffer, buffer.readInt32(false), false));
                ArrayList<TLRPC.PhotoSize> thumbs = deserializeMultiple(entity.thumbsSerialized, buffer -> TLRPC.PhotoSize.TLdeserialize(0L, 0L, 0L, buffer, buffer.readInt32(false), false));
                if (thumbs != null) {
                    for (int i = 0; i < thumbs.size(); i++) {
                        TLRPC.PhotoSize photoSize = thumbs.get(i);
                        if (photoSize != null) {
                            if ((photoSize instanceof TLRPC.TL_photoSize) && !TextUtils.isEmpty(entity.hqThumbPath) && (photoSize.bytes == null || photoSize.bytes.length == 0)) {
                                photoSize.location = new AyuFileLocation(entity.hqThumbPath);
                            }
                            if ((photoSize.bytes != null && photoSize.bytes.length != 0) || photoSize.location != null) {
                                document.thumbs.add(photoSize);
                            }
                        }
                    }
                }
            }
        }
    }

    public void map(TLRPC.Dialog dialog, DeletedDialog deletedDialog) {
        deletedDialog.userId = getUserConfig().getClientUserId();
        deletedDialog.dialogId = dialog.id;
        deletedDialog.peerId = MessageObject.getPeerId(dialog.peer);
        deletedDialog.folderId = dialog.folder_id;
        deletedDialog.topMessage = dialog.top_message;
        deletedDialog.lastMessageDate = dialog.last_message_date;
        deletedDialog.flags = dialog.flags;
        deletedDialog.entityCreateDate = getConnectionsManager().getCurrentTime();
    }

    public void map(DeletedDialog deletedDialog, TLRPC.Dialog dialog) {
        dialog.id = deletedDialog.dialogId;
        dialog.peer = getMessagesController().getPeer(deletedDialog.peerId);
        if (deletedDialog.folderId != null) {
            dialog.folder_id = deletedDialog.folderId;
        }
        dialog.top_message = deletedDialog.topMessage;
        dialog.last_message_date = deletedDialog.lastMessageDate;
        dialog.flags = deletedDialog.flags;
    }
}

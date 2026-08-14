package com.radolyn.ayugram.database.entities;

/* JADX INFO: loaded from: classes.dex */
public abstract class AyuMessageBase {
    public int date;
    public long dialogId;
    public byte[] documentAttributesSerialized;
    public byte[] documentSerialized;
    public int documentType;
    public int editDate;
    public int entityCreateDate;
    public int flags;
    public long fromId;
    public int fwdDate;
    public int fwdFlags;
    public long fwdFromId;
    public String fwdName;
    public String fwdPostAuthor;
    public long groupedId;
    public String hqThumbPath;
    public String mediaPath;
    public int messageId;
    public String mimeType;
    public long peerId;
    public String postAuthor;
    public int replyFlags;
    public boolean replyForumTopic;
    public byte[] replyMarkupSerialized;
    public int replyMessageId;
    public long replyPeerId;
    public byte[] replySerialized;
    public int replyTopId;
    public String text;
    public byte[] textEntities;
    public byte[] thumbsSerialized;
    public long topicId;
    public long userId;
    public int views;
}

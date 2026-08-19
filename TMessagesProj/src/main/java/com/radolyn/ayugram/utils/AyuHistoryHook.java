package com.radolyn.ayugram.utils;

import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import androidx.collection.LongSparseArray;
import com.radolyn.ayugram.AyuConfig;
import com.radolyn.ayugram.controllers.AyuMapper;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.entities.AyuMessageBase;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.DeletedMessageReaction;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;

public abstract class AyuHistoryHook {
    private static final SparseArray<WeakReference<ChatActivity>> instances = new SparseArray<>();

    public static synchronized void setInstance(ChatActivity chatActivity) {
        try {
            for (int size = instances.size() - 1; size >= 0; size--) {
                int iKeyAt = instances.keyAt(size);
                WeakReference<ChatActivity> ref = instances.valueAt(size);
                ChatActivity chatActivity2 = ref != null ? ref.get() : null;
                if (chatActivity2 == null || (chatActivity2 == chatActivity && iKeyAt != chatActivity.getClassGuid())) {
                    instances.removeAt(size);
                }
            }
            instances.put(chatActivity.getClassGuid(), new WeakReference<>(chatActivity));
        } catch (Throwable th) {
            FileLog.e(th);
        }
    }

    public static synchronized void removeInstance(ChatActivity chatActivity) {
        if (chatActivity != null) {
            instances.remove(chatActivity.getClassGuid());
        }
    }

    public static void doHook(int account, ArrayList<MessageObject> arrayList, SparseArray<MessageObject>[] sparseArrayArr, int minId, int maxId, long dialogId, long topicId, boolean isSecret, boolean z2) {
        try {
            AyuMessagesController ayuMessagesController = AyuMessagesController.getInstance(account);
            List<DeletedMessageFull> messages = ayuMessagesController.getMessages(dialogId, topicId, minId, maxId);
            if (messages == null || messages.isEmpty()) {
                return;
            }
            LongSparseArray<TLRPC.TL_message> longSparseArray = new LongSparseArray<>();
            ArrayList<Long> arrayList2 = new ArrayList<>();
            ArrayList<Long> arrayList3 = new ArrayList<>();
            for (DeletedMessageFull deletedMessageFull : messages) {
                if (!isEmpty(deletedMessageFull.message)) {
                    TLRPC.TL_message map = map(deletedMessageFull, account);
                    longSparseArray.put((long) map.id, map);
                    MessagesStorage.addUsersAndChatsFromMessage(map, arrayList2, arrayList3, null);
                }
            }
            if (longSparseArray.isEmpty()) {
                return;
            }
            MessagesStorage messagesStorage = MessagesStorage.getInstance(account);
            MessagesController messagesController = MessagesController.getInstance(account);
            QuadroResult entities = getEntities(messagesStorage, arrayList2, arrayList3);
            Pair<LongSparseArray<TLRPC.User>, LongSparseArray<TLRPC.Chat>> dicts = entities.getDicts();
            ArrayList<TLRPC.User> users = entities.getUsers();
            ArrayList<TLRPC.Chat> chats = entities.getChats();
            if (!users.isEmpty()) {
                messagesController.putUsers(users, true);
            }
            if (!chats.isEmpty()) {
                messagesController.putChats(chats, true);
            }
            ArrayList<MessageObject> arrayList4 = new ArrayList<>();
            for (int i4 = 0; i4 < longSparseArray.size(); i4++) {
                try {
                    MessageObject msgObj = new MessageObject(account, longSparseArray.get(longSparseArray.keyAt(i4)), dicts.first, dicts.second, false, true);
                    arrayList4.add(msgObj);
                } catch (Exception unused) {
                }
            }
            if (arrayList4.isEmpty()) {
                return;
            }
            merge(arrayList4, arrayList, isSecret, z2);
            fixReplies(account, arrayList, sparseArrayArr, ayuMessagesController, dicts, messagesStorage);
        } catch (Throwable th) {
            FileLog.e("AyuHistoryHook.doHook error", th);
        }
    }

    public static void fixReplies(int account, List<MessageObject> list, SparseArray<MessageObject>[] sparseArrayArr, AyuMessagesController ayuMessagesController, Pair<LongSparseArray<TLRPC.User>, LongSparseArray<TLRPC.Chat>> pair, MessagesStorage messagesStorage) {
        if (list == null) return;
        for (MessageObject messageObject2 : list) {
            if (messageObject2 == null || messageObject2.messageOwner == null) continue;
            TLRPC.Message message3 = messageObject2.messageOwner;
            if (message3.reply_to != null && ((message3.replyMessage == null || messageObject2.replyMessageObject == null) && !messageObject2.isReplyToStory())) {
                MessageObject messageObject = null;
                if (sparseArrayArr != null) {
                    if (sparseArrayArr.length > 0 && sparseArrayArr[0] != null) {
                        messageObject = sparseArrayArr[0].get(message3.reply_to.reply_to_msg_id);
                    }
                    if ((messageObject == null || messageObject.getId() != message3.reply_to.reply_to_msg_id) && sparseArrayArr.length > 1 && sparseArrayArr[1] != null) {
                        messageObject = sparseArrayArr[1].get(message3.reply_to.reply_to_msg_id);
                    }
                }
                if (messageObject == null || messageObject.getId() != message3.reply_to.reply_to_msg_id) {
                    for (MessageObject messageObject3 : list) {
                        if (messageObject3 != null && messageObject3.getId() == message3.reply_to.reply_to_msg_id) {
                            messageObject = messageObject3;
                            break;
                        }
                    }
                }
                if ((messageObject == null || messageObject.getId() != message3.reply_to.reply_to_msg_id) && ayuMessagesController != null) {
                    DeletedMessageFull message = ayuMessagesController.getMessage(MessageObject.getDialogId(message3), message3.reply_to.reply_to_msg_id);
                    if (message != null) {
                        try {
                            messageObject = new MessageObject(account, map(message, account), pair != null ? pair.first : null, pair != null ? pair.second : null, false, false);
                        } catch (Exception unused) {
                        }
                    }
                }
                if (messageObject == null && messagesStorage != null) {
                    TLRPC.Message message2 = messagesStorage.getMessage(MessageObject.getDialogId(message3), message3.reply_to.reply_to_msg_id);
                    if (message2 != null) {
                        try {
                            messageObject = new MessageObject(account, message2, pair != null ? pair.first : null, pair != null ? pair.second : null, false, false);
                        } catch (Exception unused2) {
                        }
                    }
                }
                if (messageObject != null) {
                    message3.replyMessage = messageObject.messageOwner;
                    if (messageObject.messageOwner != null) {
                        message3.reply_to.reply_to_peer_id = messageObject.messageOwner.peer_id;
                    }
                    messageObject2.replyMessageObject = messageObject;
                }
            }
        }
    }

    private static void merge(ArrayList<MessageObject> arrayList, ArrayList<MessageObject> arrayList2, boolean isSecret, boolean z2) {
        if (arrayList == null || arrayList.isEmpty() || arrayList2 == null) {
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = arrayList.get(i);
            if (messageObject == null) continue;
            int targetId = messageObject.getId();
            int targetDate = messageObject.messageOwner != null ? messageObject.messageOwner.date : 0;

            boolean exists = false;
            for (int k = 0; k < arrayList2.size(); k++) {
                MessageObject existing = arrayList2.get(k);
                if (existing != null && existing.getId() == targetId) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                continue;
            }

            if (arrayList2.isEmpty()) {
                arrayList2.add(messageObject);
                continue;
            }

            // Determine current list direction (Telegram defaults to descending: newest first)
            boolean isAscending = false;
            if (arrayList2.size() >= 2) {
                MessageObject first = arrayList2.get(0);
                MessageObject last = arrayList2.get(arrayList2.size() - 1);
                if (first != null && last != null) {
                    if (first.getId() > 0 && last.getId() > 0) {
                        isAscending = first.getId() < last.getId();
                    } else if (first.messageOwner != null && last.messageOwner != null) {
                        isAscending = first.messageOwner.date < last.messageOwner.date;
                    }
                }
            }

            int insertIndex = -1;
            for (int j = 0; j < arrayList2.size(); j++) {
                MessageObject current = arrayList2.get(j);
                if (current == null) continue;
                int currentId = current.getId();
                int currentDate = current.messageOwner != null ? current.messageOwner.date : 0;

                boolean shouldInsertBefore;
                if (targetId > 0 && currentId > 0) {
                    shouldInsertBefore = isAscending ? (targetId < currentId) : (targetId > currentId);
                } else {
                    shouldInsertBefore = isAscending ? (targetDate < currentDate) : (targetDate > currentDate);
                }

                if (shouldInsertBefore) {
                    insertIndex = j;
                    break;
                }
            }

            if (insertIndex >= 0) {
                arrayList2.add(insertIndex, messageObject);
            } else {
                arrayList2.add(messageObject);
            }
        }
    }

    public static QuadroResult getEntities(MessagesStorage messagesStorage, ArrayList<Long> arrayList, ArrayList<Long> arrayList2) {
        ArrayList<TLRPC.User> arrayList3 = new ArrayList<>();
        ArrayList<TLRPC.Chat> arrayList4 = new ArrayList<>();
        try {
            if (!arrayList.isEmpty()) {
                messagesStorage.getUsersInternal(arrayList, arrayList3);
            }
        } catch (Exception unused) {
        }
        try {
            if (!arrayList2.isEmpty()) {
                messagesStorage.getChatsInternal(TextUtils.join(",", arrayList2), arrayList4);
            }
        } catch (Exception unused2) {
        }
        return new QuadroResult(arrayList3, arrayList4);
    }

    public static boolean isEmpty(AyuMessageBase ayuMessageBase) {
        if (ayuMessageBase != null) {
            return TextUtils.isEmpty(ayuMessageBase.text) && TextUtils.isEmpty(ayuMessageBase.mediaPath) && ayuMessageBase.documentSerialized == null;
        }
        return true;
    }

    public static TLRPC.TL_message map(DeletedMessageFull deletedMessageFull, int i) {
        TLRPC.TL_message tL_message = new TLRPC.TL_message();
        AyuMapper.getInstance(i).map(deletedMessageFull.message, tL_message);
        AyuMapper.getInstance(i).mapMedia(deletedMessageFull.message, tL_message);
        List<DeletedMessageReaction> list = deletedMessageFull.reactions;
        if (list != null && !list.isEmpty()) {
            tL_message.reactions = new TLRPC.TL_messageReactions();
            int i2 = 0;
            for (DeletedMessageReaction deletedMessageReaction : deletedMessageFull.reactions) {
                TLRPC.TL_reactionCount tL_reactionCount = new TLRPC.TL_reactionCount();
                tL_reactionCount.count = deletedMessageReaction.count;
                tL_reactionCount.chosen = deletedMessageReaction.selfSelected;
                i2++;
                tL_reactionCount.chosen_order = i2;
                if (deletedMessageReaction.isCustom) {
                    TLRPC.TL_reactionCustomEmoji tL_reactionCustomEmoji = new TLRPC.TL_reactionCustomEmoji();
                    tL_reactionCustomEmoji.document_id = deletedMessageReaction.documentId;
                    tL_reactionCount.reaction = tL_reactionCustomEmoji;
                } else if (deletedMessageReaction.isPaid) {
                    tL_reactionCount.reaction = new TLRPC.TL_reactionPaid();
                } else {
                    TLRPC.TL_reactionEmoji tL_reactionEmoji = new TLRPC.TL_reactionEmoji();
                    tL_reactionEmoji.emoticon = deletedMessageReaction.emoticon;
                    tL_reactionCount.reaction = tL_reactionEmoji;
                }
                tL_message.reactions.results.add(tL_reactionCount);
            }
        }
        tL_message.ayuDeleted = true;
        return tL_message;
    }

    public static Pair<Integer, Integer> getMinAndMaxIds(ArrayList<MessageObject> arrayList, boolean z) {
        int size = arrayList.size();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i3 = 0; i3 < size; i3++) {
            MessageObject messageObject = arrayList.get(i3);
            if (messageObject != null && messageObject.isSent() && messageObject.getId() != 0) {
                TLRPC.Message message = messageObject.messageOwner;
                if (message != null && !(message instanceof TLRPC.TL_messageEmpty) && (z || !(message instanceof TLRPC.TL_messageService))) {
                    int id = messageObject.getId();
                    if (id < min) {
                        min = id;
                    }
                    if (id > max) {
                        max = id;
                    }
                }
            }
        }
        return new Pair<>(min, max);
    }

    public static class QuadroResult {
        private final ArrayList<TLRPC.User> users;
        private final ArrayList<TLRPC.Chat> chats;
        private LongSparseArray<TLRPC.User> usersDict;
        private LongSparseArray<TLRPC.Chat> chatsDict;

        public QuadroResult(ArrayList<TLRPC.User> arrayList, ArrayList<TLRPC.Chat> arrayList2) {
            this.users = arrayList;
            this.chats = arrayList2;
        }

        public Pair<LongSparseArray<TLRPC.User>, LongSparseArray<TLRPC.Chat>> getDicts() {
            if (this.usersDict == null && this.chatsDict == null) {
                this.usersDict = new LongSparseArray<>();
                this.chatsDict = new LongSparseArray<>();
                if (this.users != null) {
                    for (TLRPC.User user : this.users) {
                        if (user != null) {
                            this.usersDict.put(user.id, user);
                        }
                    }
                }
                if (this.chats != null) {
                    for (TLRPC.Chat chat : this.chats) {
                        if (chat != null) {
                            this.chatsDict.put(chat.id, chat);
                        }
                    }
                }
            }
            return new Pair<>(this.usersDict, this.chatsDict);
        }

        public ArrayList<TLRPC.User> getUsers() {
            return this.users;
        }

        public ArrayList<TLRPC.Chat> getChats() {
            return this.chats;
        }
    }
}

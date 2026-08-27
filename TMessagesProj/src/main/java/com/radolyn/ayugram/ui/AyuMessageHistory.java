package com.radolyn.ayugram.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.radolyn.ayugram.controllers.AyuMessagesController;
import com.radolyn.ayugram.database.entities.DeletedMessageFull;
import com.radolyn.ayugram.database.entities.EditedMessage;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class AyuMessageHistory extends BaseFragment {
    public static final int MODE_DELETED = 0;
    public static final int MODE_EDITED = 1;

    private int mode;
    private long dialogId;
    private long topicId;
    private int messageId;
    private MessageObject currentMessageObject;
    private TLRPC.Chat currentChat;
    private TLRPC.User currentUser;
    private ChatActivity parentActivity;

    private RecyclerListView listView;
    private ListAdapter listAdapter;
    private EmptyTextProgressView emptyView;
    private ArrayList<MessageObject> messages = new ArrayList<>();
    private boolean loading;
    private boolean endReached;

    public AyuMessageHistory(ChatActivity parentActivity, TLRPC.Chat chat, TLRPC.User user, MessageObject messageObject, ChatActivity.ThemeDelegate themeDelegate) {
        this.parentActivity = parentActivity;
        this.currentChat = chat;
        this.currentUser = user;
        this.currentMessageObject = messageObject;
        this.mode = MODE_EDITED;
        if (messageObject != null) {
            this.dialogId = messageObject.getDialogId();
            this.messageId = messageObject.getId();
        }
    }

    public AyuMessageHistory(ChatActivity parentActivity, TLRPC.Chat chat, TLRPC.User user, TLRPC.EncryptedChat encryptedChat, long dialogId, ChatActivity.ThemeDelegate themeDelegate) {
        this.parentActivity = parentActivity;
        this.currentChat = chat;
        this.currentUser = user;
        this.dialogId = dialogId;
        this.mode = MODE_DELETED;
    }

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        loadHistory(true);
        return true;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        if (mode == MODE_EDITED) {
            actionBar.setTitle("Edit History");
        } else {
            actionBar.setTitle("Deleted Messages");
        }
        actionBar.setAllowOverlayTitle(true);
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        fragmentView = new FrameLayout(context);
        fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        emptyView = new EmptyTextProgressView(context);
        emptyView.setShowAtCenter(true);
        emptyView.setText(LocaleController.getString(R.string.NoResult));
        frameLayout.addView(emptyView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        listView = new RecyclerListView(context);
        listView.setEmptyView(emptyView);
        listView.setVerticalScrollBarEnabled(true);
        listView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
        listAdapter = new ListAdapter(context);
        listView.setAdapter(listAdapter);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!loading && !endReached) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null && layoutManager.findLastVisibleItemPosition() >= messages.size() - 5) {
                        loadHistory(false);
                    }
                }
            }
        });

        return fragmentView;
    }

    private void loadHistory(boolean first) {
        if (loading) return;
        loading = true;
        if (emptyView != null && first) {
            emptyView.showProgress();
        }

        final int offset = first ? 0 : messages.size();
        final int limit = 30;

        Utilities.globalQueue.postRunnable(() -> {
            ArrayList<MessageObject> loadedObjects = new ArrayList<>();
            AyuMessagesController controller = AyuMessagesController.getInstance(currentAccount);

            if (mode == MODE_EDITED) {
                List<EditedMessage> revisions = controller.getRevisions(dialogId, messageId, offset, limit);
                if (revisions != null) {
                    for (int i = 0; i < revisions.size(); i++) {
                        EditedMessage rev = revisions.get(i);
                        TLRPC.TL_message msg = new TLRPC.TL_message();
                        controller.getAyuMapperInternal().map(rev, msg);
                        try {
                            controller.getAyuMapperInternal().mapMedia(rev, msg);
                        } catch (Exception e) {
                        }
                        MessageObject obj = new MessageObject(currentAccount, msg, false, true);
                        loadedObjects.add(obj);
                    }
                }
            } else {
                List<DeletedMessageFull> deleted = controller.getMessagesPaginated(dialogId, topicId, offset, limit);
                if (deleted != null) {
                    for (int i = 0; i < deleted.size(); i++) {
                        DeletedMessageFull item = deleted.get(i);
                        if (item != null && item.message != null) {
                            TLRPC.TL_message msg = new TLRPC.TL_message();
                            controller.getAyuMapperInternal().map(item.message, msg);
                            try {
                                controller.getAyuMapperInternal().mapMedia(item.message, msg);
                            } catch (Exception e) {
                            }
                            MessageObject obj = new MessageObject(currentAccount, msg, false, true);
                            loadedObjects.add(obj);
                        }
                    }
                }
            }

            AndroidUtilities.runOnUIThread(() -> {
                loading = false;
                if (loadedObjects.size() < limit) {
                    endReached = true;
                }
                messages.addAll(loadedObjects);
                if (listAdapter != null) {
                    listAdapter.notifyDataSetChanged();
                }
                if (emptyView != null) {
                    emptyView.showTextView();
                }
            });
        });
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            return false;
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ChatMessageCell cell = new ChatMessageCell(mContext, currentAccount);
            cell.setDelegate(new ChatMessageCell.ChatMessageCellDelegate() {});
            return new RecyclerListView.Holder(cell);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ChatMessageCell cell = (ChatMessageCell) holder.itemView;
            MessageObject message = messages.get(position);
            cell.setMessageObject(message, null, false, false, false);
        }
    }
}

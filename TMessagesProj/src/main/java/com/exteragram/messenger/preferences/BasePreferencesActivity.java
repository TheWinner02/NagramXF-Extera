package com.exteragram.messenger.preferences;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import androidx.collection.LongSparseArray;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.exteragram.messenger.utils.ui.PopupUtils;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.ToIntFunction;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ItemOptions;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;

/* JADX INFO: loaded from: classes.dex */
public abstract class BasePreferencesActivity extends BaseFragment {
    protected LinearLayoutManager layoutManager;
    public UniversalRecyclerView listView;

    public abstract void fillItems(ArrayList<UItem> arrayList, UniversalAdapter universalAdapter);

    public abstract String getTitle();

    public boolean hasHeaderCell() {
        return false;
    }

    public boolean hasWhiteActionBar() {
        return false;
    }

    public void initializeOptionStrings() {
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSupportEdgeToEdge() {
        return true;
    }

    public boolean needHideTitle() {
        return false;
    }

    public abstract void onClick(UItem uItem, View view, int i, float f, float f2);

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        initializeOptionStrings();
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        this.actionBar.setTitle(getTitle());
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: com.exteragram.messenger.preferences.BasePreferencesActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i) {
                if (i == -1) {
                    BasePreferencesActivity.this.finishFragment();
                }
            }
        });
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        if (actionBar.menu == null) {
            actionBar.createMenu();
        }
        UniversalRecyclerView universalRecyclerView = new UniversalRecyclerView(this, this::fillItems, (uItem, view1, i1, f1, f2) -> onClick((UItem) uItem, (View) view1, ((Integer) i1).intValue(), ((Float) f1).floatValue(), ((Float) f2).floatValue()), (uItem, view1, i1, f1, f2) -> Boolean.valueOf(onLongClick((UItem) uItem, (View) view1, ((Integer) i1).intValue(), ((Float) f1).floatValue(), ((Float) f2).floatValue())));
        this.listView = universalRecyclerView;
        universalRecyclerView.setSections();
        if (!hasHeaderCell()) {
            this.actionBar.setAdaptiveBackground(this.listView, needHideTitle());
        }
        this.listView.adapter.setApplyBackground(false);
        this.listView.setClipToPadding(false);
        UniversalRecyclerView universalRecyclerView2 = this.listView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 1, false);
        this.layoutManager = linearLayoutManager;
        universalRecyclerView2.setLayoutManager(linearLayoutManager);
        frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        this.fragmentView = frameLayout;
        return frameLayout;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        this.listView.adapter.update(false);
        Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: com.exteragram.messenger.preferences.BasePreferencesActivity.2
            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getTopOffset(int i) {
                if (BasePreferencesActivity.this.hasHeaderCell()) {
                    return AndroidUtilities.statusBarHeight;
                }
                return 0;
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getBottomOffset(int i) {
                return BasePreferencesActivity.this.getBottomInset();
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        Bulletin.removeDelegate(this);
    }

    public void scrollToItem(final int i) {
        UniversalRecyclerView universalRecyclerView = this.listView;
        if (universalRecyclerView == null || universalRecyclerView.adapter == null || this.layoutManager == null) {
            return;
        }
        int iFindPositionByItemId = universalRecyclerView.findPositionByItemId(i);
        if (iFindPositionByItemId >= 0 && iFindPositionByItemId < this.listView.adapter.getItemCount()) {
            this.layoutManager.scrollToPositionWithOffset(iFindPositionByItemId, AndroidUtilities.dp(80.0f));
            this.listView.highlightRow(() -> lambda$scrollToItem$0(i));
        } else {
            BulletinFactory.of(this).createEmojiBulletin("🤷\u200d♂️", "Setting not found").show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ int lambda$scrollToItem$0(int i) {
        return this.listView.findPositionByItemId(i);
    }

    public int[] unBox(Collection<Integer> collection) {
        return collection.stream().mapToInt(new ToIntFunction() { // from class: com.exteragram.messenger.preferences.BasePreferencesActivity$$ExternalSyntheticLambda3
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(Object obj) {
                return ((Integer) obj).intValue();
            }
        }).toArray();
    }

    public void showListDialog(UItem uItem, CharSequence[] charSequenceArr, String str, int i, PopupUtils.OnItemClickListener onItemClickListener) {
        showListDialog(uItem, charSequenceArr, null, str, i, onItemClickListener);
    }

    public void showListDialog(UItem uItem, CharSequence[] charSequenceArr, int[] iArr, String str, int i, PopupUtils.OnItemClickListener onItemClickListener) {
        showListDialog(uItem, charSequenceArr, iArr, str, i, onItemClickListener, iArr == null, true);
    }

    public void showListDialog(final UItem uItem, final CharSequence[] charSequenceArr, int[] iArr, String str, final int i, final PopupUtils.OnItemClickListener onItemClickListener, boolean z, final boolean z2) {
        if (getParentActivity() == null) {
            return;
        }
        PopupUtils.showDialog(charSequenceArr, iArr, str, i, getContext(), i2 -> lambda$showListDialog$1(z2, i, onItemClickListener, uItem, charSequenceArr, i2), getResourceProvider(), z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showListDialog$1(boolean z, int i, PopupUtils.OnItemClickListener onItemClickListener, UItem uItem, CharSequence[] charSequenceArr, int i2) {
        if (z && i == i2) {
            return;
        }
        onItemClickListener.onClick(i2);
        View viewFindViewByItemId = this.listView.findViewByItemId(uItem.id);
        if (viewFindViewByItemId instanceof TextCell) {
            ((TextCell) viewFindViewByItemId).setValue(charSequenceArr[i2], true);
        }
        this.listView.adapter.update(true);
    }

    public void showRestartBulletin() {
        BulletinFactory.of(this).createSimpleBulletin(R.raw.info, "Restart Required", "Restart app to apply changes", this::lambda$showRestartBulletin$2).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showRestartBulletin$2() {
        Context context = getContext();
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        Intent intentMakeRestartActivityTask = Intent.makeRestartActivityTask(launchIntentForPackage == null ? null : launchIntentForPackage.getComponent());
        intentMakeRestartActivityTask.setPackage(context.getPackageName());
        context.startActivity(intentMakeRestartActivityTask);
        Runtime.getRuntime().exit(0);
    }

    public void toggleBooleanSettingAndRefresh(UItem uItem, Consumer<Boolean> consumer) {
        boolean z = !uItem.checked;
        consumer.accept(Boolean.valueOf(z));
        uItem.setChecked(z);
        View viewFindViewByItemId = this.listView.findViewByItemId(uItem.id);
        if (viewFindViewByItemId instanceof CheckBoxCell) {
            ((CheckBoxCell) viewFindViewByItemId).setChecked(z, true);
        } else if (viewFindViewByItemId instanceof TextCheckCell) {
            ((TextCheckCell) viewFindViewByItemId).setChecked(z);
        }
        this.listView.adapter.update(true);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        if (hasWhiteActionBar()) {
            return ColorUtils.calculateLuminance(getThemedColor(Theme.key_windowBackgroundWhite)) > 0.699999988079071d;
        }
        return super.isLightStatusBar();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean onLongClick(UItem uItem, View view, int i, float f, float f2) {
        String firstSettingLink = null;
        if (TextUtils.isEmpty(firstSettingLink)) {
            return false;
        }
        showCopyLinkOptions(view, firstSettingLink);
        return false;
    }

    public void showCopyLinkOptions(View view, final String str) {
        view.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS);
        ItemOptions.makeOptions(this, view).add(R.drawable.msg_copy, LocaleController.getString(R.string.CopyLink), () -> showCopyLinkOptions_lambda3(str)).add(R.drawable.msg_share, LocaleController.getString(R.string.ShareLink), () -> showCopyLinkOptions_lambda4(str)).setScrimViewBackground(this.listView.getClipBackground(view)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void showCopyLinkOptions_lambda3(String str) {
        if (AndroidUtilities.addToClipboard(str)) {
            BulletinFactory.of(this).createCopyBulletin(LocaleController.getString(R.string.LinkCopied)).show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void showCopyLinkOptions_lambda4(String str) {
        showDialog(new ShareAlert(getContext(), null, str, false, str, false, getResourceProvider()) { // from class: com.exteragram.messenger.preferences.BasePreferencesActivity.3
            @Override // org.telegram.ui.Components.ShareAlert
            public void onSend(LongSparseArray<TLRPC.Dialog> longSparseArray, int i, TLRPC.TL_forumTopic tL_forumTopic, boolean z) {
                String string;
                if (z) {
                    if (longSparseArray != null && longSparseArray.size() == 1) {
                        long j = longSparseArray.valueAt(0).id;
                        if (j == 0 || j == BasePreferencesActivity.this.getUserConfig().getClientUserId()) {
                            string = "Link sent to Saved Messages";
                        } else {
                            string = "Link sent to " + BasePreferencesActivity.this.getMessagesController().getPeerName(j, true);
                        }
                    } else {
                        string = "Link sent to " + i + " chats";
                    }
                    Bulletin bulletinCreateSimpleBulletin = BulletinFactory.of(BasePreferencesActivity.this).createSimpleBulletin(R.raw.forward, string);
                    bulletinCreateSimpleBulletin.hideAfterBottomSheet = false;
                    bulletinCreateSimpleBulletin.show(true);
                }
            }
        });
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onInsets(int i, int i2, int i3, int i4) {
        this.listView.setPadding(0, 0, 0, i4);
        this.listView.setClipToPadding(false);
    }
}

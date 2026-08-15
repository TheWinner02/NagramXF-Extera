package com.exteragram.messenger.plugins.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.plugins.Plugin;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.ui.components.EmptyPluginsView;
import com.exteragram.messenger.plugins.ui.components.PluginCell;
import com.exteragram.messenger.plugins.ui.components.PluginCellDelegate;
import com.exteragram.messenger.plugins.utils.PluginsWatchdog;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.exteragram.messenger.utils.text.LocaleUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

/* JADX INFO: loaded from: classes.dex */
@SourceDebugExtension({"SMAP\nPluginsActivity.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PluginsActivity.kt\ncom/exteragram/messenger/plugins/ui/PluginsActivity\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,407:1\n1#2:408\n*E\n"})
public final class PluginsActivity extends BasePreferencesActivity implements NotificationCenter.NotificationCenterDelegate {
    private EmptyPluginsView emptyView;
    private ActionBarMenuItem infoItem;
    private boolean isSwitchingEngineState;
    private String query;
    private ActionBarMenuItem searchItem;
    private boolean searching;

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        View viewCreateView = super.createView(context);
        ActionBarMenuItem actionBarMenuItemSearchListener = this.actionBar.menu.addItem(0, R.drawable.outline_header_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() { // from class: com.exteragram.messenger.plugins.ui.PluginsActivity.createView.1
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchExpand() {
                PluginsActivity.this.searching = true;
                ((BasePreferencesActivity) PluginsActivity.this).listView.adapter.update(true);
                ((BasePreferencesActivity) PluginsActivity.this).listView.scrollToPosition(0);
                ActionBarMenuItem actionBarMenuItem = PluginsActivity.this.infoItem;
                if (actionBarMenuItem != null) {
                    actionBarMenuItem.setVisibility(8);
                }
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onSearchCollapse() {
                PluginsActivity.this.searching = false;
                PluginsActivity.this.query = null;
                ((BasePreferencesActivity) PluginsActivity.this).listView.adapter.update(true);
                ((BasePreferencesActivity) PluginsActivity.this).listView.scrollToPosition(0);
                ActionBarMenuItem actionBarMenuItem = PluginsActivity.this.infoItem;
                if (actionBarMenuItem != null) {
                    actionBarMenuItem.setVisibility(0);
                }
            }

            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener
            public void onTextChanged(EditText editText) {
                PluginsActivity.this.query = editText.getText().toString();
                ((BasePreferencesActivity) PluginsActivity.this).listView.adapter.update(true);
                ((BasePreferencesActivity) PluginsActivity.this).listView.scrollToPosition(0);
            }
        });
        this.searchItem = actionBarMenuItemSearchListener;
        if (actionBarMenuItemSearchListener != null) {
            actionBarMenuItemSearchListener.setSearchFieldHint(LocaleController.getString(R.string.Search));
        }
        AndroidUtilities.updateViewVisibilityAnimated(this.searchItem, ExteraConfig.getPluginsEngine() && !PluginsController.INSTANCE.getInstance().getPlugins().isEmpty(), 0.5f, false);
        ActionBarMenuItem actionBarMenuItemAddItem = this.actionBar.menu.addItem(1, R.drawable.msg_info);
        this.infoItem = actionBarMenuItemAddItem;
        if (actionBarMenuItemAddItem != null) {
            actionBarMenuItemAddItem.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.PluginsActivity$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    presentFragment(new PluginsInfoActivity());
                }
            });
        }
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.exteragram.messenger.plugins.ui.PluginsActivity.createView.3
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 1) {
                    AndroidUtilities.hideKeyboard(PluginsActivity.this.getParentActivity().getCurrentFocus());
                }
            }
        });
        this.fragmentView = viewCreateView;
        return viewCreateView;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return "Plugins";
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        Plugin plugin;
        if (!this.searching) {
            items.add(UItem.asRippleCheck(0, "Enable Python Plugins Engine").setChecked(ExteraConfig.getPluginsEngine()));
        }
        if (ExteraConfig.getPluginsEngine()) {
            HashMap map = new HashMap(PluginsController.INSTANCE.getInstance().getPlugins());
            UItem uItemAsSpace = UItem.asSpace(AndroidUtilities.dp(8.0f));
            uItemAsSpace.transparent = true;
            items.add(uItemAsSpace);
            if (this.searching && !TextUtils.isEmpty(this.query)) {
                map.values().removeIf(pluginItem -> m1343$r8$lambda$pqUrQXSOvACq2ucF3IrTdNqGZA(this, (Plugin) pluginItem));
            }
            if (map.isEmpty()) {
                if (this.emptyView == null) {
                    this.emptyView = new EmptyPluginsView(getContext(), this.resourceProvider);
                }
                EmptyPluginsView emptyPluginsView = this.emptyView;
                if (emptyPluginsView == null) {
                    return;
                }
                if (this.searching) {
                    if (emptyPluginsView.getTag() == null || ((Integer) emptyPluginsView.getTag()).intValue() != 1) {
                        MediaDataController.getInstance(UserConfig.selectedAccount).setPlaceholderImage(emptyPluginsView.getBackupImageView(), "AnimatedEmojies", "🔎", "100_100");
                        emptyPluginsView.setText("No plugins found");
                        emptyPluginsView.setTag(1);
                    }
                } else if (emptyPluginsView.getTag() == null || ((Integer) emptyPluginsView.getTag()).intValue() != 2) {
                    MediaDataController.getInstance(UserConfig.selectedAccount).setPlaceholderImage(emptyPluginsView.getBackupImageView(), "AnimatedEmojies", "📂", "100_100");
                    emptyPluginsView.setText(LocaleUtils.formatWithUsernames("Install plugins via Telegram messages or .py/.zip files"));
                    emptyPluginsView.setTag(2);
                }
                items.add(UItem.asFullscreenCustom(emptyPluginsView, AndroidUtilities.dp((this.searching ? 2 : 1) * 74), true));
            } else {
                if (!ExteraConfig.getPinnedPlugins().isEmpty()) {
                    for (String str : ExteraConfig.getPinnedPlugins()) {
                        if (map.containsKey(str) && (plugin = (Plugin) map.get(str)) != null) {
                            items.add(createPluginItem(plugin));
                            items.add(UItem.asSpace(AndroidUtilities.dp(8.0f)));
                        }
                    }
                }
                ArrayList<Plugin> arrayList = new ArrayList(map.values());
                arrayList.sort(Comparator.comparing(Plugin::getName));
                for (Plugin plugin2 : arrayList) {
                    if (!PluginsController.INSTANCE.getInstance().isPluginPinned(plugin2.getId())) {
                        items.add(createPluginItem(plugin2));
                        items.add(UItem.asSpace(AndroidUtilities.dp(8.0f)));
                    }
                }
            }
            UItem uItemAsSpace2 = UItem.asSpace(AndroidUtilities.dp(4.0f));
            uItemAsSpace2.transparent = true;
            items.add(uItemAsSpace2);
        }
    }

    public static boolean m1343$r8$lambda$pqUrQXSOvACq2ucF3IrTdNqGZA(PluginsActivity pluginsActivity, Plugin plugin) {
        String name = plugin.getName();
        Locale locale = Locale.ROOT;
        return !name.toLowerCase(locale).contains(pluginsActivity.query.toLowerCase(locale));
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.plugins.ui.PluginsActivity$createPluginItem$1, reason: invalid class name */
    /* JADX INFO: loaded from: classes4.dex */
    public static final class AnonymousClass1 implements PluginCellDelegate {
        final /* synthetic */ Plugin $plugin;
        final /* synthetic */ PluginsActivity this$0;

        public AnonymousClass1(Plugin plugin, PluginsActivity pluginsActivity) {
            this.$plugin = plugin;
            this.this$0 = pluginsActivity;
        }

        @Override // com.exteragram.messenger.plugins.ui.components.PluginCellDelegate
        public void sharePlugin() {
            PluginsController.PluginsEngine pluginEngine = PluginsController.INSTANCE.getInstance().getPluginEngine(this.$plugin.getId());
            if (pluginEngine != null) {
                pluginEngine.sharePlugin(this.$plugin.getId());
            }
        }

        @Override // com.exteragram.messenger.plugins.ui.components.PluginCellDelegate
        public void openInExternalApp() {
            PluginsController.PluginsEngine pluginEngine = PluginsController.INSTANCE.getInstance().getPluginEngine(this.$plugin.getId());
            if (pluginEngine != null) {
                pluginEngine.openInExternalApp(this.$plugin.getId());
            }
        }

        @Override // com.exteragram.messenger.plugins.ui.components.PluginCellDelegate
        public void deletePlugin() {
            if (this.$plugin.getIsNotResponding()) {
                PluginsWatchdog.INSTANCE.showNotRespondingAlert(this.$plugin);
                return;
            }
            AlertDialog.Builder message = new AlertDialog.Builder(this.this$0.getParentActivity(), this.this$0.getResourceProvider()).setTitle("Delete Plugin").setMessage(AndroidUtilities.replaceTags("Are you sure you want to delete " + this.$plugin.getName() + "?"));
            String string = LocaleController.getString(R.string.Delete);
            final Plugin plugin = this.$plugin;
            final PluginsActivity pluginsActivity = this.this$0;
            AlertDialog alertDialogCreate = message.setPositiveButton(string, new AlertDialog.OnButtonClickListener() { // from class: com.exteragram.messenger.plugins.ui.PluginsActivity$createPluginItem$1$$ExternalSyntheticLambda4
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i) {
                    PluginsController.INSTANCE.getInstance().deletePlugin(plugin.getId(), new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.PluginsActivity$createPluginItem$1$$ExternalSyntheticLambda1
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            PluginsActivity.AnonymousClass1.deletePlugin$lambda$0$0(pluginsActivity, (String) obj);
                        }
                    });
                }
            }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).create();
            alertDialogCreate.show();
            View button = alertDialogCreate.getButton(-1);
            TextView textView = button instanceof TextView ? (TextView) button : null;
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void deletePlugin$lambda$0$0(final PluginsActivity pluginsActivity, final String str) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginsActivity$createPluginItem$1$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PluginsActivity.AnonymousClass1.deletePlugin$lambda$0$0$0(pluginsActivity, str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void deletePlugin$lambda$0$0$0(PluginsActivity pluginsActivity, String str) {
            if (pluginsActivity.fragmentView == null) {
                return;
            }
            ((BasePreferencesActivity) pluginsActivity).listView.adapter.update(true);
            if (str != null) {
                BulletinFactory.of(pluginsActivity).createSimpleBulletin(R.raw.error, str).show();
            }
        }

        @Override // com.exteragram.messenger.plugins.ui.components.PluginCellDelegate
        public void togglePlugin(View view) {
            if (this.$plugin.getIsNotResponding()) {
                PluginsWatchdog.INSTANCE.showNotRespondingAlert(this.$plugin);
                return;
            }
            final PluginCell pluginCell = (PluginCell) view;
            final boolean z = !this.$plugin.isEnabled();
            PluginsController companion = PluginsController.INSTANCE.getInstance();
            String id = this.$plugin.getId();
            final PluginsActivity pluginsActivity = this.this$0;
            final Plugin plugin = this.$plugin;
            companion.setPluginEnabled(id, z, obj -> {
                final String str = obj instanceof String ? (String) obj : null;
                AndroidUtilities.runOnUIThread(() -> PluginsActivity.AnonymousClass1.togglePlugin$lambda$1$0(pluginsActivity, str, z, plugin, pluginCell));
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void togglePlugin$lambda$1$0(final PluginsActivity pluginsActivity, final String str, boolean z, Plugin plugin, PluginCell pluginCell) {
            if (pluginsActivity.fragmentView == null || pluginsActivity.getParentActivity() == null) {
                return;
            }
            if (str != null) {
                BulletinFactory.of(pluginsActivity).createSimpleBulletin(R.raw.error, (z ? "Failed to enable " : "Failed to disable ") + plugin.getName(), LocaleUtils.createCopySpan(pluginsActivity), () -> PluginsActivity.AnonymousClass1.togglePlugin$lambda$1$0$0(str, pluginsActivity)).show();
            } else {
                pluginCell.setChecked(z, true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void togglePlugin$lambda$1$0$0(String str, PluginsActivity pluginsActivity) {
            if (AndroidUtilities.addToClipboard(str)) {
                BulletinFactory.of(pluginsActivity).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();
            }
        }

        @Override // com.exteragram.messenger.plugins.ui.components.PluginCellDelegate
        public void openPluginSettings() {
            PluginsController.INSTANCE.openPluginSettings(this.$plugin.getId());
        }

        @Override // com.exteragram.messenger.plugins.ui.components.PluginCellDelegate
        public void pinPlugin(View view) {
            PluginsController.Companion companion = PluginsController.INSTANCE;
            boolean zIsPluginPinned = companion.isPluginPinned(this.$plugin.getId());
            companion.setPluginPinned(this.$plugin.getId(), !zIsPluginPinned);
            ((PluginCell) view).setPinned(!zIsPluginPinned);
            ((BasePreferencesActivity) this.this$0).listView.adapter.update(true);
            ((BasePreferencesActivity) this.this$0).listView.smoothScrollToPosition(0);
        }

        @Override // com.exteragram.messenger.plugins.ui.components.PluginCellDelegate
        public boolean canOpenInExternalApp() {
            PluginsController.PluginsEngine pluginEngine = PluginsController.INSTANCE.getInstance().getPluginEngine(this.$plugin.getId());
            return pluginEngine != null && pluginEngine.canOpenInExternalApp();
        }
    }

    private final UItem createPluginItem(Plugin plugin) {
        return PluginCell.Factory.INSTANCE.asPlugin(plugin, new AnonymousClass1(plugin, this));
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public void onClick(UItem item, View view, int position, float x, float y) {
        if (item.viewType == 9) {
            togglePluginsEngine(view, item);
        }
    }

    private final void togglePluginsEngine(View view, UItem item) {
        if (this.isSwitchingEngineState) {
            return;
        }
        this.isSwitchingEngineState = true;
        ExteraConfig.setPluginsEngine(!ExteraConfig.getPluginsEngine());
        TextCheckCell textCheckCell = (TextCheckCell) view;
        boolean pluginsEngine = ExteraConfig.getPluginsEngine();
        item.checked = pluginsEngine;
        textCheckCell.setChecked(pluginsEngine);
        Runnable runnable = () -> AndroidUtilities.runOnUIThread(() -> togglePluginsEngine$lambda$1$0(PluginsActivity.this));
        if (ExteraConfig.getPluginsEngine()) {
            PluginsController.INSTANCE.getInstance().init(runnable);
        } else {
            PluginsController.INSTANCE.getInstance().shutdown(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void togglePluginsEngine$lambda$1$0(PluginsActivity pluginsActivity) {
        if (pluginsActivity.fragmentView == null) {
            return;
        }
        if (pluginsActivity.searching) {
            pluginsActivity.actionBar.closeSearchField();
        }
        AndroidUtilities.updateViewVisibilityAnimated(pluginsActivity.searchItem, ExteraConfig.getPluginsEngine() && !PluginsController.INSTANCE.getInstance().getPlugins().isEmpty(), 0.5f, true);
        pluginsActivity.listView.adapter.update(true);
        pluginsActivity.isSwitchingEngineState = false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public int getNavigationBarColor() {
        return Theme.getColor(Theme.key_windowBackgroundGray);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pluginsUpdated);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.reloadInterface);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pluginIsNotResponding);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pluginsUpdated);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.reloadInterface);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pluginIsNotResponding);
        super.onFragmentDestroy();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.pluginsUpdated) {
            AndroidUtilities.updateViewVisibilityAnimated(this.searchItem, ExteraConfig.getPluginsEngine() && !PluginsController.INSTANCE.getInstance().getPlugins().isEmpty(), 0.5f, true);
            this.listView.adapter.update(true);
        } else if (id == NotificationCenter.reloadInterface) {
            this.listView.invalidateViews();
        } else if (id == NotificationCenter.pluginIsNotResponding) {
            this.listView.adapter.update(true);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed(boolean invoked) {
        if (!this.searching) {
            return super.onBackPressed(invoked);
        }
        if (!invoked) {
            return false;
        }
        this.actionBar.closeSearchField();
        return false;
    }
}

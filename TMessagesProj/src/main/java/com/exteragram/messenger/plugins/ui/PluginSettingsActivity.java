package com.exteragram.messenger.plugins.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chaquo.python.PyObject;
import com.exteragram.messenger.plugins.Plugin;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.PythonPluginsEngine;
import com.exteragram.messenger.plugins.models.CustomSetting;
import com.exteragram.messenger.plugins.models.DividerSetting;
import com.exteragram.messenger.plugins.models.EditTextSetting;
import com.exteragram.messenger.plugins.models.HeaderSetting;
import com.exteragram.messenger.plugins.models.InputSetting;
import com.exteragram.messenger.plugins.models.SelectorSetting;
import com.exteragram.messenger.plugins.models.SettingItem;
import com.exteragram.messenger.plugins.models.SwitchSetting;
import com.exteragram.messenger.plugins.models.TextSetting;
import com.exteragram.messenger.plugins.ui.components.PluginEditTextCell;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.exteragram.messenger.utils.text.LocaleUtils;
import com.sun.jna.Callback;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.RadioColorCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;

/* JADX INFO: loaded from: classes.dex */
public final class PluginSettingsActivity extends BasePreferencesActivity implements NotificationCenter.NotificationCenterDelegate {
    private PyObject createSubFragmentCallback;
    private String customTitle;
    private Plugin plugin;
    private ActionBarMenuItem resetItem;
    private List<? extends SettingItem> settingItems;
    private String settingsLinkPrefix;
    private Integer targetSettingItemId;
    private String targetSettingName;

    public PluginSettingsActivity() {
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public PluginSettingsActivity(Plugin plugin) {
        this();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-130611972163119L);
        this.plugin = plugin;
        this.customTitle = null;
        this.settingItems = null;
        this.createSubFragmentCallback = null;
        this.targetSettingName = null;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public PluginSettingsActivity(Plugin plugin, String str) {
        this();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-130581907392047L);
        this.plugin = plugin;
        this.customTitle = null;
        this.settingItems = null;
        this.createSubFragmentCallback = null;
        this.targetSettingName = str;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public PluginSettingsActivity(Plugin plugin, String str, List<? extends SettingItem> list, PyObject pyObject) {
        this();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-130672101705263L);
        this.plugin = plugin;
        this.customTitle = str;
        this.settingItems = list;
        this.createSubFragmentCallback = pyObject;
        this.targetSettingName = null;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public PluginSettingsActivity(Plugin plugin, String str, List<? extends SettingItem> list, PyObject pyObject, String str2) {
        this();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-130642036934191L);
        this.plugin = plugin;
        this.customTitle = str;
        this.settingItems = list;
        this.createSubFragmentCallback = pyObject;
        this.targetSettingName = str2;
    }

    public final PluginSettingsActivity setSettingsLinkPrefix(String settingsLinkPrefix) {
        this.settingsLinkPrefix = settingsLinkPrefix;
        return this;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        String str = this.customTitle;
        if (str != null) {
            return str;
        }
        Plugin plugin = this.plugin;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-130663511770671L);
            plugin = null;
        }
        return plugin.getName();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pluginSettingsRegistered);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pluginSettingsUnregistered);
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pluginSettingsRegistered);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pluginSettingsUnregistered);
        super.onFragmentDestroy();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int id, int account, Object... args) {
        UniversalAdapter universalAdapter;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-130770885953071L);
        Plugin plugin = null;
        if (id == NotificationCenter.pluginSettingsRegistered) {
            Object objFirstOrNull = ArraysKt.firstOrNull(args);
            String str = objFirstOrNull instanceof String ? (String) objFirstOrNull : null;
            if (str != null) {
                Plugin plugin2 = this.plugin;
                if (plugin2 == null) {
                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-130715051378223L);
                    plugin2 = null;
                }
                if (!Intrinsics.areEqual(plugin2.getId(), str)) {
                    return;
                }
            }
            final PyObject pyObject = this.createSubFragmentCallback;
            if (pyObject != null) {
                PluginsController.INSTANCE.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        PluginSettingsActivity.m1334$r8$lambda$D4rjU7ABfWQR5leqQiROpDYjDw(PluginSettingsActivity.this, pyObject);
                    }
                });
                return;
            }
            UniversalRecyclerView universalRecyclerView = this.listView;
            if (universalRecyclerView != null && (universalAdapter = universalRecyclerView.adapter) != null) {
                universalAdapter.update(true);
            }
            ActionBarMenuItem actionBarMenuItem = this.resetItem;
            if (actionBarMenuItem != null) {
                PluginsController companion = PluginsController.INSTANCE.getInstance();
                Plugin plugin3 = this.plugin;
                if (plugin3 == null) {
                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-130822425560623L);
                } else {
                    plugin = plugin3;
                }
                AndroidUtilities.updateViewVisibilityAnimated(actionBarMenuItem, companion.hasPluginSettingsPreferences(plugin.getId()), 0.5f, true);
                return;
            }
            return;
        }
        if (id == NotificationCenter.pluginSettingsUnregistered) {
            Object objFirstOrNull2 = ArraysKt.firstOrNull(args);
            String str2 = objFirstOrNull2 instanceof String ? (String) objFirstOrNull2 : null;
            if (str2 == null) {
                return;
            }
            Plugin plugin4 = this.plugin;
            if (plugin4 == null) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-130775180920367L);
                plugin4 = null;
            }
            if (Intrinsics.areEqual(plugin4.getId(), str2)) {
                PluginsController companion2 = PluginsController.INSTANCE.getInstance();
                Plugin plugin5 = this.plugin;
                if (plugin5 == null) {
                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-130332799288879L);
                } else {
                    plugin = plugin5;
                }
                if (companion2.hasPluginSettings(plugin.getId())) {
                    return;
                }
                finishFragment();
            }
        }
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [T, java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r7v4, types: [T, java.util.List] */
    /* JADX INFO: renamed from: $r8$lambda$D4rjU7ABfWQR5leqQ-iROpDYjDw, reason: not valid java name */
    public static void m1334$r8$lambda$D4rjU7ABfWQR5leqQiROpDYjDw(final PluginSettingsActivity pluginSettingsActivity, PyObject pyObject) {
        PluginsController.Companion companion = PluginsController.INSTANCE;
        PluginsController companion2 = companion.getInstance();
        Plugin plugin = pluginSettingsActivity.plugin;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-126080781665839L);
            plugin = null;
        }
        if (companion2.isPluginActive$TMessagesProj(plugin)) {
            final Ref.ObjectRef objectRef = new Ref.ObjectRef();
            objectRef.element = new ArrayList();
            try {
                PyObject pyObjectCall = pyObject.call(new Object[0]);
                if (pyObjectCall != null) {
                    PluginsController.PluginsEngine pluginsEngine = companion.getEngines().get(Deobfuscator$exteraGramDev$TMessagesProj.getString(-126033537025583L));
                    PythonPluginsEngine pythonPluginsEngine = pluginsEngine instanceof PythonPluginsEngine ? (PythonPluginsEngine) pluginsEngine : null;
                    if (pythonPluginsEngine == null) {
                        return;
                    }
                    List<PyObject> listAsList = pyObjectCall.asList();
                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-126140911207983L);
                    try {
                        objectRef.element = pythonPluginsEngine.parsePySettingDefinitions(listAsList);
                    } catch (Throwable th) {}
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        PluginSettingsActivity.didReceivedNotification$lambda$0$0(null, objectRef);
                    }
                });
            } catch (Exception unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void didReceivedNotification$lambda$0$0(PluginSettingsActivity pluginSettingsActivity, Ref.ObjectRef objectRef) {
        UniversalAdapter universalAdapter;
        pluginSettingsActivity.settingItems = (List) objectRef.element;
        UniversalRecyclerView universalRecyclerView = pluginSettingsActivity.listView;
        if (universalRecyclerView == null || (universalAdapter = universalRecyclerView.adapter) == null) {
            return;
        }
        universalAdapter.update(true);
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-130354274125359L);
        View viewCreateView = super.createView(context);
        if (this.createSubFragmentCallback == null) {
            ActionBarMenu actionBarMenuCreateMenu = this.actionBar.createMenu();
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-130319914386991L);
            final ActionBarMenuItem actionBarMenuItemAddItem = actionBarMenuCreateMenu.addItem(0, R.drawable.msg_reset);
            actionBarMenuItemAddItem.setContentDescription(LocaleController.getString(R.string.Reset));
            PluginsController companion = PluginsController.INSTANCE.getInstance();
            Plugin plugin = this.plugin;
            if (plugin == null) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-130388633863727L);
                plugin = null;
            }
            AndroidUtilities.updateViewVisibilityAnimated(actionBarMenuItemAddItem, companion.hasPluginSettingsPreferences(plugin.getId()), 0.5f, false);
            actionBarMenuItemAddItem.setTag(null);
            actionBarMenuItemAddItem.setOnClickListener(view -> createView$lambda$0$0(actionBarMenuItemAddItem, PluginSettingsActivity.this, view));
            this.resetItem = actionBarMenuItemAddItem;
        }
        checkTargetSetting();
        this.fragmentView = viewCreateView;
        return viewCreateView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void createView$lambda$0$0(final ActionBarMenuItem actionBarMenuItem, final PluginSettingsActivity pluginSettingsActivity, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(actionBarMenuItem.getContext(), pluginSettingsActivity.resourceProvider);
        Plugin plugin = pluginSettingsActivity.plugin;
        builder.setMessage("Reset settings for " + (plugin != null ? plugin.getName() : "plugin") + "?");
        builder.setPositiveButton(LocaleController.getString(R.string.Reset), new AlertDialog.OnButtonClickListener() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda3
            @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
            public final void onClick(AlertDialog alertDialog, int i2) {
                PluginSettingsActivity.createView$lambda$0$0$0(null, actionBarMenuItem, alertDialog, i2);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        AlertDialog alertDialogCreate = builder.create();
        pluginSettingsActivity.showDialog(alertDialogCreate);
        View button = alertDialogCreate.getButton(-1);
        TextView textView = button instanceof TextView ? (TextView) button : null;
        if (textView != null) {
            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void createView$lambda$0$0$0(final PluginSettingsActivity pluginSettingsActivity, ActionBarMenuItem actionBarMenuItem, AlertDialog alertDialog, int i) {
        View viewFindFocus;
        View view = pluginSettingsActivity.fragmentView;
        if (view != null && (viewFindFocus = view.findFocus()) != null) {
            viewFindFocus.clearFocus();
        }
        AndroidUtilities.updateViewVisibilityAnimated(actionBarMenuItem, false, 0.5f, true);
        PluginsController.INSTANCE.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PluginSettingsActivity.createView$lambda$0$0$0$0(null);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void createView$lambda$0$0$0$0(final PluginSettingsActivity pluginSettingsActivity) {
        PluginsController.Companion companion = PluginsController.INSTANCE;
        PluginsController companion2 = companion.getInstance();
        Plugin plugin = pluginSettingsActivity.plugin;
        Plugin plugin2 = null;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-124564658210351L);
            plugin = null;
        }
        PluginsController.clearPluginSettingsPreferences$default(companion2, plugin.getId(), false, 2, null);
        PluginsController companion3 = companion.getInstance();
        Plugin plugin3 = pluginSettingsActivity.plugin;
        if (plugin3 == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-124534593439279L);
        } else {
            plugin2 = plugin3;
        }
        companion3.loadPluginSettings(plugin2.getId());
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                PluginSettingsActivity.createView$lambda$0$0$0$0$0(null);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void createView$lambda$0$0$0$0$0(PluginSettingsActivity pluginSettingsActivity) {
        Plugin plugin = pluginSettingsActivity.plugin;
        BulletinFactory.of(pluginSettingsActivity).createSimpleBulletin(R.raw.info, "Reset settings for " + (plugin != null ? plugin.getName() : "plugin")).show();
    }

    public final void checkTargetSetting() {
        Integer num = this.targetSettingItemId;
        if (num != null) {
            scrollToItem(num.intValue());
            this.targetSettingItemId = null;
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        int identifier;
        UItem uItemAs;
        UItem item;
        UItem uItemAsButton;
        CharSequence string;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-130496008046127L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-130453058373167L);
        List<SettingItem> pluginSettingsList = (List) this.settingItems;
        if (pluginSettingsList == null) {
            PluginsController companion = PluginsController.INSTANCE.getInstance();
            Plugin plugin = this.plugin;
            if (plugin == null) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-130556137588271L);
                plugin = null;
            }
            pluginSettingsList = companion.getPluginSettingsList(plugin.getId());
        }
        List list = pluginSettingsList;
        if (list == null || list.isEmpty()) {
            return;
        }
        boolean z = false;
        for (SettingItem settingItem : pluginSettingsList) {
            uItemAs = null;
            if (TextUtils.isEmpty(settingItem.getIcon())) {
                identifier = 0;
            } else {
                Context context = ApplicationLoader.applicationContext;
                identifier = context.getResources().getIdentifier(settingItem.getIcon(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-130508892948015L), context.getPackageName());
            }
            try {
                String type = settingItem.getType();
                switch (type.hashCode()) {
                    case -1866021310:
                        if (type.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-128932639950383L))) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-127631264859695L);
                            EditTextSetting editTextSetting = (EditTextSetting) settingItem;
                            if (editTextSetting.getKey() != null && editTextSetting.getHint() != null) {
                                PluginEditTextCell.Factory.Companion companion2 = PluginEditTextCell.Factory.INSTANCE;
                                Plugin plugin2 = this.plugin;
                                if (plugin2 == null) {
                                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-128589042566703L);
                                    plugin2 = null;
                                }
                                uItemAs = companion2.as(plugin2, editTextSetting);
                                break;
                            }
                        }
                        uItemAs = null;
                        break;
                    case -1349088399:
                        if (type.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-129009949361711L))) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-127180293293615L);
                            CustomSetting customSetting = (CustomSetting) settingItem;
                            item = customSetting.getItem();
                            if (item == null) {
                                CustomSetting.Factory<?> factory = customSetting.getFactory();
                                if (factory != null) {
                                    Plugin plugin3 = this.plugin;
                                    if (plugin3 == null) {
                                        Deobfuscator$exteraGramDev$TMessagesProj.getString(-125844558464559L);
                                        plugin3 = null;
                                    }
                                    item = factory.create(plugin3, customSetting, customSetting.getFactoryArgs());
                                } else {
                                    item = null;
                                }
                            }
                            if (item != null) {
                                item.object = customSetting;
                            }
                            uItemAs = item;
                        } else {
                            uItemAs = null;
                        }
                        break;
                    case -1221270899:
                        if (type.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-129100143674927L))) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-129151683282479L);
                            HeaderSetting headerSetting = (HeaderSetting) settingItem;
                            if (headerSetting.getText() != null) {
                                item = UItem.asHeader(headerSetting.getText());
                                item.object = headerSetting;
                                uItemAs = item;
                            }
                            break;
                        }
                        uItemAs = null;
                        break;
                    case -889473228:
                        if (type.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-129130208445999L))) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-129551115241007L);
                            SwitchSetting switchSetting = (SwitchSetting) settingItem;
                            if (switchSetting.getKey() != null && switchSetting.getText() != null) {
                                PluginsController companion3 = PluginsController.INSTANCE.getInstance();
                                Plugin plugin4 = this.plugin;
                                if (plugin4 == null) {
                                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-129452330993199L);
                                    plugin4 = null;
                                }
                                boolean pluginSettingBoolean = companion3.getPluginSettingBoolean(plugin4.getId(), switchSetting.getKey(), switchSetting.getDefaultValue());
                                UItem uItemAsCheck = UItem.asCheck(0, switchSetting.getText());
                                uItemAsCheck.setChecked(pluginSettingBoolean);
                                if (switchSetting.getSubtext() != null) {
                                    uItemAsCheck.subtext = switchSetting.getSubtext();
                                }
                                if (identifier != 0) {
                                    uItemAsCheck.iconResId = identifier;
                                }
                                uItemAsCheck.object2 = switchSetting.getKey();
                                uItemAsCheck.object = switchSetting;
                                uItemAs = uItemAsCheck;
                                break;
                            }
                        }
                        uItemAs = null;
                        break;
                    case 3556653:
                        if (type.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-129168863151663L))) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-126879645582895L);
                            TextSetting textSetting = (TextSetting) settingItem;
                            uItemAsButton = UItem.asButton(0, textSetting.getText());
                            uItemAsButton.object = textSetting;
                            if (identifier != 0) {
                                uItemAsButton.iconResId = identifier;
                            }
                            uItemAsButton.accent = textSetting.getAccent();
                            uItemAsButton.red = textSetting.getRed();
                            if (!TextUtils.isEmpty(textSetting.getSubtext())) {
                                uItemAsButton.subtext = textSetting.getSubtext();
                                uItemAsButton.intValue = 60;
                            }
                            uItemAs = uItemAsButton;
                        } else {
                            uItemAs = null;
                        }
                        break;
                    case 100358090:
                        if (type.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-128975589623343L))) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-129422266222127L);
                            InputSetting inputSetting = (InputSetting) settingItem;
                            if (inputSetting.getKey() != null && inputSetting.getText() != null) {
                                PluginsController companion4 = PluginsController.INSTANCE.getInstance();
                                Plugin plugin5 = this.plugin;
                                if (plugin5 == null) {
                                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-127678509499951L);
                                    plugin5 = null;
                                }
                                uItemAsButton = UItem.asButton(0, inputSetting.getText(), companion4.getPluginSettingString(plugin5.getId(), inputSetting.getKey(), inputSetting.getDefaultValue()));
                                if (identifier != 0) {
                                    uItemAsButton.iconResId = identifier;
                                }
                                uItemAsButton.object2 = inputSetting.getKey();
                                uItemAsButton.object = inputSetting;
                                uItemAs = uItemAsButton;
                                break;
                            }
                        }
                        uItemAs = null;
                        break;
                    case 1191572447:
                        if (type.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-129070078903855L))) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-128129481066031L);
                            SelectorSetting selectorSetting = (SelectorSetting) settingItem;
                            if (selectorSetting.getKey() != null && selectorSetting.getText() != null) {
                                if (!(selectorSetting.getItems().length == 0)) {
                                    PluginsController.Companion companion5 = PluginsController.INSTANCE;
                                    PluginsController companion6 = companion5.getInstance();
                                    Plugin plugin6 = this.plugin;
                                    if (plugin6 == null) {
                                        Deobfuscator$exteraGramDev$TMessagesProj.getString(-126819516040751L);
                                        plugin6 = null;
                                    }
                                    int pluginSettingInt = companion6.getPluginSettingInt(plugin6.getId(), selectorSetting.getKey(), selectorSetting.getDefaultValue());
                                    if (pluginSettingInt < 0 || pluginSettingInt >= selectorSetting.getItems().length) {
                                        pluginSettingInt = Math.max(0, Math.min(selectorSetting.getDefaultValue(), selectorSetting.getItems().length - 1));
                                        PluginsController companion7 = companion5.getInstance();
                                        Plugin plugin7 = this.plugin;
                                        if (plugin7 == null) {
                                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-126909710353967L);
                                            plugin7 = null;
                                        }
                                        companion7.setPluginSetting(plugin7.getId(), selectorSetting.getKey(), Integer.valueOf(pluginSettingInt));
                                    }
                                    uItemAsButton = UItem.asButton(0, selectorSetting.getText(), selectorSetting.getItems()[pluginSettingInt]);
                                    uItemAsButton.texts = selectorSetting.getItems();
                                    uItemAsButton.intValue = pluginSettingInt;
                                    if (identifier != 0) {
                                        uItemAsButton.iconResId = identifier;
                                    }
                                    uItemAsButton.object2 = selectorSetting.getKey();
                                    uItemAsButton.object = selectorSetting;
                                    uItemAs = uItemAsButton;
                                }
                            }
                            break;
                        }
                        uItemAs = null;
                        break;
                    case 1674318617:
                        if (type.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-129044309100079L))) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-127322027214383L);
                            String text = ((DividerSetting) settingItem).getText();
                            if (text == null || (string = LocaleUtils.fullyFormatText(text, this, null)) == null) {
                                string = Deobfuscator$exteraGramDev$TMessagesProj.getString(-127167408391727L);
                            }
                            uItemAs = UItem.asShadow(string);
                        } else {
                            uItemAs = null;
                        }
                        break;
                    default:
                        uItemAs = null;
                        break;
                }
            } catch (Exception e) {
                Log.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-125402176833071L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-125458011407919L), e);
            }
            if (uItemAs != null) {
                uItemAs.id = getStableId(settingItem);
                SettingItem settingItem2 = uItemAs.object instanceof SettingItem ? (SettingItem) uItemAs.object : null;
                String linkAlias = settingItem2 != null ? settingItem2.getLinkAlias() : null;
                if (!TextUtils.isEmpty(linkAlias) && !TextUtils.isEmpty(this.targetSettingName) && Intrinsics.areEqual(linkAlias, this.targetSettingName)) {
                    this.targetSettingItemId = Integer.valueOf(uItemAs.id);
                    this.targetSettingName = null;
                    z = true;
                }
                items.add(uItemAs);
            }
        }
        if (z || TextUtils.isEmpty(this.targetSettingName)) {
            return;
        }
        BulletinFactory.of(this).createEmojiBulletin("🤷\u200d♂️", "Setting not found").show();
        this.targetSettingName = null;
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public void onClick(UItem item, View view, int position, float x, float y) {
        SettingItem settingItem;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-125543910753839L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-125505256048175L);
        PluginsController.Companion companion = PluginsController.INSTANCE;
        PluginsController companion2 = companion.getInstance();
        Plugin plugin = this.plugin;
        Plugin plugin2 = null;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-125586860426799L);
            plugin = null;
        }
        if (companion2.isPluginActive$TMessagesProj(plugin) && item.object instanceof SettingItem) {
            settingItem = (SettingItem) item.object;
            try {
                if (settingItem instanceof TextSetting) {
                    TextSetting textSetting = (TextSetting) settingItem;
                    PyObject createSubFragmentCallback = textSetting.getCreateSubFragmentCallback();
                    PyObject onClickCallback = textSetting.getOnClickCallback();
                    if (createSubFragmentCallback != null) {
                        openSubFragmentNative(item, createSubFragmentCallback);
                        return;
                    } else if (onClickCallback != null) {
                        onClickCallback.call(view);
                        return;
                    }
                } else if (settingItem instanceof CustomSetting) {
                    CustomSetting customSetting = (CustomSetting) settingItem;
                    PyObject createSubFragmentCallback2 = customSetting.getCreateSubFragmentCallback();
                    CustomSetting.Factory<?> factory = customSetting.getFactory();
                    PyObject onClickCallback2 = customSetting.getOnClickCallback();
                    if (createSubFragmentCallback2 != null) {
                        openSubFragmentNative(item, createSubFragmentCallback2);
                        return;
                    }
                    if (factory == null) {
                        if (onClickCallback2 != null) {
                            onClickCallback2.call(view);
                            return;
                        }
                        return;
                    } else {
                        Plugin plugin3 = this.plugin;
                        if (plugin3 == null) {
                            Deobfuscator$exteraGramDev$TMessagesProj.getString(-125556795655727L);
                        } else {
                            plugin2 = plugin3;
                        }
                        factory.onClick(plugin2, item, view);
                        return;
                    }
                }
                Object obj = item.object2;
                final String str = obj instanceof String ? (String) obj : null;
                if (str == null) {
                    return;
                }
                if (view instanceof TextCheckCell) {
                    TextCheckCell textCheckCell = (TextCheckCell) view;
                    final boolean z = !textCheckCell.isChecked();
                    textCheckCell.setChecked(z);
                    item.setChecked(z);
                    SettingItem settingItem2 = item.object instanceof SettingItem ? (SettingItem) item.object : null;
                    SwitchSetting switchSetting = settingItem2 instanceof SwitchSetting ? (SwitchSetting) settingItem2 : null;
                    final PyObject onChangeCallback = switchSetting != null ? switchSetting.getOnChangeCallback() : null;
                    companion.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda16
                        @Override // java.lang.Runnable
                        public final void run() {
                            PluginSettingsActivity.m1335$r8$lambda$EajWUHITV3f6_0IeGGY3lpPLg8(PluginSettingsActivity.this, str, z, onChangeCallback);
                        }
                    });
                    return;
                }
                if (view instanceof NotificationsCheckCell) {
                    NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell) view;
                    final boolean z2 = !notificationsCheckCell.isChecked();
                    notificationsCheckCell.setChecked(z2);
                    item.setChecked(z2);
                    SettingItem settingItem3 = item.object instanceof SettingItem ? (SettingItem) item.object : null;
                    SwitchSetting switchSetting2 = settingItem3 instanceof SwitchSetting ? (SwitchSetting) settingItem3 : null;
                    final PyObject onChangeCallback2 = switchSetting2 != null ? switchSetting2.getOnChangeCallback() : null;
                    companion.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda17
                        @Override // java.lang.Runnable
                        public final void run() {
                            PluginSettingsActivity.$r8$lambda$26YNrTDTVXmfKlDtiRR_AgdiZz0(PluginSettingsActivity.this, str, z2, onChangeCallback2);
                        }
                    });
                    return;
                }
                if (view instanceof TextCell) {
                    SettingItem settingItem4 = item.object instanceof SettingItem ? (SettingItem) item.object : null;
                    if (settingItem4 instanceof SelectorSetting) {
                        showSelectorDialog(item, view, str);
                    } else if (settingItem4 instanceof InputSetting) {
                        showStringInputDialog(item, view, str);
                    }
                }
            } catch (Exception unused) {
            }
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$EajWUHITV3f6_0I-eGGY3lpPLg8, reason: not valid java name */
    public static void m1335$r8$lambda$EajWUHITV3f6_0IeGGY3lpPLg8(PluginSettingsActivity pluginSettingsActivity, String str, boolean z, PyObject pyObject) {
        PluginsController companion = PluginsController.INSTANCE.getInstance();
        Plugin plugin = pluginSettingsActivity.plugin;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-124594722981423L);
            plugin = null;
        }
        companion.setPluginSettingAndTriggerOnChange(plugin.getId(), str, Boolean.valueOf(z), pyObject);
    }

    public static void $r8$lambda$26YNrTDTVXmfKlDtiRR_AgdiZz0(PluginSettingsActivity pluginSettingsActivity, String str, boolean z, PyObject pyObject) {
        PluginsController companion = PluginsController.INSTANCE.getInstance();
        Plugin plugin = pluginSettingsActivity.plugin;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-124616197817903L);
            plugin = null;
        }
        companion.setPluginSettingAndTriggerOnChange(plugin.getId(), str, Boolean.valueOf(z), pyObject);
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public boolean onLongClick(UItem item, View view, int position, float x, float y) {
        CustomSetting.Factory<?> factory;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-125578270492207L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-126226810553903L);
        PluginsController companion = PluginsController.INSTANCE.getInstance();
        Plugin plugin = this.plugin;
        Plugin plugin2 = null;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-126170975979055L);
            plugin = null;
        }
        if (!companion.isPluginActive$TMessagesProj(plugin) || !(item.object instanceof SettingItem)) {
            return false;
        }
        SettingItem settingItem = (SettingItem) item.object;
        PyObject onLongClickCallback = settingItem.getOnLongClickCallback();
        if (onLongClickCallback != null) {
            try {
                onLongClickCallback.call(view);
            } catch (Exception unused) {
            }
            return true;
        }
        if (!TextUtils.isEmpty(settingItem.getLinkAlias())) {
            Plugin plugin3 = this.plugin;
            if (plugin3 == null) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-126278350161455L);
            } else {
                plugin2 = plugin3;
            }
            showCopyLinkOptions(view, settingItem.getLink(plugin2.getId(), this.settingsLinkPrefix));
            return true;
        }
        if (!(settingItem instanceof CustomSetting) || (factory = ((CustomSetting) settingItem).getFactory()) == null) {
            return false;
        }
        try {
            Plugin plugin4 = this.plugin;
            if (plugin4 == null) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-126299824997935L);
            } else {
                plugin2 = plugin4;
            }
            factory.onLongClick(plugin2, item, view);
        } catch (Exception unused2) {
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [T, android.app.Dialog, org.telegram.ui.ActionBar.AlertDialog] */
    private final void showStringInputDialog(UItem item, final View view, final String key) {
        SettingItem settingItem = item.object instanceof SettingItem ? (SettingItem) item.object : null;
        final InputSetting inputSetting = settingItem instanceof InputSetting ? (InputSetting) settingItem : null;
        if (inputSetting == null || getParentActivity() == null) {
            return;
        }
        final Ref.ObjectRef objectRef = new Ref.ObjectRef();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourceProvider);
        builder.setTitle(item.text);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        if (inputSetting.getSubtext() != null) {
            TextView textView = new TextView(getContext());
            textView.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, this.resourceProvider));
            textView.setTextSize(1, 16.0f);
            textView.setText(inputSetting.getSubtext());
            linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 24.0f, 5.0f, 24.0f, 12.0f));
        }
        final EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(getContext());
        editTextBoldCursor.lineYFix = true;
        editTextBoldCursor.setTextSize(1, 18.0f);
        PluginsController companion = PluginsController.INSTANCE.getInstance();
        Plugin plugin = this.plugin;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-126269760226863L);
            plugin = null;
        }
        editTextBoldCursor.setText(companion.getPluginSettingString(plugin.getId(), key, inputSetting.getDefaultValue()));
        editTextBoldCursor.setTextColor(Theme.getColor(Theme.key_dialogTextBlack, this.resourceProvider));
        editTextBoldCursor.setHintColor(Theme.getColor(Theme.key_groupcreate_hintText, this.resourceProvider));
        editTextBoldCursor.setHintText("Enter value");
        editTextBoldCursor.setFocusable(true);
        editTextBoldCursor.setInputType(147457);
        int i = Theme.key_windowBackgroundWhiteInputFieldActivated;
        editTextBoldCursor.setCursorColor(Theme.getColor(i, this.resourceProvider));
        editTextBoldCursor.setLineColors(Theme.getColor(Theme.key_windowBackgroundWhiteInputField, this.resourceProvider), Theme.getColor(i, this.resourceProvider), Theme.getColor(Theme.key_text_RedRegular, this.resourceProvider));
        editTextBoldCursor.setBackground(null);
        editTextBoldCursor.setPadding(0, AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f));
        final Runnable runnable = new Runnable() {
            @Override
            public final void run() {
                PluginSettingsActivity.m1332$r8$lambda$ATxfzrI9UmSnf_gboVo0ib94H8(editTextBoldCursor, objectRef, view, PluginSettingsActivity.this, key, inputSetting);
            }
        };
        linearLayout.addView(editTextBoldCursor, LayoutHelper.createLinear(-1, -2, 24.0f, 0.0f, 24.0f, 10.0f));
        builder.makeCustomMaxHeight();
        builder.setView(linearLayout);
        builder.setWidth(AndroidUtilities.dp(292.0f));
        builder.setPositiveButton(LocaleController.getString(R.string.Done), (alertDialog, i2) -> runnable.run());
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), (alertDialog, i2) -> alertDialog.dismiss());

        AlertDialog dialog = builder.create();
        objectRef.element = dialog;
        dialog.setOnDismissListener(dialogInterface -> AndroidUtilities.hideKeyboard(editTextBoldCursor));
        dialog.setOnShowListener(dialogInterface -> PluginSettingsActivity.$r8$lambda$XATyCfuUD2Re7N0jRXAxTQrfe70(editTextBoldCursor, dialogInterface));
        dialog.setDismissDialogByButtons(false);
        showDialog(dialog);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: renamed from: $r8$lambda$ATxfzrI9UmSnf_gboVo0-ib94H8, reason: not valid java name */
    public static void m1332$r8$lambda$ATxfzrI9UmSnf_gboVo0ib94H8(EditTextBoldCursor editTextBoldCursor, Ref.ObjectRef objectRef, View view, final PluginSettingsActivity pluginSettingsActivity, final String str, final InputSetting inputSetting) {
        Editable text = editTextBoldCursor.getText();
        final String string = (text != null && text.length() > 0) ? text.toString() : "";
        AlertDialog alertDialog = (AlertDialog) objectRef.element;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-124719277033007L);
        ((TextCell) view).setValue(string, true);
        PluginsController.INSTANCE.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                PluginSettingsActivity.showStringInputDialog$lambda$3$0(null, str, string, inputSetting);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void showStringInputDialog$lambda$3$0(PluginSettingsActivity pluginSettingsActivity, String str, String str2, InputSetting inputSetting) {
        PluginsController companion = PluginsController.INSTANCE.getInstance();
        Plugin plugin = pluginSettingsActivity.plugin;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-124392859518511L);
            plugin = null;
        }
        companion.setPluginSettingAndTriggerOnChange(plugin.getId(), str, str2, inputSetting.getOnChangeCallback());
    }

    public static void $r8$lambda$XATyCfuUD2Re7N0jRXAxTQrfe70(EditTextBoldCursor editTextBoldCursor, DialogInterface dialogInterface) {
        editTextBoldCursor.requestFocus();
        editTextBoldCursor.setSelection(editTextBoldCursor.length());
        AndroidUtilities.showKeyboard(editTextBoldCursor);
    }

    private final void showSelectorDialog(UItem item, final View view, final String key) {
        SettingItem settingItem = item.object instanceof SettingItem ? (SettingItem) item.object : null;
        final SelectorSetting selectorSetting = settingItem instanceof SelectorSetting ? (SelectorSetting) settingItem : null;
        if (selectorSetting == null || getParentActivity() == null) {
            return;
        }
        final AtomicReference atomicReference = new AtomicReference();
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(1);
        final String[] items = selectorSetting.getItems();
        int length = items.length;
        for (int j = 0; j < length; j++) {
            final int index = j;
            RadioColorCell radioColorCell = new RadioColorCell(getParentActivity());
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setCheckColor(Theme.getColor(Theme.key_radioBackground), Theme.getColor(Theme.key_dialogRadioBackgroundChecked));
            PluginsController companion = PluginsController.INSTANCE.getInstance();
            Plugin plugin = this.plugin;
            if (plugin == null) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-126359954540079L);
                plugin = null;
            }
            radioColorCell.setTextAndValue(items[index], companion.getPluginSettingInt(plugin.getId(), key, selectorSetting.getDefaultValue()) == index);
            radioColorCell.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector), 2));
            linearLayout.addView(radioColorCell);
            radioColorCell.setOnClickListener(view2 -> PluginSettingsActivity.m1331$r8$lambda$4POcQzJfVWfbcSaEpIiFMUCpp4(atomicReference, view, items, index, PluginSettingsActivity.this, key, selectorSetting, view2));
        }
        AlertDialog alertDialogCreate = new AlertDialog.Builder(getParentActivity()).setTitle(item.text).setView(linearLayout).setNegativeButton(LocaleController.getString(R.string.Cancel), null).create();
        atomicReference.set(alertDialogCreate);
        showDialog(alertDialogCreate);
    }

    /* JADX INFO: renamed from: $r8$lambda$4PO-cQzJfVWfbcSaEpIiFMUCpp4, reason: not valid java name */
    public static void m1331$r8$lambda$4POcQzJfVWfbcSaEpIiFMUCpp4(AtomicReference atomicReference, View view, String[] strArr, final int i, final PluginSettingsActivity pluginSettingsActivity, final String str, final SelectorSetting selectorSetting, View view2) {
        Dialog dialog = (Dialog) atomicReference.get();
        if (dialog != null) {
            dialog.dismiss();
        }
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-124500233700911L);
        ((TextCell) view).setValue(strArr[i], true);
        PluginsController.INSTANCE.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                PluginSettingsActivity.showSelectorDialog$lambda$2$0(null, str, 0, selectorSetting);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void showSelectorDialog$lambda$2$0(PluginSettingsActivity pluginSettingsActivity, String str, int i, SelectorSetting selectorSetting) {
        PluginsController companion = PluginsController.INSTANCE.getInstance();
        Plugin plugin = pluginSettingsActivity.plugin;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-125342047290927L);
            plugin = null;
        }
        companion.setPluginSettingAndTriggerOnChange(plugin.getId(), str, Integer.valueOf(i), selectorSetting.getOnChangeCallback());
    }

    /* JADX WARN: Code duplicated, block: B:42:0x00f3  */
    private final int getStableId(SettingItem item) {
        Integer numValueOf;
        int iHashCode;
        if (item instanceof SwitchSetting) {
            return Objects.hash(Deobfuscator$exteraGramDev$TMessagesProj.getString(-126329889769007L), ((SwitchSetting) item).getKey());
        }
        if (item instanceof InputSetting) {
            return Objects.hash(Deobfuscator$exteraGramDev$TMessagesProj.getString(-126420084082223L), ((InputSetting) item).getKey());
        }
        if (item instanceof EditTextSetting) {
            return Objects.hash(Deobfuscator$exteraGramDev$TMessagesProj.getString(-126377134409263L), ((EditTextSetting) item).getKey());
        }
        if (item instanceof SelectorSetting) {
            return Objects.hash(Deobfuscator$exteraGramDev$TMessagesProj.getString(-126407199180335L), ((SelectorSetting) item).getKey());
        }
        if (item instanceof HeaderSetting) {
            return Objects.hash(Deobfuscator$exteraGramDev$TMessagesProj.getString(-125956227614255L), ((HeaderSetting) item).getText());
        }
        if (item instanceof DividerSetting) {
            return Objects.hash(Deobfuscator$exteraGramDev$TMessagesProj.getString(-125926162843183L), ((DividerSetting) item).getText());
        }
        if (item instanceof TextSetting) {
            return Objects.hash(Deobfuscator$exteraGramDev$TMessagesProj.getString(-126029242058287L), ((TextSetting) item).getText());
        }
        if (item instanceof CustomSetting) {
            String string = Deobfuscator$exteraGramDev$TMessagesProj.getString(-125973407483439L);
            numValueOf = null;
            CustomSetting customSetting = (CustomSetting) item;
            UItem item2 = customSetting.getItem();
            if (item2 == null) {
                CustomSetting.Factory<?> factory = customSetting.getFactory();
                if (factory != null) {
                    iHashCode = factory.hashCode();
                    numValueOf = Integer.valueOf(iHashCode);
                } else {
                    numValueOf = null;
                }
                PyObject factoryArgs = customSetting.getFactoryArgs();
                return Objects.hash(string, numValueOf, factoryArgs != null ? Integer.valueOf(factoryArgs.hashCode()) : null);
            }
            iHashCode = item2.id;
            numValueOf = Integer.valueOf(iHashCode);
            PyObject factoryArgs2 = customSetting.getFactoryArgs();
            return Objects.hash(string, numValueOf, factoryArgs2 != null ? Integer.valueOf(factoryArgs2.hashCode()) : null);
        }
        return item.hashCode();
    }

    private final void openSubFragmentNative(final UItem item, final PyObject callback) {
        PluginsController.INSTANCE.runOnPluginsQueue(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                PluginSettingsActivity.$r8$lambda$py2IHrU6RevEMbA19W9LY0CNXXI(PluginSettingsActivity.this, callback, item);
            }
        });
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [T, java.util.List] */
    /* JADX WARN: Type inference failed for: r2v2, types: [T, java.util.ArrayList] */
    public static void $r8$lambda$py2IHrU6RevEMbA19W9LY0CNXXI(final PluginSettingsActivity pluginSettingsActivity, final PyObject pyObject, final UItem uItem) {
        PluginsController.Companion companion = PluginsController.INSTANCE;
        PluginsController companion2 = companion.getInstance();
        Plugin plugin = pluginSettingsActivity.plugin;
        if (plugin == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-125294802650671L);
            plugin = null;
        }
        if (companion2.isPluginActive$TMessagesProj(plugin)) {
            final Ref.ObjectRef objectRef = new Ref.ObjectRef();
            objectRef.element = new ArrayList();
            try {
                PyObject pyObjectCall = pyObject.call(new Object[0]);
                if (pyObjectCall != null) {
                    PluginsController.PluginsEngine pluginsEngine = companion.getEngines().get(Deobfuscator$exteraGramDev$TMessagesProj.getString(-124852421019183L));
                    PythonPluginsEngine pythonPluginsEngine = pluginsEngine instanceof PythonPluginsEngine ? (PythonPluginsEngine) pluginsEngine : null;
                    if (pythonPluginsEngine == null) {
                        return;
                    }
                    List<PyObject> listAsList = pyObjectCall.asList();
                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-124805176378927L);
                    try {
                        objectRef.element = pythonPluginsEngine.parsePySettingDefinitions(listAsList);
                    } catch (Throwable th) {}
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.PluginSettingsActivity$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        PluginSettingsActivity.openSubFragmentNative$lambda$0$0(objectRef, uItem, pluginSettingsActivity, pyObject);
                    }
                });
            } catch (Exception e) {
                FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-124925435463215L), e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void openSubFragmentNative$lambda$0$0(Ref.ObjectRef objectRef, UItem uItem, PluginSettingsActivity pluginSettingsActivity, PyObject pyObject) {
        String name;
        String string;
        if (((Collection) objectRef.element).isEmpty()) {
            return;
        }
        Plugin plugin = null;
        if (!TextUtils.isEmpty(uItem.text)) {
            name = uItem.text.toString();
        } else {
            name = pluginSettingsActivity.customTitle;
            if (name == null) {
                Plugin plugin2 = pluginSettingsActivity.plugin;
                if (plugin2 == null) {
                    Deobfuscator$exteraGramDev$TMessagesProj.getString(-125037104612911L);
                    plugin2 = null;
                }
                name = plugin2.getName();
            }
        }
        Plugin plugin3 = pluginSettingsActivity.plugin;
        if (plugin3 == null) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-125007039841839L);
        } else {
            plugin = plugin3;
        }
        PluginSettingsActivity pluginSettingsActivity2 = new PluginSettingsActivity(plugin, name, (List) objectRef.element, pyObject);
        StringBuilder sb = new StringBuilder();
        String str = pluginSettingsActivity.settingsLinkPrefix;
        sb.append(str != null ? str.concat(":") : Deobfuscator$exteraGramDev$TMessagesProj.getString(-125028514678319L));
        SettingItem settingItem = uItem.object instanceof SettingItem ? (SettingItem) uItem.object : null;
        if (settingItem == null || (string = settingItem.getLinkAlias()) == null) {
            string = Deobfuscator$exteraGramDev$TMessagesProj.getString(-123460851615279L);
        }
        sb.append(string);
        pluginSettingsActivity.presentFragment(pluginSettingsActivity2.setSettingsLinkPrefix(sb.toString()));
    }
}

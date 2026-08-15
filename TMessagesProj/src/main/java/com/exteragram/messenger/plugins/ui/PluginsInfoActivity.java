package com.exteragram.messenger.plugins.ui;

import android.content.SharedPreferences;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.PythonPluginsEngine;
import com.exteragram.messenger.preferences.BasePreferencesActivity;
import com.exteragram.messenger.utils.text.LocaleUtils;
import com.google.android.exoplayer2.util.Consumer;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
import kotlin.jvm.internal.SourceDebugExtension;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

/* JADX INFO: loaded from: classes.dex */
@SourceDebugExtension({"SMAP\nPluginsInfoActivity.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PluginsInfoActivity.kt\ncom/exteragram/messenger/plugins/ui/PluginsInfoActivity\n+ 2 SharedPreferences.kt\nandroidx/core/content/SharedPreferencesKt\n*L\n1#1,294:1\n41#2,12:295\n*S KotlinDebug\n*F\n+ 1 PluginsInfoActivity.kt\ncom/exteragram/messenger/plugins/ui/PluginsInfoActivity\n*L\n253#1:295,12\n*E\n"})
public final class PluginsInfoActivity extends BasePreferencesActivity implements NotificationCenter.NotificationCenterDelegate {

    /* JADX INFO: loaded from: classes4.dex */
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[PreferenceItem.values().length];
            try {
                iArr[PreferenceItem.DEVELOPER_MODE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[PreferenceItem.COMPACT_VIEW.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[PreferenceItem.SAFE_MODE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[PreferenceItem.PLUGINS_DISABLE_ART_OPTS.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[PreferenceItem.SDK_AUTO_UPDATE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                iArr[PreferenceItem.SDK_BETA_VERSIONS.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: loaded from: classes4.dex */
    public enum PreferenceItem {
        DEVELOPER_MODE,
        COMPACT_VIEW,
        SAFE_MODE,
        SDK_AUTO_UPDATE,
        SDK_BETA_VERSIONS,
        CHECK_SDK_UPDATES,
        RESTORE_SDK_FROM_APK,
        DOCUMENTATION,
        TRUSTED_PLUGINS,
        PLUGINS_DISABLE_ART_OPTS,
        SDK_HEADER;

        private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries(values());

        public static EnumEntries<PreferenceItem> getEntries() {
            return $ENTRIES;
        }

        public final int getId() {
            return ordinal() + 1;
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public String getTitle() {
        return "Plugins Engine";
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pluginsPySdkInfoChanged);
        PythonPluginsEngine.Updater.INSTANCE.setNotifyWhenChangeStatus(true);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pluginsPySdkInfoChanged);
        PythonPluginsEngine.Updater.Companion companion = PythonPluginsEngine.Updater.INSTANCE;
        companion.setNotifyWhenChangeStatus(false);
        if (companion.getStatus() == 2) {
            companion.setStatus(0);
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int id, int account, Object... args) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-137797452449327L);
        if (id == NotificationCenter.pluginsPySdkInfoChanged) {
            this.listView.adapter.update(true);
        }
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        CharSequence string;
        items.add(UItem.asHeader(LocaleController.getString(R.string.Settings)));
        UItem devItem = UItem.asCheck(PreferenceItem.DEVELOPER_MODE.getId(), "Developer Mode").setChecked(ExteraConfig.getPluginsDevMode()).setEnabled(ExteraConfig.getPluginsEngine() && !ExteraConfig.getPluginsSafeMode()); devItem.iconResId = R.drawable.msg_settings; items.add(devItem);
        UItem compactItem = UItem.asCheck(PreferenceItem.COMPACT_VIEW.getId(), "Compact View").setChecked(ExteraConfig.getPluginsCompactView()).setEnabled(ExteraConfig.getPluginsEngine()); compactItem.iconResId = R.drawable.msg_topics; items.add(compactItem);
        UItem artItem = UItem.asCheck(PreferenceItem.PLUGINS_DISABLE_ART_OPTS.getId(), "Disable ART Optimizations").setChecked(ExteraConfig.getPluginsDisableArtOpts()).setEnabled(ExteraConfig.getPluginsEngine()); artItem.iconResId = R.drawable.msg_link2; items.add(artItem);
        UItem safeItem = UItem.asCheck(PreferenceItem.SAFE_MODE.getId(), "Safe Mode").setChecked(ExteraConfig.getPluginsSafeMode()); safeItem.iconResId = R.drawable.msg_secret; items.add(safeItem);
        items.add(UItem.asShadow("Plugins safe mode info"));
        int id = PreferenceItem.SDK_HEADER.getId();
        items.add(UItem.asAnimatedHeader(id, "Python SDK"));
        UItem searchable = UItem.asCheck(PreferenceItem.SDK_AUTO_UPDATE.getId(), "PySDK Auto Update").setChecked(ExteraConfig.getPluginsPySdkAutoUpdate());
        PythonPluginsEngine.Updater.Companion companion = PythonPluginsEngine.Updater.INSTANCE;
        items.add(searchable.setEnabled(companion.getStatus() < 3));
        items.add(UItem.asCheck(PreferenceItem.SDK_BETA_VERSIONS.getId(), "Enable PySDK Beta Versions").setChecked(ExteraConfig.getPluginsPySdkBetaVersions()).setEnabled(companion.getStatus() < 3));
        items.add(UItem.asButton(PreferenceItem.CHECK_SDK_UPDATES.getId(), "Check PySDK Updates").accent().setEnabled(companion.getStatus() < 3));
        if (ExteraConfig.getPluginsDevMode() && !ExteraConfig.getPluginsEngine() && !companion.isSdkFromApk()) {
            items.add(UItem.asButton(PreferenceItem.RESTORE_SDK_FROM_APK.getId(), "Restore PySDK from APK").red());
        }
        items.add(UItem.asShadow(null));
        items.add(UItem.asHeader("Links"));
        items.add(UItem.asButton(PreferenceItem.DOCUMENTATION.getId(), "Plugins Documentation"));
        items.add(UItem.asButton(PreferenceItem.TRUSTED_PLUGINS.getId(), "Trusted Plugins").accent());
        items.add(UItem.asShadow(LocaleUtils.formatWithHtmlURLs(new SpannableString(Html.fromHtml("Plugins powered by Chaquopy Python Engine", 0)))));
    }

    @Override // com.exteragram.messenger.preferences.BasePreferencesActivity
    public void onClick(UItem item, View view, int position, float x, float y) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-136719415658031L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-136749480429103L);
        int i = item.id;
        if (i <= 0 || i > PreferenceItem.getEntries().size()) {
            return;
        }
        PreferenceItem preferenceItem = PreferenceItem.getEntries().get(item.id - 1);
        if ((view instanceof TextCheckCell) && (ExteraConfig.getPluginsEngine() || preferenceItem == PreferenceItem.SAFE_MODE || preferenceItem == PreferenceItem.SDK_AUTO_UPDATE || preferenceItem == PreferenceItem.SDK_BETA_VERSIONS)) {
            switch (WhenMappings.$EnumSwitchMapping$0[preferenceItem.ordinal()]) {
                case 1:
                    toggleBooleanSettingAndRefresh(item, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda0
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            PluginsInfoActivity.m1349$r8$lambda$8QlKkw5kfayJfhO899himkCzI(PluginsInfoActivity.this, (Boolean) obj);
                        }
                    });
                    break;
                case 2:
                    toggleBooleanSettingAndRefresh(item, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda1
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            PluginsInfoActivity.$r8$lambda$wqIs6z_14DjJRgtbGrlNat2R__w((Boolean) obj);
                        }
                    });
                    break;
                case 3:
                    final SharedPreferences preferences = PluginsController.INSTANCE.getInstance().getPreferences();
                    toggleBooleanSettingAndRefresh(item, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda2
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            PluginsInfoActivity.$r8$lambda$uvPKm0MyDJVKV9ru_YI_2UEFTNQ(preferences, (Boolean) obj);
                        }
                    });
                    break;
                case 4:
                    toggleBooleanSettingAndRefresh(item, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda3
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            PluginsInfoActivity.m1350$r8$lambda$fA84ngNcg3E8iytEUb70vzqJw(PluginsInfoActivity.this, (Boolean) obj);
                        }
                    });
                    break;
                case 5:
                    toggleBooleanSettingAndRefresh(item, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda4
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            ExteraConfig.setPluginsPySdkAutoUpdate(((Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 6:
                    toggleBooleanSettingAndRefresh(item, new Consumer() { // from class: com.exteragram.messenger.plugins.ui.PluginsInfoActivity$$ExternalSyntheticLambda5
                        @Override // com.google.android.exoplayer2.util.Consumer
                        public final void accept(Object obj) {
                            PluginsInfoActivity.m1351$r8$lambda$lIyiDc9HTWaRKBjgUIkTIimadU((Boolean) obj);
                        }
                    });
                    break;
            }
            return;
        }
        PreferenceItem preferenceItem2 = PreferenceItem.DOCUMENTATION;
        if (preferenceItem == preferenceItem2 || preferenceItem == PreferenceItem.TRUSTED_PLUGINS) {
            Browser.openUrl(getParentActivity(), Deobfuscator$exteraGramDev$TMessagesProj.getString(preferenceItem == preferenceItem2 ? -136693645854255L : -136831084807727L));
            return;
        }
        if (preferenceItem == PreferenceItem.CHECK_SDK_UPDATES) {
            PythonPluginsEngine.Updater.INSTANCE.checkUpdates(true);
        } else if (preferenceItem == PreferenceItem.RESTORE_SDK_FROM_APK) {
            PythonPluginsEngine.Updater.INSTANCE.restoreSdkFromApk();
            BulletinFactory.of(this).createSimpleBulletin(R.raw.contact_check, "App restart required").show();
            this.listView.adapter.update(true);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$8Ql-Kkw5kfayJfhO89-9himkCzI, reason: not valid java name */
    public static void m1349$r8$lambda$8QlKkw5kfayJfhO899himkCzI(PluginsInfoActivity pluginsInfoActivity, Boolean bool) {
        ExteraConfig.setPluginsDevMode(bool.booleanValue());
        PluginsController.INSTANCE.getInstance().checkDevServers();
        BulletinFactory.of(pluginsInfoActivity).createSimpleBulletin(ExteraConfig.getPluginsDevMode() ? R.raw.contact_check : R.raw.error, ExteraConfig.getPluginsDevMode() ? "Developer server launched" : "Developer server stopped").show();
    }

    public static void $r8$lambda$wqIs6z_14DjJRgtbGrlNat2R__w(Boolean bool) {
        ExteraConfig.setPluginsCompactView(bool.booleanValue());
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.reloadInterface);
    }

    public static void $r8$lambda$uvPKm0MyDJVKV9ru_YI_2UEFTNQ(SharedPreferences sharedPreferences, Boolean bool) {
        ExteraConfig.setPluginsSafeMode(bool.booleanValue());
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        if (bool.booleanValue()) {
            editorEdit.putString(Deobfuscator$exteraGramDev$TMessagesProj.getString(-136513257227823L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-136590566639151L));
        } else {
            editorEdit.remove(Deobfuscator$exteraGramDev$TMessagesProj.getString(-136556206900783L));
        }
        editorEdit.apply();
        PluginsController.INSTANCE.getInstance().restart(bool.booleanValue());
    }

    /* JADX INFO: renamed from: $r8$lambda$fA84ngN-cg3E8iytE-Ub70vzqJw, reason: not valid java name */
    public static void m1350$r8$lambda$fA84ngNcg3E8iytEUb70vzqJw(PluginsInfoActivity pluginsInfoActivity, Boolean bool) {
        ExteraConfig.setPluginsDisableArtOpts(bool.booleanValue());
        PluginsController.INSTANCE.applyArtOpts();
        pluginsInfoActivity.showRestartBulletin();
    }

    /* JADX INFO: renamed from: $r8$lambda$lIyiDc9HTWaRKBjgUIkTIi-madU, reason: not valid java name */
    public static void m1351$r8$lambda$lIyiDc9HTWaRKBjgUIkTIimadU(Boolean bool) {
        ExteraConfig.setPluginsPySdkBetaVersions(bool.booleanValue());
        PythonPluginsEngine.Updater.INSTANCE.checkUpdates(true);
    }
}

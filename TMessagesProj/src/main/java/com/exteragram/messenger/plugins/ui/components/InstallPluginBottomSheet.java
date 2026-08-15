package com.exteragram.messenger.plugins.ui.components;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.exteragram.messenger.ExteraConfig;
import org.telegram.ui.Components.ColoredImageSpan;
import com.exteragram.messenger.plugins.Plugin;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.PythonPluginsEngine;
import com.exteragram.messenger.utils.chats.ChatUtils;
import com.exteragram.messenger.utils.text.LocaleUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.StringsKt;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.utils.ViewOutlineProviderImpl;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.EffectsTextView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ScaleStateListAnimator;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.HintView2;

/* JADX INFO: loaded from: classes.dex */
@SourceDebugExtension({"SMAP\nInstallPluginBottomSheet.kt\nKotlin\n*S Kotlin\n*F\n+ 1 InstallPluginBottomSheet.kt\ncom/exteragram/messenger/plugins/ui/components/InstallPluginBottomSheet\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,749:1\n1#2:750\n*E\n"})
public final class InstallPluginBottomSheet extends BottomSheet {
    final ButtonWithCounterView button;
    volatile boolean cancellationRequested;
    private HintView2 currentHint;
    Runnable delayedLoadingRunnable;
    private boolean enableAfterInstallation;
    private boolean installing;
    private final boolean isUpdate;
    private final PluginInstallParams params;
    private final Plugin plugin;

    @Override // org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public /* bridge */ /* synthetic */ void setLastVisible(boolean z) {
        super.setLastVisible(z);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public InstallPluginBottomSheet(final BaseFragment baseFragment, PluginsController.PluginValidationResult pluginValidationResult, PluginInstallParams pluginInstallParams) {
        super(baseFragment.getParentActivity(), false, baseFragment.getResourceProvider());
        float f;
        Plugin plugin;
        boolean z;
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-140279943546415L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-140378727794223L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-140460332172847L);
        this.params = pluginInstallParams;
        Plugin plugin2 = pluginValidationResult.getPlugin();
        if (plugin2 == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        this.plugin = plugin2;
        PluginsController.Companion companion = PluginsController.INSTANCE;
        boolean zContainsKey = companion.getInstance().getPlugins().containsKey(plugin2.getId());
        this.isUpdate = zContainsKey;
        setDelegate(new BottomSheet.BottomSheetDelegate() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet.1
            @Override // org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegate, org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegateInterface
            public boolean canDismiss() {
                return !InstallPluginBottomSheet.this.installing;
            }
        });
        Activity parentActivity = baseFragment.getParentActivity();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-138948503684655L);
        fixNavigationBar();
        FrameLayout frameLayout = new FrameLayout(parentActivity);
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setOrientation(1);
        linearLayout.setClipChildren(false);
        linearLayout.setClipToPadding(false);
        frameLayout.addView(linearLayout);
        if (plugin2.getPack() != null && plugin2.getIndex() >= 0) {
            FrameLayout frameLayout2 = new FrameLayout(parentActivity);
            frameLayout2.setClipChildren(false);
            frameLayout2.setClipToPadding(false);
            f = 12.0f;
            BackupImageView backupImageView = new BackupImageView(parentActivity);
            backupImageView.getImageReceiver().setAutoRepeat(1);
            frameLayout2.addView(backupImageView, LayoutHelper.createFrame(78, 78, 17));
            FrameLayout frameLayout3 = new FrameLayout(parentActivity);
            frameLayout3.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(28.0f), getThemedColor(Theme.key_dialogBackground)));
            FrameLayout frameLayout4 = new FrameLayout(parentActivity);
            frameLayout4.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(24.0f), getThemedColor(Theme.key_featuredStickers_addButton)));
            frameLayout3.addView(frameLayout4, LayoutHelper.createFrame(24, 24, 17));
            ImageView imageView = new ImageView(parentActivity);
            imageView.setImageResource(R.drawable.msg_customize);
            imageView.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_featuredStickers_buttonText), PorterDuff.Mode.SRC_IN));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            frameLayout3.addView(imageView, LayoutHelper.createFrame(16, 16, 17));
            frameLayout2.addView(frameLayout3, LayoutHelper.createFrame(28, 28.0f, 8388693, 0.0f, 0.0f, -4.0f, -4.0f));
            linearLayout.addView(frameLayout2, LayoutHelper.createLinear(78, 78, 1, 0.0f, 28.0f, 0.0f, 0.0f));
            backupImageView.setImageResource(R.drawable.msg_customize);
        } else {
            f = 12.0f;
            ImageView imageView2 = new ImageView(parentActivity);
            imageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView2.setImageResource(R.drawable.msg_customize);
            imageView2.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_featuredStickers_buttonText), PorterDuff.Mode.SRC_IN));
            imageView2.setBackground(Theme.createCircleDrawable(AndroidUtilities.dp(78.0f), getThemedColor(Theme.key_featuredStickers_addButton)));
            int iDp = AndroidUtilities.dp(16.0f);
            imageView2.setPadding(iDp, iDp, iDp, iDp);
            linearLayout.addView(imageView2, LayoutHelper.createLinear(78, 78, 1, 0.0f, 28.0f, 0.0f, 0.0f));
        }
        TextView textView = new TextView(parentActivity);
        textView.setGravity(1);
        int i = Theme.key_windowBackgroundWhiteBlackText;
        textView.setTextColor(getThemedColor(i));
        textView.setTextSize(1, 18.0f);
        textView.setTypeface(AndroidUtilities.bold());
        textView.setText(plugin2.getName());
        linearLayout.addView(textView, LayoutHelper.createLinear(-1, -2, 0, 40.0f, 16.0f, 40.0f, 0.0f));
        EffectsTextView effectsTextView = new EffectsTextView(parentActivity, baseFragment.getResourceProvider());
        effectsTextView.setGravity(1);
        effectsTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        int i2 = Theme.key_dialogTextLink;
        effectsTextView.setLinkTextColor(getThemedColor(i2));
        effectsTextView.setTextSize(1, 14.0f);
        int i3 = Theme.key_windowBackgroundWhiteGrayText;
        effectsTextView.setTextColor(getThemedColor(i3));
        SpannableStringBuilder spannableStringBuilderAppend = new SpannableStringBuilder("Version: ").append((CharSequence) Deobfuscator$exteraGramDev$TMessagesProj.getString(-138613496235567L));
        int length = spannableStringBuilderAppend.length();
        if (zContainsKey) {
            Plugin plugin3 = companion.getInstance().getPlugins().get(plugin2.getId());
            if (plugin3 != null) {
                String version = plugin3.getVersion();
                version = version == null ? Deobfuscator$exteraGramDev$TMessagesProj.getString(-138622086170159L) : version;
                String version2 = plugin2.getVersion();
                plugin = plugin2;
                spannableStringBuilderAppend.append((CharSequence) version).append((CharSequence) Deobfuscator$exteraGramDev$TMessagesProj.getString(-138630676104751L)).append((CharSequence) (version2 == null ? Deobfuscator$exteraGramDev$TMessagesProj.getString(-138617791202863L) : version2));
                spannableStringBuilderAppend.setSpan(new ColoredImageSpan(R.drawable.msg_mini_arrow_mediathin), 0, 1, 33);
                spannableStringBuilderAppend.setSpan(new StrikethroughSpan(), length, version.length() + length, 33);
            } else {
                plugin = plugin2;
            }
        } else {
            plugin = plugin2;
            spannableStringBuilderAppend.append((CharSequence) plugin.getVersion());
        }
        spannableStringBuilderAppend.append((CharSequence) Deobfuscator$exteraGramDev$TMessagesProj.getString(-138733755319855L)).append(LocaleUtils.formatWithUsernames(plugin.getAuthor(), baseFragment, new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                dismiss();
            }
        }));
        effectsTextView.setText(spannableStringBuilderAppend);
        linearLayout.addView(effectsTextView, LayoutHelper.createLinear(-1, -2, 0, 21.0f, 4.0f, 21.0f, 0.0f));
        int i4 = pluginInstallParams.getTrusted() ? R.drawable.msg_autodelete : R.drawable.msg_mini_customize;
        int themedColor = getThemedColor(pluginInstallParams.getTrusted() ? Theme.key_windowBackgroundWhiteGreenText : Theme.key_text_RedRegular);
        String string = pluginInstallParams.getTrusted() ? "Trusted Source" : "Unknown Source";
        final LinearLayout linearLayout2 = new LinearLayout(parentActivity);
        ScaleStateListAnimator.apply(linearLayout2, 0.05f, 1.5f);
        linearLayout2.setOrientation(0);
        linearLayout2.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(20.0f), AndroidUtilities.dp(20.0f), AndroidUtilities.multiplyAlphaComponent(themedColor, 0.1f)));
        linearLayout2.setPadding(AndroidUtilities.dp(f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(6.0f));
        linearLayout2.setGravity(17);
        ImageView imageView3 = new ImageView(parentActivity);
        imageView3.setImageResource(i4);
        imageView3.setColorFilter(new PorterDuffColorFilter(themedColor, PorterDuff.Mode.SRC_IN));
        linearLayout2.addView(imageView3, LayoutHelper.createLinear(14, 14, 16, 0.0f, 0.0f, 6.0f, 0.0f));
        TextView textView2 = new TextView(parentActivity);
        textView2.setTextColor(themedColor);
        textView2.setTextSize(1, 13.0f);
        textView2.setText(string);
        linearLayout2.addView(textView2);
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-2, -2, 17, 0.0f, 12.0f, 0.0f, 0.0f));
        linearLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                InstallPluginBottomSheet.m1352$r8$lambda$6QsFbuezsLvhAJ04uYNgmHwy8(InstallPluginBottomSheet.this, view);
            }
        });
        EffectsTextView effectsTextView2 = new EffectsTextView(parentActivity);
        effectsTextView2.setGravity(3);
        effectsTextView2.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        effectsTextView2.setLinkTextColor(getThemedColor(i2));
        effectsTextView2.setTextSize(1, 15.0f);
        effectsTextView2.setTextColor(getThemedColor(i));
        effectsTextView2.setText(LocaleUtils.fullyFormatText(plugin.getDescription(), baseFragment, new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                dismiss();
            }
        }));
        linearLayout.addView(effectsTextView2, LayoutHelper.createLinear(-1, -2, 0, 21.0f, 28.0f, 21.0f, 0.0f));
        List<String> requirements = plugin.getRequirements();
        if (requirements != null && !requirements.isEmpty()) {
            PluginRequirementsView pluginRequirementsView = new PluginRequirementsView(parentActivity, baseFragment.getResourceProvider());
            linearLayout.addView(pluginRequirementsView, LayoutHelper.createLinear(-1, -2, 0, 21.0f, 12.0f, 21.0f, 0.0f));
            pluginRequirementsView.setRequirements(plugin.getRequirements());
        }
        final ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(parentActivity, true, this.resourcesProvider);
        buttonWithCounterView.setRound();
        buttonWithCounterView.setText(zContainsKey ? "Update Plugin" : "Install Plugin", false);
        buttonWithCounterView.setSubText(null, false);
        buttonWithCounterView.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                InstallPluginBottomSheet.$r8$lambda$mhmbFSrf6ODSDx9RaNzu7UYWBis(InstallPluginBottomSheet.this, buttonWithCounterView, baseFragment, view);
            }
        });
        this.button = buttonWithCounterView;
        linearLayout.addView(buttonWithCounterView, LayoutHelper.createLinear(-1, 48, 0, 16.0f, 28.0f, 16.0f, 16.0f));
        if (!plugin.isEnabled() && !ExteraConfig.getPluginsSafeMode()) {
            final CheckBox2 checkBox2 = new CheckBox2(parentActivity, 21, this.resourcesProvider);
            checkBox2.setColor(Theme.key_radioBackgroundChecked, Theme.key_checkboxDisabled, Theme.key_checkboxCheck);
            checkBox2.setDrawUnchecked(true);
            checkBox2.setChecked(this.enableAfterInstallation, false);
            checkBox2.setDrawBackgroundAsArc(10);
            TextView textView3 = new TextView(parentActivity);
            textView3.setTextColor(getThemedColor(i));
            textView3.setTextSize(1, 14.0f);
            textView3.setText("Enable after installation");
            FrameLayout frameLayout5 = new FrameLayout(parentActivity);
            frameLayout5.addView(checkBox2, LayoutHelper.createFrame(21, 21.0f, 17, 0.0f, 0.0f, 0.0f, 0.0f));
            LinearLayout linearLayout3 = new LinearLayout(parentActivity);
            linearLayout3.setOrientation(0);
            linearLayout3.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(6.0f));
            linearLayout3.addView(frameLayout5, LayoutHelper.createLinear(24, 24, 16, 0.0f, 0.0f, 6.0f, 0.0f));
            linearLayout3.addView(textView3, LayoutHelper.createLinear(-2, -2, 16));
            linearLayout3.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    checkBox2.setChecked(!checkBox2.isChecked(), true);
                    InstallPluginBottomSheet.this.enableAfterInstallation = checkBox2.isChecked();
                }
            });
            ScaleStateListAnimator.apply(linearLayout3, 0.05f, 1.2f);
            linearLayout3.setBackground(Theme.createRadSelectorDrawable(getThemedColor(Theme.key_listSelector), 8, 8));
            linearLayout.addView(linearLayout3, LayoutHelper.createLinear(-2, -2, 1, 0.0f, 0.0f, 0.0f, 8.0f));
        }
        ImageView imageView4 = new ImageView(parentActivity);
        ScaleStateListAnimator.apply(imageView4, 0.15f, 1.5f);
        Drawable drawable = ContextCompat.getDrawable(parentActivity, R.drawable.msg_openin);
        imageView4.setImageDrawable(drawable != null ? drawable.mutate() : null);
        imageView4.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_windowBackgroundWhiteGrayIcon), PorterDuff.Mode.MULTIPLY));
        imageView4.setScaleType(ImageView.ScaleType.CENTER);
        imageView4.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                InstallPluginBottomSheet.$r8$lambda$f6lHCVPGyeXGXAmhyEm1tg18hC8(InstallPluginBottomSheet.this, baseFragment, view);
            }
        });
        imageView4.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_dialogButtonSelector), 1, AndroidUtilities.dp(20.0f)));
        frameLayout.addView(imageView4, LayoutHelper.createFrame(40, 40.0f, 53, 0.0f, 16.0f, 16.0f, 0.0f));
        ScrollView scrollView = new ScrollView(parentActivity);
        scrollView.addView(frameLayout);
        setCustomView(scrollView);
        if (pluginInstallParams.getTrusted()) {
            z = false;
            if (ExteraConfig.getPreferences().getBoolean(Deobfuscator$exteraGramDev$TMessagesProj.getString(-138682215712303L), false)) {
            }
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public final void run() {
                    linearLayout2.setVisibility(View.VISIBLE);
                }
            }, 600L);
        }
        z = false;
        if (pluginInstallParams.getTrusted() || ExteraConfig.getPreferences().getBoolean(Deobfuscator$exteraGramDev$TMessagesProj.getString(-138768115058223L), z)) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() {
            @Override
            public final void run() {
                linearLayout2.setVisibility(View.VISIBLE);
            }
        }, 600L);
    }

    /* JADX INFO: renamed from: $r8$lambda$6QsFbuez-sLv-hAJ04uYNgmHwy8, reason: not valid java name */
    public static void m1352$r8$lambda$6QsFbuezsLvhAJ04uYNgmHwy8(final InstallPluginBottomSheet installPluginBottomSheet, final View view) {
        HintView2 hintView2 = installPluginBottomSheet.currentHint;
        if (hintView2 != null) {
            hintView2.hide();
        }
        installPluginBottomSheet.currentHint = null;
        final HintView2 rounding = new HintView2(installPluginBottomSheet.getContext(), 3).setMultilineText(true).setBgColor(installPluginBottomSheet.getThemedColor(Theme.key_undo_background)).setTextColor(installPluginBottomSheet.getThemedColor(Theme.key_undo_infoColor)).setText(installPluginBottomSheet.params.getTrusted() ? "This plugin comes from a verified source." : "This plugin comes from an unknown source.").setTextAlign(Layout.Alignment.ALIGN_CENTER).allowBlur(true).setRounding(12.0f);
        rounding.setMaxWidthPx(HintView2.cutInFancyHalf(rounding.getText(), rounding.getTextPaint()));
        installPluginBottomSheet.container.addView(rounding, LayoutHelper.createFrame(-1, 100.0f, 55, 32.0f, 0.0f, 32.0f, 0.0f));
        installPluginBottomSheet.currentHint = rounding;
        installPluginBottomSheet.container.post(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                InstallPluginBottomSheet.$r8$lambda$PL7ooqHXepYuyf5ZRxpUpkoVJtQ(view, installPluginBottomSheet, rounding);
            }
        });
    }

    public static void $r8$lambda$PL7ooqHXepYuyf5ZRxpUpkoVJtQ(View view, InstallPluginBottomSheet installPluginBottomSheet, HintView2 hintView2) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        int[] iArr2 = new int[2];
        installPluginBottomSheet.container.getLocationInWindow(iArr2);
        iArr[0] = iArr[0] - iArr2[0];
        int i = iArr[1] - iArr2[1];
        iArr[1] = i;
        int iDp = (i - AndroidUtilities.dp(100.0f)) - AndroidUtilities.dp(6.0f);
        float measuredWidth = iArr[0] + (view.getMeasuredWidth() / 2.0f);
        hintView2.setTranslationY(iDp);
        hintView2.setJointPx(0.0f, (-AndroidUtilities.dp(32.0f)) + measuredWidth);
        hintView2.setDuration(5500L);
        hintView2.show();
    }

    public static void $r8$lambda$mhmbFSrf6ODSDx9RaNzu7UYWBis(final InstallPluginBottomSheet installPluginBottomSheet, ButtonWithCounterView buttonWithCounterView, final BaseFragment baseFragment, View view) {
        if (installPluginBottomSheet.installing) {
            if (installPluginBottomSheet.cancellationRequested) {
                return;
            }
            installPluginBottomSheet.cancellationRequested = true;
            buttonWithCounterView.setLoading(true);
            buttonWithCounterView.setSubText(null, true);
            return;
        }
        installPluginBottomSheet.installing = true;
        installPluginBottomSheet.cancellationRequested = false;
        installPluginBottomSheet.setCanDismissWithSwipe(false);
        installPluginBottomSheet.setCanDismissWithTouchOutside(false);
        Runnable runnable = installPluginBottomSheet.delayedLoadingRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        Runnable runnable2 = new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                if (installPluginBottomSheet.button != null) installPluginBottomSheet.button.setLoading(true);
            }
        };
        installPluginBottomSheet.delayedLoadingRunnable = runnable2;
        AndroidUtilities.runOnUIThread(runnable2, 250L);
        PluginsController.PluginsEngine pluginsEngine = PluginsController.INSTANCE.getEngines().get(Deobfuscator$exteraGramDev$TMessagesProj.getString(-139472489694767L));
        PythonPluginsEngine pythonPluginsEngine = pluginsEngine instanceof PythonPluginsEngine ? (PythonPluginsEngine) pluginsEngine : null;
        if (pythonPluginsEngine == null) {
            return;
        }
        pythonPluginsEngine.loadPluginFromFile(installPluginBottomSheet.params.getFilePath(), installPluginBottomSheet.plugin, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda11
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(final Object obj) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda17
                    @Override // java.lang.Runnable
                    public final void run() {
                        InstallPluginBottomSheet.$r8$lambda$6zxqI9k4hzUm636fgiMNfKCapIU(installPluginBottomSheet, obj != null ? obj.toString() : null, baseFragment);
                    }
                });
            }
        }, new InstallPluginBottomSheet$buttonView$1$1$4(installPluginBottomSheet));
    }

    public static void $r8$lambda$6zxqI9k4hzUm636fgiMNfKCapIU(final InstallPluginBottomSheet installPluginBottomSheet, final String str, final BaseFragment baseFragment) {
        Runnable runnable = installPluginBottomSheet.delayedLoadingRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        installPluginBottomSheet.delayedLoadingRunnable = null;
        installPluginBottomSheet.button.setLoading(false);
        installPluginBottomSheet.setCancelable(true);
        installPluginBottomSheet.setCanDismissWithSwipe(true);
        installPluginBottomSheet.setCanDismissWithTouchOutside(true);
        installPluginBottomSheet.installing = false;
        if (installPluginBottomSheet.cancellationRequested) {
            installPluginBottomSheet.cancellationRequested = false;
            installPluginBottomSheet.restoreButtonText(true);
            return;
        }
        if (str != null) {
            if (str.contains("cancel")) {
                installPluginBottomSheet.restoreButtonText(true);
                return;
            } else {
                installPluginBottomSheet.restoreButtonText(true);
                BulletinFactory.of(installPluginBottomSheet.topBulletinContainer, installPluginBottomSheet.resourcesProvider).createSimpleBulletin(R.raw.error, "Failed to install plugin " + installPluginBottomSheet.plugin.getName(), LocaleUtils.createCopySpan(baseFragment), () -> $r8$lambda$2g0LtQ8c5aj3CwgjkXzlSntggOU(str, baseFragment)).show();
                return;
            }
        }
        installPluginBottomSheet.dismiss();
        if (installPluginBottomSheet.enableAfterInstallation && !ExteraConfig.getPluginsSafeMode()) {
            PluginsController.INSTANCE.getInstance().setPluginEnabled(installPluginBottomSheet.plugin.getId(), true, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda1
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    InstallPluginBottomSheet.m1354$r8$lambda$XazxihW_JS_X65B2kIJsbNdqQg(null, baseFragment, (String) obj);
                }
            });
        } else {
            installPluginBottomSheet.showSuccessBulletin(baseFragment, installPluginBottomSheet.plugin);
        }
    }

    public static void $r8$lambda$2g0LtQ8c5aj3CwgjkXzlSntggOU(String str, BaseFragment baseFragment) {
        if (AndroidUtilities.addToClipboard(str)) {
            BulletinFactory.of(baseFragment).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$XazxihW_JS_X65B2kIJ-sbNdqQg, reason: not valid java name */
    public static void m1354$r8$lambda$XazxihW_JS_X65B2kIJsbNdqQg(InstallPluginBottomSheet installPluginBottomSheet, final BaseFragment baseFragment, final String str) {
        if (str == null) {
            installPluginBottomSheet.showSuccessBulletin(baseFragment, installPluginBottomSheet.plugin);
        } else {
            BulletinFactory.of(baseFragment).createSimpleBulletin(R.raw.error, "Plugin installed, but failed to enable " + installPluginBottomSheet.plugin.getName(), LocaleUtils.createCopySpan(baseFragment), () -> $r8$lambda$rBBotAd4KCEuAuU0dxzvUit8T6k(str, baseFragment)).show();
        }
    }

    public static void $r8$lambda$rBBotAd4KCEuAuU0dxzvUit8T6k(String str, BaseFragment baseFragment) {
        if (AndroidUtilities.addToClipboard(str)) {
            BulletinFactory.of(baseFragment).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();
        }
    }

    public static void $r8$lambda$R8l3SwnXpEYtAjSIVKWgpJ3tSSc(CheckBox2 checkBox2, InstallPluginBottomSheet installPluginBottomSheet, View view) {
        checkBox2.setChecked(!checkBox2.isChecked(), true);
        installPluginBottomSheet.enableAfterInstallation = checkBox2.isChecked();
    }

    public static void $r8$lambda$f6lHCVPGyeXGXAmhyEm1tg18hC8(InstallPluginBottomSheet installPluginBottomSheet, BaseFragment baseFragment, View view) {
        String name;
        if (installPluginBottomSheet.installing) {
            return;
        }
        File file = new File(installPluginBottomSheet.params.getFilePath());
        if (file.exists()) {
            String name2 = file.getName();
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-139532619236911L);
            if (StringsKt.endsWith(name2, Deobfuscator$exteraGramDev$TMessagesProj.getString(-139579863877167L), true)) {
                name = file.getName();
            } else {
                name = installPluginBottomSheet.plugin.getId() + Deobfuscator$exteraGramDev$TMessagesProj.getString(-139133187278383L);
            }
            if (PluginFileViewer.INSTANCE.open(baseFragment, file, name)) {
                installPluginBottomSheet.dismiss();
            }
        }
    }

    public static void $r8$lambda$0dAXGrdKgOqN6gkG9N_fcAhckns(LinearLayout linearLayout, InstallPluginBottomSheet installPluginBottomSheet) {
        if (linearLayout != null) linearLayout.performClick();
    }

    private final void showSuccessBulletin(BaseFragment fragment, final Plugin plugin) {
        final BulletinFactory bulletinFactoryOf = BulletinFactory.of(fragment);
        showSimpleSuccessBulletin(plugin, bulletinFactoryOf);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void showSuccessBulletin$lambda$2$0(AtomicBoolean atomicBoolean, TLRPC.TL_messages_stickerSet tL_messages_stickerSet, final Plugin plugin, Runnable runnable, InstallPluginBottomSheet installPluginBottomSheet, String str, BulletinFactory bulletinFactory) {
        Bulletin bulletinCreateSimpleBulletin;
        int index;
        if (atomicBoolean.get()) {
            return;
        }
        TLRPC.Document document = null;
        if ((tL_messages_stickerSet != null ? tL_messages_stickerSet.documents : null) != null) {
            ArrayList<TLRPC.Document> arrayList = tL_messages_stickerSet.documents;
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-139339345708591L);
            if (!arrayList.isEmpty() && (index = plugin.getIndex()) >= 0 && index < tL_messages_stickerSet.documents.size()) {
                document = tL_messages_stickerSet.documents.get(index);
            }
        }
        if (document == null || atomicBoolean.getAndSet(true)) {
            return;
        }
        AndroidUtilities.cancelRunOnUIThread(runnable);
        SpannableStringBuilder spannableStringBuilderReplaceTags = AndroidUtilities.replaceTags((installPluginBottomSheet.isUpdate ? "Updated " : "Installed ") + str);
        PluginsController.Companion companion = PluginsController.INSTANCE;
        Plugin plugin2 = companion.getInstance().getPlugins().get(plugin.getId());
        if (plugin2 != null && companion.getInstance().hasPluginSettings(plugin.getId()) && plugin2.isEnabled()) {
            bulletinCreateSimpleBulletin = bulletinFactory.createEmojiBulletin(document, spannableStringBuilderReplaceTags, LocaleController.getString(R.string.Settings), new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    InstallPluginBottomSheet.showSuccessBulletin$lambda$2$0$0(plugin);
                }
            });
        } else {
            bulletinCreateSimpleBulletin = bulletinFactory.createSimpleBulletin(document, spannableStringBuilderReplaceTags);
        }
        bulletinCreateSimpleBulletin.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void showSuccessBulletin$lambda$2$0$0(Plugin plugin) {
        PluginsController.INSTANCE.openPluginSettings(plugin.getId());
    }

    private final void showSimpleSuccessBulletin(final Plugin plugin, BulletinFactory bf) {
        String string = (this.isUpdate ? "Updated " : "Installed ") + plugin.getName();
        PluginsController.Companion companion = PluginsController.INSTANCE;
        Plugin plugin2 = companion.getInstance().getPlugins().get(plugin.getId());
        if (plugin2 != null && companion.getInstance().hasPluginSettings(plugin.getId()) && plugin2.isEnabled()) {
            bf.createSimpleBulletin(R.raw.contact_check, string, LocaleController.getString(R.string.Settings), new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.InstallPluginBottomSheet$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    PluginsController.INSTANCE.openPluginSettings(plugin.getId());
                }
            }).show();
        } else {
            bf.createSimpleBulletin(R.raw.contact_check, string).show();
        }
    }

    private final void restoreButtonText(boolean animated) {
        if (this.button != null) this.button.setText(this.isUpdate ? "Update Plugin" : "Install Plugin", animated);
        this.button.setSubText(null, animated);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss() {
        HintView2 hintView2 = this.currentHint;
        if (hintView2 != null) {
            hintView2.hide();
        }
        this.currentHint = null;
        super.dismiss();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void onSwipeStarts() {
        HintView2 hintView2 = this.currentHint;
        if (hintView2 != null) {
            hintView2.hide();
        }
        this.currentHint = null;
    }

    public static final class PluginInstallParams {

        /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);
        private String filePath;
        private boolean trusted;

        @JvmStatic
        public static final PluginInstallParams of(MessageObject messageObject) {
            return INSTANCE.of(messageObject);
        }

        public PluginInstallParams(String str, boolean z) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123301937825327L);
            this.filePath = str;
            this.trusted = z;
        }

        public final String getFilePath() {
            return this.filePath;
        }

        public final void setFilePath(String str) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123417901942319L);
            this.filePath = str;
        }

        public final boolean getTrusted() {
            return this.trusted;
        }

        public final void setTrusted(boolean z) {
            this.trusted = z;
        }

        /* JADX INFO: loaded from: classes4.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            /* JADX WARN: Code duplicated, block: B:11:0x0037  */
            @JvmStatic
            public final PluginInstallParams of(MessageObject messageObject) {
                String pathToMessage = ChatUtils.getInstance().getPathToMessage(messageObject);
                return new PluginInstallParams(pathToMessage, false);
            }
        }
    }
}

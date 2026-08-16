package com.exteragram.messenger.plugins.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.exteragram.messenger.ExteraConfig;
import com.exteragram.messenger.plugins.Plugin;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.utils.AppUtils;
import com.exteragram.messenger.utils.text.LocaleUtils;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.utils.ViewOutlineProviderImpl;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.EffectsTextView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.Switch;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalRecyclerView;
import org.telegram.ui.LaunchActivity;

/* JADX INFO: loaded from: classes4.dex */
@SuppressLint({"ViewConstructor"})
public final class PluginCell extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private final Switch checkBox;
    private boolean compactMode;
    private final ImageView deleteButton;
    private final EffectsTextView descriptionView;
    private final LinearLayout headerLayout;
    private final BackupImageView imageView;
    private final ImageView openInButton;
    private final ImageView pinButton;
    private Plugin plugin;
    private PluginCellDelegate pluginCellDelegate;
    private final TextView pluginNameView;
    private final PluginRequirementsView requirementsLayout;
    private final ImageView settingsButton;
    private final ImageView shareButton;
    private final EffectsTextView subtitleView;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PluginCell(Context context) {
        super(context);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-123752909391407L);
        setClickable(false);
        setClipChildren(false);
        setClipToPadding(false);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        addView(linearLayout, LayoutHelper.createFrame(-1, -2.0f, 119, 16.0f, 16.0f, 16.0f, 8.0f));
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        this.headerLayout = linearLayout2;
        linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, -2));
        BackupImageView backupImageView = new BackupImageView(context);
        backupImageView.setVisibility(8);
        backupImageView.setOutlineProvider(createPluginIconOutlineProvider());
        backupImageView.setClipToOutline(true);
        backupImageView.getImageReceiver().setAutoRepeat(1);
        this.imageView = backupImageView;
        linearLayout2.addView(backupImageView, LayoutHelper.createLinear(56, 56, 51, 0.0f, 0.0f, 0.0f, 12.0f));
        LinearLayout linearLayout3 = new LinearLayout(context);
        linearLayout3.setOrientation(1);
        linearLayout2.addView(linearLayout3, LayoutHelper.createLinear(-1, -2));
        TextView textView = new TextView(context);
        textView.setGravity(3);
        textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        textView.setTextSize(1, 18.0f);
        TextUtils.TruncateAt truncateAt = TextUtils.TruncateAt.END;
        textView.setEllipsize(truncateAt);
        textView.setTypeface(AndroidUtilities.bold());
        this.pluginNameView = textView;
        linearLayout3.addView(textView, LayoutHelper.createLinear(-1, -2));
        EffectsTextView effectsTextView = new EffectsTextView(context);
        effectsTextView.setGravity(3);
        effectsTextView.setTypeface(AndroidUtilities.bold());
        effectsTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        int i = Theme.key_windowBackgroundWhiteLinkText;
        effectsTextView.setLinkTextColor(Theme.getColor(i));
        effectsTextView.setTextSize(1, 14.0f);
        effectsTextView.setEllipsize(truncateAt);
        effectsTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText));
        this.subtitleView = effectsTextView;
        linearLayout3.addView(effectsTextView, LayoutHelper.createLinear(-1, -2, 0.0f, 2.0f, 0.0f, 0.0f));
        EffectsTextView effectsTextView2 = new EffectsTextView(context);
        effectsTextView2.setGravity(3);
        effectsTextView2.setTypeface(AndroidUtilities.bold());
        effectsTextView2.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        effectsTextView2.setLinkTextColor(Theme.getColor(i));
        this.descriptionView = effectsTextView2;
        linearLayout.addView(effectsTextView2, LayoutHelper.createLinear(-1, -2, 0, 0, 12, 0, 0));
        PluginRequirementsView pluginRequirementsView = new PluginRequirementsView(context, null, 2, null);
        pluginRequirementsView.setVisibility(8);
        this.requirementsLayout = pluginRequirementsView;
        linearLayout.addView(pluginRequirementsView, LayoutHelper.createLinear(-1, -2, 0, 0, 12, 0, 0));
        View view = new View(context);
        view.setBackgroundColor(Theme.getColor(Theme.key_divider));
        linearLayout.addView(view, LayoutHelper.createLinear(-1, 1.0f / AndroidUtilities.density, 0, 0, 12, 0, 8));
        LinearLayout linearLayout4 = new LinearLayout(context);
        linearLayout4.setOrientation(0);
        ImageView imageViewCreateButton = createButton(context, R.drawable.msg_share, false, new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginCell$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PluginCell.$r8$lambda$gPhyRoTYC0gInhhjd5k0po863gM(PluginCell.this, view2);
            }
        });
        this.shareButton = imageViewCreateButton;
        linearLayout4.addView(imageViewCreateButton, LayoutHelper.createFrame(40, 40.0f, 51, 0.0f, 0.0f, 8.0f, 0.0f));
        ImageView imageViewCreateButton2 = createButton(context, R.drawable.msg_openin, false, new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginCell$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PluginCell.$r8$lambda$TkTsTkgHS2XoNteC2HxEG9EpXDk(PluginCell.this, view2);
            }
        });
        this.openInButton = imageViewCreateButton2;
        linearLayout4.addView(imageViewCreateButton2, LayoutHelper.createFrame(40, 40.0f, 51, 0.0f, 0.0f, 8.0f, 0.0f));
        ImageView imageViewCreateButton3 = createButton(context, R.drawable.msg_pin, false, new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginCell$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PluginCell.m1358$r8$lambda$miXUx0RJdhvQyk3TfMBgG7YnBI(PluginCell.this, view2);
            }
        });
        this.pinButton = imageViewCreateButton3;
        linearLayout4.addView(imageViewCreateButton3, LayoutHelper.createFrame(40, 40.0f, 51, 0.0f, 0.0f, 8.0f, 0.0f));
        ImageView imageViewCreateButton4 = createButton(context, R.drawable.msg_settings, false, new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginCell$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PluginCell.$r8$lambda$vFaf3kpNKKpdG19431rjWCjd68Y(PluginCell.this, view2);
            }
        });
        imageViewCreateButton4.setVisibility(8);
        this.settingsButton = imageViewCreateButton4;
        linearLayout4.addView(imageViewCreateButton4, LayoutHelper.createFrame(40, 40.0f, 51, 0.0f, 0.0f, 8.0f, 0.0f));
        ImageView imageViewCreateButton5 = createButton(context, R.drawable.msg_delete, true, new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginCell$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                PluginCell.m1359$r8$lambda$txZU19WGsUHbCEQUldGESo9Wwc(PluginCell.this, view2);
            }
        });
        this.deleteButton = imageViewCreateButton5;
        addView(imageViewCreateButton5, LayoutHelper.createFrame(40, 40.0f, 85, 0.0f, 0.0f, 16.0f, 8.0f));
        linearLayout.addView(linearLayout4, LayoutHelper.createFrame(-1, 40, 83));
        Switch r3 = new Switch(context);
        int i2 = Theme.key_switchTrack;
        int i3 = Theme.key_switchTrackChecked;
        int i4 = Theme.key_windowBackgroundWhite;
        r3.setColors(i2, i3, i4, i4);
        r3.setFocusable(false);
        r3.setClickable(true);
        this.checkBox = r3;
        addView(r3, LayoutHelper.createFrame(37, 40.0f, 53, 0.0f, 16.0f, 24.0f, 0.0f));
        this.headerLayout.setOnClickListener(view2 -> {
            if (PluginCell.this.pluginCellDelegate != null) {
                PluginCell.this.pluginCellDelegate.togglePlugin(PluginCell.this);
            }
        });
        setCompact(ExteraConfig.getPluginsCompactView());
    }

    public static void $r8$lambda$gPhyRoTYC0gInhhjd5k0po863gM(PluginCell pluginCell, View view) {
        PluginCellDelegate pluginCellDelegate = pluginCell.pluginCellDelegate;
        if (pluginCellDelegate != null) {
            pluginCellDelegate.sharePlugin();
        }
    }

    public static void $r8$lambda$TkTsTkgHS2XoNteC2HxEG9EpXDk(PluginCell pluginCell, View view) {
        PluginCellDelegate pluginCellDelegate = pluginCell.pluginCellDelegate;
        if (pluginCellDelegate != null) {
            pluginCellDelegate.openInExternalApp();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$miXUx0RJdhvQyk3T-fMBgG7YnBI, reason: not valid java name */
    public static void m1358$r8$lambda$miXUx0RJdhvQyk3TfMBgG7YnBI(PluginCell pluginCell, View view) {
        PluginCellDelegate pluginCellDelegate = pluginCell.pluginCellDelegate;
        if (pluginCellDelegate != null) {
            pluginCellDelegate.pinPlugin(pluginCell);
        }
    }

    public static void $r8$lambda$vFaf3kpNKKpdG19431rjWCjd68Y(PluginCell pluginCell, View view) {
        PluginCellDelegate pluginCellDelegate = pluginCell.pluginCellDelegate;
        if (pluginCellDelegate != null) {
            pluginCellDelegate.openPluginSettings();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$txZU1-9WGsUHbCEQUldGESo9Wwc, reason: not valid java name */
    public static void m1359$r8$lambda$txZU19WGsUHbCEQUldGESo9Wwc(PluginCell pluginCell, View view) {
        PluginCellDelegate pluginCellDelegate = pluginCell.pluginCellDelegate;
        if (pluginCellDelegate != null) {
            pluginCellDelegate.deletePlugin();
        }
    }

    public final void setCompact(boolean compact) {
        if (this.compactMode == compact) {
            return;
        }
        this.compactMode = compact;
        updateLayout();
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec), TLObject.FLAG_30), heightMeasureSpec);
    }

    private final void updateLayout() {
        Plugin plugin = this.plugin;
        if (plugin == null) {
            return;
        }
        View childAt = this.headerLayout.getChildAt(1);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-123718549653039L);
        LinearLayout linearLayout = (LinearLayout) childAt;
        this.headerLayout.setOrientation(!this.compactMode ? 1 : 0);
        ViewGroup.LayoutParams layoutParams = this.imageView.getLayoutParams();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-139932051195439L);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
        layoutParams2.rightMargin = this.compactMode ? AndroidUtilities.dp(16.0f) : 0;
        layoutParams2.bottomMargin = this.compactMode ? 0 : AndroidUtilities.dp(12.0f);
        layoutParams2.width = this.compactMode ? AndroidUtilities.dp(49.0f) : AndroidUtilities.dp(56.0f);
        layoutParams2.height = this.compactMode ? AndroidUtilities.dp(49.0f) : AndroidUtilities.dp(56.0f);
        ViewGroup.LayoutParams layoutParams3 = linearLayout.getLayoutParams();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-139717302830639L);
        LinearLayout.LayoutParams layoutParams4 = (LinearLayout.LayoutParams) layoutParams3;
        boolean z = this.compactMode;
        layoutParams4.gravity = z ? 16 : 3;
        this.pluginNameView.setSingleLine(z);
        this.subtitleView.setSingleLine(this.compactMode);
        final int iDp = ((!this.compactMode || plugin.hasError()) && (this.compactMode || (plugin.getPack() != null && plugin.getIndex() >= 0) || plugin.hasError())) ? 0 : AndroidUtilities.dp(61.0f);
        this.pluginNameView.setPadding(0, 0, iDp, 0);
        this.pluginNameView.post(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.PluginCell$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                PluginCell.$r8$lambda$DDJoJMGrt7paL1pOJFoOkItIZSA(PluginCell.this, iDp);
            }
        });
        requestLayout();
    }

    public static void $r8$lambda$DDJoJMGrt7paL1pOJFoOkItIZSA(PluginCell pluginCell, int i) {
        EffectsTextView effectsTextView = pluginCell.subtitleView;
        if (pluginCell.pluginNameView.getLineCount() > 1) {
            i = 0;
        }
        effectsTextView.setPadding(0, 0, i, 0);
    }

    public final void set(Plugin plugin, final PluginCellDelegate pluginCellDelegate) {
        String string;
        if (plugin == null || pluginCellDelegate == null) {
            return;
        }
        this.pluginCellDelegate = pluginCellDelegate;
        this.plugin = plugin;
        updatePluginIconOutlineProvider();
        PluginsController.Companion companion = PluginsController.INSTANCE;
        setPinned(companion.isPluginPinned(plugin.getId()));
        this.openInButton.setVisibility(pluginCellDelegate.canOpenInExternalApp() ? 0 : 8);
        boolean z = plugin.getPack() != null && plugin.getIndex() >= 0;
        this.imageView.setVisibility(z ? 0 : 8);
        if (z) {
            MediaDataController.getInstance(UserConfig.selectedAccount).setPlaceholderImage(this.imageView, plugin.getPack(), String.valueOf(plugin.getIndex()), "100_100");
        } else {
            this.imageView.setImageDrawable(null);
        }
        this.pluginNameView.setText(plugin.getName());
        EffectsTextView effectsTextView = this.subtitleView;
        if (this.compactMode) {
            string = "";
        } else {
            string = "v";
        }
        String author = plugin.getAuthor();
        if (TextUtils.isEmpty(author) || "plugin.getAuthor() or \"Unknown\"".equals(author)) {
            author = "Unknown";
        }
        effectsTextView.setText(new SpannableStringBuilder(string).append((CharSequence) plugin.getVersion()).append((CharSequence) " ").append(LocaleUtils.formatWithUsernames(author)));
        if (plugin.getIsNotResponding()) {
            bindNotRespondingState();
        } else if (plugin.hasError()) {
            bindErrorState();
        } else {
            bindNormalState();
        }
        this.requirementsLayout.setRequirements(plugin.getRequirements());
        updateLayout();
        this.checkBox.setChecked(plugin.isEnabled(), false);
        this.checkBox.setClickable(true);
        this.checkBox.setOnClickListener(view -> {
            if (pluginCellDelegate != null) {
                pluginCellDelegate.togglePlugin(PluginCell.this);
            }
        });
        AndroidUtilities.updateViewVisibilityAnimated(this.settingsButton, plugin.isEnabled() && companion.getInstance().hasPluginSettings(plugin.getId()), 0.5f, true, false);
    }

    private final void bindNotRespondingState() {
        this.descriptionView.setText("Plugin is not responding");
        this.descriptionView.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
        this.descriptionView.setTypeface(AndroidUtilities.bold());
        this.descriptionView.setTextSize(1, 12.0f);
        this.descriptionView.setOnClickListener(null);
        this.checkBox.setVisibility(8);
        updateDeleteButton();
    }

    private final void updateDeleteButton() {
        int color;
        Plugin plugin = this.plugin;
        if (plugin == null) {
            return;
        }
        boolean isNotResponding = plugin.getIsNotResponding();
        this.deleteButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(!isNotResponding ? Theme.key_text_RedRegular : Theme.key_windowBackgroundWhiteGrayIcon), PorterDuff.Mode.MULTIPLY));
        ImageView imageView = this.deleteButton;
        if (!isNotResponding) {
            color = Theme.multAlpha(Theme.getColor(Theme.key_text_RedRegular), 0.12f);
        } else {
            color = Theme.getColor(Theme.key_dialogButtonSelector);
        }
        imageView.setBackground(Theme.createSelectorDrawable(color, 1, AndroidUtilities.dp(20.0f)));
        this.deleteButton.setImageResource(!isNotResponding ? R.drawable.msg_delete : R.drawable.ic_ab_other);
    }

    private final void bindErrorState() {
        final Plugin plugin = this.plugin;
        if (plugin == null) {
            return;
        }
        EffectsTextView effectsTextView = this.descriptionView;
        Throwable error = plugin.getError();
        String errorMsg = "Unknown error";
        if (error != null) {
            if (error.getCause() != null) {
                errorMsg = error.getClass().getSimpleName() + ": " + error.getMessage() + "\nCause: " + error.getCause().toString();
            } else {
                errorMsg = error.getClass().getSimpleName() + ": " + error.getMessage();
            }
        }
        effectsTextView.setText(errorMsg);
        this.descriptionView.setTextColor(Theme.getColor(Theme.key_text_RedRegular));
        this.descriptionView.setTypeface(AndroidUtilities.bold());
        this.descriptionView.setTextSize(1, 12.0f);
        this.descriptionView.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginCell$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PluginCell.$r8$lambda$6rpDPmjlQ6jnFtZWLEsa5Bv1A4Q(plugin, view);
            }
        });
        this.checkBox.setVisibility(8);
        updateDeleteButton();
    }

    public static void $r8$lambda$6rpDPmjlQ6jnFtZWLEsa5Bv1A4Q(Plugin plugin, View view) {
        if (AndroidUtilities.addToClipboard(AppUtils.stackTraceToString(plugin.getError()))) {
            BulletinFactory.of(LaunchActivity.getSafeLastFragment()).createCopyBulletin(LocaleController.getString(R.string.TextCopied)).show();
        }
    }

    private final void bindNormalState() {
        Plugin plugin = this.plugin;
        if (plugin == null) {
            return;
        }
        this.descriptionView.setText(LocaleUtils.fullyFormatText(plugin.getDescription()));
        this.descriptionView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.descriptionView.setTypeface(AndroidUtilities.bold());
        this.descriptionView.setTextSize(1, 15.0f);
        this.descriptionView.setOnClickListener(null);
        this.checkBox.setVisibility(0);
        updateDeleteButton();
    }

    public final void setChecked(boolean checked, boolean animated) {
        this.checkBox.setChecked(checked, animated);
    }

    public final void setPinned(boolean pinned) {
        this.pinButton.setImageResource(pinned ? R.drawable.msg_unpin : R.drawable.msg_pin);
    }

    private final ImageView createButton(Context context, int iconResId, boolean isRed, View.OnClickListener onClickListener) {
        int color;
        ImageView imageView = new ImageView(context);
        applyClickAnimation(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(iconResId);
        imageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(isRed ? Theme.key_text_RedRegular : Theme.key_windowBackgroundWhiteGrayIcon), PorterDuff.Mode.MULTIPLY));
        if (isRed) {
            color = Theme.multAlpha(Theme.getColor(Theme.key_text_RedRegular), 0.12f);
        } else {
            color = Theme.getColor(Theme.key_dialogButtonSelector);
        }
        imageView.setBackground(Theme.createSelectorDrawable(color, 1, AndroidUtilities.dp(20.0f)));
        imageView.setOnClickListener(onClickListener);
        return imageView;
    }

    private final void applyClickAnimation(View view) {
        view.setOnTouchListener(new View.OnTouchListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginCell$$ExternalSyntheticLambda5
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                return PluginCell.$r8$lambda$GNS5TyJK00U1k_uT5XN4XND2ouo(view2, motionEvent);
            }
        });
    }

    public static boolean $r8$lambda$GNS5TyJK00U1k_uT5XN4XND2ouo(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            view.setPressed(true);
            view.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80L).start();
            return true;
        }
        if (action != 1) {
            if (action != 3) {
                return false;
            }
            view.setPressed(false);
            view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(350L).setInterpolator(new OvershootInterpolator(1.5f)).start();
            return true;
        }
        view.setPressed(false);
        if (motionEvent.getX() >= 0.0f && motionEvent.getX() <= view.getWidth() && motionEvent.getY() >= 0.0f && motionEvent.getY() <= view.getHeight()) {
            view.performClick();
        }
        view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(350L).setInterpolator(new OvershootInterpolator(1.5f)).start();
        return true;
    }

    private final int getPluginIconRadiusDp() {
        return Math.max(ExteraConfig.getSectionRadiusDp() - 16, 8);
    }

    private final ViewOutlineProvider createPluginIconOutlineProvider() {
        return new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), AndroidUtilities.dp(getPluginIconRadiusDp()));
            }
        };
    }

    private final void updatePluginIconOutlineProvider() {
        this.imageView.setOutlineProvider(createPluginIconOutlineProvider());
        this.imageView.invalidateOutline();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pluginSettingsRegistered);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.pluginSettingsUnregistered);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pluginSettingsRegistered);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pluginSettingsUnregistered);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int id, int account, Object... args) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-140318598252079L);
        Plugin plugin = this.plugin;
        if (plugin == null) {
            return;
        }
        if (id == NotificationCenter.pluginSettingsRegistered || id == NotificationCenter.pluginSettingsUnregistered) {
            Object objFirstOrNull = ArraysKt.firstOrNull(args);
            String str = objFirstOrNull instanceof String ? (String) objFirstOrNull : null;
            if (str != null && Intrinsics.areEqual(str, plugin.getId())) {
                AndroidUtilities.updateViewVisibilityAnimated(this.settingsButton, id == NotificationCenter.pluginSettingsRegistered && plugin.isEnabled(), 0.5f, true, true);
            }
        }
    }

    public static final class Factory extends UItem.UItemFactory<PluginCell> {

        /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
        public static final Companion INSTANCE = new Companion(null);

        @JvmStatic
        public static final UItem asPlugin(Plugin plugin, PluginCellDelegate pluginCellDelegate) {
            return INSTANCE.asPlugin(plugin, pluginCellDelegate);
        }

        public boolean getIsClickableValue() {
            return false;
        }

        @Override // org.telegram.ui.Components.UItem.UItemFactory
        public PluginCell createView(Context context, RecyclerListView listView, int currentAccount, int classGuid, Theme.ResourcesProvider resourcesProvider) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123456556647983L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123422196909615L);
            return new PluginCell(context);
        }

        @Override // org.telegram.ui.Components.UItem.UItemFactory
        public void bindView(View view, UItem item, boolean divider, UniversalAdapter adapter, UniversalRecyclerView listView) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123538161026607L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123551045928495L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123512391222831L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123615470437935L);
            if (view instanceof PluginCell) {
                PluginCell pluginCell = (PluginCell) view;
                Object obj = item.object;
                Plugin plugin = obj instanceof Plugin ? (Plugin) obj : null;
                Object obj2 = item.object2;
                pluginCell.set(plugin, obj2 instanceof PluginCellDelegate ? (PluginCellDelegate) obj2 : null);
            }
        }

        @Override // org.telegram.ui.Components.UItem.UItemFactory
        public boolean equals(UItem a2, UItem b2) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123576815732271L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123585405666863L);
            return a2.id == b2.id;
        }

        @Override // org.telegram.ui.Components.UItem.UItemFactory
        public boolean contentsEquals(UItem a2, UItem b2) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123662715078191L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-123671305012783L);
            if (a2.id != b2.id || a2.intValue != b2.intValue) {
                return false;
            }
            Object obj = a2.object;
            Plugin plugin = obj instanceof Plugin ? (Plugin) obj : null;
            if (plugin == null) {
                return false;
            }
            Object obj2 = b2.object;
            Plugin plugin2 = obj2 instanceof Plugin ? (Plugin) obj2 : null;
            if (plugin2 == null) {
                return false;
            }
            return Intrinsics.areEqual(plugin, plugin2);
        }

        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            @JvmStatic
            public final UItem asPlugin(Plugin plugin, PluginCellDelegate pluginCellDelegate) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-123641240241711L);
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-123181678741039L);
                UItem uItemOfFactory = UItem.ofFactory(Factory.class);
                uItemOfFactory.id = plugin.getId().hashCode();
                uItemOfFactory.object = plugin;
                uItemOfFactory.object2 = pluginCellDelegate;
                uItemOfFactory.intValue = (plugin.getIsNotResponding() ? 1 : 0) | (plugin.isEnabled() ? 2 : 0) | (plugin.hasError() ? 4 : 0) | (PluginsController.INSTANCE.getInstance().getPluginPath(plugin.getId()) != null ? 8 : 0);
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-123271873054255L);
                return uItemOfFactory;
            }
        }

        static {
            UItem.UItemFactory.setup(new Factory());
        }
    }
}

package com.exteragram.messenger.plugins.ui.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.EffectsTextView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ViewHelper;

/* JADX INFO: loaded from: classes4.dex */
@SuppressLint({"ClickableViewAccessibility"})
public final class EmptyPluginsView extends FrameLayout {
    private final BackupImageView backupImageView;
    private final EffectsTextView textView;

    @JvmOverloads
    public EmptyPluginsView(Context context) {
        this(context, null);
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @JvmOverloads
    public EmptyPluginsView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        LinearLayout linearLayout = new LinearLayout(context);
        ViewHelper.setPadding(linearLayout, 20.0f, 0.0f, 20.0f, 0.0f);
        linearLayout.setGravity(1);
        linearLayout.setClipChildren(false);
        linearLayout.setClipToPadding(false);
        linearLayout.setOrientation(1);
        BackupImageView backupImageView = new BackupImageView(context);
        this.backupImageView = backupImageView;
        linearLayout.addView(backupImageView, LayoutHelper.createLinear(100, 100, 17, 0.0f, 0.0f, 0.0f, 20.0f));
        EffectsTextView effectsTextView = new EffectsTextView(context);
        effectsTextView.setTextSize(1, 14.0f);
        effectsTextView.setTextColor(Theme.getColor(Theme.key_emptyListPlaceholder, resourcesProvider));
        effectsTextView.setGravity(1);
        effectsTextView.setText(LocaleController.getString(R.string.NoResult));
        effectsTextView.setTypeface(AndroidUtilities.bold());
        effectsTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
        effectsTextView.setLinkTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText));
        this.textView = effectsTextView;
        linearLayout.addView(effectsTextView, LayoutHelper.createLinear(-2, -2, 17));
        addView(linearLayout, LayoutHelper.createFrame(-2, -2, 17));
        setOnTouchListener(new View.OnTouchListener() { // from class: com.exteragram.messenger.plugins.ui.components.EmptyPluginsView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return EmptyPluginsView.$r8$lambda$Vd6WFN2r51k6Ofv4Z8vIUXzLhEQ(view, motionEvent);
            }
        });
    }

    public /* synthetic */ EmptyPluginsView(Context context, Theme.ResourcesProvider resourcesProvider, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : resourcesProvider);
    }

    public final BackupImageView getBackupImageView() {
        return this.backupImageView;
    }

    public static boolean $r8$lambda$Vd6WFN2r51k6Ofv4Z8vIUXzLhEQ(View view, MotionEvent motionEvent) {
        return true;
    }

    public final void setText(CharSequence text) {
        this.textView.setText(text);
    }
}

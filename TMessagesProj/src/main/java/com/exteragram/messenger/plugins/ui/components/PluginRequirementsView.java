package com.exteragram.messenger.plugins.ui.components;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import com.chaquo.python.internal.Common;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Regex;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ScaleStateListAnimator;

/* JADX INFO: loaded from: classes.dex */
@SourceDebugExtension({"SMAP\nPluginRequirementsView.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PluginRequirementsView.kt\ncom/exteragram/messenger/plugins/ui/components/PluginRequirementsView\n+ 2 View.kt\nandroidx/core/view/ViewKt\n*L\n1#1,141:1\n297#2:142\n297#2:143\n*S KotlinDebug\n*F\n+ 1 PluginRequirementsView.kt\ncom/exteragram/messenger/plugins/ui/components/PluginRequirementsView\n*L\n86#1:142\n119#1:143\n*E\n"})
public final class PluginRequirementsView extends ViewGroup {
    private static final Pattern REQUIREMENT_PATTERN = Pattern.compile("^([a-zA-Z0-9_\\-.]+)");
    private final int itemSpacing;
    private final int lineSpacing;
    private final Theme.ResourcesProvider resourcesProvider;

    @JvmOverloads
    public PluginRequirementsView(Context context) {
        this(context, null);
    }

    @JvmOverloads
    public PluginRequirementsView(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.resourcesProvider = resourcesProvider;
        this.itemSpacing = AndroidUtilities.dp(4.0f);
        this.lineSpacing = AndroidUtilities.dp(4.0f);
    }

    public /* synthetic */ PluginRequirementsView(Context context, Theme.ResourcesProvider resourcesProvider, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : resourcesProvider);
    }

    public final void setRequirements(List<String> requirements) {
        removeAllViews();
        List<String> list = requirements;
        if (list == null || list.isEmpty()) {
            setVisibility(8);
            return;
        }
        setVisibility(0);
        for (final String str : requirements) {
            final TextView textView = new TextView(getContext());
            textView.setText(str);
            textView.setTextSize(1, 12.0f);
            int themedColor = getThemedColor(Theme.key_featuredStickers_addButton);
            textView.setTextColor(themedColor);
            textView.setTypeface(AndroidUtilities.bold());
            textView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(6.0f), ColorUtils.setAlphaComponent(themedColor, 30)));
            textView.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(2.0f));
            ScaleStateListAnimator.apply(textView);
            textView.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginRequirementsView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PluginRequirementsView.setRequirements$lambda$0$0(str, textView, view);
                }
            });
            addView(textView);
        }
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void setRequirements$lambda$0$0(String str, TextView textView, View view) {
        Matcher matcher = REQUIREMENT_PATTERN.matcher(str);
        if (matcher.find()) {
            Browser.openUrl(textView.getContext(), "https://pypi.org/project/" + matcher.group(1) + '/');
        }
    }

    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        int mode = View.MeasureSpec.getMode(widthMeasureSpec);
        int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        int iMax = 0;
        int iMax2 = 0;
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() != 8) {
                measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (i + measuredWidth > paddingLeft) {
                    paddingTop += iMax + this.lineSpacing;
                    iMax = 0;
                    i = 0;
                }
                i += measuredWidth + this.itemSpacing;
                iMax = Math.max(iMax, measuredHeight);
                iMax2 = Math.max(iMax2, i - this.itemSpacing);
            }
        }
        int paddingBottom = paddingTop + iMax + getPaddingBottom();
        if (mode != 1073741824) {
            size = iMax2 + getPaddingRight() + getPaddingLeft();
        }
        setMeasuredDimension(size, paddingBottom);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b2) {
        int i = r - l;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        int iMax = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() != 8) {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (paddingLeft + measuredWidth > i - getPaddingRight()) {
                    paddingLeft = getPaddingLeft();
                    paddingTop += iMax + this.lineSpacing;
                    iMax = 0;
                }
                childAt.layout(paddingLeft, paddingTop, paddingLeft + measuredWidth, paddingTop + measuredHeight);
                paddingLeft += measuredWidth + this.itemSpacing;
                iMax = Math.max(iMax, measuredHeight);
            }
        }
    }

    private final int getThemedColor(int key) {
        return Theme.getColor(key, this.resourcesProvider);
    }
}

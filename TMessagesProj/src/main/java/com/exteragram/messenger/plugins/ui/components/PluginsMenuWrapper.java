package com.exteragram.messenger.plugins.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.core.content.ContextCompat;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.plugins.hooks.MenuItemRecord;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.scilab.forge.jlatexmath.TeXSymbolParser;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PopupSwipeBackLayout;

/* JADX INFO: loaded from: classes4.dex */
public class PluginsMenuWrapper {
    public static final int GAP_ITEM_HEIGHT = 8;
    public static final int ITEM_HEIGHT = 48;
    public static final int SUBTITLE_ITEM_HEIGHT = 56;
    private final Map<String, Object> contextData;
    private final LinearLayout menuItemsContainer;
    private final String menuType;
    private final Theme.ResourcesProvider resourcesProvider;
    private final LinearLayout swipeBack;

    public void closeMenu() {
    }

    public PluginsMenuWrapper(final PopupSwipeBackLayout popupSwipeBackLayout, List<MenuItemRecord> list, String str, Map<String, ? extends Object> map, Theme.ResourcesProvider resourcesProvider) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-123383542203951L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-124002017494575L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-124100801742383L);
        this.menuType = str;
        this.contextData = (Map) map;
        this.resourcesProvider = resourcesProvider;
        Context context = popupSwipeBackLayout.getContext();
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        this.swipeBack = linearLayout;
        ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(context, true, false, resourcesProvider);
        actionBarMenuSubItem.setItemHeight(44);
        actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(R.string.Back), R.drawable.msg_arrow_back);
        actionBarMenuSubItem.getTextView().setPadding(LocaleController.isRTL ? 0 : AndroidUtilities.dp(40.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(40.0f) : 0, 0);
        actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginsMenuWrapper$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                popupSwipeBackLayout.closeForeground();
            }
        });
        linearLayout.addView(actionBarMenuSubItem, LayoutHelper.createLinear(-1, -2));
        LinearLayout linearLayout2 = new LinearLayout(context);
        linearLayout2.setOrientation(1);
        this.menuItemsContainer = linearLayout2;
        ScrollView scrollViewCreateScrollView = createScrollView(context);
        scrollViewCreateScrollView.addView(linearLayout2);
        linearLayout.addView(scrollViewCreateScrollView);
        rebuildMenu(list);
    }

    public final LinearLayout getMenuItemsContainer() {
        return this.menuItemsContainer;
    }

    public final LinearLayout getSwipeBack() {
        return this.swipeBack;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public PluginsMenuWrapper(PopupSwipeBackLayout popupSwipeBackLayout, String str, Map<String, ? extends Object> map, Theme.ResourcesProvider resourcesProvider) {
        this(popupSwipeBackLayout, null, str, map, resourcesProvider);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-124152341349935L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-124221060826671L);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-124182406121007L);
    }

    public final void rebuildMenu(List<MenuItemRecord> existingItems) {
        int i;
        this.menuItemsContainer.removeAllViews();
        if (existingItems == null) {
            existingItems = PluginsController.INSTANCE.getInstance().getMenuItemsForLocation(this.menuType, this.contextData);
        }
        Context context = this.menuItemsContainer.getContext();
        this.menuItemsContainer.addView(createGap(), LayoutHelper.createLinear(-1, 8));
        int i2 = 0;
        for (final MenuItemRecord menuItemRecord : existingItems) {
            String text = menuItemRecord.getText();
            if (text != null && text.length() != 0) {
                ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(context, false, false, this.resourcesProvider);
                actionBarMenuSubItem.setTextAndIcon(menuItemRecord.getText(), menuItemRecord.getIconResId());
                actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
                actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: com.exteragram.messenger.plugins.ui.components.PluginsMenuWrapper$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PluginsMenuWrapper.rebuildMenu$lambda$0$0(PluginsMenuWrapper.this, menuItemRecord, view);
                    }
                });
                if (TextUtils.isEmpty(menuItemRecord.getSubtext())) {
                    i = 48;
                } else {
                    actionBarMenuSubItem.setSubtext(menuItemRecord.getSubtext());
                    i = 56;
                    actionBarMenuSubItem.setItemHeight(56);
                }
                this.menuItemsContainer.addView(actionBarMenuSubItem, LayoutHelper.createLinear(-1, i));
                i2 += i;
                actionBarMenuSubItem.setTag(menuItemRecord);
            }
        }
        int iDp = AndroidUtilities.dp(436.0f);
        Object parent = this.menuItemsContainer.getParent();
        View view = parent instanceof View ? (View) parent : null;
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        LinearLayout.LayoutParams layoutParamsCreateLinear = layoutParams instanceof LinearLayout.LayoutParams ? (LinearLayout.LayoutParams) layoutParams : null;
        if (layoutParamsCreateLinear == null) {
            layoutParamsCreateLinear = LayoutHelper.createLinear(-1, -2);
        }
        if (i2 <= iDp || Math.abs(i2 - iDp) <= 112) {
            iDp = -2;
        }
        layoutParamsCreateLinear.height = iDp;
        view.setLayoutParams(layoutParamsCreateLinear);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void rebuildMenu$lambda$0$0(PluginsMenuWrapper pluginsMenuWrapper, MenuItemRecord menuItemRecord, View view) {
        pluginsMenuWrapper.closeMenu();
        menuItemRecord.executeClick(pluginsMenuWrapper.contextData);
    }

    private final ScrollView createScrollView(Context context) {
        return new ScrollView(context) {
            private final AnimatedFloat alphaFloat = new AnimatedFloat(this, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
            private Drawable topShadowDrawable;
            private boolean wasCanScrollVertically;

            @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
            public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-123241808283183L);
                super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
                boolean zCanScrollVertically = canScrollVertically(-1);
                if (this.wasCanScrollVertically != zCanScrollVertically) {
                    invalidate();
                    this.wasCanScrollVertically = zCanScrollVertically;
                }
            }

            @Override // android.view.ViewGroup, android.view.View
            public void dispatchDraw(Canvas canvas) {
                Deobfuscator$exteraGramDev$TMessagesProj.getString(-123349182465583L);
                super.dispatchDraw(canvas);
                float f = this.alphaFloat.set(canScrollVertically(-1) ? 1.0f : 0.0f) * 0.5f;
                if (f <= 0.0f) {
                    return;
                }
                if (this.topShadowDrawable == null) {
                    this.topShadowDrawable = ContextCompat.getDrawable(context, R.drawable.header_shadow);
                }
                Drawable drawable = this.topShadowDrawable;
                if (drawable != null) {
                    drawable.setBounds(0, getScrollY(), getWidth(), getScrollY() + drawable.getIntrinsicHeight());
                    drawable.setAlpha((int) (255.0f * f));
                    drawable.draw(canvas);
                }
            }
        };
    }

    private final View createGap() {
        ActionBarPopupWindow.GapView gapView = new ActionBarPopupWindow.GapView(this.menuItemsContainer.getContext(), this.resourcesProvider);
        return gapView;
    }
}

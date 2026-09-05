/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package org.telegram.ui.ActionBar;

import static org.telegram.messenger.AndroidUtilities.dp;
import static org.telegram.messenger.AndroidUtilities.find;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;
import org.telegram.ui.Adapters.FiltersView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.M3ExpressiveButtonDrawable;
import org.telegram.ui.Components.RLottieDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.vkryl.android.animator.FactorAnimator;
import xyz.nextalone.nagram.ui.UIStyleEngine;

public class ActionBarMenu extends LinearLayout {

    public boolean drawBlur = true;
    protected ActionBar parentActionBar;
    protected boolean isActionMode;
    private boolean glassMode;

    private boolean isCenteredTitle = false;

    private ArrayList<Integer> ids;

    public ActionBarMenu(Context context, ActionBar layer) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        parentActionBar = layer;
    }

    public ActionBarMenu(Context context) {
        super(context);
    }

    private boolean isItemPressed;

    public void onItemPressed(ActionBarMenuItem item, boolean pressed) {
        if (pressed) {
            isItemPressed = true;
        } else {
            boolean any = false;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isPressed()) {
                    any = true;
                    break;
                }
            }
            isItemPressed = any;
        }
        if (parentActionBar != null) {
            parentActionBar.onMenuPressed(isItemPressed);
        }
    }

    public boolean isItemPressed() {
        return isItemPressed;
    }

    protected void updateItemsBackgroundColor() {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                int color = isActionMode ? parentActionBar.itemsActionModeBackgroundColor : parentActionBar.itemsBackgroundColor;
                if (xyz.nextalone.nagram.ui.UIStyleEngine.isMaterial3Expressive() && !((ActionBarMenuItem) view).isSearchField()) {
                    view.setBackgroundDrawable(new M3ExpressiveButtonDrawable(
                        0,
                        Theme.multAlpha(color, 0.40f),
                        dp(20),
                        dp(12),
                        dp(4)
                    ));
                } else {
                    view.setBackgroundDrawable(Theme.createSelectorDrawable(color));
                }
            }
        }
    }

    protected void updateItemsColor() {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem) view).setIconColor(isActionMode ? parentActionBar.itemsActionModeColor : parentActionBar.itemsColor);
            }
        }
    }

    public void setCenteredTitle(boolean centeredTitle) {
        isCenteredTitle = centeredTitle;
    }

    public boolean isCenteredTitle() {
        return isCenteredTitle;
    }

    public ActionBarMenuItem addItem(int id, Drawable drawable) {
        return addItem(id, 0, null, isActionMode ? parentActionBar.itemsActionModeBackgroundColor : parentActionBar.itemsBackgroundColor, drawable, dp(48), null);
    }

    public ActionBarMenuItem addItem(int id, int icon) {
        return addItem(id, icon, isActionMode ? parentActionBar.itemsActionModeBackgroundColor : parentActionBar.itemsBackgroundColor, null);
    }

    public ActionBarMenuItem addItem(int id, int icon, Theme.ResourcesProvider resourcesProvider) {
        return addItem(id, icon, isActionMode ? parentActionBar.itemsActionModeBackgroundColor : parentActionBar.itemsBackgroundColor, resourcesProvider);
    }

    public ActionBarMenuItem addItem(int id, CharSequence text) {
        return addItem(id, 0, text, isActionMode ? parentActionBar.itemsActionModeBackgroundColor : parentActionBar.itemsBackgroundColor, null, 0, text);
    }

    public ActionBarMenuItem addItem(int id, int icon, int backgroundColor) {
        return addItem(id, icon, backgroundColor, null);
    }

    public ActionBarMenuItem addItem(int id, int icon, int backgroundColor, Theme.ResourcesProvider resourcesProvider) {
        return addItem(id, icon, null, backgroundColor, null, dp(48), null, resourcesProvider);
    }

    public ActionBarMenuItem addItemWithWidth(int id, int icon, int width) {
        return addItem(id, icon, null, isActionMode ? parentActionBar.itemsActionModeBackgroundColor : parentActionBar.itemsBackgroundColor, null, width, null);
    }

    public ActionBarMenuItem addItemWithWidth(int id, Drawable drawable, int width, CharSequence title) {
        return addItem(id, 0, null, isActionMode ? parentActionBar.itemsActionModeBackgroundColor : parentActionBar.itemsBackgroundColor, drawable, width, title);
    }

    public ActionBarMenuItem addItemWithWidth(int id, int icon, int width, CharSequence title) {
        return addItem(id, icon, null, isActionMode ? parentActionBar.itemsActionModeBackgroundColor : parentActionBar.itemsBackgroundColor, null, width, title);
    }

    public ActionBarMenuItem addItem(int id, int icon, CharSequence text, int backgroundColor, Drawable drawable, int width, CharSequence title) {
        return addItem(id, icon, text, backgroundColor, drawable, width, title, null);
    }

    public ActionBarMenuItem addItem(int id, int icon, CharSequence text, int backgroundColor, Drawable drawable, int width, CharSequence title, Theme.ResourcesProvider resourcesProvider) {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        ids.add(id);
        return addItemAt(-1, id, icon, text, backgroundColor, drawable, width, title, resourcesProvider);
    }

    public ActionBarMenuItem addItemAt(int index, int id, int icon, CharSequence text, int backgroundColor, Drawable drawable, int width, CharSequence title, Theme.ResourcesProvider resourcesProvider) {
        ActionBarMenuItem menuItem = new ActionBarMenuItem(getContext(), this, backgroundColor, isActionMode ? parentActionBar.itemsActionModeColor : parentActionBar.itemsColor, text != null, resourcesProvider);
        menuItem.setTag(id);

        if (isCenteredTitle) {
            menuItem.setAlpha(0f);
            menuItem.setVisibility(GONE);
            menuItem.setClickable(false);
            menuItem.setEnabled(false);
        }

        if (text != null) {
            menuItem.textView.setText(text);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width != 0 ? width : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.leftMargin = layoutParams.rightMargin = dp(14);
            addView(menuItem, index, layoutParams);
        } else {
            if (drawable != null) {
                if (drawable instanceof RLottieDrawable) {
                    menuItem.iconView.setAnimation((RLottieDrawable) drawable);
                } else {
                    menuItem.iconView.setImageDrawable(drawable);
                }
            } else if (icon != 0) {
                menuItem.iconView.setImageResource(icon);
            }
            addView(menuItem, index, new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        menuItem.setOnClickListener(view -> {
            ActionBarMenuItem item = (ActionBarMenuItem) view;
            if (item.hasSubMenu()) {
                if (parentActionBar.actionBarMenuOnItemClick.canOpenMenu()) {
                    item.toggleSubMenu();
                }
            } else if (item.isSearchField()) {
                parentActionBar.onSearchFieldVisibilityChanged(item.toggleSearch(true));
            } else {
                onItemClick((Integer) view.getTag());
            }
        });
        if (title != null) {
            menuItem.setContentDescription(title);
        }
        return menuItem;
    }

    public LazyItem lazilyAddItem(int id, int icon, Theme.ResourcesProvider resourcesProvider) {
        return lazilyAddItem(id, icon, null, isActionMode ? parentActionBar.itemsActionModeBackgroundColor : parentActionBar.itemsBackgroundColor, null, dp(48), null, resourcesProvider);
    }

    public LazyItem lazilyAddItem(int id, Drawable drawable, Theme.ResourcesProvider resourcesProvider) {
        return lazilyAddItem(id, 0, null, isActionMode ? parentActionBar.itemsActionModeBackgroundColor : parentActionBar.itemsBackgroundColor, drawable, dp(48), null, resourcesProvider);
    }

    public LazyItem lazilyAddItem(int id, int icon, CharSequence text, int backgroundColor, Drawable drawable, int width, CharSequence title, Theme.ResourcesProvider resourcesProvider) {
        if (ids == null) {
            ids = new ArrayList<>();
        }
        ids.add(id);
        return new LazyItem(this, id, icon, text, backgroundColor, drawable, width, title, resourcesProvider);
    }

    public static class LazyItem {
        ActionBarMenu parent;

        int id;
        int icon;
        CharSequence text;
        CharSequence contentDescription;
        int backgroundColor;
        Drawable drawable;
        int width;
        CharSequence title;
        Theme.ResourcesProvider resourcesProvider;

        float alpha = 1;
        Boolean overrideMenuClick;
        Boolean allowCloseAnimation;
        Boolean isSearchField;
        ActionBarMenuItem.ActionBarMenuItemSearchListener searchListener;
        CharSequence searchFieldHint;

        public LazyItem(ActionBarMenu parent, int id, int icon, CharSequence text, int backgroundColor, Drawable drawable, int width, CharSequence title, Theme.ResourcesProvider resourcesProvider) {
            this.parent = parent;
            this.id = id;
            this.icon = icon;
            this.text = text;
            this.backgroundColor = backgroundColor;
            this.drawable = drawable;
            this.width = width;
            this.title = title;
            this.resourcesProvider = resourcesProvider;
        }

        int visibility = GONE;
        ActionBarMenuItem cell;
        ArrayList<Utilities.Callback<ActionBarMenuItem>> onViews;

        public void setVisibility(int visibility) {
            if (this.visibility != visibility) {
                this.visibility = visibility;
                if (visibility == VISIBLE) {
                    add();
                }
                if (cell != null) {
                    cell.setVisibility(visibility);
                }
            }
        }

        public int getVisibility() {
            return visibility;
        }

        Object tag;
        public Object getTag() {
            return tag;
        }
        public void setTag(Object tag) {
            this.tag = tag;
        }

        @Nullable
        public ActionBarMenuItem getView() {
            return cell;
        }

        public ActionBarMenuItem createView() {
            add();
            return cell;
        }

        public void setContentDescription(CharSequence contentDescription) {
            this.contentDescription = contentDescription;
            if (cell != null) {
                cell.setContentDescription(contentDescription);
            }
        }

        public void setOverrideMenuClick(boolean value) {
            overrideMenuClick = value;
            if (cell != null) {
                cell.setOverrideMenuClick(value);
            }
        }

        public void setAllowCloseAnimation(boolean value) {
            allowCloseAnimation = value;
            if (cell != null) {
                cell.setAllowCloseAnimation(allowCloseAnimation);
            }
        }

        public void setIsSearchField(boolean value) {
            isSearchField = value;
            if (cell != null) {
                cell.setIsSearchField(isSearchField);
            }
        }

        public void setActionBarMenuItemSearchListener(ActionBarMenuItem.ActionBarMenuItemSearchListener listener) {
            this.searchListener = listener;
            if (cell != null) {
                cell.setActionBarMenuItemSearchListener(listener);
            }
        }

        public void setSearchFieldHint(CharSequence searchFieldHint) {
            this.searchFieldHint = searchFieldHint;
            if (cell != null) {
                cell.setSearchFieldHint(searchFieldHint);
            }
        }

        public void setAlpha(float alpha) {
            this.alpha = alpha;
            if (cell != null) {
                cell.setAlpha(alpha);
            }
        }

        public void add() {
            if (cell != null) {
                return;
            }

            int index = parent.getChildCount();
            if (parent.ids != null) {
                int myIndex = parent.ids.indexOf(this.id);
                for (int i = 0; i < parent.getChildCount(); ++i) {
                    View child = parent.getChildAt(i);
                    Object tag = child.getTag();
                    if (tag instanceof Integer) {
                        int thisId = (Integer) tag;
                        int thisIndex = parent.ids.indexOf(thisId);
                        if (thisIndex > myIndex) {
                            index = i;
                            break;
                        }
                    }
                }
            }
            cell = parent.addItemAt(index, id, icon, text, backgroundColor, drawable, width, title, resourcesProvider);
            cell.setVisibility(visibility);
            if (contentDescription != null) {
                cell.setContentDescription(contentDescription);
            }
            if (allowCloseAnimation != null) {
                cell.setAllowCloseAnimation(allowCloseAnimation);
            }
            if (overrideMenuClick != null) {
                cell.setOverrideMenuClick(overrideMenuClick);
            }
            if (isSearchField != null) {
                cell.setIsSearchField(isSearchField);
            }
            if (searchListener != null) {
                cell.setActionBarMenuItemSearchListener(searchListener);
            }
            if (searchFieldHint != null) {
                cell.setSearchFieldHint(searchFieldHint);
            }
            cell.setAlpha(alpha);

            if (onViews != null) {
                for (Utilities.Callback<ActionBarMenuItem> onView : onViews)
                    onView.run(cell);
                onViews = null;
            }
        }

        public void onView(Utilities.Callback<ActionBarMenuItem> onView) {
            if (cell != null) {
                onView.run(cell);
                return;
            }
            if (onViews == null) onViews = new ArrayList<>();
            onViews.add(onView);
        }
    }

    public void hideAllPopupMenus() {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem) view).closeSubMenu();
            }
        }
    }

    protected void setPopupItemsColor(int color, boolean icon) {
        for (int a = 0, count = getChildCount(); a < count; a++) {
            final View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem) view).setPopupItemsColor(color, icon);
            }
        }
    }

    protected void setPopupItemsSelectorColor(int color) {
        for (int a = 0, count = getChildCount(); a < count; a++) {
            final View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem) view).setPopupItemsSelectorColor(color);
            }
        }
    }

    protected void redrawPopup(int color) {
        for (int a = 0, count = getChildCount(); a < count; a++) {
            final View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem) view).redrawPopup(color);
            }
        }
    }

    public void onItemClick(int id) {
        if (parentActionBar.actionBarMenuOnItemClick != null) {
            parentActionBar.actionBarMenuOnItemClick.onItemClick(id);
        }
    }

    public void clearItems() {
        if (ids != null) {
            ids.clear();
        }
        removeAllViews();
    }

    public void onMenuButtonPressed() {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.getVisibility() != VISIBLE) {
                    continue;
                }
                if (item.hasSubMenu()) {
                    item.toggleSubMenu();
                    break;
                } else if (item.overrideMenuClick) {
                    onItemClick((Integer) item.getTag());
                    break;
                }
            }
        }
    }

    public void closeSearchField(boolean closeKeyboard) {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.isSearchField() && item.isSearchFieldVisible()) {
                    if (item.listener == null || item.listener.canCollapseSearch()) {
                        parentActionBar.onSearchFieldVisibilityChanged(false);
                        item.toggleSearch(closeKeyboard);
                    }
                    break;
                }
            }
        }
    }

    public void setSearchCursorColor(int color) {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.isSearchField()) {
                    item.getSearchField().setCursorColor(color);
                    break;
                }
            }
        }
    }

    public void setSearchTextColor(int color, boolean placeholder) {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.isSearchField()) {
                    if (placeholder) {
                        item.getSearchField().setHintTextColor(color);
                    } else {
                        item.getSearchField().setTextColor(color);
                    }
                    break;
                }
            }
        }
    }

    public void setSearchFieldText(String text) {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.isSearchField()) {
                    item.setSearchFieldText(text, false);
                    item.getSearchField().setSelection(text.length());
                }
            }
        }
    }

    public void onSearchPressed() {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.isSearchField()) {
                    item.onSearchPressed();
                }
            }
        }
    }

    public void openSearchField(boolean toggle, boolean showKeyboard, String text, boolean animated) {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.isSearchField()) {
                    if (toggle) {
                        parentActionBar.onSearchFieldVisibilityChanged(item.toggleSearch(showKeyboard));
                    }
                    item.setSearchFieldText(text, animated);
                    item.getSearchField().setSelection(text.length());
                    break;
                }
            }
        }
    }

    public void setFilter(FiltersView.MediaFilterData filter) {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.isSearchField()) {
                    item.addSearchFilter(filter);
                    break;
                }
            }
        }
    }

    public ActionBarMenuItem getItem(int id) {
        View v = findViewWithTag(id);
        if (v instanceof ActionBarMenuItem) {
            return (ActionBarMenuItem) v;
        }
        return null;
    }

    public void setItemVisibility(int id, int visibility) {
        View item = getItem(id);
        if (item != null) {
            item.setVisibility(visibility);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            view.setEnabled(enabled);
        }
    }

    public int getItemsMeasuredWidth(boolean ignoreAlpha) {
        int w = 0;
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (!ignoreAlpha && (view.getAlpha() == 0 || view.getVisibility() != View.VISIBLE)) {
                continue;
            }
            if (view instanceof ActionBarMenuItem) {
                w += view.getMeasuredWidth();
            }
        }
        return w;
    }

    public int getVisibleItemsMeasuredWidth() {
        int w = 0;
        for (int i = 0, count = getChildCount(); i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ActionBarMenuItem && view.getVisibility() != View.GONE) {
                w += view.getMeasuredWidth();
            }
        }
        return w;
    }

    public int getVisibleItemsMeasuredWidthWithAlpha() {
        float w = 0;
        for (int i = 0, count = getChildCount(); i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof ActionBarMenuItem && view.getVisibility() == View.VISIBLE) {
                w += view.getMeasuredWidth() * view.getAlpha();
            }
        }
        return (int) w;
    }

    public boolean searchFieldVisible() {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem && ((ActionBarMenuItem) view).getSearchContainer() != null && ((ActionBarMenuItem) view).getSearchContainer().getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return false;
    }

    public void translateXItems(float offset) {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ((ActionBarMenuItem) view).setTransitionOffset(offset);
            }
        }
    }

    public void clearSearchFilters() {
        int count = getChildCount();
        for (int a = 0; a < count; a++) {
            View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem) {
                ActionBarMenuItem item = (ActionBarMenuItem) view;
                if (item.isSearchField()) {
                    item.clearSearchFilters();
                    break;
                }
            }
        }
    }

    private Runnable onLayoutListener;
    public void setOnLayoutListener(Runnable listener) {
        this.onLayoutListener = listener;
    }

    public void setGlassMode(boolean glassMode) {
        this.glassMode = glassMode;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (glassMode) {
            for (int a = 0, N = getChildCount(); a < N; a++) {
                final View view = getChildAt(a);
                if (view instanceof ActionBarMenuItem) {
                    final ViewGroup.LayoutParams lp = view.getLayoutParams();
                    if (lp instanceof MarginLayoutParams) {
                        MarginLayoutParams mlp = (MarginLayoutParams) lp;
                        mlp.leftMargin = -dp(5);
                        mlp.rightMargin = -dp(5);
                    }
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getItemsWidth() {
        float mLeft = Float.POSITIVE_INFINITY;
        float mRight = Float.NEGATIVE_INFINITY;
        boolean found = false;


        for (int a = 0, N = getChildCount(); a < N; a++) {
            final View view = getChildAt(a);
            if (view instanceof ActionBarMenuItem && view.getVisibility() == View.VISIBLE) {
                final float left = view.getX();
                final float right = left + view.getWidth();
                mLeft = Math.min(mLeft, left);
                mRight = Math.max(mRight, right);
                found = true;
            }
        }

        return found ? (int)(mRight - mLeft) : 0;
    }

    private final ArrayList<View> m3VisibleChildren = new ArrayList<>();
    private final Map<View, M3ChildState> m3ChildStates = new HashMap<>();
    private float m3ChildSizeChange = 0.18f;
    private float m3OuterCornerRadius = dp(20);
    private float m3InnerCornerRadius = dp(8);
    private float m3PressedCornerRadius = dp(16);
    private boolean m3IsConnected = true;

    public static class M3ChildState {
        public final View view;
        public final SpringAnimation springAnimation;
        public float progress = 0f;
        public boolean pressed = false;
        public float baseWeight = 1.0f;

        public M3ChildState(View view, Runnable onUpdate) {
            this.view = view;
            springAnimation = new SpringAnimation(new FloatValueHolder(0f));
            SpringForce force = new SpringForce(0f);
            force.setStiffness(500f);
            force.setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
            springAnimation.setSpring(force);
            springAnimation.addUpdateListener((animation, value, velocity) -> {
                progress = value;
                if (view.getBackground() instanceof M3ExpressiveButtonDrawable) {
                    ((M3ExpressiveButtonDrawable) view.getBackground()).setMorphProgress(progress);
                }
                onUpdate.run();
            });
        }
    }

    public void updateChildShapes() {
        if (!UIStyleEngine.isMaterial3Expressive() || isActionMode) return;
        m3VisibleChildren.clear();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE && child instanceof ActionBarMenuItem && !((ActionBarMenuItem) child).isSearchField()) {
                m3VisibleChildren.add(child);
            }
        }
        int totalVisible = m3VisibleChildren.size();
        if (totalVisible == 0) return;

        float outer = m3OuterCornerRadius;
        float inner = m3IsConnected && totalVisible > 1 ? m3InnerCornerRadius : m3OuterCornerRadius;
        float morph = m3PressedCornerRadius;

        for (int i = 0; i < totalVisible; i++) {
            View child = m3VisibleChildren.get(i);
            Drawable bg = child.getBackground();
            if (bg instanceof M3ExpressiveButtonDrawable) {
                M3ExpressiveButtonDrawable drawable = (M3ExpressiveButtonDrawable) bg;
                float[] restRadii;
                float[] pressedRadii = new float[]{morph, morph, morph, morph, morph, morph, morph, morph};

                if (totalVisible == 1 || !m3IsConnected) {
                    restRadii = new float[]{outer, outer, outer, outer, outer, outer, outer, outer};
                } else if (i == 0) {
                    // Start child (leftmost)
                    restRadii = new float[]{outer, outer, inner, inner, inner, inner, outer, outer};
                } else if (i == totalVisible - 1) {
                    // End child (rightmost)
                    restRadii = new float[]{inner, inner, outer, outer, outer, outer, inner, inner};
                } else {
                    // Middle child
                    restRadii = new float[]{inner, inner, inner, inner, inner, inner, inner, inner};
                }
                drawable.setRadii(restRadii, pressedRadii);
            }
        }
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (UIStyleEngine.isMaterial3Expressive() && !isActionMode) {
            if (!m3ChildStates.containsKey(child)) {
                M3ChildState state = new M3ChildState(child, this::applyM3ChildLayouts);
                m3ChildStates.put(child, state);
            }
            updateChildShapes();
        }
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if (UIStyleEngine.isMaterial3Expressive() && !isActionMode) {
            M3ChildState state = m3ChildStates.remove(child);
            if (state != null) {
                state.springAnimation.cancel();
            }
            updateChildShapes();
        }
    }

    @Override
    public void childDrawableStateChanged(View child) {
        super.childDrawableStateChanged(child);
        if (UIStyleEngine.isMaterial3Expressive() && !isActionMode) {
            M3ChildState state = m3ChildStates.get(child);
            if (state != null) {
                boolean isPressed = child.isPressed() || child.isSelected();
                if (state.pressed != isPressed) {
                    state.pressed = isPressed;
                    state.springAnimation.animateToFinalPosition(isPressed ? 1f : 0f);
                }
            }
        }
    }

    private void applyM3ChildLayouts() {
        if (!UIStyleEngine.isMaterial3Expressive() || isActionMode) return;
        m3VisibleChildren.clear();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE && child instanceof ActionBarMenuItem && !((ActionBarMenuItem) child).isSearchField()) {
                m3VisibleChildren.add(child);
                if (!m3ChildStates.containsKey(child)) {
                    m3ChildStates.put(child, new M3ChildState(child, this::applyM3ChildLayouts));
                }
            }
        }
        int visibleCount = m3VisibleChildren.size();
        if (visibleCount <= 1) {
            invalidate();
            return;
        }

        int totalW = getWidth() - getPaddingLeft() - getPaddingRight();
        int totalH = getHeight() - getPaddingTop() - getPaddingBottom();
        if (totalW <= 0 || totalH <= 0) {
            invalidate();
            return;
        }

        float[] weights = new float[visibleCount];
        float totalWeight = 0f;
        float totalExpansion = 0f;
        int pressedCount = 0;

        for (int i = 0; i < visibleCount; i++) {
            View child = m3VisibleChildren.get(i);
            M3ChildState state = m3ChildStates.get(child);
            float progress = state != null ? state.progress : 0f;
            if (progress > 0.001f) {
                totalExpansion += m3ChildSizeChange * progress;
                pressedCount++;
            }
        }

        for (int i = 0; i < visibleCount; i++) {
            View child = m3VisibleChildren.get(i);
            M3ChildState state = m3ChildStates.get(child);
            float base = state != null ? state.baseWeight : 1.0f;
            float progress = state != null ? state.progress : 0f;
            if (progress > 0.001f) {
                weights[i] = base * (1f + m3ChildSizeChange * progress);
            } else if (visibleCount > pressedCount && totalExpansion > 0f) {
                float shrink = totalExpansion / (float) (visibleCount - pressedCount);
                weights[i] = Math.max(0.5f * base, base * (1f - shrink));
            } else {
                weights[i] = base;
            }
            totalWeight += weights[i];
        }

        int curX = getPaddingLeft();
        int topY = getPaddingTop();

        for (int i = 0; i < visibleCount; i++) {
            View child = m3VisibleChildren.get(i);
            int childW;
            if (i == visibleCount - 1) {
                childW = (getPaddingLeft() + totalW) - curX;
            } else {
                childW = Math.round((weights[i] / totalWeight) * totalW);
            }
            childW = Math.max(dp(28), childW);

            child.measure(
                MeasureSpec.makeMeasureSpec(childW, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(totalH, MeasureSpec.EXACTLY)
            );
            child.layout(curX, topY, curX + childW, topY + totalH);
            curX += childW;
        }
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (UIStyleEngine.isMaterial3Expressive() && !isActionMode) {
            updateChildShapes();
            if (m3VisibleChildren.size() > 1) {
                applyM3ChildLayouts();
            }
        }
        if (parentActionBar != null) {
            parentActionBar.checkMenuItemsWidth();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}

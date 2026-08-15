package com.exteragram.messenger.plugins.ui.components.templates;

import android.content.Context;
import android.view.View;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;

/* JADX INFO: loaded from: classes4.dex */
public final class UniversalFragment extends org.telegram.ui.Components.UniversalFragment {
    private UniversalFragmentDelegate delegate;

    public UniversalFragment() {
        this((UniversalFragmentDelegate) null);
    }

    public UniversalFragment(UniversalFragmentDelegate universalFragmentDelegate) {
        this.delegate = universalFragmentDelegate;
    }

    public /* synthetic */ UniversalFragment(UniversalFragmentDelegate universalFragmentDelegate, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : universalFragmentDelegate);
    }

    public final UniversalFragmentDelegate getDelegate() {
        return this.delegate;
    }

    public final void setDelegate(UniversalFragmentDelegate universalFragmentDelegate) {
        this.delegate = universalFragmentDelegate;
    }

    @Override // org.telegram.ui.Components.UniversalFragment, org.telegram.ui.ActionBar.BaseFragment
    public View createView(Context context) {
        UniversalFragmentDelegate universalFragmentDelegate = this.delegate;
        View viewBeforeCreateView = universalFragmentDelegate != null ? universalFragmentDelegate.beforeCreateView() : null;
        if (viewBeforeCreateView != null) {
            return viewBeforeCreateView;
        }
        View viewCreateView = super.createView(context);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFragment.createView.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int id) {
                UniversalFragment universalFragment = UniversalFragment.this;
                if (id == -1) {
                    if (universalFragment.onBackPressed(true)) {
                        UniversalFragment.this.finishFragment();
                    }
                } else {
                    UniversalFragmentDelegate delegate = universalFragment.getDelegate();
                    if (delegate != null) {
                        delegate.onMenuItemClick(id);
                    }
                }
            }
        });
        UniversalFragmentDelegate universalFragmentDelegate2 = this.delegate;
        View viewAfterCreateView = universalFragmentDelegate2 != null ? universalFragmentDelegate2.afterCreateView(viewCreateView) : null;
        return viewAfterCreateView == null ? viewCreateView : viewAfterCreateView;
    }

    public final ActionBarMenu getActionBarMenu() {
        return this.actionBar.createMenu();
    }

    @Override // org.telegram.ui.Components.UniversalFragment
    public CharSequence getTitle() {
        UniversalFragmentDelegate universalFragmentDelegate = this.delegate;
        if (universalFragmentDelegate != null) {
            return universalFragmentDelegate.getTitle();
        }
        return null;
    }

    public final void setTitle(CharSequence title, boolean animated, long duration) {
        ActionBar actionBar = this.actionBar;
        if (animated) {
            if (duration <= 0) {
                duration = 300;
            }
            actionBar.setTitleAnimated(title, false, duration);
            return;
        }
        actionBar.setTitle(title);
    }

    @Override // org.telegram.ui.Components.UniversalFragment
    public void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        UniversalFragmentDelegate universalFragmentDelegate = this.delegate;
        if (universalFragmentDelegate != null) {
            universalFragmentDelegate.fillItems(items, adapter);
        }
    }

    @Override // org.telegram.ui.Components.UniversalFragment
    public void onClick(UItem item, View view, int position, float x, float y) {
        UniversalFragmentDelegate universalFragmentDelegate = this.delegate;
        if (universalFragmentDelegate != null) {
            universalFragmentDelegate.onClick(item, view, position, x, y);
        }
    }

    @Override // org.telegram.ui.Components.UniversalFragment
    public boolean onLongClick(UItem item, View view, int position, float x, float y) {
        UniversalFragmentDelegate universalFragmentDelegate = this.delegate;
        if (universalFragmentDelegate != null) {
            return universalFragmentDelegate.onLongClick(item, view, position, x, y);
        }
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        UniversalFragmentDelegate universalFragmentDelegate = this.delegate;
        if (universalFragmentDelegate != null) {
            universalFragmentDelegate.onFragmentCreate();
        }
        return super.onFragmentCreate();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        UniversalFragmentDelegate universalFragmentDelegate = this.delegate;
        if (universalFragmentDelegate != null) {
            universalFragmentDelegate.onFragmentDestroy();
        }
        super.onFragmentDestroy();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed(boolean invoked) {
        UniversalFragmentDelegate universalFragmentDelegate = this.delegate;
        Boolean boolOnBackPressed = universalFragmentDelegate != null ? universalFragmentDelegate.onBackPressed() : null;
        if (boolOnBackPressed != null) {
            return !boolOnBackPressed.booleanValue();
        }
        return super.onBackPressed(invoked);
    }

    public interface UniversalFragmentDelegate {
        default View afterCreateView(View view) {
            return view;
        }

        default View beforeCreateView() {
            return null;
        }

        default void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        }

        default CharSequence getTitle() {
            return null;
        }

        default Boolean onBackPressed() {
            return null;
        }

        default void onClick(UItem item, View view, int position, float x, float y) {
        }

        default void onFragmentCreate() {
        }

        default void onFragmentDestroy() {
        }

        default boolean onLongClick(UItem item, View view, int position, float x, float y) {
            return false;
        }

        default void onMenuItemClick(int id) {
        }

    }
}

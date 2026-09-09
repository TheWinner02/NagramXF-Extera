package org.telegram.ui.Components.Premium.boosts.cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.ColorUtils;

import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.CombinedDrawable;
import xyz.nextalone.nagram.ui.M3ColorRoles;

@SuppressLint("ViewConstructor")
public class TextInfoCell extends TextInfoPrivacyCell {
    private final Theme.ResourcesProvider resourcesProvider;

    public TextInfoCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, resourcesProvider);
        this.resourcesProvider = resourcesProvider;
    }

    public void setBackground(boolean onlyTopDivider) {
        Drawable shadowDrawable = Theme.getThemedDrawable(getContext(), onlyTopDivider ? R.drawable.greydivider_bottom : R.drawable.greydivider, Theme.getColor(Theme.key_windowBackgroundGrayShadow, resourcesProvider));
        Drawable background = new ColorDrawable(getBackgroundColor());
        CombinedDrawable combinedDrawable = new CombinedDrawable(background, shadowDrawable, 0, 0);
        combinedDrawable.setFullsize(true);
        setBackground(combinedDrawable);
    }

    private int getBackgroundColor() {
        int fallbackColor = Theme.getColor(Theme.key_windowBackgroundGray, resourcesProvider);
        if (!xyz.nextalone.nagram.ui.UIStyleEngine.isMaterial3Expressive()) {
            return fallbackColor;
        }
        int color = M3ColorRoles.get(M3ColorRoles.Role.SURFACE, fallbackColor);
        return ColorUtils.setAlphaComponent(color, Color.alpha(fallbackColor));
    }
}

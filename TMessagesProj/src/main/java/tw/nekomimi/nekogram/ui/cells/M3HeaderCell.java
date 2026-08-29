package tw.nekomimi.nekogram.ui.cells;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public class M3HeaderCell extends FrameLayout {

    private final ImageView iconView;
    private final TextView titleView;
    private final Theme.ResourcesProvider resourcesProvider;

    public M3HeaderCell(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.resourcesProvider = resourcesProvider;

        iconView = new ImageView(context);
        iconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        addView(iconView, LayoutHelper.createFrame(48, 48, Gravity.TOP | (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT), 24, 16, 24, 0));

        titleView = new TextView(context);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28);
        titleView.setTypeface(AndroidUtilities.bold());
        titleView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
        addView(titleView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP | (LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT), 24, 76, 24, 16));
    }

    public void setIconAndTitle(int iconRes, CharSequence title) {
        if (iconRes != 0) {
            iconView.setImageResource(iconRes);
            int color = Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider);
            iconView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            iconView.setVisibility(VISIBLE);
        } else {
            iconView.setVisibility(GONE);
        }
        titleView.setText(title);
    }

    public void setIconAlpha(float alpha) {
        iconView.setAlpha(alpha);
    }

    public void updateColors() {
        int color = Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider);
        iconView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        titleView.setTextColor(color);
    }
}

package tw.nekomimi.nekogram.ui.cells;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

import tw.nekomimi.nekogram.ui.components.M3ConnectedButtonGroup;

public class M3ConnectedButtonGroupCell extends FrameLayout {
    private final TextView titleTextView;
    private final M3ConnectedButtonGroup buttonGroup;

    public M3ConnectedButtonGroupCell(Context context) {
        super(context);
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);

        titleTextView = new TextView(context);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        titleTextView.setTypeface(AndroidUtilities.bold());
        titleTextView.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        addView(titleTextView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 24, Gravity.TOP | Gravity.LEFT, 21, 8, 21, 0));

        buttonGroup = new M3ConnectedButtonGroup(context, null);
        addView(buttonGroup, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.LEFT | Gravity.BOTTOM, 16, 0, 16, 10));
    }

    public void setTextAndItems(String title, String[] items, int selectedIndex, Theme.ResourcesProvider resourcesProvider, M3ConnectedButtonGroup.OnItemSelectedListener listener) {
        titleTextView.setText(title);
        titleTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader, resourcesProvider));
        buttonGroup.setResourcesProvider(resourcesProvider);
        buttonGroup.setItems(items, selectedIndex);
        buttonGroup.setOnItemSelectedListener(listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int buttonWidth = Math.max(0, width - dp(32));
        int buttonHeight = buttonGroup.getPreferredHeight(buttonWidth);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(dp(42) + buttonHeight + dp(10), MeasureSpec.EXACTLY));
        setMeasuredDimension(width, dp(42) + buttonHeight + dp(10));
    }
}

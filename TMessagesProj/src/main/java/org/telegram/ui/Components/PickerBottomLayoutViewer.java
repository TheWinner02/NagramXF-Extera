/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package org.telegram.ui.Components;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;

import xyz.nextalone.nagram.ui.UIStyleEngine;

public class PickerBottomLayoutViewer extends FrameLayout {

    public TextView cancelButton;
    public TextView doneButton;
    public TextView doneButtonBadgeTextView;
    public TextView originalButton;
    public M3ExpressiveButtonGroup buttonGroup;

    private boolean isDarkTheme;

    public PickerBottomLayoutViewer(Context context) {
        this(context, true);
    }

    public PickerBottomLayoutViewer(Context context, boolean darkTheme) {
        this(context, darkTheme, false);
    }

    public PickerBottomLayoutViewer(Context context, boolean darkTheme, boolean needOriginal) {
        super(context);
        isDarkTheme = darkTheme;

        boolean isM3 = UIStyleEngine.isMaterial3Expressive();

        setBackgroundColor(isM3 ? (isDarkTheme ? 0x00000000 : 0x00000000) : (isDarkTheme ? 0xff1a1a1a : 0xffffffff));

        cancelButton = new TextView(context);
        cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        cancelButton.setTextColor(isDarkTheme ? 0xffffffff : 0xff19a7e8);
        cancelButton.setGravity(Gravity.CENTER);
        cancelButton.setText(LocaleController.getString(R.string.Cancel).toUpperCase());
        cancelButton.setTypeface(AndroidUtilities.bold());

        if (needOriginal) {
            originalButton = new TextView(context);
            originalButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            originalButton.setTextColor(isDarkTheme ? 0xffffffff : 0xff19a7e8);
            originalButton.setGravity(Gravity.CENTER);
            originalButton.setText(LocaleController.getString(R.string.QualityOriginal).toUpperCase());
            originalButton.setTypeface(AndroidUtilities.bold());
        }

        doneButton = new TextView(context);
        doneButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        doneButton.setTextColor(isDarkTheme ? 0xffffffff : 0xff19a7e8);
        doneButton.setGravity(Gravity.CENTER);
        doneButton.setText(LocaleController.getString(R.string.Send).toUpperCase());
        doneButton.setTypeface(AndroidUtilities.bold());

        doneButtonBadgeTextView = new TextView(context);
        doneButtonBadgeTextView.setTypeface(AndroidUtilities.bold());
        doneButtonBadgeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        doneButtonBadgeTextView.setTextColor(0xffffffff);
        doneButtonBadgeTextView.setGravity(Gravity.CENTER);
        doneButtonBadgeTextView.setBackgroundResource(isDarkTheme ? R.drawable.photobadge : R.drawable.bluecounter);
        doneButtonBadgeTextView.setMinWidth(AndroidUtilities.dp(23));
        doneButtonBadgeTextView.setPadding(AndroidUtilities.dp(8), 0, AndroidUtilities.dp(8), AndroidUtilities.dp(1));

        if (isM3) {
            buttonGroup = new M3ExpressiveButtonGroup(context);
            buttonGroup.setSpacing(AndroidUtilities.dp(6));
            buttonGroup.setOuterCornerRadius(AndroidUtilities.dp(22));
            buttonGroup.setInnerCornerRadius(AndroidUtilities.dp(8));
            buttonGroup.setPressedCornerRadius(AndroidUtilities.dp(16));
            buttonGroup.setChildSizeChange(0.18f);

            int btnBg = isDarkTheme ? 0x24FFFFFF : 0x18000000;
            int strokeColor = isDarkTheme ? 0x30FFFFFF : 0x24000000;
            int pressedOverlay = isDarkTheme ? 0x28FFFFFF : 0x1A000000;

            cancelButton.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
            M3ExpressiveButtonDrawable cancelDrawable = M3ExpressiveButtonDrawable.createOutlined(btnBg, strokeColor, pressedOverlay, AndroidUtilities.dp(22));
            buttonGroup.addView(cancelButton, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));
            buttonGroup.setChildDrawable(cancelButton, cancelDrawable);

            if (needOriginal && originalButton != null) {
                originalButton.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
                M3ExpressiveButtonDrawable origDrawable = M3ExpressiveButtonDrawable.createOutlined(btnBg, strokeColor, pressedOverlay, AndroidUtilities.dp(22));
                buttonGroup.addView(originalButton, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));
                buttonGroup.setChildDrawable(originalButton, origDrawable);
            }

            doneButton.setPadding(AndroidUtilities.dp(16), 0, AndroidUtilities.dp(16), 0);
            M3ExpressiveButtonDrawable doneDrawable = M3ExpressiveButtonDrawable.createOutlined(btnBg, strokeColor, pressedOverlay, AndroidUtilities.dp(22));
            buttonGroup.addView(doneButton, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT));
            buttonGroup.setChildDrawable(doneButton, doneDrawable);

            addView(buttonGroup, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 44, Gravity.CENTER, 12, 2, 12, 2));
            addView(doneButtonBadgeTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 23, Gravity.TOP | Gravity.RIGHT, 0, 0, 16, 0));
        } else {
            cancelButton.setBackground(Theme.createSelectorDrawable(isDarkTheme ? Theme.ACTION_BAR_PICKER_SELECTOR_COLOR : Theme.ACTION_BAR_AUDIO_SELECTOR_COLOR, 0));
            cancelButton.setPadding(AndroidUtilities.dp(20), 0, AndroidUtilities.dp(20), 0);
            addView(cancelButton, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.TOP | Gravity.LEFT));

            if (needOriginal && originalButton != null) {
                originalButton.setBackground(Theme.createSelectorDrawable(isDarkTheme ? Theme.ACTION_BAR_PICKER_SELECTOR_COLOR : Theme.ACTION_BAR_AUDIO_SELECTOR_COLOR, 0));
                originalButton.setPadding(AndroidUtilities.dp(20), 0, AndroidUtilities.dp(20), 0);
                addView(originalButton, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL));
            }

            doneButton.setBackgroundDrawable(Theme.createSelectorDrawable(isDarkTheme ? Theme.ACTION_BAR_PICKER_SELECTOR_COLOR : Theme.ACTION_BAR_AUDIO_SELECTOR_COLOR, 0));
            doneButton.setPadding(AndroidUtilities.dp(20), 0, AndroidUtilities.dp(20), 0);
            addView(doneButton, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, Gravity.TOP | Gravity.RIGHT));

            addView(doneButtonBadgeTextView, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT, 23, Gravity.TOP | Gravity.RIGHT, 0, 0, 7, 0));
        }
    }

    public void updateSelectedCount(int count, boolean disable) {
        if (count == 0) {
            doneButtonBadgeTextView.setVisibility(View.GONE);

            if (disable) {
                doneButton.setTextColor(0xff999999);
                doneButton.setEnabled(false);
            } else {
                doneButton.setTextColor(isDarkTheme ? 0xffffffff : 0xff19a7e8);
            }
        } else {
            doneButtonBadgeTextView.setVisibility(View.VISIBLE);
            doneButtonBadgeTextView.setText(String.format("%d", count));

            doneButton.setTextColor(isDarkTheme ? 0xffffffff : 0xff19a7e8);
            if (disable) {
                doneButton.setEnabled(true);
            }
        }
    }
}

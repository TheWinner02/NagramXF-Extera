package tw.nekomimi.nekogram.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.graphics.ColorUtils;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;

public class M3ConnectedButtonGroup extends View {

    public interface OnItemSelectedListener {
        void onItemSelected(int index);
    }

    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private final Path path = new Path();
    private final float[] radii = new float[8];

    private Theme.ResourcesProvider resourcesProvider;
    private String[] items = new String[0];
    private int selectedIndex;
    private int pressedIndex = -1;
    private OnItemSelectedListener listener;

    public M3ConnectedButtonGroup(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.resourcesProvider = resourcesProvider;
        setClickable(true);
        setFocusable(true);
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics()));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(AndroidUtilities.bold());
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(AndroidUtilities.dp(1));
    }

    public void setResourcesProvider(Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
        invalidate();
    }

    public void setItems(String[] items, int selectedIndex) {
        this.items = items != null ? items : new String[0];
        this.selectedIndex = Math.max(0, Math.min(selectedIndex, Math.max(0, this.items.length - 1)));
        requestLayout();
        invalidate();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getPreferredHeight(width);
        setMeasuredDimension(width, height);
    }

    public int getPreferredHeight(int width) {
        int rows = getRowCount(width);
        return rows * AndroidUtilities.dp(48) + (rows - 1) * AndroidUtilities.dp(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = items.length;
        if (count == 0) {
            return;
        }

        final boolean rtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
        final float gap = AndroidUtilities.dp(2);
        final int rows = getRowCount(getWidth());
        final int columns = Math.max(1, (int) Math.ceil(count / (float) rows));
        final float segmentWidth = (getWidth() - getPaddingLeft() - getPaddingRight() - gap * (columns - 1)) / columns;
        final float segmentHeight = (getHeight() - getPaddingTop() - getPaddingBottom() - gap * (rows - 1)) / rows;
        final int surfaceColor = Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider);
        final int selectedColor = Theme.getColor(Theme.key_switch2TrackChecked, resourcesProvider);
        final int outlineColor = ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_windowBackgroundWhiteGrayIcon, resourcesProvider), 80);
        final int selectorColor = Theme.getColor(Theme.key_listSelector, resourcesProvider);
        final int unselectedColor = ColorUtils.blendARGB(surfaceColor, Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider), 0.05f);

        for (int visualIndex = 0; visualIndex < count; visualIndex++) {
            int row = visualIndex / columns;
            int column = visualIndex % columns;
            int visualColumn = rtl ? columns - column - 1 : column;
            int itemIndex = row * columns + (rtl ? visualColumn : column);
            if (itemIndex < 0 || itemIndex >= count) {
                continue;
            }
            float left = getPaddingLeft() + visualColumn * (segmentWidth + gap);
            float right = left + segmentWidth;
            float top = getPaddingTop() + row * (segmentHeight + gap);
            float bottom = top + segmentHeight;
            rect.set(left, top, right, bottom);

            boolean selected = itemIndex == selectedIndex;
            boolean pressed = itemIndex == pressedIndex;
            int fillColor = selected ? selectedColor : unselectedColor;
            if (pressed && !selected) {
                fillColor = ColorUtils.blendARGB(fillColor, selectorColor, 0.45f);
            } else if (pressed) {
                fillColor = ColorUtils.blendARGB(fillColor, Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider), 0.12f);
            }

            backgroundPaint.setColor(fillColor);
            buildSegmentPath(row, visualColumn, rows, columns);
            canvas.drawPath(path, backgroundPaint);
            if (!selected) {
                strokePaint.setColor(outlineColor);
                canvas.drawPath(path, strokePaint);
            }

            textPaint.setColor(selected ? Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider) : Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
            drawLabel(canvas, items[itemIndex], rect);
        }
    }

    private void drawLabel(Canvas canvas, String text, RectF bounds) {
        float availableWidth = Math.max(0, bounds.width() - AndroidUtilities.dp(14));
        if (textPaint.measureText(text) <= availableWidth) {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float textY = bounds.centerY() - (fontMetrics.ascent + fontMetrics.descent) / 2f;
            canvas.drawText(text, bounds.centerX(), textY, textPaint);
            return;
        }

        String firstLine = text;
        String secondLine = "";
        int split = findBestSplit(text, availableWidth);
        if (split > 0) {
            firstLine = text.substring(0, split).trim();
            secondLine = text.substring(split).trim();
        }
        firstLine = TextUtils.ellipsize(firstLine, textPaint, availableWidth, TextUtils.TruncateAt.END).toString();
        secondLine = TextUtils.ellipsize(secondLine, textPaint, availableWidth, TextUtils.TruncateAt.END).toString();
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float lineHeight = fontMetrics.descent - fontMetrics.ascent;
        float firstY = bounds.centerY() - lineHeight / 2f - fontMetrics.ascent - AndroidUtilities.dp(1);
        canvas.drawText(firstLine, bounds.centerX(), firstY, textPaint);
        if (!secondLine.isEmpty()) {
            canvas.drawText(secondLine, bounds.centerX(), firstY + lineHeight, textPaint);
        }
    }

    private int findBestSplit(String text, float availableWidth) {
        int best = -1;
        float bestOverflow = Float.MAX_VALUE;
        for (int i = 1; i < text.length() - 1; i++) {
            if (!Character.isWhitespace(text.charAt(i))) {
                continue;
            }
            String first = text.substring(0, i).trim();
            String second = text.substring(i).trim();
            float overflow = Math.max(textPaint.measureText(first), textPaint.measureText(second)) - availableWidth;
            overflow = Math.abs(overflow);
            if (overflow < bestOverflow) {
                bestOverflow = overflow;
                best = i;
            }
        }
        return best;
    }

    private void buildSegmentPath(int row, int column, int rows, int columns) {
        float outer = AndroidUtilities.dp(24);
        float inner = AndroidUtilities.dp(8);
        boolean top = row == 0;
        boolean bottom = row == rows - 1;
        boolean left = column == 0;
        boolean right = column == columns - 1;
        radii[0] = top && left ? outer : inner;
        radii[1] = radii[0];
        radii[2] = top && right ? outer : inner;
        radii[3] = radii[2];
        radii[4] = bottom && right ? outer : inner;
        radii[5] = radii[4];
        radii[6] = bottom && left ? outer : inner;
        radii[7] = radii[6];
        path.reset();
        path.addRoundRect(rect, radii, Path.Direction.CW);
    }

    private int getRowCount(int width) {
        return items.length > 3 ? 2 : 1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || items.length == 0) {
            return false;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                pressedIndex = findItemIndex(event.getX(), event.getY());
                invalidate();
                return pressedIndex >= 0;
            case MotionEvent.ACTION_MOVE:
                int moveIndex = findItemIndex(event.getX(), event.getY());
                if (moveIndex != pressedIndex) {
                    pressedIndex = moveIndex;
                    invalidate();
                }
                return true;
            case MotionEvent.ACTION_UP:
                int upIndex = findItemIndex(event.getX(), event.getY());
                int oldPressedIndex = pressedIndex;
                pressedIndex = -1;
                invalidate();
                if (upIndex >= 0 && upIndex == oldPressedIndex) {
                    performClick();
                    if (upIndex != selectedIndex) {
                        selectedIndex = upIndex;
                        performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
                        invalidate();
                        if (listener != null) {
                            listener.onItemSelected(upIndex);
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                pressedIndex = -1;
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private int findItemIndex(float x, float y) {
        int count = items.length;
        if (count == 0) {
            return -1;
        }
        final boolean rtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
        final float gap = AndroidUtilities.dp(2);
        final int rows = getRowCount(getWidth());
        final int columns = Math.max(1, (int) Math.ceil(count / (float) rows));
        final float segmentWidth = (getWidth() - getPaddingLeft() - getPaddingRight() - gap * (columns - 1)) / columns;
        final float segmentHeight = (getHeight() - getPaddingTop() - getPaddingBottom() - gap * (rows - 1)) / rows;
        float relativeX = x - getPaddingLeft();
        float relativeY = y - getPaddingTop();
        int visualColumn = (int) (relativeX / (segmentWidth + gap));
        int row = (int) (relativeY / (segmentHeight + gap));
        if (visualColumn < 0 || visualColumn >= columns || row < 0 || row >= rows) {
            return -1;
        }
        float segmentLeft = visualColumn * (segmentWidth + gap);
        float segmentTop = row * (segmentHeight + gap);
        if (relativeX < segmentLeft || relativeX > segmentLeft + segmentWidth || relativeY < segmentTop || relativeY > segmentTop + segmentHeight) {
            return -1;
        }
        int column = rtl ? columns - visualColumn - 1 : visualColumn;
        int index = row * columns + column;
        return index < count ? index : -1;
    }
}

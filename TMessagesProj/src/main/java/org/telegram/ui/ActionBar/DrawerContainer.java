package org.telegram.ui.ActionBar;

import static org.telegram.messenger.AndroidUtilities.lerp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;

public class DrawerContainer extends FrameLayout {
    private final Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int backgroundColor;
    private int navbarInset;

    public DrawerContainer(@NonNull Context context) {
        super(context);
        ViewCompat.setOnApplyWindowInsetsListener(this, this::onApplyWindowInsets);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);

        RecyclerView listView = null;
        LayoutParams lp;
        int topInset = 0;

        for (int a = 0, N = getChildCount(); a < N; a++) {
            final View child = getChildAt(a);
            if (child instanceof RecyclerView) {
                listView = (RecyclerView) child;
            } else {
                lp = (LayoutParams) child.getLayoutParams();
                lp.bottomMargin = 0;

                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                topInset += child.getMeasuredHeight();
            }
        }

        if (listView != null) {
            if (listView.getPaddingTop() != topInset || listView.getPaddingBottom() != navbarInset) {
                listView.setPadding(listView.getPaddingLeft(), topInset, listView.getPaddingRight(), navbarInset);
            }
            lp = (LayoutParams) listView.getLayoutParams();
            lp.bottomMargin = 0;

            measureChildWithMargins(listView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
    }

    private final android.graphics.Path clipPath = new android.graphics.Path();
    private final float[] radii = new float[8];

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        if (xyz.nextalone.nagram.ui.UIStyleEngine.isMaterial3Expressive()) {
            float rad = AndroidUtilities.dp(28);
            clipPath.rewind();
            radii[0] = 0;
            radii[1] = 0;
            radii[2] = rad;
            radii[3] = rad;
            radii[4] = rad;
            radii[5] = rad;
            radii[6] = 0;
            radii[7] = 0;
            AndroidUtilities.rectTmp.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
            clipPath.addRoundRect(AndroidUtilities.rectTmp, radii, android.graphics.Path.Direction.CW);
            canvas.save();
            canvas.clipPath(clipPath);
            super.dispatchDraw(canvas);
            canvas.restore();
        } else {
            super.dispatchDraw(canvas);
        }
        if (backgroundPaint.getAlpha() > 0) {
            canvas.drawRect(0, getMeasuredHeight() - navbarInset, getMeasuredWidth(), getMeasuredHeight(), backgroundPaint);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        backgroundColor = color;
        checkBackgroundColorPaint();
    }

    private void checkBackgroundColorPaint() {
        final float thirdButtonsFactor = AndroidUtilities.getNavigationBarThirdButtonsFactor(navbarInset);
        final int color = ColorUtils.compositeColors(0x20000000, backgroundColor);
        backgroundPaint.setColor(Theme.multAlpha(color, lerp(0.0f, 0.75f, thirdButtonsFactor)));
    }

    @NonNull
    private WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat insets) {
        final int navbarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;
        if (this.navbarInset != navbarInset) {
            this.navbarInset = navbarInset;
            checkBackgroundColorPaint();
            requestLayout();
        }

        return WindowInsetsCompat.CONSUMED;
    }
}

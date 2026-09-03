package org.telegram.ui.ActionBar;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.CircularProgressDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;

public class TextViewWithLoading extends TextView {

    private boolean loading = false;
    private final AnimatedFloat animatedLoading = new AnimatedFloat(this, 320, CubicBezierInterpolator.EASE_OUT_QUINT);
    private CircularProgressDrawable spinner;

    public TextViewWithLoading(Context context) {
        super(context);

        spinner = new CircularProgressDrawable();
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        spinner.setColor(color);
    }

    public void setLoading(boolean loading, boolean animated) {
        if (this.loading == loading) {
            return;
        }
        this.loading = loading;
        invalidate();
        if (!animated) {
            animatedLoading.force(loading);
        }
    }

    public boolean isLoading() {
        return loading;
    }

    private final AnimatedFloat pressedMorphProgress = new AnimatedFloat(this, 300, CubicBezierInterpolator.EASE_OUT_QUINT);
    private final android.graphics.Path buttonMorphPath = new android.graphics.Path();
    private final android.graphics.RectF buttonMorphRect = new android.graphics.RectF();

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        if (xyz.nextalone.nagram.ui.UIStyleEngine.isMaterial3Expressive()) {
            float progress = pressedMorphProgress.set(isPressed() ? 1f : 0f);
            float pillRad = getHeight() / 2f;
            float currentRad = org.telegram.messenger.AndroidUtilities.lerp(pillRad, dp(8f), progress);
            buttonMorphPath.rewind();
            buttonMorphRect.set(0, 0, getWidth(), getHeight());
            buttonMorphPath.addRoundRect(buttonMorphRect, currentRad, currentRad, android.graphics.Path.Direction.CW);
            canvas.save();
            canvas.clipPath(buttonMorphPath);
            super.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawInternal(canvas);
    }

    private void drawInternal(Canvas canvas) {
        final float loading = animatedLoading.set(this.loading);

        if (loading < 1) {
            if (loading <= 0) {
                canvas.save();
            } else {
                canvas.saveLayerAlpha(0, 0, getWidth(), getHeight(), (int) (0xFF * (1.0f - loading)), Canvas.ALL_SAVE_FLAG);
            }
            canvas.translate(0, dp(6) * loading);
            super.onDraw(canvas);
            canvas.restore();
        }

        if (loading > 0) {
            int cx = getWidth() / 2, cy = getHeight() / 2;
            cx -= (int) (dp(6) * (1.0f - loading));
            spinner.setAlpha((int) (0xFF * loading));
            spinner.setBounds(
                cx - spinner.getIntrinsicWidth() / 2, cy - spinner.getIntrinsicWidth() / 2,
                cx + spinner.getIntrinsicWidth() / 2, cy + spinner.getIntrinsicHeight() / 2
            );
            spinner.draw(canvas);
            invalidate();
        }
    }

}

package org.telegram.ui.Components.Premium.boosts;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;

import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;

@SuppressLint("ViewConstructor")
public class GradientButtonWithCounterView extends ButtonWithCounterView {

    private final RectF rect = new RectF();
    private boolean incGradient;
    private float progress;
    private final CellFlickerDrawable flickerDrawable;

    public GradientButtonWithCounterView(Context context, boolean filled, Theme.ResourcesProvider resourcesProvider) {
        super(context, filled, resourcesProvider);
        flickerDrawable = new CellFlickerDrawable();
        flickerDrawable.animationSpeedScale = 1.2f;
        flickerDrawable.drawFrame = false;
        flickerDrawable.repeatProgress = 4f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (incGradient) {
            progress += 16f / 1000f;
            if (progress > 3) {
                incGradient = false;
            }
        } else {
            progress -= 16f / 1000f;
            if (progress < 1) {
                incGradient = true;
            }
        }

        rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        PremiumGradient.getInstance().updateMainGradientMatrix(0, 0, getMeasuredWidth(), getMeasuredHeight(), -getMeasuredWidth() * 0.1f * progress, 0);
        float radius = xyz.nextalone.nagram.ui.UIStyleEngine.isMaterial3Expressive() ? getM3ExpressiveButtonRadius() : dp(8);
        canvas.drawRoundRect(rect, radius, radius, PremiumGradient.getInstance().getMainGradientPaint());
        flickerDrawable.setParentWidth(getMeasuredWidth());
        flickerDrawable.draw(canvas, rect, radius, null);
        super.onDraw(canvas);
        invalidate();
    }
}

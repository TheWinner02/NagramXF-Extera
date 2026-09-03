package org.telegram.ui.Components;

import static org.telegram.messenger.AndroidUtilities.dp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;

public class M3ExpressiveButtonDrawable extends Drawable {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private final int backgroundColor;
    private final int pressedOverlayColor;
    private final float restRadius;
    private final int inset;
    private int alpha = 255;
    private boolean pressed;
    private boolean externalProgress;
    private float progress;
    private ValueAnimator animator;
    private ColorFilter colorFilter;

    public M3ExpressiveButtonDrawable(int backgroundColor, int pressedOverlayColor) {
        this(backgroundColor, pressedOverlayColor, 0, 0);
    }

    public M3ExpressiveButtonDrawable(int backgroundColor, int pressedOverlayColor, float restRadius, int inset) {
        this.backgroundColor = backgroundColor;
        this.pressedOverlayColor = pressedOverlayColor;
        this.restRadius = restRadius;
        this.inset = inset;
    }

    public void setMorphProgress(float progress) {
        externalProgress = true;
        if (animator != null) {
            animator.removeAllListeners();
            animator.cancel();
            animator = null;
        }
        progress = Utilities.clamp(progress, 1f, 0f);
        if (Math.abs(this.progress - progress) < 0.001f) {
            return;
        }
        this.progress = progress;
        invalidateSelf();
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        boolean nextPressed = false;
        for (int state : stateSet) {
            if (state == android.R.attr.state_pressed) {
                nextPressed = true;
                break;
            }
        }
        if (pressed == nextPressed) {
            return false;
        }
        pressed = nextPressed;
        if (externalProgress) {
            return true;
        }
        animateTo(pressed ? 1f : 0f);
        return true;
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    private void animateTo(float target) {
        if (animator != null) {
            animator.removeAllListeners();
            animator.cancel();
            animator = null;
        }
        if (Math.abs(progress - target) < 0.001f) {
            progress = target;
            invalidateSelf();
            return;
        }
        animator = ValueAnimator.ofFloat(progress, target);
        animator.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            invalidateSelf();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animator = null;
            }
        });
        animator.setInterpolator(target == 0f ? new OvershootInterpolator(2.0f) : new DecelerateInterpolator(1.5f));
        animator.setDuration(target == 0f ? 350 : 90);
        animator.start();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        rect.set(getBounds());
        rect.inset(inset, inset);
        if (rect.isEmpty()) {
            return;
        }

        float safeProgress = Utilities.clamp(progress, 1f, 0f);
        float radius = AndroidUtilities.lerp(restRadius > 0 ? restRadius : rect.height() / 2f, dp(8), safeProgress);

        paint.setColor(applyAlpha(backgroundColor, alpha));
        paint.setColorFilter(colorFilter);
        if (Color.alpha(paint.getColor()) > 0) {
            canvas.drawRoundRect(rect, radius, radius, paint);
        }

        int pressedAlpha = (int) (Color.alpha(pressedOverlayColor) * (alpha / 255f) * safeProgress);
        if (pressedAlpha > 0) {
            paint.setColor((pressedOverlayColor & 0x00ffffff) | (pressedAlpha << 24));
            paint.setColorFilter(colorFilter);
            canvas.drawRoundRect(rect, radius, radius, paint);
        }
    }

    private static int applyAlpha(int color, int alpha) {
        return (color & 0x00ffffff) | ((int) (Color.alpha(color) * (alpha / 255f)) << 24);
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        this.colorFilter = colorFilter;
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

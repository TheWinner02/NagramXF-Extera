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
    private int backgroundColor;
    private int pressedOverlayColor;
    private float restRadius;
    private float pressedRadius;
    private int inset;
    private int alpha = 255;
    private boolean pressed;
    private boolean externalProgress;
    private float progress;
    private ValueAnimator animator;
    private ColorFilter colorFilter;

    private float[] restRadii;
    private float[] pressedRadii;
    private final android.graphics.Path path = new android.graphics.Path();
    private final float[] currentRadii = new float[8];

    public void setColors(int backgroundColor, int pressedOverlayColor) {
        this.backgroundColor = backgroundColor;
        this.pressedOverlayColor = pressedOverlayColor;
        invalidateSelf();
    }

    public void setRadii(float[] restRadii, float[] pressedRadii) {
        this.restRadii = restRadii;
        this.pressedRadii = pressedRadii;
        invalidateSelf();
    }

    public M3ExpressiveButtonDrawable(int backgroundColor, int pressedOverlayColor) {
        this(backgroundColor, pressedOverlayColor, 0, -1, 0);
    }

    public M3ExpressiveButtonDrawable(int backgroundColor, int pressedOverlayColor, float restRadius, int inset) {
        this(backgroundColor, pressedOverlayColor, restRadius, -1, inset);
    }

    public M3ExpressiveButtonDrawable(int backgroundColor, int pressedOverlayColor, float restRadius, float pressedRadius, int inset) {
        this.backgroundColor = backgroundColor;
        this.pressedOverlayColor = pressedOverlayColor;
        this.restRadius = restRadius;
        this.pressedRadius = pressedRadius;
        this.inset = inset;
    }

    public M3ExpressiveButtonDrawable(int backgroundColor, int pressedOverlayColor, float[] restRadii, float[] pressedRadii, int inset) {
        this.backgroundColor = backgroundColor;
        this.pressedOverlayColor = pressedOverlayColor;
        this.restRadius = 0;
        this.pressedRadius = -1;
        this.restRadii = restRadii;
        this.pressedRadii = pressedRadii;
        this.inset = inset;
    }

    public static M3ExpressiveButtonDrawable createConnectedLeft(int backgroundColor, int pressedOverlayColor) {
        float outer = dp(24);
        float inner = dp(8);
        float pressedInner = dp(14);
        return new M3ExpressiveButtonDrawable(
            backgroundColor, pressedOverlayColor,
            new float[]{outer, outer, inner, inner, inner, inner, outer, outer},
            new float[]{pressedInner, pressedInner, pressedInner, pressedInner, pressedInner, pressedInner, pressedInner, pressedInner},
            0
        );
    }

    public static M3ExpressiveButtonDrawable createConnectedMiddle(int backgroundColor, int pressedOverlayColor) {
        float inner = dp(8);
        float pressedInner = dp(14);
        return new M3ExpressiveButtonDrawable(
            backgroundColor, pressedOverlayColor,
            new float[]{inner, inner, inner, inner, inner, inner, inner, inner},
            new float[]{pressedInner, pressedInner, pressedInner, pressedInner, pressedInner, pressedInner, pressedInner, pressedInner},
            0
        );
    }

    public static M3ExpressiveButtonDrawable createConnectedRight(int backgroundColor, int pressedOverlayColor) {
        float outer = dp(24);
        float inner = dp(8);
        float pressedInner = dp(14);
        return new M3ExpressiveButtonDrawable(
            backgroundColor, pressedOverlayColor,
            new float[]{inner, inner, outer, outer, outer, outer, inner, inner},
            new float[]{pressedInner, pressedInner, pressedInner, pressedInner, pressedInner, pressedInner, pressedInner, pressedInner},
            0
        );
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
        if (restRadii != null) {
            path.reset();
            for (int i = 0; i < 8; i++) {
                float from = restRadii[i];
                float to = pressedRadii != null ? pressedRadii[i] : dp(14);
                currentRadii[i] = AndroidUtilities.lerp(from, to, safeProgress);
            }
            path.addRoundRect(rect, currentRadii, android.graphics.Path.Direction.CW);
            paint.setColor(applyAlpha(backgroundColor, alpha));
            paint.setColorFilter(colorFilter);
            if (Color.alpha(paint.getColor()) > 0) {
                canvas.drawPath(path, paint);
            }

            int pressedAlpha = (int) (Color.alpha(pressedOverlayColor) * (alpha / 255f) * safeProgress);
            if (pressedAlpha > 0) {
                paint.setColor((pressedOverlayColor & 0x00ffffff) | (pressedAlpha << 24));
                paint.setColorFilter(colorFilter);
                canvas.drawPath(path, paint);
            }
            return;
        }

        float fromRadius = restRadius > 0 ? restRadius : rect.height() / 2f;
        float toRadius;
        if (pressedRadius >= 0) {
            toRadius = pressedRadius;
        } else if (fromRadius < rect.height() / 2f - dp(2)) {
            toRadius = rect.height() / 2f;
        } else {
            toRadius = dp(8);
        }
        float radius = AndroidUtilities.lerp(fromRadius, toRadius, safeProgress);

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

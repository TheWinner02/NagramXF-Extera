package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

public class M3PressMorphHelper {
    private final View view;
    private boolean pressed;
    private float progress;
    private ValueAnimator animator;
    private Runnable onProgressUpdate;
    private float overshoot = 2.5f;

    public M3PressMorphHelper(View view) {
        this(view, 2.5f);
    }

    public M3PressMorphHelper(View view, float overshoot) {
        this.view = view;
        this.overshoot = overshoot;
    }

    public M3PressMorphHelper setOnProgressUpdate(Runnable onProgressUpdate) {
        this.onProgressUpdate = onProgressUpdate;
        return this;
    }

    public void setPressed(boolean pressed) {
        if (this.pressed == pressed) {
            return;
        }
        this.pressed = pressed;
        if (animator != null) {
            animator.removeAllListeners();
            animator.cancel();
            animator = null;
        }
        animator = ValueAnimator.ofFloat(progress, pressed ? 1f : 0f);
        animator.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (animator == animation) {
                    animator = null;
                    progress = pressed ? 1f : 0f;
                    invalidate();
                }
            }
        });
        if (pressed) {
            animator.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animator.setDuration(70);
        } else {
            animator.setInterpolator(new OvershootInterpolator(overshoot));
            animator.setDuration(360);
        }
        animator.start();
    }

    public float getProgress() {
        return progress;
    }

    public boolean isPressed() {
        return pressed;
    }

    private void invalidate() {
        if (view != null) {
            view.invalidate();
        }
        if (onProgressUpdate != null) {
            onProgressUpdate.run();
        }
    }
}

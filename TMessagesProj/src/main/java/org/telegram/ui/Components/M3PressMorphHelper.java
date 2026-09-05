package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.Utilities;

public class M3PressMorphHelper {
    private final View view;
    private boolean pressed;
    private float progress;
    private ValueAnimator animator;

    private Runnable onProgressUpdate;

    public M3PressMorphHelper(View view) {
        this.view = view;
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
        invalidate();
        if (pressed) {
            if (animator != null) {
                animator.removeAllListeners();
                animator.cancel();
                animator = null;
            }
        } else if (progress != 0f) {
            animator = ValueAnimator.ofFloat(progress, 0f);
            animator.addUpdateListener(animation -> {
                progress = (float) animation.getAnimatedValue();
                invalidate();
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animator = null;
                }
            });
            animator.setInterpolator(new OvershootInterpolator(2.0f));
            animator.setDuration(350);
            animator.start();
        }
    }

    public float getProgress() {
        if (pressed && progress != 1f) {
            progress += (float) Math.min(40, 1000f / AndroidUtilities.screenRefreshRate) / 100f;
            progress = Utilities.clamp(progress, 1f, 0f);
            invalidate();
        }
        return progress;
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

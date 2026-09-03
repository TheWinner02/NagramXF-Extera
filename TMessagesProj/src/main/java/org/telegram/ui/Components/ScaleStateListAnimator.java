package org.telegram.ui.Components;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

public class ScaleStateListAnimator {

    public static void apply(View view) {
        apply(view, .1f, 1.5f);
    }

    public static void apply(View view, float scale, float tension) {
        if (view == null) {
            return;
        }

        if (xyz.nextalone.nagram.ui.UIStyleEngine.isMaterial3Expressive()) {
            scale = 0.04f;
            tension = 2.0f;
        }

        AnimatorSet pressedAnimator = new AnimatorSet();
        pressedAnimator.playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1f - scale),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f - scale)
        );
        pressedAnimator.setDuration(80);

        AnimatorSet defaultAnimator = new AnimatorSet();
        defaultAnimator.playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f)
        );
        if (xyz.nextalone.nagram.ui.UIStyleEngine.isMaterial3Expressive()) {
            defaultAnimator.setInterpolator(new OvershootInterpolator(tension));
            defaultAnimator.setDuration(350);
        } else {
            defaultAnimator.setInterpolator(new OvershootInterpolator(tension));
            defaultAnimator.setDuration(350);
        }

        StateListAnimator scaleStateListAnimator = new StateListAnimator();

        scaleStateListAnimator.addState(new int[]{android.R.attr.state_pressed}, pressedAnimator);
        scaleStateListAnimator.addState(new int[0], defaultAnimator);

        view.setStateListAnimator(scaleStateListAnimator);
    }

    public static void reset(View view) {
        view.setStateListAnimator(null);
    }

}

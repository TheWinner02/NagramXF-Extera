package com.exteragram.messenger.plugins.ui.components.templates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.Utilities;

/* JADX INFO: loaded from: classes4.dex */
public final class UniversalFrameLayout extends FrameLayout {
    private UniversalFrameLayoutListener universalFrameLayoutListener;

    @JvmOverloads
    public UniversalFrameLayout(Context context) {
        this(context, null);
    }

    @JvmOverloads
    public UniversalFrameLayout(Context context, UniversalFrameLayoutListener universalFrameLayoutListener) {
        super(context);
        this.universalFrameLayoutListener = universalFrameLayoutListener;
    }

    public /* synthetic */ UniversalFrameLayout(Context context, UniversalFrameLayoutListener universalFrameLayoutListener, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : universalFrameLayoutListener);
    }

    public final UniversalFrameLayoutListener getUniversalFrameLayoutListener() {
        return this.universalFrameLayoutListener;
    }

    public final void setUniversalFrameLayoutListener(UniversalFrameLayoutListener universalFrameLayoutListener) {
        this.universalFrameLayoutListener = universalFrameLayoutListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperOnLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperSetTranslationX(float translationX) {
        super.setTranslationX(translationX);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperSetTranslationY(float translationY) {
        super.setTranslationY(translationY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperOnDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperOnAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperDispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperRequestLayout() {
        super.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperInvalidate() {
        super.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperOnDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperOnInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean callSuperOnInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean callSuperOnTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean callSuperDrawChild(Canvas canvas, View child, long drawingTime) {
        return super.drawChild(canvas, child, drawingTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperSetVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.onLayout(changed, left, top, right, bottom, new Utilities.Callback5() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda9
                @Override // org.telegram.messenger.Utilities.Callback5
                public final void run(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                    callSuperOnLayout(((Boolean) obj).booleanValue(), ((Integer) obj2).intValue(), ((Integer) obj3).intValue(), ((Integer) obj4).intValue(), ((Integer) obj5).intValue());
                }
            });
        } else {
            super.onLayout(changed, left, top, right, bottom);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.onMeasure(widthMeasureSpec, heightMeasureSpec, new Utilities.Callback2() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda12
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    callSuperOnMeasure(((Integer) obj).intValue(), ((Integer) obj2).intValue());
                }
            });
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override // android.view.View
    public void setTranslationX(float translationX) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.setTranslationX(translationX, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda7
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    callSuperSetTranslationX(((Float) obj).floatValue());
                }
            });
        } else {
            super.setTranslationX(translationX);
        }
    }

    @Override // android.view.View
    public void setTranslationY(float translationY) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.setTranslationY(translationY, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda0
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    callSuperSetTranslationY(((Float) obj).floatValue());
                }
            });
        } else {
            super.setTranslationY(translationY);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.onAttachedToWindow(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    callSuperOnAttachedToWindow();
                }
            });
        } else {
            super.onAttachedToWindow();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.onDetachedFromWindow(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    callSuperOnDetachedFromWindow();
                }
            });
        } else {
            super.onDetachedFromWindow();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.dispatchDraw(canvas, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda6
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    callSuperDispatchDraw((Canvas) obj);
                }
            });
        } else {
            super.dispatchDraw(canvas);
        }
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.requestLayout(new Runnable() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    callSuperRequestLayout();
                }
            });
        } else {
            super.requestLayout();
        }
    }

    @Override // android.view.View
    public void invalidate() {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.invalidate(UniversalFrameLayout.super::invalidate);
        } else {
            super.invalidate();
        }
    }

    @Override // android.view.View
    @Deprecated
    public void invalidate(int l, int t, int r, int b2) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.invalidate(l, t, r, b2, UniversalFrameLayout.super::invalidate);
        } else {
            super.invalidate(l, t, r, b2);
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.onDraw(canvas, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda8
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    callSuperOnDraw((Canvas) obj);
                }
            });
        } else {
            super.onDraw(canvas);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.onInitializeAccessibilityNodeInfo(info, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda10
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    callSuperOnInitializeAccessibilityNodeInfo((AccessibilityNodeInfo) obj);
                }
            });
        } else {
            super.onInitializeAccessibilityNodeInfo(info);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            return universalFrameLayoutListener.onInterceptTouchEvent(ev, new Utilities.CallbackReturn() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda4
                @Override // org.telegram.messenger.Utilities.CallbackReturn
                public final Object run(Object obj) {
                    return Boolean.valueOf(callSuperOnInterceptTouchEvent((MotionEvent) obj));
                }
            });
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        return universalFrameLayoutListener != null ? universalFrameLayoutListener.onTouchEvent(event, new Utilities.CallbackReturn() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda2
            @Override // org.telegram.messenger.Utilities.CallbackReturn
            public final Object run(Object obj) {
                return Boolean.valueOf(callSuperOnTouchEvent((MotionEvent) obj));
            }
        }) : super.onTouchEvent(event);
    }

    @Override // android.view.ViewGroup
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            return universalFrameLayoutListener.drawChild(canvas, child, drawingTime, new Utilities.Callback3Return() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda1
                @Override // org.telegram.messenger.Utilities.Callback3Return
                public final Object run(Object obj, Object obj2, Object obj3) {
                    return Boolean.valueOf(callSuperDrawChild((Canvas) obj, (View) obj2, ((Long) obj3).longValue()));
                }
            });
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        UniversalFrameLayoutListener universalFrameLayoutListener = this.universalFrameLayoutListener;
        if (universalFrameLayoutListener != null) {
            universalFrameLayoutListener.setVisibility(visibility, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalFrameLayout$$ExternalSyntheticLambda14
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    callSuperSetVisibility(((Integer) obj).intValue());
                }
            });
        } else {
            super.setVisibility(visibility);
        }
    }

    public interface UniversalFrameLayoutListener {

        default void onLayout(boolean changed, int left, int top, int right, int bottom, Utilities.Callback5<Boolean, Integer, Integer, Integer, Integer> originalMethod) {
            originalMethod.run(Boolean.valueOf(changed), Integer.valueOf(left), Integer.valueOf(top), Integer.valueOf(right), Integer.valueOf(bottom));
        }

        default void onMeasure(int widthMeasureSpec, int heightMeasureSpec, Utilities.Callback2<Integer, Integer> originalMethod) {
            originalMethod.run(Integer.valueOf(widthMeasureSpec), Integer.valueOf(heightMeasureSpec));
        }

        default void setTranslationX(float translationX, Utilities.Callback<Float> originalMethod) {
            originalMethod.run(Float.valueOf(translationX));
        }

        default void setTranslationY(float translationY, Utilities.Callback<Float> originalMethod) {
            originalMethod.run(Float.valueOf(translationY));
        }

        default void onAttachedToWindow(Runnable originalMethod) {
            originalMethod.run();
        }

        default void onDetachedFromWindow(Runnable originalMethod) {
            originalMethod.run();
        }

        default void dispatchDraw(Canvas canvas, Utilities.Callback<Canvas> originalMethod) {
            originalMethod.run(canvas);
        }

        default void requestLayout(Runnable originalMethod) {
            originalMethod.run();
        }

        default void invalidate(Runnable originalMethod) {
            originalMethod.run();
        }

        default void invalidate(int l, int t, int r, int b2, Runnable originalMethod) {
            originalMethod.run();
        }

        default void onDraw(Canvas canvas, Utilities.Callback<Canvas> originalMethod) {
            originalMethod.run(canvas);
        }

        default void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info, Utilities.Callback<AccessibilityNodeInfo> originalMethod) {
            originalMethod.run(info);
        }

        default boolean onInterceptTouchEvent(MotionEvent ev, Utilities.CallbackReturn<MotionEvent, Boolean> originalMethod) {
            return originalMethod.run(ev).booleanValue();
        }

        default boolean onTouchEvent(MotionEvent event, Utilities.CallbackReturn<MotionEvent, Boolean> originalMethod) {
            return originalMethod.run(event).booleanValue();
        }

        default boolean drawChild(Canvas canvas, View child, long drawingTime, Utilities.Callback3Return<Canvas, View, Long, Boolean> originalMethod) {
            return originalMethod.run(canvas, child, Long.valueOf(drawingTime)).booleanValue();
        }

        default void setVisibility(int visibility, Utilities.Callback<Integer> originalMethod) {
            originalMethod.run(Integer.valueOf(visibility));
        }
    }
}

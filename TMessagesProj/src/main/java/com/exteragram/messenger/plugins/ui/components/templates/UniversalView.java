package com.exteragram.messenger.plugins.ui.components.templates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.internal.url._UrlKt;
import org.telegram.messenger.Utilities;

/* JADX INFO: loaded from: classes4.dex */
public final class UniversalView extends View {
    private UniversalViewDelegate delegate;

    @JvmOverloads
    public UniversalView(Context context) {
        this(context, null);
    }

    @JvmOverloads
    public UniversalView(Context context, UniversalViewDelegate universalViewDelegate) {
        super(context);
        this.delegate = universalViewDelegate;
    }

    public /* synthetic */ UniversalView(Context context, UniversalViewDelegate universalViewDelegate, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : universalViewDelegate);
    }

    public final UniversalViewDelegate getDelegate() {
        return this.delegate;
    }

    public final void setDelegate(UniversalViewDelegate universalViewDelegate) {
        this.delegate = universalViewDelegate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperOnDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean callSuperOnTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void callSuperOnInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        UniversalViewDelegate universalViewDelegate = this.delegate;
        if (universalViewDelegate != null) {
            universalViewDelegate.onDraw(canvas, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalView$$ExternalSyntheticLambda0
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
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        UniversalViewDelegate universalViewDelegate = this.delegate;
        if (universalViewDelegate != null) {
            universalViewDelegate.onAttachedToWindow();
        }
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent event) {
        UniversalViewDelegate universalViewDelegate = this.delegate;
        return universalViewDelegate != null ? universalViewDelegate.onTouchEvent(event, new Utilities.CallbackReturn() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalView$$ExternalSyntheticLambda3
            @Override // org.telegram.messenger.Utilities.CallbackReturn
            public final Object run(Object obj) {
                return Boolean.valueOf(callSuperOnTouchEvent((MotionEvent) obj));
            }
        }) : super.onTouchEvent(event);
    }

    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        UniversalViewDelegate universalViewDelegate = this.delegate;
        if (universalViewDelegate != null) {
            universalViewDelegate.onMeasure(widthMeasureSpec, heightMeasureSpec, new Utilities.Callback2() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalView$$ExternalSyntheticLambda1
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
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        UniversalViewDelegate universalViewDelegate = this.delegate;
        if (universalViewDelegate != null) {
            universalViewDelegate.onDetachedFromWindow();
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        UniversalViewDelegate universalViewDelegate = this.delegate;
        if (universalViewDelegate != null) {
            universalViewDelegate.onInitializeAccessibilityNodeInfo(info, new Utilities.Callback() { // from class: com.exteragram.messenger.plugins.ui.components.templates.UniversalView$$ExternalSyntheticLambda2
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    callSuperOnInitializeAccessibilityNodeInfo((AccessibilityNodeInfo) obj);
                }
            });
        } else {
            super.onInitializeAccessibilityNodeInfo(info);
        }
    }

    public interface UniversalViewDelegate {
        default void onAttachedToWindow() {
        }

        default void onDetachedFromWindow() {
        }

        default void onDraw(Canvas canvas, Utilities.Callback<Canvas> originalMethod) {
            originalMethod.run(canvas);
        }

        default boolean onTouchEvent(MotionEvent event, Utilities.CallbackReturn<MotionEvent, Boolean> originalMethod) {
            return originalMethod.run(event).booleanValue();
        }

        default void onMeasure(int widthMeasureSpec, int heightMeasureSpec, Utilities.Callback2<Integer, Integer> originalMethod) {
            originalMethod.run(Integer.valueOf(widthMeasureSpec), Integer.valueOf(heightMeasureSpec));
        }

        default void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info, Utilities.Callback<AccessibilityNodeInfo> originalMethod) {
            originalMethod.run(info);
        }
    }
}

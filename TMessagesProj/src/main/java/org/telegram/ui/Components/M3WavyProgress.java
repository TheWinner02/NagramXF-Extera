package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import org.telegram.messenger.AndroidUtilities;

final class M3WavyProgress {

    private static final Path path = new Path();

    private M3WavyProgress() {
    }

    static void drawLinear(Canvas canvas, RectF rect, Paint paint, float phasePx, float amplitudePx, float wavelengthPx) {
        if (rect.width() <= 0 || rect.height() <= 0) {
            return;
        }
        float strokeWidth = Math.max(1f, rect.height());
        float amplitude = Math.min(amplitudePx, strokeWidth * 0.42f);
        float wavelength = Math.max(AndroidUtilities.dp(12), wavelengthPx);
        if (amplitude < 0.5f || rect.width() < AndroidUtilities.dp(10)) {
            canvas.drawRoundRect(rect, strokeWidth / 2f, strokeWidth / 2f, paint);
            return;
        }

        path.reset();
        float centerY = rect.centerY();
        int steps = Math.max(6, (int) (rect.width() / AndroidUtilities.dp(3)));
        for (int i = 0; i <= steps; i++) {
            float t = i / (float) steps;
            float x = rect.left + rect.width() * t;
            float y = centerY + (float) Math.sin((x + phasePx) / wavelength * Math.PI * 2f) * amplitude;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        Paint.Style oldStyle = paint.getStyle();
        Paint.Cap oldCap = paint.getStrokeCap();
        float oldStrokeWidth = paint.getStrokeWidth();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawPath(path, paint);
        paint.setStyle(oldStyle);
        paint.setStrokeCap(oldCap);
        paint.setStrokeWidth(oldStrokeWidth);
    }

    static void drawCircular(Canvas canvas, RectF rect, Paint paint, float startAngle, float sweepAngle, float strokeWidthPx, float amplitudePx, float phaseDegrees) {
        if (rect.width() <= 0 || rect.height() <= 0 || Math.abs(sweepAngle) <= 0.1f) {
            return;
        }
        float strokeWidth = Math.max(1f, strokeWidthPx);
        float amplitude = Math.min(amplitudePx, strokeWidth * 0.38f);
        float radius = Math.min(rect.width(), rect.height()) / 2f - strokeWidth / 2f - amplitude;
        if (amplitude < 0.5f || radius <= 0) {
            Paint.Style oldStyle = paint.getStyle();
            Paint.Cap oldCap = paint.getStrokeCap();
            float oldStrokeWidth = paint.getStrokeWidth();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(strokeWidth);
            canvas.drawArc(rect, startAngle, sweepAngle, false, paint);
            paint.setStyle(oldStyle);
            paint.setStrokeCap(oldCap);
            paint.setStrokeWidth(oldStrokeWidth);
            return;
        }

        path.reset();
        float cx = rect.centerX();
        float cy = rect.centerY();
        int steps = Math.max(12, (int) (Math.abs(sweepAngle) / 4f));
        for (int i = 0; i <= steps; i++) {
            float t = i / (float) steps;
            float angle = startAngle + sweepAngle * t;
            float wave = (float) Math.sin(Math.toRadians(angle * 7f + phaseDegrees)) * amplitude;
            float r = radius + wave;
            double angleRad = Math.toRadians(angle);
            float x = cx + (float) Math.cos(angleRad) * r;
            float y = cy + (float) Math.sin(angleRad) * r;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        Paint.Style oldStyle = paint.getStyle();
        Paint.Cap oldCap = paint.getStrokeCap();
        float oldStrokeWidth = paint.getStrokeWidth();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawPath(path, paint);
        paint.setStyle(oldStyle);
        paint.setStrokeCap(oldCap);
        paint.setStrokeWidth(oldStrokeWidth);
    }
}

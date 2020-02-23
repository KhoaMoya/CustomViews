package com.khoa.selectableindicatorview;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Board {
    public float left;
    public float top;
    public float right;
    public float bottom;

    public float cornersRadius;
    public int strokeColor;
    public float strokeWidth;

    public int backgroundColor;

    public Paint fillPaint;
    public Paint strokePaint;

    public Board(float cornersRadius, int strokeColor, float strokeWidth, int backgroundColor) {
        this.cornersRadius = cornersRadius;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.backgroundColor = backgroundColor;

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(backgroundColor);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);
        strokePaint.setColor(strokeColor);
    }

    public void draw(Canvas canvas){
        canvas.drawRoundRect(left, top, right, bottom, cornersRadius, cornersRadius, fillPaint);
        canvas.drawRoundRect(left, top, right, bottom, cornersRadius, cornersRadius, strokePaint);
    }
}

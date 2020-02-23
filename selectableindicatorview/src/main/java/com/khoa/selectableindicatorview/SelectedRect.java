package com.khoa.selectableindicatorview;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SelectedRect {

    public float left;
    public float top;
    public float right;
    public float bottom;

    public float startX;

    public int selectedBackgroundColor;

    public float cornersRadius;

    public Paint rectPaint;

    public SelectedRect(float startX, int selectedBackgroundColor, float cornersRadius) {
        this.startX = startX;
        this.selectedBackgroundColor = selectedBackgroundColor;
        this.cornersRadius = cornersRadius;

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setColor(selectedBackgroundColor);
    }

    public void draw(Canvas canvas){
        canvas.drawRoundRect(left, top , right, bottom, cornersRadius, cornersRadius, rectPaint);
    }
}

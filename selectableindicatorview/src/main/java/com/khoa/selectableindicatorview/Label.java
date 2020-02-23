package com.khoa.selectableindicatorview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextPaint;

public class Label {

    public int width;
    public int height;

    public float x;
    public float y;
    public int alpha=255;

    public String name;
    public float textSize;
    public int currentColor;

    public float paddingHorizontal;
    public float paddingVertical;

    public TextPaint labelPaint;

    public Label(String name, float textSize, int currentColor, float paddingHorizontal, float paddingVertical) {
        this.name = name;
        this.textSize = textSize;
        this.currentColor = currentColor;
        this.paddingHorizontal = paddingHorizontal;
        this.paddingVertical = paddingVertical;

        labelPaint = new TextPaint();
        labelPaint.setTextSize(textSize);
        labelPaint.setAntiAlias(true);
        labelPaint.setColor(currentColor);
        labelPaint.setAlpha(alpha);

        computeSize();
    }

    public void draw(Canvas canvas){
        canvas.drawText(name, x, y, labelPaint);
    }

    private void computeSize(){
        Rect rect = new Rect();
        labelPaint.getTextBounds(name, 0, name.length(), rect);
        width = rect.width();
        height = rect.height();
    }

    public void setColor(int color){
        labelPaint.setColor(color);
    }

    public void setAlpha(int alpha){
        labelPaint.setAlpha(alpha);
    }
}

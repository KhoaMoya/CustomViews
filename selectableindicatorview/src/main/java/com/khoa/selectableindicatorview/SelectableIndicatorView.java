package com.khoa.selectableindicatorview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class SelectableIndicatorView extends View implements View.OnTouchListener {

    private final int DEFAULT_ANIMATION_DURATION = 200;
    private final int DEFAULT_STROKE_COLOR = Color.BLUE;
    private final float DEFAUL_STROKE_WIDTH = Util.convertDpToPixel(0.005f, getContext());
    private final int DEFAULT_BACKGROUND_COLOR = Color.WHITE;
    private final float DEFAULT_CORNERS_RADIUS = Util.convertDpToPixel(0f, getContext());
    private final float DEFAULT_PADDING_HORIZONTAL = Util.convertDpToPixel(0.07f, getContext());
    private final float DEFAULT_PADDING_VERTICAL = Util.convertDpToPixel(0.05f, getContext());
    private final float DEFAULT_TEXT_SIZE = Util.convertDpToPixel(0.09f, getContext());
    private final boolean DEFAUL_DRAW_SAPARATES = false;

    private int animationDuration = DEFAULT_ANIMATION_DURATION;
    private int strokeColor = DEFAULT_STROKE_COLOR;
    private float strokeWidth = DEFAUL_STROKE_WIDTH;
    private int backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private float cornersRadius = DEFAULT_CORNERS_RADIUS;
    private float paddingHorizontal = DEFAULT_PADDING_HORIZONTAL;
    private float paddingVertical = DEFAULT_PADDING_VERTICAL;
    private float textSize = DEFAULT_TEXT_SIZE;

    private int seletedPosition = 0;
    private int prevPosition = -1;
    private int maxWidth;
    private int maxPosition;
    private boolean drawSeparates = DEFAUL_DRAW_SAPARATES;

    private Label[] labels;
    private Board board;
    private SelectedRect selectedRect;
    private String[] names;
    private Separate[] separates;
    private float labelWidth;

    private OnSelectIndicator selectIndicator;

    private ValueAnimator alphaAnimator;
    private ValueAnimator moveAnimator;
    private AnimatorSet animatorSet;

    public SelectableIndicatorView(Context context) {
        super(context);
        init();
    }

    public SelectableIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectableIndicatorView);
        animationDuration = typedArray.getInteger(R.styleable.SelectableIndicatorView_animation_duration, DEFAULT_ANIMATION_DURATION);
        strokeColor = typedArray.getColor(R.styleable.SelectableIndicatorView_stroke_color, DEFAULT_STROKE_COLOR);
        strokeWidth = typedArray.getDimension(R.styleable.SelectableIndicatorView_stroke_width, DEFAUL_STROKE_WIDTH);
        backgroundColor = typedArray.getColor(R.styleable.SelectableIndicatorView_tbn_background_color, DEFAULT_BACKGROUND_COLOR);
        cornersRadius = typedArray.getDimension(R.styleable.SelectableIndicatorView_corners_radius, DEFAULT_CORNERS_RADIUS);
        paddingHorizontal = typedArray.getDimension(R.styleable.SelectableIndicatorView_tbn_padding_horizotal, DEFAULT_PADDING_HORIZONTAL);
        paddingVertical = typedArray.getDimension(R.styleable.SelectableIndicatorView_tbn_padding_vertical, DEFAULT_PADDING_VERTICAL);
        textSize = typedArray.getDimension(R.styleable.SelectableIndicatorView_tbn_text_size, DEFAULT_TEXT_SIZE);
        drawSeparates = typedArray.getBoolean(R.styleable.SelectableIndicatorView_tbn_draw_saparates, DEFAUL_DRAW_SAPARATES);

        typedArray.recycle();

        init();
    }

    public SelectableIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SelectableIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float width;
        float desiredWidth = labelWidth * labels.length;
        if (widthMode == MeasureSpec.EXACTLY) {  // set height
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) { // wrapcontent
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        float height;
        float desiredHeight = labels[0].height + 2 * paddingVertical;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension((int) width, (int) height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (labelWidth < getWidth()) {
            labelWidth = getWidth() / labels.length;
        }
    }

    private void init() {
        board = new Board(cornersRadius, strokeColor, strokeWidth, backgroundColor);
        selectedRect = new SelectedRect(0, strokeColor, cornersRadius);

        animatorSet = new AnimatorSet();
        animatorSet.setDuration(animationDuration);
        animatorSet.setInterpolator(new DecelerateInterpolator());

        alphaAnimator = ValueAnimator.ofInt(0, 255);

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        if (drawSeparates) drawSeparates(canvas);
        drawSelectedRect(canvas);
        drawLabels(canvas);
    }

    private void drawLabels(Canvas canvas) {
        float totalHeight = 0;
        for (Label label : labels) totalHeight += label.height;
        float y = (float) getHeight() / 2 + totalHeight / labels.length / 3;

        for (int i = 0; i < labels.length; i++) {
            labels[i].x = (labelWidth * i + labelWidth / 2 - (float) labels[i].width / 2);
            labels[i].y = y;

            labels[i].draw(canvas);
        }
    }

    private void drawSelectedRect(Canvas canvas) {
        selectedRect.left = selectedRect.startX + 1;
        selectedRect.right = selectedRect.left + labelWidth - 1;
        selectedRect.top = 1;
        selectedRect.bottom = getHeight() - 2;
        selectedRect.draw(canvas);
    }

    private void drawSeparates(Canvas canvas) {
        for (int i = 0; i < separates.length; i++) {
            separates[i].startX = separates[i].stopX = labelWidth * (i + 1);
            separates[i].startY = 0;
            separates[i].stopY = getHeight();

            separates[i].draw(canvas);
        }
    }

    private void drawBoard(Canvas canvas) {
        board.left = 1;
        board.top = 1;
        board.right = getWidth() - 2;
        board.bottom = getHeight() - 2;
        board.draw(canvas);
    }

    public void setNames(String[] names) {
        this.names = names;
        this.labels = new Label[names.length];

        for (int i = 0; i < names.length; i++) {
            labels[i] = new Label(names[i], textSize, strokeColor, paddingHorizontal, paddingVertical);
            if (i == seletedPosition) {
                labels[i].setColor(backgroundColor);
            }
        }

        maxWidth = 0;
        maxPosition = 0;
        for (int i = 0; i < labels.length; i++) {
            if (labels[i].width > maxWidth) {
                maxWidth = labels[i].width;
                maxPosition = i;
            }
        }
        labelWidth = maxWidth + 2 * paddingHorizontal;

        separates = new Separate[names.length - 1];
        for (int i = 0; i < separates.length; i++)
            separates[i] = new Separate(cornersRadius, strokeColor, strokeWidth);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float x = motionEvent.getX();
        float y = motionEvent.getY();

        int newPosition = computeSelectedPosition(x, y);
        if (newPosition >= 0 && newPosition < labels.length && seletedPosition != newPosition) {
            selectedPositionChange(seletedPosition, newPosition);
        }

//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                Log.e("Loi", x + ":" + y);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.e("Loi", x + ":" + y);
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e("Loi", x + ":" + y);
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//
//            default:
//                break;
//        }
        return true;
    }

    private int computeSelectedPosition(float x, float y) {
        if (y >= 0 && y <= getHeight() && x >= 0 && x <= getWidth()) {
            return (int) (x / labelWidth);
        }
        return -1;
    }

    private void selectedPositionChange(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) return;

        seletedPosition = toPosition;
        prevPosition = fromPosition;

        if(selectIndicator!=null) selectIndicator.onChangeIndicator(seletedPosition);

        labels[seletedPosition].setColor(backgroundColor);
        labels[prevPosition].setColor(strokeColor);

        moveAnimator = ValueAnimator.ofFloat(prevPosition * labelWidth, seletedPosition * labelWidth);
        moveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                moveSelectedRect((Float) valueAnimator.getAnimatedValue());
            }
        });

        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                changeAlpha((Integer) valueAnimator.getAnimatedValue());
            }
        });

        animatorSet.play(moveAnimator);
//                    .with(alphaAnimator);
        animatorSet.start();
    }

    private void changeAlpha(int newAlpha) {
        labels[seletedPosition].setAlpha(newAlpha);
        labels[prevPosition].setAlpha(newAlpha);

        invalidate();
    }

    private void moveSelectedRect(float newStartX) {
        selectedRect.startX = newStartX;
        invalidate();
    }

    public OnSelectIndicator getSelectIndicator() {
        return selectIndicator;
    }

    public void setSelectIndicator(OnSelectIndicator selectIndicator) {
        this.selectIndicator = selectIndicator;
    }

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        this.animationDuration = animationDuration;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getPaddingHorizontal() {
        return paddingHorizontal;
    }

    public void setPaddingHorizontal(float paddingHorizontal) {
        this.paddingHorizontal = paddingHorizontal;
    }

    public float getPaddingVertical() {
        return paddingVertical;
    }

    public void setPaddingVertical(float paddingVertical) {
        this.paddingVertical = paddingVertical;
    }

    public int getSeletedPosition() {
        return seletedPosition;
    }

    public void setSeletedPosition(int seletedPosition) {
        selectedPositionChange(this.seletedPosition, seletedPosition);
    }

    public String[] getNames() {
        return names;
    }

    public boolean isDrawSeparates() {
        return drawSeparates;
    }

    public void setDrawSeparates(boolean drawSeparates) {
        this.drawSeparates = drawSeparates;
    }
}

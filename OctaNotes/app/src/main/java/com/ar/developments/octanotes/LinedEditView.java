package com.ar.developments.octanotes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;


public class LinedEditView extends EditText {
    private Paint mPaint = new Paint();
    private Typeface font;

    public LinedEditView(Context context) {
        super(context);
        initPaint();
    }

    public LinedEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        font = Typeface.createFromAsset(context.getAssets(), "ShortStack-Regular.otf");
        initPaint();
    }

    public LinedEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    private void initPaint() {
//        mPaint.setColor(R.color.orange);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setARGB(72,17,138,184);
        mPaint.setTypeface(font);
    }

    @Override protected void onDraw(Canvas canvas) {
        int left = getLeft();
        int right = getRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int height = getHeight();
        int lineHeight = getLineHeight();
        int count = (height-paddingTop-paddingBottom) / lineHeight;


        for (int i = 0; i < count; i++) {
            int baseline = lineHeight * (i+1) + paddingTop-30;
            Log.d("canvas","count "+count+" baseline"+baseline);
            canvas.drawLine(left+paddingLeft, baseline, right-paddingRight, baseline, mPaint);
        }

        super.onDraw(canvas);
    }
}
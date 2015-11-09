package com.specenergocontrol.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Комп on 23.07.2015.
 */
public class ProgressLine extends View {

    private int cornersRadius = 5;
    private Paint paint = new Paint();
    private Paint paintSecondary = new Paint();
    private float progress;

    public ProgressLine(Context context) {
        super(context);
    }

    public ProgressLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void update(float progress, int mainColor, int secondaryColor){
        this.progress = progress;
        paint.setColor(mainColor);
        paintSecondary.setColor(secondaryColor);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        canvas.drawRoundRect(new RectF(0, 0, width, getHeight()), cornersRadius, cornersRadius, paintSecondary);

        width = (int) (width*progress);
        canvas.drawRoundRect(new RectF(0, 0, width, getHeight()), cornersRadius, cornersRadius, paint);
        super.onDraw(canvas);
    }
}

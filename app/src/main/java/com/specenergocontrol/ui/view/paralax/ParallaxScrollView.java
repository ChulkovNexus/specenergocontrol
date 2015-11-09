package com.specenergocontrol.ui.view.paralax;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by Комп on 08.07.2015.
 */
public class ParallaxScrollView extends ScrollView {
    private static final float SCROLL_MULTIPLIER = -0.4f;
    private static final int START_OFFSET= 80;
    private int recyclerViewScroll;

    public ParallaxScrollView(Context context) {
        super(context);
        init();
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    public int getContentHeight() {
        if (getChildCount()==0)
            return -1;
        int measuredHeight = getChildAt(0).getMeasuredHeight();
        if (measuredHeight>START_OFFSET)
            return measuredHeight - START_OFFSET;
        else
            return measuredHeight;

    }

    public void setRecyclerViewScroll(int of) {
        float ofCalculated = of * SCROLL_MULTIPLIER;
        int measuredHeight = getChildAt(0).getMeasuredHeight();
        if (ofCalculated<measuredHeight){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getChildAt(0).setTranslationY(ofCalculated);
            } else {
                TranslateAnimation anim = new TranslateAnimation(0, 0, ofCalculated, ofCalculated);
                anim.setFillAfter(true);
                anim.setDuration(0);
                getChildAt(0).startAnimation(anim);
            }
        }
    }
}

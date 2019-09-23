package com.ritara.svustudent;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class DynamicViewPager extends ViewPager {

    private View mCurrentView;
    private Handler handler = new Handler();
    private int a, b;

    public DynamicViewPager(Context context) {
        super(context);
    }

    public DynamicViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCurrentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int height = 0;
        mCurrentView.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int h = mCurrentView.getMeasuredHeight();
        if (h > height) height = h;
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        handler.removeCallbacks(runnable);

//        int mode = MeasureSpec.getMode(heightMeasureSpec);
//        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
//        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
//        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
//            // super has to be called in the beginning so the child views can be initialized.
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            int height = 0;
//            for (int i = 0; i < getChildCount(); i++) {
//                View child = getChildAt(i);
//                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//                int childMeasuredHeight = child.getMeasuredHeight();
//                if (childMeasuredHeight > height) {
//                    height = childMeasuredHeight;
//                }
//            }
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        }
//        // super has to be called again so the new specs are treated as exact measurements
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }
}

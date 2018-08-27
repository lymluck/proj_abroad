package com.smartstudy.commonlib.ui.customView.transferImage.style.view.indicator;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import java.util.Locale;

/**
 * 数字索引指示器
 * <p>
 * Created by louis on 2017/4/23 0023.
 * <p>
 */
public class NumberIndicator extends AppCompatTextView {

    private static final String STR_NUM_FORMAT = "%s/%s";

    private ViewPager mViewPager;
    private final ViewPager.OnPageChangeListener mInternalPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

            if (mViewPager.getAdapter() == null || mViewPager.getAdapter().getCount() <= 0)
                return;

            setText(String.format(Locale.getDefault(),
                    STR_NUM_FORMAT,
                    position + 1,
                    mViewPager.getAdapter().getCount()));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    public NumberIndicator(Context context) {
        this(context, null);
    }

    public NumberIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNumberIndicator();
    }

    private void initNumberIndicator() {
        setTextColor(Color.WHITE);
        setTextSize(18);
    }

    public void setViewPager(ViewPager viewPager) {
        if (viewPager != null && viewPager.getAdapter() != null) {
            mViewPager = viewPager;
            mViewPager.removeOnPageChangeListener(mInternalPageChangeListener);
            mViewPager.addOnPageChangeListener(mInternalPageChangeListener);
            mInternalPageChangeListener.onPageSelected(mViewPager.getCurrentItem());
        }
    }
}

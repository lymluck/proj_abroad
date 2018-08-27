package com.smartstudy.commonlib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.smartstudy.commonlib.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by yqy on 2017/12/13.
 */

public class SlidingTabStrip extends HorizontalScrollView {

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }

    public interface TabOnclick {
        void getSelectPosition(int position);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{
        android.R.attr.textSize,
        android.R.attr.textColor
    };
    // @formatter:on
    private LinearLayout.LayoutParams matchParentTabLayoutParams;
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private LinearLayout tabsContainer;

    private TabOnclick tabOnclick;

    private int tabCount;

    private int currentPosition = 0;
    private int selectedPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;

    //该属性表示里面的TAB是否均分整个PagerSlidingTabStrip控件的宽,
    // true是,false不均分,从左到右排列,默认false
    private boolean shouldExpand = false;
    private boolean textAllCaps = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 8;
    private int indicatorMargin = 0;
    private int underlineHeight = 2;
    private int dividerPaddingTopBottom = 12;
    private int tabPadding = 19;
    private int dividerWidth = 1;

    private int tabTextSize = 12;
    private int tabTextColor = 0xFF666666;
    private int selectedTabTextColor = 0xFF45c01a;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX = 0;

    private int tabBackgroundResId;
    private Locale locale;
    private Context context;
    private boolean smoothScrollWhenClickTab = true;//设置当点击TAB时,内容区域的ViewPager是否需要动画切换
    private List<Map<String, View>> tabViews = new ArrayList<>();
    private boolean mFadeEnabled = true;
    private float zoomMax = 0.3f;
    private SlidingTabStrip.State mState;

    private enum State {
        IDLE, GOING_LEFT, GOING_RIGHT
    }

    private int oldPage;

    public void setTabOnclick(TabOnclick tabOnclick) {
        this.tabOnclick = tabOnclick;
    }

    public SlidingTabStrip(Context context) {
        this(context, null);
    }

    public SlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setFillViewport(true);
        setWillNotDraw(false);// 防止onDraw方法不执行

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        //设置默认值
        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        indicatorMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorMargin, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPaddingTopBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPaddingTopBottom, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);
        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        indicatorMargin = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorMargin, indicatorMargin);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPaddingTopBottom = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPaddingTopBottom, dividerPaddingTopBottom);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);
        selectedTabTextColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsTextSelectedColor, selectedTabTextColor);
        zoomMax = a.getFloat(R.styleable.PagerSlidingTabStrip_pstsScaleZoomMax, zoomMax);
        smoothScrollWhenClickTab = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsSmoothScrollWhenClickTab, smoothScrollWhenClickTab);
        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        matchParentTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);


        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }


    public void addTextTab(final int position, String title, int tabCount) {
        this.tabCount = tabCount;
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        tab.setIncludeFontPadding(false);

        TextView tab2 = new TextView(getContext());
        tab2.setText(title);
        tab2.setGravity(Gravity.CENTER);
        tab2.setSingleLine();
        tab2.setIncludeFontPadding(false);

        addTab(position, tab, tab2);
    }


    public void setCurrentIndex(final int currentIndex) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                currentPosition = currentIndex;
                selectedPosition = currentIndex;
                scrollToChild(currentPosition, 0);
                updateTabStyles();
            }
        });
    }

    public void scrollToChild(final int currentIndex) {
        currentPosition = currentIndex;
        selectedPosition = currentIndex;
        scrollToChild(currentPosition, 0);
        updateTabStyles();
        invalidate();
    }

    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab, null);
    }

    private void addTab(final int position, View tab, View tab2) {

        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tab2.setPadding(tabPadding, 0, tabPadding, 0);

        FrameLayout framelayout = new FrameLayout(context);
        framelayout.addView(tab, 0, matchParentTabLayoutParams);
        framelayout.addView(tab2, 1, matchParentTabLayoutParams);
        tabsContainer.addView(framelayout, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);

        framelayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFadeEnabled = false;//点击时没有文字颜色渐变效果
                selectedPosition = position;
                currentPosition = position;
                scrollToChild(position, 0);//滚动HorizontalScrollView
                updateTabStyles();
                tabOnclick.getSelectPosition(selectedPosition);
            }
        });


        Map<String, View> map = new HashMap<>();

        ViewHelper.setAlpha(tab, 1);
        map.put("normal", tab);

        ViewHelper.setAlpha(tab2, 0);
        map.put("selected", tab2);

        tabViews.add(position, map);
    }

    private void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {
            FrameLayout frameLayout = (FrameLayout) tabsContainer.getChildAt(i);
            frameLayout.setBackgroundResource(tabBackgroundResId);

            for (int j = 0; j < frameLayout.getChildCount(); j++) {
                View v = frameLayout.getChildAt(j);
                if (v instanceof TextView) {
                    TextView tab = (TextView) v;
                    tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                    tab.setTypeface(tabTypeface, tabTypefaceStyle);
                    tab.setPadding(tabPadding, 0, tabPadding, 0);
                    if (j == 0) {
                        tab.setTextColor(tabTextColor);
                    } else {
                        tab.setTextColor(selectedTabTextColor);
                    }
                    ViewHelper.setAlpha(tabViews.get(i).get("normal"), 1);
                    ViewHelper.setAlpha(tabViews.get(i).get("selected"), 0);

                    //set normal  Scale
                    ViewHelper.setPivotX(frameLayout, frameLayout.getMeasuredWidth() * 0.5f);
                    ViewHelper.setPivotY(frameLayout, frameLayout.getMeasuredHeight() * 0.5f);
                    ViewHelper.setScaleX(frameLayout, 1f);
                    ViewHelper.setScaleY(frameLayout, 1f);

                    // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
                    // pre-ICS-build
                    if (textAllCaps) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                            tab.setAllCaps(true);
                        } else {
                            tab.setText(tab.getText().toString().toUpperCase(locale));
                        }
                    }
                    if (i == selectedPosition) {
                        ViewHelper.setAlpha(tabViews.get(i).get("normal"), 0);
                        ViewHelper.setAlpha(tabViews.get(i).get("selected"), 1);

                        //set select  Scale
                        ViewHelper.setPivotX(frameLayout, frameLayout.getMeasuredWidth() * 0.5f);
                        ViewHelper.setPivotY(frameLayout, frameLayout.getMeasuredHeight() * 0.5f);
                        ViewHelper.setScaleX(frameLayout, 1 + zoomMax);
                        ViewHelper.setScaleY(frameLayout, 1 + zoomMax);
                    }
                }
            }
        }
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            //不居中的
            // smoothScrollTo(newScrollX, 0);
            //以下是当tab很多时，点击屏幕右边的，点击的那个居中!!!
            int k = tabsContainer.getChildAt(position).getMeasuredWidth();
            int l = tabsContainer.getChildAt(position).getLeft() + offset;
            int i2 = l + k / 2 - this.getMeasuredWidth() / 2;
            smoothScrollTo(i2, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0) {
            return;
        }
        final int height = getHeight();

        // draw underline
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // draw indicator line
        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }
        canvas.drawRect(lineLeft + indicatorMargin, height - indicatorHeight,
            lineRight - indicatorMargin, height, rectPaint);


        // draw divider

        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPaddingTopBottom, tab.getRight(), height - dividerPaddingTopBottom, dividerPaint);
        }
    }


    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightDp) {
        this.indicatorHeight = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, indicatorLineHeightDp, getResources().getDisplayMetrics());
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightDp) {
        this.underlineHeight = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, underlineHeightDp, getResources().getDisplayMetrics());
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPaddingTopBottom(int dividerPaddingDp) {
        this.dividerPaddingTopBottom = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dividerPaddingDp, getResources().getDisplayMetrics());
        invalidate();
    }

    public int getDividerPaddingTopBottom() {
        return dividerPaddingTopBottom;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizeSp) {
        this.tabTextSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, textSizeSp, getResources().getDisplayMetrics());
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setSelectedTextColor(int textColor) {
        this.selectedTabTextColor = textColor;
        updateTabStyles();
    }

    public void setSelectedTextColorResource(int resId) {
        this.selectedTabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getSelectedTextColor() {
        return selectedTabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
        updateTabStyles();
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingDp) {
        this.tabPadding = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, paddingDp, getResources().getDisplayMetrics());
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    public boolean isSmoothScrollWhenClickTab() {
        return smoothScrollWhenClickTab;
    }

    public void setSmoothScrollWhenClickTab(boolean smoothScrollWhenClickTab) {
        this.smoothScrollWhenClickTab = smoothScrollWhenClickTab;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SlidingTabStrip.SavedState savedState = (SlidingTabStrip.SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SlidingTabStrip.SavedState savedState = new SlidingTabStrip.SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SlidingTabStrip.SavedState> CREATOR = new Creator<SlidingTabStrip.SavedState>() {
            @Override
            public SlidingTabStrip.SavedState createFromParcel(Parcel in) {
                return new SlidingTabStrip.SavedState(in);
            }

            @Override
            public SlidingTabStrip.SavedState[] newArray(int size) {
                return new SlidingTabStrip.SavedState[size];
            }
        };
    }


    public void setFadeEnabled(boolean enabled) {
        mFadeEnabled = enabled;
    }

    public boolean getFadeEnabled() {
        return mFadeEnabled;
    }

    public float getZoomMax() {
        return zoomMax;
    }

    public void setZoomMax(float zoomMax) {
        this.zoomMax = zoomMax;
    }

    private boolean isSmall(float positionOffset) {
        return Math.abs(positionOffset) < 0.0001;
    }


    protected void animateFadeScale(View left, View right, float positionOffset, int position) {
        if (mState != SlidingTabStrip.State.IDLE) {
            if (left != null) {
                ViewHelper.setAlpha(tabViews.get(position).get("normal"), positionOffset);
                ViewHelper.setAlpha(tabViews.get(position).get("selected"), 1 - positionOffset);
                float mScale = 1 + zoomMax - zoomMax * positionOffset;
                ViewHelper.setPivotX(left, left.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(left, left.getMeasuredHeight() * 0.5f);
                ViewHelper.setScaleX(left, mScale);
                ViewHelper.setScaleY(left, mScale);
            }
            if (right != null) {
                ViewHelper.setAlpha(tabViews.get(position + 1).get("normal"), 1 - positionOffset);
                ViewHelper.setAlpha(tabViews.get(position + 1).get("selected"), positionOffset);
                float mScale = 1 + zoomMax * positionOffset;
                ViewHelper.setPivotX(right, right.getMeasuredWidth() * 0.5f);
                ViewHelper.setPivotY(right, right.getMeasuredHeight() * 0.5f);
                ViewHelper.setScaleX(right, mScale);
                ViewHelper.setScaleY(right, mScale);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }


    public int getCurrentIndex() {
        return this.currentPosition;
    }
}

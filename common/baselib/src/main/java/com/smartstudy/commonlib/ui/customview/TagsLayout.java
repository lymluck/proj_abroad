package com.smartstudy.commonlib.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.utils.DensityUtils;

import java.util.List;

/**
 * Created by yqy on 2017/12/4.
 */

public class TagsLayout<T> extends ViewGroup {
    private int childHorizontalSpace;
    private int childVerticalSpace;
    private int childTextColor;
    private float childTextSize;
    private int maxLine = 0;
    private int backgroup;
    private int padingLeftAndRight;
    private int getPadingBottomAndTop;

    public TagsLayout(Context context) {
        this(context, null);
    }

    public TagsLayout(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public TagsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.TagsLayout);
        if (attrArray != null) {
            childHorizontalSpace = attrArray.getDimensionPixelSize(R.styleable.TagsLayout_tagHorizontalSpace, 8);
            childVerticalSpace = attrArray.getDimensionPixelSize(R.styleable.TagsLayout_tagVerticalSpace, 0);
            childTextColor = attrArray.getColor(R.styleable.TagsLayout_textColor, getResources().getColor(R.color.orange_clor));
            childTextSize = attrArray.getDimension(R.styleable.TagsLayout_textChildSize, DensityUtils.dip2px(11));
            padingLeftAndRight=attrArray.getDimensionPixelSize(R.styleable.TagsLayout_padding_left_right, 15);
            getPadingBottomAndTop=attrArray.getDimensionPixelSize(R.styleable.TagsLayout_padding_bottom_top, 0);
            attrArray.recycle();
        }
    }

    /**
     * 负责设置子控件的测量模式和大小 根据所有子控件设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        // 如果是warp_content情况下，记录宽和高
        int width = 0;
        int height = 0;
        /**
         * 记录每一行的宽度，width不断取最大宽度
         */
        int lineWidth = 0;
        /**
         * 每一行的高度，累加至height
         */
        int lineHeight = 0;

        int count = getChildCount();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        // 遍历每个子元素
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            // 测量每一个child的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到child的lp
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            // 当前子空间实际占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin + childHorizontalSpace;
            // 当前子空间实际占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin + childVerticalSpace;
            /**
             * 如果加入当前child，则超出最大宽度，则的到目前最大宽度给width，类加height 然后开启新行
             */
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(lineWidth, childWidth);// 取最大的
                lineWidth = childWidth; // 重新开启新行，开始记录
                // 叠加当前高度，
                height += lineHeight;
                // 开启记录下一行的高度
                lineHeight = childHeight;
                child.setTag(new Location(left, top + height, childWidth + left - childHorizontalSpace, height + child.getMeasuredHeight() + top));
            } else {// 否则累加值lineWidth,lineHeight取最大高度
                child.setTag(new Location(lineWidth + left, top + height, lineWidth + childWidth - childHorizontalSpace + left, height + child.getMeasuredHeight() + top));
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
        }
        width = Math.max(width, lineWidth) + getPaddingLeft() + getPaddingRight();
        height += lineHeight;
        sizeHeight += getPaddingTop() + getPaddingBottom();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            Location location = (Location) child.getTag();
            child.layout(location.left, location.top, location.right, location.bottom);
        }
    }

    /**
     * 记录子控件的坐标
     */
    public class Location {
        public Location(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        public int left;
        public int top;
        public int right;
        public int bottom;

    }


    public void deleteAllView() {
        int size = this.getChildCount();
        for (int i = 0; i < size; i++) {
            this.removeViewAt(i);
        }
    }

    public void setMaxLine(int number) {
        this.maxLine = number;
    }


    public void setBackGroup(int backgroup) {
        this.backgroup = backgroup;
    }

    public void createTab(Context context, List<String> tList) {
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int j = maxLine == 0 ? tList.size() : maxLine;
        if (j > tList.size()) {
            j = tList.size();
        }

        if (this.getChildCount() > 0) {
            return;
        }
        for (int i = 0; i < j; i++) {
            TextView textView = new TextView(context);
            textView.setText(tList.get(i));
            textView.setTextColor(childTextColor);
            textView.setTextSize(DensityUtils.px2sp(childTextSize));
            textView.setPadding(padingLeftAndRight, getPadingBottomAndTop, padingLeftAndRight, getPadingBottomAndTop);
            textView.setBackgroundResource(backgroup);
            this.addView(textView, lp);
        }

    }

}
package com.smartstudy.commonlib.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.smartstudy.commonlib.utils.ScreenUtils;

/**
 * @author louis
 * @date on 2018/5/29
 * @describe 可设置最大高度的scrollview
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class MaxHeightableScrollView extends ScrollView {
    private Context mContext;


    public MaxHeightableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public MaxHeightableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            //最大高度显示为屏幕内容高度的一半
            int screenH = ScreenUtils.getScreenHeight();
            //此处是关键
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(screenH / 5, MeasureSpec.AT_MOST);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //重新计算控件高、宽
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

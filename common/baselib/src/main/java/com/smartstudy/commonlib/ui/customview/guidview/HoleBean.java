package com.smartstudy.commonlib.ui.customview.guidview;

import android.graphics.RectF;
import android.view.View;

public class HoleBean {

    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_RECTANGLE = TYPE_CIRCLE + 1;
    public static final int TYPE_OVAL = TYPE_RECTANGLE + 1;

    private View mHole;
    private int mType;

    public HoleBean(View hole, int type) {
        this.mHole = hole;
        this.mType = type;
    }

    public int getRadius() {
        return mHole != null ? Math.min(mHole.getWidth(), mHole.getHeight()) / 2 + 15 : 0; //添加15的修正值，达到padding效果
    }

    public RectF getRectF() {
        RectF rectF = new RectF();
        if (mHole != null) {
            int[] location = new int[2];
            mHole.getLocationOnScreen(location);
            //上下左右调整15个大小
            rectF.left = location[0] - 15;
            rectF.top = location[1] - 15;
            rectF.right = location[0] + mHole.getWidth() + 15;
            rectF.bottom = location[1] + mHole.getHeight() + 15;
        }
        return rectF;
    }

    public int getType() {
        return mType;
    }

}
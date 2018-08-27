package com.smartstudy.commonlib.ui.customview.calendar.theme;

import android.graphics.Color;

import com.smartstudy.commonlib.utils.DensityUtils;

/**
 * Created by Administrator on 2016/8/9.
 */
public class ADCircleDayTheme implements IDayTheme {
    @Override
    public int colorSelectBG(String des) {
        return desColor(des);
    }

    @Override
    public int colorSelectDay() {
        return Color.parseColor("#FFFFFF");
    }

    @Override
    public int colorDecor(String des) {
        return desColor(des);
    }

    @Override
    public int colorDay() {
        return Color.parseColor("#26343F");
    }

    @Override
    public int colorDisableDay() {
        return Color.parseColor("#c4c9cc");
    }

    @Override
    public int colorRest() {
        return Color.parseColor("#2AC5C8");
    }

    @Override
    public int colorWork() {
        return Color.parseColor("#C78D7D");
    }

    @Override
    public int colorDesc(String des) {
        return desColor(des);
    }

    @Override
    public int colorSelectDesc() {
        return Color.parseColor("#FFFFFF");
    }

    @Override
    public int sizeDay() {
        return DensityUtils.sp2px(16f);
    }

    @Override
    public int sizeDesc() {
        return DensityUtils.sp2px(11f);
    }

    @Override
    public int sizeDecor() {
        return DensityUtils.dip2px(2.5f);
    }

    @Override
    public int dateHeight() {
        return DensityUtils.dip2px(56f);
    }

    private int desColor(String des) {
        if ("托福".equals(des)) {
            return Color.parseColor("#FFB02F");
        } else if ("雅思".equals(des)) {
            return Color.parseColor("#FF5A52");
        } else if ("GMAT".equals(des)) {
            return Color.parseColor("#4FC069");
        } else if ("GRE".equals(des)) {
            return Color.parseColor("#4B67BF");
        } else if ("SAT".equals(des)) {
            return Color.parseColor("#5FACFF");
        } else if ("多种考试".equals(des)) {
            return Color.parseColor("#FF7FA4");
        } else {
            return Color.parseColor("#5FACFF");
        }
    }

}

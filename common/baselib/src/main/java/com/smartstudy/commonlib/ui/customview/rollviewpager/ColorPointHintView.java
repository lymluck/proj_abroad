package com.smartstudy.commonlib.ui.customview.rollviewpager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.smartstudy.commonlib.utils.DensityUtils;

/**
 * Created by louis on 2016/1/10.
 */
public class ColorPointHintView extends ShapeHintView {
    private int focusColor;
    private int normalColor;

    public ColorPointHintView(Context context, int focusColor, int normalColor) {
        super(context);
        this.focusColor = focusColor;
        this.normalColor = normalColor;
    }

    @Override
    public Drawable makeFocusDrawable() {
        GradientDrawable dot_focus = new GradientDrawable();
        dot_focus.setColor(focusColor);
        dot_focus.setCornerRadius(DensityUtils.dip2px(3));
        dot_focus.setSize(DensityUtils.dip2px(6), DensityUtils.dip2px(6));
        return dot_focus;
    }

    @Override
    public Drawable makeNormalDrawable() {
        GradientDrawable dot_normal = new GradientDrawable();
        dot_normal.setColor(normalColor);
        dot_normal.setCornerRadius(DensityUtils.dip2px(3));
        dot_normal.setSize(DensityUtils.dip2px(6), DensityUtils.dip2px(6));
        return dot_normal;
    }
}

package com.smartstudy.commonlib.base.tools;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * Created by louis on 17/4/25.
 */

public class IeltsScoreFilter implements InputFilter {
    /**
     * 限制输入框小数的位数1位
     * 限制整数范围120以内
     */
    private static final int DECIMAL_DIGITS = 1;

    /**
     * source    新输入的字符串
     * start    新输入的字符串起始下标，一般为0
     * end    新输入的字符串终点下标，一般为source长度-1
     * dest    输入之前文本框内容
     * dstart    原内容起始坐标，一般为0
     * dend    原内容终点坐标，一般为dest长度-1
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        String dValue = dest.toString();
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
            return null;
        }
        //第一位输入.自动过变为9.0
        if (dest.length() == 0 &&"9".equals(source.toString())) {
            return "9.0";
        }
        //第一位输入.自动过变为0.
        if (dest.length() == 0 && ".".equals(source)) {
            return "0.";
        }
        //第一位不能输入0
        if (dest.length() == 0 && "0".equals(source)) {
            return "";
        }
        String[] splitArray = dValue.split("\\.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            if (dotValue.length() == DECIMAL_DIGITS) {
                return "";
            }
        } else {
            if (!TextUtils.isEmpty(dValue)) {
                double value = Double.parseDouble(dValue);
                return value >= 0.0 && value < 9.0 ? source : "";
            } else {
                return source + ".";
            }

        }
        return null;
    }
}

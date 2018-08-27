package com.smartstudy.commonlib.base.tools;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import com.smartstudy.commonlib.utils.LogUtils;

/**
 * Created by louis on 17/4/25.
 */

public class ToeflScoreFilter implements InputFilter {

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
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
            return null;
        }
        //第一位不能输入0
        if (dest.length() == 0 && "0".equals(source)) {
            return "";
        }
        //第一位不能输入.
        if (dest.length() == 0 && ".".equals(source)) {
            return "";
        }
        LogUtils.d(source + "");
        LogUtils.d(start + "");
        LogUtils.d(end + "");
        LogUtils.d(dest + "");
        LogUtils.d(dstart + "");
        LogUtils.d(dend + "");
        String dValue = dest.toString();
        if (!TextUtils.isEmpty(dValue)) {
            int value = Integer.parseInt(dValue, 10);
            if (value <= 12) {
                if (value == 12) {
                    return "0".equals(source) ? source : "0";
                } else {
                    return ".".equals(source) ? "" : source;
                }
            } else {
                return "";
            }
        }
        return null;
    }
}

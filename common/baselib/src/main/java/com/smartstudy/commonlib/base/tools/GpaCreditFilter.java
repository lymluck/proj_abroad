package com.smartstudy.commonlib.base.tools;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * Created by yqy on 2017/10/25.
 */

public class GpaCreditFilter implements InputFilter {
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


        String dValue = dest.toString();
        if (dValue.contains(".")) {
            if (".".equals(source.toString())) {
                return null;
            }else{
                return source;
            }
        }
        if (!TextUtils.isEmpty(dValue)) {
            double value = Double.parseDouble(dValue);
            if (value < 10) {
               return source;
            } else {
                return ".".equals(source) ? source : "";
            }
        }
        return null;
    }
}

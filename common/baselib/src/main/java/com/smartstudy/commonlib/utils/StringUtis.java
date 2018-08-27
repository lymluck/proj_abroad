package com.smartstudy.commonlib.utils;

import java.util.Arrays;

/**
 * @author louis
 * @date on 2018/2/5
 * @describe 字符串处理工具类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class StringUtis {

    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}

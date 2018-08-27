package com.smartstudy.commonlib.utils;

import android.content.Context;
import android.widget.Toast;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.R;

public class ToastUtils {

    private static Toast mToast;

    /**
     * 非阻塞试显示Toast,防止出现连续点击Toast时的显示问题
     */
    public static void showToast(CharSequence text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.appContext, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    public static void showToast(CharSequence text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showNotNull(Context context, CharSequence text) {
        showToast(String.format(context.getString(R.string.not_null), text), Toast.LENGTH_SHORT);
    }
}

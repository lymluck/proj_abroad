package com.smartstudy.commonlib.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.tools.SystemBarTintManager;

import io.rong.imkit.activity.FilePreviewActivity;

/**
 * @author louis
 * @date on 2018/2/11
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class ImFilePreviewActivity extends FilePreviewActivity {

    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBar();
    }

    @Override
    public void openFile(String fileName, String fileSavePath) {
        super.openFile(fileName, fileSavePath);
    }

    /**
     * 沉浸式状态栏
     */
    public void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }
            tintManager.setStatusBarLightMode(this, true);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_top_color);
        }
    }

    /**
     * 透明状态栏
     *
     * @param on
     */
    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}

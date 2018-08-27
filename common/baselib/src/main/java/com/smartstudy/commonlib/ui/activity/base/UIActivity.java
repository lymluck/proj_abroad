package com.smartstudy.commonlib.ui.activity.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.smartstudy.commonlib.app.BaseApplication;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.tools.SystemBarTintManager;
import com.smartstudy.commonlib.utils.NoFastClickUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.StringUtis;
import com.smartstudy.permissions.AfterPermissionGranted;
import com.smartstudy.permissions.AppSettingsDialog;
import com.smartstudy.permissions.Permission;
import com.smartstudy.permissions.PermissionUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * activity父类基本封装
 * Created by louis on 2017/2/22.
 */
public abstract class UIActivity extends AppCompatActivity implements View.OnClickListener,PermissionUtil.PermissionCallbacks{
    //自定义activity栈
    public static List<UIActivity> activities = new ArrayList<>();
    private boolean changeStatusTrans = false;
    private boolean darkMode = true;
    private SystemBarTintManager tintManager;
    private AppSettingsDialog permissionDialog;
    private int statusColor = R.color.app_top_color;
    protected BaseApplication application;
    protected LayoutInflater mInflater;
    protected boolean hasBasePer = false;
    protected boolean canFastClick = false;
    private static String[] mDenyPerms = StringUtis.concatAll(
            Permission.STORAGE, Permission.PHONE, Permission.MICROPHONE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (!changeStatusTrans) {
            initSystemBar();
        }
        application = BaseApplication.getInstance();
        mInflater = getLayoutInflater();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        RelativeLayout rlyt_top = findViewById(R.id.rlyt_top);
        if (rlyt_top != null) {
            rlyt_top.getBackground().setAlpha(255);
        }
        if (application.isBackground()) {
            application.setBackground(false);
        }
        requestPermissions();
        super.onResume();
        //统计时长
        MobclickAgent.onResume(this);
    }

    @AfterPermissionGranted(ParameterUtils.REQUEST_CODE_PERMISSIONS)
    public void requestPermissions() {
        if (!PermissionUtil.hasPermissions(this, mDenyPerms)) {
            hasBasePer = false;
            //申请基本的权限
            PermissionUtil.requestPermissions(this, Permission.getPermissionContent(Arrays.asList(mDenyPerms)),
                    ParameterUtils.REQUEST_CODE_PERMISSIONS, mDenyPerms);
        } else {
            hasBasePer = true;
            hasBasePermission();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //统计时长
        MobclickAgent.onPause(this);
        if (!isAppOnForeground()) {
            application.setBackground(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mInflater != null) {
            mInflater = null;
        }
        if (application != null) {
            application = null;
        }
        if (tintManager != null) {
            tintManager = null;
        }
        if (permissionDialog != null) {
            permissionDialog.dialogDismiss();
            permissionDialog = null;
        }
        UMShareAPI.get(this).release();
    }

    //填充页面视图
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initViewAndData();
        initEvent();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initViewAndData();
        initEvent();
    }

    protected void setChangeStatusTrans(boolean changeStatusTrans) {
        this.changeStatusTrans = changeStatusTrans;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    protected void setStatusColor(int color) {
        this.statusColor = color;
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
            if (darkMode) {
                tintManager.setStatusBarLightMode(this, true);
            }
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(statusColor);
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    //初始化view
    protected abstract void initViewAndData();

    //初始化事件
    public abstract void initEvent();

    //响应点击事件
    protected abstract void doClick(View v);

    //获取了100%的基本权限
    public void hasBasePermission() {
    }


    public static void addActivity(UIActivity activity) {
        activities.add(activity);
        activity = null;
    }

    /**
     * 移除某个路径中的某个页面
     *
     * @param activity_name activity.class.getSimpleName()
     */
    public static void removeActivity(String activity_name) {
        for (Iterator<UIActivity> it = activities.iterator(); it.hasNext(); ) {
            Activity activity = it.next();
            if (activity.getClass().getName().endsWith(activity_name)) {
                it.remove();
            }
        }
    }

    /**
     * 销毁同一路径上的所有页面
     */
    public static void finishAll() {
        for (UIActivity activity : activities) {
            if (!activity.isFinishing()) {
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }
        }
        activities.clear();
    }

    //物理回退
    @Override
    public void onBackPressed() {
        finish();
    }

    //点击事件
    @Override
    public void onClick(View v) {
        if (canFastClick) {
            doClick(v);
        } else {
            if (!NoFastClickUtils.isFastClick()) {
                doClick(v);
            }
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        /**
         * 获取Android设备中所有正在运行的App
         */
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 程序由前台转后台
     */
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (!application.isBackground()) {
            application.setBackground(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> denyPerms) {
        String[] perms = new String[denyPerms.size()];
        mDenyPerms = denyPerms.toArray(perms);
        if (PermissionUtil.shouldShowRationale(this, mDenyPerms)) {
            //继续申请被拒绝了的基本权限
            PermissionUtil.requestPermissions(this, Permission.getPermissionContent(denyPerms),
                    requestCode, mDenyPerms);
        } else {
            verifyPermission(denyPerms);
        }
    }

    public void verifyPermission(List<String> denyPerms) {
        if (denyPerms != null && denyPerms.size() > 0) {
            if (permissionDialog != null && permissionDialog.isShowing()) {
                permissionDialog.dialogDismiss();
            }
            permissionDialog = new AppSettingsDialog.Builder(this).build(denyPerms);
            permissionDialog.show();
        }
    }
}


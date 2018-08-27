package com.smartstudy.commonlib.ui.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.utils.NoFastClickUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.StatusBarUtils;
import com.smartstudy.commonlib.utils.StringUtis;
import com.smartstudy.permissions.AfterPermissionGranted;
import com.smartstudy.permissions.AppSettingsDialog;
import com.smartstudy.permissions.Permission;
import com.smartstudy.permissions.PermissionUtil;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.sdk.api.UShare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * activity父类基本封装
 * Created by louis on 2017/2/22.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, PermissionUtil.PermissionCallbacks {
    //自定义activity栈
    public List<BaseActivity> activities = new ArrayList<>();
    private boolean changeStatusTrans = false;
    private AppSettingsDialog permissionDialog;
    private int statusColor = R.color.app_top_color;
    public LayoutInflater mInflater;
    protected boolean hasBasePer = false;
    protected boolean canFastClick = false;
    private static String[] mDenyPerms = StringUtis.concatAll(
        Permission.STORAGE, Permission.PHONE, Permission.MICROPHONE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mInflater = LayoutInflater.from(this);
        super.onCreate(savedInstanceState);
        initSystemBar();
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
        requestPermissions();
        super.onResume();
        UApp.pageSessionStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UApp.pageSessionEnd(this);
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
    protected void onDestroy() {
        super.onDestroy();
        if (permissionDialog != null) {
            permissionDialog.dialogDismiss();
            permissionDialog = null;
        }
        UShare.release(this);
    }

    //填充页面视图
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        setContentView(mInflater.inflate(layoutResID, null));
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

    protected void setStatusColor(int color) {
        this.statusColor = color;
    }

    /**
     * 沉浸式状态栏
     */
    public void initSystemBar() {
        int result = StatusBarUtils.setLightMode(this);
        if (!changeStatusTrans) {
            if (result == 3) {
                // 6.0以上沉浸式
                StatusBarUtils.setColor(this, getResources().getColor(statusColor), 0);
            } else if (result == 4) {
                // 其它半透明效果
                StatusBarUtils.setColor(this, getResources().getColor(statusColor));
            } else {
                // miui、flyme沉浸式
                StatusBarUtils.setColor(this, getResources().getColor(statusColor), 0);
            }
        } else {
            if (result == 4) {
                // 其它半透明效果
                StatusBarUtils.setTranslucent(this, StatusBarUtils.DEFAULT_STATUS_BAR_ALPHA);
            } else {
                // 透明效果
                StatusBarUtils.setTransparent(this);
            }
        }
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


    public void addActivity(BaseActivity activity) {
        activities.add(activity);
        activity = null;
    }

    /**
     * 移除某个路径中的某个页面
     *
     * @param activity_name activity.class.getSimpleName()
     */
    public void removeActivity(String activity_name) {
        for (Iterator<BaseActivity> it = activities.iterator(); it.hasNext(); ) {
            Activity activity = it.next();
            if (activity.getClass().getName().endsWith(activity_name)) {
                it.remove();
            }
        }
    }

    /**
     * 销毁同一路径上的所有页面
     */
    public void finishAll() {
        for (BaseActivity activity : activities) {
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


package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.tools.SystemBarTintManager;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.umeng.analytics.MobclickAgent;

import io.rong.imkit.RongIM;

public class ReloginActivity extends UIActivity {

    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relogin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSysBar();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initSysBar();
    }

    @Override
    protected void initViewAndData() {
        TextView tv_content = findViewById(R.id.tv_content);
        if (getIntent().hasExtra("conent")) {
            tv_content.setText(getIntent().getStringExtra("conent"));
        }
        setFinishOnTouchOutside(false);
        LinearLayout llyt_dialog = findViewById(R.id.llyt_dialog);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) llyt_dialog.getLayoutParams();
        lp.width = (int) (ScreenUtils.getScreenWidth() * 0.9);
        llyt_dialog.setLayoutParams(lp);
        initSysBar();
    }

    /**
     * 初始化状态栏
     */
    private void initSysBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }
            setTranslucentStatus(true);
            tintManager.setStatusBarLightMode(this, true);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);
            tintManager.setStatusBarAlpha(0);
        }
    }

    @Override
    public void initEvent() {
        MobclickAgent.onProfileSignOff();
        findViewById(R.id.btn_relogin).setOnClickListener(this);
        SPCacheUtils.put("user_name", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("user_pic", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("ticket", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("user", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("ss_user", ParameterUtils.CACHE_NULL);
        SPCacheUtils.put("user_id", ParameterUtils.CACHE_NULL);
    }

    @Override
    protected void doClick(View v) {
        if (v.getId() == R.id.btn_relogin) {
            Intent to_login = new Intent(this, LoginActivity.class);
            to_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            to_login.putExtra("toMain", true);
            startActivity(to_login);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
}

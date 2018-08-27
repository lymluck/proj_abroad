package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.SensorsUtils;
import com.smartstudy.sdk.api.UApp;

import static com.smartstudy.commonlib.utils.ParameterUtils.MEIQIA_UNREAD;
import static com.smartstudy.commonlib.utils.ParameterUtils.QA_UNREAD;
import static com.smartstudy.commonlib.utils.ParameterUtils.XXD_UNREAD;

public class ReloginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relogin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSystemBar();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initSystemBar();
    }

    @Override
    protected void initViewAndData() {
        setFinishOnTouchOutside(false);
        LinearLayout llyt_dialog = findViewById(R.id.llyt_dialog);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) llyt_dialog.getLayoutParams();
        lp.width = (int) (ScreenUtils.getScreenWidth() * 0.9);
        llyt_dialog.setLayoutParams(lp);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.btn_relogin).setOnClickListener(this);
        UApp.logout();
        // SensorsData
        SensorsUtils.trackLogout();
        SPCacheUtils.remove("user_name");
        SPCacheUtils.remove("user_pic");
        SPCacheUtils.remove("ticket");
        SPCacheUtils.remove("user");
        SPCacheUtils.remove("ss_user");
        SPCacheUtils.remove("user_id");
        SPCacheUtils.remove("zhike_id");
        SPCacheUtils.remove(QA_UNREAD);
        SPCacheUtils.remove(XXD_UNREAD);
        SPCacheUtils.remove(MEIQIA_UNREAD);
    }

    @Override
    protected void doClick(View v) {
        if (v.getId() == R.id.btn_relogin) {
            Intent to_login = new Intent(this, LoginActivity.class);
            to_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            to_login.putExtra("toMain", true);
            startActivity(to_login);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
}

package com.smartstudy.xxd.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.xxd.R;

/**
 * @author louis
 */
public class AboutXxdActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_xxd);
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.tv_version)).setText("v" + AppUtils.getVersionName());
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("关于选校帝");
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }
    }

}

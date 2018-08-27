package com.smartstudy.commonlib.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;


public class FeedBackActivity extends UIActivity {

    private TextView topdefault_centertitle;
    private EditText et_content;
    private EditText et_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

    }

    @Override
    protected void initViewAndData() {
        topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        et_content = (EditText) findViewById(R.id.et_content);
        et_contact = (EditText) findViewById(R.id.et_contact);
        topdefault_centertitle.setText("意见反馈");
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send) {
            contactUs();
        } else if (id == R.id.topdefault_leftbutton) {
            finish();
        }
    }

    public void contactUs() {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_FEEDBACK);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("content", et_content.getText().toString());
                params.put("contact", et_contact.getText().toString());
                return params;
            }
        }, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                ToastUtils.showToast(FeedBackActivity.this, msg);
            }

            @Override
            public void onSuccess(String result) {
                ToastUtils.showToast(getApplicationContext(), "提交成功！");
                finish();
            }
        });
    }
}

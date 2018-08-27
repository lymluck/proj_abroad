package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author louis
 */
@Route("AddQuestionActivity")
public class AddQuestionActivity extends UIActivity {

    private EditText etqa;
    private TextView topdefault_rightmenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
    }

    @Override
    protected void initViewAndData() {
        this.etqa = (EditText) findViewById(R.id.et_qa);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("提问");
        topdefault_rightmenu = (TextView) findViewById(R.id.topdefault_rightmenu);
        topdefault_rightmenu.setText("发布");
        topdefault_rightmenu.setTextColor(getResources().getColor(R.color.app_main_color));
        topdefault_rightmenu.setVisibility(View.VISIBLE);

    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        topdefault_rightmenu.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                KeyBoardUtils.closeKeybord(etqa, getApplicationContext());
                finish();
                break;
            case R.id.topdefault_rightmenu:
                String ticket = (String) SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL);
                if (ParameterUtils.CACHE_NULL.equals(ticket)) {
                    DialogCreator.createLoginDialog(AddQuestionActivity.this);
                } else {
                    if (TextUtils.isEmpty(etqa.getText().toString().replace(" ", ""))) {
                        ToastUtils.showNotNull(this, "提问");
                        return;
                    } else if (etqa.getText().toString().replace(" ", "").length() < 30) {
                        ToastUtils.showToast(this, "提问需不少于30字！");
                        return;
                    }
                    addQuestion();
                }
                break;
        }
    }

    private void addQuestion() {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_ADD_QUES);
            }

            @Override
            public Map getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("question", etqa.getText().toString());
                params.put("source", "android");
                return params;
            }
        }, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                if (ParameterUtils.RESPONSE_CODE_NOLOGIN.equals(errCode)) {
                    DialogCreator.createLoginDialog(AddQuestionActivity.this);
                }
                ToastUtils.showToast(AddQuestionActivity.this, msg);
            }

            @Override
            public void onSuccess(String result) {
                KeyBoardUtils.closeKeybord(etqa, getApplicationContext());
                Intent toRelated = new Intent(AddQuestionActivity.this, RelatedQuesActivity.class);
                toRelated.putExtra("ques_id", result);
                startActivity(toRelated);
                finish();
            }
        });
    }
}

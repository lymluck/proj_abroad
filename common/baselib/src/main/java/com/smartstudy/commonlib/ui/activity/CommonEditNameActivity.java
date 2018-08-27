package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.entity.PersonalParamsInfo;
import com.smartstudy.commonlib.mvp.contract.EditMyInfoContract;
import com.smartstudy.commonlib.mvp.presenter.EditMyInfoPresenter;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;

public class CommonEditNameActivity extends UIActivity implements EditMyInfoContract.View {

    private TextView topdefault_righttext;
    private EditText etname;

    private EditMyInfoContract.Presenter editP;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        topdefault_righttext = (TextView) findViewById(R.id.topdefault_righttext);
        topdefault_righttext.setText("保存");
        topdefault_righttext.setVisibility(View.VISIBLE);
        topdefault_righttext.setTextColor(getResources().getColor(R.color.app_main_color));
        TextView topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        topdefault_centertitle.setText(data.getStringExtra("title"));
        this.etname = (EditText) findViewById(R.id.et_name);
        etname.setText(data.getStringExtra("value"));
        etname.requestFocus();
        KeyBoardUtils.openKeybord(etname, this);
        flag = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
        new EditMyInfoPresenter(this);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        topdefault_righttext.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        if (id == R.id.topdefault_leftbutton) {
            KeyBoardUtils.closeKeybord(etname, this);
            finish();
        } else if (id == R.id.topdefault_righttext) {
            PersonalParamsInfo paramsModel = new PersonalParamsInfo();
            String content = etname.getText().toString().replaceAll(" ", "");
            if (TextUtils.isEmpty(content)) {
                ToastUtils.showToast(this, "输入的内容不能为空！");
                return;
            }
            if (ParameterUtils.EDIT_NAME.equals(flag)) {
                paramsModel.setName(content);
            } else if (ParameterUtils.EDIT_GZ.equals(flag) || ParameterUtils.EDIT_BK.equals(flag)||ParameterUtils.EDIT_CZ.equals(flag)) {
                paramsModel.setCurrentSchool(content);
            }
            editP.editMyInfo(paramsModel);
        }
    }

    @Override
    public void setPresenter(EditMyInfoContract.Presenter presenter) {
        if (presenter != null) {
            this.editP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void editMyInfoSuccess(String jsonObject) {
        Intent data = new Intent();
        if (ParameterUtils.EDIT_NAME.equals(flag)) {
            //更新缓存
            SPCacheUtils.put("user_name", etname.getText().toString());
        }
        data.putExtra(ParameterUtils.TRANSITION_FLAG, flag);
        data.putExtra("new_value", etname.getText().toString());
        //保存成功后销毁页面隐藏软键盘
        KeyBoardUtils.closeKeybord(etname, this);
        setResult(RESULT_OK, data);
        finish();
    }
}

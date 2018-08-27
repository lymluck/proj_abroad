package com.smartstudy.xxd.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.ui.activity.ChooseListActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.AddQaContract;
import com.smartstudy.xxd.mvp.presenter.AddQaPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author louis
 */
@Route("AddQuestionActivity")
public class AddQuestionActivity extends BaseActivity implements AddQaContract.View {

    private EditText etqa;
    private TextView topdefault_rightmenu;
    private TextView tvIntent;
    private TextView tvApplyProject;

    private String countryId;
    private String projectId;
    private ArrayList<IdNameInfo> countries;
    private ArrayList<IdNameInfo> degrees;
    private AddQaContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        UApp.actionEvent(this, "20_B_question_post");
    }

    @Override
    protected void initViewAndData() {
        this.etqa = findViewById(R.id.et_qa);
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("提问");
        topdefault_rightmenu = (TextView) findViewById(R.id.topdefault_rightmenu);
        topdefault_rightmenu.setText("发布");
        topdefault_rightmenu.setTextColor(getResources().getColor(R.color.app_main_color));
        topdefault_rightmenu.setVisibility(View.VISIBLE);
        tvIntent = findViewById(R.id.tv_intent);
        tvApplyProject = findViewById(R.id.tv_apply_project);
        new AddQaPresenter(this);
        presenter.getOptions();
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        topdefault_rightmenu.setOnClickListener(this);
        findViewById(R.id.llyt_intent).setOnClickListener(this);
        findViewById(R.id.llyt_apply_project).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                UApp.actionEvent(this, "20_A_cancel_btn");
                KeyBoardUtils.closeKeybord(etqa, getApplicationContext());
                finish();
                break;
            case R.id.topdefault_rightmenu:
                if (TextUtils.isEmpty(etqa.getText().toString().replace(" ", ""))) {
                    ToastUtils.showNotNull(this, "提问");
                    return;
                } else if (etqa.getText().toString().replace(" ", "").length() < 30) {
                    ToastUtils.showToast("提问需不少于30字！");
                    return;
                }
                if (TextUtils.isEmpty(tvIntent.getText().toString())) {
                    ToastUtils.showToast("请选择意向国家！");
                    return;
                }
                if (TextUtils.isEmpty(tvApplyProject.getText().toString())) {
                    ToastUtils.showToast("请选择申请项目！");
                    return;
                }
                UApp.actionEvent(this, "20_A_post_btn");
                presenter.addQa(etqa.getText().toString(), countryId, projectId);
                break;
            case R.id.llyt_intent:
                if (countries != null) {
                    Intent toChooseIntent = new Intent(this, ChooseListActivity.class);
                    toChooseIntent.putExtra("value", tvIntent.getText());
                    toChooseIntent.putParcelableArrayListExtra("list", countries);
                    toChooseIntent.putExtra("ischange", false);
                    toChooseIntent.putExtra("from", ParameterUtils.TYPE_OPTIONS_COURTY);
                    toChooseIntent.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_INTENT);
                    toChooseIntent.putExtra(TITLE, "选择意向国家");
                    startActivityForResult(toChooseIntent, ParameterUtils.REQUEST_CODE_ADD_QA);
                }
                break;
            case R.id.llyt_apply_project:
                if (degrees != null) {
                    Intent toChooseProj = new Intent(this, ChooseListActivity.class);
                    toChooseProj.putExtra("value", tvApplyProject.getText());
                    toChooseProj.putParcelableArrayListExtra("list", degrees);
                    toChooseProj.putExtra("ischange", false);
                    toChooseProj.putExtra("from", ParameterUtils.TYPE_OPTIONS_PROJ);
                    toChooseProj.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.CHOOSE_APPLY_PROJ);
                    toChooseProj.putExtra(TITLE, "选择申请项目");
                    startActivityForResult(toChooseProj, ParameterUtils.REQUEST_CODE_ADD_QA);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UApp.actionEvent(this, "20_A_cancel_btn");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_ADD_QA:
                String flag = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
                String value = data.getStringExtra("new_value");
                IdNameInfo info = JSON.parseObject(value, IdNameInfo.class);
                if (ParameterUtils.EDIT_INTENT.equals(flag)) {
                    tvIntent.setText(info.getName());
                    countryId = info.getId();
                } else if (ParameterUtils.CHOOSE_APPLY_PROJ.equals(flag)) {
                    tvApplyProject.setText(info.getName());
                    projectId = info.getId();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void getOptionsSuccess(List<IdNameInfo> country, List<IdNameInfo> degree) {
        this.countries = new ArrayList<>(country);
        this.degrees = new ArrayList<>(degree);
        for (IdNameInfo info : country) {
            if (info.isSelected()) {
                countryId = info.getId();
                tvIntent.setText(info.getName());
            }
        }
        for (IdNameInfo info : degree) {
            if (info.isSelected()) {
                projectId = info.getId();
                tvApplyProject.setText(info.getName());
            }
        }
    }

    @Override
    public void addQaSuccess() {
        showTip(null, "问题已发布成功");
        KeyBoardUtils.closeKeybord(etqa, getApplicationContext());
        finish();
    }

    @Override
    public void setPresenter(AddQaContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (ParameterUtils.RESPONSE_CODE_NOLOGIN.equals(errCode)) {
            DialogCreator.createLoginDialog(AddQuestionActivity.this);
        }
        ToastUtils.showToast(message);
    }
}

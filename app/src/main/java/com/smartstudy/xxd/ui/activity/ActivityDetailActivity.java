package com.smartstudy.xxd.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ActivityInfo;
import com.smartstudy.xxd.mvp.contract.ActivityDetailContract;
import com.smartstudy.xxd.mvp.presenter.ActivityDetailPresenter;

public class ActivityDetailActivity extends BaseActivity implements ActivityDetailContract.View {

    private LinearLayout llConent;
    private ActivityDetailContract.Presenter presenter;

    private String tel = "15810425405";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("活动");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        llConent = findViewById(R.id.ll_content);
        new ActivityDetailPresenter(this);
        presenter.getActivityDetail(getIntent().getStringExtra("id"));
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.btn_call).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.btn_call:
                Utils.openWithWeb(this, "tel:" + tel);
                break;
            default:
                break;
        }
    }

    @Override
    public void showAcitvity(ActivityInfo info) {
        llConent.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(info.getTel())) {
            tel = info.getTel();
            findViewById(R.id.btn_call).setVisibility(View.VISIBLE);
        }
        // 标题
        TextView title = new TextView(this);
        title.setPadding(0, DensityUtils.dip2px(28f), 0, 0);
        title.setTextSize(23);
        title.setLineSpacing(DensityUtils.dip2px(5f), 1f);
        title.setTextColor(getResources().getColor(R.color.app_text_color2));
        title.setText(info.getName());
        llConent.addView(title, -1);
        // 分割线
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(DensityUtils.dip2px(30f),
            DensityUtils.dip2px(1f));
        lineParams.topMargin = DensityUtils.dip2px(24f);
        View line = new View(this);
        line.setBackgroundColor(getResources().getColor(R.color.app_text_color2));
        line.setLayoutParams(lineParams);
        llConent.addView(line, -1);
        // 简介
        TextView intro = new TextView(this);
        intro.setPadding(0, DensityUtils.dip2px(20f), 0, 0);
        intro.setTextSize(16);
        intro.setLineSpacing(DensityUtils.dip2px(5f), 1f);
        intro.setTextColor(getResources().getColor(R.color.app_text_color2));
        intro.setText(info.getIntroduction());
        llConent.addView(intro, -1);
        // 项目预期成果
        if (!TextUtils.isEmpty(info.getExpectedOutcome())) {
            showOthers("项目预期成果", info.getExpectedOutcome());
        }
        // 适合专业
        if (!TextUtils.isEmpty(info.getMajor())) {
            showOthers("适合专业", info.getMajor());
        }
        // 活动时间
        if (!TextUtils.isEmpty(info.getActivityTime())) {
            String content = "";
            if (!TextUtils.isEmpty(info.getRegistrationDeadline())) {
                content = "报名截止时间：" + info.getRegistrationDeadline() + "\n";
            }
            content += "活动起止时间：" + info.getActivityTime();
            showOthers("活动时间", content);
        }
        // 活动地点
        if (!TextUtils.isEmpty(info.getPlace())) {
            showOthers("活动地点", info.getPlace());
        }
        // 备注
        if (!TextUtils.isEmpty(info.getNote())) {
            showOthers("备注", info.getNote());
        }

    }

    private void showOthers(String title, String content) {
        // 分割线
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            DensityUtils.dip2px(0.5f));
        lineParams.topMargin = DensityUtils.dip2px(20f);
        View line = new View(this);
        line.setBackgroundColor(getResources().getColor(R.color.horizontal_line_color));
        line.setLayoutParams(lineParams);
        llConent.addView(line, -1);
        // 标题
        TextView moduleTitle = new TextView(this);
        moduleTitle.setPadding(0, DensityUtils.dip2px(20f), 0, 0);
        moduleTitle.setTextSize(19);
        moduleTitle.setTextColor(getResources().getColor(R.color.app_text_color2));
        moduleTitle.setText(title);
        llConent.addView(moduleTitle, -1);
        // 内容
        TextView moduleContent = new TextView(this);
        moduleContent.setPadding(0, DensityUtils.dip2px(12f), 0, 0);
        moduleContent.setTextSize(14);
        moduleContent.setLineSpacing(DensityUtils.dip2px(3f), 1f);
        moduleContent.setTextColor(getResources().getColor(R.color.app_text_color1));
        moduleContent.setText(content);
        llConent.addView(moduleContent, -1);
    }

    @Override
    public void setPresenter(ActivityDetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }
}

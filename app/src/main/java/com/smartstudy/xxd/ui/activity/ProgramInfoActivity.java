package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.ProgramInfoContract;
import com.smartstudy.xxd.mvp.presenter.ProgramInfoPresenter;

public class ProgramInfoActivity extends BaseActivity implements ProgramInfoContract.View {

    private ProgramInfoContract.Presenter presenter;
    private LinearLayout llContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_info);
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        llContent = findViewById(R.id.ll_content);
        new ProgramInfoPresenter(this);
        presenter.getProgramInfo(data.getStringExtra("id"));
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

    @Override
    public void showTop(String logo, String name, String egName, String schoolName, String rank) {
        findViewById(R.id.ll_page).setVisibility(View.VISIBLE);
        DisplayImageUtils.formatCircleImgUrl(this, logo, (ImageView) findViewById(R.id.iv_school_logo));
        ((TextView) findViewById(R.id.tv_major_name)).setText(name);
        ((TextView) findViewById(R.id.tv_major_eg_name)).setText(egName);
        ((TextView) findViewById(R.id.tv_school_name)).setText(schoolName);
        TextView tvRank = findViewById(R.id.tv_rank);
        if (TextUtils.isEmpty(rank)) {
            tvRank.setText("-");
        } else {
            if (rank.length() < 3) {
                tvRank.setTextSize(13f);
            } else {
                tvRank.setTextSize(10f);
            }
            tvRank.setText(rank);
        }
    }

    @Override
    public void showFeature(String content) {
        if (!TextUtils.isEmpty(content)) {
            View view = mInflater.inflate(R.layout.item_major_intro, null);
            TextView title = view.findViewById(R.id.intro_title);
            title.setText("专业特点");
            TextView tvConent = view.findViewById(R.id.intro_content);
            tvConent.setText(content);
            llContent.addView(view, -1);
        }
    }

    @Override
    public void showRequest(String content) {
        if (!TextUtils.isEmpty(content)) {
            View view = mInflater.inflate(R.layout.item_major_intro, null);
            TextView title = view.findViewById(R.id.intro_title);
            title.setText("申请要求");
            TextView tvConent = view.findViewById(R.id.intro_content);
            tvConent.setText(content);
            llContent.addView(view, -1);
        }
    }

    @Override
    public void showCourse(String content) {
        if (!TextUtils.isEmpty(content)) {
            View view = mInflater.inflate(R.layout.item_major_intro, null);
            TextView title = view.findViewById(R.id.intro_title);
            title.setText("课程设置");
            TextView tvConent = view.findViewById(R.id.intro_content);
            tvConent.setText(content);
            llContent.addView(view, -1);
        }
    }

    @Override
    public void setPresenter(ProgramInfoContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }
}

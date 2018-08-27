package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HomeHotProgramInfo;
import com.smartstudy.xxd.mvp.contract.MajorInfoContract;
import com.smartstudy.xxd.mvp.presenter.MajorInfoPresenter;

import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

public class MajorInfoActivity extends BaseActivity implements MajorInfoContract.View {

    private MajorInfoContract.Presenter presenter;
    private LinearLayout llIntro;
    private String mTitle;
    private String majorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_info);
    }

    @Override
    protected void initViewAndData() {
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        Intent data = getIntent();
        mTitle = data.getStringExtra(TITLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(mTitle);
        llIntro = findViewById(R.id.ll_intro);
        majorId = data.getStringExtra("majorId");
        new MajorInfoPresenter(this);
        presenter.getMajorInfo(majorId);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.btn_more).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.btn_more:
                // 跳转到更多排名列表
                Intent toMore = new Intent(MajorInfoActivity.this, MajorProgramRankActivity.class);
                toMore.putExtra(TITLE, mTitle);
                toMore.putExtra("id", majorId);
                startActivity(toMore);
                break;
            default:
                break;
        }
    }

    @Override
    public void showFeature(String content) {
        View view = mInflater.inflate(R.layout.item_major_intro, null);
        TextView title = view.findViewById(R.id.intro_title);
        title.setText("专业特点");
        TextView tvConent = view.findViewById(R.id.intro_content);
        tvConent.setText(content);
        llIntro.addView(view, -1);
    }

    @Override
    public void showFactor(String content) {
        View view = mInflater.inflate(R.layout.item_major_intro, null);
        TextView title = view.findViewById(R.id.intro_title);
        title.setText("择校因素");
        TextView tvConent = view.findViewById(R.id.intro_content);
        tvConent.setText(content);
        llIntro.addView(view, -1);
    }

    @Override
    public void showEmployment(String content) {
        View view = mInflater.inflate(R.layout.item_major_intro, null);
        TextView title = view.findViewById(R.id.intro_title);
        title.setText("就业概况");
        TextView tvConent = view.findViewById(R.id.intro_content);
        tvConent.setText(content);
        llIntro.addView(view, -1);
    }

    @Override
    public void showAdvices(String content) {
        View view = mInflater.inflate(R.layout.item_major_intro, null);
        TextView title = view.findViewById(R.id.intro_title);
        title.setText("申请建议");
        TextView tvConent = view.findViewById(R.id.intro_content);
        tvConent.setText(content);
        llIntro.addView(view, -1);
    }

    @Override
    public void showPrograms(List<HomeHotProgramInfo> datas) {
        int len = datas.size();
        if (len > 0) {
            findViewById(R.id.ll_programs).setVisibility(View.VISIBLE);
            LinearLayout llProgramsList = findViewById(R.id.ll_programs_list);
            int lineH = DensityUtils.dip2px(0.5f);
            int margin16 = DensityUtils.dip2px(16f);
            int margin48 = DensityUtils.dip2px(48f);
            int margin12 = DensityUtils.dip2px(11.5f);
            for (int i = 0; i < len; i++) {
                HomeHotProgramInfo info = datas.get(i);
                initProgramData(info, llProgramsList, margin12);
                View line = new View(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    lineH);
                layoutParams.leftMargin = margin48;
                layoutParams.rightMargin = margin16;
                line.setBackgroundColor(getResources().getColor(R.color.horizontal_line_color));
                line.setLayoutParams(layoutParams);
                if (i != len - 1) {
                    llProgramsList.addView(line, -1);
                }
            }
        }
    }

    private void initProgramData(final HomeHotProgramInfo info, LinearLayout llProgramsList, int margin12) {
        View view = mInflater.inflate(R.layout.item_major_program_list, null);
        TextView tvRank = view.findViewById(R.id.tv_school_rank);
        if (TextUtils.isEmpty(info.getCategoryRank())) {
            tvRank.setText("-");
            tvRank.setTextSize(13f);
        } else {
            if (info.getCategoryRank().length() < 3) {
                tvRank.setTextSize(13f);
            } else {
                tvRank.setTextSize(10f);
            }
            tvRank.setText(info.getCategoryRank());
        }
        TextView tvSchool = view.findViewById(R.id.tv_school_name);
        tvSchool.setText(info.getSchoolName());
        TextView tvName = view.findViewById(R.id.tv_name);
        tvName.setText(info.getChineseName());
        TextView tvEgName = view.findViewById(R.id.tv_eg_name);
        tvEgName.setText(info.getEnglishName());
        llProgramsList.addView(view, -1);
        LinearLayout llInfo = view.findViewById(R.id.ll_info);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llInfo.getLayoutParams();
        params.topMargin = margin12;
        llInfo.setLayoutParams(params);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toProgram = new Intent(MajorInfoActivity.this, ProgramInfoActivity.class);
                toProgram.putExtra("id", info.getId());
                startActivity(toProgram);
            }
        });
    }

    @Override
    public void setPresenter(MajorInfoContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }
}

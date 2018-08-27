package com.smartstudy.xxd.ui.activity;

/**
 * @author yqy
 * @date on 2018/3/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqy on 2017/12/28.
 */

public class TeacherInfoActivity extends UIActivity {
    private TextView tvTitle;
    private TextView tv_year_working;

    private ImageView imageView;

    private TextView tv_certification;

    private TextView tvSchool;


    private ConsultantsInfo consultantsInfo;

    private RelativeLayout ll_qa;

    private RecyclerView recyclerView;

    private List<ConsultantsInfo.AnsweredQuestions.AnswerData> answerDataList;

    private NoScrollLinearLayoutManager mLayoutManager;

    private CommonAdapter<ConsultantsInfo.AnsweredQuestions.AnswerData> mAdapter;

    private TextView tv_total_count;

    private LinearLayout ll_answer;

    private String name;

    private LinearLayout ll_organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
    }

    @Override
    protected void initViewAndData() {
        consultantsInfo = (ConsultantsInfo) getIntent().getSerializableExtra("consultantsInfo");
        tvTitle = findViewById(R.id.tv_title);
        imageView = findViewById(R.id.iv_image);
        tv_year_working = findViewById(R.id.tv_year_working);
        tvSchool = findViewById(R.id.school);
        tv_certification = findViewById(R.id.tv_certification);
        ll_qa = findViewById(R.id.ll_qa);
        recyclerView = findViewById(R.id.rv_qa);
        recyclerView.setHasFixedSize(true);
        ll_answer = findViewById(R.id.ll_answer);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        ll_organization = findViewById(R.id.ll_organization);
        tv_total_count = findViewById(R.id.tv_total_count);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.bg_home_search).build());
        initAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setFocusable(false);

        if (consultantsInfo != null) {
            getTeacherInfoSuccess(consultantsInfo);
        }

    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        ll_answer.setOnClickListener(this);
        ll_organization.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.ll_answer:
                Intent intent = new Intent();
                intent.putExtra("data_flag", "teacher");
                intent.putExtra("teacherId", consultantsInfo.getId());
                intent.putExtra("nickName", name);
                intent.setClass(this, TeacherAnswerActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_organization:
                Intent organizaIntent = new Intent();
                organizaIntent.putExtra("teacherInfo", consultantsInfo);
                organizaIntent.setClass(this, OrganizationActivity.class);
                startActivity(organizaIntent);
                break;
            case R.id.topdefault_leftbutton:
                finish();
                break;

            default:
                break;
        }

    }


    public void getTeacherInfoSuccess(ConsultantsInfo consultantsInfo) {
        if (consultantsInfo != null) {
            name = consultantsInfo.getName();
            DisplayImageUtils.displayCircleImage(this, consultantsInfo.getAvatar(), (ImageView) this.findViewById(R.id.iv_teacher_avatar));
            ((TextView) findViewById(R.id.tv_name)).setText(consultantsInfo.getName());
            if (consultantsInfo.getOrganization() != null) {
                DisplayImageUtils.formatCircleImgUrl(this, consultantsInfo.getOrganization().getLogo(), (ImageView) this.findViewById(R.id.iv_logo));
                ((TextView) findViewById(R.id.tv_company)).setText(consultantsInfo.getOrganization().getName());
                ((TextView) findViewById(R.id.tv_subtitle)).setText(consultantsInfo.getOrganization().getSubtitle());
                ll_organization.setVisibility(View.VISIBLE);
            } else {
                ll_organization.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(consultantsInfo.getTitle())) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(consultantsInfo.getTitle());
            }

            if (TextUtils.isEmpty(consultantsInfo.getYearsOfWorking())) {
                tv_year_working.setVisibility(View.GONE);
            } else {
                tv_year_working.setVisibility(View.VISIBLE);
                tv_year_working.setText("从业" + consultantsInfo.getYearsOfWorking() + "年");
            }
            if (consultantsInfo.isSchoolCertified()) {
                imageView.setImageResource(R.drawable.certification);
                tv_certification.setText("已认证");
                tv_certification.setTextColor(Color.parseColor("#FF9C08"));
            } else {
                imageView.setImageResource(R.drawable.uncertification);
                tv_certification.setText("未认证");
                tv_certification.setTextColor(Color.parseColor("#949ba1"));
            }
            tvSchool.setText(consultantsInfo.getSchool());

            if (consultantsInfo.getAnsweredQuestions() != null) {
                tv_total_count.setText("共回答 " + consultantsInfo.getAnsweredQuestions().getPagination().getCount() + " 个问题");
                ll_qa.setVisibility(View.VISIBLE);
                if (consultantsInfo.getAnsweredQuestions().getData() != null && consultantsInfo.getAnsweredQuestions().getData().size() > 0) {
                    if (answerDataList != null) {
                        answerDataList.clear();
                    }
                    answerDataList.addAll(consultantsInfo.getAnsweredQuestions().getData());
                    mAdapter.notifyDataSetChanged();
                }

            } else {
                ll_qa.setVisibility(View.GONE);
            }

        } else {
            ToastUtils.showToast(this, "查不到该老师的详细信息");
            finish();
        }
    }


    private void initAdapter() {
        answerDataList = new ArrayList<>();
        mAdapter = new CommonAdapter<ConsultantsInfo.AnsweredQuestions.AnswerData>(this, R.layout.item_question_list, answerDataList) {

            @Override
            protected void convert(ViewHolder holder, final ConsultantsInfo.AnsweredQuestions.AnswerData answerData, int position) {
                setupHolderView(holder, View.VISIBLE);
                String avatar = answerData.getAsker().getAvatar();
                String askName = answerData.getAsker().getName();
                TextView tv_default_name = holder.getView(R.id.tv_default_name);
                holder.setPersonImageUrl(R.id.iv_asker, avatar, true);
                tv_default_name.setVisibility(View.GONE);
                holder.setText(R.id.tv_qa_name, askName);
                holder.setText(R.id.tv_see, String.format(getString(R.string.visit_count), answerData.getVisitCount()));
                holder.setText(R.id.tv_ansewer_count, answerData.getAnswerCount() + " 回答");
                holder.setText(R.id.tv_my_time, answerData.getCreateTimeText());
                holder.setText(R.id.tv_qa, answerData.getContent());
                holder.setText(R.id.tv_time, answerData.getCreateTimeText());


                mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        ConsultantsInfo.AnsweredQuestions.AnswerData answer = answerDataList.get(position);
                        StatisticUtils.actionEvent(TeacherInfoActivity.this, "19_A_question_detail_cell");
                        Intent toMoreDetails = new Intent(TeacherInfoActivity.this, QaDetailActivity.class);
                        toMoreDetails.putExtra("id", answer.getId() + "");
                        startActivity(toMoreDetails);
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });
            }
        };
    }

    private void setupHolderView(ViewHolder holder, int visibility) {
        holder.getView(R.id.llyt_qa_person).setVisibility(visibility);
        holder.getView(R.id.tv_see).setVisibility(visibility);
        holder = null;
    }

}


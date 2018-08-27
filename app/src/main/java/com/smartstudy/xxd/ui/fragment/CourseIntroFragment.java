package com.smartstudy.xxd.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.CourseIntroInfo;
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.commonlib.ui.customview.ExpandableTextView;
import com.smartstudy.commonlib.ui.customview.RatingBar;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.CourseIntroContract;
import com.smartstudy.xxd.mvp.presenter.CourseIntroPresenter;
import com.smartstudy.xxd.ui.activity.ShowWebViewActivity;

public class CourseIntroFragment extends UIFragment implements CourseIntroContract.View {
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_course_name;
    private TextView tv_course_see;
    private TextView tv_course_provider;
    private ExpandableTextView etv_intro;
    private ExpandableTextView etv_user;
    private ImageView iv_img;
    private ImageView iv_teacher_avatar;
    private TextView tv_teacher;
    private TextView tv_level;
    private ExpandableTextView etv_teacher_intro;
    private RatingBar rb_course_rate;
    private TextView tv_course_rate;

    private CourseIntroContract.Presenter presenter;
    private String courseId;

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        presenter.getIntros(courseId);
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.fragment_course_intro, null);
    }

    @Override
    protected void initView(View rootView) {
        StatisticUtils.actionEvent(mActivity, "16_A_intro_btn");
        Bundle data = getArguments();
        courseId = data.getString("courseId");
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srlt_intro);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        tv_course_name = (TextView) rootView.findViewById(R.id.tv_course_name);
        tv_course_see = (TextView) rootView.findViewById(R.id.tv_course_see);
        rb_course_rate = (RatingBar) rootView.findViewById(R.id.rb_course_rate);
        tv_course_rate = (TextView) rootView.findViewById(R.id.tv_course_rate);
        tv_course_provider = (TextView) rootView.findViewById(R.id.tv_course_provider);
        etv_intro = (ExpandableTextView) rootView.findViewById(R.id.etv_intro);
        etv_user = (ExpandableTextView) rootView.findViewById(R.id.etv_user);
        iv_img = (ImageView) rootView.findViewById(R.id.iv_img);
        iv_img.setOnClickListener(this);
        iv_teacher_avatar = (ImageView) rootView.findViewById(R.id.iv_teacher_avatar);
        tv_teacher = (TextView) rootView.findViewById(R.id.tv_teacher);
        tv_level = (TextView) rootView.findViewById(R.id.tv_level);
        etv_teacher_intro = (ExpandableTextView) rootView.findViewById(R.id.etv_teacher_intro);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getIntros(courseId);
            }
        });
        new CourseIntroPresenter(this);
    }

    @Override
    public void setPresenter(CourseIntroContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_img:
                Intent toMoreDetails = new Intent(mActivity, ShowWebViewActivity.class);
                toMoreDetails.putExtra("web_url", HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_ZHIKE_SERVICE));
                toMoreDetails.putExtra("title", "智课留学服务");
                toMoreDetails.putExtra("url_action", "get");
                startActivity(toMoreDetails);
                break;
            default:
                break;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        swipeRefreshLayout.setRefreshing(false);
        ToastUtils.showToast(mActivity, message);
    }

    @Override
    public void showIntro(CourseIntroInfo info) {
        if (isAdded()) {
            swipeRefreshLayout.setRefreshing(false);
            tv_course_name.setText(info.getName());
            rb_course_rate.setStar(TextUtils.isEmpty(info.getRate()) ? 0 : Float.parseFloat(info.getRate()));
            tv_course_rate.setText(TextUtils.isEmpty(info.getRate()) ? "0" : info.getRate());
            tv_course_see.setText(String.format(getString(R.string.course_see_count), TextUtils.isEmpty(info.getPlayCount()) ? "0" : info.getPlayCount()));
            tv_course_provider.setText(String.format(getString(R.string.course_provider), info.getProvider()));
            etv_user.setText(info.getTargetUser());
            etv_intro.setText(info.getIntroduction());
            DisplayImageUtils.formatImgUrl(mActivity, info.getAbroadServiceImageUrl(), iv_img);
            if (info.getTeachers() != null && info.getTeachers().size() > 0) {
                showTeacher(info.getTeachers().get(0));
            }
        }
    }

    @Override
    public void showTeacher(CourseIntroInfo.TeachersEntity teachersEntity) {
        if (mActivity != null && !mActivity.isFinishing()) {
            DisplayImageUtils.displayPersonImage(mActivity, teachersEntity.getAvatarUrl(), iv_teacher_avatar);
        }
        tv_teacher.setText(teachersEntity.getName());
        tv_level.setText(teachersEntity.getTitle());
        etv_teacher_intro.setText(teachersEntity.getIntroduction());
    }
}

package com.smartstudy.xxd.ui.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.smartstudy.commonlib.entity.Event;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.mvp.contract.MyInfoContract;
import com.smartstudy.commonlib.mvp.contract.SetAvatarContract;
import com.smartstudy.commonlib.mvp.presenter.MyInfoPresenter;
import com.smartstudy.commonlib.mvp.presenter.SetAvatarPresenter;
import com.smartstudy.commonlib.ui.activity.ChooseListActivity;
import com.smartstudy.commonlib.ui.activity.CommonEditNameActivity;
import com.smartstudy.commonlib.ui.activity.SelectMyPhotoActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.customview.ReboundScrollView;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.PersonalInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;
import static com.smartstudy.xxd.utils.AppContants.USER_NAME;

public class MyInfoActivity extends BaseActivity implements MyInfoContract.View, SetAvatarContract.View {

    private KenBurnsView kbv_user;
    private TextView tvnickname;
    private RelativeLayout topmyinfo;
    private ReboundScrollView rslmyinfo;
    private View view_bg;
    private ImageView topdefault_leftbutton;
    private TextView tvintent;
    private TextView tvabroadtime;
    private TextView tvapplyproject;
    private TextView tvFee;
    private TextView tv_target_school;
    private TextView tv_cz_score;
    private TextView tv_cz_school;
    private TextView tv_gz_score;
    private TextView tv_gz_school;
    private TextView tv_bk_school;
    private TextView tv_bk_score;
    private TextView tv_eg_score;
    private TextView tv_gre_score;
    private TextView tv_sat_score;
    private TextView tv_sx_event;
    private TextView tv_ky_event;
    private TextView tv_kw_event;
    private TextView tv_shehui_event;
    private TextView tv_internal_event;
    private TextView tv_target_special;
    private TextView topdefault_centertitle;
    private View top_line;
    private CardView civcamera;
    private String from;

    private int top_alpha = 0;
    private boolean isWhite = true;
    private MyInfoContract.Presenter myP;
    private SetAvatarContract.Presenter avatarP;
    private PersonalInfo personalInfo;
    private String socialIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
    }

    @Override
    protected void onResume() {
        super.onResume();
        topmyinfo.setBackgroundColor(Color.argb(top_alpha, 255, 255, 255));
    }

    @Override
    protected void initViewAndData() {
        from = getIntent().getStringExtra("from");
        this.rslmyinfo = (ReboundScrollView) findViewById(R.id.rsl_myinfo);
        this.tvapplyproject = (TextView) findViewById(R.id.tv_apply_project);
        this.tvFee = (TextView) findViewById(R.id.tv_apply_fee);
        this.tvabroadtime = (TextView) findViewById(R.id.tv_abroad_time);
        this.tv_target_school = (TextView) findViewById(R.id.tv_target_school);
        this.tv_cz_score = (TextView) findViewById(R.id.tv_cz_score);
        this.tv_cz_school = (TextView) findViewById(R.id.tv_cz_school);
        this.tv_gz_score = (TextView) findViewById(R.id.tv_gz_score);
        this.tv_gz_school = (TextView) findViewById(R.id.tv_gz_school);
        this.tv_bk_school = (TextView) findViewById(R.id.tv_bk_school);
        this.tv_bk_score = (TextView) findViewById(R.id.tv_bk_score);
        this.tv_eg_score = (TextView) findViewById(R.id.tv_eg_score);
        this.tv_gre_score = (TextView) findViewById(R.id.tv_gre_score);
        this.tv_sat_score = (TextView) findViewById(R.id.tv_sat_score);
        this.tv_sx_event = (TextView) findViewById(R.id.tv_sx_event);
        this.tv_ky_event = (TextView) findViewById(R.id.tv_ky_event);
        this.tv_kw_event = (TextView) findViewById(R.id.tv_kw_event);
        this.tv_internal_event = (TextView) findViewById(R.id.tv_internal_event);
        this.tv_shehui_event = (TextView) findViewById(R.id.tv_shehui_event);
        this.tv_target_special = (TextView) findViewById(R.id.tv_target_special);
        this.tvintent = (TextView) findViewById(R.id.tv_intent);
        this.tvnickname = (TextView) findViewById(R.id.tv_nickname);
        this.civcamera = (CardView) findViewById(R.id.civ_camera);
        this.kbv_user = (KenBurnsView) findViewById(R.id.kbv_user);
        this.topmyinfo = (RelativeLayout) findViewById(R.id.rlyt_top);
        this.view_bg = findViewById(R.id.view_bg);
        this.topdefault_leftbutton = (ImageView) findViewById(R.id.topdefault_leftbutton);
        this.topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        this.top_line = findViewById(R.id.top_line);
        new MyInfoPresenter(this);
        if ("MeFragment".equals(from)) {
            myP.getMyInfo();
            this.topdefault_leftbutton.setImageResource(R.drawable.ic_go_back_white);
            topdefault_centertitle.setText(R.string.myinfo);
            topdefault_centertitle.setVisibility(View.GONE);
        } else {
            top_alpha = 255;
            top_line.setVisibility(View.VISIBLE);
            topdefault_centertitle.setText(R.string.editinfo);
            findViewById(R.id.cc_user_avatar).setVisibility(View.GONE);
            civcamera.setVisibility(View.GONE);
            findViewById(R.id.llyt_nickName).setVisibility(View.GONE);
            if ("editInfo".equals(from)) {
                myP.getMyInfo();
                TextView tvRight = findViewById(R.id.topdefault_righttext);
                tvRight.setText("完成");
                tvRight.setTextColor(getResources().getColor(R.color.app_main_color));
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setOnClickListener(this);
            }
            if ("seeInfo".equals(from)) {
                myP.getUserInfo(getIntent().getStringExtra("id"));
                setArrowGone();
                topdefault_centertitle.setText(getIntent().getStringExtra(TITLE));
            }
        }
        initTitleBar();
    }

    @Override
    public void initEvent() {
        //标题栏滑出背景图片过程中颜色渐变
        if ("MeFragment".equals(from)) {
            initScroll();
        }
        if (!"seeInfo".equals(from)) {
            findViewById(R.id.llyt_nickName).setOnClickListener(this);
            findViewById(R.id.llyt_intent).setOnClickListener(this);
            findViewById(R.id.llyt_abroad_time).setOnClickListener(this);
            findViewById(R.id.llyt_apply_project).setOnClickListener(this);
            findViewById(R.id.llyt_apply_fee).setOnClickListener(this);
            findViewById(R.id.llyt_target_school).setOnClickListener(this);
            findViewById(R.id.llyt_target_special).setOnClickListener(this);
            findViewById(R.id.llyt_cz_school).setOnClickListener(this);
            findViewById(R.id.llyt_cz_score).setOnClickListener(this);
            findViewById(R.id.llyt_gz_school).setOnClickListener(this);
            findViewById(R.id.llyt_gz_score).setOnClickListener(this);
            findViewById(R.id.llyt_bk_school).setOnClickListener(this);
            findViewById(R.id.llyt_bk_score).setOnClickListener(this);
            findViewById(R.id.llyt_eg_score).setOnClickListener(this);
            findViewById(R.id.llyt_gre_score).setOnClickListener(this);
            findViewById(R.id.llyt_sat_score).setOnClickListener(this);
            findViewById(R.id.llyt_sx_event).setOnClickListener(this);
            findViewById(R.id.llyt_ky_event).setOnClickListener(this);
            findViewById(R.id.llyt_kw_event).setOnClickListener(this);
            findViewById(R.id.llyt_shehui_event).setOnClickListener(this);
            findViewById(R.id.llyt_internal_event).setOnClickListener(this);
        }
        topdefault_leftbutton.setOnClickListener(this);
        civcamera.setOnClickListener(this);
    }

    private void initScroll() {
        rslmyinfo.setScrollViewListener(new ReboundScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ReboundScrollView scrollView, int x, int y, int oldx, int oldy) {
                int diff = kbv_user.getHeight() - topmyinfo.getHeight();
                if (y > 0 && y <= diff) {
                    top_alpha = y * 255 / diff;
                    topdefault_centertitle.setVisibility(View.GONE);
                    top_line.setVisibility(View.GONE);
                } else if (y > diff) {
                    top_alpha = 255;
                    topdefault_centertitle.setVisibility(View.VISIBLE);
                    top_line.setVisibility(View.VISIBLE);
                } else {
                    top_alpha = 0;
                    topdefault_centertitle.setVisibility(View.GONE);
                    top_line.setVisibility(View.GONE);
                }
                //翻转动画
                // alpha > 0.5设置绿色图标
                if (top_alpha > 128) {
                    if (isWhite) {
                        topdefault_leftbutton.setImageResource(R.drawable.ic_go_back);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(topdefault_leftbutton, "alpha", 0.5f, 1);
                        animator.setDuration(1000);
                        animator.start();
                    }
                    isWhite = false;
                } else { // 否则设置白色
                    if (!isWhite) {
                        topdefault_leftbutton.setImageResource(R.drawable.ic_go_back_white);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(topdefault_leftbutton, "alpha", 0.5f, 1);
                        animator.setDuration(1000);
                        animator.start();
                    }
                    isWhite = true;
                }
                topmyinfo.setBackgroundColor(Color.argb(top_alpha, 255, 255, 255));
            }
        });
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.topdefault_righttext:
                myP.sendInfoToTeacher(getIntent().getStringExtra("questionId"),
                    getIntent().getStringExtra("commentId"));
                break;
            case R.id.civ_camera:
                Intent intent = new Intent(MyInfoActivity.this, SelectMyPhotoActivity.class);
                intent.putExtra("singlePic", true);
                //是否需要裁剪照片，目前支持圆形、正方形
                intent.putExtra("neeclip", true);
                startActivityForResult(intent, ParameterUtils.REQUEST_CODE_CHANGEPHOTO);
                break;
            case R.id.llyt_nickName:
                Intent toNickName = new Intent(this, CommonEditNameActivity.class);
                toNickName.putExtra("value", tvnickname.getText());
                toNickName.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_NAME);
                toNickName.putExtra(TITLE, "修改昵称");
                startActivityForResult(toNickName, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.llyt_abroad_time:
                if (personalInfo != null && personalInfo.getTargetSection().getAdmissionTime() != null) {
                    Intent toChooseTime = new Intent(this, ChooseListActivity.class);
                    toChooseTime.putExtra("value", tvabroadtime.getText());
                    toChooseTime.putParcelableArrayListExtra("list", personalInfo.getTargetSection().getAdmissionTime().getOptions());
                    toChooseTime.putExtra("ischange", true);
                    toChooseTime.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toChooseTime.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_ABOARD_YEAR);
                    toChooseTime.putExtra(TITLE, "修改出国时间");
                    startActivityForResult(toChooseTime, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_intent:
                if (personalInfo != null && personalInfo.getTargetSection().getTargetCountry() != null) {
                    Intent toChooseIntent = new Intent(this, ChooseListActivity.class);
                    toChooseIntent.putExtra("value", tvintent.getText());
                    toChooseIntent.putParcelableArrayListExtra("list", personalInfo.getTargetSection().getTargetCountry().getOptions());
                    toChooseIntent.putExtra("ischange", true);
                    toChooseIntent.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toChooseIntent.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_INTENT);
                    toChooseIntent.putExtra(TITLE, "修改意向国家");
                    startActivityForResult(toChooseIntent, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_apply_project:
                if (personalInfo != null && personalInfo.getTargetSection().getTargetDegree() != null) {
                    Intent toChooseProj = new Intent(this, ChooseListActivity.class);
                    toChooseProj.putExtra("value", tvapplyproject.getText());
                    toChooseProj.putParcelableArrayListExtra("list", personalInfo.getTargetSection().getTargetDegree().getOptions());
                    toChooseProj.putExtra("ischange", true);
                    toChooseProj.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toChooseProj.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_APPLY_PROJ);
                    toChooseProj.putExtra(TITLE, "修改申请项目");
                    startActivityForResult(toChooseProj, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_apply_fee:
                if (personalInfo != null && personalInfo.getTargetSection().getBudget() != null) {
                    Intent toChooseProj = new Intent(this, ChooseListActivity.class);
                    toChooseProj.putExtra("value", tvFee.getText());
                    toChooseProj.putParcelableArrayListExtra("list", personalInfo.getTargetSection().getBudget().getOptions());
                    toChooseProj.putExtra("ischange", true);
                    toChooseProj.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toChooseProj.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_APPLY_FEE);
                    toChooseProj.putExtra(TITLE, "修改资金预算");
                    startActivityForResult(toChooseProj, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_target_school:
                if (personalInfo != null && personalInfo.getTargetSection().getTargetSchoolRank() != null) {
                    Intent toChooseTop = new Intent(this, ChooseListActivity.class);
                    toChooseTop.putExtra("value", tv_target_school.getText());
                    toChooseTop.putParcelableArrayListExtra("list", personalInfo.getTargetSection().getTargetSchoolRank().getOptions());
                    toChooseTop.putExtra("ischange", true);
                    toChooseTop.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toChooseTop.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_TOP_SCHOOL);
                    toChooseTop.putExtra(TITLE, "修改目标学校");
                    startActivityForResult(toChooseTop, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_target_special:
                if (personalInfo != null && personalInfo.getTargetSection().getTargetMajorDirection() != null) {
                    Intent toChooseSpe = new Intent(this, ChooseListActivity.class);
                    toChooseSpe.putExtra("value", tv_target_special.getText());
                    toChooseSpe.putParcelableArrayListExtra("list", personalInfo.getTargetSection().getTargetMajorDirection().getOptions());
                    toChooseSpe.putExtra("ischange", true);
                    toChooseSpe.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toChooseSpe.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_MAJOR);
                    toChooseSpe.putExtra(TITLE, "修改专业方向");
                    startActivityForResult(toChooseSpe, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_cz_school:
                Intent toCzSchool = new Intent(this, CommonEditNameActivity.class);
                toCzSchool.putExtra("value", tv_cz_school.getText());
                toCzSchool.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_CZ);
                toCzSchool.putExtra(TITLE, "修改初中学校");
                startActivityForResult(toCzSchool, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.llyt_cz_score:
                if (personalInfo != null && personalInfo.getBackgroundSection().getScore() != null) {
                    Intent toCzScore = new Intent(this, ChooseListActivity.class);
                    toCzScore.putExtra("value", tv_cz_score.getText());
                    toCzScore.putParcelableArrayListExtra("list", personalInfo.getBackgroundSection().getScore().getOptions());
                    toCzScore.putExtra("ischange", true);
                    toCzScore.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toCzScore.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_CZ_SCORE);
                    toCzScore.putExtra(TITLE, "修改初中成绩");
                    startActivityForResult(toCzScore, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_gz_school:
                Intent toGzSchool = new Intent(this, CommonEditNameActivity.class);
                toGzSchool.putExtra("value", tv_gz_school.getText());
                toGzSchool.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_GZ);
                toGzSchool.putExtra("title", "修改高中学校");
                startActivityForResult(toGzSchool, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.llyt_gz_score:
                if (personalInfo != null && personalInfo.getBackgroundSection().getScore() != null) {
                    Intent toGzScore = new Intent(this, ChooseListActivity.class);
                    toGzScore.putExtra("value", tv_gz_score.getText());
                    toGzScore.putParcelableArrayListExtra("list", personalInfo.getBackgroundSection().getScore().getOptions());
                    toGzScore.putExtra("ischange", true);
                    toGzScore.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toGzScore.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_GZ_SCORE);
                    toGzScore.putExtra("title", "修改高中成绩");
                    startActivityForResult(toGzScore, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_bk_school:
                Intent toBkSchool = new Intent(this, CommonEditNameActivity.class);
                toBkSchool.putExtra("value", tv_bk_school.getText());
                toBkSchool.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_BK);
                toBkSchool.putExtra("title", "修改本科学校");
                startActivityForResult(toBkSchool, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                break;
            case R.id.llyt_bk_score:
                if (personalInfo != null && personalInfo.getBackgroundSection().getScore() != null) {
                    Intent toBkScore = new Intent(this, ChooseListActivity.class);
                    toBkScore.putExtra("value", tv_bk_score.getText());
                    toBkScore.putParcelableArrayListExtra("list", personalInfo.getBackgroundSection().getScore().getOptions());
                    toBkScore.putExtra("ischange", true);
                    toBkScore.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toBkScore.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_BK_SCORE);
                    toBkScore.putExtra("title", "修改本科成绩");
                    startActivityForResult(toBkScore, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_eg_score:
                if (personalInfo != null && personalInfo.getBackgroundSection().getScoreLanguage() != null) {
                    Intent toEgScore = new Intent(this, ChooseListActivity.class);
                    toEgScore.putExtra("value", tv_eg_score.getText());
                    toEgScore.putParcelableArrayListExtra("list", handleEgGroup(personalInfo.getBackgroundSection().getScoreLanguage().getGroupedOptions()));
                    toEgScore.putExtra("ischange", true);
                    toEgScore.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toEgScore.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_EG_SCORE);
                    toEgScore.putExtra("title", "修改语言成绩");
                    startActivityForResult(toEgScore, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_gre_score:
                if (personalInfo != null && personalInfo.getBackgroundSection().getScoreStandard() != null) {
                    Intent toGreScore = new Intent(this, ChooseListActivity.class);
                    toGreScore.putExtra("value", tv_gre_score.getText());
                    toGreScore.putParcelableArrayListExtra("list", handleStandardGroup(personalInfo.getBackgroundSection().getScoreStandard().getGroupedOptions()));
                    toGreScore.putExtra("ischange", true);
                    toGreScore.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toGreScore.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_GRE_SCORE);
                    toGreScore.putExtra("title", "修改标准化考试成绩");
                    startActivityForResult(toGreScore, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_sat_score:
                if (personalInfo != null && personalInfo.getBackgroundSection().getScoreStandard() != null) {
                    Intent toSatScore = new Intent(this, ChooseListActivity.class);
                    toSatScore.putExtra("value", tv_sat_score.getText());
                    toSatScore.putParcelableArrayListExtra("list", handleStandardGroup(personalInfo.getBackgroundSection().getScoreStandard().getGroupedOptions()));
                    toSatScore.putExtra("ischange", true);
                    toSatScore.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toSatScore.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_SAT_SCORE);
                    toSatScore.putExtra("title", "修改标准化考试成绩");
                    startActivityForResult(toSatScore, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_sx_event:
                if (personalInfo != null && personalInfo.getBackgroundSection().getActivityInternship() != null) {
                    Intent toSx = new Intent(this, ChooseListActivity.class);
                    toSx.putExtra("value", tv_sx_event.getText());
                    toSx.putParcelableArrayListExtra("list", personalInfo.getBackgroundSection().getActivityInternship().getOptions());
                    toSx.putExtra("ischange", true);
                    toSx.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toSx.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_SX_EVENT);
                    toSx.putExtra("title", "修改实习活动");
                    startActivityForResult(toSx, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_ky_event:
                if (personalInfo != null && personalInfo.getBackgroundSection().getActivityResearch() != null) {
                    Intent toKy = new Intent(this, ChooseListActivity.class);
                    toKy.putExtra("value", tv_ky_event.getText());
                    toKy.putParcelableArrayListExtra("list", personalInfo.getBackgroundSection().getActivityResearch().getOptions());
                    toKy.putExtra("ischange", true);
                    toKy.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toKy.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_KY_EVENT);
                    toKy.putExtra("title", "修改科研活动");
                    startActivityForResult(toKy, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_kw_event:
                if (personalInfo != null && personalInfo.getBackgroundSection().getActivityCommunity() != null) {
                    Intent toKw = new Intent(this, ChooseListActivity.class);
                    toKw.putExtra("value", tv_kw_event.getText());
                    toKw.putParcelableArrayListExtra("list", personalInfo.getBackgroundSection().getActivityCommunity().getOptions());
                    toKw.putExtra("ischange", true);
                    toKw.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toKw.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_KW_EVENT);
                    toKw.putExtra("title", "修改课外活动");
                    startActivityForResult(toKw, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_shehui_event:
                if (personalInfo != null && personalInfo.getBackgroundSection().getActivitySocial() != null) {
                    Intent toSocial = new Intent(this, ChooseListActivity.class);
                    toSocial.putExtra("value", socialIds);
                    toSocial.putParcelableArrayListExtra("list", personalInfo.getBackgroundSection().getActivitySocial().getOptions());
                    toSocial.putExtra("ischange", true);
                    toSocial.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toSocial.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_SHEHUI_EVENT);
                    toSocial.putExtra("title", "修改社会活动");
                    startActivityForResult(toSocial, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            case R.id.llyt_internal_event:
                if (personalInfo != null && personalInfo.getBackgroundSection().getActivityExchange() != null) {
                    Intent toGj = new Intent(this, ChooseListActivity.class);
                    toGj.putExtra("value", tv_internal_event.getText());
                    toGj.putParcelableArrayListExtra("list", personalInfo.getBackgroundSection().getActivityExchange().getOptions());
                    toGj.putExtra("ischange", true);
                    toGj.putExtra("from", ParameterUtils.TYPE_OPTIONS_PERSON);
                    toGj.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_GJ_EVENT);
                    toGj.putExtra(TITLE, "修改国际交流");
                    startActivityForResult(toGj, ParameterUtils.REQUEST_CODE_EDIT_MYINFO);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
        int statusBarHeight = ScreenUtils.getStatusHeight();
        int realTopHeight = statusBarHeight + topHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            topmyinfo.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, realTopHeight));
            view_bg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, realTopHeight));
            topmyinfo.setPadding(topmyinfo.getPaddingLeft(), statusBarHeight, topmyinfo.getPaddingRight(), 0);
        }
        if (!"MeFragment".equals(from)) {
            LinearLayout llMyInfo = findViewById(R.id.ll_my_info);
            LinearLayout.LayoutParams infoParams = (LinearLayout.LayoutParams) llMyInfo.getLayoutParams();
            infoParams.topMargin = realTopHeight;
            llMyInfo.setLayoutParams(infoParams);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CHANGEPHOTO:
                String temppath = data.getStringExtra("path");
                DisplayImageUtils.downloadImageFile(getApplicationContext(), temppath, new SimpleTarget<File>() {

                    @Override
                    public void onResourceReady(@NonNull File file, @Nullable Transition<? super File> transition) {
                        avatarP = new SetAvatarPresenter(MyInfoActivity.this);
                        avatarP.setAvatar(file);
                    }
                });
                break;
            case ParameterUtils.REQUEST_CODE_EDIT_MYINFO:
                String flag = data.getStringExtra(ParameterUtils.TRANSITION_FLAG);
                String value = data.getStringExtra("new_value");
                String result = data.getStringExtra("result");
                if (ParameterUtils.EDIT_NAME.equals(flag)) {
                    //更新缓存
                    SPCacheUtils.put(USER_NAME, value);
                    tvnickname.setText(value);
                } else if (ParameterUtils.EDIT_ABOARD_YEAR.equals(flag)) {
                    EventBus.getDefault().post(new Event.HomeFilterEvent(personalInfo != null
                        ? personalInfo.isAbroadPlanAvailable() : false));
                    tvabroadtime.setText(value);
                } else if (ParameterUtils.EDIT_INTENT.equals(flag)) {
                    getInfoSuccess(result);
                    EventBus.getDefault().post(new Event.HomeFilterEvent(personalInfo != null
                        ? personalInfo.isAbroadPlanAvailable() : false));
                    tvintent.setText(value);
                } else if (ParameterUtils.EDIT_APPLY_PROJ.equals(flag)) {
                    getInfoSuccess(result);
                    EventBus.getDefault().post(new Event.HomeFilterEvent(personalInfo != null
                        ? personalInfo.isAbroadPlanAvailable() : false));
                    tvapplyproject.setText(value);
                } else if (ParameterUtils.EDIT_APPLY_FEE.equals(flag)) {
                    tvFee.setText(value);
                } else if (ParameterUtils.EDIT_TOP_SCHOOL.equals(flag)) {
                    tv_target_school.setText(value);
                } else if (ParameterUtils.EDIT_MAJOR.equals(flag)) {
                    tv_target_special.setText(value);
                    getInfoSuccess(result);
                } else if (ParameterUtils.EDIT_CZ.equals(flag)) {
                    tv_cz_school.setText(value);
                } else if (ParameterUtils.EDIT_CZ_SCORE.equals(flag)) {
                    tv_cz_score.setText(value);
                } else if (ParameterUtils.EDIT_GZ.equals(flag)) {
                    tv_gz_school.setText(value);
                } else if (ParameterUtils.EDIT_GZ_SCORE.equals(flag)) {
                    tv_gz_score.setText(value);
                } else if (ParameterUtils.EDIT_BK.equals(flag)) {
                    tv_bk_school.setText(value);
                } else if (ParameterUtils.EDIT_BK_SCORE.equals(flag)) {
                    tv_bk_score.setText(value);
                } else if (ParameterUtils.EDIT_SX_EVENT.equals(flag)) {
                    tv_sx_event.setText(value);
                } else if (ParameterUtils.EDIT_KY_EVENT.equals(flag)) {
                    tv_ky_event.setText(value);
                } else if (ParameterUtils.EDIT_KW_EVENT.equals(flag)) {
                    tv_kw_event.setText(value);
                } else if (ParameterUtils.EDIT_GJ_EVENT.equals(flag)) {
                    tv_internal_event.setText(value);
                } else if (ParameterUtils.EDIT_SHEHUI_EVENT.equals(flag)) {
                    getInfoSuccess(result);
                } else if (ParameterUtils.EDIT_EG_SCORE.equals(flag)) {
                    tv_eg_score.setText(value);
                } else if (ParameterUtils.EDIT_GRE_SCORE.equals(flag)) {
                    tv_gre_score.setText(value);
                } else if (ParameterUtils.EDIT_SAT_SCORE.equals(flag)) {
                    tv_sat_score.setText(value);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(MyInfoContract.Presenter presenter) {
        if (presenter != null) {
            this.myP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (ParameterUtils.RESPONSE_CODE_NOLOGIN.equals(errCode)) {
            DialogCreator.createLoginDialog(this);
        }
        ToastUtils.showToast(message);
    }

    @Override
    public void getInfoSuccess(String jsonObject) {
        personalInfo = JSON.parseObject(jsonObject, PersonalInfo.class);
        if (personalInfo != null) {
            if (personalInfo.getAvatar() != null) {
                SPCacheUtils.put("user_pic", personalInfo.getAvatar());
                DisplayImageUtils.displayImage(getApplicationContext(), personalInfo.getAvatar(), kbv_user);
            }
            if (personalInfo.getName() != null) {
                SPCacheUtils.put(USER_NAME, personalInfo.getName());
                tvnickname.setText(personalInfo.getName());
            }
            if (personalInfo.getTargetSection() != null) {
                PersonalInfo.TargetSectionEntity targetSection = personalInfo.getTargetSection();
                if (targetSection != null) {
                    if (targetSection.getTargetSchoolRank() != null) {
                        tv_target_school.setText(targetSection.getTargetSchoolRank().getName());
                    }
                    if (targetSection.getTargetMajorDirection() != null) {
                        tv_target_special.setText(targetSection.getTargetMajorDirection().getName());
                        SPCacheUtils.put("target_major", JSON.toJSONString(targetSection.getTargetMajorDirection()));
                    }
                    if (targetSection.getTargetCountry() != null) {
                        SPCacheUtils.put("target_countryInfo", JSON.toJSONString(targetSection.getTargetCountry()));
                        if ("美国".equals(targetSection.getTargetCountry().getName())) {
                            findViewById(R.id.llyt_top_school).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.llyt_top_school).setVisibility(View.GONE);
                        }
                        tvintent.setText(targetSection.getTargetCountry().getName());
                    }
                    if (targetSection.getAdmissionTime() != null) {
                        tvabroadtime.setText(targetSection.getAdmissionTime().getName());
                    }
                    if (targetSection.getBudget() != null) {
                        tvFee.setText(targetSection.getBudget().getName());
                    }
                    if (targetSection.getTargetDegree() != null) {
                        String projName = targetSection.getTargetDegree().getName();
                        SPCacheUtils.put("project_name", projName);
                        if ("高中".equals(projName)) {
                            findViewById(R.id.llyt_top_school).setVisibility(View.GONE);
                            findViewById(R.id.llyt_bg).setVisibility(View.GONE);
                            findViewById(R.id.llyt_cz).setVisibility(View.VISIBLE);
                            findViewById(R.id.llyt_gz).setVisibility(View.GONE);
                            findViewById(R.id.llyt_bk).setVisibility(View.GONE);
                            findViewById(R.id.llyt_want_special).setVisibility(View.GONE);
                            findViewById(R.id.llyt_gmat).setVisibility(View.GONE);
                            findViewById(R.id.llyt_sat).setVisibility(View.GONE);
                        } else if ("本科".equals(projName)) {
                            findViewById(R.id.llyt_bg).setVisibility(View.VISIBLE);
                            findViewById(R.id.llyt_want_special).setVisibility(View.GONE);
                            findViewById(R.id.llyt_bk_event).setVisibility(View.VISIBLE);
                            findViewById(R.id.llyt_yjs_event).setVisibility(View.GONE);
                            findViewById(R.id.llyt_cz).setVisibility(View.GONE);
                            findViewById(R.id.llyt_gz).setVisibility(View.VISIBLE);
                            findViewById(R.id.llyt_bk).setVisibility(View.GONE);
                            findViewById(R.id.llyt_gmat).setVisibility(View.GONE);
                            findViewById(R.id.llyt_sat).setVisibility(View.VISIBLE);
                            ((TextView) findViewById(R.id.tv_bkscore_title)).setText("本科成绩");
                            ((TextView) findViewById(R.id.tv_bkschool_title)).setText("本科学校");
                        } else {
                            findViewById(R.id.llyt_bg).setVisibility(View.VISIBLE);
                            findViewById(R.id.llyt_want_special).setVisibility(View.VISIBLE);
                            findViewById(R.id.llyt_bk_event).setVisibility(View.GONE);
                            findViewById(R.id.llyt_yjs_event).setVisibility(View.VISIBLE);
                            findViewById(R.id.llyt_cz).setVisibility(View.GONE);
                            findViewById(R.id.llyt_gz).setVisibility(View.GONE);
                            findViewById(R.id.llyt_bk).setVisibility(View.VISIBLE);
                            findViewById(R.id.llyt_gmat).setVisibility(View.VISIBLE);
                            findViewById(R.id.llyt_sat).setVisibility(View.GONE);
                            if ("其他".equals(projName)) {
                                findViewById(R.id.llyt_gmat).setVisibility(View.GONE);
                                findViewById(R.id.llyt_sat).setVisibility(View.GONE);
                                ((TextView) findViewById(R.id.tv_bkscore_title)).setText("在读成绩");
                                ((TextView) findViewById(R.id.tv_bkschool_title)).setText("在读学校");
                            }
                        }
                        tvapplyproject.setText(projName);
                    }
                }
                PersonalInfo.BackgroundSectionEntity backgroundSection = personalInfo.getBackgroundSection();
                if (backgroundSection != null) {
                    if (backgroundSection.getCurrentSchool() != null) {
                        tv_cz_school.setText(backgroundSection.getCurrentSchool().getName());
                        tv_gz_school.setText(backgroundSection.getCurrentSchool().getName());
                        tv_bk_school.setText(backgroundSection.getCurrentSchool().getName());
                    }
                    if (backgroundSection.getScore() != null) {
                        tv_cz_score.setText(backgroundSection.getScore().getName());
                        tv_gz_score.setText(backgroundSection.getScore().getName());
                        tv_bk_score.setText(backgroundSection.getScore().getName());
                    }
                    if (backgroundSection.getScoreLanguage() != null) {
                        tv_eg_score.setText(backgroundSection.getScoreLanguage().getName());
                    }
                    if (backgroundSection.getScoreStandard() != null) {
                        tv_gre_score.setText(backgroundSection.getScoreStandard().getName());
                        tv_sat_score.setText(backgroundSection.getScoreStandard().getName());
                    } else {
                        findViewById(R.id.llyt_gmat).setVisibility(View.GONE);
                        findViewById(R.id.llyt_sat).setVisibility(View.GONE);
                    }
                    if (backgroundSection.getActivityInternship() != null) {
                        tv_sx_event.setText(backgroundSection.getActivityInternship().getName());
                    }
                    if (backgroundSection.getActivityResearch() != null) {
                        tv_ky_event.setText(backgroundSection.getActivityResearch().getName());
                    }
                    if (backgroundSection.getActivityCommunity() != null) {
                        tv_kw_event.setText(backgroundSection.getActivityCommunity().getName());
                    }
                    if (backgroundSection.getActivityExchange() != null) {
                        tv_internal_event.setText(backgroundSection.getActivityExchange().getName());
                    }
                    if (backgroundSection.getActivitySocial() != null) {
                        tv_shehui_event.setText(backgroundSection.getActivitySocial().getName());
                        socialIds = backgroundSection.getActivitySocial().getId();
                    }
                }
            }
        }
    }

    @Override
    public void sendSuccess() {
        // 发送个人信息成功
        EventBus.getDefault().post(new Event.CompleteInfoEvent());
        finish();
    }

    @Override
    public void setAvatarSuccess(String jsonObject, File avatar) {
        JSONObject data = JSON.parseObject(jsonObject);
        if (data != null) {
            String avatarUrl = data.getString("avatar");
            DisplayImageUtils.displayImage(getApplicationContext(), avatar.getAbsolutePath(), kbv_user);
            SPCacheUtils.put("user_pic", avatarUrl);
        }
    }

    @Override
    public void setAvatarFailed(String msg) {
        ToastUtils.showToast(msg);
    }

    private ArrayList handleEgGroup(List<PersonalInfo.BackgroundSectionEntity.ScoreLanguageEntity.GroupedOptionsEntity> list) {
        ArrayList<IdNameInfo> data = new ArrayList<>();
        for (PersonalInfo.BackgroundSectionEntity.ScoreLanguageEntity.GroupedOptionsEntity entity : list) {
            entity.getOptions().get(0).setTop(true);
            data.addAll(entity.getOptions());
        }
        return data;
    }

    private ArrayList handleStandardGroup(List<PersonalInfo.BackgroundSectionEntity.ScoreStandardEntity.GroupedOptionsEntity> list) {
        ArrayList<IdNameInfo> data = new ArrayList<>();
        for (PersonalInfo.BackgroundSectionEntity.ScoreStandardEntity.GroupedOptionsEntity entity : list) {
            entity.getOptions().get(0).setTop(true);
            data.addAll(entity.getOptions());
        }
        return data;
    }

    private void setArrowGone() {
        findViewById(R.id.iv_nickname).setVisibility(View.GONE);
        findViewById(R.id.iv_abroad).setVisibility(View.GONE);
        findViewById(R.id.iv_intent).setVisibility(View.GONE);
        findViewById(R.id.iv_apply).setVisibility(View.GONE);
        findViewById(R.id.iv_fee).setVisibility(View.GONE);
        findViewById(R.id.iv_school).setVisibility(View.GONE);
        findViewById(R.id.iv_spe).setVisibility(View.GONE);
        findViewById(R.id.iv_gz).setVisibility(View.GONE);
        findViewById(R.id.iv_gz_score).setVisibility(View.GONE);
        findViewById(R.id.iv_cz).setVisibility(View.GONE);
        findViewById(R.id.iv_cz_score).setVisibility(View.GONE);
        findViewById(R.id.iv_bk).setVisibility(View.GONE);
        findViewById(R.id.iv_bk_score).setVisibility(View.GONE);
        findViewById(R.id.iv_score).setVisibility(View.GONE);
        findViewById(R.id.iv_gre).setVisibility(View.GONE);
        findViewById(R.id.iv_act).setVisibility(View.GONE);
        findViewById(R.id.iv_sx).setVisibility(View.GONE);
        findViewById(R.id.iv_ky).setVisibility(View.GONE);
        findViewById(R.id.iv_kw).setVisibility(View.GONE);
        findViewById(R.id.iv_sh).setVisibility(View.GONE);
        findViewById(R.id.iv_gj).setVisibility(View.GONE);
    }


}

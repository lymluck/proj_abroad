package com.smartstudy.xxd.ui.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.listener.ShareListener;
import com.smartstudy.commonlib.entity.NewsInfo;
import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.PullParallaxScrollView;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.sdk.utils.UMShareUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.PersonalInfo;
import com.smartstudy.xxd.entity.SchoolDegreeInfo;
import com.smartstudy.xxd.entity.SchoolIntroInfo;
import com.smartstudy.xxd.listener.PostErrDialogClickListener;
import com.smartstudy.xxd.mvp.contract.CollegeDetailContract;
import com.smartstudy.xxd.mvp.presenter.CollegeDetailPresenter;
import com.smartstudy.xxd.ui.dialog.PostSchoolErrDialog;
import com.smartstudy.xxd.utils.AppContants;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;
import static com.smartstudy.xxd.utils.AppContants.USER_ACCOUNT;
import static com.smartstudy.xxd.utils.AppContants.USER_NAME;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

@Route("CollegeDetailActivity")
public class CollegeDetailActivity extends BaseActivity implements CollegeDetailContract.View {

    private RelativeLayout topSchoolDetail;
    private ImageView topLeftBtn;
    private PullParallaxScrollView ppsvSchoolDetail;
    private TextView topTitle;
    private TextView tvAdd;
    private View topLine;
    private Dialog mDialog;
    private View bkView;
    private View yjsView;
    private View siaView;

    private int top_alpha = 0;
    private boolean isWhite = true;
    private String schoolId;
    private String schoolName;
    private String schoolIntro;
    private String countryId;
    private String logo;
    private boolean selected;
    private boolean isTrue;
    private CollegeDetailContract.Presenter detailP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_detail);
        UApp.actionEvent(this, "10_B_school_detail");
    }

    @Override
    protected void onResume() {
        super.onResume();
        topSchoolDetail.setBackgroundColor(Color.argb(top_alpha, 255, 255, 255));
    }

    @Override
    protected void onDestroy() {
        if (detailP != null) {
            detailP = null;
        }
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    @Override
    protected void initViewAndData() {
        this.topSchoolDetail = findViewById(R.id.top_schooldetail);
        this.tvAdd = findViewById(R.id.tv_add);
        this.ppsvSchoolDetail = findViewById(R.id.rsl_schooldetail);
        ppsvSchoolDetail.setHeader(findViewById(R.id.rylt_cover));
        this.topLeftBtn = findViewById(R.id.topdefault_leftbutton);
        this.topLeftBtn.setImageResource(R.drawable.ic_go_back_white);
        this.topTitle = findViewById(R.id.topdefault_centertitle);
        this.topLine = findViewById(R.id.top_line);
        topTitle.setVisibility(View.GONE);
        initDirection();
        schoolId = getIntent().getExtras().getString("id");
        initTitleBar();
        new CollegeDetailPresenter(this);
        detailP.getSchoolIntro(schoolId);
        detailP.getSchoolDetail(schoolId);
    }

    /**
     * 初始化本科、研究生、艺术院校视图
     */
    private void initDirection() {
        if (mInflater != null) {
            bkView = mInflater.inflate(R.layout.layout_collegedetail_bk, null);
            bkView.findViewById(R.id.tv_bk_err).setOnClickListener(this);
            yjsView = mInflater.inflate(R.layout.layout_collegedetail_yjs, null);
            yjsView.findViewById(R.id.tv_yjs_err).setOnClickListener(this);
            siaView = mInflater.inflate(R.layout.layout_collegedetail_sia, null);
            siaView.findViewById(R.id.tv_sia_err).setOnClickListener(this);
            LinearLayout llDirection = findViewById(R.id.ll_direction);
            String projectName = (String) SPCacheUtils.get("project_name", "");
            String strMajor = (String) SPCacheUtils.get("target_major", "");
            PersonalInfo.TargetSectionEntity.TargetMajorDirectionEntity major = JSON.parseObject(strMajor,
                PersonalInfo.TargetSectionEntity.TargetMajorDirectionEntity.class);
            // 艺术院校
            if ("研究生".equals(projectName)) {
                if (major != null && "ARTS".equals(major.getId())) {
                    llDirection.addView(siaView, -1);
                    llDirection.addView(yjsView, -1);
                    llDirection.addView(bkView, -1);
                } else {
                    llDirection.addView(yjsView, -1);
                    llDirection.addView(bkView, -1);
                    llDirection.addView(siaView, -1);
                }
            } else {
                llDirection.addView(bkView, -1);
                llDirection.addView(yjsView, -1);
                llDirection.addView(siaView, -1);
            }
        }
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View viewBg = findViewById(R.id.view_bg);
            int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
            int statusBarHeight = ScreenUtils.getStatusHeight();
            topSchoolDetail.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight + topHeight));
            viewBg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight + topHeight));
            topSchoolDetail.setPadding(topSchoolDetail.getPaddingLeft(), statusBarHeight,
                topSchoolDetail.getPaddingRight(), 0);
        }
    }

    @Override
    public void initEvent() {
        //标题栏滑出背景图片过程中颜色渐变
        ppsvSchoolDetail.setScrollViewListener(new PullParallaxScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(PullParallaxScrollView scrollView, int x, int y, int oldx, int oldy) {
                int diff = ppsvSchoolDetail.getHeaderVisibleHeight() - topSchoolDetail.getHeight();
                if (y > 0 && y <= diff) {
                    top_alpha = y * 255 / diff;
                    topTitle.setVisibility(View.GONE);
                    topLine.setVisibility(View.GONE);
                } else if (y > diff) {
                    top_alpha = 255;
                    topTitle.setVisibility(View.VISIBLE);
                    topLine.setVisibility(View.VISIBLE);
                } else {
                    top_alpha = 0;
                    topTitle.setVisibility(View.GONE);
                    topLine.setVisibility(View.GONE);
                }
                //翻转动画
                if (top_alpha > 128) {
                    // alpha > 0.5设置绿色图标
                    if (isWhite) {
                        topLeftBtn.setImageResource(R.drawable.ic_go_back);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(topLeftBtn, "alpha", 0.5f, 1);
                        animator.setDuration(1000);
                        animator.start();
                    }
                    isWhite = false;
                } else { // 否则设置白色
                    if (!isWhite) {
                        topLeftBtn.setImageResource(R.drawable.ic_go_back_white);
                        ObjectAnimator animator = ObjectAnimator.ofFloat(topLeftBtn, "alpha", 0.5f, 1);
                        animator.setDuration(1000);
                        animator.start();
                    }
                    isWhite = true;
                }
                topSchoolDetail.setBackgroundColor(Color.argb(top_alpha, 255, 255, 255));
            }
        });
        topLeftBtn.setOnClickListener(this);
        findViewById(R.id.ll_more_intro).setOnClickListener(this);
        findViewById(R.id.ll_more_bk).setOnClickListener(this);
        findViewById(R.id.ll_more_yjs).setOnClickListener(this);
        findViewById(R.id.ll_more_sia).setOnClickListener(this);
        findViewById(R.id.ll_more_news).setOnClickListener(this);
        findViewById(R.id.ll_more_qa).setOnClickListener(this);
        findViewById(R.id.tv_share).setOnClickListener(this);
        findViewById(R.id.tv_intro_err).setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        findViewById(R.id.tv_ask).setOnClickListener(this);
        findViewById(R.id.tv_rate).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                Intent data = new Intent();
                data.putExtra("isTrue", isTrue);
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.ll_more_intro:
                UApp.actionEvent(this, "10_A_intro_more_btn");
                //更多简介
                String url_intro = String.format(HttpUrlUtils.getWebUrl(
                    HttpUrlUtils.WEBURL_SCHOOL_INTRO), schoolId);
                goWebMoreInfo(url_intro);
                break;
            case R.id.ll_more_bk:
                UApp.actionEvent(this, "10_A_undergraduate_more_btn");
                //更多本科申请
                String url_bk = String.format(HttpUrlUtils.getWebUrl(
                    HttpUrlUtils.WEBURL_SCHOOL_UNDERGRADUATE), schoolId);
                goWebMoreInfo(url_bk);
                break;
            case R.id.ll_more_yjs:
                UApp.actionEvent(this, "10_A_graduate_more_btn");
                //更多研究生申请
                String url_yjs = String.format(HttpUrlUtils.getWebUrl(
                    HttpUrlUtils.WEBURL_SCHOOL_GRADUATE), schoolId);
                goWebMoreInfo(url_yjs);
                break;
            case R.id.ll_more_sia:
                UApp.actionEvent(this, "10_A_art_more_btn");
                //更多艺术生申请
                String url_sia = String.format(HttpUrlUtils.getWebUrl(
                    HttpUrlUtils.WEBURL_SCHOOL_SIA), schoolId);
                goWebMoreInfo(url_sia);
                break;
            case R.id.ll_more_news:
                UApp.actionEvent(this, "10_A_news_more_btn");
                //更多资讯
                startActivity(new Intent(this, NewsActivity.class)
                    .putExtra("schoolId", schoolId)
                    .putExtra(TITLE, schoolName + "资讯"));
                break;
            case R.id.ll_more_qa:
                UApp.actionEvent(this, "10_A_qustion_more_btn");
                //更多问答
                Bundle qa_data = new Bundle();
                qa_data.putString("data_flag", "school");
                qa_data.putString("schoolId", schoolId);
                startActivity(new Intent(this, QaListActivity.class)
                    .putExtras(qa_data));
                break;
            case R.id.tv_share:
                UApp.actionEvent(this, "10_A_share_btn");
                String webUrl = HttpUrlUtils.getWebUrl(String.format(HttpUrlUtils.WEBURL_SCHOOL_DETAIL, schoolId));
                UMShareUtils.showShare(this, webUrl, schoolName, schoolIntro, logo,
                    new ShareListener(webUrl, "school_detail"));
                break;
            case R.id.tv_add:
                if (selected) {
                    detailP.deleteMyChoose(schoolId);
                } else {
                    UApp.actionEvent(this, "10_A_add_btn");
                    showDialog(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.llyt_top:
                                    detailP.add2MySchool(ParameterUtils.MATCH_TOP, schoolId);
                                    mDialog.dismiss();
                                    break;
                                case R.id.llyt_mid:
                                    detailP.add2MySchool(ParameterUtils.MATCH_MID, schoolId);
                                    mDialog.dismiss();
                                    break;
                                case R.id.llyt_bottom:
                                    detailP.add2MySchool(ParameterUtils.MATCH_BOT, schoolId);
                                    mDialog.dismiss();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                }
                break;
            case R.id.tv_ask:
                UApp.actionEvent(this, "10_A_consul_btn");
                HashMap<String, String> clientInfo = new HashMap<>();
                clientInfo.put(AppContants.NAME, (String) SPCacheUtils.get(USER_NAME, ""));
                clientInfo.put(AppContants.TEL, (String) SPCacheUtils.get(USER_ACCOUNT, ""));
                Intent intent = new MQIntentBuilder(this)
                    .setClientInfo(clientInfo)
                    .build();
                startActivity(intent);
                break;
            case R.id.tv_rate:
                UApp.actionEvent(this, "10_A_acceptance_rate_btn");
                Bundle bundle = new Bundle();
                bundle.putString("schoolId", schoolId);
                bundle.putString("countryId", countryId);
                Intent toTest = new Intent(this, SrtRateTestActivity.class);
                toTest.putExtras(bundle);
                startActivity(toTest);
                break;
            case R.id.tv_intro_err:
                postErr("学校简介");
                break;
            case R.id.tv_bk_err:
                postErr("本科申请");
                break;
            case R.id.tv_yjs_err:
                postErr("研究生申请");
                break;
            case R.id.tv_sia_err:
                postErr("艺术生申请");
                break;
            default:
                break;
        }
    }

    private void postErr(final String type) {
        new PostSchoolErrDialog(this, new PostErrDialogClickListener() {
            @Override
            public void onClick(String content) {
                detailP.postErr(schoolId, type, content);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("isTrue", isTrue);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void setPresenter(CollegeDetailContract.Presenter presenter) {
        if (presenter != null) {
            detailP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void showInfo(SchoolIntroInfo info) {
        DisplayImageUtils.formatImgUrlNoHolder(this, info.getCoverPicture(), (ImageView) findViewById(R.id.iv_school_cover));
        ImageView iv_school_logo = (ImageView) findViewById(R.id.iv_school_logo);
        logo = info.getLogo();
        DisplayImageUtils.formatCircleImgUrl(this, logo, iv_school_logo);
        if (TextUtils.isEmpty(info.getLocalRank()) && TextUtils.isEmpty(info.getWorldRank())) {
            findViewById(R.id.llyt_local_rank).setVisibility(View.GONE);
            findViewById(R.id.llyt_world_rank).setVisibility(View.GONE);
        } else {
            TextView tv_local_rank = (TextView) findViewById(R.id.tv_local_rank);
            TextView tv_internal_rank = (TextView) findViewById(R.id.tv_internal_rank);
            tv_local_rank.setText(TextUtils.isEmpty(info.getLocalRank()) ? "暂无" : info.getLocalRank());
            tv_internal_rank.setText(TextUtils.isEmpty(info.getWorldRank()) ? "暂无" : info.getWorldRank());
            findViewById(R.id.llyt_local_rank).setVisibility(View.VISIBLE);
            findViewById(R.id.llyt_world_rank).setVisibility(View.VISIBLE);
        }
        TextView tv_schoolname = (TextView) findViewById(R.id.tv_schoolname);
        schoolName = info.getChineseName();
        tv_schoolname.setText(schoolName);
        topTitle.setText(info.getChineseName());
        TextView tv_egname = (TextView) findViewById(R.id.tv_egname);
        tv_egname.setText(info.getEnglishName());
        TextView tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_addr.setText(info.getCityPath());
    }

    @Override
    public void showIntro(String intro, String countryId, boolean selected) {
        this.countryId = countryId;
        this.selected = selected;
        if (selected) {
            tvAdd.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_choose_blue, 0, 0);
        } else {
            tvAdd.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_add_school, 0, 0);
        }
        TextView tv_school_intro = (TextView) findViewById(R.id.tv_school_intro);
        schoolIntro = intro;
        tv_school_intro.setText(intro);
        //获取到数据隐藏加载loading
        findViewById(R.id.llyt_loading).setVisibility(View.GONE);
        findViewById(R.id.detail_intro).setVisibility(View.VISIBLE);
        findViewById(R.id.llyt_bot_bar).setVisibility(View.VISIBLE);
    }

    @Override
    public void addSuccess() {
        isTrue = true;
        this.selected = true;
        tvAdd.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_choose_blue, 0, 0);
    }

    @Override
    public void delSuccess() {
        isTrue = true;
        this.selected = false;
        tvAdd.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_add_school, 0, 0);
    }

    @Override
    public void showBk(final List<SchoolDegreeInfo> data) {
        if (data.size() > 0 && bkView != null) {
            bkView.setVisibility(View.VISIBLE);
            RecyclerView rlv_bk = bkView.findViewById(R.id.rcv_bk);
            showDegree(rlv_bk, data, R.color.app_main_color, R.drawable.bg_oval_blue_size);
        }
    }

    @Override
    public void showYjs(List<SchoolDegreeInfo> data) {
        if (data.size() > 0 && yjsView != null) {
            yjsView.setVisibility(View.VISIBLE);
            RecyclerView rcv_yjs = yjsView.findViewById(R.id.rcv_yjs);
            showDegree(rcv_yjs, data, R.color.bg_yjs_color, R.drawable.bg_oval_red_size);
        }
    }

    @Override
    public void showSia(List<SchoolDegreeInfo> data) {
        if (data.size() > 0 && siaView != null) {
            siaView.setVisibility(View.VISIBLE);
            RecyclerView rcv_yjs = siaView.findViewById(R.id.rcv_sia);
            showDegree(rcv_yjs, data, R.color.school_sia, R.drawable.bg_oval_purple_size);
        }
    }

    private void showDegree(RecyclerView recyclerView, final List<SchoolDegreeInfo> data, final int viewColor,
                            final int viewBackground) {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f))
            .colorResId(R.color.horizontal_line_color).build());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new NoScrollLinearLayoutManager(this));
        recyclerView.setAdapter(new CommonAdapter<SchoolDegreeInfo>(this, R.layout.item_schooldetail_application, data) {
            @Override
            protected void convert(ViewHolder holder, SchoolDegreeInfo info, int position) {
                TextView tv_left = holder.getView(R.id.tv_left);
                TextView tv_left_bot = holder.getView(R.id.tv_left_bot);
                TextView tv_right = holder.getView(R.id.tv_right);
                TextView tv_right_bot = holder.getView(R.id.tv_right_bot);
                View view_line = holder.getView(R.id.view_line);
                View viewDeleteLeft = holder.getView(R.id.v_delete_left);
                View viewDeleteRight = holder.getView(R.id.v_delete_right);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_line.getLayoutParams();
                if (position == 0) {
                    tv_left.setTextColor(getResources().getColor(viewColor));
                    tv_left_bot.setTextColor(getResources().getColor(viewColor));
                    tv_right.setTextColor(getResources().getColor(viewColor));
                    tv_right_bot.setTextColor(getResources().getColor(viewColor));
                }
                view_line.setLayoutParams(params);
                if (!TextUtils.isEmpty(info.getLeftValue())) {
                    tv_left.setText(info.getLeftValue());
                    tv_left_bot.setText(info.getLeftName());
                    viewDeleteLeft.setVisibility(View.GONE);
                } else {
                    viewDeleteLeft.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(info.getRightValue())) {
                    tv_right.setText(info.getRightValue());
                    tv_right_bot.setText(info.getRightName());
                    viewDeleteRight.setVisibility(View.GONE);
                } else {
                    viewDeleteRight.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void showNews(List<NewsInfo> data) {
        if (data.size() > 0) {
            findViewById(R.id.detail_news).setVisibility(View.VISIBLE);
            RecyclerView rlv_news = (RecyclerView) findViewById(R.id.rlv_news);
            rlv_news.setHasFixedSize(true);
            rlv_news.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16), 0).colorResId(R.color.horizontal_line_color).build());
            rlv_news.setNestedScrollingEnabled(false);
            rlv_news.setLayoutManager(new NoScrollLinearLayoutManager(this));
            rlv_news.setAdapter(new CommonAdapter<NewsInfo>(this, R.layout.item_news_list, data) {
                @Override
                protected void convert(ViewHolder holder, final NewsInfo newsInfo, int position) {
                    if (newsInfo != null) {
                        ImageView iv_cover = holder.getView(R.id.iv_cover);
                        if (TextUtils.isEmpty(newsInfo.getCoverUrl())) {
                            iv_cover.setVisibility(View.GONE);
                        } else {
                            iv_cover.setVisibility(View.VISIBLE);
                            holder.setImageUrl(iv_cover, newsInfo.getCoverUrl(), true);
                        }
                        holder.setText(R.id.tv_title, newsInfo.getTitle());
                        TextView tv_tag = holder.getView(R.id.tv_tag);
                        if (TextUtils.isEmpty(newsInfo.getTags())) {
                            tv_tag.setVisibility(View.GONE);
                        } else {
                            tv_tag.setVisibility(View.VISIBLE);
                            tv_tag.setText(newsInfo.getTags());
                        }
                        if (TextUtils.isEmpty(newsInfo.getVisitCount())) {
                            holder.setText(R.id.tv_see_num, String.format(getString(R.string.news_see), "0"));
                        } else {
                            holder.setText(R.id.tv_see_num, String.format(getString(R.string.news_see), newsInfo.getVisitCount()));
                        }
                        holder.getView(R.id.llyt_news).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent toMoreDetails = new Intent(CollegeDetailActivity.this, ShowWebViewActivity.class);
                                toMoreDetails.putExtra(WEBVIEW_URL, String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_NEWS_DETAIL), newsInfo.getId()));
                                toMoreDetails.putExtra(TITLE, newsInfo.getTitle());
                                toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
                                startActivity(toMoreDetails);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void showQa(List<QuestionInfo> data) {
        if (data.size() > 0) {
            findViewById(R.id.detail_qa).setVisibility(View.VISIBLE);
            RecyclerView rlv_qa = (RecyclerView) findViewById(R.id.rlv_qa);
            rlv_qa.setHasFixedSize(true);
            int marginlr = DensityUtils.dip2px(16);
            rlv_qa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).margin(marginlr, marginlr).colorResId(R.color.horizontal_line_color).build());
            rlv_qa.setNestedScrollingEnabled(false);
            rlv_qa.setLayoutManager(new NoScrollLinearLayoutManager(this));
            rlv_qa.setAdapter(new CommonAdapter<QuestionInfo>(this, R.layout.item_qa_list, data) {
                @Override
                protected void convert(ViewHolder holder, final QuestionInfo questionInfo, int position) {
                    if (questionInfo != null) {
                        String avatar = questionInfo.getAsker().getAvatar();
                        String askName = questionInfo.getAsker().getName();
                        TextView tv_default_name = holder.getView(R.id.tv_default_name);
                        holder.setPersonImageUrl(R.id.iv_asker, avatar, true);
                        tv_default_name.setVisibility(View.GONE);
                        holder.setText(R.id.tv_asker_name, askName);
                        holder.setText(R.id.tv_time, questionInfo.getCreateTimeText());
                        holder.setText(R.id.tv_question, questionInfo.getContent());
                        holder.setText(R.id.tv_see, String.format(getString(R.string.visit_count), questionInfo.getVisitCount()));
                        holder.setText(R.id.tv_answer_count, questionInfo.getAnswerCount() + " 回答");
                        if (TextUtils.isEmpty(questionInfo.getTargetCountryName())
                            && TextUtils.isEmpty(questionInfo.getTargetDegreeName())) {
                            holder.getView(R.id.ll_tag).setVisibility(View.GONE);
                        } else {
                            holder.getView(R.id.ll_tag).setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(questionInfo.getTargetCountryName())) {
                                holder.getView(R.id.tv_country).setVisibility(View.GONE);
                            } else {
                                holder.getView(R.id.tv_country).setVisibility(View.VISIBLE);
                                holder.setText(R.id.tv_country, questionInfo.getTargetCountryName());
                            }
                            if (TextUtils.isEmpty(questionInfo.getTargetDegreeName())) {
                                holder.getView(R.id.tv_degree).setVisibility(View.GONE);
                            } else {
                                holder.getView(R.id.tv_degree).setVisibility(View.VISIBLE);
                                holder.setText(R.id.tv_degree, questionInfo.getTargetDegreeName());
                            }
                        }
                        holder.getView(R.id.llyt_qa).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent toMoreDetails = new Intent(CollegeDetailActivity.this, QaDetailActivity.class);
                                toMoreDetails.putExtra("id", questionInfo.getId() + "");
                                startActivity(toMoreDetails);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void postSuccess() {
        showTip(null, "提交成功！");
    }

    private void showDialog(View.OnClickListener listener) {
        mDialog = DialogCreator.createAdd2SchoolDialog(this, "添加为",
            listener);
        mDialog.getWindow().setLayout((int) (0.85 * ScreenUtils.getScreenWidth()),
            WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }

    private void goWebMoreInfo(String url) {
        Intent toMoreDetails = new Intent(this, ShowWebViewActivity.class);
        toMoreDetails.putExtra(WEBVIEW_URL, url);
        toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
        startActivity(toMoreDetails);
    }

    /**
     * 涉及到分享时必须调用到方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}

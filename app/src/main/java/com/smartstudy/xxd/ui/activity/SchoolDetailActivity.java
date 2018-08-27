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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.tools.SystemBarTintManager;
import com.smartstudy.commonlib.entity.NewsInfo;
import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customView.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customView.ReboundScrollView;
import com.smartstudy.commonlib.ui.customView.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ShareUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.SchoolDegreeInfo;
import com.smartstudy.xxd.entity.SchoolIntroInfo;
import com.smartstudy.xxd.mvp.contract.SchoolDetailContract;
import com.smartstudy.xxd.mvp.presenter.SchoolDetailPresenter;

import java.util.HashMap;
import java.util.List;

@Route("SchoolDetailActivity")
public class SchoolDetailActivity extends UIActivity implements SchoolDetailContract.View {

    private RelativeLayout top_schooldetail;
    private View view_bg;
    private ImageView topdefault_leftbutton;
    private ImageView iv_school_cover;
    private ReboundScrollView rsl_schooldetail;
    private TextView topdefault_centertitle;
    private TextView tv_add;
    private View top_line;
    private Dialog mDialog;

    private SystemBarTintManager tintManager;
    private int top_alpha = 0;
    private boolean isWhite = true;
    private String schoolId;
    private String schoolName;
    private String schoolIntro;
    private String countryId;
    private String logo;
    private boolean selected;
    private boolean isTrue;
    private SchoolDetailContract.Presenter detailP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_detail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //复位标题栏和状态栏
        initSysBar();
        top_schooldetail.setBackgroundColor(Color.argb(top_alpha, 255, 255, 255));
    }

    @Override
    protected void onDestroy() {
        if (tintManager != null) {
            tintManager = null;
        }
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
        this.top_schooldetail = (RelativeLayout) findViewById(R.id.top_schooldetail);
        this.view_bg = findViewById(R.id.view_bg);
        this.tv_add = (TextView) findViewById(R.id.tv_add);
        this.rsl_schooldetail = (ReboundScrollView) findViewById(R.id.rsl_schooldetail);
        this.iv_school_cover = (ImageView) findViewById(R.id.iv_school_cover);
        this.topdefault_leftbutton = (ImageView) findViewById(R.id.topdefault_leftbutton);
        this.topdefault_leftbutton.setImageResource(R.drawable.ic_go_back_white);
        this.topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        this.top_line = findViewById(R.id.top_line);
        topdefault_centertitle.setVisibility(View.GONE);
        schoolId = getIntent().getExtras().getString("id");
        initSysBar();
        initTitleBar();
        new SchoolDetailPresenter(this);
        detailP.getSchoolIntro(schoolId);
        detailP.getSchoolDetail(schoolId);
    }

    /**
     * 初始化状态栏
     */
    private void initSysBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (tintManager == null) {
                tintManager = new SystemBarTintManager(this);
            }
            setTranslucentStatus(true);
            tintManager.setStatusBarLightMode(this, true);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.app_top_color);
            tintManager.setStatusBarAlpha(top_alpha);
        }
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            top_schooldetail.setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
            int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
            int statusBarHeight = ScreenUtils.getStatusHeight(getApplicationContext());
            top_schooldetail.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + topHeight));
            view_bg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + topHeight));
            top_schooldetail.setPadding(top_schooldetail.getPaddingLeft(), statusBarHeight, top_schooldetail.getPaddingRight(), 0);
        }
    }

    @Override
    public void initEvent() {
        //标题栏滑出背景图片过程中颜色渐变
        rsl_schooldetail.setScrollViewListener(new ReboundScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ReboundScrollView scrollView, int x, int y, int oldx, int oldy) {
                int diff = iv_school_cover.getHeight() - top_schooldetail.getHeight();
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
                if (top_alpha > 128) { // alpha > 0.5设置绿色图标
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    tintManager.setStatusBarAlpha(top_alpha);
                }
                top_schooldetail.setBackgroundColor(Color.argb(top_alpha, 255, 255, 255));
            }
        });
        topdefault_leftbutton.setOnClickListener(this);
        findViewById(R.id.tv_more_intro).setOnClickListener(this);
        findViewById(R.id.tv_more_intro).setOnClickListener(this);
        findViewById(R.id.tv_more_bk).setOnClickListener(this);
        findViewById(R.id.tv_more_yjs).setOnClickListener(this);
        findViewById(R.id.tv_more_sia).setOnClickListener(this);
        findViewById(R.id.tv_more_news).setOnClickListener(this);
        findViewById(R.id.tv_more_qa).setOnClickListener(this);
        findViewById(R.id.tv_share).setOnClickListener(this);
        tv_add.setOnClickListener(this);
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
            case R.id.tv_more_intro:
                //更多简介
                String url_intro = String.format(HttpUrlUtils.getWebUrl(
                        HttpUrlUtils.URL_SCHOOL_INTRO), schoolId);
                goWebMoreInfo(url_intro);
                break;
            case R.id.tv_more_bk:
                //更多本科申请
                String url_bk = String.format(HttpUrlUtils.getWebUrl(
                        HttpUrlUtils.URL_SCHOOL_UNDERGRADUATE), schoolId);
                goWebMoreInfo(url_bk);
                break;
            case R.id.tv_more_yjs:
                //更多研究生申请
                String url_yjs = String.format(HttpUrlUtils.getWebUrl(
                        HttpUrlUtils.URL_SCHOOL_GRADUATE), schoolId);
                goWebMoreInfo(url_yjs);
                break;
            case R.id.tv_more_sia:
                //更多艺术生申请
                String url_sia = String.format(HttpUrlUtils.getWebUrl(
                        HttpUrlUtils.URL_SCHOOL_SIA), schoolId);
                goWebMoreInfo(url_sia);
                break;
            case R.id.tv_more_news:
                //更多资讯
                startActivity(new Intent(this, NewsActivity.class)
                        .putExtra("schoolId", schoolId)
                        .putExtra("title", schoolName + "资讯"));
                break;
            case R.id.tv_more_qa:
                //更多问答
                Bundle qa_data = new Bundle();
                qa_data.putString("data_flag", "school");
                qa_data.putString("schoolId", schoolId);
                startActivity(new Intent(this, QaListActivity.class)
                        .putExtras(qa_data));
                break;
            case R.id.tv_share:
                String webUrl = HttpUrlUtils.getWebUrl(String.format(HttpUrlUtils.URL_SCHOOL_DETAIL_WEB, schoolId));
                ShareUtils.showShare(this, webUrl, schoolName, schoolIntro, logo, null);
                break;
            case R.id.tv_add:
                if (selected) {
                    detailP.deleteMyChoose(schoolId);
                } else {
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
                            }
                        }
                    });
                }
                break;
            case R.id.tv_ask:
                HashMap<String, String> clientInfo = new HashMap<>();
                clientInfo.put("name", (String) SPCacheUtils.get("user_name", ""));
                clientInfo.put("tel", (String) SPCacheUtils.get("user_account", ""));
                Intent intent = new MQIntentBuilder(this)
                        .setClientInfo(clientInfo)
                        .build();
                startActivity(intent);
                break;
            case R.id.tv_rate:
                Bundle bundle = new Bundle();
                bundle.putString("schoolId", schoolId);
                bundle.putString("countryId", countryId);
                Intent toTest = new Intent(this, SrtRateTestActivity.class);
                toTest.putExtras(bundle);
                startActivity(toTest);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("isTrue", isTrue);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void setPresenter(SchoolDetailContract.Presenter presenter) {
        if (presenter != null) {
            detailP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void showInfo(SchoolIntroInfo info) {
        ImageView iv_school_cover = (ImageView) findViewById(R.id.iv_school_cover);
        DisplayImageUtils.formatImgUrlNoHolder(this, info.getCoverPicture(), iv_school_cover);
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
        topdefault_centertitle.setText(info.getChineseName());
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
            tv_add.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_choose_blue, 0, 0);
        } else {
            tv_add.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_add_school, 0, 0);
        }
        TextView tv_school_intro = (TextView) findViewById(R.id.tv_school_intro);
        schoolIntro = intro;
        tv_school_intro.setText(intro);
        findViewById(R.id.llyt_loading).setVisibility(View.GONE);
        findViewById(R.id.detail_intro).setVisibility(View.VISIBLE);
        findViewById(R.id.llyt_bot_bar).setVisibility(View.VISIBLE);
    }

    @Override
    public void addSuccess() {
        isTrue = true;
        this.selected = true;
        tv_add.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_choose_blue, 0, 0);
    }

    @Override
    public void delSuccess() {
        isTrue = true;
        this.selected = false;
        tv_add.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_add_school, 0, 0);
    }

    @Override
    public void showBk(final List<SchoolDegreeInfo> data) {
        if (data.size() > 0) {
            findViewById(R.id.detail_bk).setVisibility(View.VISIBLE);
            RecyclerView rlv_bk = (RecyclerView) findViewById(R.id.rcv_bk);
            showDegree(rlv_bk, data, R.color.app_main_color, R.drawable.bg_oval_blue_size);
        }
    }

    @Override
    public void showYjs(List<SchoolDegreeInfo> data) {
        if (data.size() > 0) {
            findViewById(R.id.detail_yjs).setVisibility(View.VISIBLE);
            RecyclerView rcv_yjs = (RecyclerView) findViewById(R.id.rcv_yjs);
            showDegree(rcv_yjs, data, R.color.bg_answer_color, R.drawable.bg_oval_red_size);
        }
    }

    @Override
    public void showSia(List<SchoolDegreeInfo> data) {
        if (data.size() > 0) {
            findViewById(R.id.detail_sia).setVisibility(View.VISIBLE);
            RecyclerView rcv_yjs = (RecyclerView) findViewById(R.id.rcv_sia);
            showDegree(rcv_yjs, data, R.color.school_sia, R.drawable.bg_oval_purple_size);
        }
    }

    private void showDegree(RecyclerView recyclerView, final List<SchoolDegreeInfo> data, final int viewColor,
                            final int viewBackground) {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16f), DensityUtils.dip2px(16f))
                .colorResId(R.color.horizontal_line_color).build());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new NoScrollLinearLayoutManager(this));
        recyclerView.setAdapter(new CommonAdapter<SchoolDegreeInfo>(this, R.layout.item_schooldetail_application, data) {
            @Override
            protected void convert(ViewHolder holder, SchoolDegreeInfo info, int position) {
                TextView tv_left = holder.getView(R.id.tv_left);
                TextView tv_left_bot = holder.getView(R.id.tv_left_bot);
                View dot_left = holder.getView(R.id.dot_left);
                TextView tv_right = holder.getView(R.id.tv_right);
                TextView tv_right_bot = holder.getView(R.id.tv_right_bot);
                View dot_right = holder.getView(R.id.dot_right);
                View view_line = holder.getView(R.id.view_line);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_line.getLayoutParams();
                if (position == 0) {
                    params.topMargin = DensityUtils.dip2px(16);
                    tv_left.setTextColor(getResources().getColor(viewColor));
                    tv_left_bot.setTextColor(getResources().getColor(viewColor));
                    dot_left.setBackgroundResource(viewBackground);
                    tv_right.setTextColor(getResources().getColor(viewColor));
                    tv_right_bot.setTextColor(getResources().getColor(viewColor));
                    dot_right.setBackgroundResource(viewBackground);
                } else if (position == data.size() - 1) {
                    params.bottomMargin = DensityUtils.dip2px(16);
                }
                view_line.setLayoutParams(params);
                if (!TextUtils.isEmpty(info.getLeftValue())) {
                    tv_left.setText(info.getLeftValue());
                    tv_left_bot.setText(info.getLeftName());
                    dot_left.setVisibility(View.VISIBLE);
                } else {
                    tv_left.setText("－");
                    tv_left_bot.setText("－");
                    dot_left.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(info.getRightValue())) {
                    tv_right.setText(info.getRightValue());
                    tv_right_bot.setText(info.getRightName());
                    dot_right.setVisibility(View.VISIBLE);
                } else {
                    tv_right.setText("－");
                    tv_right_bot.setText("－");
                    dot_right.setVisibility(View.GONE);
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
                                Intent toMoreDetails = new Intent(SchoolDetailActivity.this, ShowWebViewActivity.class);
                                toMoreDetails.putExtra("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_NEWS_DETAIL), newsInfo.getId()));
                                toMoreDetails.putExtra("title", newsInfo.getTitle());
                                toMoreDetails.putExtra("url_action", "get");
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
            rlv_qa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                    .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16), 0).colorResId(R.color.horizontal_line_color).build());
            rlv_qa.setNestedScrollingEnabled(false);
            rlv_qa.setLayoutManager(new NoScrollLinearLayoutManager(this));
            rlv_qa.setAdapter(new CommonAdapter<QuestionInfo>(this, R.layout.item_qa_list, data) {
                @Override
                protected void convert(ViewHolder holder, final QuestionInfo questionInfo, int position) {
                    if (questionInfo != null) {
                        String avatar = questionInfo.getAsker().getAvatar();
                        String askName = questionInfo.getAsker().getName();
                        String placeholder = questionInfo.getAsker().getAvatarPlaceholder();
                        TextView tv_default_name = holder.getView(R.id.tv_default_name);
                        holder.setPersonImageUrl(R.id.iv_asker, avatar, true);
                        if (!TextUtils.isEmpty(placeholder)) {
                            tv_default_name.setVisibility(View.VISIBLE);
                            tv_default_name.setText(placeholder);
                        } else {
                            tv_default_name.setVisibility(View.GONE);
                        }
                        holder.setText(R.id.tv_asker_name, askName);
                        holder.setText(R.id.tv_question, questionInfo.getQuestion());
                        holder.setText(R.id.tv_answer, questionInfo.getAnswer());
                        holder.getView(R.id.llyt_qa).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent toMoreDetails = new Intent(SchoolDetailActivity.this, ShowWebViewActivity.class);
                                toMoreDetails.putExtra("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_QUESTION_DETAIL), questionInfo.getId()));
                                toMoreDetails.putExtra("title", "问答详情");
                                toMoreDetails.putExtra("url_action", "get");
                                startActivity(toMoreDetails);
                            }
                        });
                    }
                }
            });
        }
    }

    private void showDialog(View.OnClickListener listener) {
        mDialog = DialogCreator.createAdd2SchoolDialog(this, "添加为",
                listener);
        mDialog.getWindow().setLayout((int) (0.8 * ScreenUtils.getScreenWidth()),
                WindowManager.LayoutParams.WRAP_CONTENT);
        mDialog.show();
    }

    private void goWebMoreInfo(String url) {
        Intent toMoreDetails = new Intent(this, ShowWebViewActivity.class);
        toMoreDetails.putExtra("web_url", url);
        toMoreDetails.putExtra("url_action", "get");
        startActivity(toMoreDetails);
    }
}

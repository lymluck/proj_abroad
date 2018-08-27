package com.smartstudy.xxd.ui.activity;

/**
 * @author yqy
 * @date on 2018/3/12
 * @describe 教师详情页
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.FastBlur;
import com.smartstudy.commonlib.utils.LogUtils;
import com.smartstudy.commonlib.utils.MediaUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.StatusBarUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.medialib.ijkplayer.listener.OnPlayComplete;
import com.smartstudy.medialib.ijkplayer.listener.OnToggleFullScreenListener;
import com.smartstudy.medialib.ijkplayer.widget.PlayStateParams;
import com.smartstudy.medialib.ijkplayer.widget.PlayerView;
import com.smartstudy.medialib.ui.MediaApplication;
import com.smartstudy.medialib.videocache.HttpProxyCacheServer;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ConsultantsInfo;
import com.smartstudy.xxd.mvp.contract.TeacherInfoContract;
import com.smartstudy.xxd.mvp.presenter.TeacherInfoPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqy on 2017/12/28.
 */

public class TeacherInfoActivity extends BaseActivity implements TeacherInfoContract.View {
    private TextView tvTitle;
    private TextView tvYearWorking;
    private TextView tvCertification;
    private TextView tvSchool;
    private ConsultantsInfo consultantsInfo;
    private RelativeLayout llQa;
    private RecyclerView recyclerView;
    private List<ConsultantsInfo.AnsweredQuestions.AnswerData> answerDataList;
    private NoScrollLinearLayoutManager mLayoutManager;
    private CommonAdapter<ConsultantsInfo.AnsweredQuestions.AnswerData> mAdapter;
    private TextView tvTotalCount;
    private LinearLayout llAnswer;
    private String name;
    private RelativeLayout rlOrganization;
    private PlayerView player;
    private ImageView ivPlayer;
    private FrameLayout flAvatar;
    private RelativeLayout.LayoutParams params;
    private ImageView ivThumb;
    private View rootView;
    private String videoUrl;
    private TextView tvAddGood;
    private TeacherInfoContract.Presenter presenter;
    private LottieAnimationView animationView;
    private LinearLayout llAddGood;
    private int statusBarHeight;
    private RelativeLayout rlTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        rootView = mInflater.inflate(R.layout.activity_teacher_info, null);
        setContentView(rootView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //复位标题栏和状态栏
        StatusBarUtils.setDarkMode(this);
        if (player != null) {
            player.onResume();
            if (!TextUtils.isEmpty(videoUrl)) {
                player.setForbidDoulbeUp(false).startPlay();
            }
            player.hideCenterPlayer(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animationView.cancelAnimation();
        if (player != null) {
            player.onDestroy();
            player = null;
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void initViewAndData() {
        flAvatar = findViewById(R.id.fl_avatar);
        consultantsInfo = (ConsultantsInfo) getIntent().getSerializableExtra("consultantsInfo");
        tvTitle = findViewById(R.id.tv_title);
        tvYearWorking = findViewById(R.id.tv_year_working);
        tvSchool = findViewById(R.id.school);
        tvCertification = findViewById(R.id.tv_certification);
        llQa = findViewById(R.id.ll_qa);
        recyclerView = findViewById(R.id.rv_qa);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        llAnswer = findViewById(R.id.ll_answer);
        tvAddGood = findViewById(R.id.tv_add_good);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        rlOrganization = findViewById(R.id.rl_organization);
        llAddGood = findViewById(R.id.ll_add_good);
        rlTeacher = findViewById(R.id.rl_teacher);
        animationView = findViewById(R.id.animation_view);
        tvTotalCount = findViewById(R.id.tv_total_count);
        int marginlr = DensityUtils.dip2px(16f);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).margin(marginlr, marginlr).colorResId(R.color.bg_home_search).build());
        initAdapter();

        recyclerView.setAdapter(mAdapter);
        recyclerView.setFocusable(false);
        initTitleBar();
        handleAvatar();
        initPlayer();
        new TeacherInfoPresenter(this);
        if (consultantsInfo != null) {
            getTeacherInfoSuccess(consultantsInfo);
        }
    }

    private void handleAvatar() {
        // 头像的半径
        final int size = DensityUtils.dip2px(90f);
        NestedScrollView nsvTeacher = findViewById(R.id.nsv_teacher);
        nsvTeacher.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= size) {
                    int padding = scrollY;
                    flAvatar.setPadding(padding, padding, padding, padding);
                    flAvatar.setVisibility(View.VISIBLE);
                } else {
                    flAvatar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initTitleBar() {
        statusBarHeight = ScreenUtils.getStatusHeight();
        LinearLayout llTop = findViewById(R.id.app_video_top_box);
        findViewById(R.id.app_video_menu).setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                rlTeacher.setPadding(0, statusBarHeight, 0, 0);
            } else {
                rlTeacher.setPadding(0, 0, 0, 0);
                int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
                llTop.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + topHeight));
                llTop.setPadding(llTop.getPaddingLeft(), statusBarHeight, llTop.getPaddingRight(), 0);
            }
        }
    }

    private void initPlayer() {
        player = new PlayerView(this, rootView);
        if (player != null) {
            player.setOnPlayComplete(new OnPlayComplete() {
                @Override
                public void onComplete() {
                    // 播放完成
                    player.startPlay();
                }
            });
            player.setOnToggleFullScreenListener(new OnToggleFullScreenListener() {
                @Override
                public void onLandScape() {
                    rlTeacher.setPadding(0, 0, 0, 0);
                    flAvatar.setVisibility(View.GONE);
                    llAddGood.setVisibility(View.GONE);
                    if (player != null) {
                        player.forbidScroll(false).hideTime(false)
                            .hideSeeBar(false).hideFullscreen(false);
                    }
                    params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    ivPlayer.setLayoutParams(params);
                    rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
                }

                @Override
                public void onPortrait() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        rlTeacher.setPadding(0, statusBarHeight, 0, 0);
                    }
                    flAvatar.setVisibility(View.VISIBLE);
                    llAddGood.setVisibility(View.VISIBLE);
                    if (player != null) {
                        player.forbidScroll(true).hideTime(true)
                            .hideSeeBar(true).hideFullscreen(true);
                    }
                    params.width = DensityUtils.dip2px(45);
                    params.height = DensityUtils.dip2px(45);
                    ivPlayer.setLayoutParams(params);
                    rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            });
            ivThumb = player.getIv_trumb();
            player.setScaleType(PlayStateParams.fillparent)
                .hideControlPanl(true)
                .hideFullscreen(true)
                .hideTime(true)
                .hideSeeBar(true)
                .hideCenterPlayer(true)
                .hideSteam(true)
                .setForbidDoulbeUp(true)
                .hideRotation(true);
            ivPlayer = player.getPlayerView();
            params = (RelativeLayout.LayoutParams) ivPlayer.getLayoutParams();
            params.topMargin = DensityUtils.dip2px(8);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            //初始小屏模式
            player.forbidScroll(true);
            params.width = DensityUtils.dip2px(45);
            params.height = DensityUtils.dip2px(45);
            ivPlayer.setLayoutParams(params);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void initEvent() {
        llAnswer.setOnClickListener(this);
        llAddGood.setOnClickListener(this);
        findViewById(R.id.app_video_finish).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.ll_answer:
                Intent intent = new Intent();
                intent.putExtra("data_flag", "teacher");
                intent.putExtra("teacherId", consultantsInfo.getId());
                intent.putExtra("nickName", name);
                intent.setClass(this, QaListActivity.class);
                startActivity(intent);
                break;
            case R.id.app_video_finish:
                if (player != null && player.onBackPressed()) {
                    return;
                }
                finish();
                break;

            case R.id.ll_add_good:
                startAnima();
                if (consultantsInfo != null) {
                    presenter.addGood(consultantsInfo.getImUserId());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    /*
     * 开始动画
     */
    private void startAnima() {
        boolean inPlaying = animationView.isAnimating();
        if (!inPlaying) {
            animationView.setProgress(0f);
            animationView.playAnimation();
        }
    }

    public void getTeacherInfoSuccess(ConsultantsInfo consultantsInfo) {
        if (consultantsInfo != null) {
            name = consultantsInfo.getName();
            videoUrl = consultantsInfo.getVideo();
            if (!TextUtils.isEmpty(consultantsInfo.getAvatar())) {
                DisplayImageUtils.displayImage(this, consultantsInfo.getAvatar(), new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        if (TextUtils.isEmpty(videoUrl)) {
                            // 如果没有视频，展示封面
                            ivThumb.setImageBitmap(bitmap);
                            ViewTreeObserver vto = ivThumb.getViewTreeObserver();
                            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {
                                    ViewTreeObserver obs = ivThumb.getViewTreeObserver();
                                    ivThumb.buildDrawingCache();
                                    Bitmap bmp = ivThumb.getDrawingCache();
                                    FastBlur.blur(bmp, ivThumb);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        obs.removeOnGlobalLayoutListener(this);
                                    } else {
                                        obs.removeGlobalOnLayoutListener(this);
                                    }
                                }
                            });
                        }
                        ((ImageView) findViewById(R.id.iv_teacher_avatar)).setImageBitmap(bitmap);
                    }
                });
            }
            ((TextView) findViewById(R.id.tv_name)).setText(consultantsInfo.getName());
            tvAddGood.setText(consultantsInfo.getLikesCount());
            if (consultantsInfo.getOrganization() != null) {
                DisplayImageUtils.formatCircleImgUrl(this, consultantsInfo.getOrganization().getLogo(), (ImageView) this.findViewById(R.id.iv_logo));
                ((TextView) findViewById(R.id.tv_company)).setText(consultantsInfo.getOrganization().getName());
                ((TextView) findViewById(R.id.tv_subtitle)).setText(consultantsInfo.getOrganization().getSubtitle());
                rlOrganization.setVisibility(View.VISIBLE);
            } else {
                rlOrganization.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(consultantsInfo.getTitle())) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(consultantsInfo.getTitle());
            }

            if (!TextUtils.isEmpty(videoUrl)) {
                HttpProxyCacheServer proxy = MediaApplication.getProxy();
                if (proxy.isCached(videoUrl)) {
                    // 已经缓存，缓冲进度100%
                    player.setSecondaryProgress(1000);
                }
                String proxyUrl = proxy.getProxyUrl(videoUrl);
                LogUtils.d("======Use proxy url " + proxyUrl + " instead of original url " + videoUrl);
                // 播放视频
                if (player != null) {
                    player.hideControlPanl(false)
                        .setForbidDoulbeUp(false)
                        .autoPlay(proxyUrl)
                        .hideCenterPlayer(false);
                    player.hidePlayUI();
                    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
            if (TextUtils.isEmpty(consultantsInfo.getYearsOfWorking())) {
                tvYearWorking.setVisibility(View.GONE);
            } else {
                tvYearWorking.setVisibility(View.VISIBLE);
                tvYearWorking.setText("从业" + consultantsInfo.getYearsOfWorking() + "年");
            }
            if (consultantsInfo.isSchoolCertified()) {
                tvCertification.setText("已认证");
                tvCertification.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.certification,
                    0, 0);
                tvCertification.setTextColor(Color.parseColor("#FF9C08"));
            } else {
                tvCertification.setText("未认证");
                tvCertification.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.uncertification,
                    0, 0);
                tvCertification.setTextColor(Color.parseColor("#949ba1"));
            }
            tvSchool.setText(consultantsInfo.getSchool());

            if (consultantsInfo.getAnsweredQuestions() != null) {
                tvTotalCount.setText("共回答 " + consultantsInfo.getAnsweredQuestions().getPagination().getCount() + " 个问题");
                llQa.setVisibility(View.VISIBLE);
                if (consultantsInfo.getAnsweredQuestions().getData() != null && consultantsInfo.getAnsweredQuestions().getData().size() > 0) {
                    if (answerDataList != null) {
                        answerDataList.clear();
                    }
                    answerDataList.addAll(consultantsInfo.getAnsweredQuestions().getData());
                    mAdapter.notifyDataSetChanged();
                }

            } else {
                llQa.setVisibility(View.GONE);
            }

        } else {
            ToastUtils.showToast("查不到该老师的详细信息");
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
                holder.setText(R.id.tv_answer_count, answerData.getAnswerCount() + " 回答");
                holder.setText(R.id.tv_qa, answerData.getContent());
                holder.setText(R.id.tv_time, answerData.getCreateTimeText());
                if (TextUtils.isEmpty(answerData.getTargetCountryName())
                    && TextUtils.isEmpty(answerData.getTargetDegreeName())) {
                    holder.getView(R.id.ll_tag).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.ll_tag).setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(answerData.getTargetCountryName())) {
                        holder.getView(R.id.tv_country).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.tv_country).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_country, answerData.getTargetCountryName());
                    }
                    if (TextUtils.isEmpty(answerData.getTargetDegreeName())) {
                        holder.getView(R.id.tv_degree).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.tv_degree).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_degree, answerData.getTargetDegreeName());
                    }
                }
            }
        };
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ConsultantsInfo.AnsweredQuestions.AnswerData answer = answerDataList.get(position);
                UApp.actionEvent(TeacherInfoActivity.this, "19_A_question_detail_cell");
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

    private void setupHolderView(ViewHolder holder, int visibility) {
        holder.getView(R.id.llyt_qa_person).setVisibility(visibility);
        holder.getView(R.id.tv_see).setVisibility(visibility);
        holder = null;
    }

    @Override
    public void setPresenter(TeacherInfoContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void addGoodSuccess(String count) {
        tvAddGood.setText(count);
    }

}


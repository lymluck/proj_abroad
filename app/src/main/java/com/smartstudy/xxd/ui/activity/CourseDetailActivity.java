package com.smartstudy.xxd.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.PlayListener;
import com.smartstudy.commonlib.base.tools.SystemBarTintManager;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.commonlib.ui.customView.PagerSlidingTabStrip;
import com.smartstudy.commonlib.ui.customView.ViewPagerScroller;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.MediaUtils;
import com.smartstudy.commonlib.utils.NetUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ShareUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.medialib.ijkplayer.listener.OnPlayComplete;
import com.smartstudy.medialib.ijkplayer.listener.OnShowThumbnailListener;
import com.smartstudy.medialib.ijkplayer.listener.OnToggleFullScreenListener;
import com.smartstudy.medialib.ijkplayer.widget.PlayStateParams;
import com.smartstudy.medialib.ijkplayer.widget.PlayerView;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.model.CourseModel;
import com.smartstudy.xxd.ui.adapter.XxdViewPagerFragmentAdapter;
import com.smartstudy.xxd.ui.fragment.CourseCommentFragment;
import com.smartstudy.xxd.ui.fragment.CourseContentFragment;
import com.smartstudy.xxd.ui.fragment.CourseIntroFragment;

import java.util.Arrays;
import java.util.List;

@Route("courseDetail")
public class CourseDetailActivity extends UIActivity implements PlayListener {

    private RelativeLayout topDetail;
    private ImageView topdefault_leftbutton;
    private View rootView;
    public PlayerView player;
    private ImageView iv_player;
    private ViewPager pager_course;

    private SystemBarTintManager tintManager;
    private PowerManager.WakeLock wakeLock;
    private String courseId;
    private String coverUrl;
    private CourseContentFragment courseContentFragment;
    private CourseCommentFragment courseCommentFragment;
    private RelativeLayout.LayoutParams params;
    private int currentIndex = 1;
    private boolean commented = false;
    private WeakHandler mHandler;


    public PlayerView getPlayer() {
        return player;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        rootView = mInflater.inflate(R.layout.activity_course_detail, null);
        setContentView(rootView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSysBar();
        topDetail.setBackgroundColor(Color.argb(0, 00, 00, 00));
        /**demo的内容，暂停系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(this, false);
        /**demo的内容，激活设备常亮状态*/
        if (wakeLock != null) {
            wakeLock.acquire();
        }
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
            tintManager.setStatusBarAlpha(0);
        }
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            topDetail.setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
            int topHeight = getResources().getDimensionPixelSize(R.dimen.app_top_height);
            int statusBarHeight = ScreenUtils.getStatusHeight(getApplicationContext());
            topDetail.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight + topHeight));
            topDetail.setPadding(topDetail.getPaddingLeft(), statusBarHeight, topDetail.getPaddingRight(), 0);
        }
    }

    @Override
    protected void initViewAndData() {
        this.topDetail = (RelativeLayout) findViewById(R.id.rlyt_top);
        this.topdefault_leftbutton = (ImageView) findViewById(R.id.topdefault_leftbutton);
        this.topdefault_leftbutton.setImageResource(R.drawable.ic_go_back_white);
        Intent data = getIntent();
        coverUrl = data.getStringExtra("courseCover");
        pager_course = (ViewPager) findViewById(R.id.pager_course);
        ViewPagerScroller scroller = new ViewPagerScroller(this);
        scroller.initViewPagerScroll(pager_course);
        List<String> titles = Arrays.asList("简介", "大纲", "评价");
        Bundle bundle = new Bundle();
        courseId = data.getStringExtra("id");
        if (!TextUtils.isEmpty(courseId)) {
            courseId = data.getExtras() != null ? data.getExtras().getString("id") : courseId;
        }
        bundle.putString("courseId", courseId);
        CourseIntroFragment courseIntroFragment = new CourseIntroFragment();
        courseIntroFragment.setArguments(bundle);
        courseContentFragment = new CourseContentFragment();
        courseContentFragment.setArguments(bundle);
        courseCommentFragment = new CourseCommentFragment();
        courseCommentFragment.setArguments(bundle);
        List fragments = Arrays.asList(courseIntroFragment
                , courseContentFragment, courseCommentFragment);
        int len = fragments.size();
        pager_course.setAdapter(new XxdViewPagerFragmentAdapter(getSupportFragmentManager(), titles, fragments));
        pager_course.setOffscreenPageLimit(3);
        PagerSlidingTabStrip tabs_course = (PagerSlidingTabStrip) findViewById(R.id.tabs_course);
        tabs_course.setViewPager(pager_course);
        initPageChangeListener(tabs_course, len, fragments);
        pager_course.setCurrentItem(currentIndex);
        initSysBar();
        initTitleBar();
        initPlayer(coverUrl);
        getCourseBrief();
    }

    private void initPageChangeListener(PagerSlidingTabStrip tabs_course, final int len, final List fragments) {
        tabs_course.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < len; i++) {
                    if (i == position) {
                        ((UIFragment) fragments.get(i)).setUserVisibleHint(true);
                    } else {
                        ((UIFragment) fragments.get(i)).setUserVisibleHint(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void getCourseBrief() {
        CourseModel.getCourseBrief(courseId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                ToastUtils.showToast(getApplicationContext(), msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSON.parseObject(result);
                if (obj != null) {
                    commented = obj.getBoolean("commented");
                    if (TextUtils.isEmpty(coverUrl)) {
                        coverUrl = obj.getString("coverUrl");
                        if (!TextUtils.isEmpty(coverUrl)) {
                            player.showThumbnail(new OnShowThumbnailListener() {
                                @Override
                                public void onShowThumbnail(ImageView ivThumbnail) {
                                    DisplayImageUtils.formatImgUrl(CourseDetailActivity.this, coverUrl, ivThumbnail);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private void initPlayer(final String coverUrl) {
        /**常亮*/
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();
        player = new PlayerView(this, rootView)
                .setOnPlayComplete(new OnPlayComplete() {
                    @Override
                    public void onComplete() {
                        if (NetUtils.isConnected(getApplicationContext())) {
                            courseContentFragment.recordePlay(true);
                            String cacheKey = DateTimeUtils.getTimeOnlyMd() + SPCacheUtils.get("user_account", ParameterUtils.CACHE_NULL) + courseId;
                            if (!commented) {
                                if (!player.getIsFullScreen() && !"YES".equals(SPCacheUtils.get(cacheKey, ""))) {
                                    currentIndex = 2;
                                    pager_course.setCurrentItem(currentIndex, true);
                                    courseCommentFragment.dialogComment();
                                    SPCacheUtils.put(cacheKey, "YES");
                                }
                            } else {
                                SPCacheUtils.remove(cacheKey);
                            }
                        } else {
                            ToastUtils.showToast(getApplicationContext(), ParameterUtils.NET_ERR);
                        }
                    }
                })
                .setOnToggleFullScreenListener(new OnToggleFullScreenListener() {
                    @Override
                    public void onLandScape() {
                        player.forbidScroll(false);
                        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        iv_player.setLayoutParams(params);
                        rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        if (courseCommentFragment.getDialog() != null && courseCommentFragment.getDialog().isShowing()) {
                            courseCommentFragment.getDialog().dismiss();
                        }
                    }

                    @Override
                    public void onPortrait() {
                        player.forbidScroll(true);
                        params.width = DensityUtils.dip2px(45);
                        params.height = DensityUtils.dip2px(45);
                        iv_player.setLayoutParams(params);
                        rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        long startTime = (long) SPCacheUtils.get("playstart", 0L);
                        String cacheKey = DateTimeUtils.getTimeOnlyMd() + SPCacheUtils.get("user_account", ParameterUtils.CACHE_NULL) + courseId;
                        if (!commented) {
                            if (startTime > 0L) {
                                if (System.currentTimeMillis() - startTime > 30 * 1000) {
                                    if (!"YES".equals(SPCacheUtils.get(cacheKey, ""))) {
                                        mHandler.sendEmptyMessageDelayed(ParameterUtils.MSG_WHAT_REFRESH, 300);
                                        SPCacheUtils.put(cacheKey, "YES");
                                    }
                                }
                            }
                        } else {
                            SPCacheUtils.remove(cacheKey);
                        }
                    }
                })
                .setScaleType(PlayStateParams.fillparent)
                .hideControlPanl(true)
                .hideCenterPlayer(true)
                .setForbidDoulbeUp(true)
                .hideRotation(true);
        if (!TextUtils.isEmpty(coverUrl)) {
            player.showThumbnail(new OnShowThumbnailListener() {
                @Override
                public void onShowThumbnail(ImageView ivThumbnail) {
                    DisplayImageUtils.formatImgUrl(CourseDetailActivity.this, coverUrl, ivThumbnail);
                }
            });
        }
        iv_player = player.getPlayerView();
        params = (RelativeLayout.LayoutParams) iv_player.getLayoutParams();
        params.topMargin = DensityUtils.dip2px(8);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        //初始小屏模式
        player.forbidScroll(true);
        params.width = DensityUtils.dip2px(45);
        params.height = DensityUtils.dip2px(45);
        iv_player.setLayoutParams(params);
    }

    @Override
    public void initEvent() {
        topdefault_leftbutton.setOnClickListener(this);
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        currentIndex = 2;
                        pager_course.setCurrentItem(currentIndex);
                        courseCommentFragment.dialogComment();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                if (player != null && player.onBackPressed()) {
                    return;
                }
                /**demo的内容，恢复设备亮度状态*/
                if (wakeLock != null) {
                    wakeLock.release();
                }
                finish();
                break;
        }
    }

    @Override
    public void startPlay(String url, final String title, long lastTime, long duration, final String sectionId) {
        if (url != null) {
            topDetail.setVisibility(View.GONE);
            player.hideControlPanl(false)
                    .hideSteam(true)
                    .setTitle(title)
                    .setPlaySource(url)
                    .hideCenterPlayer(false);
            if (lastTime < duration - 7 * 1000) {
                player.setCurrentPosition((int) lastTime);
            } else {
                player.setCurrentPosition((int) (duration - 7 * 1000));
            }
            if (ParameterUtils.CACHE_NULL.equals(SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL))) {
                player.setLoginTime(true, 30)
                        .setForbidHideControlPanl(true);
            } else {
                player.setForbidDoulbeUp(false);
            }
            player.getMenuView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.showShare(CourseDetailActivity.this, String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_COURSE_SHARE), courseId, sectionId),
                            title, "点击查看课程详情", coverUrl, null);
                }
            });
            player.hidePlayUI();
            player.startPlay();
            SPCacheUtils.put("playstart", System.currentTimeMillis());
        } else {
            ToastUtils.showToast(this, "抱歉，该课程暂时无法观看！");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(this, true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        /**demo的内容，恢复设备亮度状态*/
        if (wakeLock != null) {
            wakeLock.release();
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_LOGIN:
                player.setLoginTime(false, 0);
                if ("comment".equals(SPCacheUtils.get("course_from", ""))) {
                    //CourseCommentFragment
                    SPCacheUtils.remove("course_from");
                    courseCommentFragment.refresh();
                }
                if (courseContentFragment.lastClickTime > 0L) {
                    player.hideCenterPlayer(false)
                            .setForbidDoulbeUp(false)
                            .setForbidHideControlPanl(false);
                    player.startPlay();
                }
                courseContentFragment.refresh();
                break;
        }
    }
}

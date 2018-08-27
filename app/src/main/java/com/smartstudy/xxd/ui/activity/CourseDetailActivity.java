package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.PlayListener;
import com.smartstudy.commonlib.base.listener.ShareListener;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.customview.PagerSlidingTabStrip;
import com.smartstudy.commonlib.ui.customview.ViewPagerScroller;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.MediaUtils;
import com.smartstudy.commonlib.utils.NetUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.StatusBarUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.medialib.ijkplayer.listener.OnPlayComplete;
import com.smartstudy.medialib.ijkplayer.listener.OnShowThumbnailListener;
import com.smartstudy.medialib.ijkplayer.listener.OnToggleFullScreenListener;
import com.smartstudy.medialib.ijkplayer.widget.PlayStateParams;
import com.smartstudy.medialib.ijkplayer.widget.PlayerView;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.sdk.utils.UMShareUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.model.CourseModel;
import com.smartstudy.xxd.ui.adapter.XxdViewPagerFragmentAdapter;
import com.smartstudy.xxd.ui.fragment.CourseCommentFragment;
import com.smartstudy.xxd.ui.fragment.CourseContentFragment;
import com.smartstudy.xxd.ui.fragment.CourseIntroFragment;
import com.umeng.socialize.UMShareAPI;

import java.util.Arrays;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.USER_ACCOUNT;

@Route("courseDetail")
public class CourseDetailActivity extends BaseActivity implements PlayListener {

    private View rootView;
    public PlayerView player;
    private ImageView iv_player;
    private ViewPager pager_course;

    private String courseId;
    private String coverUrl;
    private CourseContentFragment courseContentFragment;
    private CourseCommentFragment courseCommentFragment;
    private RelativeLayout.LayoutParams params;
    private int currentIndex = 1;
    private boolean commented = false;
    private WeakHandler mHandler;
    private OnPageChangeListener pageChangeListener;
    private LinearLayout llContent;
    private int statusBarHeight;


    public PlayerView getPlayer() {
        return player;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        rootView = mInflater.inflate(R.layout.activity_course_detail, null);
        setContentView(rootView);
        UApp.actionEvent(this, "16_B_course_detail");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
            player = null;
        }
        if (pageChangeListener != null) {
            pageChangeListener = null;
        }
        if (pager_course != null) {
            pager_course.removeAllViews();
            pager_course = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setDarkMode(this);
        /**暂停系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(false);
    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {
        statusBarHeight = ScreenUtils.getStatusHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            llContent.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    @Override
    protected void initViewAndData() {
        llContent = findViewById(R.id.ll_content);
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
        pager_course.setAdapter(new XxdViewPagerFragmentAdapter(getSupportFragmentManager(), titles, null, fragments));
        pager_course.setOffscreenPageLimit(3);
        PagerSlidingTabStrip tabs_course = (PagerSlidingTabStrip) findViewById(R.id.tabs_course);
        tabs_course.setViewPager(pager_course);
        initPageChangeListener(tabs_course, len, fragments);
        pager_course.setCurrentItem(currentIndex);
        initTitleBar();
        initPlayer(coverUrl);
        getCourseBrief();
    }

    private void initPageChangeListener(PagerSlidingTabStrip tabs_course, final int len, final List fragments) {
        pageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < len; i++) {
                    if (i == position) {
                        ((BaseFragment) fragments.get(i)).setUserVisibleHint(true);
                    } else {
                        ((BaseFragment) fragments.get(i)).setUserVisibleHint(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        tabs_course.setOnPageChangeListener(pageChangeListener);
    }

    private void getCourseBrief() {
        CourseModel.getCourseBrief(courseId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                ToastUtils.showToast(msg);
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
        player = new PlayerView(this, rootView);
        if (player != null) {
            player.setOnPlayComplete(new OnPlayComplete() {
                @Override
                public void onComplete() {
                    if (NetUtils.isConnected(getApplicationContext())) {
                        courseContentFragment.recordePlay(true);
                        String cacheKey = DateTimeUtils.getTimeOnlyMd() + SPCacheUtils.get(USER_ACCOUNT, "") + courseId;
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
                        ToastUtils.showToast(ParameterUtils.NET_ERR);
                    }
                }
            });
            player.setOnToggleFullScreenListener(new OnToggleFullScreenListener() {
                @Override
                public void onLandScape() {
                    llContent.setPadding(0, 0, 0, 0);
                    if (player != null) {
                        player.forbidScroll(false);
                    }
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        llContent.setPadding(0, statusBarHeight, 0, 0);
                    }
                    if (player != null) {
                        player.forbidScroll(true);
                    }
                    params.width = DensityUtils.dip2px(45);
                    params.height = DensityUtils.dip2px(45);
                    iv_player.setLayoutParams(params);
                    rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    long startTime = (long) SPCacheUtils.get("playstart", 0L);
                    String cacheKey = DateTimeUtils.getTimeOnlyMd() + SPCacheUtils.get(USER_ACCOUNT, "") + courseId;
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
            });
            player.setScaleType(PlayStateParams.fillparent)
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
    }

    @Override
    public void initEvent() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        currentIndex = 2;
                        pager_course.setCurrentItem(currentIndex);
                        courseCommentFragment.dialogComment();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void doClick(View v) {
    }

    @Override
    public void startPlay(String url, final String title, long lastTime, long duration, final String sectionId) {
        if (url != null && player != null) {
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
            if (TextUtils.isEmpty((String) SPCacheUtils.get("ticket", ""))) {
                player.setLoginTime(true, 30)
                    .setForbidHideControlPanl(true);
            } else {
                player.setForbidDoulbeUp(false);
            }
            player.getMenuView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_COURSE_SHARE),
                        courseId, sectionId);
                    UMShareUtils.showShare(CourseDetailActivity.this, url,
                        title, "点击查看课程详情", coverUrl, new ShareListener(url, "course_detail"));
                }
            });
            player.hidePlayUI();
            player.startPlay();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            SPCacheUtils.put("playstart", System.currentTimeMillis());
        } else {
            ToastUtils.showToast("抱歉，该课程暂时无法观看！");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //涉及到分享时必须调用到方法
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                courseContentFragment.refresh();
                break;
            default:
                break;
        }
    }
}

package com.smartstudy.xxd.ui.fragment;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nineoldandroids.animation.ObjectAnimator;
import com.smartstudy.commonlib.entity.Event;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.customview.VerticalSwipeRefreshLayout;
import com.smartstudy.commonlib.ui.customview.rollviewpager.RollPagerView;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.StatusBarUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.qrcode.CodeScanActivity;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.BannerInfo;
import com.smartstudy.xxd.entity.PersonalInfo;
import com.smartstudy.xxd.mvp.contract.BannerFragmentContract;
import com.smartstudy.xxd.mvp.presenter.BannerFragmentPresenter;
import com.smartstudy.xxd.ui.activity.HomeSearchActivity;
import com.smartstudy.xxd.ui.activity.MsgCenterActivity;
import com.smartstudy.xxd.ui.adapter.XxdBannerAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.commonlib.utils.ParameterUtils.XXD_UNREAD;

/**
 * @author luoyongming
 * @date on 2018/4/12
 * @describe 首页第一个tab对应的fragment
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class BannerFragment extends BaseFragment implements BannerFragmentContract.View {

    private VerticalSwipeRefreshLayout vsrlHome;
    private XxdBannerAdapter myBannerAdapter;
    private RollPagerView rpvBanner;
    private View topLine;
    private LinearLayout llHomeSearch;
    private LinearLayout llHomeTop;
    private ImageView ivScan;
    private ImageView ivMsg;
    private TextView msgRed;

    private List<BannerInfo> bannerInfos;
    private BannerFragmentContract.Presenter presenter;
    private boolean isWhite = true;
    private boolean isDarkMode = true;
    private int topAlpha = 0;
    private int topHeight;
    private ArgbEvaluator mArgbEvaluator;
    private BaseFragment childFragment;
    private boolean isAbroadPlanAvailable = true;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_banner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = true;
        UApp.actionEvent(mActivity, "8_B_home_page");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onUserInvisible() {
        if (rpvBanner.isPlaying()) {
            rpvBanner.pause();
        }
        if (vsrlHome != null) {
            vsrlHome.setRefreshing(false);
        }
        if (childFragment != null) {
            childFragment.setUserVisibleHint(false);
        }
        isAbroadPlanAvailable = true;
        super.onUserInvisible();
    }

    @Override
    public void onUserVisible() {
        if (!rpvBanner.isPlaying()) {
            rpvBanner.resume();
        }
        super.onUserVisible();
        if (!isAdded()) {
            vsrlHome.setRefreshing(false);
            return;
        }
        // fragment切换
        initFragment(true);
        handleMsgRed();
        showPlanDialog(isAbroadPlanAvailable);
        showTowhere();
    }

    //未读消息处理
    public void handleMsgRed() {
        int unread = (int) SPCacheUtils.get(XXD_UNREAD, 0);
        if (unread > 0) {
            msgRed.setVisibility(View.VISIBLE);
        } else {
            msgRed.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        topAlpha = 0;
        if (bannerInfos != null) {
            bannerInfos.clear();
            bannerInfos = null;
        }
        if (myBannerAdapter != null) {
            myBannerAdapter.destroy();
            myBannerAdapter = null;
        }
        if (rpvBanner != null) {
            rpvBanner.removeAllViews();
            rpvBanner = null;
        }
        if (childFragment != null) {
            childFragment = null;
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.HomeFilterEvent homeFilterEvent) {
        isAbroadPlanAvailable = homeFilterEvent.isAbroadPlanAvailable();
        // 移除fragment
        removeFragment();
    }

    @Override
    protected void initView() {
        // 异常发生事移除fragment
        removeFragment();
        // 加载fragment
        initFragment(false);
        if (childFragment != null) {
            childFragment.setUserVisibleHint(false);
        }
        llHomeSearch = rootView.findViewById(R.id.llyt_home_search);
        ivScan = rootView.findViewById(R.id.iv_scan);
        ivMsg = rootView.findViewById(R.id.iv_msg);
        msgRed = rootView.findViewById(R.id.msg_red);
        llHomeTop = rootView.findViewById(R.id.llyt_home_top);
        llHomeTop.setBackgroundColor(Color.argb(topAlpha, 255, 255, 255));
        llHomeSearch.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_raduis_search_gray));
        ivMsg.setImageResource(R.drawable.ic_home_msg_blue);
        ivScan.setImageResource(R.drawable.ic_home_scan_blue);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llHomeTop.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            topHeight = params.height + ScreenUtils.getStatusHeight();
            params.height = topHeight;
            llHomeTop.setLayoutParams(params);
            llHomeTop.setPadding(0, ScreenUtils.getStatusHeight(), 0, 0);
        } else {
            topHeight = params.height;
        }
        params = null;
        rpvBanner = rootView.findViewById(R.id.rpv_banner);
        // 初始时的高度
        LinearLayout.LayoutParams pvParams = (LinearLayout.LayoutParams) rpvBanner.getLayoutParams();
        pvParams.height = topHeight;
        rpvBanner.setLayoutParams(pvParams);
        pvParams = null;
        topLine = rootView.findViewById(R.id.top_line);
        topLine.setVisibility(View.VISIBLE);
        vsrlHome = rootView.findViewById(R.id.vsrl_home);
        vsrlHome.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            90, getResources().getDisplayMetrics()));
        vsrlHome.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        bannerInfos = new ArrayList<>();
        myBannerAdapter = new XxdBannerAdapter(mActivity.getApplicationContext(), rpvBanner, bannerInfos);
        rpvBanner.setAdapter(myBannerAdapter);
        mArgbEvaluator = new ArgbEvaluator();
        handleMsgRed();
        initEvent();
        new BannerFragmentPresenter(this);
        presenter.getBanners(ParameterUtils.CACHED_ELSE_NETWORK, bannerInfos);
    }

    private void initEvent() {
        llHomeSearch.setOnClickListener(this);
        ivScan.setOnClickListener(this);
        rootView.findViewById(R.id.rl_msg).setOnClickListener(this);
        vsrlHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAdv();
                if (childFragment != null) {
                    if (childFragment instanceof UsHighSchoolFragment) {
                        ((UsHighSchoolFragment) childFragment).dataRefresh();
                    } else if (childFragment instanceof UsCollegeFragment) {
                        ((UsCollegeFragment) childFragment).dataRefresh();
                    }
                }
            }
        });
        // 解决滑动冲突
        handleTouch();
        handleScroll();
    }

    private void refreshAdv() {
        presenter.getBanners(ParameterUtils.NETWORK_ELSE_CACHED, bannerInfos);
    }

    /**
     * 处理滑动标题栏颜色渐变
     */
    private void handleScroll() {
        ((NestedScrollView) rootView.findViewById(R.id.nsv_home)).setOnScrollChangeListener(
            new NestedScrollView.OnScrollChangeListener() {

                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    // 滑动时头部搜索栏的变换
                    // 控制刷新何时出现
                    if (!vsrlHome.isRefreshing()) {
                        vsrlHome.setEnabled(v.getScrollY() == 0);
                    }
                    float diff = rpvBanner.getHeight() - topHeight;
                    float positionOffset;
                    if (scrollY <= diff) {
                        positionOffset = scrollY / diff;
                        topAlpha = (int) (positionOffset * 255);
                        topLine.setVisibility(View.GONE);
                    } else if (scrollY > diff) {
                        topAlpha = 255;
                        positionOffset = 1f;
                        topLine.setVisibility(View.VISIBLE);
                    } else {
                        topAlpha = 0;
                        positionOffset = 0f;
                    }
                    topAnim();
                    transTopIcon(positionOffset, oldScrollY, scrollY);
                    llHomeTop.setBackgroundColor(Color.argb(topAlpha, 255, 255, 255));
                }
            });
    }

    /**
     * 头部icon渐变
     *
     * @param positionOffset
     * @param oldScrollY
     * @param scrollY
     */
    private void transTopIcon(float positionOffset, int oldScrollY, int scrollY) {
        int mColor = (int) mArgbEvaluator.evaluate(positionOffset, mActivity.getResources().getColor(R.color.white),
            mActivity.getResources().getColor(R.color.app_main_color));
        if (oldScrollY < scrollY) {
            // 向上
            Drawable msgColor = tintDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_msg_blue),
                ColorStateList.valueOf(mColor));
            Drawable scanColor = tintDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_scan_blue),
                ColorStateList.valueOf(mColor));
            ivMsg.setImageDrawable(msgColor);
            ivScan.setImageDrawable(scanColor);
        } else if (oldScrollY > scrollY) {
            // 向下
            Drawable msgColor = tintDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_msg_white),
                ColorStateList.valueOf(mColor));
            Drawable scanColor = tintDrawable(mActivity.getResources().getDrawable(R.drawable.ic_home_scan_white),
                ColorStateList.valueOf(mColor));
            ivMsg.setImageDrawable(msgColor);
            ivScan.setImageDrawable(scanColor);
        }
    }

    /**
     * 头部动画变换
     */
    private void topAnim() {
        // 翻转动画
        if (topAlpha > 128) {
            // alpha > 0.5设置蓝色图标
            if (isWhite) {
                if (!isDarkMode) {
                    StatusBarUtils.setLightMode(mActivity);
                    isDarkMode = true;
                }
                llHomeSearch.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_raduis_search_gray));
                ObjectAnimator animator = ObjectAnimator.ofFloat(llHomeSearch, "alpha", 0.5f, 1);
                animator.setDuration(800);
                animator.start();
            }
            isWhite = false;
        } else { // 否则设置白色
            if (!isWhite) {
                if (isDarkMode) {
                    StatusBarUtils.setDarkMode(mActivity);
                    isDarkMode = false;
                }
                llHomeSearch.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_raduis_search_white95));
                ObjectAnimator animator = ObjectAnimator.ofFloat(llHomeSearch, "alpha", 0.5f, 1);
                animator.setDuration(800);
                animator.start();
            }
            isWhite = true;
        }
    }

    private void removeFragment() {
        FragmentTransaction mTransaction = getChildFragmentManager().beginTransaction();
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            mTransaction.remove(fragment).commitAllowingStateLoss();
        }
        mTransaction = null;
        childFragment = null;
    }

    public void initFragment(boolean visible) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // 处理美高，进行页面切换
        String targetCountryInfo = (String) SPCacheUtils.get("target_countryInfo", "");
        String targetCountryName = "意向国家";
        if (!TextUtils.isEmpty(targetCountryInfo)) {
            PersonalInfo.TargetSectionEntity.TargetCountryEntity targetCountryEntity
                = JSON.parseObject(targetCountryInfo, PersonalInfo.TargetSectionEntity.TargetCountryEntity.class);
            if (targetCountryEntity != null) {
                targetCountryName = targetCountryEntity.getName();
            }
        }
        String projName = (String) SPCacheUtils.get("project_name", "研究生");
        if ("高中".equals(projName) && "美国".equals(targetCountryName)) {
            if (childFragment == null) {
                childFragment = new UsHighSchoolFragment();
            } else {
                if (!(childFragment instanceof UsHighSchoolFragment)) {
                    childFragment = new UsHighSchoolFragment();
                }
            }
        } else {
            if (childFragment == null) {
                childFragment = new UsCollegeFragment();
            } else {
                if (!(childFragment instanceof UsCollegeFragment)) {
                    childFragment = new UsCollegeFragment();
                }
            }
        }
        if (isAdded()) {
            transaction.replace(R.id.content, childFragment);
            transaction.commitAllowingStateLoss();
        }
        if (visible) {
            // 可见时显式调用
            childFragment.setUserVisibleHint(visible);
        }
        transaction = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llyt_home_search:
                UApp.actionEvent(mActivity, "8_A_search_btn");
                Intent toSearch = new Intent(mActivity, HomeSearchActivity.class);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(mActivity, R.anim.fade_in, R.anim.fade_out);
                ActivityCompat.startActivity(mActivity, toSearch, compat.toBundle());
                break;
            case R.id.iv_scan:
                startActivity(new Intent(mActivity, CodeScanActivity.class));
                break;
            case R.id.rl_msg:
                UApp.actionEvent(mActivity, "22_A_my_message_cell");
                SPCacheUtils.put(XXD_UNREAD, 0);
                startActivity(new Intent(mActivity, MsgCenterActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 解决pagerView与swipeRefreshLayout滑动冲突
     */
    private void handleTouch() {
        rpvBanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (rpvBanner.isPlaying()) {
                            rpvBanner.pause();
                        }
                        if (!vsrlHome.isRefreshing()) {
                            vsrlHome.setEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (!rpvBanner.isPlaying()) {
                            rpvBanner.resume();
                        }
                        vsrlHome.setEnabled(true);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void setPresenter(BannerFragmentContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        vsrlHome.setRefreshing(false);
        if (presenter != null) {
            ToastUtils.showToast(message);
        }
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    @Override
    public void showBanner() {
        vsrlHome.setRefreshing(false);
        if (myBannerAdapter != null) {
            myBannerAdapter.notifyDataSetChanged();
            // 展示banner时做一些UI布局调整
            topLine.setVisibility(View.GONE);
            if (mActivity != null && isAdded()) {
                if (topAlpha == 0) {
                    llHomeSearch.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_raduis_search_white95));
                    ivMsg.setImageResource(R.drawable.ic_home_msg_white);
                    ivScan.setImageResource(R.drawable.ic_home_scan_white);
                }
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rpvBanner.getLayoutParams();
            params.height = ScreenUtils.getScreenWidth() / 2;
            rpvBanner.setLayoutParams(params);
            params = null;
            if (isDarkMode && topAlpha == 0) {
                StatusBarUtils.setDarkMode(mActivity);
                isDarkMode = false;
            }
        }
    }

    public VerticalSwipeRefreshLayout getHomeVsrl() {
        return vsrlHome;
    }

    public Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }


    public void showTowhere() {
        if (childFragment != null) {
            if (childFragment instanceof UsCollegeFragment && childFragment.isAdded()) {
                UsCollegeFragment usCollegeFragment = (UsCollegeFragment) childFragment;
                usCollegeFragment.showIntentionally();
            }
        }
    }

    public void showPlanDialog(boolean show) {
        String key = SPCacheUtils.get("user_id", "").toString() + "QA";
        String project_name = SPCacheUtils.get("project_name", "").toString();
        boolean isDelete = false;
        if (SPCacheUtils.contains(key)) {
            isDelete = (boolean) SPCacheUtils.get(key, true);
        }
        if (!show && !isDelete) {
            if (!mActivity.isFinishing()) {
                String targetCountryInfo = (String) SPCacheUtils.get("target_countryInfo", "");
                String targetCountryName = null;
                if (!TextUtils.isEmpty(targetCountryInfo)) {
                    PersonalInfo.TargetSectionEntity.TargetCountryEntity targetCountryEntity
                        = JSON.parseObject(targetCountryInfo, PersonalInfo.TargetSectionEntity.TargetCountryEntity.class);
                    if (targetCountryEntity != null) {
                        targetCountryName = targetCountryEntity.getName();
                    }
                }
                if ("美国".equals(targetCountryName)) {
                    if (project_name.equals("本科") || project_name.equals("研究生")) {
                        DialogCreator.createHomeQaDialog(mActivity);
                    }
                }
            }
        }
    }

    public void setAvailable(boolean isAbroadPlanAvailable) {
        this.isAbroadPlanAvailable = isAbroadPlanAvailable;
    }
}

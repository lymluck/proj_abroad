package com.smartstudy.xxd.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.customview.StartSnapHelper;
import com.smartstudy.commonlib.ui.customview.rollviewpager.RollPagerView;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.BannerInfo;
import com.smartstudy.xxd.entity.HomeHotInfo;
import com.smartstudy.xxd.entity.HomeHotListInfo;
import com.smartstudy.xxd.entity.HomeHotRankInfo;
import com.smartstudy.xxd.mvp.contract.HomeFragmentContract;
import com.smartstudy.xxd.mvp.presenter.HomeFragmentPresenter;
import com.smartstudy.xxd.ui.activity.HomeSearchActivity;
import com.smartstudy.xxd.ui.activity.MainActivity;
import com.smartstudy.xxd.ui.activity.RankActivity;
import com.smartstudy.xxd.ui.activity.RankTypeActivity;
import com.smartstudy.xxd.ui.activity.SchoolListActivity;
import com.smartstudy.xxd.ui.activity.ShowWebViewActivity;
import com.smartstudy.xxd.ui.activity.SpecialListActivity;
import com.smartstudy.xxd.ui.activity.SrtChooseSchoolActivity;
import com.smartstudy.xxd.ui.activity.SrtChooseSpecialActivity;
import com.smartstudy.xxd.ui.activity.StudyConsultantsActivity;
import com.smartstudy.xxd.ui.activity.ThematicCenterActivity;
import com.smartstudy.xxd.ui.adapter.HomeHotSubAdapter;
import com.smartstudy.xxd.ui.adapter.XxdBannerAdapter;
import com.smartstudy.xxd.ui.adapter.homeschool.HomeHotAdapter;

import java.util.ArrayList;
import java.util.List;

public class SchoolFragment extends UIFragment implements HomeFragmentContract.View {

    private XxdBannerAdapter myBannerAdapter;
    private RecyclerView rcv_hots;
    private HomeHotAdapter mAdapter;
    private HomeHotSubAdapter hotSubAdapter;
    private HeaderAndFooterWrapper mHeader;
    private LinearLayout llyt_home_top;
    private LinearLayout llyt_home_search;
    private View top_line;
    private LinearLayout llyt_header_grid;
    private RollPagerView pagerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View headView;
    private View footView;
    private TextView tv_world_name;
    private TextView tv_us_name;
    private TextView tv_au_name;
    private TextView tv_ca_name;
    private TextView tv_uk_name;
    private RecyclerView rcv_topic;
    private LinearLayout llyt_home_footer;
    private LinearLayout llyt_home_rank;

    private List<BannerInfo> bannerInfos;
    private List<HomeHotListInfo> hotListInfos;
    private List<HomeHotInfo> hotSubList;
    private HomeFragmentContract.Presenter homeP;
    private int top_alpha = 0;
    private boolean isWhite = true;
    private boolean isDarkMode;
    private int topHeight;
    private boolean hasAddFooter;

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.fragment_school, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatisticUtils.actionEvent(mActivity, "8_B_home_page");
        isPrepared = true;
    }

    @Override
    public void onDetach() {
        releaseRes();
        super.onDetach();
    }

    private void releaseRes() {
        if (homeP != null) {
            homeP = null;
        }
        if (myBannerAdapter != null) {
            myBannerAdapter.destroy();
            myBannerAdapter = null;
        }
        if (rcv_hots != null) {
            rcv_hots.removeAllViews();
            rcv_hots = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (hotSubAdapter != null) {
            hotSubAdapter.destroy();
            hotSubAdapter = null;
        }
        if (bannerInfos != null) {
            bannerInfos.clear();
            bannerInfos = null;
        }
        if (hotListInfos != null) {
            hotListInfos.clear();
            hotListInfos = null;
        }
        if (hotSubList != null) {
            hotSubList.clear();
            hotSubList = null;
        }
    }

    @Override
    protected void initView(View rootView) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srfl_home);
        swipeRefreshLayout.setProgressViewOffset(true, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                90, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeP.getBanners(ParameterUtils.NETWORK_ELSE_CACHED, bannerInfos);
                homeP.getHomeHot();
            }
        });
        llyt_home_top = (LinearLayout) rootView.findViewById(R.id.llyt_home_top);
        top_line = rootView.findViewById(R.id.top_line);
        top_line.setVisibility(View.VISIBLE);
        llyt_home_search = (LinearLayout) rootView.findViewById(R.id.llyt_home_search);
        llyt_home_search.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_raduis_search_gray));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llyt_home_top.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            topHeight = params.height + ScreenUtils.getStatusHeight(mActivity);
            params.height = topHeight;
            llyt_home_top.setLayoutParams(params);
            llyt_home_top.setPadding(0, ScreenUtils.getStatusHeight(mActivity), 0, 0);
        } else {
            topHeight = params.height;
        }
        params = null;
//        DialogCreator.createHomeQaDialog(getActivity());
        this.rcv_hots = (RecyclerView) rootView.findViewById(R.id.rcv_hots);
        hotListInfos = new ArrayList<>();
        mAdapter = new HomeHotAdapter(mActivity, hotListInfos, mActivity.getLayoutInflater());
        mHeader = new HeaderAndFooterWrapper(mAdapter);
        final GridLayoutManager manager = new GridLayoutManager(mActivity, 3);
        //设置span
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.isSectionHeaderPosition(position - mHeader.getHeadersCount())
                        || mAdapter.isSectionFooterPosition(position - mHeader.getHeadersCount())) {
                    return manager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        manager.setOrientation(GridLayoutManager.VERTICAL);
        rcv_hots.setHasFixedSize(true);
        rcv_hots.setLayoutManager(manager);
        rcv_hots.setNestedScrollingEnabled(false);
        rcv_hots.setAdapter(mHeader);
        initHeaderAndFooter();
        new HomeFragmentPresenter(this);
        homeP.getBanners(ParameterUtils.CACHED_ELSE_NETWORK, bannerInfos);
        homeP.getHomeHot();
    }

    //设置header和footer
    private void initHeaderAndFooter() {
        headView = mActivity.getLayoutInflater().inflate(R.layout.layout_home_header, null, false);
        footView = mActivity.getLayoutInflater().inflate(R.layout.layout_home_footer, null, false);
        llyt_home_rank = (LinearLayout) headView.findViewById(R.id.llyt_home_rank);
        llyt_home_footer = (LinearLayout) footView.findViewById(R.id.llyt_home_footer);
        rcv_topic = (RecyclerView) footView.findViewById(R.id.rcv_topic);
        pagerView = (RollPagerView) headView.findViewById(R.id.view_pager);
        tv_world_name = (TextView) headView.findViewById(R.id.tv_world_name);
        tv_us_name = (TextView) headView.findViewById(R.id.tv_us_name);
        tv_uk_name = (TextView) headView.findViewById(R.id.tv_uk_name);
        tv_ca_name = (TextView) headView.findViewById(R.id.tv_ca_name);
        tv_au_name = (TextView) headView.findViewById(R.id.tv_au_name);
        llyt_header_grid = (LinearLayout) headView.findViewById(R.id.llyt_header_grid);
        //banner未显示时，llyt_header_content往下移
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llyt_header_grid.getLayoutParams();
        params.topMargin = topHeight;
        llyt_header_grid.setLayoutParams(params);
        params = null;
        bannerInfos = new ArrayList<>();
        myBannerAdapter = new XxdBannerAdapter(mActivity.getApplicationContext(), pagerView, bannerInfos);
        pagerView.setAdapter(myBannerAdapter);
        mHeader.addHeaderView(headView);
        initSubAdapter();
        initEvent();
    }

    //初始化热门专题适配器
    private void initSubAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcv_topic.setHasFixedSize(true);
        rcv_topic.setLayoutManager(layoutManager);
        layoutManager = null;
        rcv_topic.setNestedScrollingEnabled(false);
        hotSubList = new ArrayList<>();
        hotSubAdapter = new HomeHotSubAdapter(mActivity, R.layout.item_hometopic_list, hotSubList);
        StartSnapHelper snapHelper = new StartSnapHelper();
        snapHelper.attachToRecyclerView(rcv_topic);
        rcv_topic.setAdapter(hotSubAdapter);
    }

    private void initEvent() {
        //解决滑动冲突
        handleTouch();
        handleScroll();
        headView.findViewById(R.id.cd_world).setOnClickListener(this);
        headView.findViewById(R.id.cd_us).setOnClickListener(this);
        headView.findViewById(R.id.cd_uk).setOnClickListener(this);
        headView.findViewById(R.id.cd_ca).setOnClickListener(this);
        headView.findViewById(R.id.cd_au).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_school).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_rank).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_special).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_topic).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_rate).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_srt_spe).setOnClickListener(this);
        headView.findViewById(R.id.tv_choose_school).setOnClickListener(this);
        headView.findViewById(R.id.tv_home_gpa).setOnClickListener(this);
        headView.findViewById(R.id.tv_see_more_rank).setOnClickListener(this);
        footView.findViewById(R.id.tv_see_more_topic).setOnClickListener(this);
        llyt_home_search.setOnClickListener(this);
        hotSubAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                StatisticUtils.actionEvent(mActivity, "8_A_specific_subject_btn");
                HomeHotInfo info = hotSubList.get(position);
                Intent toMoreDetails = new Intent(mActivity, ShowWebViewActivity.class);
                toMoreDetails.putExtra("web_url", info.getSubjectUrl());
                toMoreDetails.putExtra("title", info.getName());
                toMoreDetails.putExtra("url_action", "get");
                startActivity(toMoreDetails);
                info = null;
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cd_world:
                StatisticUtils.actionEvent(mActivity, "8_A_specific_rank_btn");
                goSchoolRank(tv_world_name.getText(), tv_world_name.getTag());
                break;
            case R.id.cd_us:
                StatisticUtils.actionEvent(mActivity, "8_A_specific_rank_btn");
                goSchoolRank(tv_us_name.getText(), tv_us_name.getTag());
                break;
            case R.id.cd_uk:
                StatisticUtils.actionEvent(mActivity, "8_A_specific_rank_btn");
                goSchoolRank(tv_uk_name.getText(), tv_uk_name.getTag());
                break;
            case R.id.cd_ca:
                StatisticUtils.actionEvent(mActivity, "8_A_specific_rank_btn");
                goSchoolRank(tv_ca_name.getText(), tv_ca_name.getTag());
                break;
            case R.id.cd_au:
                StatisticUtils.actionEvent(mActivity, "8_A_specific_rank_btn");
                goSchoolRank(tv_au_name.getText(), tv_au_name.getTag());
                break;
            case R.id.tv_home_rate:
                StatisticUtils.actionEvent(mActivity, "8_A_acceptance_rate_btn");
                Intent toTest = new Intent(mActivity, CommonSearchActivity.class);
                toTest.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.HOME_RATE_FLAG);
                toTest.putExtra("searchHint", "请输入要测试的学校");
                startActivity(toTest);
                break;
            case R.id.llyt_home_search:
                StatisticUtils.actionEvent(mActivity, "8_A_search_btn");
                Intent toSearch = new Intent(mActivity, HomeSearchActivity.class);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(mActivity, R.anim.fade_in, R.anim.fade_out);
                ActivityCompat.startActivity(mActivity, toSearch, compat.toBundle());
                break;
            case R.id.tv_home_school:
                StatisticUtils.actionEvent(mActivity, "8_A_school_library_btn");
                startActivity(new Intent(mActivity, SchoolListActivity.class));
                break;
            case R.id.tv_home_rank:
                StatisticUtils.actionEvent(mActivity, "8_A_rank_btn");
                startActivity(new Intent(mActivity, RankTypeActivity.class));
                break;
            case R.id.tv_home_gpa:
                StatisticUtils.actionEvent(mActivity, "8_A_gpa_btn");
//                startActivity(new Intent(mActivity, GpaCalculationActivity.class));
                startActivity(new Intent(mActivity, StudyConsultantsActivity.class));
                break;
            case R.id.tv_home_srt_spe:
                if (!ParameterUtils.CACHE_NULL.equals(SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL))) {
                    StatisticUtils.actionEvent(mActivity, "8_A_choose_professional_btn");
                    startActivity(new Intent(mActivity, SrtChooseSpecialActivity.class));
                } else {
                    DialogCreator.createLoginDialog(mActivity);
                }
                break;
            case R.id.tv_choose_school:
                if (!ParameterUtils.CACHE_NULL.equals(SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL))) {
                    StatisticUtils.actionEvent(mActivity, "8_A_choose_school_btn");
                    startActivity(new Intent(mActivity, SrtChooseSchoolActivity.class));
                } else {
                    DialogCreator.createLoginDialog(mActivity);
                }
                break;
            case R.id.tv_home_special:
                StatisticUtils.actionEvent(mActivity, "8_A_professional_library_btn");
                startActivity(new Intent(mActivity, SpecialListActivity.class)
                        .putExtra("flag", "home")
                        .putExtra("title", "专业库"));
                break;
            case R.id.tv_see_more_rank:
                StatisticUtils.actionEvent(mActivity, "8_A_rank_more_btn");
                startActivity(new Intent(mActivity, RankTypeActivity.class));
                break;
            case R.id.tv_see_more_topic:
                StatisticUtils.actionEvent(mActivity, "8_A_subject_more_btn");
                startActivity(new Intent(mActivity, ThematicCenterActivity.class));
                break;
            case R.id.tv_home_topic:
                StatisticUtils.actionEvent(mActivity, "8_A_subject_btn");
                startActivity(new Intent(mActivity, ThematicCenterActivity.class));
                break;
            default:
                break;
        }
    }

    private void goSchoolRank(CharSequence title, Object obj) {
        if (obj != null) {
            Intent touk = new Intent(mActivity, RankActivity.class);
            touk.putExtra("title", title);
            touk.putExtra("id", obj.toString());
            startActivity(touk);
            obj = null;
        }
    }

    //解决pagerView与swipeRefreshLayout滑动冲突
    private void handleTouch() {
        pagerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (pagerView.isPlaying()) {
                            pagerView.pause();
                        }
                        swipeRefreshLayout.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (!pagerView.isPlaying()) {
                            pagerView.resume();
                        }
                        swipeRefreshLayout.setEnabled(true);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    //处理滑动标题栏颜色渐变
    private void handleScroll() {
        rcv_hots.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalDy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滑动时头部搜索栏的变换
                totalDy -= dy;
                //控制刷新何时出现
                swipeRefreshLayout.setEnabled(totalDy == 0);
                int diff = pagerView.getHeight() - llyt_home_top.getHeight();
                if (-totalDy > 0 && -totalDy <= diff) {
                    top_alpha = -totalDy * 255 / diff;
                    top_line.setVisibility(View.GONE);
                } else if (-totalDy > diff) {
                    top_alpha = 255;
                    top_line.setVisibility(View.VISIBLE);
                } else {
                    top_alpha = 0;
                }
                //翻转动画
                if (top_alpha > 128) {
                    // alpha > 0.5设置蓝色图标
                    if (isWhite) {
                        ((MainActivity) mActivity).controlDarkMode(true);
                        isDarkMode = true;
                        llyt_home_search.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_raduis_search_gray));
                        ObjectAnimator animator = ObjectAnimator.ofFloat(llyt_home_search, "alpha", 0.5f, 1);
                        animator.setDuration(1000);
                        animator.start();
                    }
                    isWhite = false;
                } else { // 否则设置白色
                    if (!isWhite) {
                        ((MainActivity) mActivity).controlDarkMode(false);
                        isDarkMode = false;
                        llyt_home_search.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_raduis_search_white95));
                        ObjectAnimator animator = ObjectAnimator.ofFloat(llyt_home_search, "alpha", 0.5f, 1);
                        animator.setDuration(1000);
                        animator.start();
                    }
                    isWhite = true;
                }
                llyt_home_top.setBackgroundColor(Color.argb(top_alpha, 255, 255, 255));
            }
        });
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        llyt_home_top.setBackgroundColor(Color.argb(top_alpha, 255, 255, 255));
    }

    @Override
    public void setPresenter(HomeFragmentContract.Presenter presenter) {
        if (presenter != null) {
            homeP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (homeP != null) {
            swipeRefreshLayout.setRefreshing(false);
            ToastUtils.showToast(mActivity, message);
        }
    }

    @Override
    public void showBanner() {
        if (homeP != null) {
            myBannerAdapter.notifyDataSetChanged();
            //展示banner时做一些UI布局调整
            top_line.setVisibility(View.GONE);
            if (isAdded()) {
                llyt_home_search.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_raduis_search_white95));
            }
            LinearLayout.LayoutParams content_params = (LinearLayout.LayoutParams) llyt_header_grid.getLayoutParams();
            content_params.topMargin = 0;
            llyt_header_grid.setLayoutParams(content_params);
            content_params = null;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pagerView.getLayoutParams();
            params.height = ScreenUtils.getScreenWidth() / 2;
            pagerView.setLayoutParams(params);
            params = null;
        }
    }

    @Override
    public void showHotRank(HomeHotRankInfo world, HomeHotRankInfo us, HomeHotRankInfo uk, HomeHotRankInfo ca, HomeHotRankInfo au) {
        if (homeP != null) {
            llyt_home_rank.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            tv_world_name.setTag(world.getId());
            tv_world_name.setText(world.getYear() + world.getType() + world.getTitle());
            tv_us_name.setTag(us.getId());
            tv_us_name.setText(us.getYear() + us.getType() + us.getTitle());
            tv_uk_name.setTag(uk.getId());
            tv_uk_name.setText(uk.getYear() + uk.getType() + uk.getTitle());
            tv_ca_name.setTag(ca.getId());
            tv_ca_name.setText(ca.getYear() + ca.getType() + ca.getTitle());
            tv_au_name.setTag(au.getId());
            tv_au_name.setText(au.getYear() + au.getType() + au.getTitle());
            world = null;
            us = null;
            uk = null;
            ca = null;
            au = null;
        }
    }

    @Override
    public void showHomeHot(List<HomeHotListInfo> mDatas) {
        if (homeP != null) {
            swipeRefreshLayout.setRefreshing(false);
            hotListInfos.clear();
            hotListInfos.addAll(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showHotSub(List<HomeHotInfo> mDatas) {
        if (homeP != null) {
            if (mDatas != null && mDatas.size() > 0) {
                llyt_home_footer.setVisibility(View.VISIBLE);
                if (!hasAddFooter) {
                    mHeader.addFootView(footView);
                    hasAddFooter = true;
                }
                mHeader.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
                hotSubList.clear();
                hotSubList.addAll(mDatas);
                hotSubAdapter.notifyDataSetChanged();
            } else {
                llyt_home_footer.setVisibility(View.GONE);
            }
        }
        mDatas = null;
    }

}

package com.smartstudy.xxd.ui.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;

import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ActivityInfo;
import com.smartstudy.xxd.mvp.contract.ActivityListFragmentContract;
import com.smartstudy.xxd.mvp.presenter.ActivityListPresenter;
import com.smartstudy.xxd.ui.activity.ActivityDetailActivity;

import java.util.ArrayList;
import java.util.List;


public class ActivityListFragment extends BaseFragment implements ActivityListFragmentContract.View {
    private LoadMoreRecyclerView lmrvActivities;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<ActivityInfo> mAdapter;
    private LoadMoreWrapper<ActivityInfo> loadMoreWrapper;
    private EmptyWrapper<ActivityInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;

    private String title;
    private ActivityListFragmentContract.Presenter presenter;
    private List<ActivityInfo> activityInfoList;
    private int mPage = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_activity_list;
    }

    public static ActivityListFragment getInstance(Bundle bundle) {
        ActivityListFragment fragment = new ActivityListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter = null;
        }
        if (lmrvActivities != null) {
            lmrvActivities.removeAllViews();
            lmrvActivities = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (activityInfoList != null) {
            activityInfoList.clear();
            activityInfoList = null;
        }
        super.onDetach();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, lmrvActivities,
            false);
        presenter.showLoading(mActivity, emptyView);
        presenter.getActivities(title, mPage, ParameterUtils.PULL_DOWN);
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void initView() {
        swipeRefreshLayout = rootView.findViewById(R.id.srlt_activity);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getActivities(title, mPage, ParameterUtils.PULL_DOWN);
            }
        });
        lmrvActivities = rootView.findViewById(R.id.rclv_activity);
        lmrvActivities.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvActivities.setLayoutManager(mLayoutManager);
        lmrvActivities.setItemAnimator(new DefaultItemAnimator());
        lmrvActivities.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
            .size(DensityUtils.dip2px(0.5f))
            .colorResId(R.color.horizontal_line_color)
            .build());
        initAdapter();
        title = getArguments().getString("title");
        new ActivityListPresenter(this);
    }

    private void initAdapter() {
        activityInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<ActivityInfo>(mActivity, R.layout.item_activity_list, activityInfoList) {
            @Override
            protected void convert(ViewHolder holder, ActivityInfo info, int position) {
                if (info != null) {
                    holder.setText(R.id.tv_title, info.getName());
                    SpannableString ssTime = new SpannableString("活动时间：" + info.getActivityTime());
                    SpannableString ssAddr = new SpannableString("活动地点：" + info.getPlace());
                    StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                    ssTime.setSpan(styleSpan, 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    ssAddr.setSpan(styleSpan, 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    holder.setText(R.id.tv_time, ssTime);
                    holder.setText(R.id.tv_addr, ssAddr);
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvActivities.setAdapter(loadMoreWrapper);
        //加载更多
        lmrvActivities.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    lmrvActivities.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getActivities(title, mPage, ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //跳转到webview
                ActivityInfo info = activityInfoList.get(position);
                if (info != null) {
                    Intent toDetail = new Intent(mActivity, ActivityDetailActivity.class);
                    toDetail.putExtra("id", info.getId() + "");
                    startActivity(toDetail);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void showList(List<ActivityInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvActivities.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                activityInfoList.clear();
                activityInfoList.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    if (activityInfoList.size() > 0) {
                        lmrvActivities.loadComplete(false);
                    } else {
                        lmrvActivities.loadComplete(true);
                    }
                } else {
                    lmrvActivities.loadComplete(true);
                    activityInfoList.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        data = null;
    }

    /**
     * 展示空页面
     *
     * @param view
     */
    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        lmrvActivities.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public void setPresenter(ActivityListFragmentContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            lmrvActivities.loadComplete(true);
            ToastUtils.showToast(message);
        }
    }
}

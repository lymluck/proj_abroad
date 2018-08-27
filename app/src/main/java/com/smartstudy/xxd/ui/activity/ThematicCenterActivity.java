package com.smartstudy.xxd.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customView.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customView.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ThematicCenterInfo;
import com.smartstudy.xxd.mvp.contract.ThematicCenterContract;
import com.smartstudy.xxd.mvp.presenter.ThematicCenterPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqy on 2017/10/10.
 */

public class ThematicCenterActivity extends UIActivity implements ThematicCenterContract.View {
    private LoadMoreRecyclerView rclvThematicCenter;
    private SwipeRefreshLayout srltThematicCentet;
    private NoScrollLinearLayoutManager mLayoutManager;
    private int mPage = 1;
    private CommonAdapter<ThematicCenterInfo> mAdapter;
    private LoadMoreWrapper<ThematicCenterInfo> loadMoreWrapper;
    private View emptyView;
    private EmptyWrapper<ThematicCenterInfo> emptyWrapper;
    private List<ThematicCenterInfo> thematicCenterInfoList;
    private ThematicCenterContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thematic_center);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        if (rclvThematicCenter != null) {
            rclvThematicCenter.removeAllViews();
            rclvThematicCenter = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (thematicCenterInfoList != null) {
            thematicCenterInfoList.clear();
            thematicCenterInfoList = null;
        }
    }

    @Override
    protected void initViewAndData() {
        ((TextView) this.findViewById(R.id.topdefault_centertitle)).setText("专题中心");
        srltThematicCentet = (SwipeRefreshLayout) this.findViewById(R.id.srlt_thematic_center);
        srltThematicCentet.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        srltThematicCentet.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getThematicCenter(mPage, ParameterUtils.PULL_DOWN);
            }
        });

        rclvThematicCenter = (LoadMoreRecyclerView) this.findViewById(R.id.rclv_thematic_center);
        rclvThematicCenter.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvThematicCenter.setLayoutManager(mLayoutManager);
        initAdapter();
        new ThematicCenterPresenter(this);
        //加载动画
        emptyView = this.getLayoutInflater().inflate(R.layout.layout_empty, rclvThematicCenter, false);
        presenter.showLoading(this, emptyView);
        presenter.getThematicCenter(mPage, ParameterUtils.PULL_DOWN);
        this.findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }


    private void initAdapter() {
        thematicCenterInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<ThematicCenterInfo>(this, R.layout.item_thematic_center, thematicCenterInfoList) {
            @Override
            protected void convert(ViewHolder holder, ThematicCenterInfo thematicCenterInfo, int position) {
                if (position == getItemCount() - 1) {
                    holder.getView(R.id.view_line).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.view_line).setVisibility(View.VISIBLE);
                }
                DisplayImageUtils.formatImgUrlNoHolder(ThematicCenterActivity.this, thematicCenterInfo.getImageUrl(), (ImageView) holder.getView(R.id.iv_thematic_center));
                holder.setText(R.id.tv_thematic_see, String.format(getString(R.string.thematic_see),
                        TextUtils.isEmpty(thematicCenterInfo.getVisitCount()) ? "0" : thematicCenterInfo.getVisitCount()));
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvThematicCenter.setAdapter(loadMoreWrapper);
        rclvThematicCenter.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (srltThematicCentet.isRefreshing()) {
                    rclvThematicCenter.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getThematicCenter(mPage, ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ThematicCenterInfo info = thematicCenterInfoList.get(position);
                Intent toMoreDetails = new Intent(ThematicCenterActivity.this, ShowWebViewActivity.class);
                toMoreDetails.putExtra("web_url", info.getSubjectUrl());
                toMoreDetails.putExtra("title", info.getName());
                toMoreDetails.putExtra("url_action", "get");
                startActivity(toMoreDetails);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
        }
    }

    @Override
    public void setPresenter(ThematicCenterContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            srltThematicCentet.setRefreshing(false);
            rclvThematicCenter.loadComplete(true);
            ToastUtils.showToast(this, message);
        }
    }

    @Override
    public void showThematicCenter(List<ThematicCenterInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvThematicCenter.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                thematicCenterInfoList.clear();
                thematicCenterInfoList.addAll(data);
                srltThematicCentet.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    if (thematicCenterInfoList.size() > 0) {
                        rclvThematicCenter.loadComplete(false);
                    } else {
                        rclvThematicCenter.loadComplete(true);
                    }
                } else {
                    rclvThematicCenter.loadComplete(true);
                    thematicCenterInfoList.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        data = null;
    }


    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclvThematicCenter.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }
}

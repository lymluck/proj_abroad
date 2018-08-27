package com.smartstudy.xxd.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.SchooolRankInfo;
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
import com.smartstudy.xxd.entity.CountryTypeInfo;
import com.smartstudy.xxd.mvp.contract.HotSchoolRankContract;
import com.smartstudy.xxd.mvp.presenter.HotSchoolRankPresenter;
import com.smartstudy.xxd.ui.activity.CollegeDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author yqy
 * @date on 2018/6/12
 * @describe 热门院校排行
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HotSchoolRankFragment extends BaseFragment implements HotSchoolRankContract.View {
    private LoadMoreRecyclerView lmrvRank;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<SchooolRankInfo> mAdapter;
    private LoadMoreWrapper<SchooolRankInfo> loadMoreWrapper;
    private EmptyWrapper<SchooolRankInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;

    private CountryTypeInfo titleInfo;
    private HotSchoolRankContract.Presenter presenter;
    private List<SchooolRankInfo> schooolRankInfos;
    private int mPage = 1;
    private String from;


    public static HotSchoolRankFragment getInstance(Bundle bundle) {
        HotSchoolRankFragment fragment = new HotSchoolRankFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter = null;
        }
        if (lmrvRank != null) {
            lmrvRank.removeAllViews();
            lmrvRank = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (schooolRankInfos != null) {
            schooolRankInfos.clear();
            schooolRankInfos = null;
        }
        if (mLayoutManager != null) {
            mLayoutManager.removeAllViews();
            mLayoutManager = null;
        }
        if (titleInfo != null) {
            titleInfo = null;
        }
        super.onDetach();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, lmrvRank,
            false);
        presenter.showLoading(mActivity, emptyView);
        presenter.getHotSchoolRank(ParameterUtils.CACHED_ELSE_NETWORK, titleInfo.getCountryId(), from, mPage,
            ParameterUtils.PULL_DOWN);
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_hot_coutry_rank;
    }

    @Override
    protected void initView() {
        from = mActivity.getIntent().getStringExtra("from");
        swipeRefreshLayout = rootView.findViewById(R.id.srlt_rank);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getHotSchoolRank(ParameterUtils.NETWORK_ELSE_CACHED, titleInfo.getCountryId(), from, mPage,
                    ParameterUtils.PULL_DOWN);
            }
        });
        lmrvRank = rootView.findViewById(R.id.rclv_rank);
        lmrvRank.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvRank.setLayoutManager(mLayoutManager);
        lmrvRank.setItemAnimator(new DefaultItemAnimator());
        lmrvRank.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
            .size(DensityUtils.dip2px(0.5f))
            .margin(DensityUtils.dip2px(16), 0)
            .colorResId(R.color.horizontal_line_color)
            .build());
        initAdapter();
        titleInfo = getArguments().getParcelable(TITLE);
        new HotSchoolRankPresenter(this);
    }

    private void initAdapter() {
        schooolRankInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<SchooolRankInfo>(mActivity, R.layout.item_hot_school, schooolRankInfos) {
            @Override
            protected void convert(ViewHolder holder, SchooolRankInfo schooolRankInfo, int position) {
                if (schooolRankInfo != null) {
                    if (holder.getView(R.id.llyt_home_rate).getVisibility() == View.GONE) {
                        holder.getView(R.id.llyt_home_rate).setVisibility(View.VISIBLE);
                    }
                    holder.setCircleImageUrl(R.id.iv_logo, schooolRankInfo.getLogo(), true);
                    holder.setText(R.id.tv_school_name, schooolRankInfo.getChineseName());
                    holder.setText(R.id.tv_English_name, schooolRankInfo.getEnglishName());
                    if (schooolRankInfo.getCityName() != null && !"".equals(schooolRankInfo.getCityName())) {
                        holder.setText(R.id.tv_area, schooolRankInfo.getProvinceName()
                            + "-" + schooolRankInfo.getCityName());
                    } else {
                        holder.setText(R.id.tv_area, schooolRankInfo.getProvinceName());
                    }
                    holder.setText(R.id.tv_rate, String.format(getString(R.string.rate),
                        TextUtils.isEmpty(schooolRankInfo.getTIE_ADMINSSION_RATE()) ? "暂无" : schooolRankInfo.getTIE_ADMINSSION_RATE()));

                    TextView tvCount = holder.getView(R.id.tv_count);
                    if ("hottest".equals(from)) {
                        tvCount.setBackgroundColor(Color.parseColor("#FF6453"));
                    } else {
                        tvCount.setBackgroundColor(Color.parseColor("#FF9A4C"));
                    }
                    tvCount.setText(schooolRankInfo.getOrderRank());
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvRank.setAdapter(loadMoreWrapper);
        //加载更多
        lmrvRank.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    lmrvRank.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getHotSchoolRank(ParameterUtils.CACHED_ELSE_NETWORK, titleInfo.getCountryId(), from, mPage,
                    ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                SchooolRankInfo info = schooolRankInfos.get(position);
                Bundle params = new Bundle();
                params.putString("id", info.getId() + "");
                Intent toMoreDetails = new Intent(mActivity, CollegeDetailActivity.class);
                toMoreDetails.putExtras(params);
                startActivity(toMoreDetails);
                info = null;
                params = null;
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void getHotSchoolRankSuccess(List<SchooolRankInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvRank.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                schooolRankInfos.clear();
                schooolRankInfos.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    if (schooolRankInfos.size() > 0) {
                        lmrvRank.loadComplete(false);
                    } else {
                        lmrvRank.loadComplete(true);
                    }
                } else {
                    lmrvRank.loadComplete(true);
                    schooolRankInfos.addAll(data);
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
        lmrvRank.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }


    @Override
    public void setPresenter(HotSchoolRankContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            lmrvRank.loadComplete(true);
            ToastUtils.showToast(message);
        }
    }
}

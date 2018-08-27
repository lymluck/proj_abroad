package com.smartstudy.xxd.ui.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
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
import com.smartstudy.xxd.entity.RemarkListInfo;
import com.smartstudy.xxd.mvp.contract.RemarkListContract;
import com.smartstudy.xxd.mvp.presenter.RemarkListPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/5/24
 * @describe TODO 备考列表
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class RemarkListActivity extends BaseActivity implements RemarkListContract.View {
    private RemarkListContract.Presenter presenter;
    private LoadMoreRecyclerView rclvRemark;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<RemarkListInfo> mAdapter;
    private LoadMoreWrapper<RemarkListInfo> loadMoreWrapper;
    private EmptyWrapper<RemarkListInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private List<RemarkListInfo> remarkListInfos;
    private int mPage = 1;
    private String id;


    @Override
    public void setPresenter(RemarkListContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_list);
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            rclvRemark.loadComplete(true);
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void getRemarkListSuccess(List<RemarkListInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(this, emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvRemark.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                remarkListInfos.clear();
                remarkListInfos.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rclvRemark.loadComplete(false);
                } else {
                    rclvRemark.loadComplete(true);
                    remarkListInfos.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclvRemark.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    protected void initViewAndData() {
        id = getIntent().getStringExtra("remarkId");
        String title = getIntent().getStringExtra("title");
        String dateStr = getIntent().getStringExtra("dateStr");
        rclvRemark = findViewById(R.id.rclv_remark);
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(String.format(getString(R.string.remark_title, title, dateStr)));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlt_remark);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getRemarkList(id, mPage, ParameterUtils.PULL_DOWN);
            }
        });

        rclvRemark.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvRemark.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16f), 0).colorResId(R.color.bg_home_search).build());
        rclvRemark.setLayoutManager(mLayoutManager);
        initAdapter();
        new RemarkListPresenter(this);
        emptyView = getLayoutInflater().inflate(R.layout.layout_empty, rclvRemark, false);
        presenter.showLoading(this, emptyView);
        presenter.getRemarkList(id, mPage, ParameterUtils.PULL_DOWN);
    }

    private void initAdapter() {
        remarkListInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<RemarkListInfo>(this, R.layout.item_remarks_list, remarkListInfos) {
            @Override
            protected void convert(ViewHolder holder, RemarkListInfo remarkListInfo, int position) {
                holder.setPersonImageUrl(R.id.iv_avatar, remarkListInfo.getAvatar(), true);
                holder.setText(R.id.tv_name, remarkListInfo.getName());
                TextView tvCountDay = holder.getView(R.id.tv_count_day);
                Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/GothamMedium.otf");
                // 应用字体
                tvCountDay.setTypeface(typeFace);
                tvCountDay.setText(remarkListInfo.getCheckinCount() + "");
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvRemark.setAdapter(loadMoreWrapper);
        rclvRemark.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    rclvRemark.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getRemarkList(id, mPage, ParameterUtils.PULL_UP);
            }
        });

    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rclvRemark != null) {
            rclvRemark.removeAllViews();
            rclvRemark = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (remarkListInfos != null) {
            remarkListInfos.clear();
            remarkListInfos = null;
        }
        if (presenter != null) {
            presenter = null;
        }
    }
}

package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
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
import com.smartstudy.xxd.entity.ColumnListItemInfo;
import com.smartstudy.xxd.mvp.contract.ColumnListContract;
import com.smartstudy.xxd.mvp.presenter.ColumnListPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.PAGES_COUNT;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe 专栏列表页面
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ColumnListActivity extends BaseActivity implements ColumnListContract.View {

    private LoadMoreRecyclerView lmrvColumn;
    private CommonAdapter<ColumnListItemInfo> mAdapter;
    private LoadMoreWrapper<ColumnListItemInfo> loadMoreWrapper;
    private EmptyWrapper<ColumnListItemInfo> emptyWrapper;
    private NoScrollLinearLayoutManager nsllManager;
    private View emptyView;
    private List<ColumnListItemInfo> columnInfoList;
    private ColumnListContract.Presenter presenter;
    private int mPage = PAGES_COUNT;
    private SwipeRefreshLayout srtColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_list);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        if (lmrvColumn != null) {
            lmrvColumn.removeAllViews();
            lmrvColumn = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (columnInfoList != null) {
            columnInfoList.clear();
            columnInfoList = null;
        }
        if (nsllManager != null) {
            nsllManager = null;
        }

        if (loadMoreWrapper != null) {
            loadMoreWrapper = null;
        }
    }

    @Override
    protected void initViewAndData() {
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("专栏");
        this.srtColumn = findViewById(R.id.srt_column);
        srtColumn.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        //刷新数据
        srtColumn.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getColumn(mPage, ParameterUtils.PULL_DOWN);

            }
        });
        lmrvColumn = findViewById(R.id.lmrv_column);
        lmrvColumn.setHasFixedSize(true);
        nsllManager = new NoScrollLinearLayoutManager(this);
        nsllManager.setScrollEnabled(true);
        nsllManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvColumn.setLayoutManager(nsllManager);
        lmrvColumn.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f))
            .colorResId(R.color.horizontal_line_color).build());
        new ColumnListPresenter(this);
        initAdapter();
        emptyView = mInflater.inflate(R.layout.layout_empty, lmrvColumn, false);
        presenter.showLoading(this, emptyView);
        presenter.getColumn(mPage, ParameterUtils.PULL_DOWN);
    }


    private void initAdapter() {
        columnInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<ColumnListItemInfo>(this, R.layout.item_column_list, columnInfoList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, ColumnListItemInfo info, int position) {
                if (info != null) {
                    holder.setText(R.id.tv_title, info.getTitle());
                    holder.setImageUrl(R.id.iv_cover, info.getCoverUrl(), true);
                    holder.setPersonImageUrl(R.id.iv_user_avatar, info.getAuthor().getAvatar(), true);
                    holder.setText(R.id.tv_user_name, info.getAuthor().getName());
                    holder.setText(R.id.tv_see, info.getVisitCount() + "");
                    holder.setText(R.id.tv_like, info.getLikesCount() + "");
                }
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < columnInfoList.size()) {
                    ColumnListItemInfo info = columnInfoList.get(position);
                    if (info != null) {
                        startActivity(new Intent(ColumnListActivity.this, ColumnActivity.class)
                            .putExtra("id", info.getId() + ""));
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvColumn.setAdapter(loadMoreWrapper);
    }

    @Override
    public void initEvent() {
        lmrvColumn.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (srtColumn.isRefreshing()) {
                    lmrvColumn.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getColumn(mPage, ParameterUtils.PULL_UP);
            }
        });

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
    public void setPresenter(ColumnListContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            srtColumn.setRefreshing(false);
            lmrvColumn.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void showList(List<ColumnListItemInfo> datas, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            nsllManager.setScrollEnabled(true);
            int len = datas.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvColumn.loadComplete(true);
                    nsllManager.setScrollEnabled(false);
                }
                srtColumn.setRefreshing(false);
                this.columnInfoList.clear();
                this.columnInfoList.addAll(datas);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    lmrvColumn.loadComplete(false);
                } else {
                    lmrvColumn.loadComplete(true);
                    this.columnInfoList.addAll(datas);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        datas = null;
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        lmrvColumn.loadComplete(true);
        nsllManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public void reload() {
        presenter.showLoading(this, emptyView);
        presenter.getColumn(mPage, ParameterUtils.PULL_DOWN);
    }
}

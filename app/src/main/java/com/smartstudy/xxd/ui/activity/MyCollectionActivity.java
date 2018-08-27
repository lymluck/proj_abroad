package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CollectionInfo;
import com.smartstudy.xxd.mvp.contract.MyCollectionContract;
import com.smartstudy.xxd.mvp.presenter.MyCollectionPresenter;

import java.util.ArrayList;
import java.util.List;

public class MyCollectionActivity extends UIActivity implements MyCollectionContract.View {

    private LoadMoreRecyclerView rclvcollection;
    private SwipeRefreshLayout srltcollection;
    private CommonAdapter<CollectionInfo> mAdapter;
    private LoadMoreWrapper<CollectionInfo> loadMoreWrapper;
    private EmptyWrapper<CollectionInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;

    private int mPage = 1;
    private MyCollectionContract.Presenter presenter;
    private List<CollectionInfo> collectionInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        if (rclvcollection != null) {
            rclvcollection.removeAllViews();
            rclvcollection = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (collectionInfos != null) {
            collectionInfos.clear();
            collectionInfos = null;
        }
    }

    @Override
    protected void initViewAndData() {
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("我的收藏");
        this.srltcollection = (SwipeRefreshLayout) findViewById(R.id.srlt_collection);
        srltcollection.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        srltcollection.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getCollection(ParameterUtils.NETWORK_ELSE_CACHED, mPage, ParameterUtils.PULL_DOWN);

            }
        });
        this.rclvcollection = (LoadMoreRecyclerView) findViewById(R.id.rclv_collection);
        rclvcollection.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvcollection.setLayoutManager(mLayoutManager);
        initAdapter();
        new MyCollectionPresenter(this);
        //加载动画
        emptyView = mInflater.inflate(R.layout.layout_empty, rclvcollection, false);
        presenter.showLoading(this, emptyView);
        presenter.getCollection(ParameterUtils.NETWORK_ELSE_CACHED, mPage, ParameterUtils.PULL_DOWN);
    }

    private void initAdapter() {
        collectionInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<CollectionInfo>(this, R.layout.item_collection_list, collectionInfos) {
            @Override
            protected void convert(ViewHolder holder, CollectionInfo collectionInfo, int position) {
                holder.setText(R.id.tv_type_name, collectionInfo.getCollectTypeName(getApplicationContext()));
                holder.setText(R.id.tv_time, collectionInfo.getCollectTime());
                holder.setText(R.id.tv_content, collectionInfo.getDisplayText());
                ImageView iv = holder.getView(R.id.iv_cover);
                if (collectionInfo.getDisplayImage() != null) {
                    holder.setImageUrl(iv, collectionInfo.getDisplayImage(), true);
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.GONE);
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvcollection.setAdapter(loadMoreWrapper);
        rclvcollection.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (srltcollection.isRefreshing()) {
                    rclvcollection.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getCollection(ParameterUtils.NETWORK_ELSE_CACHED, mPage, ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                CollectionInfo info = collectionInfos.get(position);
                if (info.getUrl() != null) {
                    if ("question".equals(info.getCollectType())) {
                        Intent toMoreDetails = new Intent(MyCollectionActivity.this, QaDetailActivity.class);
                        toMoreDetails.putExtra("id", info.getId() + "");
                        startActivity(toMoreDetails);
                    } else {
                        Intent toMoreDetails = new Intent(MyCollectionActivity.this, ShowWebViewActivity.class);
                        toMoreDetails.putExtra("web_url", info.getUrl());
                        toMoreDetails.putExtra("url_action", "get");
                        startActivity(toMoreDetails);
                    }
                } else {
                    startActivity(new Intent(MyCollectionActivity.this, CommandSchoolListActivity.class)
                            .putExtra("id", info.getId() + "")
                            .putExtra("title", info.getName() + "推荐院校"));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
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
    public void setPresenter(MyCollectionContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            if (ParameterUtils.RESPONSE_CODE_NOLOGIN.equals(errCode)) {
                DialogCreator.createLoginDialog(this);
            }
            srltcollection.setRefreshing(false);
            rclvcollection.loadComplete(true);
            ToastUtils.showToast(this, message);
        }
    }

    @Override
    public void getCollectionSuccess(List<CollectionInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(this, emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvcollection.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                collectionInfos.clear();
                collectionInfos.addAll(data);
                srltcollection.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    if (collectionInfos.size() > 0) {
                        rclvcollection.loadComplete(false);
                    } else {
                        rclvcollection.loadComplete(true);
                    }
                } else {
                    rclvcollection.loadComplete(true);
                    collectionInfos.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclvcollection.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }
}

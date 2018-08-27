package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;
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
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HighSchoolRankDetailInfo;
import com.smartstudy.xxd.mvp.contract.HighSchoolRankDetailContract;
import com.smartstudy.xxd.mvp.presenter.HighSchoolDetailPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.CATEGORY_ID;
import static com.smartstudy.xxd.utils.AppContants.NAME;
import static com.smartstudy.xxd.utils.AppContants.PAGES_COUNT;
import static com.smartstudy.xxd.utils.AppContants.TEL;
import static com.smartstudy.xxd.utils.AppContants.TITLE;
import static com.smartstudy.xxd.utils.AppContants.USER_ACCOUNT;
import static com.smartstudy.xxd.utils.AppContants.USER_NAME;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe 高中院校排名具体详情列表页面
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HighSchoolRankDetailActivity extends BaseActivity implements HighSchoolRankDetailContract.View {

    private LoadMoreRecyclerView lmrvSchools;
    private CommonAdapter<HighSchoolRankDetailInfo> mAdapter;
    private LoadMoreWrapper<HighSchoolRankDetailInfo> loadMoreWrapper;
    private EmptyWrapper<HighSchoolRankDetailInfo> emptyWrapper;
    private NoScrollLinearLayoutManager nsllManager;
    private View emptyView;
    private List<HighSchoolRankDetailInfo> highSchoolRankDetailInfos;
    private HighSchoolRankDetailContract.Presenter presenter;
    private int mPage = PAGES_COUNT;
    private String categoryId;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_school_rank_detail);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        if (lmrvSchools != null) {
            lmrvSchools.removeAllViews();
            lmrvSchools = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (highSchoolRankDetailInfos != null) {
            highSchoolRankDetailInfos.clear();
            highSchoolRankDetailInfos = null;
        }
        if (nsllManager != null) {
            nsllManager = null;
        }

        if (loadMoreWrapper != null) {
            loadMoreWrapper = null;
        }
        if (nsllManager != null) {
            nsllManager = null;
        }

    }

    @Override
    protected void initViewAndData() {
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        categoryId = getIntent().getStringExtra(CATEGORY_ID);
        title = getIntent().getStringExtra(TITLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(title);
        lmrvSchools = (LoadMoreRecyclerView) findViewById(R.id.rclv_schools);
        lmrvSchools.setHasFixedSize(true);
        nsllManager = new NoScrollLinearLayoutManager(this);
        nsllManager.setScrollEnabled(true);
        nsllManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvSchools.setLayoutManager(nsllManager);
        lmrvSchools.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f))
            .margin(DensityUtils.dip2px(16f), 0)
            .colorResId(R.color.horizontal_line_color).build());
        new HighSchoolDetailPresenter(this);
        initAdapter();
        emptyView = mInflater.inflate(R.layout.layout_empty, lmrvSchools, false);
        presenter.showLoading(this, emptyView);
        presenter.getHighSchoolDetail(categoryId, mPage + "", ParameterUtils.PULL_DOWN);
    }


    private void initAdapter() {
        highSchoolRankDetailInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<HighSchoolRankDetailInfo>(this, R.layout.item_high_school_detail, highSchoolRankDetailInfos, mInflater) {
            @Override
            protected void convert(ViewHolder holder, HighSchoolRankDetailInfo highSchoolRankDetailInfo, int position) {
                if (highSchoolRankDetailInfo != null) {
                    holder.setText(R.id.tv_school_name, highSchoolRankDetailInfo.getChineseName());
                    holder.setText(R.id.tv_number, highSchoolRankDetailInfo.getRank());
                    holder.getView(R.id.tv_ask).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HashMap<String, String> clientInfo = new HashMap<>();
                            clientInfo.put(NAME, (String) SPCacheUtils.get(USER_NAME, ""));
                            clientInfo.put(TEL, (String) SPCacheUtils.get(USER_ACCOUNT, ""));
                            Intent intent = new MQIntentBuilder(HighSchoolRankDetailActivity.this)
                                .setClientInfo(clientInfo)
                                .build();
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent();
                intent.putExtra("id", highSchoolRankDetailInfos.get(position).getHighschoolId());
                intent.setClass(HighSchoolRankDetailActivity.this, HighSchoolDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvSchools.setAdapter(loadMoreWrapper);
    }

    @Override
    public void initEvent() {
        lmrvSchools.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                mPage = mPage + 1;
                presenter.getHighSchoolDetail(categoryId, mPage + "", ParameterUtils.PULL_UP);
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
    public void setPresenter(HighSchoolRankDetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            lmrvSchools.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void getHighSchoolListSuccess(List<HighSchoolRankDetailInfo> highSchoolRankDetailInfos, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            nsllManager.setScrollEnabled(true);
            int len = highSchoolRankDetailInfos.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvSchools.loadComplete(true);
                    nsllManager.setScrollEnabled(false);
                }
                this.highSchoolRankDetailInfos.clear();
                this.highSchoolRankDetailInfos.addAll(highSchoolRankDetailInfos);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    lmrvSchools.loadComplete(false);
                } else {
                    lmrvSchools.loadComplete(true);
                    this.highSchoolRankDetailInfos.addAll(highSchoolRankDetailInfos);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        highSchoolRankDetailInfos = null;
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        lmrvSchools.loadComplete(true);
        nsllManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public void reload() {
        presenter.showLoading(this, emptyView);
        presenter.getHighSchoolDetail(categoryId, mPage + "", ParameterUtils.PULL_DOWN);
    }
}

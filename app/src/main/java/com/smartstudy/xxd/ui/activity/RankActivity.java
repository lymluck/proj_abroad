package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.HidingScrollListener;
import com.smartstudy.commonlib.entity.SchooolRankInfo;
import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.RankContract;
import com.smartstudy.xxd.mvp.presenter.RankPresenter;

import java.util.ArrayList;
import java.util.List;

@Route("RankActivity")
public class RankActivity extends UIActivity implements RankContract.View {
    private LoadMoreRecyclerView rclv_ranks;
    private CommonAdapter<SchooolRankInfo> mAdapter;
    private LoadMoreWrapper<SchooolRankInfo> loadMoreWrapper;
    private EmptyWrapper<SchooolRankInfo> emptyWrapper;
    private ImageView topdefault_leftbutton;
    private View searchView;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;

    private RankContract.Presenter rankP;
    private ArrayList<SchooolRankInfo> schoolsRankList;
    private int mPage = 1;
    private String categoryId;
    private WeakHandler mHandler;
    private boolean canPullUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        addActivity(this);
        StatisticUtils.actionEvent(this, "12_B_rank_school_list");
    }

    @Override
    protected void onDestroy() {
        if (rankP != null) {
            rankP = null;
        }
        if (rclv_ranks != null) {
            rclv_ranks.removeAllViews();
            rclv_ranks = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (schoolsRankList != null) {
            schoolsRankList.clear();
            schoolsRankList = null;
        }
        super.onDestroy();
        removeActivity(RankActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        TextView titleView = (TextView) findViewById(R.id.topdefault_centertitle);
        Intent data = getIntent();
        categoryId = data.getStringExtra("id");
        String title = data.getStringExtra("title");
        titleView.setText(TextUtils.isEmpty(title) ? "院校排名" : title);
        if (!TextUtils.isEmpty(categoryId) && data.getExtras() != null) {
            categoryId = data.getExtras().getString("id");
        }
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        searchView = findViewById(R.id.searchView);
        topdefault_leftbutton = (ImageView) findViewById(R.id.topdefault_leftbutton);
        rclv_ranks = (LoadMoreRecyclerView) findViewById(R.id.rclv_ranks);
        rclv_ranks.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_ranks.setLayoutManager(mLayoutManager);
        rclv_ranks.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
        new RankPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclv_ranks, false);
        rankP.showLoading(this, emptyView);
        rankP.getRank(ParameterUtils.NETWORK_ELSE_CACHED, categoryId, mPage, ParameterUtils.PULL_DOWN);
    }

    @Override
    public void initEvent() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        rclv_ranks.scrollBy(0, searchView.getHeight());
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        topdefault_leftbutton.setOnClickListener(this);
        searchView.setOnClickListener(this);
        rclv_ranks.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (canPullUp) {
                    mPage = mPage + 1;
                    rankP.getRank(ParameterUtils.CACHED_ELSE_NETWORK, categoryId, mPage, ParameterUtils.PULL_UP);
                    canPullUp = false;
                }
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                StatisticUtils.actionEvent(RankActivity.this, "12_A_school_detail_cell");
                SchooolRankInfo info = schoolsRankList.get(position);
                if (info.getSchool_id() != null && !TextUtils.isEmpty(info.getSchool_id())) {
                    Bundle params = new Bundle();
                    params.putString("id", info.getSchool_id());
                    Intent toMoreDetails = new Intent(RankActivity.this, SchoolDetailActivity.class);
                    toMoreDetails.putExtras(params);
                    startActivity(toMoreDetails);
                } else {
                    showTip(null, "该学校暂无更多详情可查看！");
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        rclv_ranks.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                searchView.animate()
                        .translationY(-ScreenUtils.getScreenHeight())
                        .setDuration(800)
                        .setInterpolator(new AccelerateInterpolator(2))
                        .start();
                searchView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onShow() {
                searchView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).setDuration(800).start();
                searchView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.searchView:
                mHandler.sendEmptyMessageAtTime(ParameterUtils.MSG_WHAT_REFRESH, 600);
                Intent toSearch = new Intent(RankActivity.this, CommonSearchActivity.class);
                toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.RANKS_FLAG);
                toSearch.putExtra("categoryId", categoryId);
                Pair<View, String> searchTop = Pair.create(searchView, "search_top");
                ActivityOptionsCompat compat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(RankActivity.this, searchTop);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, compat.toBundle());
                } else {
                    startActivity(toSearch);
                }
                break;
            default:
                break;
        }
    }

    private void initAdapter() {
        schoolsRankList = new ArrayList<>();
        mAdapter = new CommonAdapter<SchooolRankInfo>(this, R.layout.item_rank_list, schoolsRankList) {
            @Override
            protected void convert(ViewHolder holder, SchooolRankInfo rankInfo, int position) {
                if (rankInfo != null) {
                    if (!TextUtils.isEmpty(rankInfo.getRank())) {
                        if (rankInfo.getRank().contains("-")) {
                            ((TextView) holder.getView(R.id.tv_school_rank)).setTextSize(12);

                        } else {
                            ((TextView) holder.getView(R.id.tv_school_rank)).setTextSize(17);
                        }
                        holder.setText(R.id.tv_school_rank, rankInfo.getRank());
                    }
                    holder.setText(R.id.tv_school_name, rankInfo.getChineseName());
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclv_ranks.setAdapter(loadMoreWrapper);
    }

    @Override
    public void setPresenter(RankContract.Presenter presenter) {
        if (presenter != null) {
            this.rankP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (rankP != null) {
            rclv_ranks.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
            ToastUtils.showToast(this, message);
        }
    }

    @Override
    public void getRankSuccess(List<SchooolRankInfo> data, int request_state) {
        if (rankP != null) {
            rankP.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclv_ranks.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                schoolsRankList.clear();
                schoolsRankList.addAll(data);
                loadMoreWrapper.notifyDataSetChanged();
                if (searchView.getVisibility() == View.INVISIBLE) {
                    rclv_ranks.scrollBy(0, searchView.getHeight());
                }
                //判断是否可滑动， -1 表示 向上， 1 表示向下
                if (!rclv_ranks.canScrollVertically(-1)) {
                    searchView.setVisibility(View.VISIBLE);
                }
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rclv_ranks.loadComplete(false);
                } else {
                    rclv_ranks.loadComplete(true);
                    schoolsRankList.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
            canPullUp = true;
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclv_ranks.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    public void reload() {
        rankP.showLoading(this, emptyView);
        rankP.getRank(ParameterUtils.NETWORK_ELSE_CACHED, categoryId, mPage, ParameterUtils.PULL_DOWN);
    }
}

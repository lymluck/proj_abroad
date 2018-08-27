package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.smartstudy.xxd.entity.HomeHotProgramInfo;
import com.smartstudy.xxd.mvp.contract.MarjorProgramRankContract;
import com.smartstudy.xxd.mvp.presenter.MajorProgramRankPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

public class MajorProgramRankActivity extends BaseActivity implements MarjorProgramRankContract.View {

    private LoadMoreRecyclerView lmrvRanks;
    private CommonAdapter<HomeHotProgramInfo> mAdapter;
    private LoadMoreWrapper<HomeHotProgramInfo> loadMoreWrapper;
    private EmptyWrapper<HomeHotProgramInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;

    private MarjorProgramRankContract.Presenter rankP;
    private ArrayList<HomeHotProgramInfo> schoolsRankList;
    private int mPage = 1;
    private String categoryId;
    private boolean canPullUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_program_rank);
    }

    @Override
    protected void onDestroy() {
        if (rankP != null) {
            rankP = null;
        }
        if (lmrvRanks != null) {
            lmrvRanks.removeAllViews();
            lmrvRanks = null;
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
        removeActivity(MajorProgramRankActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(data.getStringExtra(TITLE) + "排名");
        categoryId = data.getStringExtra("id");
        lmrvRanks = findViewById(R.id.rclv_ranks);
        lmrvRanks.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvRanks.setLayoutManager(mLayoutManager);
        lmrvRanks.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
        new MajorProgramRankPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, lmrvRanks, false);
        rankP.showLoading(this, emptyView);
        rankP.getRank(ParameterUtils.NETWORK_ELSE_CACHED, categoryId, mPage, ParameterUtils.PULL_DOWN);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        lmrvRanks.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
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
                HomeHotProgramInfo info = schoolsRankList.get(position);
                Intent toProgram = new Intent(MajorProgramRankActivity.this, ProgramInfoActivity.class);
                toProgram.putExtra("id", info.getId());
                startActivity(toProgram);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
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

    private void initAdapter() {
        schoolsRankList = new ArrayList<>();
        mAdapter = new CommonAdapter<HomeHotProgramInfo>(this, R.layout.item_major_program_list, schoolsRankList) {
            @Override
            protected void convert(ViewHolder holder, HomeHotProgramInfo rankInfo, int position) {
                if (rankInfo != null) {
                    TextView tvRank = holder.getView(R.id.tv_school_rank);
                    if (TextUtils.isEmpty(rankInfo.getCategoryRank())) {
                        tvRank.setText("-");
                        tvRank.setTextSize(13f);
                    } else {
                        if (rankInfo.getCategoryRank().length() < 3) {
                            tvRank.setTextSize(13f);
                        } else {
                            tvRank.setTextSize(10f);
                        }
                        tvRank.setText(rankInfo.getCategoryRank());
                    }
                    TextView tvSchoolName = holder.getView(R.id.tv_school_name);
                    tvSchoolName.setTextSize(17f);
                    tvSchoolName.setText(rankInfo.getSchoolName());
                    TextView tvName = holder.getView(R.id.tv_name);
                    tvName.setTextSize(14f);
                    tvName.setText(rankInfo.getChineseName());
                    TextView tvEgName = holder.getView(R.id.tv_eg_name);
                    tvEgName.setTextSize(14f);
                    tvEgName.setText(rankInfo.getEnglishName());
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvRanks.setAdapter(loadMoreWrapper);
    }

    @Override
    public void setPresenter(MarjorProgramRankContract.Presenter presenter) {
        if (presenter != null) {
            this.rankP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (rankP != null) {
            lmrvRanks.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void getRankSuccess(List<HomeHotProgramInfo> data, int request_state) {
        if (rankP != null) {
            rankP.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvRanks.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                schoolsRankList.clear();
                schoolsRankList.addAll(data);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    lmrvRanks.loadComplete(false);
                } else {
                    lmrvRanks.loadComplete(true);
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
        lmrvRanks.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    public void reload() {
        rankP.showLoading(this, emptyView);
        rankP.getRank(ParameterUtils.NETWORK_ELSE_CACHED, categoryId, mPage, ParameterUtils.PULL_DOWN);
    }
}

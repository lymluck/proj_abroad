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
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.HidingScrollListener;
import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HotRankInfo;
import com.smartstudy.xxd.mvp.contract.HotRankContract;
import com.smartstudy.xxd.mvp.presenter.HotRankPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

@Route("HotRankActivity")
public class HotRankActivity extends BaseActivity implements HotRankContract.View {

    private RecyclerView lmrvRanks;
    private CommonAdapter<HotRankInfo> mAdapter;
    private EmptyWrapper<HotRankInfo> emptyWrapper;
    private View searchView;
    private View emptyView;
    private View topLine;
    private TextView tvTitle;

    private HotRankContract.Presenter rankP;
    private ArrayList<HotRankInfo> schoolsRankList;
    private String flag;
    private WeakHandler mHandler;
    private boolean isFirstLoad;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_rank);
    }

    @Override
    protected void onDestroy() {
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
        isFirstLoad = false;
        removeActivity(HotRankActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        isFirstLoad = true;
        topLine = findViewById(R.id.top_line);
        topLine.setVisibility(View.VISIBLE);
        tvTitle = findViewById(R.id.topdefault_centertitle);
        Intent data = getIntent();
        mTitle = data.getStringExtra(TITLE);
        flag = data.getStringExtra("flag");
        tvTitle.setText(mTitle);

        searchView = findViewById(R.id.searchView);
        lmrvRanks = findViewById(R.id.rclv_ranks);
        lmrvRanks.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvRanks.setLayoutManager(mLayoutManager);
        lmrvRanks.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
        new HotRankPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, lmrvRanks, false);
        rankP.showLoading(this, emptyView);
        rankP.getRank(flag);
    }

    @Override
    public void initEvent() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        lmrvRanks.scrollBy(0, searchView.getHeight());
                        // 将searchView隐藏掉
                        searchView.animate()
                            .translationY(-searchView.getHeight())
                            .setDuration(600)
                            .setInterpolator(new AccelerateInterpolator(2))
                            .start();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        searchView.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                HotRankInfo info = schoolsRankList.get(position);
                String title = info.getTitle();
                if (!"art".equals(flag)) {
                    Intent toRank = new Intent(HotRankActivity.this, RankActivity.class);
                    title = title.substring(0, info.getTitle().length() - 2);
                    toRank.putExtra(TITLE, title);
                    toRank.putExtra("id", info.getId() + "");
                    startActivity(toRank);
                } else {
                    Intent toRank = new Intent(HotRankActivity.this, ArtRankActivity.class);
                    toRank.putExtra(TITLE, title);
                    toRank.putExtra("id", info.getId() + "");
                    startActivity(toRank);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        lmrvRanks.addOnScrollListener(new HidingScrollListener() {
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
                searchView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2))
                    .setDuration(800).start();
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
                Intent toSearch = new Intent(HotRankActivity.this, CommonSearchActivity.class);
                if ("art".equals(flag)) {
                    toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.RANKART_FLAG);
                } else {
                    toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.RANKTYPE_FLAG);
                }
                Pair<View, String> searchTop = Pair.create(searchView, "search_top");
                ActivityOptionsCompat compat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(HotRankActivity.this, searchTop);
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
        mAdapter = new CommonAdapter<HotRankInfo>(this, R.layout.item_hot_rank, schoolsRankList) {
            @Override
            protected void convert(ViewHolder holder, HotRankInfo rankInfo, int position) {
                if (rankInfo != null) {
                    if ("art".equals(flag)) {
                        holder.setText(R.id.tv_name, rankInfo.getTitle());
                    } else {
                        holder.setText(R.id.tv_name, rankInfo.getYear() + rankInfo.getType() + rankInfo.getTitle());
                    }
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        lmrvRanks.setAdapter(emptyWrapper);
    }

    @Override
    public void setPresenter(HotRankContract.Presenter presenter) {
        if (presenter != null) {
            this.rankP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (rankP != null) {
            emptyWrapper.notifyDataSetChanged();
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void showList(List<HotRankInfo> data) {
        if (data != null && schoolsRankList != null) {
            rankP.setEmptyView(emptyView);
            schoolsRankList.clear();
            schoolsRankList.addAll(data);
            emptyWrapper.notifyDataSetChanged();
            if (isFirstLoad) {
                isFirstLoad = false;
                lmrvRanks.scrollBy(0, searchView.getHeight());
            }
            //判断是否可滑动， -1 表示 向上， 1 表示向下
            if (!lmrvRanks.canScrollVertically(-1)) {
                searchView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void reload() {
        rankP.showLoading(this, emptyView);
        rankP.getRank(flag);
    }
}

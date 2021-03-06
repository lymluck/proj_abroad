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
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.HidingScrollListener;
import com.smartstudy.commonlib.entity.SchooolRankInfo;
import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
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
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CountryTypeInfo;
import com.smartstudy.xxd.entity.PersonalInfo;
import com.smartstudy.xxd.mvp.contract.RankContract;
import com.smartstudy.xxd.mvp.presenter.RankPresenter;
import com.smartstudy.xxd.ui.popupwindow.PopWindowSchoolCountry;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

@Route("RankActivity")
public class RankActivity extends BaseActivity implements RankContract.View {

    private LoadMoreRecyclerView lmrvRanks;
    private CommonAdapter<SchooolRankInfo> mAdapter;
    private LoadMoreWrapper<SchooolRankInfo> loadMoreWrapper;
    private EmptyWrapper<SchooolRankInfo> emptyWrapper;
    private TextView topRightMenu;
    private View searchView;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private View topLine;
    private LinearLayout llTopRight;
    private RelativeLayout rlCenter;
    private TextView tvTitle;
    private PopWindowSchoolCountry school_country;

    private RankContract.Presenter rankP;
    private ArrayList<SchooolRankInfo> schoolsRankList;
    private int mPage = 1;
    private String categoryId;
    private WeakHandler mHandler;
    private boolean canPullUp;
    private List<CountryTypeInfo> countryTypeInfos;
    private String countryId = "";
    private String countryName;
    private boolean isFirstLoad;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        addActivity(this);
        UApp.actionEvent(this, "12_B_rank_school_list");
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
        isFirstLoad = false;
        removeActivity(RankActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        isFirstLoad = true;
        topLine = findViewById(R.id.top_line);
        topLine.setVisibility(View.VISIBLE);
        topRightMenu = findViewById(R.id.topdefault_rightmenu);
        tvTitle = findViewById(R.id.topdefault_centertitle);
        Intent data = getIntent();
        categoryId = data.getStringExtra("id");
        mTitle = data.getStringExtra(TITLE);
        tvTitle.setText(TextUtils.isEmpty(mTitle) ? "院校排名" : mTitle);
        llTopRight = findViewById(R.id.ll_top_right);
        rlCenter = findViewById(R.id.rl_top_center);
        // 测量调整头部title的宽度
        measureTitleWidth();
        if (!TextUtils.isEmpty(categoryId) && data.getExtras() != null) {
            categoryId = data.getExtras().getString("id");
        }
        searchView = findViewById(R.id.searchView);
        lmrvRanks = findViewById(R.id.rclv_ranks);
        lmrvRanks.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvRanks.setLayoutManager(mLayoutManager);
        lmrvRanks.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
        new RankPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, lmrvRanks, false);
        rankP.showLoading(this, emptyView);
        countryName = "全球";
        if (data.hasExtra("countryId")) {
            countryId = data.getStringExtra("countryId");
        }
        initCountryFromCache(data);
        if (data.getBooleanExtra("showBtn", false)) {
            showRightBtn();
        }
        topRightMenu.setText(countryName);
        rankP.getRank(ParameterUtils.NETWORK_ELSE_CACHED, countryId, categoryId, mPage, ParameterUtils.PULL_DOWN);
        countryTypeInfos = JSON.parseArray(Utils.getJson("json/home_school_title.json"), CountryTypeInfo.class);
    }

    private void initCountryFromCache(Intent data) {
        String targetCountryInfo = (String) SPCacheUtils.get("target_countryInfo", "");
        if (!TextUtils.isEmpty(targetCountryInfo)) {
            PersonalInfo.TargetSectionEntity.TargetCountryEntity targetCountryEntity
                = JSON.parseObject(targetCountryInfo, PersonalInfo.TargetSectionEntity.TargetCountryEntity.class);
            if (targetCountryEntity != null) {
                if (!"OTHER".equals(targetCountryEntity.getId())) {
                    countryId = targetCountryEntity.getId();
                    countryName = targetCountryEntity.getName();
                }
            }
        }
    }

    private void measureTitleWidth() {
        llTopRight.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    llTopRight.getViewTreeObserver().removeOnPreDrawListener(this);
                    int width = llTopRight.getMeasuredWidth();
                    RelativeLayout.LayoutParams center = (RelativeLayout.LayoutParams) rlCenter.getLayoutParams();
                    center.leftMargin = width;
                    center.rightMargin = width;
                    rlCenter.setLayoutParams(center);
                    hasMeasured = true;
                }
                return true;
            }
        });
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
        lmrvRanks.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (canPullUp) {
                    mPage = mPage + 1;
                    rankP.getRank(ParameterUtils.CACHED_ELSE_NETWORK, countryId, categoryId, mPage, ParameterUtils.PULL_UP);
                    canPullUp = false;
                }
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                UApp.actionEvent(RankActivity.this, "12_A_school_detail_cell");
                SchooolRankInfo info = schoolsRankList.get(position);
                if (info.getSchool_id() != null && !TextUtils.isEmpty(info.getSchool_id())) {
                    Bundle params = new Bundle();
                    params.putString("id", info.getSchool_id());
                    Intent toMoreDetails = new Intent(RankActivity.this, CollegeDetailActivity.class);
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
            case R.id.topdefault_rightmenu:
                if (school_country == null) {
                    View contentView = mInflater.inflate(R.layout.popupwindow_choose_data,
                        null);
                    school_country = new PopWindowSchoolCountry(contentView,
                        countryTypeInfos, topRightMenu.getText().toString(), "rank") {
                        @Override
                        public void dismiss() {
                            super.dismiss();
                            topRightMenu.setTextColor(getResources().getColor(R.color.app_main_color));
                            topRightMenu.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                R.drawable.ic_blue_arrow_down, 0);
                            if (this.getId() != null) {
                                String str_name = topRightMenu.getText().toString();
                                if (!str_name.equals(this.getName())) {
                                    topRightMenu.setText(this.getName());
                                    // 测量头部标题宽度
                                    measureTitleWidth();
                                    mPage = 1;
                                    schoolsRankList.clear();
                                    loadMoreWrapper.notifyDataSetChanged();
                                    rankP.showLoading(RankActivity.this, emptyView);
                                    countryId = this.getId();
                                    rankP.getRank(ParameterUtils.NETWORK_ELSE_CACHED, countryId, categoryId,
                                        mPage, ParameterUtils.PULL_DOWN);
                                }
                            }
                        }
                    };
                }
                school_country.showPopupWindow(topLine, topRightMenu);
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
        lmrvRanks.setAdapter(loadMoreWrapper);
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
            lmrvRanks.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
            ToastUtils.showToast(message);
        }
    }

    public void showRightBtn() {
        topRightMenu.setTextColor(getResources().getColor(R.color.app_main_color));
        topRightMenu.setCompoundDrawablePadding(DensityUtils.dip2px(4f));
        topRightMenu.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_blue_arrow_down,
            0);
        topRightMenu.setVisibility(View.VISIBLE);
        topRightMenu.setOnClickListener(this);
    }

    @Override
    public void getRankSuccess(List<SchooolRankInfo> data, boolean showBtn, String title, int request_state) {
        if (rankP != null) {
            // 右上角按钮逻辑
            if (showBtn) {
                if (TextUtils.isEmpty(mTitle)) {
                    tvTitle.setText(title);
                }
                showRightBtn();
            }
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
                if (isFirstLoad) {
                    isFirstLoad = false;
                    lmrvRanks.scrollBy(0, searchView.getHeight());
                }
                //判断是否可滑动， -1 表示 向上， 1 表示向下
                if (!lmrvRanks.canScrollVertically(-1)) {
                    searchView.setVisibility(View.VISIBLE);
                }
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
        rankP.getRank(ParameterUtils.NETWORK_ELSE_CACHED, countryId, categoryId, mPage, ParameterUtils.PULL_DOWN);
    }
}

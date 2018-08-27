package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.entity.SchooolRankInfo;
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
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.ArtRankContract;
import com.smartstudy.xxd.mvp.presenter.ArtRankPresenter;
import com.smartstudy.xxd.ui.popupwindow.PopWindowArtCountry;
import com.smartstudy.xxd.ui.popupwindow.PopWindowArtDegree;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

@Route("ArtRankActivity")
public class ArtRankActivity extends BaseActivity implements ArtRankContract.View {

    private LoadMoreRecyclerView lmrvRanks;
    private CommonAdapter<SchooolRankInfo> mAdapter;
    private LoadMoreWrapper<SchooolRankInfo> loadMoreWrapper;
    private EmptyWrapper<SchooolRankInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private TextView tvCountry;
    private TextView tvDegree;

    private ArtRankContract.Presenter rankP;
    private ArrayList<SchooolRankInfo> schoolsRankList;
    private int mPage = 1;
    private String majorId;
    private boolean canPullUp;
    private PopWindowArtCountry artCountry;
    private PopWindowArtDegree artDegree;
    private List<IdNameInfo> artCountries;
    private List<IdNameInfo> artDegrees;
    private String countryId;
    private String degreeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_rank);
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
        removeActivity(ArtRankActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        tvCountry = findViewById(R.id.tv_country);
        tvDegree = findViewById(R.id.tv_degree);
        artCountries = new ArrayList<>();
        artDegrees = new ArrayList<>();
        Intent data = getIntent();
        majorId = data.getStringExtra("id");
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(data.getStringExtra(TITLE));
        lmrvRanks = findViewById(R.id.rclv_ranks);
        lmrvRanks.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvRanks.setLayoutManager(mLayoutManager);
        lmrvRanks.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
        new ArtRankPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, lmrvRanks, false);
        rankP.showLoading(this, emptyView);
        rankP.getArtRank(majorId, countryId, degreeId, mPage, ParameterUtils.PULL_DOWN);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.llyt_country).setOnClickListener(this);
        findViewById(R.id.llyt_degree).setOnClickListener(this);
        lmrvRanks.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (canPullUp) {
                    mPage = mPage + 1;
                    rankP.getArtRank(majorId, countryId, degreeId, mPage, ParameterUtils.PULL_UP);
                    canPullUp = false;
                }
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                UApp.actionEvent(ArtRankActivity.this, "12_A_school_detail_cell");
                SchooolRankInfo info = schoolsRankList.get(position);
                if (info != null && !TextUtils.isEmpty(info.getWebsite())) {
                    Intent toMoreDetails = new Intent(ArtRankActivity.this, ShowWebViewActivity.class);
                    toMoreDetails.putExtra(WEBVIEW_URL, info.getWebsite());
                    toMoreDetails.putExtra(TITLE, "专业信息");
                    toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
                    startActivity(toMoreDetails);
                } else {
                    ToastUtils.showToast("暂无该专业信息！");
                }
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
            case R.id.llyt_country:
                if (artCountry == null) {
                    View contentView = mInflater.inflate(R.layout.popupwindow_choose_data,
                        null);
                    artCountry = new PopWindowArtCountry(contentView,
                        artCountries, tvCountry.getText().toString()) {
                        @Override
                        public void dismiss() {
                            super.dismiss();
                            tvCountry.setTextColor(getResources().getColor(R.color.app_text_color2));
                            tvCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                R.drawable.ic_arrow_down_gray, 0);
                            if (this.getId() != null) {
                                String str_name = tvCountry.getText().toString();
                                if (!str_name.equals(this.getName())) {
                                    tvCountry.setText(this.getName());
                                    mPage = 1;
                                    schoolsRankList.clear();
                                    loadMoreWrapper.notifyDataSetChanged();
                                    rankP.showLoading(ArtRankActivity.this, emptyView);
                                    countryId = this.getId();
                                    rankP.getArtRank(majorId, countryId, degreeId, mPage,
                                        ParameterUtils.PULL_DOWN);
                                }
                            }
                        }
                    };
                }
                artCountry.showPopupWindow(findViewById(R.id.line_condition), tvCountry);
                break;
            case R.id.llyt_degree:
                if (artDegree == null) {
                    View contentView = mInflater.inflate(R.layout.popupwindow_choose_data,
                        null);
                    artDegree = new PopWindowArtDegree(contentView, artDegrees,
                        tvDegree.getText().toString()) {
                        @Override
                        public void dismiss() {
                            super.dismiss();
                            tvDegree.setTextColor(getResources().getColor(R.color.app_text_color2));
                            tvDegree.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                                R.drawable.ic_arrow_down_gray, 0);
                            if (this.getId() != null) {
                                String str_name = tvDegree.getText().toString();
                                if (!str_name.equals(this.getName())) {
                                    tvDegree.setText(this.getName());
                                    mPage = 1;
                                    schoolsRankList.clear();
                                    loadMoreWrapper.notifyDataSetChanged();
                                    rankP.showLoading(ArtRankActivity.this, emptyView);
                                    degreeId = this.getId();
                                    rankP.getArtRank(majorId, countryId, degreeId, mPage,
                                        ParameterUtils.PULL_DOWN);
                                }
                            }
                        }
                    };
                }
                artDegree.showPopupWindow(findViewById(R.id.line_condition), tvDegree);
                break;
            default:
                break;
        }
    }

    private void initAdapter() {
        schoolsRankList = new ArrayList<>();
        mAdapter = new CommonAdapter<SchooolRankInfo>(this, R.layout.item_art_rank_list, schoolsRankList) {
            @Override
            protected void convert(ViewHolder holder, SchooolRankInfo rankInfo, int position) {
                if (rankInfo != null) {
                    holder.setText(R.id.tv_school_rank, rankInfo.getRank());
                    holder.setText(R.id.tv_major_name, rankInfo.getEnglishName());
                    holder.setText(R.id.tv_school, rankInfo.getSchoolChineseName());
                    holder.setText(R.id.tv_school_eg_name, rankInfo.getSchoolEnglishName());
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvRanks.setAdapter(loadMoreWrapper);
    }

    @Override
    public void setPresenter(ArtRankContract.Presenter presenter) {
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
    public void getRankSuccess(List<SchooolRankInfo> data, boolean showBtn, String title, int request_state) {
        findViewById(R.id.ll_condition).setVisibility(View.VISIBLE);
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
    public void setCountries(List<IdNameInfo> datas) {
        artCountries.clear();
        artCountries.addAll(datas);
    }

    @Override
    public void setDegrees(List<IdNameInfo> datas) {
        artDegrees.clear();
        artDegrees.addAll(datas);
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
        rankP.getArtRank(majorId, countryId, degreeId, mPage, ParameterUtils.PULL_DOWN);
    }
}

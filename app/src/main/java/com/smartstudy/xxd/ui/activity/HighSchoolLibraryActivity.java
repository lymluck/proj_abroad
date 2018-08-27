package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.commonlib.entity.HighSchoolInfo;
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
import com.smartstudy.xxd.entity.HighOptionsInfo;
import com.smartstudy.xxd.mvp.contract.HighSchoolLibraryContract;
import com.smartstudy.xxd.mvp.presenter.HighSchoolLibraryPresenter;
import com.smartstudy.xxd.ui.popupwindow.PopWindowHighSchoolFee;
import com.smartstudy.xxd.ui.popupwindow.PopWindowHighSchoolMore;
import com.smartstudy.xxd.ui.popupwindow.PopWindowHighSchoolRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.NAME;
import static com.smartstudy.xxd.utils.AppContants.PAGES_COUNT;
import static com.smartstudy.xxd.utils.AppContants.RANK_CATEGORY_ID;
import static com.smartstudy.xxd.utils.AppContants.TEL;
import static com.smartstudy.xxd.utils.AppContants.USER_ACCOUNT;
import static com.smartstudy.xxd.utils.AppContants.USER_NAME;


/**
 * @author yqy
 * @date on 2018/4/3
 * @describe 美国高中院校库
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HighSchoolLibraryActivity extends BaseActivity implements HighSchoolLibraryContract.View {

    private TextView tvRank;
    private TextView totalCount;
    private HighSchoolLibraryContract.Presenter presenter;
    private String countryId = "";
    private String schoolRankValue = "";
    private HighOptionsInfo highOptionsInfo;
    private LoadMoreRecyclerView rclvSchools;
    private CommonAdapter<HighSchoolInfo> mAdapter;
    private LoadMoreWrapper<HighSchoolInfo> loadMoreWrapper;
    private EmptyWrapper<HighSchoolInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private ArrayList<HighSchoolInfo> schoolsInfoList;
    private int mPage = PAGES_COUNT;
    private String sexualTypeId = "";
    private String boarderTypeId = "";
    private String locationTypeId = "";
    private String rankCategoryId = RANK_CATEGORY_ID;
    private String totalFee = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(HighSchoolLibraryActivity.class.getSimpleName());
        if (presenter != null) {
            presenter = null;
        }
        if (rclvSchools != null) {
            rclvSchools.removeAllViews();
            rclvSchools = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (schoolsInfoList != null) {
            schoolsInfoList.clear();
            schoolsInfoList = null;
        }
        if (mLayoutManager != null) {
            mLayoutManager = null;
        }
        if (loadMoreWrapper != null) {
            loadMoreWrapper = null;
        }
        if (mLayoutManager != null) {
            mLayoutManager = null;
        }
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("美国高中院校库");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        new HighSchoolLibraryPresenter(this);
        tvRank = (TextView) findViewById(R.id.tv_country);
        tvRank.setText("排名");
        totalCount = findViewById(R.id.tv_school_rank);
        totalCount.setText("费用");
        rclvSchools = (LoadMoreRecyclerView) findViewById(R.id.rclv_schools);
        rclvSchools.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvSchools.setLayoutManager(mLayoutManager);
        rclvSchools.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f))
            .margin(DensityUtils.dip2px(16), 0)
            .colorResId(R.color.horizontal_line_color).build());
        presenter.getHighOptions();
        initAdapter();
        emptyView = mInflater.inflate(R.layout.layout_empty, rclvSchools, false);
        presenter.showLoading(this, emptyView);
        presenter.getHighList(countryId, sexualTypeId, boarderTypeId, locationTypeId, totalFee,
            rankCategoryId, schoolRankValue, mPage + "", ParameterUtils.PULL_DOWN);
    }

    private void initAdapter() {
        schoolsInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<HighSchoolInfo>(this, R.layout.item_high_school_list, schoolsInfoList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, HighSchoolInfo schoolInfo, int position) {
                if (schoolInfo != null) {
                    holder.setText(R.id.tv_number, schoolInfo.getRank());
                    holder.setText(R.id.tv_chineseName, schoolInfo.getChineseName());
                    holder.setText(R.id.tv_englishName, schoolInfo.getEnglishName());
                    holder.setText(R.id.tv_provinceName, schoolInfo.getProvinceName());
                    holder.setText(R.id.tv_seniorFacultyRatio, "高级教师占比：" + schoolInfo.getSeniorFacultyRatio());
                    holder.getView(R.id.tv_question).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            HashMap<String, String> clientInfo = new HashMap<>();
                            clientInfo.put(NAME, (String) SPCacheUtils.get(USER_NAME, ""));
                            clientInfo.put(TEL, (String) SPCacheUtils.get(USER_ACCOUNT, ""));
                            Intent intent = new MQIntentBuilder(HighSchoolLibraryActivity.this)
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
                intent.putExtra("id", schoolsInfoList.get(position).getId());
                intent.setClass(HighSchoolLibraryActivity.this, HighSchoolDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvSchools.setAdapter(loadMoreWrapper);
    }


    @Override
    public void initEvent() {
        findViewById(R.id.llyt_country).setOnClickListener(this);
        findViewById(R.id.llyt_school_rank).setOnClickListener(this);
        findViewById(R.id.llyt_more_check).setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        rclvSchools.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                mPage = mPage + 1;
                presenter.getHighList(countryId, sexualTypeId, boarderTypeId, locationTypeId, totalFee,
                    rankCategoryId, schoolRankValue, mPage + "", ParameterUtils.PULL_UP);
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
                if (highOptionsInfo == null) {
                    return;
                }
                if (highOptionsInfo.getRanks() != null && highOptionsInfo.getRanks().getOptions() != null) {
                    if (highOptionsInfo.getRanks().getOptions().size() > 0) {
                        PopWindowHighSchoolRank pwgsRank = new PopWindowHighSchoolRank(
                            this, highOptionsInfo, rankCategoryId, schoolRankValue) {
                            @Override
                            public void dismiss() {
                                super.dismiss();
                                if (this.getRankCategoryValue() != null) {
                                    String rank = tvRank.getText().toString();
                                    if (!rank.equals(this.getRankCategoryValue())) {
                                        tvRank.setText(this.getRankCategoryValue());
                                    }
                                }
                                rankCategoryId = this.getRankCategoryId();
                                schoolRankValue = this.getSchoolRankValue();
                                mPage = PAGES_COUNT;
                                schoolsInfoList.clear();
                                loadMoreWrapper.notifyDataSetChanged();
                                presenter.showLoading(HighSchoolLibraryActivity.this, emptyView);
                                presenter.getHighList(countryId, sexualTypeId, boarderTypeId,
                                    locationTypeId, totalFee, rankCategoryId, schoolRankValue,
                                    mPage + "", ParameterUtils.PULL_DOWN);
                            }

                        };
                        pwgsRank.showPopupWindow(findViewById(R.id.line_tab), tvRank);
                    }
                }
                break;
            case R.id.llyt_school_rank:
                if (highOptionsInfo == null) {
                    return;
                }
                if (highOptionsInfo.getFeeRange() != null && highOptionsInfo.getFeeRange().getOptions() != null) {
                    if (highOptionsInfo.getFeeRange().getOptions().size() > 0) {
                        PopWindowHighSchoolFee pwhsRank = new PopWindowHighSchoolFee(
                            this, highOptionsInfo.getFeeRange().getOptions(), totalCount.getText().toString()) {
                            @Override
                            public void dismiss() {
                                super.dismiss();
                                if (this.getName() != null) {
                                    String str_name = totalCount.getText().toString();
                                    if (!str_name.equals(this.getName())) {
                                        totalCount.setText(this.getName());
                                        mPage = 1;
                                        schoolsInfoList.clear();
                                        loadMoreWrapper.notifyDataSetChanged();
                                        presenter.showLoading(HighSchoolLibraryActivity.this, emptyView);
                                        totalFee = this.getId();
                                        presenter.getHighList(countryId, sexualTypeId, boarderTypeId,
                                            locationTypeId, totalFee, rankCategoryId, schoolRankValue,
                                            mPage + "", ParameterUtils.PULL_DOWN);

                                    }
                                }
                            }

                        };
                        pwhsRank.showPopupWindow(findViewById(R.id.line_tab), totalCount);
                    }
                }
                break;

            case R.id.llyt_more_check:
                if (highOptionsInfo == null) {
                    return;
                }
                final PopWindowHighSchoolMore pwhsMore = new PopWindowHighSchoolMore(this, highOptionsInfo,
                    sexualTypeId, boarderTypeId) {
                    @Override
                    public void dismiss() {
                        super.dismiss();
                        if (this.isDone()) {
                            sexualTypeId = this.getSchoolType();
                            boarderTypeId = this.getStayType();
                            mPage = 1;
                            schoolsInfoList.clear();
                            loadMoreWrapper.notifyDataSetChanged();
                            presenter.showLoading(HighSchoolLibraryActivity.this, emptyView);
                            presenter.getHighList(countryId, sexualTypeId, boarderTypeId, locationTypeId,
                                totalFee, rankCategoryId, schoolRankValue, mPage + "",
                                ParameterUtils.PULL_DOWN);
                        }
                    }
                };
                pwhsMore.showPopupWindow(findViewById(R.id.line_tab), (TextView) findViewById(R.id.tv_more_type));
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(HighSchoolLibraryContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            rclvSchools.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void getOptionsSuccess(HighOptionsInfo highOptionsInfo) {
        this.highOptionsInfo = highOptionsInfo;
    }

    @Override
    public void getHighSchoolListSuccess(List<HighSchoolInfo> highSchools, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = highSchools.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvSchools.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                schoolsInfoList.clear();
                schoolsInfoList.addAll(highSchools);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rclvSchools.loadComplete(false);
                } else {
                    rclvSchools.loadComplete(true);
                    schoolsInfoList.addAll(highSchools);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        highSchools = null;
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclvSchools.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public void reload() {
        presenter.showLoading(this, emptyView);
        presenter.getHighList(countryId, sexualTypeId, boarderTypeId, locationTypeId, totalFee,
            rankCategoryId, schoolRankValue, mPage + "", ParameterUtils.PULL_DOWN);
    }
}

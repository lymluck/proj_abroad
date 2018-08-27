package com.smartstudy.xxd.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.entity.SchoolInfo;
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
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CountryTypeInfo;
import com.smartstudy.xxd.mvp.contract.SchoolListContract;
import com.smartstudy.xxd.mvp.presenter.SchoolListPresenter;
import com.smartstudy.xxd.ui.popupwindow.PopWindow_School_Country;
import com.smartstudy.xxd.ui.popupwindow.PopWindow_School_MoreType;
import com.smartstudy.xxd.ui.popupwindow.PopWindow_School_Rank;

import java.util.ArrayList;
import java.util.List;

@Route("SchoolListActivity")
public class SchoolListActivity extends UIActivity implements SchoolListContract.View {

    private LoadMoreRecyclerView rclv_schools;
    private CommonAdapter<SchoolInfo> mAdapter;
    private LoadMoreWrapper<SchoolInfo> loadMoreWrapper;
    private EmptyWrapper<SchoolInfo> emptyWrapper;
    private TextView tv_country;
    private TextView tv_school_rank;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;

    private SchoolListContract.Presenter schoolP;
    private ArrayList<SchoolInfo> schoolsInfoList;
    private int mPage = 1;
    private String countryId = "";
    private String rankValue;
    private String countryListStr;
    private String checkTypeStr;
    private String egTypeValue;
    private String totalFee;
    private List<CountryTypeInfo> countryTypeInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_list);
        StatisticUtils.actionEvent(this, "9_B_school_list");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(SchoolListActivity.class.getSimpleName());
        clearObj();
    }

    private void clearObj() {
        if (schoolP != null) {
            schoolP = null;
        }
        if (schoolsInfoList != null) {
            schoolsInfoList.clear();
            schoolsInfoList = null;
        }
        if (countryTypeInfos != null) {
            countryTypeInfos.clear();
            countryTypeInfos = null;
        }
        rclv_schools.removeAllViews();
        emptyView = null;
        rclv_schools = null;
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        loadMoreWrapper = null;
        emptyWrapper = null;
        mLayoutManager = null;
        countryListStr = null;
        checkTypeStr = null;
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("院校列表");
        Intent data = getIntent();
        if (ParameterUtils.MYSCHOOL_FLAG.equals(getIntent().getStringExtra("flag"))) {
            addActivity(this);
        }
        if (data.hasExtra("countryId")) {
            countryId = data.getStringExtra("countryId");
        }
        rankValue = data.getStringExtra("localRank");
        String scoreToefl = data.getStringExtra("scoreToefl");
        if (!TextUtils.isEmpty(scoreToefl)) {
            egTypeValue = "toefl:" + scoreToefl;
        }
        String scoreIelts = data.getStringExtra("scoreIelts");
        if (!TextUtils.isEmpty(scoreIelts)) {
            egTypeValue = "ielts:" + scoreIelts;
        }
        totalFee = data.getStringExtra("feeTotal");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_country.setText(data.getStringExtra("title"));
        tv_school_rank = (TextView) findViewById(R.id.tv_school_rank);
        rclv_schools = (LoadMoreRecyclerView) findViewById(R.id.rclv_schools);
        rclv_schools.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_schools.setLayoutManager(mLayoutManager);
        rclv_schools.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        initPopData();
        initAdapter();
        new SchoolListPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclv_schools, false);
        schoolP.showLoading(this, emptyView);
        schoolP.getSchools(countryId, rankValue, egTypeValue, totalFee, mPage, ParameterUtils.PULL_DOWN);
    }

    private void initPopData() {
        countryListStr = Utils.getJson("home_school_title.json");
        checkTypeStr = Utils.getJson("school_check_type.json");
        countryTypeInfos = JSON.parseArray(countryListStr, CountryTypeInfo.class);
        for (CountryTypeInfo info : countryTypeInfos) {
            if (info.getCountryId().equals(countryId) || info.getCountryNumId().equals(countryId)) {
                tv_country.setText(info.getCountryName());
                info = null;
                break;
            }
            info = null;
        }
        if (rankValue != null) {
            JSONArray ranks = JSON.parseObject(checkTypeStr).getJSONArray("localRank");
            int rankLen = ranks.size();
            JSONObject obj;
            for (int i = 0; i < rankLen; i++) {
                obj = ranks.getJSONObject(i);
                if (rankValue.equals(obj.getString("value"))) {
                    tv_school_rank.setText(obj.getString("name"));
                }
                obj = null;
            }
            ranks = null;
        }
    }

    private void initAdapter() {
        schoolsInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<SchoolInfo>(this, R.layout.item_school_list, schoolsInfoList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, SchoolInfo schoolInfo, int position) {
                if (schoolInfo != null) {
                    if (holder.getView(R.id.llyt_home_rate).getVisibility() == View.GONE) {
                        holder.getView(R.id.llyt_home_rate).setVisibility(View.VISIBLE);
                    }
                    holder.setCircleImageUrl(R.id.iv_logo, schoolInfo.getLogo(), true);
                    holder.setText(R.id.tv_school_name, schoolInfo.getChineseName());
                    holder.setText(R.id.tv_English_name, schoolInfo.getEnglishName());
                    if (schoolInfo.getCityName() != null && !"".equals(schoolInfo.getCityName())) {
                        holder.setText(R.id.tv_area, schoolInfo.getProvinceName() + "-" + schoolInfo.getCityName());
                    } else {
                        holder.setText(R.id.tv_area, schoolInfo.getProvinceName());
                    }
                    holder.setText(R.id.tv_rate, String.format(getString(R.string.rate),
                            TextUtils.isEmpty(schoolInfo.getTIE_ADMINSSION_RATE()) ? "暂无" : schoolInfo.getTIE_ADMINSSION_RATE()));
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclv_schools.setAdapter(loadMoreWrapper);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.llyt_country).setOnClickListener(this);
        findViewById(R.id.llyt_school_rank).setOnClickListener(this);
        findViewById(R.id.llyt_more_check).setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        rclv_schools.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                mPage = mPage + 1;
                schoolP.getSchools(countryId, rankValue, egTypeValue, totalFee, mPage, ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                StatisticUtils.actionEvent(SchoolListActivity.this, "9_A_school_detail_cell");
                if (position >= 0 && position < schoolsInfoList.size()) {
                    SchoolInfo info = schoolsInfoList.get(position);
                    Bundle params = new Bundle();
                    params.putString("id", info.getId() + "");
                    Intent toMoreDetails = new Intent(SchoolListActivity.this, SchoolDetailActivity.class);
                    toMoreDetails.putExtras(params);
                    startActivity(toMoreDetails);
                    info = null;
                    params = null;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.llyt_country:
                StatisticUtils.actionEvent(this, "9_A_country_change_btn");
                PopWindow_School_Country school_country = new PopWindow_School_Country(this, countryTypeInfos, tv_country.getText().toString(), "school") {
                    @Override
                    public void dismiss() {
                        super.dismiss();
                        if (this.getId() != null) {
                            String str_name = tv_country.getText().toString();
                            if (!str_name.equals(this.getName())) {
                                tv_country.setText(this.getName());
                                mPage = 1;
                                schoolsInfoList.clear();
                                loadMoreWrapper.notifyDataSetChanged();
                                schoolP.showLoading(SchoolListActivity.this, emptyView);
                                countryId = this.getId();
                                schoolP.getSchools(countryId, rankValue, egTypeValue, totalFee, mPage, ParameterUtils.PULL_DOWN);
                            }
                        }
                    }
                };
                school_country.showPopupWindow(findViewById(R.id.line_tab), tv_country);
                break;
            case R.id.llyt_school_rank:
                StatisticUtils.actionEvent(this, "9_A_rank_change_btn");
                List<IdNameInfo> rankTypeInfos = JSON.parseArray(JSON.parseObject(checkTypeStr).getString("localRank"), IdNameInfo.class);
                PopWindow_School_Rank school_rank = new PopWindow_School_Rank(this, rankTypeInfos, tv_school_rank.getText().toString()) {
                    @Override
                    public void dismiss() {
                        super.dismiss();
                        if (this.getValue() != null) {
                            String str_name = tv_school_rank.getText().toString();
                            if (!str_name.equals(this.getName())) {
                                tv_school_rank.setText(this.getName());
                                mPage = 1;
                                schoolsInfoList.clear();
                                loadMoreWrapper.notifyDataSetChanged();
                                schoolP.showLoading(SchoolListActivity.this, emptyView);
                                rankValue = this.getValue();
                                schoolP.getSchools(countryId, rankValue, egTypeValue, totalFee, mPage, ParameterUtils.PULL_DOWN);
                            }
                        }
                    }
                };
                school_rank.showPopupWindow(findViewById(R.id.line_tab), tv_school_rank);
                break;
            case R.id.llyt_more_check:
                StatisticUtils.actionEvent(this, "9_A_more_change_btn");
                PopWindow_School_MoreType school_type = new PopWindow_School_MoreType(this, checkTypeStr,
                        egTypeValue, totalFee) {
                    @Override
                    public void dismiss() {
                        super.dismiss();
                        if (this.isDone()) {
                            mPage = 1;
                            schoolsInfoList.clear();
                            loadMoreWrapper.notifyDataSetChanged();
                            schoolP.showLoading(SchoolListActivity.this, emptyView);
                            totalFee = this.getFee();
                            egTypeValue = this.getEgType() + ":" + this.getEgValue();
                            schoolP.getSchools(countryId, rankValue, egTypeValue, totalFee, mPage, ParameterUtils.PULL_DOWN);
                        }
                    }
                };
                school_type.showPopupWindow(findViewById(R.id.line_tab), (TextView) findViewById(R.id.tv_more_type));
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(SchoolListContract.Presenter presenter) {
        if (presenter != null) {
            this.schoolP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (schoolP != null) {
            rclv_schools.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
            ToastUtils.showToast(this, message);
        }
    }

    @Override
    public void getSchoolsSuccess(List<SchoolInfo> data, int request_state) {
        if (schoolP != null) {
            schoolP.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclv_schools.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                schoolsInfoList.clear();
                schoolsInfoList.addAll(data);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rclv_schools.loadComplete(false);
                } else {
                    rclv_schools.loadComplete(true);
                    schoolsInfoList.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        data = null;
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclv_schools.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public void reload() {
        schoolP.showLoading(this, emptyView);
        schoolP.getSchools(countryId, rankValue, egTypeValue, totalFee, mPage, ParameterUtils.PULL_DOWN);
    }
}

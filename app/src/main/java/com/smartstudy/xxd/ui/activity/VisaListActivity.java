package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.customView.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CountryTypeInfo;
import com.smartstudy.xxd.entity.VisaInfo;
import com.smartstudy.xxd.mvp.contract.VisaListContract;
import com.smartstudy.xxd.mvp.presenter.VisaListPresenter;
import com.smartstudy.xxd.ui.popupwindow.PopWindow_School_Country;

import java.util.ArrayList;
import java.util.List;

public class VisaListActivity extends UIActivity implements VisaListContract.View {

    private RecyclerView rclvVisa;
    private SwipeRefreshLayout srltVisa;
    private CommonAdapter<VisaInfo> mAdapter;
    private EmptyWrapper<VisaInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private TextView topdefault_centertitle;

    private VisaListContract.Presenter presenter;
    private List<VisaInfo> visaInfoList;
    private String targetCountryId;
    private List<CountryTypeInfo> countryTypeInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_list);
    }

    @Override
    protected void onDestroy() {
        if (rclvVisa != null) {
            rclvVisa.removeAllViews();
            rclvVisa = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (countryTypeInfos != null) {
            countryTypeInfos.clear();
            countryTypeInfos = null;
        }
        if (visaInfoList != null) {
            visaInfoList.clear();
            visaInfoList = null;
        }
        if (presenter != null) {
            presenter = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initViewAndData() {
        Intent data = getIntent();
        countryTypeInfos = JSON.parseArray(Utils.getJson("home_school_title.json"), CountryTypeInfo.class);
        targetCountryId = data.getStringExtra("countryId");
        String title = data.getStringExtra("title");
        if ("OTHER".equals(targetCountryId) || TextUtils.isEmpty(targetCountryId)) {
            targetCountryId = countryTypeInfos.get(1).getCountryId();
            title = countryTypeInfos.get(1).getCountryName() + "签证";
        }
        topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        topdefault_centertitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down_gray, 0);
        topdefault_centertitle.setText(title);
        this.srltVisa = (SwipeRefreshLayout) findViewById(R.id.srlt_visa);
        srltVisa.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        srltVisa.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getVisas(targetCountryId);

            }
        });
        this.rclvVisa = (RecyclerView) findViewById(R.id.rclv_visa);
        rclvVisa.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvVisa.setLayoutManager(mLayoutManager);
        initAdapter();
        new VisaListPresenter(this);
        //加载动画
        emptyView = mInflater.inflate(R.layout.layout_empty, rclvVisa, false);
        presenter.showLoading(this, emptyView);
        presenter.getVisas(targetCountryId);
    }

    private void initAdapter() {
        visaInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<VisaInfo>(this, R.layout.item_visa_list, visaInfoList) {
            @Override
            protected void convert(ViewHolder holder, VisaInfo visaInfo, int position) {
                if (position == 0) {
                    holder.getView(R.id.first_view_visa).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.first_view_visa).setVisibility(View.GONE);
                }
                if (position == visaInfoList.size() - 1) {
                    holder.getView(R.id.last_view_visa).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.last_view_visa).setVisibility(View.GONE);
                }
                if ("course".equals(visaInfo.getType())) {
                    holder.getView(R.id.iv_course).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.iv_course).setVisibility(View.GONE);
                }
                DisplayImageUtils.formatImgUrlNoHolder(getApplicationContext(), visaInfo.getBackgroundImageUrl(), (ImageView) holder.getView(R.id.iv_visa_cover));
                holder.setText(R.id.tv_visa_name, visaInfo.getTitle());
                holder.setText(R.id.tv_see_num, String.format(getString(R.string.visa_see),
                        TextUtils.isEmpty(visaInfo.getVisitCount()) ? "0" : visaInfo.getVisitCount()));
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        rclvVisa.setAdapter(emptyWrapper);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        topdefault_centertitle.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                VisaInfo info = visaInfoList.get(position);
                if ("course".equals(info.getType())) {
                    startActivity(new Intent(VisaListActivity.this, CourseDetailActivity.class)
                            .putExtra("id", info.getProductId())
                            .putExtra("courseCover", info.getCoverUrl()));
                } else if ("flow".equals(info.getType()) || "material".equals(info.getType())) {
                    Intent toMoreDetails = new Intent(VisaListActivity.this, ShowWebViewActivity.class);
                    toMoreDetails.putExtra("web_url", info.getWebviewUrl());
                    toMoreDetails.putExtra("use_title", true);
                    toMoreDetails.putExtra("title", info.getTitle());
                    toMoreDetails.putExtra("url_action", "get");
                    startActivity(toMoreDetails);
                } else if ("faq".equals(info.getType())) {
                    startActivity(new Intent(VisaListActivity.this, VisaQaActivity.class)
                            .putExtra("countryId", targetCountryId)
                            .putExtra("title", info.getTitle()));
                } else if ("centers".equals(info.getType())) {
                    startActivity(new Intent(VisaListActivity.this, VisaAddrActivity.class)
                            .putExtra("countryId", targetCountryId)
                            .putExtra("title", info.getTitle()));
                } else if ("news".equals(info.getType())) {
                    startActivity(new Intent(VisaListActivity.this, NewsActivity.class)
                            .putExtra("countryId", targetCountryId)
                            .putExtra("title", info.getTitle()));
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
            case R.id.topdefault_centertitle:
                PopWindow_School_Country school_country = new PopWindow_School_Country(this, countryTypeInfos.subList(1, countryTypeInfos.size()), topdefault_centertitle.getText().toString(), "visa") {
                    @Override
                    public void dismiss() {
                        super.dismiss();
                        findViewById(R.id.top_line).setVisibility(View.GONE);
                        if (this.getId() != null) {
                            String str_name = topdefault_centertitle.getText().toString();
                            if (!str_name.equals(this.getName())) {
                                topdefault_centertitle.setText(this.getName());
                                visaInfoList.clear();
                                emptyWrapper.notifyDataSetChanged();
                                presenter.showLoading(VisaListActivity.this, emptyView);
                                targetCountryId = this.getId();
                                presenter.getVisas(targetCountryId);
                            }
                        }
                    }
                };
                findViewById(R.id.top_line).setVisibility(View.VISIBLE);
                school_country.showPopupWindow(findViewById(R.id.top_visa), topdefault_centertitle);
                break;
        }
    }

    @Override
    public void setPresenter(VisaListContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            srltVisa.setRefreshing(false);
            ToastUtils.showToast(this, message);
        }
    }

    @Override
    public void showVisas(List<VisaInfo> data) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            //下拉刷新
            if (len <= 0) {
                mLayoutManager.setScrollEnabled(false);
            }
            visaInfoList.clear();
            visaInfoList.addAll(data);
            srltVisa.setRefreshing(false);
            emptyWrapper.notifyDataSetChanged();
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
        mLayoutManager.setScrollEnabled(false);
    }
}

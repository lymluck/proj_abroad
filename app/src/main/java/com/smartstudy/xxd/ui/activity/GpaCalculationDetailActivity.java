package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.GpaCalculationDetail;
import com.smartstudy.xxd.mvp.contract.GpaCalculationDetailContract;
import com.smartstudy.xxd.mvp.presenter.GpaCalculationPresenter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yqy on 2017/10/23.
 */

public class GpaCalculationDetailActivity extends UIActivity implements GpaCalculationDetailContract.View {
    private RecyclerView rvGpaDetail;
    private EmptyWrapper<TreeItem> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private CommonAdapter<GpaCalculationDetail> mAdapter;
    private List<GpaCalculationDetail> gpaCalculationDetailList;
    private GpaCalculationDetailContract.Presenter presenter;
    private View emptyView;
    private View footView;
    private HeaderAndFooterWrapper mHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa_detail_activity);
    }

    @Override
    protected void onDestroy() {
        if (rvGpaDetail != null) {
            rvGpaDetail.removeAllViews();
            rvGpaDetail = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (gpaCalculationDetailList != null) {
            gpaCalculationDetailList.clear();
            gpaCalculationDetailList = null;
        }
        if (presenter != null) {
            presenter = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initViewAndData() {
        ((TextView) this.findViewById(R.id.topdefault_centertitle)).setText("GPA计算");
        this.findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        rvGpaDetail = (RecyclerView) findViewById(R.id.rclv_gpa_detail);
        rvGpaDetail.setHasFixedSize(true);

        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvGpaDetail.setLayoutManager(mLayoutManager);
        new GpaCalculationPresenter(this);
        initAdapter();
        initHeaderAndFooter();
        rvGpaDetail.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        emptyView = mInflater.inflate(R.layout.layout_empty, rvGpaDetail, false);
        presenter.showLoading(this, emptyView);
        if (getIntent() != null) {
            if (getIntent().hasExtra("result")) {
                presenter.getGpaDetail(getIntent().getStringExtra("result"));
            } else {
                finish();
            }
        }
    }

    @Override
    public void initEvent() {
        footView.findViewById(R.id.tv_explain).setOnClickListener(this);
        this.findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.tv_explain:
                startActivity(new Intent(this, GpaDescriptionActivity.class));
                break;

            case R.id.topdefault_leftbutton:
                finish();
                break;

            default:
                break;
        }
    }


    private void initAdapter() {
        gpaCalculationDetailList = new ArrayList<GpaCalculationDetail>();
        mAdapter = new CommonAdapter<GpaCalculationDetail>(this, R.layout.item_gpa_detail, gpaCalculationDetailList) {
            @Override
            protected void convert(final ViewHolder holder, GpaCalculationDetail gpaCalculationDetail, final int position) {
                holder.setText(R.id.tv_name, gpaCalculationDetail.getName());
                holder.setText(R.id.tv_gpa_result, gpaCalculationDetail.getGpaResult());
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
    }

    @Override
    public void setPresenter(GpaCalculationDetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void showGpaDetail(List<GpaCalculationDetail> data) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            if (gpaCalculationDetailList != null) {
                gpaCalculationDetailList.clear();
            }
            if (data != null && data.size() >= 0) {
                gpaCalculationDetailList.addAll(data);
                mAdapter.notifyDataSetChanged();
                footView.setVisibility(View.VISIBLE);
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
        presenter.showLoading(this, emptyView);
        if (getIntent() != null) {
            if (getIntent().hasExtra("result")) {
                presenter.getGpaDetail(getIntent().getStringExtra("result"));
            } else {
                finish();
            }
        }
    }


    private void initHeaderAndFooter() {
        footView = mInflater.inflate(R.layout.layout_gpa_footer, null, false);
        mHeader = new HeaderAndFooterWrapper(emptyWrapper);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        footView.setLayoutParams(lp);
        footView.setVisibility(View.GONE);
        mHeader.addFootView(footView);
        rvGpaDetail.setAdapter(mHeader);
    }
}

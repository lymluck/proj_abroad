package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.TreeRecyclerAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItem;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.VisaAddrInfo;
import com.smartstudy.xxd.mvp.contract.VisaAddrContract;
import com.smartstudy.xxd.mvp.presenter.VisaAddrPresenter;
import com.smartstudy.xxd.ui.adapter.VisaAddrOneTreeItemParent;

import java.util.ArrayList;
import java.util.List;

public class VisaAddrActivity extends UIActivity implements VisaAddrContract.View {

    private RecyclerView rclv_visa_addr;
    private EmptyWrapper<TreeItem> emptyWrapper;
    private TreeRecyclerAdapter visaAdapter;
    private View emptyView;


    private VisaAddrContract.Presenter visaP;
    private ArrayList<TreeItem> rankTypeTreeItems;
    private String countryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_addr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity(CommonSearchActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        Intent args = getIntent();
        countryId = args.getStringExtra("countryId");
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(args.getStringExtra("title"));
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        rclv_visa_addr = (RecyclerView) findViewById(R.id.rclv_visa_addr);
        LinearLayoutManager visaLayoutManager = new LinearLayoutManager(this);
        rclv_visa_addr.setHasFixedSize(true);
        rclv_visa_addr.setLayoutManager(visaLayoutManager);
        rclv_visa_addr.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
        new VisaAddrPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclv_visa_addr, false);
        visaP.showLoading(this, emptyView);
        visaP.getVisaAddr(countryId);
    }

    private void initAdapter() {
        visaAdapter = new TreeRecyclerAdapter();
        rankTypeTreeItems = new ArrayList<>();
        emptyWrapper = new EmptyWrapper<>(visaAdapter);
        rclv_visa_addr.setAdapter(emptyWrapper);
    }

    @Override
    public void initEvent() {
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
    public void setPresenter(VisaAddrContract.Presenter presenter) {
        if (presenter != null) {
            this.visaP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void showVisaAddr(List<VisaAddrInfo> data) {
        visaP.setEmptyView(this, emptyView);
        rankTypeTreeItems.clear();
        rankTypeTreeItems.addAll(ItemFactory.createTreeItemList(data, VisaAddrOneTreeItemParent.class, null));
        visaAdapter.setDatas(rankTypeTreeItems);
        rclv_visa_addr.setAdapter(visaAdapter);
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void reload() {
        visaP.showLoading(this, emptyView);
        visaP.getVisaAddr(countryId);
    }
}

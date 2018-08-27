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
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.VisaQaInfo;
import com.smartstudy.xxd.mvp.contract.VisaQaContract;
import com.smartstudy.xxd.mvp.presenter.VisaQaPresenter;
import com.smartstudy.xxd.ui.adapter.VisaQaOneTreeItemParent;

import java.util.ArrayList;
import java.util.List;

public class VisaQaActivity extends UIActivity implements VisaQaContract.View {

    private RecyclerView rclv_visa_qa;
    private EmptyWrapper<TreeItem> emptyWrapper;
    private TreeRecyclerAdapter visaQaAdapter;
    private View emptyView;


    private VisaQaContract.Presenter rtP;
    private ArrayList<TreeItem> rankTypeTreeItems;
    private String countryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_qa);
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
        rclv_visa_qa = (RecyclerView) findViewById(R.id.rclv_visa_qa);
        LinearLayoutManager visaQaLayoutManager = new LinearLayoutManager(this);
        rclv_visa_qa.setHasFixedSize(true);
        rclv_visa_qa.setLayoutManager(visaQaLayoutManager);
        rclv_visa_qa.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
        new VisaQaPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclv_visa_qa, false);
        rtP.showLoading(this, emptyView);
        rtP.getVisaQa(countryId);
    }

    private void initAdapter() {
        visaQaAdapter = new TreeRecyclerAdapter();
        rankTypeTreeItems = new ArrayList<>();
        emptyWrapper = new EmptyWrapper<>(visaQaAdapter);
        rclv_visa_qa.setAdapter(emptyWrapper);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.tv_add_qa).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.tv_add_qa:
                startActivity(new Intent(VisaQaActivity.this, AddQuestionActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(VisaQaContract.Presenter presenter) {
        if (presenter != null) {
            this.rtP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void showVisaQas(List<VisaQaInfo> data) {
        rtP.setEmptyView(emptyView);
        rankTypeTreeItems.clear();
        rankTypeTreeItems.addAll(ItemFactory.createTreeItemList(data, VisaQaOneTreeItemParent.class, null));
        visaQaAdapter.setDatas(rankTypeTreeItems);
        rclv_visa_qa.setAdapter(visaQaAdapter);
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void reload() {
        rtP.showLoading(this, emptyView);
        rtP.getVisaQa(countryId);
    }
}

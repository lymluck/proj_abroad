package com.smartstudy.xxd.ui.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ReportReasonsInfo;
import com.smartstudy.xxd.mvp.contract.ReportContract;
import com.smartstudy.xxd.mvp.presenter.ReportPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqy on 2017/12/26.
 */

public class ReportActivity extends UIActivity implements ReportContract.View {
    private RecyclerView rvReportReason;
    private NoScrollLinearLayoutManager mLayoutManager;
    private CommonAdapter<ReportReasonsInfo> mAdapter;

    private List<ReportReasonsInfo> datas;

    private ReportContract.Presenter presenter;

    private int mSelectedPos = -1;

    private String ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_teacher);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void initViewAndData() {
        rvReportReason = findViewById(R.id.rv_teacher);

        TextView titleView = (TextView) findViewById(R.id.topdefault_centertitle);

        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);

        titleView.setText("举报原因");
        ids=getIntent().getStringExtra("id");

        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        findViewById(R.id.topdefault_righttext).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.topdefault_righttext)).setText("提交");
        ((TextView) findViewById(R.id.topdefault_righttext)).setTextColor(Color.parseColor("#078CF1"));

        rvReportReason.setHasFixedSize(true);

        rvReportReason.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).margin(48, 0).build());

        mLayoutManager = new NoScrollLinearLayoutManager(this);

        mLayoutManager.setScrollEnabled(true);

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvReportReason.setLayoutManager(mLayoutManager);

        initAdapter();

        rvReportReason.setAdapter(mAdapter);

        new ReportPresenter(this);

        presenter.getReportReson();

    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //实现单选方法二： notifyItemChanged() 定向刷新两个视图
                //如果勾选的不是已经勾选状态的Item
                if (mSelectedPos!=position){
                    //先取消上个item的勾选状态
                    if(mSelectedPos!=-1) {
                        datas.get(mSelectedPos).setSelect(false);
                        mAdapter.notifyItemChanged(mSelectedPos);
                    }
                    //设置新Item的勾选状态
                    mSelectedPos = position;
                    datas.get(mSelectedPos).setSelect(true);
                    mAdapter.notifyItemChanged(mSelectedPos);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        findViewById(R.id.topdefault_righttext).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;

            case R.id.topdefault_righttext:
                if(mSelectedPos==-1){
                    ToastUtils.showToast(this,"请选择举报原因");
                }else {
                    presenter.submitReport(ids, datas.get(mSelectedPos).getId());
                }

        }

    }


    private void initAdapter() {
        datas = new ArrayList<>();
        mAdapter = new CommonAdapter<ReportReasonsInfo>(this, R.layout.item_report_teacher, datas) {

            @Override
            protected void convert(ViewHolder holder, ReportReasonsInfo reportReasonsInfo, int position) {
                holder.setText(R.id.tv_reason, reportReasonsInfo.getName());
                if (reportReasonsInfo.isSelect()) {
                    holder.getView(R.id.iv_select).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.iv_select).setVisibility(View.GONE);
                }

            }
        };
    }


    @Override
    public void setPresenter(ReportContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }

    }

    @Override
    public void showTip(String errCode, String message) {

        ToastUtils.showToast(this, message);
    }

    @Override
    public void showReportReson(List<ReportReasonsInfo> reportReasonsInfos) {
        if (this.datas != null) {
            datas.clear();
            if (reportReasonsInfos != null && reportReasonsInfos.size() > 0) {
                datas.addAll(reportReasonsInfos);
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void submitReportResult() {
        ToastUtils.showToast(this,"提交成功");
        finish();
    }
}

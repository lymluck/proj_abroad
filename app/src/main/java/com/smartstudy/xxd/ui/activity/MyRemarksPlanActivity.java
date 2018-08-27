package com.smartstudy.xxd.ui.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.utils.DateTimeUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.MyRemarkPlanInfo;
import com.smartstudy.xxd.mvp.contract.MyRemarksPlanContract;
import com.smartstudy.xxd.mvp.presenter.MyRemarkPlanPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/5/23
 * @describe TODO 我的备考课程
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyRemarksPlanActivity extends BaseActivity implements MyRemarksPlanContract.View {
    private RecyclerView rvRemarkPlan;
    private CommonAdapter<MyRemarkPlanInfo> mAdapter;
    private EmptyWrapper<MyRemarkPlanInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private List<MyRemarkPlanInfo> myRemarkPlanInfos;
    private MyRemarksPlanContract.Presenter presenter;
    private TextView topRight;
    private LinearLayout llAddPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_plan);
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("我的考试计划");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        topRight = findViewById(R.id.topdefault_righttext);
        topRight.setTextColor(Color.parseColor("#078CF1"));
        findViewById(R.id.topdefault_leftbutton).setVisibility(View.GONE);
        topRight.setText("完成");
        topRight.setVisibility(View.VISIBLE);
        rvRemarkPlan = findViewById(R.id.rv_plan);
        llAddPlan = findViewById(R.id.ll_add_plan);
        rvRemarkPlan.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRemarkPlan.setLayoutManager(mLayoutManager);
        initAdapter();
        new MyRemarkPlanPresenter(this);
        emptyView = getLayoutInflater().inflate(R.layout.layout_empty, rvRemarkPlan, false);
        presenter.showLoading(this, emptyView);
        presenter.getMyRemarks();
    }

    @Override
    public void initEvent() {
        topRight.setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        llAddPlan.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_righttext:
                finish();
                break;
            case R.id.ll_add_plan:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(MyRemarksPlanContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }

    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void getMyRemarksPlanSuccess(List<MyRemarkPlanInfo> myRemarkPlanInfos) {
        presenter.setEmptyView(emptyView);
        if (myRemarkPlanInfos != null) {
            this.myRemarkPlanInfos.clear();
            this.myRemarkPlanInfos.addAll(myRemarkPlanInfos);
        }
        emptyWrapper.notifyDataSetChanged();
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
        mLayoutManager.setScrollEnabled(false);
    }


    private void initAdapter() {
        myRemarkPlanInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<MyRemarkPlanInfo>(this, R.layout.item_remark_plan, myRemarkPlanInfos) {
            @Override
            protected void convert(ViewHolder holder, MyRemarkPlanInfo myRemarkPlanInfo, int position) {
                holder.setText(R.id.tv_course_name, myRemarkPlanInfo.getExam());
                String dateText = DateTimeUtils.dateStrFormat(myRemarkPlanInfo.getDate(), new SimpleDateFormat("yyyy-MM-dd"),
                    new SimpleDateFormat("yyyy年MM月dd日")).substring(5);
                holder.setText(R.id.tv_time, dateText + " " + myRemarkPlanInfo.getWeekday());
                holder.setText(R.id.tv_join_count, myRemarkPlanInfo.getSelectCount() + "位选校帝用户参加");
                TextView tvRestTime = holder.getView(R.id.tv_rest_time);
                LinearLayout llCount = holder.getView(R.id.ll_count);
                TextView tvCount = holder.getView(R.id.tv_count);
                if (myRemarkPlanInfo.isOver()) {
                    llCount.setVisibility(View.GONE);
                    tvRestTime.setVisibility(View.VISIBLE);
                    tvRestTime.setTextColor(Color.parseColor("#949BA1"));
                    tvRestTime.setText("已考");
                } else {
                    if (myRemarkPlanInfo.getCountdown().equals("0")) {
                        tvRestTime.setText("今日开考");
                        llCount.setVisibility(View.GONE);
                        tvRestTime.setVisibility(View.VISIBLE);
                        tvRestTime.setTextColor(Color.parseColor("#FF8A00"));
                    } else {
                        llCount.setVisibility(View.VISIBLE);
                        tvRestTime.setVisibility(View.GONE);
                        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/GothamMedium.otf");
                        // 应用字体
                        tvCount.setTypeface(typeFace);
                        tvCount.setText(myRemarkPlanInfo.getCountdown());
                    }
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        rvRemarkPlan.setAdapter(emptyWrapper);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.remark_close);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rvRemarkPlan != null) {
            rvRemarkPlan.removeAllViews();
            rvRemarkPlan = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (myRemarkPlanInfos != null) {
            myRemarkPlanInfos.clear();
            myRemarkPlanInfos = null;
        }
    }
}
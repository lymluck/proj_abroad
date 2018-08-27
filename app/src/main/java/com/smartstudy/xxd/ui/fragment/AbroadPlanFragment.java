package com.smartstudy.xxd.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.TagsLayout;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.AbroadPlanInfo;
import com.smartstudy.xxd.mvp.contract.AbroadPlanContract;

import java.util.List;

public class AbroadPlanFragment extends UIFragment {

    private RecyclerView rclv_plan;
    private LinearLayoutManager mLayoutManager;
    private CommonAdapter<AbroadPlanInfo.StagesEntity> mAdapter;

    private AbroadPlanContract.Presenter presenter;
    private List<AbroadPlanInfo.StagesEntity> planInfoList;

    public static AbroadPlanFragment getInstance(Bundle bundle) {
        AbroadPlanFragment fragment = new AbroadPlanFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.fragment_abroad_plan, null);
    }

    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter = null;
        }
        if (rclv_plan != null) {
            rclv_plan.removeAllViews();
            rclv_plan = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (planInfoList != null) {
            planInfoList.clear();
            planInfoList = null;
        }
        super.onDetach();
    }

    @Override
    protected void initView(View rootView) {
        rclv_plan = (RecyclerView) rootView.findViewById(R.id.rclv_plan);
        rclv_plan.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_plan.setLayoutManager(mLayoutManager);
        rclv_plan.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
    }

    private void initAdapter() {
        planInfoList = getArguments().getParcelableArrayList("planList");
        mAdapter = new CommonAdapter<AbroadPlanInfo.StagesEntity>(mActivity, R.layout.item_abroad_plan_list, planInfoList) {
            @Override
            protected void convert(ViewHolder holder, AbroadPlanInfo.StagesEntity planInfo, int position) {
                if (planInfo != null) {
                    TextView view_left_circle = holder.getView(R.id.view_left_circle);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_left_circle.getLayoutParams();
                    TagsLayout tly_tags = holder.getView(R.id.tly_tags);
                    holder.getView(R.id.view_last).setVisibility(View.GONE);
                    if (position == 0) {
                        holder.getView(R.id.view_first).setVisibility(View.VISIBLE);
                        holder.getView(R.id.llyt_content).setVisibility(View.GONE);
                        tly_tags.setVisibility(View.VISIBLE);
                        view_left_circle.setText(planInfo.getName());
                        tly_tags.setBackGroup(R.drawable.bg_study_plan_tab4);
                        tly_tags.createTab(mActivity, planInfo.getItems());
                    } else {
                        if (position == planInfoList.size() - 1) {
                            holder.getView(R.id.view_last).setVisibility(View.VISIBLE);
                        }
                        holder.getView(R.id.view_first).setVisibility(View.GONE);
                        holder.getView(R.id.llyt_content).setVisibility(View.VISIBLE);
                        tly_tags.setVisibility(View.GONE);
                        holder.setText(R.id.tv_plan, planInfo.getItems().toString());
                        view_left_circle.setText("");
                    }
                    TextView tv_title = holder.getView(R.id.tv_title);
                    if (planInfo.getIsCurrentStage()) {
                        tv_title.setTextColor(getResources().getColor(R.color.mytest_txt_color));
                        holder.setText(R.id.tv_title, planInfo.getTime() + "（当前位置）");
                        params.width = DensityUtils.dip2px(24);
                        params.height = DensityUtils.dip2px(24);
                        params.topMargin = DensityUtils.dip2px(28);
                        view_left_circle.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_current_grade));
                    } else {
                        tv_title.setTextColor(getResources().getColor(R.color.app_text_color2));
                        holder.setText(R.id.tv_title, planInfo.getTime());
                        if (position == 0) {
                            params.width = DensityUtils.dip2px(42);
                            params.height = DensityUtils.dip2px(42);
                            params.topMargin = DensityUtils.dip2px(18);
                            view_left_circle.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_grade));
                        } else {
                            params.width = DensityUtils.dip2px(16);
                            params.height = DensityUtils.dip2px(16);
                            params.topMargin = DensityUtils.dip2px(32);
                            view_left_circle.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_curren_grade_blue));
                        }
                    }
                    view_left_circle.setLayoutParams(params);
                    holder.setText(R.id.tv_xq, planInfo.getName());
                }
            }
        };
        rclv_plan.setAdapter(mAdapter);

    }
}

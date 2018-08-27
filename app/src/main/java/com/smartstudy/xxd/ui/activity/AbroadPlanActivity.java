package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.SlidingTabStrip;
import com.smartstudy.commonlib.ui.customview.TagsLayout;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.AbroadPlanInfo;
import com.smartstudy.xxd.mvp.contract.AbroadPlanContract;
import com.smartstudy.xxd.mvp.presenter.AbroadPlanPresenter;

import java.util.ArrayList;
import java.util.List;

public class AbroadPlanActivity extends BaseActivity implements AbroadPlanContract.View {

    private SlidingTabStrip tabs_xl;

    private AbroadPlanContract.Presenter presenter;

    private RecyclerView rclv_plan;

    private LinearLayoutManager mLayoutManager;

    private CommonAdapter<AbroadPlanInfo.StagesEntity> mAdapter;

    private List<AbroadPlanInfo.StagesEntity> planInfoList;

    List<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abroad_plan);
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("留学时间规划");
        tabs_xl = (SlidingTabStrip) findViewById(R.id.tabs_xl);
        new AbroadPlanPresenter(this);
        rclv_plan = (RecyclerView) findViewById(R.id.rclv_plan);
        rclv_plan.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_plan.setLayoutManager(mLayoutManager);
        rclv_plan.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
        presenter.getPlan(ParameterUtils.NETWORK_ELSE_CACHED);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.tv_get_planning).setOnClickListener(this);
        tabs_xl.setTabOnclick(new SlidingTabStrip.TabOnclick() {
            @Override
            public void getSelectPosition(int position) {
                if (position > names.size()) {
                    return;
                } else {
                    ((LinearLayoutManager) rclv_plan.getLayoutManager()).scrollToPositionWithOffset(getPosition(position), 0);
                }
            }
        });


        rclv_plan.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                final int nowPosition = l.findFirstVisibleItemPosition();
                if (planInfoList.get(nowPosition).getCurrentIndex() != tabs_xl.getCurrentIndex()) {
                    tabs_xl.scrollToChild(planInfoList.get(nowPosition).getCurrentIndex());

                }
            }
        });
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.tv_get_planning:
                startActivity(new Intent(this, StudyGetPlanningActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(AbroadPlanContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void getPlanSuccess(List<AbroadPlanInfo> datas, List<String> names, List<AbroadPlanInfo.StagesEntity> stagesEntities, int position, int currentIndex) {
        if (this.names != null) {
            this.names.clear();
        }
        this.names.addAll(names);

        int len = datas.size();
        if (len > 0) {
            findViewById(R.id.llyt_has_data).setVisibility(View.VISIBLE);
            findViewById(R.id.llyt_no_data).setVisibility(View.GONE);
            tabs_xl.setVisibility(View.VISIBLE);
            for (int i = 0; i < names.size(); i++) {
                tabs_xl.addTextTab(i, names.get(i), names.size());
            }

            if (stagesEntities != null && stagesEntities.size() > 0) {
                planInfoList.addAll(stagesEntities);
                mAdapter.notifyDataSetChanged();
                tabs_xl.setCurrentIndex(currentIndex);
                if (getCurrentIndex() == -1) {
                    ((LinearLayoutManager) rclv_plan.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                } else {
                    ((LinearLayoutManager) rclv_plan.getLayoutManager()).scrollToPositionWithOffset(getCurrentIndex(), 0);

                }
            }
        } else {
            findViewById(R.id.llyt_has_data).setVisibility(View.GONE);
            findViewById(R.id.llyt_no_data).setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void toPlan() {
        findViewById(R.id.llyt_has_data).setVisibility(View.GONE);
        findViewById(R.id.llyt_no_data).setVisibility(View.VISIBLE);
    }


    private void initAdapter() {
        planInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<AbroadPlanInfo.StagesEntity>(this, R.layout.item_abroad_plan_list, planInfoList) {
            @Override
            protected void convert(ViewHolder holder, AbroadPlanInfo.StagesEntity planInfo, int position) {
                if (planInfo != null) {
                    TextView view_left_circle = holder.getView(R.id.view_left_circle);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_left_circle.getLayoutParams();
                    TagsLayout tly_tags = holder.getView(R.id.tly_tags);
                    holder.getView(R.id.view_last).setVisibility(View.GONE);
                    if ("关键词".equals(planInfo.getTime())) {
                        holder.getView(R.id.view_first).setVisibility(View.VISIBLE);
                        holder.getView(R.id.llyt_content).setVisibility(View.GONE);
                        tly_tags.setVisibility(View.VISIBLE);
                        view_left_circle.setText(planInfo.getName());
                        tly_tags.setBackGroup(R.drawable.bg_study_plan_tab4);
                        tly_tags.createTab(AbroadPlanActivity.this, planInfo.getItems());
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
                        if ("关键词".equals(planInfo.getTime())) {
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


    private int getPosition(int currentIndex) {
        if (planInfoList != null && planInfoList.size() > 0) {
            for (int i = 0; i < planInfoList.size(); i++) {
                if (planInfoList.get(i).getCurrentIndex() == currentIndex) {
                    return i;
                }
            }
        }
        return 0;
    }


    private int getCurrentIndex() {
        if (planInfoList != null && planInfoList.size() > 0) {
            for (int i = 0; i < planInfoList.size(); i++) {
                if (planInfoList.get(i).getIsCurrentStage()) {
                    return i;
                }
            }
        }
        return -1;
    }

}

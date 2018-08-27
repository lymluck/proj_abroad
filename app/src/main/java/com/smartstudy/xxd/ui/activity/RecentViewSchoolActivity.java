package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
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
import com.smartstudy.xxd.entity.PersonalInfo;
import com.smartstudy.xxd.entity.RecentViewSchoolInfo;
import com.smartstudy.xxd.mvp.contract.RecentViewSchoolContract;
import com.smartstudy.xxd.mvp.contract.SchoolListContract;
import com.smartstudy.xxd.mvp.presenter.RecentViewSchoolPresenter;
import com.smartstudy.xxd.mvp.presenter.SchoolListPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author yqy
 * @date on 2018/6/13
 * @describe 看过的学校列表界面
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class RecentViewSchoolActivity extends BaseActivity implements RecentViewSchoolContract.View {
    private LoadMoreRecyclerView rclvSchools;
    private LoadMoreWrapper<RecentViewSchoolInfo> loadMoreWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private RecentViewSchoolContract.Presenter presenter;
    private View emptyView;
    private ArrayList<RecentViewSchoolInfo> schoolsInfoList;
    private CommonAdapter<RecentViewSchoolInfo> mAdapter;
    private EmptyWrapper<RecentViewSchoolInfo> emptyWrapper;
    private int mPage = 1;
    private String lastGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_school);
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("看过的学校");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        rclvSchools = (LoadMoreRecyclerView) findViewById(R.id.rclv_recent_school);
        rclvSchools.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvSchools.setLayoutManager(mLayoutManager);
        rclvSchools.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16f),0).colorResId(R.color.horizontal_line_color).build());
        initAdapter();
        new RecentViewSchoolPresenter(this);
        emptyView = mInflater.inflate(R.layout.layout_empty, rclvSchools, false);
        presenter.showLoading(this, emptyView);
        presenter.getRecentSchoolList(mPage, ParameterUtils.PULL_DOWN);
    }


    private void initAdapter() {
        schoolsInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<RecentViewSchoolInfo>(this, R.layout.item_recent_view_school, schoolsInfoList,
            mInflater) {
            @Override
            protected void convert(ViewHolder holder, final RecentViewSchoolInfo schoolInfo, int position) {
                if (schoolInfo.isHeadVisible()) {
                    holder.getView(R.id.tv_group_name).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_group_name, schoolInfo.getGroupName());
                } else {
                    holder.getView(R.id.tv_group_name).setVisibility(View.GONE);
                }
                holder.setText(R.id.tv_school_name, schoolInfo.getChineseName());
                holder.setText(R.id.tv_time, schoolInfo.getViewTimeText());
                holder.getView(R.id.ll_school).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle params = new Bundle();
                        params.putString("id", schoolInfo.getId() + "");
                        Intent toMoreDetails = new Intent(RecentViewSchoolActivity.this,
                            CollegeDetailActivity.class);
                        toMoreDetails.putExtras(params);
                        startActivity(toMoreDetails);
                    }
                });
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvSchools.setAdapter(loadMoreWrapper);
    }


    @Override
    public void initEvent() {
        rclvSchools.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                mPage = mPage + 1;
                presenter.getRecentSchoolList(mPage, ParameterUtils.PULL_UP);
            }
        });

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
    public void setPresenter(RecentViewSchoolContract.Presenter presenter) {
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
    public void getRecentSuccess(List<RecentViewSchoolInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(this, emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvSchools.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                schoolsInfoList.clear();
                schoolsInfoList.addAll(handleGroupData(data));
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
                    schoolsInfoList.addAll(handleGroupData(data));
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
        rclvSchools.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }

    private List<RecentViewSchoolInfo> handleGroupData(List<RecentViewSchoolInfo> data) {
        if (data != null && data.size() > 0) {
            for (RecentViewSchoolInfo recentViewSchoolInfo : data) {
                if (!recentViewSchoolInfo.getGroupName().equals(lastGroup)) {
                    lastGroup = recentViewSchoolInfo.getGroupName();
                    recentViewSchoolInfo.setHeadVisible(true);
                } else {
                    recentViewSchoolInfo.setHeadVisible(false);
                }
            }
        }
        return data;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }

        if (rclvSchools != null) {
            rclvSchools.removeAllViews();
            rclvSchools = null;
        }

        if (schoolsInfoList != null) {
            schoolsInfoList.clear();
            schoolsInfoList = null;
        }

        if (mAdapter != null) {
            mAdapter = null;
        }
    }
}
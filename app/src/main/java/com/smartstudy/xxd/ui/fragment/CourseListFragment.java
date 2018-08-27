package com.smartstudy.xxd.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.CourseInfo;
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customView.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customView.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customView.RatingBar;
import com.smartstudy.commonlib.ui.customView.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CourseGroup;
import com.smartstudy.xxd.mvp.contract.CourseListContract;
import com.smartstudy.xxd.mvp.presenter.CourseListPresenter;
import com.smartstudy.xxd.ui.activity.CourseDetailActivity;
import com.smartstudy.xxd.ui.adapter.courseList.CourseListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CourseListFragment extends UIFragment implements CourseListContract.View {

    private RecyclerView rclvCourse;
    private SwipeRefreshLayout srltCourse;
//    private LoadMoreWrapper<CourseInfo> loadMoreWrapper;
    private EmptyWrapper<CourseGroup> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private CourseListAdapter mAdapter;

    private int mPage = 1;
    private CourseListContract.Presenter presenter;
    private List<CourseGroup> courseGroups;

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.fragment_course_list, null);
    }

    @Override
    public void onDetach() {
        if (rclvCourse != null) {
            rclvCourse.removeAllViews();
            rclvCourse = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (courseGroups != null) {
            courseGroups.clear();
            courseGroups = null;
        }
        if (presenter != null) {
            presenter = null;
        }
        super.onDetach();
    }

    //初始化控件
    @Override
    protected void initView(View rootView) {
        //修正标题栏的位置
        RelativeLayout top_course = (RelativeLayout) rootView.findViewById(R.id.top_course);
        rootView.findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) top_course.getLayoutParams();
            params.height = params.height + ScreenUtils.getStatusHeight(mActivity);
            top_course.setLayoutParams(params);
            top_course.setPadding(0, ScreenUtils.getStatusHeight(mActivity), 0, 0);
        }
        ((TextView) rootView.findViewById(R.id.topdefault_centertitle)).setText("留学课程");
        this.srltCourse = (SwipeRefreshLayout) rootView.findViewById(R.id.srlt_course);
        srltCourse.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        //刷新数据
        srltCourse.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getCourses(mPage, ParameterUtils.PULL_DOWN);

            }
        });
        Bundle data = getArguments();
        if ("home".equals(data.getString("data_flag"))) {
            rootView.findViewById(R.id.topdefault_leftbutton).setVisibility(View.GONE);
        }
        this.rclvCourse = (RecyclerView) rootView.findViewById(R.id.rclv_course);
        rclvCourse.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvCourse.setLayoutManager(mLayoutManager);
        //初始化适配器
        initAdapter();
        new CourseListPresenter(this);
//        rclvCourse.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
//                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).margin(16,16).build());
        //加载动画
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, rclvCourse, false);
        presenter.showLoading(mActivity, emptyView);
        presenter.getCourses(mPage, ParameterUtils.PULL_DOWN);
        rootView.findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    //初始化适配器
    private void initAdapter() {
        courseGroups = new ArrayList<>();
        mAdapter = new CourseListAdapter(getActivity(), courseGroups, getActivity().getLayoutInflater());
        emptyWrapper = new EmptyWrapper<>(mAdapter);
//        mHeader = new HeaderAndFooterWrapper(mAdapter);
//        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvCourse.setAdapter(emptyWrapper);
        //加载更多数据
//        rclvCourse.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void OnLoad() {
//                if (srltCourse.isRefreshing()) {
//                    rclvCourse.loadComplete(true);
//                    return;
//                }
//                mPage = mPage + 1;
//                presenter.getCourses(mPage, ParameterUtils.PULL_UP);
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                mActivity.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(CourseListContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            srltCourse.setRefreshing(false);
//            rclvCourse.loadComplete(true);
            ToastUtils.showToast(mActivity, message);
        }
    }

    @Override
    public void showCourses(List<CourseGroup> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if(courseGroups!=null) {
                courseGroups.clear();
                courseGroups.addAll(data);
                srltCourse.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
            }
//            if (request_state == ParameterUtils.PULL_DOWN) {
//                //下拉刷新
//                if (len <= 0) {
//                    rclvCourse.loadComplete(true);
//                    mLayoutManager.setScrollEnabled(false);
//                }
//                courseGroups.clear();
//                courseGroups.addAll(data);
//                srltCourse.setRefreshing(false);
//                mAdapter.notifyDataSetChanged();
//                loadMoreWrapper.notifyDataSetChanged();
//            } else if (request_state == ParameterUtils.PULL_UP) {
//                //上拉加载
//                if (len <= 0) {
//                    //没有更多内容
//                    if (mPage > 1) {
//                        mPage = mPage - 1;
//                    }
//                    if (courseGroups.size() > 0) {
//                        rclvCourse.loadComplete(false);
//                    } else {
//                        rclvCourse.loadComplete(true);
//                    }
//                } else {
//                    rclvCourse.loadComplete(true);
//                    courseGroups.addAll(data);
//                    mAdapter.notifyDataSetChanged();
//                    loadMoreWrapper.notifyDataSetChanged();
//                }
//            }
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
//        loadMoreWrapper.notifyDataSetChanged();
//        rclvCourse.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

}

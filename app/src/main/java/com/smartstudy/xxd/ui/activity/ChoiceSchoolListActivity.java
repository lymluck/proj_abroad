package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ChoiceSchoolListInfo;
import com.smartstudy.xxd.entity.OtherStudentChoiceDetailInfo;
import com.smartstudy.xxd.mvp.contract.ChoiceSchoolListContract;
import com.smartstudy.xxd.mvp.presenter.ChoiceSchoolListPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/4
 * @describe 选择了该校的人员列表
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChoiceSchoolListActivity extends BaseActivity implements ChoiceSchoolListContract.View {
    private ChoiceSchoolListContract.Presenter presenter;
    private LoadMoreRecyclerView rcvSchool;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<ChoiceSchoolListInfo> mAdapter;
    private LoadMoreWrapper<ChoiceSchoolListInfo> loadMoreWrapper;
    private EmptyWrapper<ChoiceSchoolListInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private List<ChoiceSchoolListInfo> choiceSchoolListInfos;
    private int mPage = 1;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_school_list);
    }

    @Override
    public void setPresenter(ChoiceSchoolListContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null && rcvSchool != null) {
            swipeRefreshLayout.setRefreshing(false);
            rcvSchool.loadComplete(true);
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void getChoiceSchoolListSuccess(List<ChoiceSchoolListInfo> data, int request_state) {
        if (presenter != null && choiceSchoolListInfos != null) {
            presenter.setEmptyView(this, emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rcvSchool.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                choiceSchoolListInfos.clear();
                choiceSchoolListInfos.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rcvSchool.loadComplete(false);
                } else {
                    rcvSchool.loadComplete(true);
                    choiceSchoolListInfos.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void showEmptyView(View view) {
        if (rcvSchool != null) {
            emptyWrapper.setEmptyView(view);
            loadMoreWrapper.notifyDataSetChanged();
            rcvSchool.loadComplete(true);
            mLayoutManager.setScrollEnabled(false);
        }
    }

    @Override
    public void getOtherStudentDetailSuccess(OtherStudentChoiceDetailInfo otherStudentChoiceDetailInfo) {
        if (otherStudentChoiceDetailInfo != null) {
            startActivity(new Intent(this, ChoiceSchoolPersonActivity.class).putExtra("person_detail", otherStudentChoiceDetailInfo));
        }
    }

    @Override
    protected void initViewAndData() {
        id = getIntent().getStringExtra("id");
        rcvSchool = findViewById(R.id.rclv_school);
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("他们选择了该校");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlt_school);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getChoiceSchoolList(id, mPage, ParameterUtils.PULL_DOWN);
            }
        });

        rcvSchool.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvSchool.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(16f), 0).colorResId(R.color.bg_home_search).build());
        rcvSchool.setLayoutManager(mLayoutManager);
        initAdapter();
        new ChoiceSchoolListPresenter(this);
        emptyView = getLayoutInflater().inflate(R.layout.layout_empty, rcvSchool, false);
        presenter.showLoading(this, emptyView);
        presenter.getChoiceSchoolList(id, mPage, ParameterUtils.PULL_DOWN);
    }

    private void initAdapter() {
        choiceSchoolListInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<ChoiceSchoolListInfo>(this, R.layout.item_choice_list_item, choiceSchoolListInfos) {
            @Override
            protected void convert(ViewHolder holder, ChoiceSchoolListInfo choiceSchoolListInfo, int position) {
                holder.setPersonImageUrl(R.id.iv_avatar, choiceSchoolListInfo.getAvatar(), true);
                holder.setText(R.id.tv_name, choiceSchoolListInfo.getName());
                TextView tvCountDay = holder.getView(R.id.tv_count_day);
                String showText = "共<font color='#078CF1'>" + choiceSchoolListInfo.getSelectSchoolCount() + "</font>所选校";
                tvCountDay.setText(Html.fromHtml(showText));
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rcvSchool.setAdapter(loadMoreWrapper);
        rcvSchool.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    rcvSchool.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getChoiceSchoolList(id, mPage, ParameterUtils.PULL_UP);
            }
        });

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (choiceSchoolListInfos != null) {
                    presenter.getOtherStudentDetail(choiceSchoolListInfos.get(position).getId());
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

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
    protected void onDestroy() {
        super.onDestroy();
        if (rcvSchool != null) {
            rcvSchool.removeAllViews();
            rcvSchool = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (choiceSchoolListInfos != null) {
            choiceSchoolListInfos.clear();
            choiceSchoolListInfos = null;
        }
    }
}

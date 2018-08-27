package com.smartstudy.xxd.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.commonlib.app.BaseApplication;
import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.ConsultantsContract;
import com.smartstudy.xxd.mvp.presenter.ConsultantsPresenter;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by yqy on 2017/12/21.
 */

public class ConsultantsFragment extends UIFragment implements ConsultantsContract.View {

    private LoadMoreRecyclerView rclvConsultants;
    private CommonAdapter<ConsultantsInfo> mAdapter;
    private LoadMoreWrapper<ConsultantsInfo> loadMoreWrapper;
    private EmptyWrapper emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ConsultantsContract.Presenter presenter;
    private List<ConsultantsInfo> consultantsInfos;
    private int mPage = 1;

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.fragment_consultants_list, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = true;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        //加载动画
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, rclvConsultants, false);
        presenter.showLoading(mActivity, emptyView);
        presenter.getConsultantsList(mPage, ParameterUtils.PULL_DOWN);
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDetach() {
        if (rclvConsultants != null) {
            rclvConsultants.removeAllViews();
            rclvConsultants = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (consultantsInfos != null) {
            consultantsInfos.clear();
            consultantsInfos = null;
        }
        if (presenter != null) {
            presenter = null;
        }
        loadMoreWrapper = null;
        emptyWrapper = null;
        mLayoutManager = null;
        super.onDetach();
    }

    //初始化控件
    @Override
    protected void initView(View rootView) {
        swipeRefreshLayout = rootView.findViewById(R.id.srlyt_consultants);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        this.rclvConsultants = rootView.findViewById(R.id.rclv_consultants);
        rclvConsultants.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvConsultants.setLayoutManager(mLayoutManager);
        new ConsultantsPresenter(this);
        //初始化适配器
        initAdapter();
        rclvConsultants.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).margin(82, 0).build());
        initEvent();
    }

    private void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        rclvConsultants.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                mPage = mPage + 1;
                presenter.getConsultantsList(mPage, ParameterUtils.PULL_UP);
            }
        });
    }

    //初始化适配器
    private void initAdapter() {
        consultantsInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<ConsultantsInfo>(mActivity, R.layout.item_consultants_list, consultantsInfos) {
            @Override
            protected void convert(ViewHolder holder, final ConsultantsInfo consultantsInfo, final int position) {
                DisplayImageUtils.formatPersonImgUrl(mActivity, consultantsInfo.getAvatar(), (ImageView) holder.getView(R.id.iv_avatar));
                holder.setText(R.id.tv_name, consultantsInfo.getName());
                TextView title = holder.getView(R.id.tv_title);
                if (!TextUtils.isEmpty(consultantsInfo.getTitle())) {
                    title.setVisibility(View.VISIBLE);
                    title.setText(consultantsInfo.getTitle());
                } else {
                    title.setVisibility(View.GONE);
                }
                String yearsOfWorking = consultantsInfo.getYearsOfWorking();
                TextView tv_year_working = holder.getView(R.id.tv_year_working);
                if (!TextUtils.isEmpty(yearsOfWorking)) {
                    tv_year_working.setVisibility(View.VISIBLE);
                    tv_year_working.setText("从业" + yearsOfWorking + "年");
                } else {
                    tv_year_working.setVisibility(View.GONE);
                }
                TextView tvSchool = holder.getView(R.id.tv_school);
                if (!TextUtils.isEmpty(consultantsInfo.getSchool())) {
                    tvSchool.setText("毕业于" + consultantsInfo.getSchool());
                } else {
                    tvSchool.setVisibility(View.GONE);
                }
                if (consultantsInfo.getOrganization() != null) {
                    holder.setText(R.id.tv_organization_name, consultantsInfo.getOrganization().getName());
                    final ImageView iv = holder.getView(R.id.iv_logo);
                    iv.setAdjustViewBounds(true);
                    DisplayImageUtils.displayImage(BaseApplication.appContext, consultantsInfo.getOrganization().getSmallLogo(), new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            iv.setImageBitmap(bitmap);
                        }
                    });
                }
                holder.setText(R.id.tv_location, consultantsInfo.getLocation());
                holder.getView(R.id.ll_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConsultantsInfo info = consultantsInfos.get(position);
                        if (info != null && info.getImUserId() != null && RongIM.getInstance() != null) {
                            //获取教师状态
                            presenter.getTeacherInfo(info);
                        }
                    }
                });
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclvConsultants.setAdapter(loadMoreWrapper);
    }


    @Override
    public void setPresenter(ConsultantsContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            swipeRefreshLayout.setRefreshing(false);
            rclvConsultants.loadComplete(true);
            ToastUtils.showToast(mActivity, message);
        }
    }


    @Override
    public void showConsultantsList(List<ConsultantsInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvConsultants.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                consultantsInfos.clear();
                consultantsInfos.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rclvConsultants.loadComplete(false);
                } else {
                    rclvConsultants.loadComplete(true);
                    consultantsInfos.addAll(data);
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
        rclvConsultants.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public void toChat(int status, ConsultantsInfo info) {
        if (status == 4) {
            refresh();
            Log.w("kim","wode---->");
            showTip(null, "对方账号异常！");
        } else {
            Log.w("kim","wode---->"+info.toString());
            SPCacheUtils.put("Rong:" + info.getImUserId(), info.toString());
            RongIM.getInstance().startPrivateChat(getActivity(), info.getImUserId(), info.getName());
        }
    }

    private void refresh() {
        mPage = 1;
        presenter.getConsultantsList(mPage, ParameterUtils.PULL_DOWN);
    }
}


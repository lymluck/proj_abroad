package com.smartstudy.xxd.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.QaListContract;
import com.smartstudy.xxd.mvp.presenter.QuestionsPresenter;
import com.smartstudy.xxd.ui.activity.AddQuestionActivity;
import com.smartstudy.xxd.ui.activity.MainActivity;
import com.smartstudy.xxd.ui.activity.QaDetailActivity;

import java.util.ArrayList;
import java.util.List;


public class QaListFragment extends BaseFragment implements QaListContract.View {

    private LoadMoreRecyclerView rmrvQa;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<QuestionInfo> mAdapter;
    private LoadMoreWrapper<QuestionInfo> loadMoreWrapper;
    private EmptyWrapper<SchoolInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private QaListContract.Presenter presenter;
    private List<QuestionInfo> questionInfoList;
    private int mPage = 1;
    private String data_tag;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_qa_list;
    }

    public static QaListFragment getInstance(Bundle bundle) {
        QaListFragment fragment = new QaListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter = null;
        }
        if (rmrvQa != null) {
            rmrvQa.removeAllViews();
            rmrvQa = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (questionInfoList != null) {
            questionInfoList.clear();
            questionInfoList = null;
        }
        super.onDetach();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, rmrvQa,
            false);
        presenter.showLoading(mActivity, emptyView);
        getQa(ParameterUtils.PULL_DOWN);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if ("my".equals(data_tag) && (boolean) SPCacheUtils.get("my_qa_refresh", false)) {
            SPCacheUtils.remove("my_qa_refresh");
            refreshList();
        }

    }

    public void refreshList() {
        mPage = 1;
        getQa(ParameterUtils.PULL_DOWN);
    }

    @Override
    public void onUserInvisible() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        super.onUserInvisible();
    }

    @Override
    public void onFirstUserInvisible() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        super.onFirstUserInvisible();
    }

    @Override
    protected void initView() {
        rmrvQa = rootView.findViewById(R.id.rclv_qa);
        data_tag = getArguments().getString("data_flag");
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srlt_qa);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        rmrvQa.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rmrvQa.setLayoutManager(mLayoutManager);
        if ("list".equals(data_tag)) {
            rmrvQa.setPadding(0, 0, 0, 0);
            rmrvQa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                .size(DensityUtils.dip2px(10f)).colorResId(R.color.home_search_title_bg).build());
        } else if ("my".equals(data_tag)) {
            rmrvQa.setPadding(0, 0, 0, 0);
            rmrvQa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                .size(DensityUtils.dip2px(10f)).colorResId(R.color.home_search_title_bg).build());
        } else if ("school".equals(data_tag)) {
            rmrvQa.setPadding(0, 0, 0, 0);
            rmrvQa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                .size(DensityUtils.dip2px(10f)).colorResId(R.color.home_search_title_bg).build());
        } else if ("teacher".equals(data_tag)) {
            rmrvQa.setPadding(0, 0, 0, 0);
            int marginlr = DensityUtils.dip2px(16f);
            rmrvQa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                .size(DensityUtils.dip2px(0.5f)).margin(marginlr, marginlr)
                .colorResId(R.color.horizontal_line_color).build());
        } else if ("recommend".equals(data_tag)) {
            rmrvQa.setPadding(0, 0, 0, 0);
            rmrvQa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                .size(DensityUtils.dip2px(10f)).colorResId(R.color.home_search_title_bg).build());
        }
        initAdapter();
        initEvent();
        new QuestionsPresenter(this);
    }

    private void initEvent() {

    }

    private void initAdapter() {
        questionInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<QuestionInfo>(mActivity, R.layout.item_question_list, questionInfoList) {
            @Override
            protected void convert(ViewHolder holder, QuestionInfo questionInfo, int position) {
                String avatar = questionInfo.getAsker().getAvatar();
                String askName = questionInfo.getAsker().getName();
                TextView tv_default_name = holder.getView(R.id.tv_default_name);
                holder.setPersonImageUrl(R.id.iv_asker, avatar, true);
                tv_default_name.setVisibility(View.GONE);
                holder.setText(R.id.tv_qa_name, askName);
                holder.setText(R.id.tv_see, String.format(getString(R.string.visit_count), questionInfo.getVisitCount()));
                holder.setText(R.id.tv_answer_count, questionInfo.getAnswerCount() + " 回答");
                if ("list".equals(data_tag) || "teacher".equals(data_tag) || "recommend".equals(data_tag)) {
                    setupHolderView(holder, View.VISIBLE);
                    holder.getView(R.id.v_cricle).setVisibility(View.GONE);
                    if ("teacher".equals(data_tag)) {
                        holder.getView(R.id.view_home_list).setVisibility(View.GONE);
                        holder.getView(R.id.llyt_answer_person).setVisibility(View.GONE);
                    }
                    if ("recommend".equals(data_tag)) {
                        holder.getView(R.id.llyt_answer_person).setVisibility(View.GONE);
                        if (!TextUtils.isEmpty(questionInfo.getSchoolName())) {
                            holder.getView(R.id.llyt_schooln).setVisibility(View.VISIBLE);
                        } else {
                            holder.getView(R.id.llyt_schooln).setVisibility(View.GONE);
                            holder.getView(R.id.view_home_list).setVisibility(View.GONE);
                        }
                    }
                } else {
                    setupHolderView(holder, View.GONE);
                    if ("school".equals(data_tag)) {
                        holder.getView(R.id.v_cricle).setVisibility(View.GONE);
                        holder.getView(R.id.llyt_qa_person).setVisibility(View.VISIBLE);
                        holder.getView(R.id.tv_see).setVisibility(View.VISIBLE);
                        holder.getView(R.id.llyt_answer_person).setVisibility(View.VISIBLE);
                        holder.getView(R.id.line).setVisibility(View.VISIBLE);
                        holder.getView(R.id.view_home_list).setVisibility(View.VISIBLE);
                    } else {
                        holder.getView(R.id.llyt_answer_person).setVisibility(View.GONE);
                        holder.getView(R.id.line).setVisibility(View.GONE);
                        holder.getView(R.id.view_home_list).setVisibility(View.GONE);
                        if (!questionInfo.isAnswered()) {
                            holder.getView(R.id.v_cricle).setVisibility(View.GONE);
                            holder.setText(R.id.tv_answer_count, "待回复");
                            ((TextView) holder.getView(R.id.tv_answer_count))
                                .setTextColor(Color.parseColor("#078CF1"));
                        } else {
                            if (questionInfo.isHasUnreadAnswers()) {
                                holder.getView(R.id.v_cricle).setVisibility(View.VISIBLE);
                                holder.setText(R.id.tv_answer_count, "已回复");
                                ((TextView) holder.getView(R.id.tv_answer_count))
                                    .setTextColor(Color.parseColor("#F6611D"));
                            } else {
                                holder.getView(R.id.v_cricle).setVisibility(View.GONE);
                                holder.setText(R.id.tv_answer_count, "已回复");
                                ((TextView) holder.getView(R.id.tv_answer_count))
                                    .setTextColor(Color.parseColor("#949BA1"));
                            }
                        }
                    }
                }
                if (questionInfo.getFirstAnswerer() != null) {
                    holder.setText(R.id.tv_answer_name, questionInfo.getFirstAnswerer().getName());
                    holder.setText(R.id.tv_title, questionInfo.getFirstAnswerer().getTitle());
                }
                holder.setText(R.id.tv_qa, questionInfo.getContent());
                holder.setText(R.id.tv_time, questionInfo.getCreateTimeText());
                holder.setCircleImageUrl(R.id.iv_school, questionInfo.getSchoolLogo(), true);
                holder.setText(R.id.tv_school_name, questionInfo.getSchoolName());
                if (TextUtils.isEmpty(questionInfo.getTargetCountryName())
                    && TextUtils.isEmpty(questionInfo.getTargetDegreeName())) {
                    holder.getView(R.id.ll_tag).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.ll_tag).setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(questionInfo.getTargetCountryName())) {
                        holder.getView(R.id.tv_country).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.tv_country).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_country, questionInfo.getTargetCountryName());
                    }
                    if (TextUtils.isEmpty(questionInfo.getTargetDegreeName())) {
                        holder.getView(R.id.tv_degree).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.tv_degree).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_degree, questionInfo.getTargetDegreeName());
                    }
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rmrvQa.setAdapter(loadMoreWrapper);
        rmrvQa.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    rmrvQa.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                getQa(ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                UApp.actionEvent(mActivity, "19_A_question_detail_cell");
                QuestionInfo info = questionInfoList.get(position);
                Intent toMoreDetails = new Intent(mActivity, QaDetailActivity.class);
                toMoreDetails.putExtra("id", info.getId() + "");
                startActivity(toMoreDetails);
                info = null;
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void setupHolderView(ViewHolder holder, int visibility) {
        holder.getView(R.id.llyt_qa_person).setVisibility(visibility);
        holder.getView(R.id.tv_see).setVisibility(visibility);
        holder.getView(R.id.line).setVisibility(visibility);
        holder.getView(R.id.view_home_list).setVisibility(visibility);
        holder.getView(R.id.llyt_answer_person).setVisibility(visibility);
        holder = null;
    }

    private void getQa(int pullAction) {
        if ("list".equals(data_tag)) {
            presenter.getQuestions(ParameterUtils.NETWORK_ELSE_CACHED, true, mPage, pullAction);
        } else if ("my".equals(data_tag)) {
            presenter.getMyQuestions(ParameterUtils.NETWORK_ELSE_CACHED, mPage, pullAction);
        } else if ("school".equals(data_tag)) {
            String schoolId = getArguments().getString("schoolId");
            presenter.getSchoolQa(schoolId, mPage, pullAction);
        } else if ("teacher".equals(data_tag)) {
            String teacherId = getArguments().getString("teacherId");
            presenter.getTeacherQa(teacherId, mPage, pullAction);
        } else if ("recommend".equals(data_tag)) {
            presenter.getRecommendQa(ParameterUtils.NETWORK_ELSE_CACHED, mPage, pullAction);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    public void setPresenter(QaListContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            if (!"list".equals(data_tag)) {
                if (ParameterUtils.RESPONSE_CODE_NOLOGIN.equals(errCode)) {
                    DialogCreator.createLoginDialog(mActivity);
                }
            }
            swipeRefreshLayout.setRefreshing(false);
            rmrvQa.loadComplete(true);
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void getQuestionsSuccess(List<QuestionInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(mActivity, emptyView, data_tag);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rmrvQa.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                questionInfoList.clear();
                questionInfoList.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rmrvQa.loadComplete(false);
                } else {
                    rmrvQa.loadComplete(true);
                    questionInfoList.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        data = null;
    }

    @Override
    public void getMyQuestionSuccess(List<QuestionInfo> data, boolean hasUnreadQuestions,
                                     int request_state) {
        if (mActivity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mActivity;
            mainActivity.showQaRed(hasUnreadQuestions ? View.VISIBLE : View.GONE);
            QaFragment qaFragment = (QaFragment) getParentFragment();
            if (qaFragment != null) {
                qaFragment.showMyQaRedIsVisible(hasUnreadQuestions ? View.VISIBLE : View.GONE);
            }
        }
        if (presenter != null) {
            presenter.setEmptyView(mActivity, emptyView, data_tag);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rmrvQa.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                questionInfoList.clear();
                questionInfoList.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rmrvQa.loadComplete(false);
                } else {
                    rmrvQa.loadComplete(true);
                    questionInfoList.addAll(data);
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
        rmrvQa.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    public void postQuestion() {
        startActivity(new Intent(mActivity, AddQuestionActivity.class));
    }

}

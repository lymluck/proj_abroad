package com.smartstudy.xxd.ui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.HidingScrollListener;
import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.ui.activity.CommonSearchActivity;
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
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
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.QaListContract;
import com.smartstudy.xxd.mvp.presenter.QuestionsPresenter;
import com.smartstudy.xxd.ui.activity.AddQuestionActivity;
import com.smartstudy.xxd.ui.activity.QaDetailActivity;

import java.util.ArrayList;
import java.util.List;


public class QaFragment extends UIFragment implements QaListContract.View {

    private LoadMoreRecyclerView rclv_qa;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonAdapter<QuestionInfo> mAdapter;
    private LoadMoreWrapper<QuestionInfo> loadMoreWrapper;
    private EmptyWrapper<SchoolInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private View searchView;

    private QaListContract.Presenter presenter;
    private List<QuestionInfo> questionInfoList;
    private int mPage = 1;
    private String data_tag;
    private boolean isFirst = true;
    private WeakHandler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isPrepared = true;
        StatisticUtils.actionEvent(mActivity, "19_B_question_list");
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.fragment_qa, null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter = null;
        }
        if (rclv_qa != null) {
            rclv_qa.removeAllViews();
            rclv_qa = null;
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
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, rclv_qa, false);
        presenter.showLoading(mActivity, emptyView);
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
    protected void initView(View rootView) {
        RelativeLayout top_qa = (RelativeLayout) rootView.findViewById(R.id.top_qa);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) top_qa.getLayoutParams();
            params.height = params.height + ScreenUtils.getStatusHeight(mActivity);
            top_qa.setLayoutParams(params);
            top_qa.setPadding(0, ScreenUtils.getStatusHeight(mActivity), 0, 0);
        }
        searchView = rootView.findViewById(R.id.searchView);
        rootView.findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        TextView topdefault_rightmenu = (TextView) rootView.findViewById(R.id.topdefault_rightmenu);
        topdefault_rightmenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_blue, 0, 0, 0);
        topdefault_rightmenu.setVisibility(View.VISIBLE);
        topdefault_rightmenu.setOnClickListener(this);
        ImageView topdefault_leftbutton = (ImageView) rootView.findViewById(R.id.topdefault_leftbutton);
        topdefault_leftbutton.setOnClickListener(this);
        TextView topdefault_centertitle = (TextView) rootView.findViewById(R.id.topdefault_centertitle);
        rclv_qa = (LoadMoreRecyclerView) rootView.findViewById(R.id.rclv_qa);
        data_tag = getArguments().getString("data_flag");
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.srlt_qa);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                getQa(ParameterUtils.PULL_DOWN);
            }
        });
        searchView.setOnClickListener(this);
        rclv_qa.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_qa.setLayoutManager(mLayoutManager);
        if ("list".equals(data_tag)) {
            topdefault_centertitle.setText(mActivity.getString(R.string.qa));
            if (!getArguments().getBoolean("showBackBtn", false)) {
                topdefault_leftbutton.setVisibility(View.GONE);
            }
            rclv_qa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                    .size(DensityUtils.dip2px(10f)).colorResId(R.color.search_bg).build());
        } else if ("my".equals(data_tag)) {
            rclv_qa.setPadding(0, 0, 0, 0);
            topdefault_centertitle.setText(mActivity.getString(R.string.my_qa));
            rclv_qa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                    .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        } else if ("school".equals(data_tag)) {
            rclv_qa.setPadding(0, 0, 0, 0);
            topdefault_centertitle.setText("留学问答");
            rclv_qa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                    .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.horizontal_line_color).build());
        } else if ("teacher".equals(data_tag)) {
            rclv_qa.setPadding(0, 0, 0, 0);
            String title = getArguments().getString("nickName");
            topdefault_centertitle.setText(title + "的回答");
            topdefault_rightmenu.setVisibility(View.GONE);
            rclv_qa.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mActivity)
                    .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.px2dip(16), 0).colorResId(R.color.horizontal_line_color).build());
        }
        initAdapter();
        initEvent();
        new QuestionsPresenter(this);
    }

    private void initEvent() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.MSG_WHAT_REFRESH:
                        rclv_qa.scrollBy(0, searchView.getHeight());
                        searchView.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        if ("list".equals(data_tag)) {
            rclv_qa.addOnScrollListener(new HidingScrollListener() {
                @Override
                public void onHide() {
                    searchView.animate()
                            .translationY(-ScreenUtils.getScreenHeight())
                            .setDuration(800)
                            .setInterpolator(new AccelerateInterpolator(2))
                            .start();
                    searchView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onShow() {
                    searchView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).setDuration(800).start();
                    searchView.setVisibility(View.VISIBLE);
                }
            });
        }
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
                if ("list".equals(data_tag) || "teacher".equals(data_tag)) {
                    setupHolderView(holder, View.VISIBLE);
                    if (questionInfo.getFirstAnswerer() != null) {
                        holder.setText(R.id.tv_answer_name, questionInfo.getFirstAnswerer().getName());
                        holder.setText(R.id.tv_title, questionInfo.getFirstAnswerer().getTitle());
                    }
                    if ("teacher".equals(data_tag)) {
                        holder.getView(R.id.view_home_list).setVisibility(View.GONE);
                        holder.getView(R.id.llyt_answer_person).setVisibility(View.GONE);
                    }


                } else {
                    setupHolderView(holder, View.GONE);
                    if ("school".equals(data_tag)) {
                        holder.getView(R.id.llyt_qa_person).setVisibility(View.VISIBLE);
                        holder.getView(R.id.tv_see).setVisibility(View.VISIBLE);
                        holder.getView(R.id.tv_my_time).setVisibility(View.GONE);
                    }else{
                        holder.getView(R.id.tv_my_time).setVisibility(View.VISIBLE);
                    }

                }
                //缩进一个空格
                holder.setText(R.id.tv_ansewer_count, questionInfo.getAnswerCount() + " 回答");
                holder.setText(R.id.tv_my_time, questionInfo.getCreateTimeText());
                holder.setText(R.id.tv_qa, questionInfo.getContent());
                holder.setText(R.id.tv_time, questionInfo.getCreateTimeText());
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        rclv_qa.setAdapter(loadMoreWrapper);
        rclv_qa.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (swipeRefreshLayout.isRefreshing()) {
                    rclv_qa.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                getQa(ParameterUtils.PULL_UP);
            }
        });
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                StatisticUtils.actionEvent(mActivity, "19_A_question_detail_cell");
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
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                mActivity.finish();
                break;
            case R.id.topdefault_rightmenu:
                String ticket = (String) SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL);
                if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
                    StatisticUtils.actionEvent(mActivity, "19_A_post_btn");
                    startActivity(new Intent(mActivity, AddQuestionActivity.class));
                } else {
                    DialogCreator.createLoginDialog(mActivity);
                }
                break;
            case R.id.searchView:
                StatisticUtils.actionEvent(mActivity, "19_A_search_btn");
                mHandler.sendEmptyMessageAtTime(ParameterUtils.MSG_WHAT_REFRESH, 600);
                Intent toSearch = new Intent(mActivity, CommonSearchActivity.class);
                toSearch.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.QA_FLAG);
                Pair<View, String> searchTop = Pair.create(searchView, "search_top");
                ActivityOptionsCompat compat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(mActivity, searchTop);
                searchTop = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(toSearch, compat.toBundle());
                    compat = null;
                } else {
                    startActivity(toSearch);
                }
                break;
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
            rclv_qa.loadComplete(true);
            ToastUtils.showToast(mActivity, message);
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
                    rclv_qa.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                questionInfoList.clear();
                questionInfoList.addAll(data);
                swipeRefreshLayout.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
                if ("list".equals(data_tag)) {
                    if (searchView.getVisibility() == View.INVISIBLE && isFirst) {
                        rclv_qa.scrollBy(0, searchView.getHeight());
                    }
                    //判断是否可滑动， -1 表示 向上， 1 表示向下
                    if (!rclv_qa.canScrollVertically(-1)) {
                        searchView.setVisibility(View.VISIBLE);
                    }
                    isFirst = false;
                }
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rclv_qa.loadComplete(false);
                } else {
                    rclv_qa.loadComplete(true);
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
        rclv_qa.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }
}

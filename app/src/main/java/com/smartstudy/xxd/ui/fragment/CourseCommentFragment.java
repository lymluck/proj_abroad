package com.smartstudy.xxd.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.entity.CommentInfo;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.RatingBar;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.CourseCommentContract;
import com.smartstudy.xxd.mvp.presenter.CourseCommentPresenter;

import java.util.ArrayList;
import java.util.List;

public class CourseCommentFragment extends BaseFragment implements CourseCommentContract.View {

    private LoadMoreRecyclerView rclv_comment;
    private SwipeRefreshLayout srlt_comment;
    private CommonAdapter<CommentInfo> mAdapter;
    private LoadMoreWrapper<CommentInfo> loadMoreWrapper;
    private HeaderAndFooterWrapper<CommentInfo> headerAndFooterWrapper;
    private EmptyWrapper<CommentInfo> emptyWrapper;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private LinearLayout headerView;
    private Dialog dialog;
    private RelativeLayout rlyt_comment;
    private TextView tv_course_count;

    private CourseCommentContract.Presenter presenter;
    private List<CommentInfo> commentInfos;
    private int mPage = 1;
    private String courseId;
    private boolean showDialog;
    private boolean isPlayed;

    @Override
    public void onDestroy() {
        super.onDestroy();
        SPCacheUtils.remove("playstart");
        SPCacheUtils.remove("canFreshComment");
    }

    @Override
    public void onDetach() {
        if (rclv_comment != null) {
            rclv_comment.removeAllViews();
            rclv_comment = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (commentInfos != null) {
            commentInfos.clear();
            commentInfos = null;
        }
        if (presenter != null) {
            presenter = null;
        }
        super.onDetach();
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        presenter.getComments(courseId, mPage, ParameterUtils.PULL_DOWN);
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        if (srlt_comment != null) {
            srlt_comment.setRefreshing(false);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_course_comment;
    }

    @Override
    protected void initView() {
        UApp.actionEvent(mActivity, "16_A_evaluation_btn");
        Bundle bundle = getArguments();
        courseId = bundle.getString("courseId");
        headerView = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.header_course_comment, null);
        headerView.setVisibility(View.GONE);
        rlyt_comment = (RelativeLayout) headerView.findViewById(R.id.rlyt_comment);
        tv_course_count = (TextView) headerView.findViewById(R.id.tv_course_count);
        srlt_comment = (SwipeRefreshLayout) rootView.findViewById(R.id.srlt_comment);
        srlt_comment.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        srlt_comment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        rclv_comment = (LoadMoreRecyclerView) rootView.findViewById(R.id.rclv_comment);
        rclv_comment.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(mActivity);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_comment.setLayoutManager(mLayoutManager);
        rclv_comment.setItemAnimator(new DefaultItemAnimator());
        initAdapter();
        initEvent();
        new CourseCommentPresenter(this);
        emptyView = mActivity.getLayoutInflater().inflate(R.layout.layout_empty, rclv_comment, false);
        presenter.showLoading(mActivity, emptyView);
    }

    private void initAdapter() {
        commentInfos = new ArrayList<>();
        mAdapter = new CommonAdapter<CommentInfo>(mActivity, R.layout.item_comments_list, commentInfos) {

            @Override
            protected void convert(ViewHolder holder, CommentInfo commentInfo, int position) {
                if (commentInfo != null) {
                    //有头部，列表从1开始
                    if (position == 1) {
                        holder.getView(R.id.view_comment).setVisibility(View.GONE);
                    } else {
                        holder.getView(R.id.view_comment).setVisibility(View.VISIBLE);
                    }
                    holder.setPersonImageUrl(R.id.iv_user_avatar, commentInfo.getUser().getAvatar(), true);
                    holder.setText(R.id.tv_user, commentInfo.getUser().getName());
                    holder.setText(R.id.tv_time, commentInfo.getCreateTime());
                    holder.setText(R.id.etv_user_intro, commentInfo.getComment());
                    RatingBar ratingBar = holder.getView(R.id.rb_course_rate);
                    ratingBar.setStar(TextUtils.isEmpty(commentInfo.getRate()) ? 0f : Float.parseFloat(commentInfo.getRate()));
                }
            }
        };
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        headerAndFooterWrapper = new HeaderAndFooterWrapper<>(emptyWrapper);
        headerAndFooterWrapper.addHeaderView(headerView);
        loadMoreWrapper = new LoadMoreWrapper<>(headerAndFooterWrapper);
        rclv_comment.setAdapter(loadMoreWrapper);
    }

    private void initEvent() {
        rlyt_comment.setOnClickListener(this);
        headerView.findViewById(R.id.view_rating).setOnClickListener(this);
        rclv_comment.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (srlt_comment.isRefreshing()) {
                    rclv_comment.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getComments(courseId, mPage, ParameterUtils.PULL_UP);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlyt_comment:
                handleRating();
                break;
            case R.id.view_rating:
                handleRating();
                break;
            default:
                break;
        }
    }

    private void handleRating() {
        long startTime = (long) SPCacheUtils.get("playstart", 0L);
        if (isPlayed) {
            dialogComment();
        } else {
            if (startTime > 0L) {
                if (System.currentTimeMillis() - startTime < 30 * 1000) {
                    if ((boolean) SPCacheUtils.get("canFreshComment", false)) {
                        showDialog = true;
                        refresh();
                        SPCacheUtils.put("canFreshComment", false);
                    } else {
                        showTip(null, "观看视频后才能进行评价！");
                    }
                } else {
                    dialogComment();
                }
            } else {
                showTip(null, "观看视频后才能进行评价！");
            }
        }
    }

    public void dialogComment() {
        if (mActivity != null) {
            if (dialog != null && dialog.isShowing()) {
                return;
            }
            if (mActivity.isFinishing()) {
                return;
            }
            dialog = DialogCreator.createCommentDialog(mActivity, "COMMENT", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String et_msg = v.getTag().toString();
                    presenter.CommentCourse(courseId, ((Button) v).getHint().toString(), et_msg);
                    KeyBoardUtils.closeKeybord((EditText) dialog.getCurrentFocus(), mActivity);
                }
            });
        }
    }

    @Override
    public void setPresenter(CourseCommentContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            if (dialog != null) {
                dialog.dismiss();
            }
            srlt_comment.setRefreshing(false);
            rclv_comment.loadComplete(true);
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void showComments(List<CommentInfo> data, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            headerView.setVisibility(View.VISIBLE);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    presenter.hideEmptyView(emptyView);
                    rclv_comment.loadComplete(true);
                    mLayoutManager.setScrollEnabled(false);
                }
                commentInfos.clear();
                commentInfos.addAll(data);
                srlt_comment.setRefreshing(false);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rclv_comment.loadComplete(false);
                } else {
                    rclv_comment.loadComplete(true);
                    commentInfos.addAll(data);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void showCommentCount(String count) {
        if (isAdded()) {
            tv_course_count.setText(String.format(getString(R.string.comment_count), count));
        }
    }

    @Override
    public void isComment(boolean isComment, boolean isPlayed) {
        this.isPlayed = isPlayed;
        if (!TextUtils.isEmpty((String) SPCacheUtils.get("ticket", ""))) {
            if (isComment) {
                rlyt_comment.setVisibility(View.GONE);
            } else {
                rlyt_comment.setVisibility(View.VISIBLE);
                if (isPlayed) {
                    if (showDialog) {
                        showDialog = false;
                        handleRating();
                    }
                }
            }
        } else {
            rlyt_comment.setVisibility(View.VISIBLE);
            SPCacheUtils.put("course_from", "comment");
        }
    }

    @Override
    public void CommentSuccess() {
        //dialog消失，刷新评论
        if (dialog != null) {
            dialog.dismiss();
        }
        mPage = 1;
        if (presenter != null) {
            presenter.getComments(courseId, mPage, ParameterUtils.PULL_DOWN);
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        rclv_comment.loadComplete(true);
        mLayoutManager.setScrollEnabled(false);
    }

    public void refresh() {
        mPage = 1;
        presenter.getComments(courseId, mPage, ParameterUtils.PULL_DOWN);
    }

    public Dialog getDialog() {
        return dialog;
    }

}

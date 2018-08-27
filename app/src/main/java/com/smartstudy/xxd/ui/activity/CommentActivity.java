package com.smartstudy.xxd.ui.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.ColumnCommentInfo;
import com.smartstudy.xxd.mvp.contract.ColumnCommentContract;
import com.smartstudy.xxd.mvp.presenter.ColumnCommentPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.PAGES_COUNT;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe 专栏列表页面
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class CommentActivity extends BaseActivity implements ColumnCommentContract.View {

    private LoadMoreRecyclerView lmrvColumn;
    private CommonAdapter<ColumnCommentInfo> mAdapter;
    private LoadMoreWrapper<ColumnCommentInfo> loadMoreWrapper;
    private EmptyWrapper<ColumnCommentInfo> emptyWrapper;
    private NoScrollLinearLayoutManager nsllManager;
    private View emptyView;
    private List<ColumnCommentInfo> commentInfoList;
    private ColumnCommentContract.Presenter presenter;
    private int mPage = PAGES_COUNT;
    private SwipeRefreshLayout srtComment;
    private String newsId;
    private EditText etContent;
    private TextView tvPost;
    private String commentId;
    private int diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        if (lmrvColumn != null) {
            lmrvColumn.removeAllViews();
            lmrvColumn = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (commentInfoList != null) {
            commentInfoList.clear();
            commentInfoList = null;
        }
        if (nsllManager != null) {
            nsllManager = null;
        }

        if (loadMoreWrapper != null) {
            loadMoreWrapper = null;
        }
    }

    @Override
    protected void initViewAndData() {
        View decorView = getWindow().getDecorView();
        // 此处的控件ID可以使用界面当中的指定的任意控件
        View contentView = findViewById(R.id.ll_root);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("评论");
        lmrvColumn = findViewById(R.id.lmrv_column);
        lmrvColumn.setHasFixedSize(true);
        nsllManager = new NoScrollLinearLayoutManager(this);
        nsllManager.setScrollEnabled(true);
        nsllManager.setOrientation(LinearLayoutManager.VERTICAL);
        lmrvColumn.setLayoutManager(nsllManager);
        newsId = getIntent().getStringExtra("id");
        this.srtComment = findViewById(R.id.srt_comment);
        this.etContent = findViewById(R.id.et_content);
        this.tvPost = findViewById(R.id.tv_post);
        tvPost.setEnabled(false);
        srtComment.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        //刷新数据
        srtComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                presenter.getColumnComments(newsId, mPage, ParameterUtils.PULL_DOWN);
            }
        });
        new ColumnCommentPresenter(this);
        initAdapter();
        emptyView = mInflater.inflate(R.layout.layout_empty, lmrvColumn, false);
        presenter.showLoading(this, emptyView);
        presenter.getColumnComments(newsId, mPage, ParameterUtils.PULL_DOWN);
    }


    private void initAdapter() {
        commentInfoList = new ArrayList<>();
        mAdapter = new CommonAdapter<ColumnCommentInfo>(this, R.layout.item_column_comment_list,
            commentInfoList, mInflater) {
            @Override
            protected void convert(ViewHolder holder, ColumnCommentInfo info, int position) {
                if (info != null) {
                    holder.setPersonImageUrl(R.id.iv_user_avatar, info.getUser().getAvatar(), true);
                    holder.setText(R.id.tv_user_name, info.getUser().getName());
                    holder.setText(R.id.tv_time, info.getCreateTimetext());
                    if (info.getPreviousComment() != null) {
                        holder.getView(R.id.tv_reply_content).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_reply_content, Html.fromHtml("<font color=\"#078CF1\">"
                            + info.getPreviousComment().getUser().getName() + " </font>"
                            + info.getPreviousComment().getContent()));
                    } else {
                        holder.getView(R.id.tv_reply_content).setVisibility(View.GONE);
                    }
                    holder.setText(R.id.tv_content, info.getContent());
                }
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (position < commentInfoList.size()) {
                    ColumnCommentInfo info = commentInfoList.get(position);
                    if (info != null) {
                        if (diff != 0) {
                            // 软件盘需要隐藏
                            hideKeybord();
                        } else {
                            // 软件盘需要显示
                            showKeybord(info);
                        }
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        loadMoreWrapper = new LoadMoreWrapper<>(emptyWrapper);
        lmrvColumn.setAdapter(loadMoreWrapper);
    }

    @Override
    public void initEvent() {
        lmrvColumn.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                if (srtComment.isRefreshing()) {
                    lmrvColumn.loadComplete(true);
                    return;
                }
                mPage = mPage + 1;
                presenter.getColumnComments(newsId, mPage, ParameterUtils.PULL_UP);
            }
        });
        etContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().replaceAll(" ", ""))) {
                    tvPost.setBackgroundResource(R.drawable.bg_qa_posted_blue);
                    tvPost.setTextColor(getResources().getColor(R.color.white));
                    tvPost.setEnabled(true);
                } else {
                    tvPost.setBackgroundResource(R.drawable.bg_qa_posted_grey);
                    tvPost.setTextColor(getResources().getColor(R.color.qa_post_clor));
                    tvPost.setEnabled(false);
                }
            }
        });
        tvPost.setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    private void showKeybord(ColumnCommentInfo info) {
        etContent.requestFocus();
        KeyBoardUtils.openKeybord(etContent, CommentActivity.this);
        etContent.setHint("回复 " + info.getUser().getName());
        commentId = String.valueOf(info.getId());
    }

    private void hideKeybord() {
        etContent.clearFocus();
        KeyBoardUtils.closeKeybord(etContent, CommentActivity.this);
        etContent.setHint("请输入你的评论");
        commentId = null;
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.tv_post:
                String content = etContent.getText().toString();
                presenter.comment(newsId, etContent.getText().toString(), commentId);
                tvPost.setEnabled(false);
                break;
            default:
                break;
        }
    }

    //监听返回键(有软键盘的情况下想直接返回，需要拦截KeyEvent.ACTION_UP事件)
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            finish();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void setPresenter(ColumnCommentContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            tvPost.setEnabled(true);
            srtComment.setRefreshing(false);
            lmrvColumn.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
            ToastUtils.showToast(message);
        }
    }

    @Override
    public void showList(List<ColumnCommentInfo> datas, int request_state) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            nsllManager.setScrollEnabled(true);
            int len = datas.size();
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    lmrvColumn.loadComplete(true);
                    nsllManager.setScrollEnabled(false);
                }
                srtComment.setRefreshing(false);
                this.commentInfoList.clear();
                this.commentInfoList.addAll(datas);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    lmrvColumn.loadComplete(false);
                } else {
                    lmrvColumn.loadComplete(true);
                    this.commentInfoList.addAll(datas);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        datas = null;
    }

    @Override
    public void commentSuccess() {
        etContent.clearFocus();
        etContent.setText("");
        KeyBoardUtils.closeKeybord(etContent, CommentActivity.this);
        etContent.setHint("请输入你的评论");
        commentId = null;
        mPage = 1;
        presenter.getColumnComments(newsId, mPage, ParameterUtils.PULL_DOWN);
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        loadMoreWrapper.notifyDataSetChanged();
        lmrvColumn.loadComplete(true);
        nsllManager.setScrollEnabled(false);
        view = null;
    }

    @Override
    public void reload() {
        presenter.showLoading(this, emptyView);
        presenter.getColumnComments(newsId, mPage, ParameterUtils.PULL_DOWN);
    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }
}

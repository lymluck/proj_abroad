package com.smartstudy.xxd.ui.activity;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.MultiItemTypeAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.LoadMoreWrapper;
import com.smartstudy.commonlib.ui.customview.LoadMoreRecyclerView;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.TextBrHandle;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CommentChoiceSchoolInfo;
import com.smartstudy.xxd.entity.OtherStudentChoiceDetailInfo;
import com.smartstudy.xxd.mvp.contract.ChoiceShoolContract;
import com.smartstudy.xxd.mvp.presenter.ChoiceShoolPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/1
 * @describe 个人选校详情界面
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChoiceSchoolPersonActivity extends BaseActivity implements ChoiceShoolContract.View {
    private RecyclerView recyclerView;
    private NoScrollLinearLayoutManager mLayoutManager;
    private NoScrollLinearLayoutManager layoutManager;
    private LoadMoreRecyclerView rclvComment;
    private View headView;
    private HeaderAndFooterWrapper mHeader;
    private View footView;
    private TextView tvPost;
    private EditText etAnswer;
    private TextView tvAddGood;
    private TextView tvNoComment;
    private ChoiceShoolContract.Presenter presenter;
    private OtherStudentChoiceDetailInfo otherStudentChoiceDetailInfo;
    private ArrayList<OtherStudentChoiceDetailInfo.SelectedSchools> schoolDataList;
    private CommonAdapter<OtherStudentChoiceDetailInfo.SelectedSchools> mAdapter;
    private CommonAdapter<CommentChoiceSchoolInfo> footAdapter;
    private List<CommentChoiceSchoolInfo> commentChoiceSchoolInfos;
    private int mPage = 1;
    private LoadMoreWrapper<CommentChoiceSchoolInfo> loadMoreWrapper;
    //是否点赞过，默认为没有
    private boolean isLike = false;
    private String toUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_student_school_info);
    }

    @Override
    protected void initViewAndData() {
        View decorView = getWindow().getDecorView();
        // 此处的控件ID可以使用界面当中的指定的任意控件
        View contentView = findViewById(R.id.ll_student);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));
        otherStudentChoiceDetailInfo = (OtherStudentChoiceDetailInfo) getIntent().getSerializableExtra("person_detail");
        isLike = otherStudentChoiceDetailInfo.isLiked();
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(otherStudentChoiceDetailInfo.getUser().getName() + "的选校");
        recyclerView = findViewById(R.id.rv_school);
        tvPost = findViewById(R.id.tv_post);
        etAnswer = findViewById(R.id.et_answer);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        initAdapter();
        initHeaderAndFooter();
        recyclerView.setFocusable(false);
        new ChoiceShoolPresenter(this);
        presenter.getComment(otherStudentChoiceDetailInfo.getUser().getId(), mPage, ParameterUtils.PULL_DOWN);
    }


    private void initAdapter() {
        schoolDataList = new ArrayList<>();
        mAdapter = new CommonAdapter<OtherStudentChoiceDetailInfo.SelectedSchools>(this, R.layout.item_other_studetn_school, schoolDataList) {
            @Override
            protected void convert(ViewHolder holder, OtherStudentChoiceDetailInfo.SelectedSchools schoolData, int position) {
                holder.setText(R.id.tv_name, schoolData.getChineseName());
                if (TextUtils.isEmpty(schoolData.getWorldRank())) {
                    holder.setText(R.id.tv_rank, "N/A");
                } else {
                    holder.setText(R.id.tv_rank, schoolData.getWorldRank());
                }
                if (schoolData.getMatchTypeId().equals("MS_MATCH_TYPE_TOP")) {
                    holder.setText(R.id.tv_status, "冲");
                    holder.setBackgroundRes(R.id.tv_status, R.drawable.bg_oval_red_size);
                } else if (schoolData.getMatchTypeId().equals("MS_MATCH_TYPE_MIDDLE")) {
                    holder.setText(R.id.tv_status, "核");
                    holder.setBackgroundRes(R.id.tv_status, R.drawable.bg_oval_blue_size);
                } else {
                    holder.setText(R.id.tv_status, "保");
                    holder.setBackgroundRes(R.id.tv_status, R.drawable.bg_oval_green_size);
                }
            }
        };
    }

    private void initHeaderAndFooter() {
        headView = mInflater.inflate(R.layout.layout_my_choose_school, null, false);
        ImageView ivAvatar = headView.findViewById(R.id.iv_avatar);
        TextView tvName = headView.findViewById(R.id.tv_name);
        TextView tvTargetCountry = headView.findViewById(R.id.tv_target_country);
        TextView tvTime = headView.findViewById(R.id.tv_time);
        TextView tvTargetDegree = headView.findViewById(R.id.tv_target_degree);
        TextView tvSchoolCount = headView.findViewById(R.id.tv_school_count);
        footView = mInflater.inflate(R.layout.layout_comment_foot, null, false);
        rclvComment = footView.findViewById(R.id.lmrv_comment);
        tvNoComment = footView.findViewById(R.id.tv_no_comment);
        rclvComment.setHasFixedSize(true);
        layoutManager = new NoScrollLinearLayoutManager(this);
        layoutManager.setScrollEnabled(true);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvComment.setLayoutManager(layoutManager);
        rclvComment.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
            .size(DensityUtils.dip2px(0.5f)).margin(DensityUtils.dip2px(64f), 0).colorResId(R.color.horizontal_line_color).build());
        tvAddGood = footView.findViewById(R.id.tv_add_good);
        if (otherStudentChoiceDetailInfo.isLiked()) {
            Drawable img = getResources().getDrawable(R.drawable.goodpress);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            tvAddGood.setTextColor(Color.parseColor("#078CF1"));
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tvAddGood.setCompoundDrawables(img, null, null, null);
            tvAddGood.setBackgroundResource(R.drawable.bg_add_good_choose_school_blue);

        } else {
            Drawable img = getResources().getDrawable(R.drawable.ic_add_good_gray);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            tvAddGood.setTextColor(Color.parseColor("#949BA1"));
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            tvAddGood.setCompoundDrawables(img, null, null, null);
            tvAddGood.setBackgroundResource(R.drawable.bg_add_good_choose_school);
        }
        tvAddGood.setText(otherStudentChoiceDetailInfo.getLikesCount() + "");
        initFootAdapter();
        mHeader = new HeaderAndFooterWrapper(mAdapter);
        mHeader.addHeaderView(headView);
        mHeader.addFootView(footView);
        recyclerView.setAdapter(mHeader);

        if (otherStudentChoiceDetailInfo != null) {
            tvSchoolCount.setText(String.format(getString(R.string.school_count), otherStudentChoiceDetailInfo.getSelectedSchoolsCount()));
            if (otherStudentChoiceDetailInfo.getSelectedSchools() != null) {
                schoolDataList.clear();
                schoolDataList.addAll(otherStudentChoiceDetailInfo.getSelectedSchools());
                mHeader.notifyDataSetChanged();
            }
            if (otherStudentChoiceDetailInfo.getUser() != null) {
                DisplayImageUtils.displayPersonImage(this, otherStudentChoiceDetailInfo.getUser().getAvatar(), ivAvatar);
                tvName.setText(otherStudentChoiceDetailInfo.getUser().getName());
                if (!TextUtils.isEmpty(otherStudentChoiceDetailInfo.getUser().getTargetCountryName())) {
                    tvTargetCountry.setText(otherStudentChoiceDetailInfo.getUser().getTargetCountryName());
                } else {
                    tvTargetCountry.setText("-");
                }
                if (!TextUtils.isEmpty(otherStudentChoiceDetailInfo.getUser().getAdmissionYear())) {
                    tvTime.setText(otherStudentChoiceDetailInfo.getUser().getAdmissionYear());
                } else {
                    tvTime.setText("-");
                }
                if (!TextUtils.isEmpty(otherStudentChoiceDetailInfo.getUser().getTargetDegreeName())) {
                    tvTargetDegree.setText(otherStudentChoiceDetailInfo.getUser().getTargetDegreeName());
                } else {
                    tvTargetDegree.setText("-");
                }

            }
        }
    }


    private void initFootAdapter() {
        commentChoiceSchoolInfos = new ArrayList<>();
        footAdapter = new CommonAdapter<CommentChoiceSchoolInfo>(this, R.layout.item_comment_choice_school, commentChoiceSchoolInfos) {
            @Override
            protected void convert(ViewHolder holder, CommentChoiceSchoolInfo commentChoiceSchoolInfo, int position) {
                holder.setPersonImageUrl(R.id.iv_avatar, commentChoiceSchoolInfo.getAuthor().getAvatar(), true);
                holder.setText(R.id.tv_name, commentChoiceSchoolInfo.getAuthor().getName());
                if (!TextUtils.isEmpty(commentChoiceSchoolInfo.getCreateTime()) && commentChoiceSchoolInfo.getCreateTime().length() > 10) {
                    holder.setText(R.id.tv_time, commentChoiceSchoolInfo.getCreateTime().substring(0, 10));
                }
                String comment = "";
                if (commentChoiceSchoolInfo.getToUser() != null) {
                    comment = "<font color='#078CF1'>回复 @" + commentChoiceSchoolInfo.getToUser().getName() + "</font>:" + commentChoiceSchoolInfo.getContent();
                    holder.setText(R.id.tv_comment, Html.fromHtml(TextBrHandle.parseContent(comment)));
                } else {
                    comment = commentChoiceSchoolInfo.getContent();
                    holder.setText(R.id.tv_comment, comment);
                }

            }
        };

        footAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (commentChoiceSchoolInfos.get(position).getAuthor().getName().length() > 17) {
                    etAnswer.setHint("回复 @" + commentChoiceSchoolInfos.get(position).getAuthor().getName().substring(0, 14) + "...");
                } else {
                    etAnswer.setHint("回复 @" + commentChoiceSchoolInfos.get(position).getAuthor().getName());
                }
                toUserId = commentChoiceSchoolInfos.get(position).getAuthor().getId();
                etAnswer.setText("");
                etAnswer.requestFocus();
                recyclerView.smoothScrollToPosition(position + schoolDataList.size() + 2);
                KeyBoardUtils.openKeybord(etAnswer, ChoiceSchoolPersonActivity.this);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        loadMoreWrapper = new LoadMoreWrapper<>(footAdapter);
        rclvComment.setAdapter(loadMoreWrapper);
    }

    @Override
    public void initEvent() {
        tvPost.setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        rclvComment.SetOnLoadMoreLister(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void OnLoad() {
                mPage = mPage + 1;
                presenter.getComment(otherStudentChoiceDetailInfo.getUser().getId(), mPage, ParameterUtils.PULL_UP);
            }
        });
        tvAddGood.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.tv_post:
                if (TextUtils.isEmpty(etAnswer.getText().toString())) {
                    ToastUtils.showToast("评论内容不能为空");
                    return;
                }
                if (otherStudentChoiceDetailInfo != null) {
                    presenter.postComment(otherStudentChoiceDetailInfo.getUser().getId(), toUserId, etAnswer.getText().toString());
                }
                break;
            case R.id.topdefault_leftbutton:
                finish();
                break;

            case R.id.tv_add_good:
                if (isLike) {
                    ToastUtils.showToast("不能重复点赞");
                    return;
                }
                if (otherStudentChoiceDetailInfo != null) {
                    presenter.addGood(otherStudentChoiceDetailInfo.getUser().getId());
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void setPresenter(ChoiceShoolContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
        if (presenter != null && rclvComment != null) {
            rclvComment.loadComplete(true);
            loadMoreWrapper.notifyDataSetChanged();
        }
    }


    @Override
    public void postCommentSuccess() {
        KeyBoardUtils.closeKeybord(etAnswer, ChoiceSchoolPersonActivity.this);
        ToastUtils.showToast("提交成功");
        etAnswer.setText("");
        mPage = 1;
        toUserId = "";
        //这里做刷新匹配
        if (otherStudentChoiceDetailInfo != null) {
            presenter.getComment(otherStudentChoiceDetailInfo.getUser().getId(), mPage, ParameterUtils.PULL_DOWN);
        }
    }

    @Override
    public void getCommentSuccess(List<CommentChoiceSchoolInfo> commentChoiceSchoolInfos, int request_state) {
        if (commentChoiceSchoolInfos != null) {
            mLayoutManager.setScrollEnabled(true);
            int len = commentChoiceSchoolInfos.size();
            if (len == 0) {
                tvNoComment.setVisibility(View.VISIBLE);
            } else {
                tvNoComment.setVisibility(View.GONE);
            }
            if (request_state == ParameterUtils.PULL_DOWN) {
                //下拉刷新
                if (len <= 0) {
                    rclvComment.loadComplete(true);
                    layoutManager.setScrollEnabled(false);
                }
                this.commentChoiceSchoolInfos.clear();
                this.commentChoiceSchoolInfos.addAll(commentChoiceSchoolInfos);
                loadMoreWrapper.notifyDataSetChanged();
            } else if (request_state == ParameterUtils.PULL_UP) {
                //上拉加载
                if (len <= 0) {
                    //没有更多内容
                    if (mPage > 1) {
                        mPage = mPage - 1;
                    }
                    rclvComment.loadComplete(false);
                } else {
                    rclvComment.loadComplete(true);
                    this.commentChoiceSchoolInfos.addAll(commentChoiceSchoolInfos);
                    loadMoreWrapper.notifyDataSetChanged();
                }
            }
        }
        commentChoiceSchoolInfos = null;
    }

    @Override
    public void addGoodSuccess() {
        isLike = true;
        tvAddGood.setText((otherStudentChoiceDetailInfo.getLikesCount() + 1) + "");
        Drawable img = getResources().getDrawable(R.drawable.goodpress);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        tvAddGood.setTextColor(Color.parseColor("#078CF1"));
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        tvAddGood.setCompoundDrawables(img, null, null, null);
        tvAddGood.setBackgroundResource(R.drawable.bg_add_good_choose_school_blue);
    }


    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    //打开键盘
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    //关闭键盘
                    if (TextUtils.isEmpty(etAnswer.getText())) {
                        etAnswer.setHint("");
                        toUserId = "";
                    }
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recyclerView != null) {
            recyclerView.removeAllViews();
            recyclerView = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (commentChoiceSchoolInfos != null) {
            commentChoiceSchoolInfos.clear();
            commentChoiceSchoolInfos = null;
        }
        if (schoolDataList != null) {
            schoolDataList.clear();
            schoolDataList = null;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                KeyBoardUtils.closeKeybord(etAnswer, this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null) {
            if (v instanceof EditText || v == rclvComment) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
                if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                    // 点击EditText的事件，忽略它。
                    return false;
                } else {
                    return true;
                }
            }
            return false;
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }
}

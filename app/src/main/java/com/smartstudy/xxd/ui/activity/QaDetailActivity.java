package com.smartstudy.xxd.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.entity.Event;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.TreeRecyclerAdapter;
import com.smartstudy.commonlib.ui.adapter.base.BaseItem;
import com.smartstudy.commonlib.ui.adapter.base.BaseRecyclerAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.TreeRecyclerViewType;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.TreeHeaderAndFootWapper;
import com.smartstudy.commonlib.ui.customview.audio.AudioRecorder;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.Answerer;
import com.smartstudy.xxd.entity.ConsultantsInfo;
import com.smartstudy.xxd.entity.QaCommenInfo;
import com.smartstudy.xxd.entity.QaDetailInfo;
import com.smartstudy.xxd.mvp.contract.QaDetailContract;
import com.smartstudy.xxd.mvp.presenter.QaDetailPresenter;
import com.smartstudy.xxd.ui.adapter.QaDetailOneTreeItemParent;
import com.smartstudy.xxd.ui.adapter.QaDetailTwoTreeItem;
import com.smartstudy.xxd.utils.AppContants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqy on 2017/12/4.
 */
@Route("qaDetail")
public class QaDetailActivity extends BaseActivity implements QaDetailContract.View {

    private String questionId;
    private RecyclerView recyclerView;
    private ImageView ivAsker;
    private TextView tvAskerName;
    private TextView tvQuestion;
    private TextView tvAskerTime;
    private TextView tvPost;
    private QaDetailContract.Presenter presenter;
    private EditText etAnswer;
    private Answerer.Comments comment;
    private LinearLayout rlPost;
    private LinearLayout rlMyPostQuestion;
    private String action = AppContants.SECOND_ACTION;
    private Answerer answerer;
    private TextView answer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View headView;
    private HeaderAndFooterWrapper mHeader;
    private Dialog dialog;

    private TreeRecyclerAdapter qaAdapter;
    private ArrayList<TreeItem> qaTreeItems;
    private LinearLayoutManager mLayoutManager;
    private TreeHeaderAndFootWapper<TreeItem> mHeaderAdapter;
    private BaseItem lastBaseItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_detail_list);
        EventBus.getDefault().register(this);
        UApp.actionEvent(this, "21_B_question_detail");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initViewAndData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        //解除订阅
        EventBus.getDefault().unregister(this);
        if (recyclerView != null) {
            recyclerView.removeAllViews();
            recyclerView = null;
        }
        if (AudioRecorder.getInstance() != null) {
            AudioRecorder.getInstance().setReset();
        }
        if (mHeader != null) {
            mHeader = null;
        }
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        if (lastBaseItem != null) {
            lastBaseItem = null;
        }
        super.onDestroy();
        SPCacheUtils.remove("isMine");
        SPCacheUtils.remove("askName");
        SPCacheUtils.remove("askId");
    }

    @Override
    protected void initViewAndData() {
        TextView titleView = (TextView) findViewById(R.id.topdefault_centertitle);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        Intent data = getIntent();
        titleView.setText(R.string.question_detail);
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        questionId = data.getStringExtra(AppContants.QUESTION_ID);
        if (!TextUtils.isEmpty(questionId) && data.getExtras() != null) {
            questionId = data.getExtras().getString(AppContants.QUESTION_ID);
        }
        new QaDetailPresenter(this);
        recyclerView = findViewById(R.id.rv_qa);
        tvPost = findViewById(R.id.tv_post);
        rlPost = findViewById(R.id.rl_post);
        etAnswer = findViewById(R.id.et_answer);
        rlMyPostQuestion = findViewById(R.id.rl_my_post_question);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlt_qa);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getQaDetails(questionId);
            }
        });
        recyclerView.setFocusable(false);
        initAdapter();
        if (!TextUtils.isEmpty(questionId)) {
            presenter.getQaDetails(questionId);
        }
    }


    @Override
    public void initEvent() {
        etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    tvPost.setBackgroundResource(R.drawable.bg_qa_posted_blue);
                    tvPost.setTextColor(getResources().getColor(R.color.white));
                    tvPost.setOnClickListener(QaDetailActivity.this);
                } else {
                    tvPost.setBackgroundResource(R.drawable.bg_qa_posted_grey);
                    tvPost.setTextColor(getResources().getColor(R.color.qa_post_clor));
                    tvPost.setOnClickListener(null);
                }
            }
        });
        tvPost.setOnClickListener(this);
        rlMyPostQuestion.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.tv_post:
                if (TextUtils.isEmpty(etAnswer.getText().toString().trim())) {
                    ToastUtils.showToast(getString(R.string.submit_question_not_empty));
                    return;
                }
                if (action.equals(AppContants.SECOND_ACTION)) {
                    if (comment != null) {
                        setUnPostClick();
                        presenter.postQuestion(comment.getQuestionId(), comment.getId(), etAnswer.getText().toString());
                    }
                } else {
                    if (answerer != null) {
                        setUnPostClick();
                        presenter.postQuestion(answerer.getQuestionId(), answerer.getId(), etAnswer.getText().toString());
                    }
                }
                break;
            case R.id.rl_my_post_question:
                UApp.actionEvent(this, "19_A_post_btn");
                startActivity(new Intent(this, AddQuestionActivity.class));
                break;
            case R.id.topdefault_leftbutton:
                if (rlPost.getVisibility() == View.VISIBLE) {
                    KeyBoardUtils.closeKeybord(etAnswer, QaDetailActivity.this);
                }
                finish();
                break;
            default:
                break;
        }
    }

    public void setPostClick() {
        tvPost.setClickable(true);
        tvPost.setBackgroundResource(R.drawable.bg_qa_posted_blue);
        tvPost.setTextColor(getResources().getColor(R.color.white));
    }

    public void setUnPostClick() {
        tvPost.setClickable(false);
        tvPost.setBackgroundResource(R.drawable.bg_qa_posted_grey);
        tvPost.setTextColor(getResources().getColor(R.color.qa_post_clor));
    }

    @Override
    public void setPresenter(QaDetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
        setPostClick();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getQaDetails(final QaDetailInfo data) {
        setPostClick();
        swipeRefreshLayout.setRefreshing(false);
        if (data.getAsker() != null) {
            headView.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(data.getTargetCountryName())
                && TextUtils.isEmpty(data.getTargetDegreeName())) {
                headView.findViewById(R.id.ll_tag).setVisibility(View.GONE);
            } else {
                headView.findViewById(R.id.ll_tag).setVisibility(View.VISIBLE);
                TextView tvCountry = headView.findViewById(R.id.tv_country);
                if (TextUtils.isEmpty(data.getTargetCountryName())) {
                    tvCountry.setVisibility(View.GONE);
                } else {
                    tvCountry.setVisibility(View.VISIBLE);
                    tvCountry.setText(data.getTargetCountryName());
                }
                TextView tvDegree = headView.findViewById(R.id.tv_degree);
                if (TextUtils.isEmpty(data.getTargetDegreeName())) {
                    tvDegree.setVisibility(View.GONE);
                } else {
                    tvDegree.setVisibility(View.VISIBLE);
                    tvDegree.setText(data.getTargetDegreeName());
                }
            }
            ivAsker.setTag(data.getAsker().getAvatar());
            DisplayImageUtils.displayPersonImage(BaseApplication.appContext, data.getAsker().getAvatar(), new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                    if (data.getAsker().getAvatar().equals(ivAsker.getTag())) {
                        ivAsker.setImageDrawable(drawable);
                    }
                }
            });
            tvAskerName.setText(data.getAsker().getName());
            tvAskerTime.setText(data.getCreateTimeText());
            tvQuestion.setText(data.getContent());
            if (data.getUserId().equals(SPCacheUtils.get("user_id", ""))) {
                rlMyPostQuestion.setVisibility(View.GONE);
            } else {
                rlMyPostQuestion.setVisibility(View.VISIBLE);
            }
        }

        if (data.getAnswers() != null && data.getAnswers().size() > 0) {
            //把是否是当前自己存进去
            SPCacheUtils.put("isMine", data.getUserId().equals(SPCacheUtils.get("user_id", "")));
            //存入询问人的名字
            SPCacheUtils.put("askName", data.getAsker().getName());
            SPCacheUtils.put("askId", data.getAsker().getId());
            answer.setText("回复");
            qaTreeItems.clear();
            qaTreeItems.addAll(ItemFactory.createTreeItemList(handleAnswer(data.getAnswers()), QaDetailOneTreeItemParent.class, null));
            qaAdapter.setDatas(qaTreeItems);
            mHeaderAdapter.notifyDataSetChanged();
        } else {
            answer.setText("选校帝留学服务老师正在解答中，请稍后...");
        }
    }

    @Override
    public void postQuestionSuccess() {
        KeyBoardUtils.closeKeybord(etAnswer, this);
        rlPost.setVisibility(View.GONE);
        etAnswer.setText("");
        presenter.getQaDetails(questionId);
    }

    @Override
    public void getTeacherInfoSuccess(ConsultantsInfo consultantsInfo) {
        //获取老师信息成功
        if (consultantsInfo != null) {
            Intent intent = new Intent();
            intent.putExtra("consultantsInfo", consultantsInfo);
            intent.setClass(this, TeacherInfoActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void updateRatingSuccess() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        presenter.getQaDetails(questionId);
    }


    private void initAdapter() {
        headView = mInflater.inflate(R.layout.layout_question_list, null, false);
        headView.setVisibility(View.GONE);
        ivAsker = headView.findViewById(R.id.iv_asker);
        tvAskerName = headView.findViewById(R.id.tv_asker_name);
        tvQuestion = headView.findViewById(R.id.tv_question);
        tvQuestion = headView.findViewById(R.id.tv_question);
        tvAskerTime = headView.findViewById(R.id.tv_ask_time);
        answer = headView.findViewById(R.id.answer);
        qaAdapter = new TreeRecyclerAdapter();
        qaAdapter.setType(TreeRecyclerViewType.SHOW_ALL);
        mHeaderAdapter = new TreeHeaderAndFootWapper<>(qaAdapter);
        mHeaderAdapter.addHeaderView(headView);
        qaTreeItems = new ArrayList<>();
        recyclerView.setAdapter(mHeaderAdapter);
        qaAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(final ViewHolder viewHolder, final BaseItem baseItem, final int itemPosition) {
                if (baseItem instanceof QaDetailOneTreeItemParent) {
                    QaDetailOneTreeItemParent qaDetailOneTreeItemParent = (QaDetailOneTreeItemParent) baseItem;
                    if (lastBaseItem != null) {
                        //上一次点击的是二级目录
                        if (lastBaseItem instanceof QaDetailTwoTreeItem) {
                            //重置上次二级目录的播放状态
                            QaDetailTwoTreeItem qaDetailTwoTreeItem = (QaDetailTwoTreeItem) lastBaseItem;
                            qaDetailTwoTreeItem.setVoiceState();
                            qaDetailOneTreeItemParent.voiceOnclick(viewHolder);
                        } else {
                            QaDetailOneTreeItemParent lastOneItem = (QaDetailOneTreeItemParent) lastBaseItem;
                            if (qaDetailOneTreeItemParent == lastBaseItem) {
                                if (qaDetailOneTreeItemParent.getIsPlaying()) {
                                    lastOneItem.setVoiceState();
                                } else {
                                    qaDetailOneTreeItemParent.voiceOnclick(viewHolder);
                                }
                            } else {
                                lastOneItem.setVoiceState();
                                qaDetailOneTreeItemParent.voiceOnclick(viewHolder);
                            }
                        }
                    } else {
                        qaDetailOneTreeItemParent.voiceOnclick(viewHolder);
                    }
                }

                if (baseItem instanceof QaDetailTwoTreeItem) {
                    QaDetailTwoTreeItem qaDetailTwoTreeItem = (QaDetailTwoTreeItem) baseItem;
                    if (lastBaseItem != null) {
                        if (lastBaseItem instanceof QaDetailOneTreeItemParent) {
                            //上次点击的是一级目录，需要把上次上次点击的状态重置，包括播放状态等
                            QaDetailOneTreeItemParent qaDetailOneTreeItemParent = (QaDetailOneTreeItemParent) lastBaseItem;
                            qaDetailOneTreeItemParent.setVoiceState();
                            qaDetailTwoTreeItem.voiceOnclick(viewHolder);
                        } else {
                            QaDetailTwoTreeItem lastTwoItem = (QaDetailTwoTreeItem) lastBaseItem;
                            //如果上次点击的是二级目录本身，也需要重置状态
                            //判断是否同一条二级目录
                            if (qaDetailTwoTreeItem == lastBaseItem) {
                                //同一条如果正在播放，不需在开启播放，否则开启播放
                                if (qaDetailTwoTreeItem.getIsPlaying()) {
                                    lastTwoItem.setVoiceState();
                                } else {
                                    qaDetailTwoTreeItem.voiceOnclick(viewHolder);
                                }
                            } else {
                                lastTwoItem.setVoiceState();
                                qaDetailTwoTreeItem.voiceOnclick(viewHolder);
                            }
                        }
                    } else {
                        qaDetailTwoTreeItem.voiceOnclick(viewHolder);
                    }
                }
                lastBaseItem = baseItem;
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.CompleteInfoEvent event) {
        //极光未读消息处理
        presenter.getQaDetails(questionId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(QaCommenInfo qaCommenInfo) {
        if (qaCommenInfo != null) {
            showCommentDialog(qaCommenInfo);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(Answerer.Comments commenter) {
        this.comment = commenter;
        action = AppContants.SECOND_ACTION;
        rlPost.setVisibility(View.VISIBLE);
        if (etAnswer.getVisibility() == View.VISIBLE) {
            etAnswer.requestFocus();
            KeyBoardUtils.openKeybord(etAnswer, QaDetailActivity.this);
            String name = comment.getCommenter().getName();
            if (!TextUtils.isEmpty(name)) {
                if (name.length() > 16) {
                    name = name.substring(0, 13) + "...";
                }
            }
            etAnswer.setHint(String.format(getString(R.string.subQuestion), name));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(Answerer answerer) {
        //点击了老师的头像触发的事件
        if ("image".equals(answerer.getFrom())) {
            if (answerer.getCommenter() == null) {
                return;
            } else {
                if (TextUtils.isEmpty(answerer.getCommenter().getId())) {
                    return;
                }

                if (answerer.getCommenterRole().equals("teacher")) {
                    return;
                }
                presenter.getTeacher(answerer.getCommenter().getId());
            }
        } else {
            QaDetailActivity.this.answerer = answerer;
            action = AppContants.FIRST_ACTION;
            rlPost.setVisibility(View.VISIBLE);
            if (etAnswer.getVisibility() == View.VISIBLE) {
                etAnswer.requestFocus();
                KeyBoardUtils.openKeybord(etAnswer, QaDetailActivity.this);
                String name = QaDetailActivity.this.answerer.getCommenter().getName();
                if (!TextUtils.isEmpty(name)) {
                    if (name.length() > 16) {
                        name = name.substring(0, 13) + "...";
                    }
                }
                etAnswer.setHint(String.format(getString(R.string.subQuestion), name));
            }
        }
    }

    private void showCommentDialog(final QaCommenInfo qaCommenInfo) {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = DialogCreator.createCommentDialog(this, "QA", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String et_msg = v.getTag().toString();
                presenter.updateRating(qaCommenInfo.getQuestionId(), qaCommenInfo.getAnswerId(), ((Button) v).getHint().toString(), et_msg);
                KeyBoardUtils.closeKeybord((EditText) dialog.getCurrentFocus(), QaDetailActivity.this);
            }
        });
    }

    private List<Answerer> handleAnswer(List<Answerer> answerers) {
        for (Answerer answerer : answerers) {
            int answererCount = answerer.getComments().size();
            if (answerer.getComments() != null && answererCount > 0) {
                for (int i = answererCount - 1; i >= 0; i--) {
                    if (answerer.getComments().get(i).getCommentType().equals("answer")) {
                        answerer.getComments().get(i).setAnswerAgain(true);
                        break;
                    } else {
                        if (i == 0) {
                            answerer.setAnswerAgain(true);
                        }
                    }
                }
            } else {
                answerer.setAnswerAgain(true);
            }
        }
        return answerers;
    }

}

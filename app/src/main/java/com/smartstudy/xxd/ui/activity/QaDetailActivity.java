package com.smartstudy.xxd.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.ui.customview.audio.AudioRecorder;
import com.smartstudy.commonlib.ui.customview.decoration.HorizontalDividerItemDecoration;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.Answerer;
import com.smartstudy.xxd.entity.QaDetailInfo;
import com.smartstudy.xxd.mvp.contract.QaDetailContract;
import com.smartstudy.xxd.mvp.presenter.QaDetailPresenter;
import com.smartstudy.xxd.ui.adapter.QaDetailAdapter;
import com.smartstudy.xxd.ui.adapter.QadetailAnswerItemAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by yqy on 2017/12/4.
 */
@Route("qaDetail")
public class QaDetailActivity extends UIActivity implements QaDetailContract.View {

    private String questionId;

    private NoScrollLinearLayoutManager mLayoutManager;

    private RecyclerView recyclerView;

    private ImageView ivAsker;

    private TextView tvAskerName;

    private TextView tvQuestion;

    private TextView tvAskerTime;

    private QaDetailAdapter qaDetailAdapter;

    private TextView tvPost;

    private QaDetailContract.Presenter presenter;

    private EditText etAnswer;

    private Answerer.Comments comment;

    private LinearLayout rl_post;

    private LinearLayout rl_my_post_question;

    private String action = "secondAction";

    private Answerer answerer;

    private TextView answer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_detail_list);
        StatisticUtils.actionEvent(this, "21_B_question_detail");
    }


    @Override
    protected void initViewAndData() {
        TextView titleView = (TextView) findViewById(R.id.topdefault_centertitle);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        Intent data = getIntent();
        titleView.setText("问题详情");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        questionId = data.getStringExtra("id");
        if (!TextUtils.isEmpty(questionId) && data.getExtras() != null) {
            questionId = data.getExtras().getString("id");
        }
        new QaDetailPresenter(this);
        recyclerView = findViewById(R.id.rv_qa);
        tvPost = findViewById(R.id.tv_post);
        rl_post = findViewById(R.id.rl_post);
        etAnswer = findViewById(R.id.et_answer);
        rl_my_post_question = findViewById(R.id.rl_my_post_question);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .size(DensityUtils.dip2px(0.5f)).colorResId(R.color.bg_home_search).build());

        ivAsker = findViewById(R.id.iv_asker);

        tvAskerName = findViewById(R.id.tv_asker_name);

        tvQuestion = findViewById(R.id.tv_question);

        tvAskerTime = findViewById(R.id.tv_ask_time);

        answer = findViewById(R.id.answer);

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

        rl_my_post_question.setOnClickListener(this);

        qaDetailAdapter.setTeacherInfoGetOnclickListener(new QaDetailAdapter.TeacherInfoGetOnclickListener() {
            @Override
            public void teacherInfoGetOnclickListener(Answerer answerer) {

                if (answerer.getCommenter() == null) {
                    ToastUtils.showToast(QaDetailActivity.this, "查不到该老师的信息");
                    return;
                } else {
                    if (TextUtils.isEmpty(answerer.getCommenter().getId())) {
                        ToastUtils.showToast(QaDetailActivity.this, "查不到该老师的信息");
                        return;
                    }

                    if (answerer.getCommenterRole().equals("teacher")) {
                        ToastUtils.showToast(QaDetailActivity.this, "查不到该老师的信息");
                        return;
                    }
                    presenter.getTeacher(answerer.getCommenter().getId());
                }
            }
        });
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.tv_post:

                if (TextUtils.isEmpty(etAnswer.getText().toString().trim())) {
                    ToastUtils.showToast(this, "提交问题不能为空");
                    return;
                }

                if (action.equals("secondAction")) {
                    if (comment != null) {
                        presenter.postQuestion(comment.getQuestionId(), comment.getId(), etAnswer.getText().toString());
                    }
                } else {
                    if (answerer != null) {
                        presenter.postQuestion(answerer.getQuestionId(), answerer.getId(), etAnswer.getText().toString());
                    }
                }
                break;

            case R.id.rl_my_post_question:
                String ticket = (String) SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL);
                if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
//                    StatisticUtils.actionEvent(this, "19_A_post_btn");
                    startActivity(new Intent(this, AddQuestionActivity.class));
                } else {
                    DialogCreator.createLoginDialog(this);
                }
                break;

            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;

        }

    }

    @Override
    public void setPresenter(QaDetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }

    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);

    }

    @Override
    public void getQaDetails(QaDetailInfo data) {
        if (data.getAsker() != null) {
            DisplayImageUtils.formatPersonImgUrl(getApplicationContext(), data.getAsker().getAvatar(), ivAsker);
            tvAskerName.setText(data.getAsker().getName());
            tvAskerTime.setText(data.getCreateTimeText());
            tvQuestion.setText(data.getContent());
            if (data.getUserId().equals(SPCacheUtils.get("user_id", ""))) {
                rl_my_post_question.setVisibility(View.GONE);
            } else {
                rl_my_post_question.setVisibility(View.VISIBLE);
            }
        }

        if (data.getAnswers() != null && data.getAnswers().size() > 0) {
            if (qaDetailAdapter != null && data.getAsker() != null) {
                boolean isMine = data.getUserId().equals(SPCacheUtils.get("user_id", ""));
                qaDetailAdapter.setAnswers(handleAnswer(data.getAnswers()), data.getAsker().getName(), isMine);
                answer.setText("回复");
            }
        } else {
            answer.setText("选校帝留学服务老师正在解答中，请稍后...");
        }
        data = null;

    }


    @Override
    public void postQuestionSuccess() {
//        rlPost.setVisibility(View.GONE);
        hideWindowSoft();
        rl_post.setVisibility(View.GONE);
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


    private void initAdapter() {
        qaDetailAdapter = new QaDetailAdapter(this);
        recyclerView.setAdapter(qaDetailAdapter);
        qaDetailAdapter.setAnswerAgainOnclickListener(new QadetailAnswerItemAdapter.AnswerAgainOnclickListener() {
            @Override
            public void answerAgainOnclick(Answerer.Comments comments) {
                comment = comments;
                action = "secondAction";
                rl_post.setVisibility(View.VISIBLE);
                showWindowSoft();
                etAnswer.setHint("追问 @" + comment.getCommenter().getName());
            }
        });

        qaDetailAdapter.setFirstAnswerAgainOnclickListener(new QaDetailAdapter.FirstAnswerAgainOnclickListener() {
            @Override
            public void answerAgainOnclickListener(Answerer answerer) {
                QaDetailActivity.this.answerer = answerer;
                action = "firstAction";
                rl_post.setVisibility(View.VISIBLE);
                showWindowSoft();
                etAnswer.setHint("追问 @" + QaDetailActivity.this.answerer.getCommenter().getName());
            }
        });
    }

    /**
     * 获取显示我要追问的位置
     *
     * @return
     */


    private void showWindowSoft() {
        etAnswer.setFocusable(true);
        etAnswer.setFocusableInTouchMode(true);
        etAnswer.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) etAnswer.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(etAnswer, 0);
    }


    private void hideWindowSoft() {
        etAnswer.setText("");
        etAnswer.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etAnswer.getWindowToken(), 0);
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


        if (presenter != null) {
            presenter = null;
        }
        super.onDestroy();
    }


    private List<Answerer> handleAnswer(List<Answerer> answerers) {
        for (Answerer answerer : answerers) {
            if (answerer.getComments() != null && answerer.getComments().size() > 0) {
                for (int i = answerer.getComments().size() - 1; i >= 0; i--) {
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

package com.meiqia.meiqiasdk.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meiqia.meiqiasdk.R;
import com.meiqia.meiqiasdk.callback.OnEvaluateRobotAnswerCallback;
import com.meiqia.meiqiasdk.model.BaseMessage;
import com.meiqia.meiqiasdk.model.RobotMessage;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQUtils;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;

/**
 * OnePiece
 * Created by xukq on 6/24/16.
 */
public class MQWebViewActivity extends BaseActivity {

    public static final String CONTENT = "content";

    private RelativeLayout mBackRl;
    private WebView mWebView;

    private RelativeLayout mEvaluateRl;
    private TextView mUsefulTv;
    private TextView mUselessTv;
    private TextView mAlreadyFeedbackTv;

    public static RobotMessage sRobotMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mq_activity_webview);
    }

    @Override
    protected void initViewAndData() {
        mBackRl = (RelativeLayout) findViewById(R.id.back_rl);
        mWebView = (WebView) findViewById(R.id.webview);

        mEvaluateRl = (RelativeLayout) findViewById(R.id.ll_robot_evaluate);
        mUsefulTv = (TextView) findViewById(R.id.tv_robot_useful);
        mUselessTv = (TextView) findViewById(R.id.tv_robot_useless);
        mAlreadyFeedbackTv = (TextView) findViewById(R.id.tv_robot_already_feedback);
        logic();
    }

    @Override
    public void initEvent() {
        mBackRl.setOnClickListener(this);
        mUsefulTv.setOnClickListener(this);
        mUselessTv.setOnClickListener(this);
        mAlreadyFeedbackTv.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        if (id == R.id.back_rl) {
            onBackPressed();
        } else if (id == R.id.tv_robot_useful) {
            evaluate(RobotMessage.EVALUATE_USEFUL);
        } else if (id == R.id.tv_robot_useless) {
            evaluate(RobotMessage.EVALUATE_USELESS);
        } else if (id == R.id.tv_robot_already_feedback) {
            mEvaluateRl.setVisibility(View.GONE);
        }
    }


    private void logic() {
        if (getIntent() != null) {
            handleRobotRichTextMessage();
            String data = getIntent().getStringExtra(CONTENT);
            mWebView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);

        }
    }

    private void handleRobotRichTextMessage() {
        if (sRobotMessage != null) {
            if (TextUtils.equals(RobotMessage.SUB_TYPE_EVALUATE, sRobotMessage.getSubType())
                    || BaseMessage.TYPE_CONTENT_RICH_TEXT.equals(sRobotMessage.getContentType())) {
                mEvaluateRl.setVisibility(View.VISIBLE);
                if (sRobotMessage.isAlreadyFeedback()) {
                    mUselessTv.setVisibility(View.GONE);
                    mUsefulTv.setVisibility(View.GONE);
                    mAlreadyFeedbackTv.setVisibility(View.VISIBLE);
                } else {
                    mUselessTv.setVisibility(View.VISIBLE);
                    mUsefulTv.setVisibility(View.VISIBLE);
                    mAlreadyFeedbackTv.setVisibility(View.GONE);
                }
            }
        }
    }

    private void evaluate(int useful) {
        MQConfig.getController(this).evaluateRobotAnswer(sRobotMessage.getId(), sRobotMessage.getQuestionId(), useful, new OnEvaluateRobotAnswerCallback() {
            @Override
            public void onFailure(int code, String message) {
                MQUtils.show(MQWebViewActivity.this, R.string.mq_evaluate_failure);
            }

            @Override
            public void onSuccess(String message) {
                sRobotMessage.setAlreadyFeedback(true);
                handleRobotRichTextMessage();
            }
        });
    }
}

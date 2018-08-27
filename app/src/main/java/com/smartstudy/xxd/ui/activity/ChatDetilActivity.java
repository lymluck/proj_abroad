package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.entity.ConsultantsInfo;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.ChatDetailContract;
import com.smartstudy.xxd.mvp.presenter.ChatDetailPresenter;
import com.smartstudy.xxd.ui.customview.PushSlideSwitchView;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by yqy on 2017/12/26.
 */
@Route("ChatDetilActivity")
public class ChatDetilActivity extends UIActivity implements ChatDetailContract.View {
    private ChatDetailContract.Presenter presenter;
    private String id;
    private ConsultantsInfo teacherInfo;
    private ImageView ivAvatar;
    private TextView tv_name;
    private TextView tv_title;
    private TextView tv_year_working;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("聊天详情");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(ChatDetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void getTeacherInfoSuccess(ConsultantsInfo teacherInfo) {
        this.teacherInfo = teacherInfo;
        if (ivAvatar != null) {
            DisplayImageUtils.displayCircleImage(this, teacherInfo.getAvatar(), ivAvatar);
        }
        if (!TextUtils.isEmpty(teacherInfo.getTitle())) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(teacherInfo.getTitle());
        }
        if (!TextUtils.isEmpty(teacherInfo.getYearsOfWorking())) {
            tv_year_working.setVisibility(View.VISIBLE);
            tv_year_working.setText(teacherInfo.getYearsOfWorking());
        }
        tv_name.setText(teacherInfo.getName());

        ((TextView) this.findViewById(R.id.tv_name)).setText(teacherInfo.getName());

        if (TextUtils.isEmpty(teacherInfo.getTitle())) {
            tv_title.setVisibility(View.GONE);
        } else {
            tv_title.setText(teacherInfo.getTitle());
        }

        if (TextUtils.isEmpty(teacherInfo.getYearsOfWorking())) {
            tv_year_working.setVisibility(View.GONE);
        } else {
            tv_year_working.setText("从业" + teacherInfo.getYearsOfWorking() + "年");
        }
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("聊天详情");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ivAvatar = findViewById(R.id.iv_avatar);
        tv_name = findViewById(R.id.tv_name);
        tv_title = findViewById(R.id.tv_title);
        tv_year_working = findViewById(R.id.tv_year_working);
        id = getIntent().getStringExtra("id");
        new ChatDetailPresenter(this);
        if (!TextUtils.isEmpty(id)) {
            presenter.getTeacherInfo(id);
        }
    }

    @Override
    public void initEvent() {
        findViewById(R.id.ll_report_reason).setOnClickListener(this);
        findViewById(R.id.ll_user_info).setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        final PushSlideSwitchView btn_black = findViewById(R.id.btn_black);
        btn_black.setChecked(false);
        RongIM.getInstance().getBlacklistStatus(id, new RongIMClient.ResultCallback<RongIMClient.BlacklistStatus>() {
            @Override
            public void onSuccess(RongIMClient.BlacklistStatus blacklistStatus) {
                if (RongIMClient.BlacklistStatus.IN_BLACK_LIST.equals(blacklistStatus)) {
                    btn_black.setChecked(true);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
        btn_black.setOnChangeListener(new PushSlideSwitchView.OnSwitchChangedListener() {
            @Override
            public void onSwitchChange(PushSlideSwitchView switchView, boolean isChecked) {
                if (isChecked) {
                    RongIM.getInstance().addToBlacklist(id, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            btn_black.setChecked(true);
                            RongIM.getInstance().getBlacklist(new RongIMClient.GetBlacklistCallback() {
                                @Override
                                public void onSuccess(String[] strings) {

                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            btn_black.setChecked(false);
                        }
                    });
                } else {
                    RongIM.getInstance().removeFromBlacklist(id, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            btn_black.setChecked(false);
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            btn_black.setChecked(true);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.ll_report_reason:
                if (teacherInfo != null) {
                    Intent intent = new Intent();
                    intent.putExtra("id", teacherInfo.getId());
                    intent.setClass(this, ReportActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_user_info:
                if (teacherInfo != null) {
                    Intent intent = new Intent();
                    intent.putExtra("consultantsInfo", teacherInfo);
                    intent.setClass(this, TeacherInfoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }
    }
}

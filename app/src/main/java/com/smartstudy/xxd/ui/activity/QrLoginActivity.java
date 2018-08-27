package com.smartstudy.xxd.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.QrLoginContract;
import com.smartstudy.xxd.mvp.model.QrCodeModel;
import com.smartstudy.xxd.mvp.presenter.QrLoginPresenter;

public class QrLoginActivity extends BaseActivity implements QrLoginContract.View {

    private ProgressDialog mProgressDialog;

    private QrLoginContract.Presenter presenter;
    private boolean valid = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_login);
    }

    @Override
    protected void initViewAndData() {
        // 显示进度条
        mProgressDialog = ProgressDialog.show(QrLoginActivity.this, null,
            getString(R.string.scan_over), false, true);
        mProgressDialog.show();
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("登录选校帝网页版");
        new QrLoginPresenter(this);
        presenter.verify(getIntent().getStringExtra("params"));
    }

    private void dismissDialog() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.tv_cancle).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.tv_cancle:
                finish();
                break;
            case R.id.btn_login:
                if (valid) {
                    login();
                } else {
                    startActivity(new Intent(this, QrCodeScanActivity.class));
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void login() {
        QrCodeModel.confirmLogin(getIntent().getStringExtra("params"),
            new BaseCallback<String>() {
                @Override
                public void onErr(String errCode, String msg) {
                    initFailedUI();
                }

                @Override
                public void onSuccess(String result) {
                    finish();
                }
            });
    }

    private void initFailedUI() {
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setText("重新扫描");
        btnLogin.setBackgroundResource(R.drawable.bg_red_btn_radius);
        TextView tvTip = findViewById(R.id.tv_tip);
        tvTip.setText("登录失败，请重新扫码登录");
        ImageView ivTip = findViewById(R.id.iv_tip);
        ivTip.setImageResource(R.drawable.img_login_wrong);
    }

    @Override
    public void setPresenter(QrLoginContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        findViewById(R.id.ll_content).setVisibility(View.VISIBLE);
        dismissDialog();
        valid = false;
        initFailedUI();
        ToastUtils.showToast(message);
    }

    @Override
    public void success() {
        findViewById(R.id.ll_content).setVisibility(View.VISIBLE);
        dismissDialog();
        valid = true;
    }

    @Override
    public void failed() {
        findViewById(R.id.ll_content).setVisibility(View.VISIBLE);
        dismissDialog();
        valid = false;
        initFailedUI();
    }
}

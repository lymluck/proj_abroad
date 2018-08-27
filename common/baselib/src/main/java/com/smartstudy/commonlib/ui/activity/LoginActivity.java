package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.SoftKeyBoardListener;
import com.smartstudy.commonlib.mvp.contract.LoginActivityContract;
import com.smartstudy.commonlib.mvp.presenter.LoginAcitivityPresenter;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.customview.EditTextWithClear;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.SensorsUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.router.Router;
import com.smartstudy.sdk.api.UApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Route("LoginActivity")
public class LoginActivity extends BaseActivity implements LoginActivityContract.View {
    private EditTextWithClear etcmobile;
    private EditText etcYzm;
    private TextView tvYzm;
    private Button btnLogin;
    private ImageView topRightbtn;
    private TextView tvContract;

    private LoginActivityContract.Presenter loginP;
    private CountDownTimer countDownTimer = null;
    private ImageView imgLogo;
    private FrameLayout flLogo;
    private List<View> list = null;
    private boolean isSelected = false;
    private boolean isDebug = Utils.isApkInDebug();
    private boolean isNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 神策事件统计
        SensorsUtils.trackCustomeEvent("login_popup");
        // umeng统计
        UApp.actionEvent(this, "1_B_login");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void initViewAndData() {
        imgLogo = (ImageView) findViewById(R.id.img_logo);
        flLogo = (FrameLayout) findViewById(R.id.flt_logo);
        topRightbtn = (ImageView) findViewById(R.id.topdefault_rightbutton);
        topRightbtn.setImageResource(R.drawable.ic_cha);
        topRightbtn.setVisibility(View.VISIBLE);
        tvContract = (TextView) findViewById(R.id.tv_xxd_contract);
        tvContract.setText(Html.fromHtml("登录即表示同意" + "<font color=#078CF1>"
            + "选校帝用户协议" + "</font>"));
        findViewById(R.id.topdefault_leftbutton).setVisibility(View.GONE);
        tvYzm = (TextView) findViewById(R.id.tv_yzm);
        btnLogin = (Button) findViewById(R.id.btn_login);
        etcmobile = (EditTextWithClear) findViewById(R.id.etc_mobile);
        etcYzm = (EditText) findViewById(R.id.etc_yzm);
        etcYzm.setHint("请输入验证码");
        etcmobile.getMyEditText().setHint("请输入您的手机号");
        etcmobile.getMyEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        etcmobile.setInputType(InputType.TYPE_CLASS_NUMBER);
        String account = (String) SPCacheUtils.get("user_account", "");
        if (!TextUtils.isEmpty(account)) {
            etcmobile.setText(account);
        }
        new LoginAcitivityPresenter(this);
    }

    @Override
    public void initEvent() {
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtils.openKeybord(etcmobile.getMyEditText(), LoginActivity.this);
            }
        }, 300);
        etcYzm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (TextUtils.isEmpty(etcmobile.getText()) || etcmobile.getText().length() != 11) {
                        ToastUtils.showToast(getString(R.string.enter_phone));
                    } else if (TextUtils.isEmpty(etcYzm.getText())) {
                        ToastUtils.showToast(String.format(getString(R.string.not_null), "验证码"));
                    } else {
                        loginP.phoneCodeLogin(etcmobile.getText(), etcYzm.getText().toString());
                    }
                }
                return true;
            }
        });
        btnLogin.setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        topRightbtn.setOnClickListener(this);
        tvContract.setOnClickListener(this);
        tvYzm.setOnClickListener(this);
        final AnimatorSet setShow = new AnimatorSet();
        final AnimatorSet setHide = new AnimatorSet();
        //当键盘弹起的时候用屏幕的高度减去布局的高度，同时获取到键盘的高度，用键盘的高度和剩余的高度做对比
        SoftKeyBoardListener.setListener(LoginActivity.this, new

            SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

                @Override
                public void keyBoardShow(int height) {
                    setShow.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flLogo.getLayoutParams();
                            layoutParams.height = 0;
                            flLogo.setLayoutParams(layoutParams);
                        }

                    });
                    setShow.playTogether(
                        ObjectAnimator.ofFloat(flLogo, "scaleX", 1, 0f),
                        ObjectAnimator.ofFloat(flLogo, "scaleY", 1, 0f),
                        ObjectAnimator.ofFloat(flLogo, "alpha", 1, 0.25f, 1)
                    );
                    setShow.setDuration(300).start();
                }

                @Override
                public void keyBoardHide(int height) {
                    setHide.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flLogo.getLayoutParams();
                            layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.login_logo_wh);
                            flLogo.setLayoutParams(layoutParams);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationStart(animation);
                        }
                    });
                    setHide.playTogether(
                        ObjectAnimator.ofFloat(flLogo, "scaleX", 0, 1f),
                        ObjectAnimator.ofFloat(flLogo, "scaleY", 0, 1f),
                        ObjectAnimator.ofFloat(flLogo, "alpha", 1, 0.25f, 1)
                    );
                    setHide.setDuration(300).start();
                }
            });
        forTest();
    }

    private void forTest() {
        //测试用
        if (isDebug) {
            imgLogo.setOnClickListener(this);
            findViewById(R.id.btn_master).setOnClickListener(this);
            findViewById(R.id.btn_test).setOnClickListener(this);
            findViewById(R.id.btn_dev).setOnClickListener(this);
            list = new ArrayList<>();
            list.add(findViewById(R.id.btn_master));
            list.add(findViewById(R.id.btn_test));
            list.add(findViewById(R.id.btn_dev));
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            imgLogo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ToastUtils.showToast("手机屏幕分辨率:" + displayMetrics.widthPixels + "X" + displayMetrics.heightPixels + "\n" + "密度:"
                        + displayMetrics.density + "\n" + "使用每英寸的像素点来显示密度:" + displayMetrics.densityDpi + "\n" + "xdpi"
                        + displayMetrics.xdpi + "\n" + "ydpi" + displayMetrics.ydpi + "\n" + "scaledDensity"
                        + displayMetrics.scaledDensity, Toast.LENGTH_LONG);
                    return false;
                }
            });
        }
    }

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            if (TextUtils.isEmpty(etcmobile.getText()) || etcmobile.getText().length() != 11) {
                ToastUtils.showToast(getString(R.string.enter_phone));
                return;
            } else if (TextUtils.isEmpty(etcYzm.getText())) {
                ToastUtils.showToast(String.format(getString(R.string.not_null), "验证码"));
                return;
            }
            // 神策事件统计
            SensorsUtils.trackCustomeEvent("click_sign_up");
            // umeng事件统计
            UApp.actionEvent(this, "1_A_login_btn");
            loginP.phoneCodeLogin(etcmobile.getText(), etcYzm.getText().toString());
        } else if (id == R.id.topdefault_leftbutton) {
            KeyBoardUtils.closeKeybord(etcmobile.getMyEditText(), this);
            finish();
        } else if (id == R.id.tv_yzm) {
            tvYzm.setEnabled(false);
            //获取验证码,手机号加密
            if (TextUtils.isEmpty(etcmobile.getText()) || etcmobile.getText().length() != 11) {
                ToastUtils.showToast(getString(R.string.enter_phone));
                tvYzm.setEnabled(true);
                return;
            }
            UApp.actionEvent(this, "1_A_verification_code_btn");
            loginP.getPhoneCode(etcmobile.getText());
        } else if (id == R.id.topdefault_rightbutton) {
            KeyBoardUtils.closeKeybord(etcmobile.getMyEditText(), this);
            finish();
        } else if (id == R.id.tv_xxd_contract) {
            Bundle data = new Bundle();
            data.putString("web_url", HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_USER_CONTRACT));
            data.putString("title", "选校帝用户协议");
            data.putString("url_action", "get");
            Router.build("showWebView").with(data).go(LoginActivity.this);
        } else if (id == R.id.img_logo) {
            //测试用
            if (isDebug) {
                if (!isSelected) {
                    startAnimator();
                } else {
                    endAnimator();
                }
            }
        } else if (id == R.id.btn_master) {
            //测试用
            if (isDebug) {
                if (!isSelected) {
                    startAnimator();
                } else {
                    endAnimator();
                }
                SPCacheUtils.put(ParameterUtils.API_SERVER, "master");
            }
        } else if (id == R.id.btn_test) {
            //测试用
            if (isDebug) {
                if (!isSelected) {
                    startAnimator();
                } else {
                    endAnimator();
                }
                SPCacheUtils.put(ParameterUtils.API_SERVER, "test");
            }
        } else if (id == R.id.btn_dev) {
            //测试用
            if (isDebug) {
                if (!isSelected) {
                    startAnimator();
                } else {
                    endAnimator();
                }
                SPCacheUtils.put(ParameterUtils.API_SERVER, "dev");
            }
        }
    }

    @Override
    public void onBackPressed() {
        KeyBoardUtils.closeKeybord(etcmobile.getMyEditText(), this);
        super.onBackPressed();

    }

    @Override
    public void setPresenter(LoginActivityContract.Presenter presenter) {
        if (presenter != null) {
            this.loginP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        tvYzm.setEnabled(true);
        ToastUtils.showToast(message);
    }

    @Override
    public void getPhoneCodeSuccess() {
        startTimer();
    }

    @Override
    public void phoneCodeLoginSuccess(boolean created, String userId) {
        isNewUser = created;
        // 神策统计登录
        JSONObject loginObj = new JSONObject();
        try {
            loginObj.put("login_way", "验证码登录");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SensorsUtils.trackCustomeEvent("login_success", loginObj);
        // 友盟账号统计
        UApp.login(userId);
        KeyBoardUtils.closeKeybord(etcYzm, this);
        if (created) {
            //跳转到个人信息完善页面
            Router.build("PerfectUserInfoActivity").requestCode(ParameterUtils.REQUEST_CODE_LOGIN).go(this);
        } else {
            if (getIntent().getBooleanExtra("toMain", false)) {
                Router.build("MainActivity").go(this);
            } else {
                setResult(RESULT_OK);
            }
            finish();
        }
    }

    protected void startTimer() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(ParameterUtils.SMS_TIMEOUT, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    tvYzm.setTextColor(getResources().getColor(R.color.app_text_color));
                    tvYzm.setText(String.format(getString(R.string.get_code_again), millisUntilFinished / 1000 + "s"));
                    //防止重复点击
                    tvYzm.setEnabled(false);
                }

                @Override
                public void onFinish() {
                    //可以在这做一些操作,如果没有获取到验证码再去请求服务器
                    //防止重复点击
                    tvYzm.setEnabled(true);
                    tvYzm.setTextColor(getResources().getColor(R.color.app_main_color));
                    tvYzm.setText(getString(R.string.send_again));
                }
            };
        }
        countDownTimer.start();
    }

    private void endAnimator() {
        isSelected = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgLogo, "rotation", 0F, 360F).setDuration(300);
        //设置插值器
        animator.setInterpolator(new BounceInterpolator());
        //开始动画
        animator.start();
        int n = DensityUtils.dip2px(45);
        ObjectAnimator objectAnimator;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                objectAnimator = ObjectAnimator.ofFloat(list.get(i), "translationX", DensityUtils.dip2px(52), 0F).setDuration(1000);
            } else {
                objectAnimator = ObjectAnimator.ofFloat(list.get(i), "translationX", n * (i + 1), 0F).setDuration(1000);
            }
            objectAnimator.start();
        }
    }

    private void startAnimator() {
        isSelected = true;
        int n = DensityUtils.dip2px(45);
        for (int i = 0; i < 3; i++) {
            ObjectAnimator animator;
            if (i == 0) {
                animator = ObjectAnimator.ofFloat(list.get(i), "translationX", 0F, DensityUtils.dip2px(52)).setDuration(1000);
            } else {
                animator = ObjectAnimator.ofFloat(list.get(i), "translationX", 0F, n * (i + 1)).setDuration(1000);
            }
            //设置插值器
            animator.setInterpolator(new BounceInterpolator());
            animator.start();
        }
        ObjectAnimator.ofFloat(imgLogo, "rotation", 0F, 360F).setDuration(300).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_LOGIN:
                if (isNewUser) {
                    Router.build("MainActivity").go(this);
                }
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }
}

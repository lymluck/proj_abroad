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
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.customView.EditTextWithClear;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.router.Router;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

@Route("LoginActivity")
public class LoginActivity extends UIActivity implements LoginActivityContract.View {
    private EditTextWithClear etcmobile;
    private EditText etc_yzm;
    private TextView tv_yzm;
    private Button btn_login;
    private ImageView topdefault_rightbutton;
    private TextView tv_xxd_contract;

    private LoginActivityContract.Presenter loginP;
    private CountDownTimer countDownTimer = null;
    private ImageView img_logo;
    private FrameLayout flt_logo;
    private List<View> list = null;
    private boolean isSelected = false;
    private boolean isDebug = Utils.isApkInDebug();
    private boolean isNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initViewAndData() {
        img_logo = (ImageView) findViewById(R.id.img_logo);
        flt_logo = (FrameLayout) findViewById(R.id.flt_logo);
        topdefault_rightbutton = (ImageView) findViewById(R.id.topdefault_rightbutton);
        topdefault_rightbutton.setImageResource(R.drawable.ic_cha);
        topdefault_rightbutton.setVisibility(View.VISIBLE);
        tv_xxd_contract = (TextView) findViewById(R.id.tv_xxd_contract);
        tv_xxd_contract.setText(Html.fromHtml("登录即表示同意" + "<font color=#078CF1>"
                + "选校帝用户协议" + "</font>"));
        findViewById(R.id.topdefault_leftbutton).setVisibility(View.GONE);
        tv_yzm = (TextView) findViewById(R.id.tv_yzm);
        btn_login = (Button) findViewById(R.id.btn_login);
        etcmobile = (EditTextWithClear) findViewById(R.id.etc_mobile);
        etc_yzm = (EditText) findViewById(R.id.etc_yzm);
        etc_yzm.setInputType(InputType.TYPE_CLASS_NUMBER);
        etc_yzm.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etc_yzm.setHint("请输入验证码");
        etcmobile.getMyEditText().setHint("请输入您的手机号");
        etcmobile.getMyEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        etcmobile.setInputType(InputType.TYPE_CLASS_NUMBER);
        String account = (String) SPCacheUtils.get("user_account", ParameterUtils.CACHE_NULL);
        if (!ParameterUtils.CACHE_NULL.equals(account)) {
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
        etc_yzm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (TextUtils.isEmpty(etcmobile.getText()) || etcmobile.getText().length() != 11) {
                        ToastUtils.showToast(LoginActivity.this, getString(R.string.enter_phone));
                    } else if (TextUtils.isEmpty(etc_yzm.getText())) {
                        ToastUtils.showToast(LoginActivity.this, String.format(getString(R.string.not_null), "验证码"));
                    } else {
                        loginP.phoneCodeLogin(etcmobile.getText(), etc_yzm.getText().toString());
                    }
                }
                return true;
            }
        });
        btn_login.setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        topdefault_rightbutton.setOnClickListener(this);
        tv_xxd_contract.setOnClickListener(this);
        tv_yzm.setOnClickListener(this);
        final AnimatorSet set = new AnimatorSet();
        //当键盘弹起的时候用屏幕的高度减去布局的高度，同时获取到键盘的高度，用键盘的高度和剩余的高度做对比
        SoftKeyBoardListener.setListener(LoginActivity.this, new

                SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

                    @Override
                    public void keyBoardShow(int height) {
                        set.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flt_logo.getLayoutParams();
                                layoutParams.height = 0;
                                flt_logo.setLayoutParams(layoutParams);
                            }

                        });
                        set.playTogether(
                                ObjectAnimator.ofFloat(flt_logo, "scaleX", 1, 0f),
                                ObjectAnimator.ofFloat(flt_logo, "scaleY", 1, 0f),
                                ObjectAnimator.ofFloat(flt_logo, "alpha", 1, 0.25f, 1)
                        );
                        set.setDuration(300).start();
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        AnimatorSet set = new AnimatorSet();
                        set.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flt_logo.getLayoutParams();
                                layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.login_logo_wh);
                                flt_logo.setLayoutParams(layoutParams);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationStart(animation);
                            }
                        });
                        set.playTogether(
                                ObjectAnimator.ofFloat(flt_logo, "scaleX", 0, 1f),
                                ObjectAnimator.ofFloat(flt_logo, "scaleY", 0, 1f),
                                ObjectAnimator.ofFloat(flt_logo, "alpha", 1, 0.25f, 1)
                        );
                        set.setDuration(300).start();
                    }
                });
        forTest();
    }

    private void forTest() {
        //测试用
        if (isDebug) {
            img_logo.setOnClickListener(this);
            findViewById(R.id.btn_master).setOnClickListener(this);
            findViewById(R.id.btn_test).setOnClickListener(this);
            findViewById(R.id.btn_dev).setOnClickListener(this);
            list = new ArrayList<>();
            list.add(findViewById(R.id.btn_master));
            list.add(findViewById(R.id.btn_test));
            list.add(findViewById(R.id.btn_dev));
            final DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            img_logo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(LoginActivity.this, "手机屏幕分辨率:" + displayMetrics.widthPixels + "X" + displayMetrics.heightPixels + "\n" + "密度:"
                            + displayMetrics.density + "\n" + "使用每英寸的像素点来显示密度:" + displayMetrics.densityDpi + "\n" + "xdpi"
                            + displayMetrics.xdpi + "\n" + "ydpi" + displayMetrics.ydpi + "\n" + "scaledDensity"
                            + displayMetrics.scaledDensity, Toast.LENGTH_LONG).show();
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
                ToastUtils.showToast(LoginActivity.this, getString(R.string.enter_phone));
                return;
            } else if (TextUtils.isEmpty(etc_yzm.getText())) {
                ToastUtils.showToast(LoginActivity.this, String.format(getString(R.string.not_null), "验证码"));
                return;
            }
            loginP.phoneCodeLogin(etcmobile.getText(), etc_yzm.getText().toString());
        } else if (id == R.id.topdefault_leftbutton) {
            KeyBoardUtils.closeKeybord(etcmobile.getMyEditText(), this);
            finish();
        } else if (id == R.id.tv_yzm) {
            tv_yzm.setEnabled(false);
            //获取验证码,手机号加密
            if (TextUtils.isEmpty(etcmobile.getText()) || etcmobile.getText().length() != 11) {
                ToastUtils.showToast(LoginActivity.this, getString(R.string.enter_phone));
                tv_yzm.setEnabled(true);
                return;
            }
            loginP.getPhoneCode(etcmobile.getText());
        } else if (id == R.id.topdefault_rightbutton) {
            KeyBoardUtils.closeKeybord(etcmobile.getMyEditText(), this);
            finish();
        } else if (id == R.id.tv_xxd_contract) {
            Bundle data = new Bundle();
            data.putString("web_url", HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_USER_CONTRACT));
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
        tv_yzm.setEnabled(true);
        ToastUtils.showToast(this, message);
    }

    @Override
    public void getPhoneCodeSuccess() {
        startTimer();
    }

    @Override
    public void phoneCodeLoginSuccess(boolean created, String user_Id) {
        isNewUser = created;
        //友盟账号统计
        MobclickAgent.onProfileSignIn(user_Id);
        KeyBoardUtils.closeKeybord(etc_yzm, this);
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
        countDownTimer = new CountDownTimer(ParameterUtils.SMS_TIMEOUT, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tv_yzm.setTextColor(getResources().getColor(R.color.app_text_color));
                tv_yzm.setText(String.format(getString(R.string.get_code_again), millisUntilFinished / 1000 + "s"));
                tv_yzm.setEnabled(false);//防止重复点击
            }

            @Override
            public void onFinish() {
                //可以在这做一些操作,如果没有获取到验证码再去请求服务器
                tv_yzm.setEnabled(true);//防止重复点击
                tv_yzm.setTextColor(getResources().getColor(R.color.app_main_color));
                tv_yzm.setText(getString(R.string.send_again));
            }
        };
        countDownTimer.start();
    }

    private void endAnimator() {
        isSelected = false;
        ObjectAnimator animator = ObjectAnimator.ofFloat(img_logo, "rotation", 0F, 360F).setDuration(300);
        animator.setInterpolator(new BounceInterpolator());//设置插值器
        animator.start();//开始动画
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
            animator.setInterpolator(new BounceInterpolator());//设置插值器
            animator.start();
        }
        ObjectAnimator.ofFloat(img_logo, "rotation", 0F, 360F).setDuration(300).start();
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
        }
    }
}

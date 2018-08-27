package com.smartstudy.commonlib.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.mvp.contract.SplashContract;
import com.smartstudy.commonlib.mvp.presenter.SplashPresenter;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.SensorsUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.router.Router;

import static com.smartstudy.commonlib.utils.ParameterUtils.TITLE;
import static com.smartstudy.commonlib.utils.ParameterUtils.WEBVIEW_ACTION;
import static com.smartstudy.commonlib.utils.ParameterUtils.WEBVIEW_URL;

/**
 * @author louis
 * @desc 启动页
 * @time created at 17/3/30 下午4:50
 * @company www.smartstudy.com
 */
public class SplashActivity extends BaseActivity implements SplashContract.View {

    private ImageView ivAdv;
    private TextView tvGo;

    private WeakHandler mHandler;
    private boolean needGo = false;
    private boolean isFirst = true;
    private boolean hasGo = false;
    private SplashContract.Presenter splashP;
    private String adId;
    private String title;
    private String adUrl;
    private CountDownTimer countDownTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setChangeStatusTrans(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onDestroy() {
        releaseRes();
        super.onDestroy();
    }

    private void releaseRes() {
        if (mHandler != null) {
            mHandler = null;
        }
        if (splashP != null) {
            splashP = null;
        }
        if (countDownTimer != null) {
            countDownTimer.onFinish();
            countDownTimer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasBasePer) {
            splashP.getAdInfo();
        }
        if (isFirst) {
            isFirst = false;
            String imgUrl = (String) SPCacheUtils.get("adImg", "");
            if (!TextUtils.isEmpty(imgUrl)) {
                //有广告时延迟时间增加
                mHandler.sendEmptyMessageDelayed(ParameterUtils.EMPTY_WHAT, 4000);
            } else {
                mHandler.sendEmptyMessageDelayed(ParameterUtils.EMPTY_WHAT, 2500);
            }
        } else {
            if (needGo && hasBasePer) {
                goIndex();
            }
        }
    }

    @Override
    protected void initViewAndData() {
        String imgUrl = (String) SPCacheUtils.get("adImg", "");
        if (!TextUtils.isEmpty(imgUrl)) {
            ((ViewStub) findViewById(R.id.vs_adv)).inflate();
            ivAdv = findViewById(R.id.iv_adv);
            tvGo = findViewById(R.id.tv_go);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivAdv.getLayoutParams();
            params.width = ScreenUtils.getScreenWidth();
            params.height = (ScreenUtils.getScreenWidth() * 7) / 5;
            ivAdv.setLayoutParams(params);
            DisplayImageUtils.formatImgUrlNoHolder(this, imgUrl, ivAdv);
            findViewById(R.id.iv_splash).setVisibility(View.GONE);
            initAdvEvent();
            startTimer();
        }
        new SplashPresenter(this);
        /**在应用的入口activity加入以下代码，解决首次安装应用，点击应用图标打开应用，点击home健回到桌面，再次点击应用图标，进入应用时多次初始化SplashActivity的问题*/
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        if (!isTaskRoot()) {
            finish();
            return;
        }
    }

    @Override
    public void initEvent() {
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ParameterUtils.EMPTY_WHAT:
                        needGo = true;
                        if (hasBasePer) {
                            if (!hasGo) {
                                goIndex();
                            }
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initAdvEvent() {
        ivAdv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!TextUtils.isEmpty(adUrl)) {
                    hasGo = true;
                    //广告页
                    splashP.trackAd(adId);
                    Router.build("showWebView")
                        .with(WEBVIEW_URL, adUrl)
                        .with(WEBVIEW_ACTION, "get")
                        .with(TITLE, title)
                        .requestCode(ParameterUtils.REQUEST_CODE_WEBVIEW).go(SplashActivity.this);
                }
                return false;
            }
        });
        tvGo.setOnClickListener(this);
    }

    //跳转到首页
    private void goIndex() {
        String ticket = (String) SPCacheUtils.get("ticket", "");
        boolean hasGuide = (boolean) SPCacheUtils.get("hasGuide", false);
        if (hasGuide) {
            if (!TextUtils.isEmpty(ticket)) {
                String userId = (String) SPCacheUtils.get("zhike_id", "");
                if (!TextUtils.isEmpty(userId)) {
                    SensorsUtils.trackLogin(userId);
                }
                Router.build("MainActivity").go(this);
            } else {
                Intent toLogin = new Intent(this, LoginActivity.class);
                toLogin.putExtra("toMain", true);
                toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toLogin);
            }
        } else {
            Router.build("GuideActivity").go(this);
        }
        finish();
    }

    @Override
    protected void doClick(View v) {
        if (v.getId() == R.id.tv_go) {
            hasGo = true;
            //跳过
            goIndex();
        }
    }

    @Override
    public void hasBasePermission() {
        //权限授予成功
        if (needGo) {
            goIndex();
        }
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        if (presenter != null) {
            splashP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void getAdSuccess(String adId, String name, String imgUrl, String adUrl) {
        this.adId = adId;
        title = name;
        this.adUrl = adUrl;
        SPCacheUtils.put("adImg", imgUrl);
    }

    protected void startTimer() {
        countDownTimer = new CountDownTimer(3500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvGo.setText(String.format(getString(R.string.ad_loop), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tvGo.setText(String.format(getString(R.string.ad_loop), 0));
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_WEBVIEW:
                goIndex();
                break;
            default:
                break;
        }
    }
}

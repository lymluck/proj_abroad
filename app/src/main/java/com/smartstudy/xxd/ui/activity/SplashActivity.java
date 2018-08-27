package com.smartstudy.xxd.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.tools.SystemBarTintManager;
import com.smartstudy.commonlib.mvp.contract.SplashContract;
import com.smartstudy.commonlib.mvp.presenter.SplashPresenter;
import com.smartstudy.commonlib.ui.activity.LoginActivity;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;

import java.util.List;

/**
 * @author louis
 * @desc 启动页
 * @time created at 17/3/30 下午4:50
 * @company www.smartstudy.com
 */
public class SplashActivity extends UIActivity implements SplashContract.View {

    private ImageView iv_splash;
    private TextView tv_go;

    private WeakHandler mHandler;
    private boolean needGo = false;
    private boolean isFirst = true;
    private boolean hasGo = false;
    private SplashContract.Presenter splashP;
    private String adId;
    private String title;
    private String adUrl;
    private CountDownTimer countDownTimer = null;
    private static String[] REQUEST_PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

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
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
        tv_go = (TextView) findViewById(R.id.tv_go);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iv_splash.getLayoutParams();
        params.width = ScreenUtils.getScreenWidth();
        params.height = (ScreenUtils.getScreenWidth() * 299) / 207;
        iv_splash.setLayoutParams(params);
        String imgUrl = (String) SPCacheUtils.get("adImg", "");
        if (!TextUtils.isEmpty(imgUrl)) {
            DisplayImageUtils.displayImageNoHolder(this, imgUrl, iv_splash);
            findViewById(R.id.llyt_ad).setVisibility(View.VISIBLE);
            startTimer();
        }
        initSysBar();
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
        iv_splash.setOnClickListener(this);
        tv_go.setOnClickListener(this);
    }

    //跳转到首页
    private void goIndex() {
        String ticket = (String) SPCacheUtils.get("ticket", ParameterUtils.CACHE_NULL);
        boolean hasGuide = (boolean) SPCacheUtils.get("hasGuide", false);
        Intent it;
        if (hasGuide) {
            if (!ParameterUtils.CACHE_NULL.equals(ticket)) {
                it = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                it = new Intent(SplashActivity.this, LoginActivity.class);
                it.putExtra("toMain", true);
            }
        } else {
            it = new Intent(SplashActivity.this, GuideActivity.class);
        }
        startActivity(it); //执行
        finish();
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.iv_splash:
                hasGo = true;
                //广告页
                splashP.trackAd(adId);
                Intent toMoreDetails = new Intent(this, ShowWebViewActivity.class);
                toMoreDetails.putExtra("web_url", adUrl);
                toMoreDetails.putExtra("title", title);
                toMoreDetails.putExtra("url_action", "get");
                startActivityForResult(toMoreDetails, ParameterUtils.REQUEST_CODE_WEBVIEW);
                break;
            case R.id.tv_go:
                hasGo = true;
                //跳过
                goIndex();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化状态栏
     */
    private void initSysBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            setTranslucentStatus(true);
            tintManager.setStatusBarLightMode(this, true);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms.size() == REQUEST_PERMISSIONS.length) {
            //有权限未授予
            if (needGo) {
                goIndex();
            }
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
        ToastUtils.showToast(this, message);
    }

    @Override
    public void getAdSuccess(String adId, String name, String imgUrl, String adUrl) {
        this.adId = adId;
        title = name;
        this.adUrl = adUrl;
        SPCacheUtils.put("adImg", imgUrl);
        DisplayImageUtils.formatImgDownload(this, imgUrl, iv_splash);
    }

    protected void startTimer() {
        countDownTimer = new CountDownTimer(3500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tv_go.setText(String.format(getString(R.string.ad_loop), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tv_go.setText(String.format(getString(R.string.ad_loop), 0));
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

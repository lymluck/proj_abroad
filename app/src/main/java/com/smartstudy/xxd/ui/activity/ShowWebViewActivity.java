package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.listener.OnWebCommonListener;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.fragment.MyWebviewFragment;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.R;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;

@Route("showWebView")
public class ShowWebViewActivity extends UIActivity implements OnWebCommonListener {

    private TextView topdefault_centertitle;
    private ProgressBar progressbar;

    private String mTitle;
    private boolean use_titile;
    private MyWebviewFragment webViewFragment;
    private boolean isTrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_show_webview);
        addActivity(this);
    }

    @Override
    protected void onDestroy() {
        releaseRes();
        super.onDestroy();
        removeActivity(ShowWebViewActivity.class.getSimpleName());
    }

    private void releaseRes() {
        if (webViewFragment != null) {
            webViewFragment = null;
        }
    }

    @Override
    protected void initViewAndData() {
        topdefault_centertitle = (TextView) findViewById(R.id.topdefault_centertitle);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        webViewFragment = new MyWebviewFragment();
        Intent intent = getIntent();
        use_titile = intent.getBooleanExtra("use_title", false);
        mTitle = intent.getStringExtra("title");
        if (use_titile) {
            topdefault_centertitle.setText(mTitle);
        }
        Bundle data = new Bundle();
        data.putString("web_url", intent.getStringExtra("web_url"));
        data.putString("url_action", intent.getStringExtra("url_action"));
        data.putString("postData", intent.getStringExtra("postData"));
        webViewFragment.setArguments(data);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flyt_body, webViewFragment);
        transaction.commit();
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        if (id == R.id.topdefault_leftbutton) {
            handleGoBack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (keyCoder == KeyEvent.KEYCODE_BACK) {
            handleGoBack();
        }
        return false;
    }

    public void handleGoBack() {
        WebView mWebView = webViewFragment.getWebView();
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.goBack();
        } else {
            Intent data = new Intent();
            data.putExtra("isTrue", isTrue);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void showWebView(String webUrl) {
        Intent toMoreDetails = new Intent(this, ShowWebViewActivity.class);
        toMoreDetails.putExtra("web_url", webUrl);
        toMoreDetails.putExtra("url_action", "get");
        startActivity(toMoreDetails);
    }

    @Override
    public void toAddQa() {
        startActivity(new Intent(this, AddQuestionActivity.class));
    }

    @Override
    public void toTestRate(Bundle bundle) {
        Intent toTest = new Intent(this, SrtRateTestActivity.class);
        toTest.putExtras(bundle);
        startActivity(toTest);
    }

    @Override
    public void handleSchool(boolean isTrue) {
        this.isTrue = isTrue;
    }

    @Override
    public void handleTitle(String title) {
        //设置标题
        if (!use_titile) {
            if (TextUtils.isEmpty(title)) {
                if (TextUtils.isEmpty(mTitle)) {
                    topdefault_centertitle.setText("详情");
                } else {
                    topdefault_centertitle.setText(mTitle);
                }
            } else {
                topdefault_centertitle.setText(title);
            }
        } else {
            if (TextUtils.isEmpty(mTitle)) {
                topdefault_centertitle.setText("详情");
            } else {
                topdefault_centertitle.setText(mTitle);
            }
        }
    }

    @Override
    public void onProgress(int newProgress) {
        if (newProgress < 100) {
            progressbar.setProgress(newProgress);
        } else {
            progressbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void goMQ(HashMap<String, String> clientInfo) {
        Intent intent = new MQIntentBuilder(ShowWebViewActivity.this)
                .setClientInfo(clientInfo)
                .build();
        startActivity(intent);
    }

    @Override
    public void goCommandSchool(String id, String name) {
        startActivity(new Intent(this, CommandSchoolListActivity.class)
                .putExtra("id", id)
                .putExtra("title", name + "推荐院校"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_LOGIN:
                webViewFragment.refreshWebView();
                break;
            default:
                break;
        }
    }
}

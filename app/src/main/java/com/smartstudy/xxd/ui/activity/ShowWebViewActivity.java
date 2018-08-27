package com.smartstudy.xxd.ui.activity;

import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.base.listener.OnWebCommonListener;
import com.smartstudy.commonlib.base.listener.ShareListener;
import com.smartstudy.commonlib.ui.activity.ChooseListActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.ui.fragment.MyWebviewFragment;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.sdk.utils.UMShareUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.PersonalInfo;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;

import static com.smartstudy.xxd.utils.AppContants.TITLE;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

@Route("showWebView")
public class ShowWebViewActivity extends BaseActivity implements OnWebCommonListener {
    private TextView topTitle;
    private ImageView topRightBtn;
    private ProgressBar progressbar;
    private String mTitle;
    private boolean useTitile;
    private MyWebviewFragment webViewFragment;
    private boolean isTrue;
    private String url;
    private String title;
    private String text;
    private String imgUrl;
    private String webUrl;
    private String from;
    private String targetCountryInfo;
    private AppBasicDialog appBasicDialog;
    private ImageView ivBack;

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
        Intent intent = getIntent();
        View decorView = getWindow().getDecorView();
        // 此处的控件ID可以使用界面当中的指定的任意控件
        View contentView = findViewById(R.id.show_webview);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));
        topRightBtn = findViewById(R.id.topdefault_rightbutton);
        if (intent.getBooleanExtra("showMenu", true)) {
            topRightBtn.setVisibility(View.VISIBLE);
        }
        topTitle = findViewById(R.id.topdefault_centertitle);
        progressbar = findViewById(R.id.progressbar);
        webViewFragment = new MyWebviewFragment();
        String flag = intent.getStringExtra("from");
        targetCountryInfo = (String) SPCacheUtils.get("target_countryInfo", "");
        if ("homeTabNews".equals(flag)) {
            UApp.actionEvent(this, "18_B_news_detail");
        }
        ivBack = findViewById(R.id.topdefault_leftbutton);
        ivBack.setImageResource(R.drawable.ic_finish_page);
        useTitile = intent.getBooleanExtra("use_title", false);
        from = intent.getStringExtra("from");
        mTitle = intent.getStringExtra(TITLE);
        topTitle.setText(mTitle);
        Bundle data = new Bundle();
        webUrl = intent.getStringExtra(WEBVIEW_URL);
        data.putString(WEBVIEW_URL, webUrl);
        data.putString(WEBVIEW_ACTION, intent.getStringExtra(WEBVIEW_ACTION));
        data.putString("postData", intent.getStringExtra("postData"));
        data.putString("from", flag);
        webViewFragment.setArguments(data);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flyt_body, webViewFragment);
        transaction.commit();
    }

    @Override
    public void initEvent() {
        ivBack.setOnClickListener(this);
        topRightBtn.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        int id = v.getId();
        if (id == R.id.topdefault_leftbutton) {
            if ("first_page".equals(from)) {
                showNormalDialog();
            } else {
                finish();
            }
        } else if (id == R.id.topdefault_rightbutton) {
            if (TextUtils.isEmpty(url)) {
                UMShareUtils.showWebShare(this, webUrl, topTitle.getText().toString(),
                    "点击查看更多详情", null, new ShareListener(webUrl, "h5_xxd"));
            } else {
                UMShareUtils.showWebShare(this, url, title, text, imgUrl, new ShareListener(webUrl, "h5_xxd"));
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCoder, KeyEvent event) {
        if (keyCoder == KeyEvent.KEYCODE_BACK) {
            if ("first_page".equals(from)) {
                showNormalDialog();
            } else {
                handleGoBack();
            }
        }
        return false;
    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
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
        toMoreDetails.putExtra(WEBVIEW_URL, webUrl);
        toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
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
        if (!useTitile) {
            if (TextUtils.isEmpty(title)) {
                if (TextUtils.isEmpty(mTitle)) {
                    topTitle.setText("详情");
                } else {
                    topTitle.setText(mTitle);
                }
            } else {
                topTitle.setText(title);
            }
        } else {
            if (TextUtils.isEmpty(mTitle)) {
                topTitle.setText("详情");
            } else {
                topTitle.setText(mTitle);
            }
        }
    }

    @Override
    public void toShowShare(boolean showShare, String url, String title, String text, String img_url) {
        if (!showShare) {
            topRightBtn.setVisibility(View.GONE);
        }
        this.url = url;
        this.title = title;
        this.text = text;
        this.imgUrl = img_url;
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
            .putExtra(TITLE, name + "推荐院校"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //涉及到分享时必须调用到方法
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

    private void showNormalDialog() {
        if (appBasicDialog == null) {
            appBasicDialog = DialogCreator.createAppBasicDialog(this, "", "要修改目前的意向国家吗?", "修改", "不修改", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.positive_btn:
                            appBasicDialog.dismiss();
                            if (!TextUtils.isEmpty(from) && from.equals("first_page")) {
                                PersonalInfo.TargetSectionEntity.TargetCountryEntity targetCountryEntity = JSON.parseObject(targetCountryInfo, PersonalInfo.TargetSectionEntity.TargetCountryEntity.class);
                                if (targetCountryEntity != null) {
                                    Intent toChooseIntent = new Intent(ShowWebViewActivity.this, ChooseListActivity.class);
                                    toChooseIntent.putExtra("value", targetCountryEntity.getName());
                                    toChooseIntent.putParcelableArrayListExtra("list", targetCountryEntity.getOptions());
                                    toChooseIntent.putExtra("ischange", true);
                                    toChooseIntent.putExtra("from", ParameterUtils.TYPE_OPTIONS_COURTY);
                                    toChooseIntent.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_INTENT);
                                    toChooseIntent.putExtra(TITLE, "修改意向国家");
                                    startActivity(toChooseIntent);
                                    handleGoBack();
                                }
                            }
                            break;
                        case R.id.negative_btn:
                            appBasicDialog.dismiss();
                            handleGoBack();
                            break;
                        default:
                            break;
                    }
                }
            });
            appBasicDialog.setCanceledOnTouchOutside(false);
            appBasicDialog.show();
        } else {
            appBasicDialog.show();
        }
    }

}

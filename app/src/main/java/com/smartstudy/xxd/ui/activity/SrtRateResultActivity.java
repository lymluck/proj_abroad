package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.commonlib.base.listener.ShareListener;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.customview.CircleProgressView;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.sdk.utils.UMShareUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.SmartChooseInfo;
import com.smartstudy.xxd.mvp.contract.SrtRateResultContract;
import com.smartstudy.xxd.mvp.presenter.SrtRateResultPresenter;
import com.smartstudy.xxd.utils.AppContants;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;

import static com.smartstudy.xxd.utils.AppContants.USER_ACCOUNT;
import static com.smartstudy.xxd.utils.AppContants.USER_NAME;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

public class SrtRateResultActivity extends BaseActivity implements SrtRateResultContract.View {
    private SrtRateResultContract.Presenter rltP;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srt_rate_result);
    }

    @Override
    protected void initViewAndData() {
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("录取率测试结果");
        Bundle bundle = getIntent().getExtras();
        ImageView iv_logo = (ImageView) findViewById(R.id.iv_school_logo);
        TextView tv_chineseName = (TextView) findViewById(R.id.tv_chineseName);
        TextView tv_englishName = (TextView) findViewById(R.id.tv_englishName);
        TextView tv_for_you = (TextView) findViewById(R.id.tv_for_you);
        DisplayImageUtils.formatCircleImgUrl(this, bundle.getString("logoUrl"), iv_logo);
        tv_chineseName.setText(bundle.getString("chineseName"));
        tv_englishName.setText(bundle.getString("englishName"));
        tv_for_you.setText(String.format(getString(R.string.for_you), bundle.getString("chineseName")));
        id = bundle.getString("id");
        new SrtRateResultPresenter(this, SrtRateResultActivity.this);
        SmartChooseInfo info = bundle.getParcelable("params");
        rltP.getTestResult(id, info, bundle.getString("chineseName"));
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.btn_rst_share).setOnClickListener(this);
        findViewById(R.id.btn_rst_ask).setOnClickListener(this);
        findViewById(R.id.tv_eg_help).setOnClickListener(this);
        findViewById(R.id.tv_test_help).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.tv_eg_help:
                openUrlWithWebView(((TextView) v).getText().toString() + "?hmsr=xxd");
                break;
            case R.id.tv_test_help:
                openUrlWithWebView(((TextView) v).getText().toString() + "?hmsr=xxd");
                break;
            case R.id.btn_rst_share:
                //分享
                String url = HttpUrlUtils.getWebUrl(String.format(HttpUrlUtils.WEBURL_RESULT_RATE, id));
                UMShareUtils.showShare(this, url,
                    "录取率测试结果", "点击查看详细内容", null, new ShareListener(url, "result_smart_rate"));
                break;
            case R.id.btn_rst_ask:
                HashMap<String, String> clientInfo = new HashMap<>();
                clientInfo.put(AppContants.NAME, (String) SPCacheUtils.get(USER_NAME, ""));
                clientInfo.put(AppContants.TEL, (String) SPCacheUtils.get(USER_ACCOUNT, ""));
                Intent intent = new MQIntentBuilder(this)
                    .setClientInfo(clientInfo)
                    .build();
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(SrtRateResultContract.Presenter presenter) {
        if (presenter != null) {
            rltP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void showResultMsg(String msg, int color, int rating) {
        TextView tv_rate_msg = (TextView) findViewById(R.id.tv_rate_msg);
        tv_rate_msg.setTextColor(getResources().getColor(color));
        tv_rate_msg.setText(msg);
        RatingBar rb_rate = (RatingBar) findViewById(R.id.rb_rate);
        rb_rate.setRating(rating);
    }

    @Override
    public void showRate(double avgRate, double userRate) {
        CircleProgressView cpv_avg_rate = (CircleProgressView) findViewById(R.id.cpv_avg_rate);
        cpv_avg_rate.setProgress(avgRate);
        cpv_avg_rate.setProgressColor(getResources().getColor(R.color.app_main_color));
        cpv_avg_rate.setmTxtHint("平均录取率");
        CircleProgressView cpv_user_rate = (CircleProgressView) findViewById(R.id.cpv_user_rate);
        cpv_user_rate.setProgress(userRate);
        cpv_user_rate.setProgressColor(getResources().getColor(R.color.user_rate_clor));
        cpv_user_rate.setmTxtHint("你的录取率");
    }

    @Override
    public void showEgSug(Spanned userScore, Spanned sug, String type, boolean showWebsite) {
        findViewById(R.id.view_test_line).setVisibility(View.VISIBLE);
        TextView tv_eg_score = (TextView) findViewById(R.id.tv_eg_score);
        tv_eg_score.setVisibility(View.VISIBLE);
        tv_eg_score.setText(userScore);
        TextView tv_eg_msg = (TextView) findViewById(R.id.tv_eg_msg);
        tv_eg_msg.setVisibility(View.VISIBLE);
        tv_eg_msg.setText(sug);
        if (showWebsite) {
            TextView tv_eg_help = (TextView) findViewById(R.id.tv_eg_help);
            tv_eg_help.setVisibility(View.VISIBLE);
            tv_eg_help.setText(String.format(getString(R.string.score_website), type));
        }
    }

    @Override
    public void showTestSug(Spanned userScore, Spanned sug, String type, boolean showWebsite) {
        TextView tv_test_score = (TextView) findViewById(R.id.tv_test_score);
        tv_test_score.setVisibility(View.VISIBLE);
        tv_test_score.setText(userScore);
        TextView tv_test_msg = (TextView) findViewById(R.id.tv_test_msg);
        tv_test_msg.setVisibility(View.VISIBLE);
        tv_test_msg.setText(sug);
        if (showWebsite) {
            TextView tv_test_help = (TextView) findViewById(R.id.tv_test_help);
            tv_test_help.setVisibility(View.VISIBLE);
            tv_test_help.setText(String.format(getString(R.string.score_website), type));
        }
    }

    private void openUrlWithWebView(String url) {
        Intent toMoreDetails = new Intent(SrtRateResultActivity.this, ShowWebViewActivity.class);
        toMoreDetails.putExtra(WEBVIEW_URL, url);
        toMoreDetails.putExtra(WEBVIEW_ACTION, "get");
        startActivity(toMoreDetails);
    }

    /**
     * 涉及到分享时必须调用到方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}

package com.smartstudy.xxd.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.router.Router;
import com.smartstudy.xxd.R;

@Route("CompareCountryActivity")
public class CompareCountryActivity extends UIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_country);
        StatisticUtils.actionEvent(this,"5_B_compare_countries");
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("意向国家对比");
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        findViewById(R.id.card_gz).setOnClickListener(this);
        findViewById(R.id.card_bk).setOnClickListener(this);
        findViewById(R.id.card_yjs).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            case R.id.card_gz:
                Bundle gz_data = new Bundle();
                gz_data.putString("web_url", HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_INTENT_HIGH));
                gz_data.putString("title", "高中留学国家对比");
                gz_data.putString("url_action", "get");
                gz_data.putBoolean("use_title", true);
                Router.build("showWebView").with(gz_data).go(CompareCountryActivity.this);
                break;
            case R.id.card_bk:
                Bundle bk_data = new Bundle();
                bk_data.putString("web_url", HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_INTENT_UNDER));
                bk_data.putString("title", "本科留学国家对比");
                bk_data.putString("url_action", "get");
                bk_data.putBoolean("use_title", true);
                Router.build("showWebView").with(bk_data).go(CompareCountryActivity.this);
                break;
            case R.id.card_yjs:
                Bundle yjs_data = new Bundle();
                yjs_data.putString("web_url", HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_INTENT_GRADUATE));
                yjs_data.putString("title", "研究生留学国家对比");
                yjs_data.putString("url_action", "get");
                yjs_data.putBoolean("use_title", true);
                Router.build("showWebView").with(yjs_data).go(CompareCountryActivity.this);
                break;
            default:
                break;
        }
    }
}

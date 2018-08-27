package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.ui.activity.ChooseListActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.router.Router;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.PersonalInfo;

import static com.smartstudy.xxd.utils.AppContants.TITLE;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

@Route("CompareCountryActivity")
public class CompareCountryActivity extends BaseActivity {
    private String from;
    private String targetCountryInfo;
    private AppBasicDialog appBasicDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_country);
        UApp.actionEvent(this, "5_B_compare_countries");
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("意向国家对比");
        from = getIntent().getStringExtra("from");
        targetCountryInfo = (String) SPCacheUtils.get("target_countryInfo", "");
    }

    @Override
    public void initEvent() {
        int screenWidth = ScreenUtils.getScreenWidth();
        int ivWidth = screenWidth - 2 * DensityUtils.dip2px(8f);
        int ivHeight = ivWidth * 12 / 25;
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
        ImageView cardGz = findViewById(R.id.card_gz);
        cardGz.setImageResource(R.drawable.ic_high_school_compare);
        LinearLayout.LayoutParams gzParams = (LinearLayout.LayoutParams) cardGz.getLayoutParams();
        gzParams.width = ivWidth;
        gzParams.height = ivHeight;
        cardGz.setLayoutParams(gzParams);
        ImageView cardBk = findViewById(R.id.card_bk);
        cardBk.setImageResource(R.drawable.ic_benke_compare);
        LinearLayout.LayoutParams bkParams = (LinearLayout.LayoutParams) cardBk.getLayoutParams();
        bkParams.width = ivWidth;
        bkParams.height = ivHeight;
        cardBk.setLayoutParams(bkParams);
        ImageView cardYjs = findViewById(R.id.card_yjs);
        cardYjs.setImageResource(R.drawable.ic_yjs_compare);
        LinearLayout.LayoutParams yjsParams = (LinearLayout.LayoutParams) cardYjs.getLayoutParams();
        yjsParams.width = ivWidth;
        yjsParams.height = ivHeight;
        cardYjs.setLayoutParams(yjsParams);
        cardGz.setOnClickListener(this);
        cardBk.setOnClickListener(this);
        cardYjs.setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                if ("first_page".equals(from)) {
                    showNormalDialog();
                } else {
                    finish();
                }
                break;
            case R.id.card_gz:
                Bundle gz_data = new Bundle();
                gz_data.putString(WEBVIEW_URL, HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_INTENT_HIGH));
                gz_data.putString(TITLE, "高中留学国家对比");
                gz_data.putString(WEBVIEW_ACTION, "get");
                gz_data.putBoolean("use_title", true);
                Router.build("showWebView").with(gz_data).go(CompareCountryActivity.this);
                break;
            case R.id.card_bk:
                Bundle bk_data = new Bundle();
                bk_data.putString(WEBVIEW_URL, HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_INTENT_UNDER));
                bk_data.putString(TITLE, "本科留学国家对比");
                bk_data.putString(WEBVIEW_ACTION, "get");
                bk_data.putBoolean("use_title", true);
                Router.build("showWebView").with(bk_data).go(CompareCountryActivity.this);
                break;
            case R.id.card_yjs:
                Bundle yjs_data = new Bundle();
                yjs_data.putString(WEBVIEW_URL, HttpUrlUtils.getWebUrl(HttpUrlUtils.WEBURL_INTENT_GRADUATE));
                yjs_data.putString(TITLE, "研究生留学国家对比");
                yjs_data.putString(WEBVIEW_ACTION, "get");
                yjs_data.putBoolean("use_title", true);
                Router.build("showWebView").with(yjs_data).go(CompareCountryActivity.this);
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
                            PersonalInfo.TargetSectionEntity.TargetCountryEntity targetCountryEntity = JSON.parseObject(targetCountryInfo, PersonalInfo.TargetSectionEntity.TargetCountryEntity.class);
                            if (targetCountryEntity != null) {
                                Intent toChooseIntent = new Intent(CompareCountryActivity.this, ChooseListActivity.class);
                                toChooseIntent.putExtra("value", targetCountryEntity.getName());
                                toChooseIntent.putParcelableArrayListExtra("list", targetCountryEntity.getOptions());
                                toChooseIntent.putExtra("ischange", true);
                                toChooseIntent.putExtra("from", ParameterUtils.TYPE_OPTIONS_COURTY);
                                toChooseIntent.putExtra(ParameterUtils.TRANSITION_FLAG, ParameterUtils.EDIT_INTENT);
                                toChooseIntent.putExtra(TITLE, "修改意向国家");
                                startActivity(toChooseIntent);
                                finish();
                            }
                            break;
                        case R.id.negative_btn:
                            appBasicDialog.dismiss();
                            finish();
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


    @Override
    public void onBackPressed() {
        if (("first_page".equals(from))) {
            showNormalDialog();
        } else {
            finish();
        }
    }

}

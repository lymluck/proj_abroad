package com.smartstudy.xxd.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.customview.PagerSlidingTabStrip;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CountryTypeInfo;
import com.smartstudy.xxd.ui.adapter.XxdViewPagerFragmentAdapter;
import com.smartstudy.xxd.ui.fragment.HotSchoolRankFragment;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

/**
 * @author yqy
 * @date on 2018/6/12
 * @describe 热门院校排行
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HotSchoolRankActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_school_rank);
    }

    @Override
    protected void initViewAndData() {
        RelativeLayout countryRank = findViewById(R.id.top_country);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) countryRank.getLayoutParams();
            params.height = params.height + ScreenUtils.getStatusHeight();
            countryRank.setLayoutParams(params);
            countryRank.setPadding(0, ScreenUtils.getStatusHeight(), 0, 0);
        }
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText(getIntent().getStringExtra(TITLE));
        ViewPager pagerCountry = findViewById(R.id.pager_country);
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        List<CountryTypeInfo> countryTypeInfos = JSON.parseArray(
            Utils.getJson("json/home_school_title.json"), CountryTypeInfo.class);
        List<String> titles = new ArrayList<>();
        List<String> eventIds = new ArrayList<>();
        for (CountryTypeInfo info : countryTypeInfos) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(TITLE, info);
            titles.add(info.getCountryName());
            eventIds.add(info.getEventId());
            fragments.add(HotSchoolRankFragment.getInstance(bundle));
        }
        pagerCountry.setAdapter(new XxdViewPagerFragmentAdapter(getSupportFragmentManager(), titles, eventIds, fragments));
        pagerCountry.setOffscreenPageLimit(4);
        PagerSlidingTabStrip countryTabs = findViewById(R.id.tabs_country);
        countryTabs.setViewPager(pagerCountry);
        pagerCountry.setCurrentItem(0);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;

        }

    }
}

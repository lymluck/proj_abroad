package com.smartstudy.xxd.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.customview.PagerSlidingTabStrip;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.ui.adapter.XxdViewPagerFragmentAdapter;
import com.smartstudy.xxd.ui.fragment.ActivityListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityCenterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("活动库");
        ViewPager pagerActivities = findViewById(R.id.vp_activity);
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        List<String> titles = Arrays.asList("工科", "商科", "理科", "文科", "其他");
        for (String title : titles) {
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            fragments.add(ActivityListFragment.getInstance(bundle));
        }
        pagerActivities.setAdapter(new XxdViewPagerFragmentAdapter(getSupportFragmentManager(), titles, fragments));
        pagerActivities.setOffscreenPageLimit(5);
        PagerSlidingTabStrip activityTabs = findViewById(R.id.activity_tabs);
        activityTabs.setViewPager(pagerActivities);
        pagerActivities.setCurrentItem(0);
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

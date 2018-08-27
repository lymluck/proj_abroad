package com.smartstudy.xxd.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.customview.PagerSlidingTabStrip;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.MajorProgramInfo;
import com.smartstudy.xxd.mvp.contract.ProgramListContract;
import com.smartstudy.xxd.mvp.presenter.ProgramListPresenter;
import com.smartstudy.xxd.ui.adapter.XxdViewPagerFragmentAdapter;
import com.smartstudy.xxd.ui.fragment.MajorProgramListFragment;

import java.util.ArrayList;
import java.util.List;

public class MajorProgramListActivity extends BaseActivity implements ProgramListContract.View {

    private ProgramListContract.Presenter presenter;
    private List<BaseFragment> fragments;
    private List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_program_list);
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("热门专业");
        new ProgramListPresenter(this);
        presenter.getProgramList();
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

    @Override
    public void showData(List<MajorProgramInfo> datas) {
        findViewById(R.id.ll_tab).setVisibility(View.VISIBLE);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        for (MajorProgramInfo info : datas) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("datas", info.getDirections());
            titles.add(info.getName());
            fragments.add(MajorProgramListFragment.getInstance(bundle));
        }
        ViewPager pagerMajors = findViewById(R.id.pager_major);
        pagerMajors.setAdapter(new XxdViewPagerFragmentAdapter(getSupportFragmentManager(), titles, null, fragments));
        PagerSlidingTabStrip majorTabs = findViewById(R.id.tabs_majors);
        majorTabs.setViewPager(pagerMajors);
        pagerMajors.setCurrentItem(0);
    }

    @Override
    public void setPresenter(ProgramListContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }
}

package com.smartstudy.xxd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HotMajorInfo;
import com.smartstudy.xxd.mvp.contract.FindMajorContract;
import com.smartstudy.xxd.mvp.presenter.FindMajorPresenter;
import com.smartstudy.xxd.ui.adapter.hotmajor.FindMajorAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.TITLE;

public class FindMajorActivity extends BaseActivity implements FindMajorContract.View {

    private RecyclerView rvMajor;
    private FindMajorAdapter mAdapter;
    private HeaderAndFooterWrapper mHeader;

    private List<HotMajorInfo> hotMajorInfos;
    private FindMajorContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_major);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (hotMajorInfos != null) {
            hotMajorInfos.clear();
            hotMajorInfos = null;
        }
        if (mHeader != null) {
            mHeader = null;
        }
    }

    @Override
    protected void initViewAndData() {
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("找专业");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        hotMajorInfos = new ArrayList<>();
        mAdapter = new FindMajorAdapter(this, hotMajorInfos, mInflater);
        mHeader = new HeaderAndFooterWrapper(mAdapter);
        View headView = mInflater.inflate(R.layout.header_find_major, null, false);
        mHeader.addHeaderView(headView);
        initHead(headView);
        final GridLayoutManager manager = new GridLayoutManager(this, 3);
        // 设置span
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.isSectionHeaderPosition(position - mHeader.getHeadersCount())
                    || mAdapter.isSectionFooterPosition(position - mHeader.getHeadersCount())) {
                    return manager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        manager.setOrientation(GridLayoutManager.VERTICAL);
        rvMajor = findViewById(R.id.rv_major);
        rvMajor.setHasFixedSize(true);
        rvMajor.setLayoutManager(manager);
        rvMajor.setNestedScrollingEnabled(false);
        rvMajor.setAdapter(mHeader);
        new FindMajorPresenter(this);
        presenter.getHotMajor();
    }

    private void initHead(View headView) {
        int screenW = ScreenUtils.getScreenWidth();
        int marginlf = (screenW - DensityUtils.dip2px(50f) * 4) / 8;
        LinearLayout llGlobal = headView.findViewById(R.id.ll_global);
        LinearLayout.LayoutParams globalLayoutParams = (LinearLayout.LayoutParams) llGlobal.getLayoutParams();
        globalLayoutParams.leftMargin = marginlf;
        globalLayoutParams.rightMargin = marginlf;
        llGlobal.setLayoutParams(globalLayoutParams);
        llGlobal.setOnClickListener(this);
        LinearLayout llUs = headView.findViewById(R.id.ll_us);
        LinearLayout.LayoutParams usLayoutParams = (LinearLayout.LayoutParams) llUs.getLayoutParams();
        usLayoutParams.leftMargin = marginlf;
        usLayoutParams.rightMargin = marginlf;
        llUs.setLayoutParams(usLayoutParams);
        llUs.setOnClickListener(this);
        LinearLayout llYjs = headView.findViewById(R.id.ll_yjs);
        LinearLayout.LayoutParams yjsLayoutParams = (LinearLayout.LayoutParams) llYjs.getLayoutParams();
        int diff = DensityUtils.dip2px(10.5f);
        yjsLayoutParams.leftMargin = marginlf - diff;
        yjsLayoutParams.rightMargin = marginlf - diff;
        llYjs.setLayoutParams(yjsLayoutParams);
        llYjs.setOnClickListener(this);
        LinearLayout llArt = headView.findViewById(R.id.ll_art);
        LinearLayout.LayoutParams artLayoutParams = (LinearLayout.LayoutParams) llArt.getLayoutParams();
        artLayoutParams.leftMargin = marginlf;
        artLayoutParams.rightMargin = marginlf;
        llUs.setLayoutParams(artLayoutParams);
        llArt.setOnClickListener(this);
        headView.findViewById(R.id.cv_smart_major).setOnClickListener(this);
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
            case R.id.ll_global:
                startActivity(new Intent(this, HotRankActivity.class)
                    .putExtra("flag", "world")
                    .putExtra(TITLE, "全球专业排名"));
                break;
            case R.id.ll_us:
                startActivity(new Intent(this, HotRankActivity.class)
                    .putExtra("flag", "us")
                    .putExtra(TITLE, "美国专业排名"));
                break;
            case R.id.ll_yjs:
                startActivity(new Intent(this, HotRankActivity.class)
                    .putExtra("flag", "yjs")
                    .putExtra(TITLE, "美国研究生专业排名"));
                break;
            case R.id.ll_art:
                startActivity(new Intent(this, HotRankActivity.class)
                    .putExtra("flag", "art")
                    .putExtra(TITLE, "艺术专业"));
                break;
            case R.id.cv_smart_major:
                UApp.actionEvent(this, "8_A_choose_professional_btn");
                startActivity(new Intent(this, SrtChooseSpecialActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void showHotMajor(List<HotMajorInfo> datas) {
        if (datas != null && hotMajorInfos != null) {
            hotMajorInfos.clear();
            hotMajorInfos.addAll(datas);
            mAdapter.notifyDataSetChanged();
            mHeader.notifyDataSetChanged();
        }
    }

    @Override
    public void setPresenter(FindMajorContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(message);
    }
}

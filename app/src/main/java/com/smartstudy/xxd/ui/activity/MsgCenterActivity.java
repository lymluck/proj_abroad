package com.smartstudy.xxd.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartstudy.annotation.Route;
import com.smartstudy.commonlib.entity.DeviceMsgInfo;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.adapter.wrapper.HeaderAndFooterWrapper;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.utils.DensityUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.MsgCenterContract;
import com.smartstudy.xxd.mvp.presenter.MsgCenterPresenter;
import com.smartstudy.xxd.ui.adapter.MsgCenterAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

@Route("xuanxiaodi://notifications")
public class MsgCenterActivity extends UIActivity implements MsgCenterContract.View {

    private RecyclerView rclvmsg;
    private SwipeRefreshLayout srltmsg;
    private MsgCenterAdapter mAdapter;
    private EmptyWrapper<DeviceMsgInfo> emptyWrapper;
    private HeaderAndFooterWrapper mHeader;
    private NoScrollLinearLayoutManager mLayoutManager;
    private View emptyView;
    private View header;

    private int mPage = 1;
    private MsgCenterContract.Presenter presenter;
    private ArrayList<DeviceMsgInfo> msgInfos;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_center);
        addActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter = null;
        }
        if (rclvmsg != null) {
            rclvmsg.removeAllViews();
            rclvmsg = null;
        }
        if (mAdapter != null) {
            mAdapter = null;
        }
        if (msgInfos != null) {
            msgInfos.clear();
            msgInfos = null;
        }
        super.onDestroy();
        removeActivity(SrtSchoolResultActivity.class.getSimpleName());
    }

    @Override
    protected void initViewAndData() {
        //进入页面，消息未读置为0
        ((TextView) findViewById(R.id.topdefault_centertitle)).setText("消息中心");
        findViewById(R.id.top_line).setVisibility(View.VISIBLE);
        this.srltmsg = (SwipeRefreshLayout) findViewById(R.id.srlt_msg);
        srltmsg.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        srltmsg.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = mPage + 1;
                presenter.getMsg(mPage);

            }
        });
        this.rclvmsg = (RecyclerView) findViewById(R.id.rclv_msg);
        rclvmsg.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclvmsg.setLayoutManager(mLayoutManager);
        initAdapter();
        new MsgCenterPresenter(this);
        //加载动画
        emptyView = mInflater.inflate(R.layout.layout_empty, rclvmsg, false);
        presenter.showLoading(this, emptyView);
        presenter.getMsg(mPage);
    }

    private void initAdapter() {
        msgInfos = new ArrayList<>();
        mAdapter = new MsgCenterAdapter(this, msgInfos);
        mHeader = new HeaderAndFooterWrapper(mAdapter);
        header = new View(this);
        header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(24)));
        emptyWrapper = new EmptyWrapper<>(mHeader);
        rclvmsg.setAdapter(emptyWrapper);
    }

    @Override
    public void initEvent() {
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.topdefault_leftbutton:
                JPushInterface.clearAllNotifications(this);
                SPCacheUtils.put("xxd_unread", 0);
                finishAll();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        JPushInterface.clearAllNotifications(this);
        SPCacheUtils.put("xxd_unread", 0);
        finishAll();
    }

    @Override
    public void setPresenter(MsgCenterContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (presenter != null) {
            srltmsg.setRefreshing(false);
            ToastUtils.showToast(this, message);
        }
    }

    @Override
    public void showMsgs(List<DeviceMsgInfo> data) {
        if (presenter != null) {
            presenter.setEmptyView(emptyView);
            srltmsg.setRefreshing(false);
            mLayoutManager.setScrollEnabled(true);
            int len = data.size();
            if (len <= 0) {
                //没有更多内容
                if (mPage > 1) {
                    mPage = mPage - 1;
                }
            } else {
                msgInfos.addAll(data);
                if (isFirst) {
                    isFirst = false;
                    mHeader.addHeaderView(header);
                }
                emptyWrapper.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        srltmsg.setRefreshing(false);
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CARD_QA:
                mPage = 1;
                msgInfos.clear();
                presenter.getMsg(mPage);
                break;
            default:
                break;
        }
    }
}

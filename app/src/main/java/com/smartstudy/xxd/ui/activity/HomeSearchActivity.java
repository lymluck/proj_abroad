package com.smartstudy.xxd.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.ui.adapter.wrapper.EmptyWrapper;
import com.smartstudy.commonlib.ui.customview.EditTextWithClear;
import com.smartstudy.commonlib.ui.customview.NoScrollLinearLayoutManager;
import com.smartstudy.commonlib.utils.KeyBoardUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HomeSearchListInfo;
import com.smartstudy.xxd.mvp.contract.HomeSearchContract;
import com.smartstudy.xxd.mvp.presenter.HomeSearchPresenter;
import com.smartstudy.xxd.ui.adapter.homesearch.HomeSearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeSearchActivity extends UIActivity implements HomeSearchContract.View {

    private EditTextWithClear searchView;
    private RecyclerView rclv_search;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NoScrollLinearLayoutManager mLayoutManager;
    private LinearLayout llyt_search;
    private HomeSearchAdapter mAdapter;
    private EmptyWrapper<SchoolInfo> emptyWrapper;

    private HomeSearchContract.Presenter searchP;
    private String keyword;
    private List<HomeSearchListInfo> resultList;
    private static int spaceTime = 300;//时间间隔
    private static long lastSearchTime = 0;//上次搜索的时间
    private WeakHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusColor(R.color.white);//修改状态栏颜色
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);
        KeyBoardUtils.openKeybord(searchView.getMyEditText(), this);
    }

    @Override
    protected void onDestroy() {
        if (searchP != null) {
            searchP = null;
        }
        if (rclv_search != null) {
            rclv_search.removeAllViews();
            rclv_search = null;
        }
        if (mAdapter != null) {
            mAdapter.destroy();
            mAdapter = null;
        }
        if (resultList != null) {
            resultList.clear();
            resultList = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initViewAndData() {
        llyt_search = (LinearLayout) findViewById(R.id.llyt_search);
        searchView = (EditTextWithClear) findViewById(R.id.searchView);
        searchView.getMyEditText().setHint(R.string.search);
        searchView.getMyEditText().requestFocus();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srlt_search);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.app_main_color));
        rclv_search = (RecyclerView) findViewById(R.id.rclv_search);
        rclv_search.setHasFixedSize(true);
        mLayoutManager = new NoScrollLinearLayoutManager(this);
        mLayoutManager.setScrollEnabled(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rclv_search.setLayoutManager(mLayoutManager);
        mHandler = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        llyt_search.setBackgroundResource(0);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        //初始化适配器
        initAdapter();
        new HomeSearchPresenter(this);
    }

    private void initAdapter() {
        resultList = new ArrayList<>();
        mAdapter = new HomeSearchAdapter(this, resultList);
        emptyWrapper = new EmptyWrapper<>(mAdapter);
        rclv_search.setAdapter(emptyWrapper);
    }

    @Override
    public void initEvent() {
        initRefresh();
        initEditor();
        initTextWatcher();
        findViewById(R.id.tv_cancle).setOnClickListener(this);
    }

    private void initRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //这里做刷新
                searchP.getResults(keyword);
            }
        });
    }

    private void initEditor() {
        searchView.getMyEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.closeKeybord(searchView.getMyEditText(), HomeSearchActivity.this);
                    //这里做搜索
                    searchP.getResults(keyword);
                }
                return true;
            }
        });
    }

    private void initTextWatcher() {
        searchView.getMyEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
                if (s.length() > 0) {
                    //开始搜索
                    //延迟搜索，防止用户在输入过程中频繁调用搜索接口
                    long currentTime = System.currentTimeMillis();//当前系统时间
                    if (currentTime - lastSearchTime > spaceTime) {
                        searchP.getResults(keyword);
                    }
                    lastSearchTime = currentTime;
                } else {
                    //这里做清空操作
                    clearList();
                    mAdapter.notifyDataSetChanged();
                    Utils.convertActivityToTranslucent(HomeSearchActivity.this);
                    showEmptyView(null);
                    mHandler.sendEmptyMessageDelayed(1, 250);
                }
            }
        });
    }

    private void clearList() {
        if (resultList != null) {
            resultList.clear();
        }
    }

    @Override
    public void finish() {
        KeyBoardUtils.closeKeybord(searchView.getMyEditText(), this);
        super.finish();
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void setPresenter(HomeSearchContract.Presenter presenter) {
        if (presenter != null) {
            this.searchP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if (searchP != null) {
            if (ParameterUtils.RESPONE_CODE_NETERR.equals(errCode)) {
                llyt_search.setBackgroundResource(R.color.main_bg);
                searchP.setEmptyView(mInflater, this, rclv_search);
            }
            swipeRefreshLayout.setRefreshing(false);
            ToastUtils.showToast(this, message);
        }
    }

    @Override
    public void showEmptyView(View view) {
        emptyWrapper.setEmptyView(view);
        emptyWrapper.notifyDataSetChanged();
        mLayoutManager.setScrollEnabled(false);
    }

    @Override
    public void showResult(List<HomeSearchListInfo> mDatas) {
        if (searchP != null) {
            swipeRefreshLayout.setRefreshing(false);
            llyt_search.setBackgroundResource(R.color.main_bg);
            searchP.setEmptyView(mInflater, this, rclv_search);
            mLayoutManager.setScrollEnabled(true);
            clearList();
            resultList.addAll(mDatas);
            mAdapter.notifyDataSetChanged();
        }
    }
}

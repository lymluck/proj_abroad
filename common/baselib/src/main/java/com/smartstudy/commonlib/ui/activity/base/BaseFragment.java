package com.smartstudy.commonlib.ui.activity.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartstudy.commonlib.utils.SensorsUtils;

/**
 * fragment父类懒加载基本封装
 * Created by louis on 2017/2/22.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    /**
     * 标志位，标志已经初始化完成。
     */
    protected boolean isPrepared;
    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    protected BaseActivity mActivity;
    protected View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //按钮点击切换时复写该方法加上isPrepared=true
        //viewPager+fragment切换时不加
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 初始化视图和数据
        rootView = mActivity.mInflater.inflate(getLayoutResId(), null);
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView();
        return rootView;
    }

    protected abstract int getLayoutResId();

    protected abstract void initView();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
            isFirstVisible = false;
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible() {
        // fragment页面浏览
        SensorsUtils.tackAppView(getClass().getSimpleName());
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {
        // fragment页面浏览
        SensorsUtils.tackAppView(getClass().getSimpleName());
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {
    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {
    }


    @Override
    public void onClick(View v) {
    }

}

package com.smartstudy.xxd.mvp.presenter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.mvp.base.BaseModel;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.entity.VersionInfo;
import com.smartstudy.xxd.mvp.contract.MainActivityContract;

/**
 * Created by louis on 2017/3/2.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View view;
    private int currentIndex;

    public MainActivityPresenter(MainActivityContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getVersion() {
        BaseModel.getVersion(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {

                VersionInfo info = JSON.parseObject(result, VersionInfo.class);
                if (info != null) {
                    if (info.isNeedUpdate()) {
                        if (info.isForceUpdate()) {
                            view.forceUpdate(info.getPackageUrl(), info.getDescription());
                        } else {
                            view.updateable(info.getPackageUrl(), info.getDescription());
                        }
                    }
                    info = null;
                }
            }
        });
    }

    @Override
    public void showFragment(FragmentManager fragmentManager, int index) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        view.hideFragment(ft);
        //注意这里设置位置
        currentIndex = index;
        switch (index) {
            case ParameterUtils.FRAGMENT_ONE:
                view.showSchools(ft);
                break;
            case ParameterUtils.FRAGMENT_TWO:
                view.showCourse(ft);
                break;
            case ParameterUtils.FRAGMENT_THREE:
                view.showNews(ft);
                break;
            case ParameterUtils.FRAGMENT_THOUR:
                view.showQa(ft);
                break;
            case ParameterUtils.FRAGMENT_FIVE:
                view.showMe(ft);
                break;
        }
        ft.commitAllowingStateLoss();
        fragmentManager = null;
    }

    @Override
    public int currentIndex() {
        return currentIndex;
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}

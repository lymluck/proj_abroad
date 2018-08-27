package com.smartstudy.xxd.mvp.presenter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.mvp.contract.StudyConsultantsContract;

/**
 * Created by yqy on 2017/12/21.
 */

public class StudyConsultantsPresenter implements StudyConsultantsContract.Presenter {

    private StudyConsultantsContract.View view;
    private int currentIndex;

    public StudyConsultantsPresenter(StudyConsultantsContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void showFragment(FragmentManager fragmentManager, int index) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        view.hideFragment(fragmentManager);
        //注意这里设置位置
        currentIndex = index;
        switch (index) {
            case ParameterUtils.FRAGMENT_ONE:
                view.showChatListFragment(ft);
                break;
            case ParameterUtils.FRAGMENT_TWO:
                view.showConsultantsListFragment(ft);
                break;
            default:
                view.showChatListFragment(ft);
                break;
        }
        ft.commitAllowingStateLoss();
        fragmentManager = null;
        ft = null;
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

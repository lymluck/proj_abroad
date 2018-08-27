package com.smartstudy.xxd.mvp.presenter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.mvp.base.BaseModel;
import com.smartstudy.commonlib.mvp.model.PersonalInfoModel;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.xxd.entity.PersonalInfo;
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
    public void getVersion(String channel) {

        BaseModel.getVersion(channel, new BaseCallback<String>() {
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
                            view.forceUpdate(info.getPackageUrl(), info.getLatestVersion(), info.getDescription());
                        } else {
                            view.updateable(info.getPackageUrl(), info.getLatestVersion(), info.getDescription());
                        }
                    }
                    info = null;
                }
            }
        });
    }

    @Override
    public void isGetMyPlan() {
        PersonalInfoModel.getMyInfo(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                PersonalInfo personalInfo = JSON.parseObject(result, PersonalInfo.class);
                if (personalInfo != null) {
                    view.showPlanDialog(personalInfo.isAbroadPlanAvailable());
                    if (personalInfo.getAvatar() != null) {
                        String cacheUrl = Utils.getCacheUrl(personalInfo.getAvatar(), 64, 64);
                        SPCacheUtils.put("user_pic", cacheUrl);
                    }
                    SPCacheUtils.put("user_name", personalInfo.getName());
                    if (personalInfo.getTargetSection() != null) {
                        PersonalInfo.TargetSectionEntity targetSection = personalInfo.getTargetSection();
                        if (targetSection != null) {
                            if (targetSection.getTargetDegree() != null) {
                                SPCacheUtils.put("project_name", targetSection.getTargetDegree().getName());
                            }
                            if (targetSection.getTargetCountry() != null) {
                                SPCacheUtils.put("target_countryInfo", JSON.toJSONString(targetSection.getTargetCountry()));
                            }
                            targetSection = null;
                        }
                    }
                    personalInfo = null;
                }
            }
        });
    }

    @Override
    public void showFragment(FragmentManager fragmentManager, int index) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        view.hideFragment(fragmentManager);
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
            default:
                view.showSchools(ft);
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

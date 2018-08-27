package com.smartstudy.xxd.mvp.presenter;

import android.view.View;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.mvp.base.BaseModel;
import com.smartstudy.commonlib.mvp.model.PersonalInfoModel;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.xxd.entity.PersonalInfo;
import com.smartstudy.xxd.entity.VersionInfo;
import com.smartstudy.xxd.mvp.contract.MainActivityContract;
import com.smartstudy.xxd.ui.fragment.FragmentFactory;

import static com.smartstudy.xxd.utils.AppContants.USER_NAME;

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
                    view.showQaRed(personalInfo.isHasUnreadQuestions() ? View.VISIBLE : View.GONE);
                    if (personalInfo.getAvatar() != null) {
                        SPCacheUtils.put("user_pic", personalInfo.getAvatar());
                    }
                    SPCacheUtils.put(USER_NAME, personalInfo.getName());
                    if (personalInfo.getTargetSection() != null) {
                        PersonalInfo.TargetSectionEntity targetSection = personalInfo.getTargetSection();
                        if (targetSection != null) {
                            if (targetSection.getTargetDegree() != null) {
                                SPCacheUtils.put("project_name", targetSection.getTargetDegree().getName());
                            }
                            if (targetSection.getTargetCountry() != null) {
                                SPCacheUtils.put("target_countryInfo", JSON.toJSONString(targetSection.getTargetCountry()));
                            }
                            if (targetSection.getTargetMajorDirection() != null) {
                                SPCacheUtils.put("target_major", JSON.toJSONString(targetSection.getTargetMajorDirection()));
                            }
                            targetSection = null;
                        }
                    }
                    view.refreshBannerFragment(personalInfo.isAbroadPlanAvailable());
                    personalInfo = null;
                }
            }
        });
    }

    @Override
    public void showFragment(FragmentFactory fragmentFactory, int index) {
        BaseFragment fragment = fragmentFactory.createMainFragment(index);
        view.hideLastFragment(fragment);
        //注意这里设置位置
        currentIndex = index;
        view.selectedTab(index);
        view.onShowFragment(fragment);
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

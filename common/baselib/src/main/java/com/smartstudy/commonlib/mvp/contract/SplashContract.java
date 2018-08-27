package com.smartstudy.commonlib.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/4.
 */

public interface SplashContract {
    interface View extends BaseView<Presenter> {

        void getAdSuccess(String adId, String name, String imgUrl, String adUrl);
    }

    interface Presenter extends BasePresenter {

        void getAdInfo();

        void trackAd(String id);

    }
}

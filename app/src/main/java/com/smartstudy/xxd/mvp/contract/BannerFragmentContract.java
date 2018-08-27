package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.BannerInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface BannerFragmentContract {

    interface View extends BaseView<BannerFragmentContract.Presenter> {

        void showBanner();

    }

    interface Presenter extends BasePresenter {

        void getBanners(int cacheType, List<BannerInfo> infos);
    }
}

package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.BannerInfo;
import com.smartstudy.xxd.entity.HomeHotInfo;
import com.smartstudy.xxd.entity.HomeHotListInfo;
import com.smartstudy.xxd.entity.HomeHotRankInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface HomeFragmentContract {

    interface View extends BaseView<HomeFragmentContract.Presenter> {

        void showBanner();

        void showHotRank(HomeHotRankInfo world, HomeHotRankInfo us, HomeHotRankInfo uk, HomeHotRankInfo ca, HomeHotRankInfo au);

        void showHomeHot(List<HomeHotListInfo> mDatas);

        void showHotSub(List<HomeHotInfo> mDatas);



    }

    interface Presenter extends BasePresenter {

        void getBanners(int cacheType, List<BannerInfo> infos);

        void getHomeHot();
    }
}

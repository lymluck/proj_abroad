package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ColumnListItemInfo;
import com.smartstudy.xxd.entity.HomeHotListInfo;
import com.smartstudy.xxd.entity.HotRankInfo;
import com.smartstudy.xxd.entity.RecommendedCourse;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface UsHighSchoolContract {

    interface View extends BaseView<UsHighSchoolContract.Presenter> {

        void showHotRank(List<HotRankInfo> homeHotRankInfos);

        void showHomeHot(List<HomeHotListInfo> mDatas);

        void showRecommendedCourse(RecommendedCourse recommendedCourse);

        void showColumnNews(List<ColumnListItemInfo> infos);

        void hideLoading();

    }

    interface Presenter extends BasePresenter {

        void getHomeHot();

        void deleCourse(String productId);

    }
}

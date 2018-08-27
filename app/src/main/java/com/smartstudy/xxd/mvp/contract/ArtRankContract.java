package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.entity.SchooolRankInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface ArtRankContract {

    interface View extends BaseView<ArtRankContract.Presenter> {

        void getRankSuccess(List<SchooolRankInfo> data, boolean showBtn, String title, int request_state);

        void setCountries(List<IdNameInfo> datas);

        void setDegrees(List<IdNameInfo> datas);

        void showEmptyView(android.view.View view);

        void reload();

    }

    interface Presenter extends BasePresenter {

        void getArtRank(String majorId, String country_id, String degreeId, int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}

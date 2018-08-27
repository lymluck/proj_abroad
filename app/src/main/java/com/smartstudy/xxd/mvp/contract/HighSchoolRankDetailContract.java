package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.HighSchoolRankDetailInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface HighSchoolRankDetailContract {
    interface View extends BaseView<HighSchoolRankDetailContract.Presenter> {

        void getHighSchoolListSuccess(List<HighSchoolRankDetailInfo> highSchoolRankDetailInfos, int request_state);


        void showEmptyView(android.view.View view);

        void reload();

    }

    interface Presenter extends BasePresenter {

        void getHighSchoolDetail(String categoryId, String page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}

package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.HighSchoolInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.HighOptionsInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface HighSchoolLibraryContract {

    interface View extends BaseView<HighSchoolLibraryContract.Presenter> {

        void getOptionsSuccess(HighOptionsInfo highOptionsInfo);

        void getHighSchoolListSuccess(List<HighSchoolInfo> highSchools, int request_state);


        void showEmptyView(android.view.View view);

        void reload();
    }

    interface Presenter extends BasePresenter {

        void getHighOptions();

        void getHighList(String countryId, String sexualTypeId, String boarderTypeId, String locationTypeId, String feeRange
                , String rankCategoryId, String rankRange, String page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }

}

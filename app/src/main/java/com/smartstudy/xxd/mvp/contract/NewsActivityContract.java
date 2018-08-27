package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.NewsInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface NewsActivityContract {

    interface View extends BaseView<NewsActivityContract.Presenter> {

        void getNewsSuccess(List<NewsInfo> data, int request_state);

        void showEmptyView(android.view.View view);

    }

    interface Presenter extends BasePresenter {

        void getVisaNews(String countryId, int page, int request_state);

        void getSchoolNews(String schoolId, int page, int request_state);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView( android.view.View emptyView);
    }
}

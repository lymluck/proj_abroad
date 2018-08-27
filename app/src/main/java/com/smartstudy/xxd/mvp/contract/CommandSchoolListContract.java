package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface CommandSchoolListContract {

    interface View extends BaseView<CommandSchoolListContract.Presenter> {

        void getSchoolsSuccess(List<SchoolInfo> data, int request_state);

        void showEmptyView(android.view.View view);

        void noitfyItem(int position);

        void reload();


    }

    interface Presenter extends BasePresenter {

        void getSchools(int cacheType, String country_id, int page, int request_state);

        void add2MySchool(String match_type, int school_Id, int position);

        void deleteMyChoose(String school_Id, int position);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}

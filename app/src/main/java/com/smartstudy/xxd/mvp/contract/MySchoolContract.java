package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.MySchoolInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface MySchoolContract {

    interface View extends BaseView<MySchoolContract.Presenter> {

        void getMySchoolSuccess(int school_len, boolean selectVisible);

        void editMySchoolSuccess(String match_type_id);

        void delMySchoolSuccess(int position);

        void setPrivacySuccess(boolean visible);
    }

    interface Presenter extends BasePresenter {

        void getMySchool(int cacheType, List<MySchoolInfo> schoolInfo);

        void editMySchool(String school_Id, String match_type_id);

        void deleteMySchool(String school_Id, int position);

        void setPrivacy(boolean visible);
    }
}

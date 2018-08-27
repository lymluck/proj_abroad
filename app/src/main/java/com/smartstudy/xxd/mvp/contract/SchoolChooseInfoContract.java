package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.OtherStudentChoiceDetailInfo;
import com.smartstudy.xxd.entity.SchoolChooseInfo;

/**
 * Created by louis on 2017/3/1.
 */

public interface SchoolChooseInfoContract {

    interface View extends BaseView<SchoolChooseInfoContract.Presenter> {

        void showSchoolStat(SchoolChooseInfo info);

        void getOtherStudentDetailSuccess(OtherStudentChoiceDetailInfo otherStudentChoiceDetailInfo);

    }

    interface Presenter extends BasePresenter {
        void getSchoolStat(String id);

        void getOtherStudentDetail(String id);
    }
}

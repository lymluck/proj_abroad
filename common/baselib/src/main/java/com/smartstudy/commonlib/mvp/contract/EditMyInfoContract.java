package com.smartstudy.commonlib.mvp.contract;

import com.smartstudy.commonlib.entity.PersonalParamsInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 17/3/15.
 */

public interface EditMyInfoContract {
    interface View extends BaseView<EditMyInfoContract.Presenter> {

        void editMyInfoSuccess(String jsonObject);

    }

    interface Presenter extends BasePresenter {

        void editMyInfo(PersonalParamsInfo myInfo);

    }
}

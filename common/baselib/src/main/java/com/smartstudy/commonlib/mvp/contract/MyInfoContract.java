package com.smartstudy.commonlib.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 17/3/15.
 */

public interface MyInfoContract {
    interface View extends BaseView<MyInfoContract.Presenter> {

        void getMyInfoSuccess(String jsonObject);

    }

    interface Presenter extends BasePresenter {

        void getMyInfo();

    }
}

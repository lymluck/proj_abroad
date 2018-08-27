package com.smartstudy.commonlib.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/4.
 */

public interface LoginActivityContract {
    interface View extends BaseView<Presenter> {

        void getPhoneCodeSuccess();

        void phoneCodeLoginSuccess(boolean created, String user_Id);
    }

    interface Presenter extends BasePresenter {

        void getPhoneCode(String phone);

        void phoneCodeLogin(String phone, String code);
    }
}

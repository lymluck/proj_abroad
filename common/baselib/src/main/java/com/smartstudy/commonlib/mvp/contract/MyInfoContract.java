package com.smartstudy.commonlib.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 17/3/15.
 */

public interface MyInfoContract {
    interface View extends BaseView<MyInfoContract.Presenter> {

        void getInfoSuccess(String jsonObject);

        void sendSuccess();

    }

    interface Presenter extends BasePresenter {

        void getMyInfo();

        void getUserInfo(String userId);

        void sendInfoToTeacher(String questionId, String answerId);
    }
}

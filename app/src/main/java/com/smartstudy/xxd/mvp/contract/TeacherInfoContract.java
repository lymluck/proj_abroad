package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * @author yqy
 * @date on 2018/5/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface TeacherInfoContract {
    interface View extends BaseView<TeacherInfoContract.Presenter> {

        void addGoodSuccess(String count);

    }

    interface Presenter extends BasePresenter {

        void addGood(String imUserId);

    }
}

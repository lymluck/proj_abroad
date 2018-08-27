package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/1.
 */

public interface QrLoginContract {

    interface View extends BaseView<QrLoginContract.Presenter> {

        void success();

        void failed();

    }

    interface Presenter extends BasePresenter {

        void verify(String str);

    }
}

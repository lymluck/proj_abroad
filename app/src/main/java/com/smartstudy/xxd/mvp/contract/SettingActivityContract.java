package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/1.
 */

public interface SettingActivityContract {

    interface View extends BaseView<SettingActivityContract.Presenter> {

        void updateable(String downUrl, String version, String des);

    }

    interface Presenter extends BasePresenter {

        void getVersion(String channel);

    }
}

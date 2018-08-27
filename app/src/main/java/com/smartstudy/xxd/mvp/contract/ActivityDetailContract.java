package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ActivityInfo;

/**
 * Created by louis on 2017/3/1.
 */

public interface ActivityDetailContract {

    interface View extends BaseView<ActivityDetailContract.Presenter> {

        void showAcitvity(ActivityInfo info);

    }

    interface Presenter extends BasePresenter {

        void getActivityDetail(String id);
    }
}

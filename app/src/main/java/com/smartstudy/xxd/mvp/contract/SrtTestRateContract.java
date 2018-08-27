package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.SmartChooseInfo;

/**
 * Created by louis on 2017/3/1.
 */

public interface SrtTestRateContract {

    interface View extends BaseView<SrtTestRateContract.Presenter> {

        void doTestSuccess(String rate_id, SmartChooseInfo info);

        void getHasTestNumSuccess(String num);

        void showSchoolInfo(String logoUrl, String chineseName, String egName);

    }

    interface Presenter extends BasePresenter {

        void goTest(SmartChooseInfo info);

        void getHasTestNum();

        void getSchoolInfo(String id);

    }
}

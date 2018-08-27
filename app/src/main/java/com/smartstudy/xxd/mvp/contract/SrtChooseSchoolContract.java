package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.entity.SmartSchoolRstInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.SmartChooseInfo;

import java.util.ArrayList;

/**
 * Created by louis on 2017/3/1.
 */

public interface SrtChooseSchoolContract {

    interface View extends BaseView<SrtChooseSchoolContract.Presenter> {

        void doChooseSuccess(ArrayList<SmartSchoolRstInfo> schoolInfo);

        void getHasTestNumSuccess(String num);

    }

    interface Presenter extends BasePresenter {

        void goChoose(SmartChooseInfo info, ArrayList<SmartSchoolRstInfo> schoolInfo);

        void getHasTestNum();

    }
}

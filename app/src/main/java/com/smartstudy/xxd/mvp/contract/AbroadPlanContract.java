package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.xxd.entity.AbroadPlanInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface AbroadPlanContract {

    interface View extends BaseView<AbroadPlanContract.Presenter> {

        void getPlanSuccess(List<AbroadPlanInfo> datas, List<String> names, List<AbroadPlanInfo.StagesEntity> states, int position,int currentIndex);

        void toPlan();

    }

    interface Presenter extends BasePresenter {

        void getPlan(int cacheType);
    }
}

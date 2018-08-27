package com.smartstudy.xxd.mvp.contract;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.entity.SpecialQaInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * Created by louis on 2017/3/1.
 */

public interface SrtChooseSpecialContract {

    interface View extends BaseView<SrtChooseSpecialContract.Presenter> {

        void getHasTestNumSuccess(String num);

        void showQa(List<SpecialQaInfo> list, JSONObject answers);

        void showResult(Map<String, Object> result, JSONArray arr_fit, JSONArray arr_rec);

    }

    interface Presenter extends BasePresenter {

        void goChooseQa();

        void getHasTestNum();

        void handlerScore(List<SpecialQaInfo> list);

    }
}

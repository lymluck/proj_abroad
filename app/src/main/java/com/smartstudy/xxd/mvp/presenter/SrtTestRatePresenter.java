package com.smartstudy.xxd.mvp.presenter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.entity.SmartChooseInfo;
import com.smartstudy.xxd.mvp.contract.SrtTestRateContract;
import com.smartstudy.xxd.mvp.model.SchoolModel;
import com.smartstudy.xxd.mvp.model.SrtTestRateModel;

/**
 * Created by louis on 2017/3/2.
 */

public class SrtTestRatePresenter implements SrtTestRateContract.Presenter {

    private SrtTestRateContract.View view;

    public SrtTestRatePresenter(SrtTestRateContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void goTest(final SmartChooseInfo info) {
        SrtTestRateModel.goTest(info, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.doTestSuccess(result, info);
            }
        });
    }

    @Override
    public void getHasTestNum() {
        SrtTestRateModel.getHasTest(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    view.getHasTestNumSuccess(result);
                }
            }
        });
    }

    @Override
    public void getSchoolInfo(String id) {
        SchoolModel.getCollegeIntro(id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSON.parseObject(result);
                if (obj != null) {
                    view.showSchoolInfo(obj.getString("logo"), obj.getString("chineseName"), obj.getString("englishName"));
                }
            }
        });
    }
}

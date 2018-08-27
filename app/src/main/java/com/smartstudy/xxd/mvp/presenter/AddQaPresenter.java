package com.smartstudy.xxd.mvp.presenter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.xxd.mvp.contract.AddQaContract;
import com.smartstudy.xxd.mvp.model.QaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author louis
 * @date on 2018/8/22
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class AddQaPresenter implements AddQaContract.Presenter {

    private AddQaContract.View view;

    public AddQaPresenter(AddQaContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getOptions() {
        QaModel.getQaOptions(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    JSONObject obj = JSON.parseObject(result);
                    List<IdNameInfo> countries = JSON.parseArray(obj.getString("targetCountry"), IdNameInfo.class);
                    List<IdNameInfo> degrees = JSON.parseArray(obj.getString("targetDegree"), IdNameInfo.class);
                    view.getOptionsSuccess(countries, degrees);
                }
            }
        });
    }

    @Override
    public void addQa(String content, String countryId, String projectId) {
        QaModel.postQa(content, countryId, projectId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.addQaSuccess();
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}

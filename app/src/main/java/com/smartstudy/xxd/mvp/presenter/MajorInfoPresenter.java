package com.smartstudy.xxd.mvp.presenter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.entity.HomeHotProgramInfo;
import com.smartstudy.xxd.mvp.contract.MajorInfoContract;
import com.smartstudy.xxd.mvp.model.MajorModel;

import java.util.List;

/**
 * Created by louis on 2017/3/2.
 */
public class MajorInfoPresenter implements MajorInfoContract.Presenter {

    private MajorInfoContract.View view;

    public MajorInfoPresenter(MajorInfoContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getMajorInfo(String id) {
        MajorModel.getMajorInfo(id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject data = JSON.parseObject(result);
                if (data != null) {
                    String features = data.getString("features");
                    if (!TextUtils.isEmpty(features)) {
                        view.showFeature(features);
                    }
                    String factors = data.getString("factors");
                    if (!TextUtils.isEmpty(factors)) {
                        view.showFactor(factors);
                    }
                    String employment = data.getString("employment");
                    if (!TextUtils.isEmpty(employment)) {
                        view.showEmployment(employment);
                    }
                    String advices = data.getString("advices");
                    if (!TextUtils.isEmpty(advices)) {
                        view.showAdvices(advices);
                    }
                    if (data.containsKey("programs")) {
                        List<HomeHotProgramInfo> programInfos = JSON.parseArray(data.getString("programs"),
                            HomeHotProgramInfo.class);
                        if (programInfos != null) {
                            view.showPrograms(programInfos);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}

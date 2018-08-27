package com.smartstudy.xxd.mvp.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.mvp.contract.TeacherInfoContract;
import com.smartstudy.xxd.mvp.model.TeacherInfoModel;

/**
 * @author yqy
 * @date on 2018/5/16
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TeacherInfoPresenter implements TeacherInfoContract.Presenter {

    private TeacherInfoContract.View view;

    public TeacherInfoPresenter(TeacherInfoContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }


    @Override
    public void addGood(String imUserId) {
        TeacherInfoModel.addGood(imUserId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject.containsKey("count")) {
                    String count = jsonObject.getString("count");
                    view.addGoodSuccess(count);
                }
            }
        });
    }
}

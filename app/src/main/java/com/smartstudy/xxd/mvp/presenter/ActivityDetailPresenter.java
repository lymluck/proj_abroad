package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.entity.ActivityInfo;
import com.smartstudy.xxd.mvp.contract.ActivityDetailContract;
import com.smartstudy.xxd.mvp.model.ActivityModel;

/**
 * Created by louis on 2017/3/2.
 */

public class ActivityDetailPresenter implements ActivityDetailContract.Presenter {

    private ActivityDetailContract.View view;

    public ActivityDetailPresenter(ActivityDetailContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void getActivityDetail(String id) {
        ActivityModel.getActivityDetail(id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                ActivityInfo info = JSON.parseObject(result, ActivityInfo.class);
                if (info != null) {
                    view.showAcitvity(info);
                }
            }
        });
    }
}

package com.smartstudy.commonlib.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.mvp.contract.SplashContract;
import com.smartstudy.commonlib.mvp.model.SplashModel;
import com.smartstudy.commonlib.utils.SPCacheUtils;

/**
 * Created by louis on 2017/3/4.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private SplashContract.View view;

    public SplashPresenter(SplashContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getAdInfo() {
        SplashModel.getAdInfo(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject obj = JSON.parseObject(result);
                if (obj != null) {
                    view.getAdSuccess(obj.getString("id"), obj.getString("name"), obj.getString("imgUrl"), obj.getString("adUrl"));
                } else {
                    SPCacheUtils.remove("adImg");
                }
                obj = null;
            }
        });
    }

    @Override
    public void trackAd(String id) {
        SplashModel.trackAd(id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }

}

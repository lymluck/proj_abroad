package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.entity.HotMajorInfo;
import com.smartstudy.xxd.mvp.contract.FindMajorContract;
import com.smartstudy.xxd.mvp.model.SpecialListModel;

import java.util.List;


/**
 * Created by louis on 2017/3/2.
 */
public class FindMajorPresenter implements FindMajorContract.Presenter {

    private FindMajorContract.View view;

    public FindMajorPresenter(FindMajorContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void onDetachView() {
        view = null;
    }

    @Override
    public void getHotMajor() {
        SpecialListModel.getHotMajor(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                List<HotMajorInfo> datas = JSON.parseArray(result, HotMajorInfo.class);
                if (datas != null) {
                    view.showHotMajor(datas);
                }
            }
        });
    }
}

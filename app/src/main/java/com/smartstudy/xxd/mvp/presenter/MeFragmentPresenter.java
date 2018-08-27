package com.smartstudy.xxd.mvp.presenter;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.mvp.model.PersonalInfoModel;
import com.smartstudy.xxd.mvp.contract.MeFragmentContract;

/**
 * Created by louis on 2017/3/2.
 */

public class MeFragmentPresenter implements MeFragmentContract.Presenter {

    private MeFragmentContract.View view;

    public MeFragmentPresenter(MeFragmentContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getMyInfo() {
        PersonalInfoModel.getMyInfo(new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.getMyInfoSuccess(result);
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}

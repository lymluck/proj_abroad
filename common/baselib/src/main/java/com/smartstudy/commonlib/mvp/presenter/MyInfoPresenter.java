package com.smartstudy.commonlib.mvp.presenter;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.mvp.contract.MyInfoContract;
import com.smartstudy.commonlib.mvp.model.PersonalInfoModel;

/**
 * Created by louis on 2017/3/2.
 */

public class MyInfoPresenter implements MyInfoContract.Presenter {

    private MyInfoContract.View view;

    public MyInfoPresenter(MyInfoContract.View view) {
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

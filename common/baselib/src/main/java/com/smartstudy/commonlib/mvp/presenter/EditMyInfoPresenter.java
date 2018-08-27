package com.smartstudy.commonlib.mvp.presenter;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.PersonalParamsInfo;
import com.smartstudy.commonlib.mvp.contract.EditMyInfoContract;
import com.smartstudy.commonlib.mvp.model.PersonalInfoModel;

/**
 * Created by louis on 2017/3/2.
 */

public class EditMyInfoPresenter implements EditMyInfoContract.Presenter {


    private EditMyInfoContract.View view;

    public EditMyInfoPresenter(EditMyInfoContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void editMyInfo(final PersonalParamsInfo myInfo) {
        PersonalInfoModel.editMyInfo(myInfo, new BaseCallback<String>() {

            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                view.editMyInfoSuccess(result);
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}

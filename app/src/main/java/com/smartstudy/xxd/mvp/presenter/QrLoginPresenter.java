package com.smartstudy.xxd.mvp.presenter;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.mvp.contract.QrLoginContract;
import com.smartstudy.xxd.mvp.model.QrCodeModel;

/**
 * Created by louis on 2017/3/2.
 */

public class QrLoginPresenter implements QrLoginContract.Presenter {

    private QrLoginContract.View view;

    public QrLoginPresenter(QrLoginContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDetachView() {
        view = null;
    }


    @Override
    public void verify(final String str) {
        QrCodeModel.verify(str, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                if (Boolean.parseBoolean(result)) {
                    view.success();
                } else {
                    view.failed();
                }
            }
        });
    }
}

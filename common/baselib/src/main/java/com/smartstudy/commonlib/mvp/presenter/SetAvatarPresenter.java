package com.smartstudy.commonlib.mvp.presenter;

import com.smartstudy.commonlib.base.callback.ReqProgressCallBack;
import com.smartstudy.commonlib.mvp.contract.SetAvatarContract;
import com.smartstudy.commonlib.mvp.model.PersonalInfoModel;
import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 2017/3/2.
 */

public class SetAvatarPresenter implements SetAvatarContract.Presenter {

    private SetAvatarContract.View view;

    public SetAvatarPresenter(SetAvatarContract.View view) {
        this.view = view;
    }

    @Override
    public void setAvatar(final File avatar) {
        String url = HttpUrlUtils.getUrl(HttpUrlUtils.URL_V2_PERSONAL);
        Map params = new HashMap<>();
        params.put("avatar", avatar);
        PersonalInfoModel.upLoadAvatar(url, params, new ReqProgressCallBack<String>() {
            @Override
            public void onProgress(long total, long current) {
            }

            @Override
            public void onErr(String errCode, String msg) {
                view.setAvatarFailed(msg);
            }

            @Override
            public void onSuccess(String result) {
                view.setAvatarSuccess(result, avatar);
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}

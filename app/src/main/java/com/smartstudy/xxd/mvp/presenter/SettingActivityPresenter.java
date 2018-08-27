package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.mvp.base.BaseModel;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.xxd.entity.VersionInfo;
import com.smartstudy.xxd.mvp.contract.SettingActivityContract;

/**
 * Created by louis on 2017/3/2.
 */

public class SettingActivityPresenter implements SettingActivityContract.Presenter {

    private SettingActivityContract.View view;

    public SettingActivityPresenter(SettingActivityContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getVersion(String channel) {
        BaseModel.getVersion(channel, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {

                VersionInfo info = JSON.parseObject(result, VersionInfo.class);
                if (info != null) {
                    if (info.isNeedUpdate()) {
                        view.updateable(info.getPackageUrl(), info.getLatestVersion(), info.getDescription());
                    } else {
                        view.showTip(null, ParameterUtils.NEWEST_VERSION);
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

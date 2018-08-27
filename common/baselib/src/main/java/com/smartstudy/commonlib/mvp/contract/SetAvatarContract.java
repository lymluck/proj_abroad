package com.smartstudy.commonlib.mvp.contract;

import com.smartstudy.commonlib.mvp.base.BasePresenter;

import java.io.File;

/**
 * Created by louis on 17/3/15.
 */

public interface SetAvatarContract {

    interface View {

        void setAvatarSuccess(String jsonObject, File avatar);

        void setAvatarFailed(String msg);

    }

    interface Presenter extends BasePresenter {

        void setAvatar(File avatar);

    }
}

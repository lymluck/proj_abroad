package com.smartstudy.commonlib.mvp.contract;


import com.smartstudy.commonlib.entity.ChatUserInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface MsgShareContract {

    interface View extends BaseView<MsgShareContract.Presenter> {

        void showUsers(List<ChatUserInfo> data);

    }

    interface Presenter extends BasePresenter {

        void getChatUsers();
    }
}

package com.smartstudy.commonlib.mvp.contract;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/4.
 */

public interface WebFragmentContract {
    interface View extends BaseView<Presenter> {

        void goMQ();

        void showLogin();

        void goCommandSchool(String id, String name);

        void doShare(String webUrl, String title, String des, String coverUrl);

        void showWebView(String webUrl);

        void toAddQa();

        void handleSchool(boolean isTrue);

        void toTestRate(Bundle bundle);

    }

    interface Presenter extends BasePresenter {

        void doJSAction(Bundle data, String webUrl);

        void savePic(Bitmap btp);
    }
}

package com.smartstudy.commonlib.mvp.contract;

import android.graphics.Bitmap;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

/**
 * Created by louis on 2017/3/4.
 */

public interface BrowerPictureContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {

        void savePic(Bitmap btp);
    }
}

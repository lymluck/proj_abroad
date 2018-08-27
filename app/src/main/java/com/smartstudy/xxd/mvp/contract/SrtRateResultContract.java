package com.smartstudy.xxd.mvp.contract;

import android.text.Spanned;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.SmartChooseInfo;

/**
 * Created by louis on 2017/3/1.
 */

public interface SrtRateResultContract {

    interface View extends BaseView<SrtRateResultContract.Presenter> {
        void showResultMsg(String msg, int color, int rating);

        void showRate(double avgRate, double userRate);

        void showEgSug(Spanned userScore, Spanned sug, String type, boolean showWebsite);

        void showTestSug(Spanned userScore, Spanned sug, String type, boolean showWebsite);

    }

    interface Presenter extends BasePresenter {

        void getTestResult(String id, SmartChooseInfo info, String chineseName);
    }
}

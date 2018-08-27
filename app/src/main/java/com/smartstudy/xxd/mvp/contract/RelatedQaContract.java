package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.RelatedQuesInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface RelatedQaContract {

    interface View extends BaseView<RelatedQaContract.Presenter> {
        void showQa(List<RelatedQuesInfo> list, String result);

        void goFinish();

        void showEmptyView(android.view.View view);

        void reload();
    }

    interface Presenter extends BasePresenter {
        void getQuestions(String ques_id);

        void qaHelpful(String id, String result, boolean helpful);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}

package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.ColumnCommentInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public interface ColumnCommentContract {

    interface View extends BaseView<ColumnCommentContract.Presenter> {

        void showList(List<ColumnCommentInfo> datas, int request_state);

        void commentSuccess();

        void showEmptyView(android.view.View view);

        void reload();

    }

    interface Presenter extends BasePresenter {

        void getColumnComments(String id, int page, int request_state);

        void comment(String newsId, String content, String commentId);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}

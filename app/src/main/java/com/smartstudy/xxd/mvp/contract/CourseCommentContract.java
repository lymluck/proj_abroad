package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.entity.CommentInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface CourseCommentContract {

    interface View extends BaseView<CourseCommentContract.Presenter> {

        void showComments(List<CommentInfo> data, int request_state);

        void showCommentCount(String count);

        void isComment(boolean isComment, boolean isPlayed);

        void CommentSuccess();

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {

        void getComments(String courseId, int page, int request_state);

        void CommentCourse(String courseId, String rate, String comment);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);

        void hideEmptyView(android.view.View emptyView);
    }
}

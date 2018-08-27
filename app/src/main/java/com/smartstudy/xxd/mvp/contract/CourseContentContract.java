package com.smartstudy.xxd.mvp.contract;

import android.content.Context;

import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.CourseContentInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface CourseContentContract {

    interface View extends BaseView<CourseContentContract.Presenter> {

        void showContent(List<CourseContentInfo> data);

        void showUrl(String url, String title, String sectionId, long lastTime, long duration);

        void recordSuccess();

        void showEmptyView(android.view.View view);

        void reload();
    }

    interface Presenter extends BasePresenter {

        void getContents(String courseId);

        void getPlayUrl(String courseId, String id, String title, long lastTime, long duration);

        void recordPlayUrl(String courseId, String id, String playTime, String playDuration);

        void showLoading(Context context, android.view.View emptyView);

        void setEmptyView(android.view.View emptyView);
    }
}

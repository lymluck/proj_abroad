package com.smartstudy.xxd.mvp.presenter;

import com.alibaba.fastjson.JSON;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.entity.CourseIntroInfo;
import com.smartstudy.xxd.mvp.contract.CourseIntroContract;
import com.smartstudy.xxd.mvp.model.CourseModel;

/**
 * Created by louis on 2017/3/2.
 */

public class CourseIntroPresenter implements CourseIntroContract.Presenter {

    private CourseIntroContract.View view;

    public CourseIntroPresenter(CourseIntroContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void getIntros(String courseId) {
        CourseModel.getCourseIntro(courseId, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                CourseIntroInfo info = JSON.parseObject(result, CourseIntroInfo.class);
                if (info != null) {
                    view.showIntro(info);
                }
            }
        });
    }

    @Override
    public void onDetachView() {
        view = null;
    }
}

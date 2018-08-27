package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;


/**
 * Created by louis on 2017/3/1.
 */

public interface HighSchoolContract {

    interface View extends BaseView<HighSchoolContract.Presenter> {

        void isCollected(boolean isCollected);

        void collectSuccess();

        void discollectSuccess();

        void showTop(String name, String egName, String addr, String rank);

        void showIntro(String intro);

        void showPhotos(List<String> photos);

        void showSummary(List<IdNameInfo> items);

        void showData(List<IdNameInfo> items);

        void showFees(List<IdNameInfo> items);

        void showApplications(List<IdNameInfo> items);

        void showSummer(List<IdNameInfo> items);

        void showContact(List<IdNameInfo> items);

        void showSports(List<String> items);

        void showApCourses(List<String> items);

        void showCommunities(List<String> items);

        void showFeatures(List<String> items);

        void showPros(List<String> items);

        void showHonors(List<String> items);

        void showFacilities(List<String> items);

        void showEsl(String esl);

        void hideLoading();

    }

    interface Presenter extends BasePresenter {

        void isCollected(String id);

        void getSchoolDetail(String schoolId);

        void collect(String schoolId);

        void disCollect(String schoolId);

    }
}

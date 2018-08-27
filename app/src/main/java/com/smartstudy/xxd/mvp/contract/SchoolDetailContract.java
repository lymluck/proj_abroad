package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.entity.NewsInfo;
import com.smartstudy.commonlib.entity.QuestionInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;
import com.smartstudy.xxd.entity.SchoolDegreeInfo;
import com.smartstudy.xxd.entity.SchoolIntroInfo;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface SchoolDetailContract {

    interface View extends BaseView<SchoolDetailContract.Presenter> {

        void showInfo(SchoolIntroInfo info);

        void showIntro(String intro, String countryId, boolean selected);

        void addSuccess();

        void delSuccess();

        void showBk(List<SchoolDegreeInfo> data);

        void showYjs(List<SchoolDegreeInfo> data);

        void showSia(List<SchoolDegreeInfo> data);

        void showNews(List<NewsInfo> data);

        void showQa(List<QuestionInfo> data);

    }

    interface Presenter extends BasePresenter {

        void getSchoolIntro(String schoolId);

        void getSchoolDetail(String schoolId);

        void add2MySchool(String match_type, String school_Id);

        void deleteMyChoose(String school_Id);
    }
}

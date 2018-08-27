package com.smartstudy.xxd.mvp.contract;

import com.smartstudy.commonlib.entity.SchoolInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.List;

/**
 * Created by louis on 2017/3/1.
 */

public interface SrtSpecialResultContract {

    interface View extends BaseView<SrtSpecialResultContract.Presenter> {

        void dataRefres();

        void noitfyItem(int position);

        void addAll2MyschoolSucc();
    }

    interface Presenter extends BasePresenter {

        void getRecSchools(String ids, List<SchoolInfo> list);

        void add2MySchool(String match_type, int school_Id, int position);

        void addAll2Myschool(String match_type, List<SchoolInfo> schoolInfo);

        void deleteMyChoose(String school_Id, int position);

    }
}

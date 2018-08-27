package com.smartstudy.xxd.mvp.contract;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.smartstudy.commonlib.entity.SmartSchoolRstInfo;
import com.smartstudy.commonlib.mvp.base.BasePresenter;
import com.smartstudy.commonlib.mvp.base.BaseView;

import java.util.ArrayList;

/**
 * Created by louis on 2017/3/1.
 */

public interface SrtSchoolResultContract {

    interface View extends BaseView<SrtSchoolResultContract.Presenter> {

        void noitfyItem(int position);

        void addAll2MyschoolSucc();

        void goShare(String url, String title, String des);

        void doFinish();

        void toMsgCenter();

        void showEmptyView(android.view.View view);
    }

    interface Presenter extends BasePresenter {

        void add2MySchool(String school_Id, String match_type_id, int position);

        void addAll2Myschool(ArrayList<SmartSchoolRstInfo> schoolInfo);

        void deleteMyChoose(String school_Id, int position);

        void doShare(Intent data);

        void doFinishDelay();

        void setEmptyView(LayoutInflater mInflater, Context context, ViewGroup parent);

    }
}

package com.smartstudy.xxd.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.ui.fragment.CourseListFragment;

public class CourseListActivity extends UIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
    }

    @Override
    protected void initViewAndData() {
        CourseListFragment courseListFragment = new CourseListFragment();
        Bundle data = new Bundle();
        data.putString("data_flag", "list");
        courseListFragment.setArguments(data);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flyt_course, courseListFragment);
        transaction.commit();
    }

    @Override
    public void initEvent() {
    }

    @Override
    protected void doClick(View v) {
    }

}

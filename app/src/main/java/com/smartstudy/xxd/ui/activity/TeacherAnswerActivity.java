package com.smartstudy.xxd.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.ui.fragment.QaFragment;

/**
 * @author yqy
 * @date on 2018/3/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class TeacherAnswerActivity extends UIActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_list);
    }

    @Override
    protected void initViewAndData() {
        QaFragment qaFragment = new QaFragment();
        qaFragment.setArguments(getIntent().getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flyt_qa, qaFragment);
        transaction.commit();
    }

    @Override
    public void initEvent() {
    }

    @Override
    protected void doClick(View v) {
    }
}

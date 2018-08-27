package com.smartstudy.xxd.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.smartstudy.commonlib.entity.Event;
import com.smartstudy.commonlib.ui.activity.base.BaseActivity;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.ui.fragment.QaFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeQaActivity extends BaseActivity {

    QaFragment qaFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_home_qa);
    }

    @Override
    protected void initViewAndData() {
        qaFragment = new QaFragment();
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.MsgQaEvent msgQaEvent) {
        // 我的问答消息红点展示
        if (qaFragment != null) {
            qaFragment.showMyQaRed(msgQaEvent.getUnRead());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void showQaRed(int visivle) {
        // 我的问答消息红点展示
        if (qaFragment != null) {
            qaFragment.showMyQaRedIsVisible(visivle);
        }
    }
}

package com.smartstudy.xxd.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.smartstudy.commonlib.ui.activity.base.UIActivity;
import com.smartstudy.commonlib.utils.IMUtils;
import com.smartstudy.commonlib.utils.LogUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.mvp.contract.StudyConsultantsContract;
import com.smartstudy.xxd.mvp.presenter.StudyConsultantsPresenter;
import com.smartstudy.xxd.ui.fragment.ConsultantsFragment;

import io.rong.common.RLog;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by yqy on 2017/12/21.
 */

public class StudyConsultantsActivity extends UIActivity implements StudyConsultantsContract.View {
    private TextView tvChatList;
    private TextView tvConsultantsList;
    private FragmentManager mfragmentManager;
    private Bundle state;
    private StudyConsultantsContract.Presenter presenter;
    private ConsultantsFragment consultantsFragment;
    private ConversationListFragment mConversationListFragment = null;
    private long firstClick = 0;
    private long secondClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = savedInstanceState;
        setContentView(R.layout.activity_study_consultants);
    }

    @Override
    protected void onResume() {
        super.onResume();
        imConnect();
    }

    private void imConnect() {
        if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            //登录融云IM
            String cacheToken = (String) SPCacheUtils.get("imToken", "");
            if (!TextUtils.isEmpty(cacheToken)) {
                RongIM.connect(cacheToken, IMUtils.getConnectCallback());
            }
        }
    }

    @Override
    protected void initViewAndData() {
        TextView titleView = (TextView) findViewById(R.id.topdefault_centertitle);
        titleView.setText("留学顾问");
        tvChatList = findViewById(R.id.chat_list);
        tvConsultantsList = findViewById(R.id.consultants_list);
        new StudyConsultantsPresenter(this);
        tvChatList.setSelected(true);
        mfragmentManager = getSupportFragmentManager();
        hideFragment(mfragmentManager);
        if (state == null) {
            presenter.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_ONE);
        }
    }

    @Override
    public void initEvent() {
        tvChatList.setOnClickListener(this);
        tvConsultantsList.setOnClickListener(this);
        findViewById(R.id.topdefault_leftbutton).setOnClickListener(this);
    }

    @Override
    protected void doClick(View v) {
        switch (v.getId()) {
            case R.id.chat_list:
                if (firstClick == 0) {
                    firstClick = System.currentTimeMillis();
                } else {
                    secondClick = System.currentTimeMillis();
                }
                RLog.i("MainActivity", "time = " + (secondClick - firstClick));
                if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {
                    mConversationListFragment.focusUnreadItem();
                    firstClick = 0;
                    secondClick = 0;
                } else if (firstClick != 0 && secondClick != 0) {
                    firstClick = 0;
                    secondClick = 0;
                }
                presenter.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_ONE);
                break;
            case R.id.consultants_list:
                LogUtils.d("consultant======");
                presenter.showFragment(mfragmentManager, ParameterUtils.FRAGMENT_TWO);
                break;
            case R.id.topdefault_leftbutton:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 保存fragment状态
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ParameterUtils.FRAGMENT_TAG, presenter.currentIndex());
        super.onSaveInstanceState(outState);
    }


    /**
     * 复位fragment状态
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            hideFragment(mfragmentManager);
            presenter.showFragment(mfragmentManager, savedInstanceState.getInt(ParameterUtils.FRAGMENT_TAG));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void setPresenter(StudyConsultantsContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void hideFragment(FragmentManager fragmentManager) {
        //如果不为空，就先隐藏起来
        if (fragmentManager != null && fragmentManager.getFragments().size() > 0) {
            for (Fragment fragment : fragmentManager.getFragments()) {
                fragment.setUserVisibleHint(false);
                if (fragment.isAdded()) {
                    fragmentManager.beginTransaction().hide(fragment)
                            .commitAllowingStateLoss();
                }
            }
        }
        tvChatList.setSelected(false);
        tvConsultantsList.setSelected(false);
    }

    @Override
    public void showChatListFragment(FragmentTransaction ft) {
        tvChatList.setSelected(true);
        /**
         * 如果Fragment为空，就新建一个实例
         * 如果不为空，就将它从栈中显示出来
         */
        if (mConversationListFragment == null) {
            mConversationListFragment = initConversationList();
            ft.add(R.id.content, mConversationListFragment);
        } else {
            ft.show(mConversationListFragment);
        }
        mConversationListFragment.setUserVisibleHint(true);
        ft = null;
    }

    @Override
    public void showConsultantsListFragment(FragmentTransaction ft) {
        tvConsultantsList.setSelected(true);
        /**
         * 如果Fragment为空，就新建一个实例
         * 如果不为空，就将它从栈中显示出来
         */
        if (consultantsFragment == null) {
            consultantsFragment = new ConsultantsFragment();
            ft.add(R.id.content, consultantsFragment);
        } else {
            ft.show(consultantsFragment);
        }
        consultantsFragment.setUserVisibleHint(true);
        ft = null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter = null;
        }
        if (state != null) {
            state = null;
        }
    }


    private ConversationListFragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                    .build();
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }
}

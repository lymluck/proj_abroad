package com.smartstudy.commonlib.app;

import android.content.Context;
import android.content.Intent;

import com.smartstudy.commonlib.base.manager.TeacherInfoManager;
import com.smartstudy.commonlib.ui.activity.ReloginActivity;
import com.smartstudy.commonlib.utils.SPCacheUtils;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * @author louis
 * @date on 2018/2/24
 * @describe Application管理工具类
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class AppManager implements RongIMClient.ConnectionStatusListener,
        RongIMClient.OnReceiveMessageListener {

    private static AppManager mInstance;
    private Context mContext;

    public AppManager(Context mContext) {
        this.mContext = mContext;
        initListener();
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                UserInfo info = RongUserInfoManager.getInstance().getUserInfo(s);
                if (info == null) {
                    TeacherInfoManager.getInstance().getTeacherInfo(s, null);
                }
                return null;

            }
        }, true);
    }

    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (AppManager.class) {
                if (mInstance == null) {
                    mInstance = new AppManager(context);
                }
            }
        }
    }

    private void initListener() {
        RongIM.setConnectionStatusListener(this);
        RongIM.setOnReceiveMessageListener(this);
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        if (ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT.equals(connectionStatus)) {
            RongIM.getInstance().logout();
            mContext.startActivity(new Intent(mContext, ReloginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public boolean onReceived(Message message, int i) {
        SPCacheUtils.remove("Rong:" + message.getTargetId());
        return false;
    }

}

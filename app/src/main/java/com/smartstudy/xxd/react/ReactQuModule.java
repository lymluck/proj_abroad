package com.smartstudy.xxd.react;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ShareUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.router.Router;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yqy on 2017/11/2.
 */
public class ReactQuModule extends ReactContextBaseJavaModule {

    public ReactQuModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ReactQuModule";
    }

    @ReactMethod
    public void getDataFromIntent(Callback successBack, Callback erroBack) {
        try {
            Activity currentActivity = getCurrentActivity();
            String result = getCurrentActivity().getIntent().getStringExtra("QU_ID");
            String uid = currentActivity.getIntent().getStringExtra("X-xxd-uid");
            String pushId = currentActivity.getIntent().getStringExtra("X-xxd-push-reg-id");
            String ticket = "";
            if (currentActivity.getIntent().hasExtra("X-xxd-ticket")) {
                ticket = currentActivity.getIntent().getStringExtra("X-xxd-ticket");
            }
            String agent = currentActivity.getIntent().getStringExtra("User-Agent");
            successBack.invoke(result, uid, pushId, ticket, agent);
        } catch (Exception e) {
            erroBack.invoke(e.getMessage());
        }
    }

    @ReactMethod
    public void showMessage(final Callback successBack) {
        addGood(getCurrentActivity().getIntent().getStringExtra("QU_ID"), new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {

            }

            @Override
            public void onSuccess(String result) {
                if (result.equals("true")) {
                    successBack.invoke(true);
                }
            }
        });
    }


    @ReactMethod
    public void checkCollection(final Callback successBack) {
        checkCollection(getCurrentActivity().getIntent().getStringExtra("QU_ID"), new BaseCallback<String>() {

            @Override
            public void onErr(String errCode, String msg) {

            }

            @Override
            public void onSuccess(String result) {
                if (result.equals("true")) {
                    successBack.invoke(true);
                } else {
                    successBack.invoke(false);
                }
            }
        });
    }


    @ReactMethod
    public void addCollection(final Callback successBack) {
        addCollection(getCurrentActivity().getIntent().getStringExtra("QU_ID"), new BaseCallback<String>() {

            @Override
            public void onErr(String errCode, String msg) {

            }

            @Override
            public void onSuccess(String result) {
                successBack.invoke(true);
            }
        });
    }


    @ReactMethod
    public void deleteCollection(final Callback successBack) {
        deleteCollection(getCurrentActivity().getIntent().getStringExtra("QU_ID"), new BaseCallback<String>() {

            @Override
            public void onErr(String errCode, String msg) {

            }

            @Override
            public void onSuccess(String result) {
                successBack.invoke(false);
            }
        });
    }


    @ReactMethod
    public void finishActivity() {
        getCurrentActivity().finish();
    }


    public void addGood(final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_ADD, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }


    private void checkCollection(final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COLLECT, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }


    private void addCollection(final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COLLECT, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }


    private void deleteCollection(final String id, BaseCallback<String> callback) {
        RequestManager.getInstance().doDelete(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COLLECT, id));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    @ReactMethod
    public void share(String title, String des, String covelUrl) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(HttpUrlUtils.getWebUrl(String.format(HttpUrlUtils.URL_QUESTION_DETAIL,
                getCurrentActivity().getIntent().getStringExtra("QU_ID"))))
                .append("?_from=app_android_").append(AppUtils.getVersionName());
        ShareUtils.showShare(getCurrentActivity(), stringBuffer.toString(), title, des, covelUrl, null);
    }


    @ReactMethod
    public void showToast(String message){
        ToastUtils.showToast(getCurrentActivity(),message);
    }

    @ReactMethod
    public void jumpMQConversationActivity(){
        HashMap<String, String> clientInfo = new HashMap<>();
        clientInfo.put("name", (String) SPCacheUtils.get("user_name", ""));
        clientInfo.put("tel", (String) SPCacheUtils.get("user_account", ""));
        Intent intent = new MQIntentBuilder(getCurrentActivity())
                .setClientInfo(clientInfo)
                .build();
        getCurrentActivity().startActivity(intent);
        clientInfo.clear();
        clientInfo = null;
    }



}

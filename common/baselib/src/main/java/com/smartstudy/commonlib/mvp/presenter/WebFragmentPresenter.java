package com.smartstudy.commonlib.mvp.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.app.BaseApplication;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.mvp.contract.WebFragmentContract;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.commonlib.utils.BitmapUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.LogUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SDCardUtils;

import java.io.File;

import static com.smartstudy.commonlib.ui.activity.base.UIActivity.activities;
import static com.smartstudy.commonlib.ui.activity.base.UIActivity.finishAll;

/**
 * Created by louis on 2017/3/4.
 */

public class WebFragmentPresenter implements WebFragmentContract.Presenter {
    private WebFragmentContract.View view;

    public WebFragmentPresenter(WebFragmentContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }


    @Override
    public void doJSAction(Bundle data, String webUrl) {
        String actionType = data.getString("actionType");
        String params = data.getString("params");
        LogUtils.d("js======" + actionType + "======" + params);
        if (ParameterUtils.WEB_ACTION_SHARE.equals(actionType)) {
            //分享
            String title = null;
            String coverUrl = null;
            String des = null;
            if (!TextUtils.isEmpty(params)) {
                JSONObject object = JSON.parseObject(params);
                title = object.getString("title") == null ? "内容详情" : object.getString("title");
                coverUrl = object.getString("coverUrl") == null ? null : object.getString("coverUrl");
                des = object.getString("description") == null ? "点击查看详情内容" : object.getString("description");
            }
            view.doShare(webUrl, title, des, coverUrl);
        } else if (ParameterUtils.WEB_ACTION_LOGIN.equals(actionType)) {
            //登录
            view.showLogin();
        } else if (ParameterUtils.WEB_ACTION_MEIQIA.equals(actionType)) {
            view.goMQ();
        } else if (ParameterUtils.WEB_CALLBACK_ADD_SCHOOL.equals(actionType)) {
            view.handleSchool(true);
            if (activities.size() > 1) {
                finishAll();
            }
        } else if (ParameterUtils.WEB_CALLBACK_DEL_SCHOOL.equals(actionType)) {
            view.handleSchool(true);
            if (activities.size() > 1) {
                finishAll();
            }
        } else if (ParameterUtils.WEB_ACTION_TOAST.equals(actionType)) {
            view.showTip(null, JSON.parseObject(params).getString("msg"));
        } else if (ParameterUtils.WEB_ACTION_PROGRAM.equals(actionType)) {
            if (!TextUtils.isEmpty(params)) {
                JSONObject object = JSON.parseObject(params);
                view.goCommandSchool(object.getString("id"), object.getString("name"));
            }
        } else if (ParameterUtils.WEB_ACTION_LINK.equals(actionType)) {
            String url = JSON.parseObject(params).getString("url");
            view.showWebView(handleUrl(url));
        } else if (ParameterUtils.WEB_ACTION_ADD_QA.equals(actionType)) {
            view.toAddQa();
        } else if (ParameterUtils.WEB_ACTION_RATE.equals(actionType)) {
            if (!TextUtils.isEmpty(params)) {
                JSONObject object = JSON.parseObject(params);
                Bundle bundle = new Bundle();
                bundle.putString("schoolId", object.getString("schoolId"));
                bundle.putString("countryId", object.getString("countryId"));
                view.toTestRate(bundle);
                bundle.clear();
                bundle = null;
            }
        }
        data.clear();
        data = null;
    }

    @Override
    public void savePic(Bitmap btp) {
        final String filePath = SDCardUtils.getFileDirPath("Xxd" + File.separator + "pictures").getAbsolutePath();
        String fileName = System.currentTimeMillis() + ".png";
        if (BitmapUtils.saveBitmap(btp, fileName, filePath, BaseApplication.appContext)) {
            view.showTip(null, BaseApplication.appContext.getString(R.string.file_save_to)
                    + filePath);

        } else {
            view.showTip(null, BaseApplication.appContext.getString(R.string.save_failed));
        }
    }

    @Override
    public void onDetachView() {
        view = null;
    }

    private String handleUrl(String url) {
        if (!(url.startsWith("http") || url.startsWith("https") || url.startsWith("//"))) {
            //添加域名
            url = HttpUrlUtils.getWebUrl(url);
        }
        //截取掉添加域名导致的多余尾巴
        if (url.lastIndexOf("?") > 0) {
            url = url.substring(0, url.lastIndexOf("?"));
        }
        if (url.contains("_from")) {
            //修改_from值
            url = url.replaceAll("(_from=[^&]*)", "_from=app_android_" + AppUtils.getVersionName());
        } else {
            if (url.contains("?")) {
                //有参数
                url = url + "&_from=app_android_" + AppUtils.getVersionName();
            } else {
                //没有参数
                url = url + "?_from=app_android_" + AppUtils.getVersionName();
            }
        }
        return url;
    }
}

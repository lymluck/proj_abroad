package com.smartstudy.commonlib.mvp.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.mvp.contract.WebFragmentContract;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.commonlib.utils.BitmapUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.LogUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SDCardUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.router.Router;

import java.io.File;
import java.net.URLDecoder;

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
    public void doJSAction(Bundle data) {
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
            view.doShare(null, title, des, coverUrl);
        } else if (ParameterUtils.WEB_ACTION_LOGIN.equals(actionType)) {
            //登录
            view.showLogin();
        } else if (ParameterUtils.WEB_ACTION_MEIQIA.equals(actionType)) {
            view.goMQ();
        } else if (ParameterUtils.WEB_CALLBACK_ADD_SCHOOL.equals(actionType)) {
            view.handleSchool(true);
        } else if (ParameterUtils.WEB_CALLBACK_DEL_SCHOOL.equals(actionType)) {
            view.handleSchool(true);
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
    public boolean handleOverrideUrl(WebView webView, String url) {
        if (!TextUtils.isEmpty(url)) {
            // 重定向这里cookie也要进行设置
            Utils.syncCookie(url);
            if (url.startsWith("tel:")) {
                Utils.openWithWeb(webView.getContext(), url);
                return true;
            } else if (url.startsWith("baidumap://")) {
                //打开百度地图
                if (Utils.isAvilible(webView.getContext(), "com.baidu.BaiduMap")) {
                    Utils.openWithWeb(webView.getContext(), url);
                    return true;
                }
                return false;
            } else if (url.startsWith("mobile://download?url=")) {
                Utils.openWithWeb(webView.getContext(), URLDecoder.decode(url.substring(url.indexOf("=") + 1)));
                return true;
            } else if (url.startsWith("http") || url.startsWith("https")) {
                webView.loadUrl(url);
                return true;
            } else if (url.startsWith("mobile://show_share")) {
                view.showShare(Uri.parse(url));
                return true;
            } else if (url.startsWith("mobile://share")) {
                Uri shareUri = Uri.parse(url);
                view.doShare(shareUri.getQueryParameter("link"), shareUri.getQueryParameter("title"),
                    shareUri.getQueryParameter("desc"), shareUri.getQueryParameter("imgUrl"));
                return true;
            } else if (url.startsWith("mobile://actions?options=show_toast")) {
                Uri toastUri = Uri.parse(url);
                String txt_positive = toastUri.getQueryParameter("txt_positive");
                String txt_negative = toastUri.getQueryParameter("txt_negative");
                String msg = toastUri.getQueryParameter("msg");
                if (TextUtils.isEmpty(txt_positive) && TextUtils.isEmpty(txt_negative)) {
                    //非弹框样式
                    view.showTip(null, msg);
                } else {
                    //弹框样式
                    view.showDialog(toastUri);
                }
                return true;
            } else {
                openPage(webView, url);
                return true;
            }
        }
        return false;
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
        if (url.contains("?")) {
            url += "&app-type=android&appName=" + BaseApplication.appContext.getPackageName() + "&appVersion=" + AppUtils.getVersionName();
        } else {
            url += "?app-type=android&appName=" + BaseApplication.appContext.getPackageName() + "&appVersion=" + AppUtils.getVersionName();
        }
        return url;
    }

    private void openPage(final WebView webView, String url) {
        if (url.contains("course?") || url.contains("school?")
            || url.contains("question?")) {
            //构造一个StringBuilder对象
            StringBuilder sb = new StringBuilder(url);
            //在指定的位置1，插入指定的字符串
            sb.insert(url.indexOf("?"), "/detail");
            url = sb.toString();
        }
        Router.build(url).addInterceptors("PreviewPictureInterceptor").go(webView.getContext());
    }
}

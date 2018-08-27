package com.smartstudy.commonlib.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.DialogClickListener;
import com.smartstudy.commonlib.base.listener.OnWebBaseListener;
import com.smartstudy.commonlib.base.listener.OnWebCommonListener;
import com.smartstudy.commonlib.base.listener.ShareListener;
import com.smartstudy.commonlib.mvp.contract.WebFragmentContract;
import com.smartstudy.commonlib.mvp.presenter.WebFragmentPresenter;
import com.smartstudy.commonlib.task.ScanQrcodeTask;
import com.smartstudy.commonlib.ui.activity.SelectMyPhotoActivity;
import com.smartstudy.commonlib.ui.activity.base.BaseFragment;
import com.smartstudy.commonlib.ui.dialog.AppBasicDialog;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.ui.dialog.OptionsPopupDialog;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.LogUtils;
import com.smartstudy.commonlib.utils.NetUtils;
import com.smartstudy.commonlib.utils.NoFastClickUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.SensorsUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.sdk.utils.UMShareUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyWebviewFragment extends BaseFragment implements WebFragmentContract.View, Handler.Callback {

    private LinearLayout llWeb;
    private WebView mWebView;

    private WeakHandler mHandler;
    private WebFragmentContract.Presenter webP;
    private OnWebBaseListener listener;
    private String webUrl;
    private String from;
    private boolean mIsWebViewAvailable;
    private OptionsPopupDialog mDialog;
    private String qrCodeUrl;
    private List<String> items;
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;


    //接口的实例化 必须
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnWebBaseListener) mActivity;
    }

    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    /**
     * Called when the fragment is no longer resumed. Pauses the WebView.
     */
    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    /**
     * Called when the WebView has been detached from the fragment.
     * The WebView is no longer available after this time.
     */
    @Override
    public void onDestroyView() {
        mIsWebViewAvailable = false;
        super.onDestroyView();
    }

    /**
     * Called when the fragment is no longer in use. Destroys the internal state of the WebView.
     */
    @Override
    public void onDestroy() {
        //移除cookie
        Utils.removeCookie(mActivity);
        if (mWebView != null) {

            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            if (llWeb != null) {
                llWeb.removeView(mWebView);
            }

            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        if (mHandler != null) {
            mHandler = null;
        }
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        if (items != null) {
            items.clear();
            items = null;
        }
        if (webP != null) {
            webP = null;
        }
        if (listener != null) {
            listener = null;
        }
        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_webview;
    }

    @Override
    protected void initView() {
        llWeb = (LinearLayout) rootView.findViewById(R.id.llyt_web);
        if (mWebView != null) {
            mWebView.destroy();
        }
        try {
            mWebView = new WebView(mActivity);
        } catch (Throwable e) {
            String trace = Log.getStackTraceString(e);
            if (trace.contains("android.content.pm.PackageManager$NameNotFoundException")
                || trace.contains("java.lang.RuntimeException: Cannot load WebView")
                || trace.contains("android.webkit.WebViewFactory$MissingWebViewPackageException: Failed to load WebView provider: No WebView installed")) {
                showTip(null, "暂不支持打开该页面！");
                mActivity.finish();
            } else {
                throw e;
            }
        }
        // SensorsData
        SensorsUtils.trackWebView(mWebView);
        mHandler = new WeakHandler(this);
        mIsWebViewAvailable = true;
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        llWeb.addView(mWebView);
        //配置webview
        configWebView();
        mWebView.requestFocus();
        mWebView.addJavascriptInterface(new AndroidJavaScriptInterface(), "app");
        //alert弹框
//        DialogCreator.createWebviewDialog(mWebView, mActivity);
        mWebView.setWebViewClient(new myWebViewClient());
        mWebView.setWebChromeClient(new myWebChromeClient());
        Bundle data = getArguments();
        String action = data.getString("url_action");
        from = data.getString("from");
        webUrl = data.getString("web_url");
        if (!"html".equals(action)) {
            Utils.syncCookie(webUrl);
        }
        if ("get".equals(action)) {
            mWebView.loadUrl(webUrl);
        } else if ("post".equals(action)) {
            String postData = data.getString("postData");
            mWebView.postUrl(webUrl, postData.getBytes());
        } else if ("html".equals(action)) {
            mWebView.loadDataWithBaseURL(null, webUrl, "text/html", "UTF-8", null);
        }
        initEvent();
        new WebFragmentPresenter(this);
    }

    private void initEvent() {
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = mWebView.getHitTestResult();
                if (result.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                    result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    final String url = result.getExtra();
                    if (!TextUtils.isEmpty(url)) {
                        //识别图片中是否有二维码
                        scanQrCode(url);
                        //弹出菜单
                        if (items == null) {
                            items = new ArrayList<>();
                        }
                        items.clear();
                        items.add("分享给朋友");
                        items.add("保存图片");
                        mDialog = OptionsPopupDialog.newInstance(mActivity, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                            @Override
                            public void onOptionsItemClicked(int which) {
                                onItemLongClick(which, url);
                            }
                        });
                        mDialog.show();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * 配置webview
     */
    private void configWebView() {
        WebSettings settings = mWebView.getSettings();
        if (settings == null) {
            return;
        }
        // 修改ua使得web端正确判断
        settings.setUserAgentString(Utils.getUserAgent(settings.getUserAgentString()));
        // 支持Js使用
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        // 开启DOM缓存。
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(mActivity.getCacheDir().getAbsolutePath());
        // 是否可访问本地文件，默认值 true
        settings.setAllowFileAccess(true);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 设置 缓存模式
        if (NetUtils.isConnected(mActivity)) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(
                WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        // 设置可以支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
            settings.setMixedContentMode(
                WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
    }

    @Override
    public void setPresenter(WebFragmentContract.Presenter presenter) {
        if (presenter != null) {
            this.webP = presenter;
        }
    }

    @Override
    public void showTip(String errCode, String message) {
        if ("homeTabNews".equals(from) && "收藏成功".equals(message)) {
            UApp.actionEvent(mActivity, "18_A_favorit_btn");
        }
        ToastUtils.showToast(message);
    }

    @Override
    public void goMQ() {
        if ("homeTabNews".equals(from)) {
            UApp.actionEvent(mActivity, "18_A_consult_btn");
        }
        if (listener != null && listener instanceof OnWebCommonListener) {
            HashMap<String, String> clientInfo = new HashMap<>();
            clientInfo.put("name", (String) SPCacheUtils.get("user_name", ""));
            clientInfo.put("tel", (String) SPCacheUtils.get("user_account", ""));
            ((OnWebCommonListener) listener).goMQ(clientInfo);
            clientInfo.clear();
            clientInfo = null;
        }
    }

    @Override
    public void showLogin() {
        DialogCreator.createLoginDialog(mActivity);
    }

    @Override
    public void goCommandSchool(String id, String name) {
        if (listener != null && listener instanceof OnWebCommonListener) {
            ((OnWebCommonListener) listener).goCommandSchool(id, name);
        }
    }

    @Override
    public void doShare(String webUrl, String title, String des, String coverUrl) {
        if ("homeTabNews".equals(from)) {
            UApp.actionEvent(mActivity, "18_A_share_btn");
        }
        String url = webUrl == null ? this.webUrl : webUrl;
        UMShareUtils.showWebShare(mActivity, url, title, des, coverUrl,
            new ShareListener(url, "h5_xxd"));
    }

    @Override
    public void showWebView(String webUrl) {
        if ("homeTabNews".equals(from)) {
            UApp.actionEvent(mActivity, "18_A_news_about_cell");
        }
        if (listener != null) {
            listener.showWebView(webUrl);
        }
    }

    @Override
    public void toAddQa() {
        if (listener != null && listener instanceof OnWebCommonListener) {
            ((OnWebCommonListener) listener).toAddQa();
        }
    }

    @Override
    public void handleSchool(boolean isTrue) {
        if (listener != null && listener instanceof OnWebCommonListener) {
            ((OnWebCommonListener) listener).handleSchool(isTrue);
        }
        if (mActivity.activities.size() > 1) {
            mActivity.finishAll();
        }
    }

    @Override
    public void toTestRate(Bundle bundle) {
        if (listener != null && listener instanceof OnWebCommonListener) {
            ((OnWebCommonListener) listener).toTestRate(bundle);
        }
    }

    /**
     * 是否展示右上角按钮
     *
     * @param uri
     */
    @Override
    public void showShare(Uri uri) {
        if (listener != null && listener instanceof OnWebCommonListener) {
            boolean showShare = uri.getBooleanQueryParameter("isShow", true);
            ((OnWebCommonListener) listener).toShowShare(showShare, uri.getQueryParameter("link"), uri.getQueryParameter("title"),
                uri.getQueryParameter("desc"), uri.getQueryParameter("imgUrl"));
        }
    }

    @Override
    public void showDialog(Uri toastUri) {
        String txt_positive = toastUri.getQueryParameter("txt_positive");
        String txt_negative = toastUri.getQueryParameter("txt_negative");
        String msg = toastUri.getQueryParameter("msg");
        String title = toastUri.getQueryParameter("title");
        final String positive_callback = toastUri.getQueryParameter("positive_callback");
        final String negative_callback = toastUri.getQueryParameter("negative_callback");
        if (!TextUtils.isEmpty(txt_positive) && !TextUtils.isEmpty(txt_negative)) {
            DialogCreator.createBaseCustomDialog(mActivity, title, msg, new DialogClickListener() {
                @Override
                public void onClick(AppBasicDialog dialog, View v) {
                    if (!TextUtils.isEmpty(positive_callback)) {
                        mWebView.loadUrl("javascript:" + positive_callback);
                    }
                    dialog.dismiss();
                }
            }, new DialogClickListener() {
                @Override
                public void onClick(AppBasicDialog dialog, View v) {
                    if (!TextUtils.isEmpty(negative_callback)) {
                        mWebView.loadUrl("javascript:" + negative_callback);
                    }
                    dialog.dismiss();
                }
            });
        } else {
            if (!TextUtils.isEmpty(txt_positive) && TextUtils.isEmpty(txt_negative)) {
                DialogCreator.createBaseCustomDialog(mActivity, title, msg, new DialogClickListener() {
                    @Override
                    public void onClick(AppBasicDialog dialog, View v) {
                        if (!TextUtils.isEmpty(positive_callback)) {
                            mWebView.loadUrl("javascript:" + positive_callback);
                        }
                        dialog.dismiss();
                    }
                }, null);
            }
            if (TextUtils.isEmpty(txt_positive) && !TextUtils.isEmpty(txt_negative)) {
                DialogCreator.createBaseCustomDialog(mActivity, title, msg, null, new DialogClickListener() {
                    @Override
                    public void onClick(AppBasicDialog dialog, View v) {
                        if (!TextUtils.isEmpty(negative_callback)) {
                            mWebView.loadUrl("javascript:" + negative_callback);
                        }
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    private class myWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (listener != null && listener instanceof OnWebCommonListener) {
                ((OnWebCommonListener) listener).handleTitle(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (listener != null && listener instanceof OnWebCommonListener) {
                ((OnWebCommonListener) listener).onProgress(newProgress);
            }
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            uploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android  >= 3.0
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            uploadMessage = valueCallback;
            openImageChooserActivity();
        }

        //For Android  >= 4.1
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            uploadMessage = valueCallback;
            openImageChooserActivity();
        }

        // For Android >= 5.0
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
            uploadMessageAboveL = filePathCallback;
            openImageChooserActivity();
            return true;
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
                if (mActivity.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                    startActivity(intent);
                } else {
                    showTip(null, "未找到可以打开的页面！");
                }
            }
        }
    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtils.d("overrideUrl=======" + url);
            return webP.handleOverrideUrl(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();// 接受所有网站的证书
        }
    }


    /**
     * The type Android java script interface.
     */
    public final class AndroidJavaScriptInterface {

        @JavascriptInterface
        public void appAction(String actionType, String params) {
            if (!NoFastClickUtils.isFastClick()) {
                Message msg = Message.obtain();
                msg.what = ParameterUtils.ACTION_FROM_WEB;
                Bundle data = new Bundle();
                data.putString("actionType", actionType);
                data.putString("params", params);
                msg.setData(data);
                mHandler.sendMessage(msg);
            }
        }
    }

    /**
     * 处理handler消息
     *
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ParameterUtils.ACTION_FROM_WEB:
                if (webP != null) {
                    webP.doJSAction(msg.getData());
                }
                break;
            case ParameterUtils.MSG_WHAT_REFRESH:
                qrCodeUrl = (String) msg.obj;
                mDialog.addItem("识别图中二维码");
                mDialog.refreshItem();
                break;
            default:
                break;
        }
        return false;
    }

    //刷新时清除缓存
    public void refreshWebView() {
        mWebView.clearCache(true);
        Utils.syncCookie(mWebView.getUrl());
        mWebView.reload();
    }

    /**
     * Gets the WebView.
     */
    public WebView getWebView() {
        return mIsWebViewAvailable ? mWebView : null;
    }

    private void scanQrCode(String url) {
        DisplayImageUtils.displayImage(mActivity, url, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                new ScanQrcodeTask(mHandler).execute(bitmap);
            }

            @Override
            public void onStart() {
                DisplayImageUtils.resumeRequest(mActivity);
                super.onStart();
            }

            @Override
            public void onStop() {
                DisplayImageUtils.pauseRequest(mActivity);
                super.onStop();
            }
        });
    }

    private void onItemLongClick(int which, String url) {
        switch (which) {
            case 0:
                //分享图片
                UMShareUtils.showShare(mActivity, "", "", "", url, new ShareListener(url, "image"));
                break;
            case 1:
                //保存图片
                DisplayImageUtils.displayImage(mActivity, url, new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        webP.savePic(bitmap);
                    }

                    @Override
                    public void onStart() {
                        DisplayImageUtils.resumeRequest(mActivity);
                        super.onStart();
                    }

                    @Override
                    public void onStop() {
                        DisplayImageUtils.pauseRequest(mActivity);
                        super.onStop();
                    }
                });
                break;
            case 2:
                //识别二维码
                showWebView(qrCodeUrl);
                break;
            default:
                break;
        }
    }

    /**
     * 打开选择图片页面
     */
    private void openImageChooserActivity() {
        Intent intent = new Intent(mActivity, SelectMyPhotoActivity.class);
        intent.putExtra("singlePic", true);
        startActivityForResult(intent, ParameterUtils.REQUEST_CODE_CHANGEPHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri result = null;
        if (resultCode != Activity.RESULT_OK) {
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }
            return;
        }
        switch (requestCode) {
            case ParameterUtils.REQUEST_CODE_CHANGEPHOTO:
                String temppath = data.getStringExtra("path");
                if (!TextUtils.isEmpty(temppath)) {
                    result = Uri.parse("file://" + temppath);
                    //5.0以上
                    if (uploadMessageAboveL != null) {
                        Uri[] results = result != null ? new Uri[]{result} : null;
                        uploadMessageAboveL.onReceiveValue(results);
                        uploadMessageAboveL = null;
                    } else if (uploadMessage != null) {
                        uploadMessage.onReceiveValue(result);
                        uploadMessage = null;
                    }
                }
                break;
            case ParameterUtils.REQUEST_CODE_LOGIN:
                //登录成功后，刷新cookie
                Utils.syncCookie(webUrl);
                break;
            default:
                break;
        }
    }
}

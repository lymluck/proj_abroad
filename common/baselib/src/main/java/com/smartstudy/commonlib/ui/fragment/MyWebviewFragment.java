package com.smartstudy.commonlib.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.listener.OnWebBaseListener;
import com.smartstudy.commonlib.base.listener.OnWebCommonListener;
import com.smartstudy.commonlib.mvp.contract.WebFragmentContract;
import com.smartstudy.commonlib.mvp.presenter.WebFragmentPresenter;
import com.smartstudy.commonlib.task.ScanQrcodeTask;
import com.smartstudy.commonlib.ui.activity.BrowserPictureActivity;
import com.smartstudy.commonlib.ui.activity.base.UIFragment;
import com.smartstudy.commonlib.ui.dialog.DialogCreator;
import com.smartstudy.commonlib.ui.dialog.OptionsPopupDialog;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.NetUtils;
import com.smartstudy.commonlib.utils.NoFastClickUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.ShareUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.commonlib.utils.ToastUtils;
import com.smartstudy.commonlib.utils.Utils;
import com.smartstudy.router.RouteCallback;
import com.smartstudy.router.RouteResult;
import com.smartstudy.router.Router;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyWebviewFragment extends UIFragment implements WebFragmentContract.View, Handler.Callback {

    private LinearLayout llyt_web;
    private WebView mWebView;

    private WeakHandler mHandler;
    private WebFragmentContract.Presenter webP;
    private OnWebBaseListener listener;
    private String web_url;
    private String from;
    private boolean mIsWebViewAvailable;
    private ArrayList<String> imgUrls;
    private OptionsPopupDialog mDialog;
    private String qrCodeUrl;
    private List<String> items;


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
        if (mWebView != null) {
            mWebView.removeAllViews();
            llyt_web.removeView(mWebView);
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
        if (imgUrls != null) {
            imgUrls.clear();
            imgUrls = null;
        }
        super.onDestroy();
    }

    @Override
    protected View getLayoutView() {
        return mActivity.getLayoutInflater().inflate(
                R.layout.fragment_webview, null);
    }

    @Override
    protected void initView(View rootView) {
        llyt_web = (LinearLayout) rootView.findViewById(R.id.llyt_web);
        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = new WebView(mActivity);
        mHandler = new WeakHandler(this);
        mIsWebViewAvailable = true;
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        llyt_web.addView(mWebView);
        //配置webview
        configWebView();
        mWebView.requestFocus();
        mWebView.addJavascriptInterface(new AndroidJavaScriptInterface(), "app");
        //alert弹框
        DialogCreator.createWebviewDialog(mWebView, mActivity);
        mWebView.setWebViewClient(new myWebViewClient());
        mWebView.setWebChromeClient(new myWebChromeClient());
        Bundle data = getArguments();
        String action = data.getString("url_action");
        from = data.getString("from");
        web_url = data.getString("web_url");
        Utils.syncCookie(web_url);
        if ("get".equals(action)) {
            mWebView.loadUrl(web_url);
        } else if ("post".equals(action)) {
            String postData = data.getString("postData");
            mWebView.postUrl(web_url, postData.getBytes());
        } else if ("html".equals(action)) {
            mWebView.loadDataWithBaseURL(null, web_url, "text/html", "UTF-8", null);
        }
        initEvent();
        new WebFragmentPresenter(this);
    }

    private void initEvent() {
        imgUrls = new ArrayList<>();
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
        // 修改ua使得web端正确判断
        settings.setUserAgentString(Utils.getUserAgent(settings.getUserAgentString()));
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
            StatisticUtils.actionEvent(mActivity, "18_A_favorit_btn");
        }
        ToastUtils.showToast(mActivity, message);
    }

    @Override
    public void goMQ() {
        if ("homeTabNews".equals(from)) {
            StatisticUtils.actionEvent(mActivity, "18_A_consult_btn");
        }
        if (listener != null) {
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
        if (listener != null) {
            ((OnWebCommonListener) listener).goCommandSchool(id, name);
        }
    }

    @Override
    public void doShare(String webUrl, String title, String des, String coverUrl) {
        if ("homeTabNews".equals(from)) {
            StatisticUtils.actionEvent(mActivity, "18_A_share_btn");
        }
        ShareUtils.showShare(mActivity, webUrl, title, des, coverUrl, null);
    }

    @Override
    public void showWebView(String webUrl) {
        if ("homeTabNews".equals(from)) {
            StatisticUtils.actionEvent(mActivity, "18_A_news_about_cell");
        }
        if (listener != null) {
            ((OnWebCommonListener) listener).showWebView(webUrl);
        }
    }

    @Override
    public void toAddQa() {
        if (listener != null) {
            ((OnWebCommonListener) listener).toAddQa();
        }
    }

    @Override
    public void handleSchool(boolean isTrue) {
        if (listener != null) {
            ((OnWebCommonListener) listener).handleSchool(isTrue);
        }
    }

    @Override
    public void toTestRate(Bundle bundle) {
        if (listener != null) {
            ((OnWebCommonListener) listener).toTestRate(bundle);
        }
    }

    private class myWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (listener != null) {
                ((OnWebCommonListener) listener).handleTitle(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (listener != null) {
                ((OnWebCommonListener) listener).onProgress(newProgress);
            }
        }
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            if (url != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(intent);
            }

        }

    }

    private class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url)) {
                web_url = url;
                if (url.startsWith("tel:")) {
                    Utils.openWithWeb(mActivity, url);
                    return true;
                } else if (url.startsWith("baidumap://")) {
                    if (Utils.isAvilible(mActivity, "com.baidu.BaiduMap")) {
                        Utils.openWithWeb(mActivity, url);
                        return true;
                    }
                    return false;
                } else if (url.startsWith("mobile://download?url=")) {
                    Utils.openWithWeb(mActivity, URLDecoder.decode(url.substring(url.indexOf("=") + 1)));
                    return true;
                } else if (url.startsWith("http") || url.startsWith("https")) {
                    //这里cookie也要进行设置
                    Utils.syncCookie(url);
                    view.loadUrl(url);
                    return true;
                } else {
                    if (url.contains("course?") || url.contains("school?")) {
                        //构造一个StringBuilder对象
                        StringBuilder sb = new StringBuilder(url);
                        //在指定的位置1，插入指定的字符串
                        sb.insert(url.indexOf("?"), "/detail");
                        url = sb.toString();
                    }
                    Router.build(url).callback(new RouteCallback() {
                        @Override
                        public void callback(RouteResult state, Uri uri, String message) {
                            String url = uri.toString();
                            if (url.contains("?")) {
                                url = url.substring(0, url.indexOf("?"));
                            }
                            switch (url) {
                                case "xuanxiaodi://customer_service":
                                    //打开美洽
                                    SPCacheUtils.put("meiqia_unread", 0);
                                    JPushInterface.clearAllNotifications(mActivity);
                                    goMQ();
                                    break;
                                case "xuanxiaodi://question":
                                    Router.build("showWebView").with("url_action", "get")
                                            .with("web_url", String.format(HttpUrlUtils.getWebUrl(HttpUrlUtils.URL_QUESTION_DETAIL), uri.getQueryParameter("id")))
                                            .go(mActivity);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }).go(mActivity);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            getAllImgUrls();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();// 接受所有网站的证书
        }

//        /**
//         * @return 本地jquery
//         */
//        private WebResourceResponse editResponse() {
//            try {
//                return new WebResourceResponse("application/x-javascript", "utf-8",
//                        getAssets().open("vendors.13207665.js"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //需处理特殊情况
//            return null;
//        }
//
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//            if (Build.VERSION.SDK_INT < 21) {
//                if (url.contains("vendors")) {
//                    return editResponse();
//                }
//            }
//            return super.shouldInterceptRequest(view, url);
//        }
//
//        @Override
//        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//            if (Build.VERSION.SDK_INT >= 21) {
//                String url = request.getUrl().toString();
//                if (!TextUtils.isEmpty(url) && url.contains("vendors")) {
//                    return editResponse();
//                }
//            }
//            return super.shouldInterceptRequest(view, request);
//        }
    }

    private void getAllImgUrls() {
        //遍历页面中所有img的节点，因为节点里面的图片的url即objs[i].src，保存所有图片的src.
        //为每个图片设置点击事件，objs[i].onclick
        mWebView.loadUrl("javascript:(function(){" +
                "  var openImage = function(){window.app.openImage(this.src);};" +
                "  var nodes = document.querySelectorAll('.album-image-container img[src]');" +
                "  Array.prototype.forEach.call(nodes, function(node){" +
                "    window.app.readImageUrl(node.src);" +
                "    node.onclick=openImage;" +
                "  });" +
                "})();");
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

        //把所有图片的url保存在ArrayList<String>中
        @JavascriptInterface
        public void readImageUrl(String img) {
            if (!imgUrls.contains(img)) {
                imgUrls.add(img);
            }
        }

        @JavascriptInterface  //对于targetSdkVersion>=17的，要加这个声明
        public void openImage(String clickimg)//点击图片所调用到的函数
        {
            int len = imgUrls.size();
            if (imgUrls != null && len > 0) {
                int index = imgUrls.indexOf(clickimg);
                if (index >= 0 && index < len) {
                    Intent toPreview = new Intent(mActivity, BrowserPictureActivity.class);
                    toPreview.putStringArrayListExtra("pathList", imgUrls);
                    toPreview.putExtra("index", index);
                    startActivity(toPreview);
                }
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ParameterUtils.ACTION_FROM_WEB:
                if (webP != null) {
                    webP.doJSAction(msg.getData(), web_url);
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
        DisplayImageUtils.downloadImageFile(mActivity, url, new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                new ScanQrcodeTask(mActivity, resource.getPath(), mHandler).execute();
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
                doShare("", "", "", url);
                break;
            case 1:
                //保存图片
                DisplayImageUtils.displayImage(mActivity, url, new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        webP.savePic(resource);
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

    public String getWebUrl() {
        return web_url;
    }
}

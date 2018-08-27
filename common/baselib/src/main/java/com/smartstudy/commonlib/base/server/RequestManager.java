package com.smartstudy.commonlib.base.server;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.meituan.android.walle.WalleChannelReader;
import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.callback.ReqProgressCallBack;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.base.okhttpcache.interceptor.CacheAndCookieInterceptor;
import com.smartstudy.commonlib.entity.ResponseInfo;
import com.smartstudy.commonlib.utils.DeviceUtils;
import com.smartstudy.commonlib.utils.LogUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;
import com.smartstudy.commonlib.utils.SDCardUtils;
import com.smartstudy.commonlib.utils.SPCacheUtils;
import com.smartstudy.commonlib.utils.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


/**
 * 网络请求管理工具类
 * Created by louis on 2017/3/2.
 */
public class RequestManager {

    private static final byte[] LOCKER = new byte[0];
    private static RequestManager mInstance;
    private OkHttpClient mOkHttpClient;
    private WeakHandler okHttpHandler;//全局处理子线程和M主线程通信
    public static final String REQUEST_CACHE_TYPE_HEAD = "requestCacheType";//请求缓存类型
    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("multipart/form-data");//mdiatype 这个需要和服务端保持一致

    private RequestManager() {
        File cache_file = SDCardUtils.getFileDirPath("Xxd" + File.separator + "cache");
        int cache_size = 150 * 1024 * 1024;
        Cache cache = new Cache(cache_file, cache_size);
        OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
        ClientBuilder.readTimeout(30, TimeUnit.SECONDS);//读取超时
        ClientBuilder.connectTimeout(5, TimeUnit.SECONDS);//连接超时
        ClientBuilder.writeTimeout(30, TimeUnit.SECONDS);//写入超时
        ClientBuilder.cache(cache);
        ClientBuilder.addInterceptor(new CacheAndCookieInterceptor());
        mOkHttpClient = ClientBuilder.build();
        //初始化Handler
        okHttpHandler = new WeakHandler();
    }

    public static RequestManager getInstance() {
        RequestManager inst = mInstance;
        if (mInstance == null) {
            synchronized (LOCKER) {
                inst = mInstance;
                if (inst == null) {
                    inst = new RequestManager();
                    mInstance = inst;
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置请求头
     *
     * @param headersParams
     * @return
     */
    private Headers setHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        Headers.Builder headersbuilder = new Headers.Builder();

        if (headersParams != null) {
            Iterator<String> iterator = headersParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                headersbuilder.add(key, headersParams.get(key));
                LogUtils.d("headers===" + key + "====", headersParams.get(key));
            }
        }
        headers = headersbuilder.build();

        return headers;
    }

    /**
     * post请求参数
     *
     * @param BodyParams
     * @return
     */
    private static RequestBody setRequestBody(Map<String, String> BodyParams) {
        RequestBody body = null;
        okhttp3.FormBody.Builder formEncodingBuilder = new okhttp3.FormBody.Builder();
        if (BodyParams != null) {
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                LogUtils.d("post_Params===" + key + "====", BodyParams.get(key) + "====");
                if (BodyParams.get(key) != null) {
                    formEncodingBuilder.add(key, BodyParams.get(key));
                }
            }
        }
        body = formEncodingBuilder.build();
        return body;
    }

    /**
     * get方法连接拼加参数
     *
     * @param mapParams
     * @return
     */
    private String setUrlParams(Map<String, String> mapParams) {
        String strParams = "";
        if (mapParams == null) {
            mapParams = new HashMap<>();
        }
//        putGetRequestParams(mapParams);
        Iterator<String> iterator = mapParams.keySet().iterator();
        String key = "";
        while (iterator.hasNext()) {
            key = iterator.next().toString();
            strParams += "&" + key + "=" + mapParams.get(key);
        }
        if (strParams.length() > 0) {
            return "?" + strParams.substring(1);
        }
        return strParams;
    }

    /**
     * get请求
     *
     * @param config
     * @param callback
     */
    public void doGet(final int cacheType, BaseRequestConfig config, final BaseCallback callback) {
        int currentCacheType;
        //网络无效的话指定读取缓存策略
        if (!Utils.isNetworkConnected()) {
            currentCacheType = ParameterUtils.ONLY_CACHED;
        } else {
            currentCacheType = cacheType;
        }
        final Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(config.getUrl() + setUrlParams(config.getParams()));//添加URL地址
        requestBuilder.removeHeader("User-Agent");
        requestBuilder.headers(setHeaders(getHttpHeaderParams()));//添加请求头
        requestBuilder.tag(config.getUrl());//添加标签
        requestBuilder.addHeader(REQUEST_CACHE_TYPE_HEAD, String.valueOf(currentCacheType));
        Request request = requestBuilder.build();
        LogUtils.d("get_url========>", request.url().toString());
        doGetReq(request, callback);
    }

    public void justGetData(String url, final BaseCallback<String> callback) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        final Request request = requestBuilder.build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                failedCallBack(null, e.getMessage(), callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                successCallBack(result, callback);
            }
        });
    }

    /**
     * post请求
     *
     * @param config
     * @param callback
     */
    public void doPost(BaseRequestConfig config, final BaseCallback callback) {
        if (Utils.isNetworkConnected()) {
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(config.getUrl());//添加URL地址
            requestBuilder.post(setRequestBody(config.getParams()));
            requestBuilder.removeHeader("User-Agent");
            requestBuilder.headers(setHeaders(getHttpHeaderParams()));//添加请求头
            requestBuilder.tag(config.getUrl());//添加标签
            Request request = requestBuilder.build();
            LogUtils.d("post_url========>", request.url().toString());
            doRequest(request, callback);
        } else {
            failedCallBack(ParameterUtils.RESPONE_CODE_NETERR, ParameterUtils.NET_ERR, callback);
        }
    }

    /**
     * put请求
     *
     * @param config
     * @param callback
     */
    public void doPut(BaseRequestConfig config, final BaseCallback callback) {
        if (Utils.isNetworkConnected()) {
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(config.getUrl());//添加URL地址
            requestBuilder.put(setRequestBody(config.getParams()));
            requestBuilder.removeHeader("User-Agent");
            requestBuilder.headers(setHeaders(getHttpHeaderParams()));//添加请求头
            requestBuilder.tag(config.getUrl());//添加标签
            Request request = requestBuilder.build();
            LogUtils.d("put_url========>", request.url().toString());
            doRequest(request, callback);
        } else {
            failedCallBack(ParameterUtils.RESPONE_CODE_NETERR, ParameterUtils.NET_ERR, callback);
        }
    }

    /**
     * delete请求
     *
     * @param config
     * @param callback
     */
    public void doDelete(BaseRequestConfig config, final BaseCallback callback) {
        if (Utils.isNetworkConnected()) {
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(config.getUrl());//添加URL地址
            requestBuilder.delete(setRequestBody(config.getParams()));
            requestBuilder.removeHeader("User-Agent");
            requestBuilder.headers(setHeaders(getHttpHeaderParams()));//添加请求头
            requestBuilder.tag(config.getUrl());//添加标签
            Request request = requestBuilder.build();
            LogUtils.d("delete_url========>", request.url().toString());
            doRequest(request, callback);
        } else {
            failedCallBack(ParameterUtils.RESPONE_CODE_NETERR, ParameterUtils.NET_ERR, callback);
        }
    }

    private void doRequest(Request request, final BaseCallback callback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("onFailure========>", e.toString());
                failedCallBack(null, ParameterUtils.GET_DATA_FAILED, callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                doResponse(response, callback);
            }
        });
    }

    private void doGetReq(Request request, final BaseCallback callback) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("onFailure========>", e.toString());
                failedCallBack(null, ParameterUtils.GET_DATA_FAILED, callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response != null) {
                    try {
                        if (response.isSuccessful()) {
                            String result = null;
                            if ("gzip".equalsIgnoreCase(response.header("Content-Encoding")) || false) {
                                InputStream inputStream = response.body().byteStream();
                                BufferedInputStream bis = new BufferedInputStream(inputStream);
                                inputStream = new GZIPInputStream(bis);
                                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                                BufferedReader br = new BufferedReader(inputStreamReader);
                                StringBuffer sb = new StringBuffer("");
                                String line;
                                while ((line = br.readLine()) != null) {
                                    sb.append(line);
                                    sb.append("\n");
                                }
                                inputStreamReader.close();
                                br.close();
                                result = sb.toString();
                            } else {
                                //没进行压缩
                                result = response.body().string();
                            }
                            if (result != null) {
                                LogUtils.d("re");
                                ResponseInfo info = JSON.parseObject(result, ResponseInfo.class);
                                if (info != null) {
                                    LogUtils.d("responseinfo======", info.toString());
                                    if (ParameterUtils.RESPONSE_CODE_SUCCESS.equals(info.getCode())) {
                                        successCallBack(info.getData(), callback);
                                    } else {
                                        if (ParameterUtils.RESPONSE_CODE_NOLOGIN.equals(info.getCode())) {
                                            //尚未登录
                                            SPCacheUtils.remove("ticket");
                                            SPCacheUtils.remove("user_name");
                                            SPCacheUtils.remove("user_pic");
                                        }
                                        failedCallBack(info.getCode(), info.getMsg(), callback);
                                    }
                                }
                            }
                        } else {
                            if (Utils.isNetworkConnected()) {
                                failedCallBack(null, ParameterUtils.GET_DATA_FAILED, callback);
                            } else {
                                failedCallBack(ParameterUtils.RESPONE_CODE_NETERR, ParameterUtils.NET_ERR, callback);
                            }
                        }
                    } catch (IOException e) {
                        failedCallBack(null, ParameterUtils.GET_DATA_FAILED, callback);
                    }
                    //关闭防止内存泄漏
                    if (response.body() != null) {
                        response.body().close();
                    }
                } else {
                    failedCallBack(null, ParameterUtils.GET_DATA_FAILED, callback);
                }
            }
        });
    }

    public void doResponse(Response response, BaseCallback callback) {
        if (response != null) {
            if (response.isSuccessful()) {
                String data = null;
                try {
                    data = response.body().string();
                } catch (IOException e) {
                    failedCallBack(null, ParameterUtils.GET_DATA_FAILED, callback);
                }
                if (data != null) {
                    ResponseInfo info = JSON.parseObject(data, ResponseInfo.class);
                    if (info != null) {
                        LogUtils.d("responseinfo======", info.toString());
                        if (ParameterUtils.RESPONSE_CODE_SUCCESS.equals(info.getCode())) {
                            successCallBack(info.getData(), callback);
                        } else {
                            if (ParameterUtils.RESPONSE_CODE_NOLOGIN.equals(info.getCode())) {
                                //尚未登录
                                SPCacheUtils.remove("ticket");
                                SPCacheUtils.remove("user_name");
                                SPCacheUtils.remove("user_pic");
                            }
                            failedCallBack(info.getCode(), info.getMsg(), callback);
                        }
                    }
                }
            }
            //关闭防止内存泄漏
            if (response.body() != null) {
                response.body().close();
            }
        }
    }

    /**
     * 上传文件
     *
     * @param config
     * @param callBack 回调
     * @param <T>
     */
    public <T> void upLoadFile(BaseRequestConfig config, final ReqProgressCallBack callBack) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(config.getUrl());
        if (Utils.isNetworkConnected()) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            Map<String, String> params = config.getParams();
            if (params != null) {
                //追加参数
                for (String key : params.keySet()) {
                    Object object = params.get(key);
                    if (!(object instanceof File)) {
                        builder.addFormDataPart(key, object.toString());
                    } else {
                        File file = (File) object;
                        builder.addFormDataPart(key, file.getName(), createProgressRequestBody(MEDIA_OBJECT_STREAM, file, callBack));
                    }
                }
            }
            requestBuilder.removeHeader("User-Agent");
            //添加请求头
            requestBuilder.headers(setHeaders(getHttpHeaderParams()));
            requestBuilder.put(builder.build());
            //创建Request
            Request request = requestBuilder.build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtils.e("上传", e.toString());
                    failedCallBack(null, ParameterUtils.UPLOAD_ERR, callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ResponseInfo info = JSON.parseObject(response.body().string(), ResponseInfo.class);
                        if (info != null) {
                            LogUtils.d("responseinfo======", info.toString());
                            if (ParameterUtils.RESPONSE_CODE_SUCCESS.equals(info.getCode())) {
                                successCallBack(info.getData(), callBack);
                            } else {
                                failedCallBack(info.getCode(), info.getMsg(), callBack);
                            }
                        }
                    }
                    //关闭防止内存泄漏
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
            });
        } else {
            failedCallBack(ParameterUtils.RESPONE_CODE_NETERR, ParameterUtils.NET_ERR, callBack);
        }
    }

    /**
     * 下载文件
     *
     * @param fileUrl     文件url
     * @param destFileDir 存储目标目录
     */
    public <T> void downLoadFile(String fileUrl, final String destFileDir, final ReqProgressCallBack<T> callBack) {
        final File file = new File(destFileDir);
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.removeHeader("User-Agent");
        requestBuilder.headers(setHeaders(getHttpHeaderParams()));//添加请求头
        requestBuilder.url(fileUrl).build();
        Request request = requestBuilder.build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e("下载", e.toString());
                failedCallBack(null, ParameterUtils.DOWNLOAD_ERR, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    LogUtils.e("下载", "total------>" + total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                        progressCallBack(total, current, callBack);
                    }
                    fos.flush();
                    successCallBack((T) file, callBack);
                } catch (IOException e) {
                    LogUtils.e("下载", e.toString());
                    failedCallBack(null, ParameterUtils.DOWNLOAD_ERR, callBack);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        LogUtils.e("下载", e.toString());
                    }
                }
            }
        });
    }

    /**
     * 请求参数中加入公共header参数
     *
     * @return
     */
    private Map<String, String> getHttpHeaderParams() {
        Map<String, String> params = new HashMap<>();
        String channel = WalleChannelReader.getChannel(BaseApplication.appContext, "xxd");
        params.put("User-Agent", Utils.getUserAgent(Utils.getAndroidUserAgent()) + " Store/"
            + channel);
        params.put("X-xxd-uid", DeviceUtils.getUniquePsuedoID());
        params.put("X-xxd-push-reg-id", JPushInterface.getRegistrationID(BaseApplication.appContext));
        String ticket = (String) SPCacheUtils.get("ticket", "");
        if (!TextUtils.isEmpty(ticket)) {
            params.put("X-xxd-ticket", ticket);
        }
        return params;
    }

    /**
     * 创建带进度的RequestBody
     *
     * @param contentType MediaType
     * @param file        准备上传的文件
     * @param callBack    回调
     * @param <T>
     * @return
     */
    private <T> RequestBody createProgressRequestBody(final MediaType contentType, final File file, final ReqProgressCallBack<T> callBack) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        LogUtils.e("writeTo", "current------>" + current);
                        progressCallBack(remaining, current, callBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 统一同意处理成功信息
     *
     * @param result
     * @param callBack
     * @param <T>
     */
    private <T> void successCallBack(final T result, final BaseCallback<T> callBack) {
        if (callBack != null) {
            if (okHttpHandler.getLooper().getThread().isAlive()) {
                okHttpHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(result);
                    }
                });
            }
        }
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg
     * @param callBack
     * @param <T>
     */
    private <T> void failedCallBack(final String errCode, final String errorMsg, final BaseCallback<T> callBack) {
        if (callBack != null) {
            if (okHttpHandler.getLooper().getThread().isAlive()) {
                okHttpHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onErr(errCode, errorMsg);
                    }
                });
            }
        }
    }

    /**
     * 统一处理进度信息
     *
     * @param total    总计大小
     * @param current  当前进度
     * @param callBack
     * @param <T>
     */
    private <T> void progressCallBack(final long total, final long current, final ReqProgressCallBack<T> callBack) {
        if (callBack != null) {
            callBack.onProgress(total, current);
        }
    }

}

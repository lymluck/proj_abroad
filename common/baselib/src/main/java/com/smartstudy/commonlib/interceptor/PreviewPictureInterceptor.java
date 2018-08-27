package com.smartstudy.commonlib.interceptor;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.smartstudy.annotation.Interceptor;
import com.smartstudy.commonlib.ui.activity.BrowserPictureActivity;
import com.smartstudy.commonlib.utils.LogUtils;
import com.smartstudy.router.RouteInterceptor;
import com.smartstudy.router.RouteResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author louis
 * @date on 2018/3/26
 * @describe 全局路由拦截器
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
@Interceptor("PreviewPictureInterceptor")
public class PreviewPictureInterceptor implements RouteInterceptor {

    @NonNull
    @Override
    public RouteResponse intercept(Chain chain) {
        Uri uri = chain.getRequest().getUri();
        LogUtils.d("uri======" + uri.toString());
        if (uri != null && !uri.isOpaque()) {
            String page = uri.getQueryParameter("page");
            if ("preview_img".equals(page)) {
                String imgUrl = uri.getQueryParameter("imgUrl");
                List<String> mPathList = JSONArray.parseArray(uri.getQueryParameter("imgArr"), String.class);
                int index = mPathList.indexOf(imgUrl);
                chain.getContext().startActivity(new Intent(chain.getContext(), BrowserPictureActivity.class)
                    .putStringArrayListExtra("pathList", new ArrayList(mPathList))
                    .putExtra("index", index));
                return chain.intercept();
            }
        }
        return chain.process();
    }
}

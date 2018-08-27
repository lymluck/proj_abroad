package com.smartstudy.xxd.mvp.model;

import android.text.TextUtils;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author louis
 * @date on 2018/7/19
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class CommentModel {

    public static void getColumnComments(final String id, final int page, BaseCallback callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COLUMN_COMMENT, id));
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void comment(final String newId, final String content, final String commentId, BaseCallback callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COLUMN_COMMENT, newId));
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                if (!TextUtils.isEmpty(commentId)) {
                    map.put("replyTo", commentId);
                }
                map.put("content", content);
                return map;
            }
        }, callback);
    }
}

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
 * @author yqy
 * @date on 2018/4/8
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HighSchoolModel {

    public static void getHighOptions(int networkElseCached, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_HIGH_OPTIONS);
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }


    public static void getHighSchoolList(final String countryId, final String sexualTypeId, final String boarderTypeId, final String locationTypeId, final String feeRange
            , final String rankCategoryId, final String rankRange, final String page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_HIGH_SCHOOL_LIST);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                if (!TextUtils.isEmpty(countryId)) {
                    map.put("countryId", countryId);
                }
                if (!TextUtils.isEmpty(sexualTypeId)) {
                    map.put("sexualTypeId", sexualTypeId);
                }
                if (!TextUtils.isEmpty(boarderTypeId)) {
                    map.put("boarderTypeId", boarderTypeId);
                }

                if (!TextUtils.isEmpty(locationTypeId)) {
                    map.put("locationTypeId", locationTypeId);
                }

                if (!TextUtils.isEmpty(feeRange)) {
                    map.put("feeRange", feeRange);
                }

                if (!TextUtils.isEmpty(rankCategoryId)) {
                    map.put("rankCategoryId", rankCategoryId);
                }


                if (!TextUtils.isEmpty(rankRange)) {
                    map.put("rankRange", rankRange);
                }

                map.put("page", page + "");
                return map;
            }
        }, callback);
    }
}

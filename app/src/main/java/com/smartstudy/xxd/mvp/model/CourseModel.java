package com.smartstudy.xxd.mvp.model;

import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.commonlib.base.config.BaseRequestConfig;
import com.smartstudy.commonlib.base.server.RequestManager;
import com.smartstudy.commonlib.utils.HttpUrlUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louis on 17/7/13.
 */

public class CourseModel {

    public static void getCourses(int cacheType, final int page, final boolean isHome,
                                  BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(cacheType, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_COURSE_LIST_GROUP);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                if (isHome) {
                    map.put("pageSize", "4");
                }
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void getCourseIntro(final String courseId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COURSE_INTRO, courseId));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void getCourseContent(final String courseId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COURSE_CONTENT, courseId));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }

    public static void getCourseComments(final String courseId, final int page, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COURSE_COMMENTS, courseId));
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("page", page + "");
                return map;
            }
        }, callback);
    }

    public static void commentCourse(final String courseId, final String rate, final String comment, BaseCallback<String> callback) {
        RequestManager.getInstance().doPost(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COMMENT_COURSE, courseId));
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("rate", rate + "");
                map.put("comment", comment + "");
                return map;
            }
        }, callback);
    }

    public static void playCourse(final String courseId, final String sectionId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.ONLY_NETWORK, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_PLAY_COURSE);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("productId", courseId);
                map.put("sectionId", sectionId);
                return map;
            }
        }, callback);
    }

    public static void recordPlay(final String courseId, final String sectionId,
                                  final String playTime, final String playDuration, BaseCallback<String> callback) {
        RequestManager.getInstance().doPut(new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(HttpUrlUtils.URL_PLAY_COURSE);
            }

            @Override
            public Map getParams() {
                Map map = new HashMap();
                map.put("productId", courseId);
                map.put("sectionId", sectionId);
                map.put("playTime", playTime);
                map.put("playDuration", playDuration);
                return map;
            }
        }, callback);
    }

    public static void getCourseBrief(final String courseId, BaseCallback<String> callback) {
        RequestManager.getInstance().doGet(ParameterUtils.NETWORK_ELSE_CACHED, new BaseRequestConfig() {
            @Override
            public String getUrl() {
                return HttpUrlUtils.getUrl(String.format(HttpUrlUtils.URL_COURSE_BRIEF, courseId));
            }

            @Override
            public Map getParams() {
                return null;
            }
        }, callback);
    }
}

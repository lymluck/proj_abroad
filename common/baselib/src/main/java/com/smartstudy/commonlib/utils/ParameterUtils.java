package com.smartstudy.commonlib.utils;

public class ParameterUtils {
    public static final long SMS_TIMEOUT = 30 * 1000;  //短信验证倒计时

    // / 没有连接
    public static final String NETWORN_NONE = "NETWORN_NONE";
    // / wifi连接
    public static final String NETWORN_WIFI = "NETWORN_WIFI";
    // / 手机网络数据连接
    public static final String NETWORN_2G = "NETWORN_2G";
    public static final String NETWORN_3G = "NETWORN_3G";
    public static final String NETWORN_4G = "NETWORN_4G";
    public static final String NETWORN_MOBILE = "NETWORN_MOBILE";
    public static final String API_SERVER = "api_server";
    public static final String GET_DATA_FAILED = "获取数据失败,请稍候重试!";
    public static final String NET_ERR = "请检查网络连接状态!";
    public static final String NEWEST_VERSION = "当前已是最新版本!";
    public static final String UPLOAD_ERR = "上传失败!";
    public static final String DOWNLOAD_ERR = "下载失败!";
    public static final String ALL_PICS = "所有图片";
    public static final String MEIQIA_KEY = "11342702eacdcfdc64e67be582aebbf5";
    //无网络连接错误码
    public static final String RESPONE_CODE_NETERR = "net_err";
    // 应用更新
    public static final int FLAG_UPDATE = 1;
    // 应用强制更新
    public static final int FLAG_UPDATE_NOW = 2;
    // 通知栏更新
    public static final int MSG_WHAT_PROGRESS = 3;
    public static final int MSG_WHAT_ERR = 4;
    public static final int MSG_WHAT_FINISH = 5;
    public static final int MSG_WHAT_REFRESH = 6;

    //message empty what
    public static final int EMPTY_WHAT = 7;


    public static final int REQUEST_CODE_CHANGEPHOTO = 8; //更换照片请求码
    public static final int REQUEST_CODE_CAMERA = 9; //拍摄照片请求码
    public static final int REQUEST_CODE_CLIP_OVER = 10; //剪裁照片请求码
    public static final int REQUEST_CODE_EDIT_MYINFO = 11; //查看我的资料请求码
    public static final int REQUEST_CODE_STORAGE = 12; //申请读写存储请求码
    public static final int REQUEST_CODE_ADD_SCHOOL = 13; //添加选校请求码
    public static final int REQUEST_CODE_PERMISSIONS = 14; //添加权限请求码
    public static final int REQUEST_CODE_LOGIN = 15; //登录请求码
    public static final int REQUEST_CODE_SMART_CHOOSE = 16; //智能选校请求码


    public static final int ONLY_NETWORK = 17; //只查询网络数据
    public static final int ONLY_CACHED = 18; //只查询本地缓存
    public static final int CACHED_ELSE_NETWORK = 19; //先查询本地缓存，如果本地没有，再查询网络数据
    public static final int NETWORK_ELSE_CACHED = 20; //先查询网络数据，如果没有，再查询本地缓

    public static final int PULL_DOWN = 21; //下拉刷新
    public static final int PULL_UP = 22; //上拉加载
    public static final int FRAGMENT_TAB_ONE = 23; //首页第一个fragment标识
    public static final int FRAGMENT_TAB_TWO = 24;//首页第二个fragment标识
    public static final int FRAGMENT_TAB_THREE = 25;//首页第三个fragment标识
    public static final int FRAGMENT_TAB_THOUR = 26;//首页第四个fragment标识
    public static final int FRAGMENT_TAB_FIVE = 37;//首页第五个fragment标识
    public static final String FRAGMENT_TAG = "frgmt_tag";
    public static final int GL_FLAG = 27;
    public static final int US_FLAG = 28;
    public static final int ACTION_FROM_WEB = 29;
    public static final int REQUEST_CODE_WEBVIEW = 30;
    public static final int REQUEST_CODE_SPECIAL = 31;
    public static final int MSG_WHAT_SMOOTH = 32;
    public static final int MSG_WHAT_REPOSITION = 33;
    public static final int GET_ORDER_DATE = 34;
    public static final int GET_ORDER_TIME = 35;
    public static final int REQUEST_CODE_CARD_QA = 36;
    public static final int REQUEST_CODE_INSTALL = 38;
    public static final int REQUEST_CODE_MANAGE_APP_SOURCE = 39;
    public static final int REQUEST_CODE_ADD_QA = 40; // 提问请求码

    public static final String MATCH_TOP = "MS_MATCH_TYPE_TOP";
    public static final String MATCH_MID = "MS_MATCH_TYPE_MIDDLE";
    public static final String MATCH_BOT = "MS_MATCH_TYPE_BOTTOM";
    public static final String MATCH_SOURCE_AUTO = "auto-match";
    public static final String MATCH_SOURCE_SELECT = "manual-select";

    public static final String TYPE_OPTIONS_PERSON = "type_person";
    public static final String TYPE_OPTIONS_COURTY = "type_courty";
    public static final String TYPE_OPTIONS_PROJ = "type_degree";
    public static final String TYPE_OPTIONS_SCHOOL = "type_school";
    public static final String TYPE_OPTIONS_RATE = "type_rate";

    public static final String WEB_ACTION_SHARE = "app_share";
    public static final String WEB_ACTION_LOGIN = "app_login";
    public static final String WEB_ACTION_MEIQIA = "app_meiqia";
    public static final String WEB_ACTION_TOAST = "app_toast";
    public static final String WEB_ACTION_PROGRAM = "app_program";
    public static final String WEB_ACTION_LINK = "app_link";
    public static final String WEB_ACTION_ADD_QA = "app_ask_question";
    public static final String WEB_CALLBACK_ADD_SCHOOL = "app_add_my_school_callback";
    public static final String WEB_CALLBACK_DEL_SCHOOL = "app_del_my_school_callback";
    public static final String WEB_ACTION_RATE = "app_admission_rate";

    public static final String RESPONSE_CODE_SUCCESS = "0";  //响应成功码
    public static final String RESPONSE_CODE_NOLOGIN = "SCHOOL_8";  //响应成功码

    public static final String TRANSITION_FLAG = "trans_flag";  //转场flag名
    public static final String SCHOOLS_FLAG = "school_list";  //flag
    public static final String HIGHSCHOOL_FLAG = "highscholl_list";  //flag
    public static final String HOME_RATE_FLAG = "home_rate";  //flag
    public static final String QA_FLAG = "qa_list";  //flag
    public static final String HOME_QA_FLAG = "home_qa_list";  //flag
    public static final String NEWS_FLAG = "news_list";  //flag
    public static final String COURSE_FLAG = "course_list";  //flag
    public static final String RANKS_FLAG = "rank_list";  //flag
    public static final String MYSCHOOL_FLAG = "mySchool";  //flag
    public static final String SPEDATA_FLAG = "speData";  //flag
    public static final String RANKTYPE_FLAG = "ranktype";  //flag
    public static final String RANKART_FLAG = "rankart";  //flag
    public static final String PROGRAMSPE_FLAG = "programSpe";  //flag
    public static final String EDIT_NAME = "edit_name";  //flag
    public static final String EDIT_MAJOR = "edit_major";  //flag
    public static final String EDIT_APPLY_MAJOR = "edit_apy_major";  //flag
    public static final String EDIT_ABOARD_YEAR = "edit_aboard_year";  //flag
    public static final String EDIT_GRADE = "edit_grade";  //flag
    public static final String EDIT_APPLY_PROJ = "edit_apy_proj";  //flag
    public static final String EDIT_APPLY_FEE = "edit_apy_fee";  //flag
    public static final String CHOOSE_APPLY_PROJ = "choose_apy_proj";  //flag
    public static final String EDIT_INTENT = "edit_intent";  //flag
    public static final String EDIT_TOP_SCHOOL = "edit_top_school";  //flag
    public static final String EDIT_CZ = "edit_cz";  //flag
    public static final String EDIT_CZ_SCORE = "edit_cz_score";  //flag
    public static final String EDIT_GZ = "edit_gz";  //flag
    public static final String EDIT_GZ_SCORE = "edit_gz_score";  //flag
    public static final String EDIT_BK = "edit_bk";  //flag
    public static final String EDIT_BK_SCORE = "edit_bk_score";  //flag
    public static final String EDIT_SX_EVENT = "edit_sx_event";  //flag
    public static final String EDIT_KY_EVENT = "edit_ky_event";  //flag
    public static final String EDIT_KW_EVENT = "edit_kw_event";  //flag
    public static final String EDIT_SHEHUI_EVENT = "edit_shehui_event";  //flag
    public static final String EDIT_GJ_EVENT = "edit_gj_event";  //flag
    public static final String EDIT_EG_SCORE = "edit_eg_score";  //flag
    public static final String EDIT_GRE_SCORE = "edit_gre_score";  //flag
    public static final String EDIT_SAT_SCORE = "edit_sat_score";  //flag

    /**
     * 通知未读数量存储
     */
    public static final String XXD_UNREAD = "xxd_unread";
    /**
     * 美洽未读数量存储
     */
    public static final String MEIQIA_UNREAD = "meiqia_unread";
    /**
     * 问答未读数量存储
     */
    public static final String QA_UNREAD = "qa_unread";
    /**
     * 一般图片url宽高
     */
    public static final String IMG_URL_WH = "%1$s?x-oss-process=image/resize,m_lfit,w_%2$s,h_%3$s";
    /**
     * 圆形图片url宽高
     */
    public static final String CIRCLE_IMG_URL_WH = "%1$s?x-oss-process=image/resize,m_pad,w_%2$s,h_%3$s";
    /**
     * webview参数传递key
     */
    public static final String WEBVIEW_URL = "web_url";
    /**
     * webview参数传递key
     */
    public static final String WEBVIEW_ACTION = "url_action";
    /**
     * 标题
     */
    public static final String TITLE = "title";
}

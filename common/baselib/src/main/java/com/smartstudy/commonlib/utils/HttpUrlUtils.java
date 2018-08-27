package com.smartstudy.commonlib.utils;

import com.smartstudy.commonlib.BaseApplication;

public class HttpUrlUtils {

    /**************************** api ***************************/
    public static final String URL_SCHOOLS = "/schools"; //院校列表
    public static final String URL_SCHOOL_INFO = "/view/school/brief"; //院校简介
    public static final String URL_SCHOOL_DETAIL = "/view/school/index"; //院校详情
    public static final String URL_HIGHSCHOOL_DETAIL = "/view/app/highschool/index"; //高中院校详情
    public static final String URL_NEWS = "/news"; //资讯列表
    public static final String URL_RANKS = "/ranks"; //排名列表
    public static final String URL_RANKS_TYPE = "/rank/categories/index"; //排名大全列表
    public static final String URL_RANKS_SEARCH = "/rank/categories"; //排名搜索
    public static final String URL_RANKS_WORLD_MAJOR = "/rank/categories/major/global"; //全球专业排行榜
    public static final String URL_RANKS_US_MAJOR = "/rank/categories/major/us/undergraduate"; //美本专业排行榜
    public static final String URL_RANKS_YJS_MAJOR = "/rank/categories/major/global"; //美研专业排行榜
    public static final String URL_RANKS_ART_MAJOR = "/arts/ranked/majors"; //艺术专业排行榜
    public static final String URL_RANKS_ART = "/arts/ranked/programs/paged"; //艺术专业排名
    public static final String URL_QUESTS = "/questions/v2/list/answered"; //问答列表
    public static final String URL_ALL_QUESTS = "/questions/v2/list"; //问答列表
    public static final String URL_QUESTS_LINK = "/questions/v2/%1$s"; //问答详情

    public static final String URL_GLOBLE_LIST = "/lov/data/DATA_WORLD_MAJOR_RANKINGS/value"; //世界专业排名列表
    public static final String URL_USA_LIST = "/lov/data/DATA_USA_MAJOR_RANKINGS/value"; //美国专业排名列表
    public static final String URL_APP_VERSION = "/android/version/%1$s/check";  //版本下载
    public static final String URL_DEVICE = "/device";  //设备信息
    public static final String URL_MYINFO = "/personal/info";  //获取我的信息
    public static final String URL_SEARCH = "/search";  //首页搜索
    public static final String URL_MATCHSHCOOL_OPTIONS = "/match_school/options";  //获取智能选校属性常量列表
    public static final String URL_RATE_OPTIONS = "/admission_rate/options";  //获取录取率测试属性常量列表
    public static final String URL_PHONE_CODE = "/user/captcha";  //获取验证码
    public static final String URL_CODE_LOGIN = "/user/register";  //验证码登录
    public static final String URL_FEEDBACK = "/feedback";  //意见反馈
    public static final String URL_MYSCHOOL = "/match_school/list";  //我的选校
    public static final String URL_MYSCHOOL_EDIT = "/match_school";  //我的选校修改
    public static final String URL_MYSCHOOL_DEL = "/match_school/%1$s";  //我的选校删除
    public static final String URL_WACTH_VISIBILITY = "/user/info/watch/visibility";  //设置隐私
    public static final String URL_MYQA = "/questions/v2/list/mine";  //获取我的问答列表
    public static final String URL_RECOMMEND_QA = "/questions/v2/list/recommended";  //获取我的问答列表
    public static final String URL_ADD_QUES = "/questions/v2";  //提问
    public static final String URL_ADD_QUES_OPTS = "/questions/v2/post/options";  //提问选项
    public static final String URL_RELATED_QUES = "/question/%1$s/related";  //相关提问
    public static final String URL_SCHOOLS_VIEWED = "/schools/viewed";//看过的学校
    public static final String URL_RELATED_QUES_HELPFUL = "/question/%1$s/related/helpful";  //相关提问是否有帮助
    public static final String URL_MY_COLLECTION = "/collection/composite";  //我的收藏
    public static final String URL_IS_COLLECTED = "/collection/%1$s/%2$s";  //是否收藏
    public static final String URL_SMART_CHOOSE = "/match_school/match";  //智能选校
    public static final String URL_SCHOOL_TEST_COUNT = "/match_school/test_count";  //智能选校测试人数统计
    public static final String URL_SPECIAL_TEST_COUNT = "/major_test/test_count";  //智能选专业测试人数统计
    public static final String URL_RATE_TEST_COUNT = "/admission_rate/test_count";  //录取率测试人数统计
    public static final String URL_RATE_TEST = "/admission_rate";  //录取率测试
    public static final String URL_SPECIAL_TEST = "/lov/data/DATA_MAJOR_TEST_QUESTIONS/value";  //智能选专业测试问题列表
    public static final String URL_ADS = "/business/app-index-banner";  //首页广告列表
    public static final String URL_SPECIAL_DATA = "/major_lib/majors";  //专业库
    public static final String URL_PROGRAM = "/major_lib/programs";  //本科项目列表
    public static final String URL_COMMAND_SCHOOL = "/major_lib/program/%1$s/schools";  //推荐院校列表
    public static final String URL_US_COLLEGE_HOT = "/view/app/tab/index";  //美本首页各种列表
    public static final String URL_US_HIGH_SCHOOL_HOT = "/view/app/tab/index/highschool";  //美高首页各种列表
    public static final String URL_DELETE_RECOMMOND_COURSE = "/course/recommended/%1$s";  //美高首页各种列表
    public static final String EXAM_SCHEDULE_CHECKIN = "/exam_schedule/%1$s/checkin";  //首页打卡事件
    public static final String URL_USER_LOGOUT = "/user/logout";  //推荐院校列表
    public static final String URL_MSG_CENTER = "/notification/list";  //消息中心
    public static final String URL_MSG_UNREAD = "/notification/count/unread";  //消息未读数
    public static final String URL_COURSE_LIST = "/course/list";  //课程列表
    public static final String URL_EXAMINEE = "/exam_schedule/%1$s/examinee";//查看参加某次考试的用户列表
    public static final String URL_WATCH_SCHOOL = "/school/%1$s/watchers";//查看选校人列表
    public static final String URL_WATCH_STUDENT_DETAIL = "/user/%1$s/watch/data";//查看用户的选校信息
    public static final String URL_EXAM_SCHEDULE_MINE = "/exam_schedule/mine";//查看我的考试计划
    public static final String URL_COURSE_LIST_GROUP = "/course/list/grouped";  //课程列表
    public static final String URL_COURSE_INTRO = "/course/%1$s/introduction";  //课程简介
    public static final String URL_COURSE_CONTENT = "/course/%1$s/outline";  //课程内容
    public static final String URL_COURSE_COMMENTS = "/course/%1$s/comments";  //课程评价
    public static final String URL_COMMENT_COURSE = "/course/%1$s/comment";  //评价课程
    public static final String URL_WATCH_COMMENTS = "/user/%1$s/watch/comments"; //选校评论列表
    public static final String URL_WATCH_LIKE = "/user/%1$s/watch/like"; //选校点赞
    public static final String URL_WATCH_COMMENT = "/user/%1$s/watch/comment"; //选校评论
    public static final String URL_PLAY_COURSE = "/course/play";  //播放课程
    public static final String URL_COURSE_BRIEF = "/course/%1$s/brief";  //课程简要信息
    public static final String URL_V2_PERSONAL = "/personal/info/v2";  //个人信息
    public static final String URL_V2_PERSONAL_INIT_OPTS = "/personal/info/v2/init/options";  //第一次登录后引导信息
    public static final String URL_VISA = "/stage/visa/";  //留学签证
    public static final String URL_VISA_QA = "/stage/visa/faq";  //留学签证问答
    public static final String URL_VISA_NEWS = "/stage/visa/news";  //留学签证资讯
    public static final String URL_VISA_ADDR = "/stage/visa/centers";  //留学签证地址
    public static final String URL_QA_CARD = "/card/appointment";  //领取预约卡
    public static final String URL_QA_CARD_USAGE = "/card/appointment/usage";  //获取预约卡使用信息
    public static final String URL_QA_CARD_USE = "/card/appointment/use";  //使用预约卡
    public static final String URL_LAUNCH_AD = "/business/launch";  //开机广告
    public static final String URL_TRACK_LAUNCH_AD = "/track/ad_launch/%1$s";  //开机广告追踪
    public static final String URL_THEMATIC_CENTER = "/subject/list"; //专题中心
    public static final String URL_ADD_GOOD = "/counsellor/user/like"; //给老师点赞
    public static final String URL_THEARCHER_INFO = "/counsellor/user"; //老师信息
    public static final String URL_UPDATE_RATING = "/questions/v2/%1$s/answer/%2$s/rating"; //老师评价
    public static final String URL_SEND_TO_TEACHER = "/questions/v2/%1$s/requestInfo/%2$s/info"; //发送信息给老师
    public static final String URL_USER_INFO = "/user/%1$s/info/v2"; //用户信息

    public static final String URL_GPA_CALCULATION_DETAI = "/tool/gpa_calc"; //GPA计算
    public static final String URL_GPA_DESCRIPTION = "/tool/gpa_calc/algorithms"; //GPA计算说明
    public static final String URL_ABROAD_PLAN = "/abroad_plan/timelines"; //留学规划

    public static final String URL_HIGH_OPTIONS = "/highschool/list/options/app"; //高中留学操作
    public static final String URL_HIGH_SCHOOL_LIST = "/highschool/list"; //高中选校列表
    public static final String URL_HIGH_RANK = "/highschool/ranks"; //高中学校排名
    public static final String URL_HIGH_SCHOOL_DETAIL = "/highschool/rank/%1$s/schools";  //高中排名详情
    public static final String URL_VERIFY_QRCODE = "/user/qrcode/verify";  //验证二维码
    public static final String URL_CONFIRM_QRCODE = "/user/qrcode/confirm";  //确认登录

    public static final String URL_POST_QUESTION = "/questions/v2/%1$s/answer/%2$s/sub_question";//提交追问
    public static final String URL_SHARE_COUNT = "/share/link/record";//分享统计
    public static final String URL_EXAM_DATE = "/exam_schedule";// 考试时间查询
    public static final String URL_JOIN_EXAM = "/exam_schedule/mine/%1$d";// 参加考试
    public static final String URL_CHOOSE_STAT = "/school/%1$s/watchers/stat";// 选校统计
    public static final String URL_APP_CRASH = "/app/crashlog?crashTime=%1$s";// crash上传
    public static final String URL_APP_SCHOOL_ERR = "/correction";// 院校纠错
    public static final String URL_ACTIVITY_LIST = "/activity/project/list";// 活动库
    public static final String URL_ACTIVITY_DETAIL = "/activity/project/%1$s";// 活动详情
    public static final String URL_HOT_MAJOR = "/major_lib/major/hottest/by_category";// 专业库
    public static final String URL_MAJOR_INFO = "/school_major/category/%1$s";// 专业详情
    public static final String URL_MAJOR_PROGRAM_INFO = "/school_major/program/%1$s";// 项目详情
    public static final String URL_MAJOR_PROGRAM_RANK = "/school_major/programs";// 专业项目排名列表
    public static final String URL_MAJOR_PROGRAM_LIST = "/school_major/categories";// 专业项目列表
    public static final String URL_COLUMN_LIST = "/column/news";// 专栏列表
    public static final String URL_COLUMN = "/column/news/%1$s";// 专栏文章
    public static final String URL_LIKE = "/favorite/%1$s/%2$s";// 通用点赞
    public static final String URL_COLUMN_COMMENT = "/column/news/%1$s/comments";// 专栏评论列表

    /**************************** web ***************************/
    public static final String WEBURL_SCHOOL_DETAIL = "/school/%1$s.html";  //学校详情
    public static final String WEBURL_QUESTION_DETAIL = "/qs/question-%1$s.html";  //问答详情
    public static final String WEBURL_NEWS_DETAIL = "/news/detail-%1$s.html";  //资讯详情
    public static final String WEBURL_SCHOOL_INTRO = "/school/%1$s/introduction.html";  //学校简介
    public static final String WEBURL_SCHOOL_UNDERGRADUATE = "/school/%1$s/undergraduate-application-department-first.html";  //本科申请
    public static final String WEBURL_SCHOOL_GRADUATE = "/school/%1$s/graduate-application-department-first.html";  //研究生申请
    public static final String WEBURL_SCHOOL_SIA = "/school/%1$s/sia";  //艺术生申请
    public static final String WEBURL_USER_CONTRACT = "/user-agreement.html";  //用户协议
    public static final String WEBURL_CHOOSE_SPEC = "/tool/holland-major-test.html";  //智能选专业
    public static final String WEBURL_RESULT_RATE = "/tool/admission-rate-result/%1$s.html";  //录取率测试结果页
    public static final String WEBURL_RESULT_SCHOOL = "/tool/match-school-result-%1$s-%2$s-%3$s-%4$s-%5$s-%6$s-%7$s-%8$s-%9$s-%10$s.html";  //智能选校结果
    public static final String WEBURL_SPE_INTRO = "/major-lib/%1$s.html";  //专业详情页
    public static final String WEBURL_INTENT_HIGH = "/compare-highschool-countries.html";  //专业详情页
    public static final String WEBURL_INTENT_UNDER = "/compare-undergraduate-countries.html";  //专业详情页
    public static final String WEBURL_INTENT_GRADUATE = "/compare-graduate-countries.html";  //意向国家对比
    public static final String WEBURL_ZHIKE_SERVICE = "/study-abroad-service";  //留学服务
    public static final String WEBURL_COURSE_SHARE = "/course-%1$s/outline?sectionId=%2$s";  //课程分享

    /*********获取api接口url***********/
    public static String getUrl(String url) {
        String SERVER = "https://api.smartstudy.com/school";
        String api = (String) SPCacheUtils.get(ParameterUtils.API_SERVER, "");
        switch (api) {
            case "master":
                SERVER = "https://api.smartstudy.com/school";
                break;
            case "test":
                SERVER = "http://api.beikaodi.com";
//                SERVER = "http://xxd.beikaodi.com"; //test
//                SERVER = "http://server.tdc.smartstudy.com:3000"; //dev
                break;
            case "dev":
                SERVER = "http://server.tdc.smartstudy.com:3000"; //dev
                break;
        }
        return SERVER + url;
    }

    /********获取web页面url*********/
    public static String getWebUrl(String url) {
        String WEB_SERVER = "https://xxd.smartstudy.com";  //master
        String api = (String) SPCacheUtils.get(ParameterUtils.API_SERVER, "");
        switch (api) {
            case "master":
                WEB_SERVER = "https://xxd.smartstudy.com";
                break;
            case "test":
                WEB_SERVER = "http://xxd.beikaodi.com";
                break;
            case "dev":
                WEB_SERVER = "http://yongle.smartstudy.com:3100";
//                WEB_SERVER = "http://xiaowei.hz.beikaodi.com:3100";
                break;
        }
        return WEB_SERVER + url + "?app-type=android&appName=" + BaseApplication.appContext.getPackageName() + "&appVersion=" + AppUtils.getVersionName();
    }
}

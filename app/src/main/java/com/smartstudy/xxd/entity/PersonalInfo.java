package com.smartstudy.xxd.entity;

import com.smartstudy.commonlib.entity.IdNameInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 17/8/2.
 */

public class PersonalInfo {


    /**
     * id : 37885
     * name : 非常
     * avatar : http://upload.beikaodi.com/customer/avatar/2a/7d/2a7dfdfd7948afcbd4bb61924c46aa1d4c2bb.png
     * genderId : null
     * contact : null
     * email : null
     * isAbroadPlanAvailable
     * targetSection : {"admissionTime":{"title":"出国时间","id":"OTHER","name":"其他","options":[{"id":"2017","name":"2017年"},{"id":"2018","name":"2018年"},{"id":"2019","name":"2019年"},{"id":"OTHER","name":"其他"}]},"targetCountry":{"title":"意向地","id":"COUNTRY_226","name":"美国","options":[{"id":"COUNTRY_226","name":"美国"},{"id":"COUNTRY_225","name":"英国"},{"id":"COUNTRY_40","name":"加拿大"},{"id":"COUNTRY_16","name":"澳大利亚"},{"id":"OTHER","name":"其他"}]},"targetDegree":{"title":"申请项目","id":"DEGREE_MDT_MASTER","name":"研究生","options":[{"id":"DEGREE_MDT_SENIOR_HIGH_SCHOOL","name":"高中"},{"id":"DEGREE_MDT_BACHELOR","name":"本科"},{"id":"DEGREE_MDT_MASTER","name":"研究生"},{"id":"DEGREE_MDT_OTHER","name":"其他"}]},"targetSchoolRank":{"title":"目标学校","id":"","name":"","options":[{"id":"30","name":"TOP 30"},{"id":"50","name":"TOP 50"},{"id":"80","name":"TOP 80"},{"id":"OTHER","name":"其他"}]},"targetMajorDirection":{"title":"专业方向","id":"SCIENCE","name":"理科","options":[{"id":"ENGINEERING","name":"工科"},{"id":"BUSINESS","name":"商科"},{"id":"SCIENCE","name":"理科"},{"id":"SOCIETY","name":"社会科学"},{"id":"OTHER","name":"其它"}]}}
     * backgroundSection : {"currentSchool":{"title":"本科学习","id":"社会4名明明","name":"社会4名明明","input":"text"},"score":{"title":"本科成绩","id":"","name":"","options":[{"id":"3.7-4.0","name":"3.7-4.0","hint":"（百分制90分以上）"},{"id":"3.5-3.7","name":"3.5-3.7","hint":"（百分制87-90分）"},{"id":"3.3-3.5","name":"3.3-3.5","hint":"（百分制85-87分）"},{"id":"3.0-3.3","name":"3.0-3.3","hint":"（百分制80-85分）"},{"id":"0.0-3.0","name":"3.0以下","hint":"（百分制80分以下）"}]},"scoreLanguage":{"title":"语言成绩","id":"","name":"","groupedOptions":[{"group":"托福","options":[{"id":"TOEFL-110-120","name":"110分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-100-110","name":"100分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-90-100","name":"90分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-80-90","name":"80分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-0-80","name":"80分以下或无成绩","group":"托福","prefix":"托福"}]},{"group":"雅思","options":[{"id":"IELTS-7.5-9.0","name":"7.5分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-7.0-7.5","name":"7.0分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-6.5-7.0","name":"6.5分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-6.0-6.5","name":"6.0分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-0.0-6.0","name":"6.0分以下或无成绩","group":"雅思","prefix":"雅思"}]},{"group":"Waiver","options":[{"id":"WAIVER","name":"Waiver（语言成绩豁免）","group":"Waiver","prefix":""}]}]},"scoreStandard":{"title":"GRE/GMAT","id":"","name":"","groupedOptions":[{"group":"GRE","options":[{"id":"GRE-325-340","name":"325分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-320-325","name":"320分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-315-320","name":"315分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-300-315","name":"300分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-0-300","name":"300分以下或无成绩","group":"GRE","prefix":"GRE"}]},{"group":"GMAT","options":[{"id":"GMAT-730-800","name":"730分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-700-730","name":"700分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-680-700","name":"680分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-650-680","name":"650分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-0-650","name":"650分以下或无成绩","group":"GMAT","prefix":"GMAT"}]}]},"activityInternship":{"title":"实习活动","id":"","name":"","options":[{"id":"SERIOUS*2","name":"2段以上在知名企业高质量实习"},{"id":"CASUAL*2","name":"2段以上实习，但都是打打酱油"},{"id":"SERIOUS*1","name":"1段踏实学习了真东西的实习"},{"id":"CASUAL*1","name":"1段走马观花，糊弄了事的实习"},{"id":"NEVER","name":"从来没实习过"}]},"activityResearch":{"title":"科研活动","id":"","name":"","options":[{"id":"SERIOUS*2","name":"2段以上相关科研，并成功抱上牛腿"},{"id":"CASUAL*2","name":"2段水水的科研"},{"id":"SERIOUS*2","name":"1段科研，踏踏实实做出了成果"},{"id":"CASUAL*2","name":"1段科研或课上大作业，糊弄了事"},{"id":"NEVER","name":"我的大学是用来打游戏的"}]},"activityCommunity":{"title":"课外活动","id":"","name":"","options":[{"id":"LEADER","name":"我在本科期间在学生会、社团、志愿协会中参与并担任领导"},{"id":"PARTICIPANT","name":"我对学生会、社团、志愿活动等兴趣浓厚，积极参与"},{"id":"NEVER","name":"我对课外活动并没有兴趣，也没有相关经历"}]},"activityExchange":{"title":"国际交流","id":"","name":"","options":[{"id":"ATTENDED","name":"我就读于英语母语的海外名校"},{"id":"REGULAR_EXCHANGED","name":"我在海外名校进行交换并修得学分，有海外教授为我出具推荐信"},{"id":"SUMMER_EXCHANGED_WELLKNOWN","name":"我在海外名校完成暑期研究"},{"id":"SUMMER_EXCHANGED","name":"我参加了海外院校的暑期课程并修得学分"},{"id":"NEVER","name":"我并没有海外学术经历"}]}}
     * account : null
     * notificationUnreadCount : 0
     */

    private String id;
    private String name;
    private String avatar;
    private String genderId;
    private String contact;
    private String email;
    private TargetSectionEntity targetSection;
    private BackgroundSectionEntity backgroundSection;
    private String account;
    private int notificationUnreadCount;
    private boolean isAbroadPlanAvailable;

    public boolean isAbroadPlanAvailable() {
        return isAbroadPlanAvailable;
    }

    public void setAbroadPlanAvailable(boolean abroadPlanAvailable) {
        isAbroadPlanAvailable = abroadPlanAvailable;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTargetSection(TargetSectionEntity targetSection) {
        this.targetSection = targetSection;
    }

    public void setBackgroundSection(BackgroundSectionEntity backgroundSection) {
        this.backgroundSection = backgroundSection;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setNotificationUnreadCount(int notificationUnreadCount) {
        this.notificationUnreadCount = notificationUnreadCount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public Object getGenderId() {
        return genderId;
    }

    public Object getContact() {
        return contact;
    }

    public Object getEmail() {
        return email;
    }

    public TargetSectionEntity getTargetSection() {
        return targetSection;
    }

    public BackgroundSectionEntity getBackgroundSection() {
        return backgroundSection;
    }

    public Object getAccount() {
        return account;
    }

    public int getNotificationUnreadCount() {
        return notificationUnreadCount;
    }

    public static class TargetSectionEntity {
        /**
         * admissionTime : {"title":"出国时间","id":"OTHER","name":"其他","options":[{"id":"2017","name":"2017年"},{"id":"2018","name":"2018年"},{"id":"2019","name":"2019年"},{"id":"OTHER","name":"其他"}]}
         * targetCountry : {"title":"意向地","id":"COUNTRY_226","name":"美国","options":[{"id":"COUNTRY_226","name":"美国"},{"id":"COUNTRY_225","name":"英国"},{"id":"COUNTRY_40","name":"加拿大"},{"id":"COUNTRY_16","name":"澳大利亚"},{"id":"OTHER","name":"其他"}]}
         * targetDegree : {"title":"申请项目","id":"DEGREE_MDT_MASTER","name":"研究生","options":[{"id":"DEGREE_MDT_SENIOR_HIGH_SCHOOL","name":"高中"},{"id":"DEGREE_MDT_BACHELOR","name":"本科"},{"id":"DEGREE_MDT_MASTER","name":"研究生"},{"id":"DEGREE_MDT_OTHER","name":"其他"}]}
         * targetSchoolRank : {"title":"目标学校","id":"","name":"","options":[{"id":"30","name":"TOP 30"},{"id":"50","name":"TOP 50"},{"id":"80","name":"TOP 80"},{"id":"OTHER","name":"其他"}]}
         * targetMajorDirection : {"title":"专业方向","id":"SCIENCE","name":"理科","options":[{"id":"ENGINEERING","name":"工科"},{"id":"BUSINESS","name":"商科"},{"id":"SCIENCE","name":"理科"},{"id":"SOCIETY","name":"社会科学"},{"id":"OTHER","name":"其它"}]}
         */

        private AdmissionTimeEntity admissionTime;
        private TargetCountryEntity targetCountry;
        private TargetDegreeEntity targetDegree;
        private TargetSchoolRankEntity targetSchoolRank;
        private TargetMajorDirectionEntity targetMajorDirection;

        public void setAdmissionTime(AdmissionTimeEntity admissionTime) {
            this.admissionTime = admissionTime;
        }

        public void setTargetCountry(TargetCountryEntity targetCountry) {
            this.targetCountry = targetCountry;
        }

        public void setTargetDegree(TargetDegreeEntity targetDegree) {
            this.targetDegree = targetDegree;
        }

        public void setTargetSchoolRank(TargetSchoolRankEntity targetSchoolRank) {
            this.targetSchoolRank = targetSchoolRank;
        }

        public void setTargetMajorDirection(TargetMajorDirectionEntity targetMajorDirection) {
            this.targetMajorDirection = targetMajorDirection;
        }

        public AdmissionTimeEntity getAdmissionTime() {
            return admissionTime;
        }

        public TargetCountryEntity getTargetCountry() {
            return targetCountry;
        }

        public TargetDegreeEntity getTargetDegree() {
            return targetDegree;
        }

        public TargetSchoolRankEntity getTargetSchoolRank() {
            return targetSchoolRank;
        }

        public TargetMajorDirectionEntity getTargetMajorDirection() {
            return targetMajorDirection;
        }

        public static class AdmissionTimeEntity {
            /**
             * title : 出国时间
             * id : OTHER
             * name : 其他
             * options : [{"id":"2017","name":"2017年"},{"id":"2018","name":"2018年"},{"id":"2019","name":"2019年"},{"id":"OTHER","name":"其他"}]
             */

            private String title;
            private String id;
            private String name;
            private ArrayList<IdNameInfo> options;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }
        }

        public static class TargetCountryEntity {
            /**
             * title : 意向地
             * id : COUNTRY_226
             * name : 美国
             * options : [{"id":"COUNTRY_226","name":"美国"},{"id":"COUNTRY_225","name":"英国"},{"id":"COUNTRY_40","name":"加拿大"},{"id":"COUNTRY_16","name":"澳大利亚"},{"id":"OTHER","name":"其他"}]
             */

            private String title;
            private String id;
            private String name;
            private ArrayList<IdNameInfo> options;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }

        }

        public static class TargetDegreeEntity {
            /**
             * title : 申请项目
             * id : DEGREE_MDT_MASTER
             * name : 研究生
             * options : [{"id":"DEGREE_MDT_SENIOR_HIGH_SCHOOL","name":"高中"},{"id":"DEGREE_MDT_BACHELOR","name":"本科"},{"id":"DEGREE_MDT_MASTER","name":"研究生"},{"id":"DEGREE_MDT_OTHER","name":"其他"}]
             */

            private String title;
            private String id;
            private String name;
            private ArrayList<IdNameInfo> options;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }
        }

        public static class TargetSchoolRankEntity {
            /**
             * title : 目标学校
             * id :
             * name :
             * options : [{"id":"30","name":"TOP 30"},{"id":"50","name":"TOP 50"},{"id":"80","name":"TOP 80"},{"id":"OTHER","name":"其他"}]
             */

            private String title;
            private String id;
            private String name;
            private ArrayList<IdNameInfo> options;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }
        }

        public static class TargetMajorDirectionEntity {
            /**
             * title : 专业方向
             * id : SCIENCE
             * name : 理科
             * options : [{"id":"ENGINEERING","name":"工科"},{"id":"BUSINESS","name":"商科"},{"id":"SCIENCE","name":"理科"},{"id":"SOCIETY","name":"社会科学"},{"id":"OTHER","name":"其它"}]
             */

            private String title;
            private String id;
            private String name;
            private ArrayList<IdNameInfo> options;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }
        }
    }

    public static class BackgroundSectionEntity {
        /**
         * currentSchool : {"title":"本科学习","id":"社会4名明明","name":"社会4名明明","input":"text"}
         * score : {"title":"本科成绩","id":"","name":"","options":[{"id":"3.7-4.0","name":"3.7-4.0","hint":"（百分制90分以上）"},{"id":"3.5-3.7","name":"3.5-3.7","hint":"（百分制87-90分）"},{"id":"3.3-3.5","name":"3.3-3.5","hint":"（百分制85-87分）"},{"id":"3.0-3.3","name":"3.0-3.3","hint":"（百分制80-85分）"},{"id":"0.0-3.0","name":"3.0以下","hint":"（百分制80分以下）"}]}
         * scoreLanguage : {"title":"语言成绩","id":"","name":"","groupedOptions":[{"group":"托福","options":[{"id":"TOEFL-110-120","name":"110分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-100-110","name":"100分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-90-100","name":"90分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-80-90","name":"80分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-0-80","name":"80分以下或无成绩","group":"托福","prefix":"托福"}]},{"group":"雅思","options":[{"id":"IELTS-7.5-9.0","name":"7.5分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-7.0-7.5","name":"7.0分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-6.5-7.0","name":"6.5分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-6.0-6.5","name":"6.0分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-0.0-6.0","name":"6.0分以下或无成绩","group":"雅思","prefix":"雅思"}]},{"group":"Waiver","options":[{"id":"WAIVER","name":"Waiver（语言成绩豁免）","group":"Waiver","prefix":""}]}]}
         * scoreStandard : {"title":"GRE/GMAT","id":"","name":"","groupedOptions":[{"group":"GRE","options":[{"id":"GRE-325-340","name":"325分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-320-325","name":"320分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-315-320","name":"315分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-300-315","name":"300分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-0-300","name":"300分以下或无成绩","group":"GRE","prefix":"GRE"}]},{"group":"GMAT","options":[{"id":"GMAT-730-800","name":"730分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-700-730","name":"700分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-680-700","name":"680分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-650-680","name":"650分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-0-650","name":"650分以下或无成绩","group":"GMAT","prefix":"GMAT"}]}]}
         * activityInternship : {"title":"实习活动","id":"","name":"","options":[{"id":"SERIOUS*2","name":"2段以上在知名企业高质量实习"},{"id":"CASUAL*2","name":"2段以上实习，但都是打打酱油"},{"id":"SERIOUS*1","name":"1段踏实学习了真东西的实习"},{"id":"CASUAL*1","name":"1段走马观花，糊弄了事的实习"},{"id":"NEVER","name":"从来没实习过"}]}
         * activityResearch : {"title":"科研活动","id":"","name":"","options":[{"id":"SERIOUS*2","name":"2段以上相关科研，并成功抱上牛腿"},{"id":"CASUAL*2","name":"2段水水的科研"},{"id":"SERIOUS*2","name":"1段科研，踏踏实实做出了成果"},{"id":"CASUAL*2","name":"1段科研或课上大作业，糊弄了事"},{"id":"NEVER","name":"我的大学是用来打游戏的"}]}
         * activityCommunity : {"title":"课外活动","id":"","name":"","options":[{"id":"LEADER","name":"我在本科期间在学生会、社团、志愿协会中参与并担任领导"},{"id":"PARTICIPANT","name":"我对学生会、社团、志愿活动等兴趣浓厚，积极参与"},{"id":"NEVER","name":"我对课外活动并没有兴趣，也没有相关经历"}]}
         * activityExchange : {"title":"国际交流","id":"","name":"","options":[{"id":"ATTENDED","name":"我就读于英语母语的海外名校"},{"id":"REGULAR_EXCHANGED","name":"我在海外名校进行交换并修得学分，有海外教授为我出具推荐信"},{"id":"SUMMER_EXCHANGED_WELLKNOWN","name":"我在海外名校完成暑期研究"},{"id":"SUMMER_EXCHANGED","name":"我参加了海外院校的暑期课程并修得学分"},{"id":"NEVER","name":"我并没有海外学术经历"}]}
         */

        private CurrentSchoolEntity currentSchool;
        private ScoreEntity score;
        private ScoreLanguageEntity scoreLanguage;
        private ScoreStandardEntity scoreStandard;
        private ActivityInternshipEntity activityInternship;
        private ActivityResearchEntity activityResearch;
        private ActivityCommunityEntity activityCommunity;
        private ActivityExchangeEntity activityExchange;
        private ActivitySocialEntity activitySocial;

        public ActivitySocialEntity getActivitySocial() {
            return activitySocial;
        }

        public void setActivitySocial(ActivitySocialEntity activitySocial) {
            this.activitySocial = activitySocial;
        }

        public void setCurrentSchool(CurrentSchoolEntity currentSchool) {
            this.currentSchool = currentSchool;
        }

        public void setScore(ScoreEntity score) {
            this.score = score;
        }

        public void setScoreLanguage(ScoreLanguageEntity scoreLanguage) {
            this.scoreLanguage = scoreLanguage;
        }

        public void setScoreStandard(ScoreStandardEntity scoreStandard) {
            this.scoreStandard = scoreStandard;
        }

        public void setActivityInternship(ActivityInternshipEntity activityInternship) {
            this.activityInternship = activityInternship;
        }

        public void setActivityResearch(ActivityResearchEntity activityResearch) {
            this.activityResearch = activityResearch;
        }

        public void setActivityCommunity(ActivityCommunityEntity activityCommunity) {
            this.activityCommunity = activityCommunity;
        }

        public void setActivityExchange(ActivityExchangeEntity activityExchange) {
            this.activityExchange = activityExchange;
        }

        public CurrentSchoolEntity getCurrentSchool() {
            return currentSchool;
        }

        public ScoreEntity getScore() {
            return score;
        }

        public ScoreLanguageEntity getScoreLanguage() {
            return scoreLanguage;
        }

        public ScoreStandardEntity getScoreStandard() {
            return scoreStandard;
        }

        public ActivityInternshipEntity getActivityInternship() {
            return activityInternship;
        }

        public ActivityResearchEntity getActivityResearch() {
            return activityResearch;
        }

        public ActivityCommunityEntity getActivityCommunity() {
            return activityCommunity;
        }

        public ActivityExchangeEntity getActivityExchange() {
            return activityExchange;
        }

        public static class CurrentSchoolEntity {
            /**
             * title : 本科学习
             * id : 社会4名明明
             * name : 社会4名明明
             * input : text
             */

            private String title;
            private String id;
            private String name;
            private String input;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setInput(String input) {
                this.input = input;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getInput() {
                return input;
            }
        }

        public static class ScoreEntity {
            /**
             * title : 本科成绩
             * id :
             * name :
             * options : [{"id":"3.7-4.0","name":"3.7-4.0","hint":"（百分制90分以上）"},{"id":"3.5-3.7","name":"3.5-3.7","hint":"（百分制87-90分）"},{"id":"3.3-3.5","name":"3.3-3.5","hint":"（百分制85-87分）"},{"id":"3.0-3.3","name":"3.0-3.3","hint":"（百分制80-85分）"},{"id":"0.0-3.0","name":"3.0以下","hint":"（百分制80分以下）"}]
             */

            private String title;
            private String id;
            private String name;
            private ArrayList<IdNameInfo> options;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }
        }

        public static class ScoreLanguageEntity {
            /**
             * title : 语言成绩
             * id :
             * name :
             * groupedOptions : [{"group":"托福","options":[{"id":"TOEFL-110-120","name":"110分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-100-110","name":"100分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-90-100","name":"90分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-80-90","name":"80分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-0-80","name":"80分以下或无成绩","group":"托福","prefix":"托福"}]},{"group":"雅思","options":[{"id":"IELTS-7.5-9.0","name":"7.5分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-7.0-7.5","name":"7.0分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-6.5-7.0","name":"6.5分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-6.0-6.5","name":"6.0分以上","group":"雅思","prefix":"雅思"},{"id":"IELTS-0.0-6.0","name":"6.0分以下或无成绩","group":"雅思","prefix":"雅思"}]},{"group":"Waiver","options":[{"id":"WAIVER","name":"Waiver（语言成绩豁免）","group":"Waiver","prefix":""}]}]
             */

            private String title;
            private String id;
            private String name;
            private List<GroupedOptionsEntity> groupedOptions;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setGroupedOptions(List<GroupedOptionsEntity> groupedOptions) {
                this.groupedOptions = groupedOptions;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public List<GroupedOptionsEntity> getGroupedOptions() {
                return groupedOptions;
            }

            public static class GroupedOptionsEntity {
                /**
                 * group : 托福
                 * options : [{"id":"TOEFL-110-120","name":"110分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-100-110","name":"100分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-90-100","name":"90分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-80-90","name":"80分以上","group":"托福","prefix":"托福"},{"id":"TOEFL-0-80","name":"80分以下或无成绩","group":"托福","prefix":"托福"}]
                 */

                private String group;
                private List<IdNameInfo> options;

                public void setGroup(String group) {
                    this.group = group;
                }

                public void setOptions(List<IdNameInfo> options) {
                    this.options = options;
                }

                public String getGroup() {
                    return group;
                }

                public List<IdNameInfo> getOptions() {
                    return options;
                }
            }
        }

        public static class ScoreStandardEntity {
            /**
             * title : GRE/GMAT
             * id :
             * name :
             * groupedOptions : [{"group":"GRE","options":[{"id":"GRE-325-340","name":"325分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-320-325","name":"320分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-315-320","name":"315分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-300-315","name":"300分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-0-300","name":"300分以下或无成绩","group":"GRE","prefix":"GRE"}]},{"group":"GMAT","options":[{"id":"GMAT-730-800","name":"730分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-700-730","name":"700分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-680-700","name":"680分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-650-680","name":"650分以上","group":"GMAT","prefix":"GMAT"},{"id":"GMAT-0-650","name":"650分以下或无成绩","group":"GMAT","prefix":"GMAT"}]}]
             */

            private String title;
            private String id;
            private String name;
            private List<GroupedOptionsEntity> groupedOptions;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setGroupedOptions(List<GroupedOptionsEntity> groupedOptions) {
                this.groupedOptions = groupedOptions;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public List<GroupedOptionsEntity> getGroupedOptions() {
                return groupedOptions;
            }

            public static class GroupedOptionsEntity {
                /**
                 * group : GRE
                 * options : [{"id":"GRE-325-340","name":"325分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-320-325","name":"320分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-315-320","name":"315分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-300-315","name":"300分以上","group":"GRE","prefix":"GRE"},{"id":"GRE-0-300","name":"300分以下或无成绩","group":"GRE","prefix":"GRE"}]
                 */

                private String group;
                private List<IdNameInfo> options;

                public void setGroup(String group) {
                    this.group = group;
                }

                public void setOptions(List<IdNameInfo> options) {
                    this.options = options;
                }

                public String getGroup() {
                    return group;
                }

                public List<IdNameInfo> getOptions() {
                    return options;
                }
            }
        }

        public static class ActivityInternshipEntity {
            /**
             * title : 实习活动
             * id :
             * name :
             * options : [{"id":"SERIOUS*2","name":"2段以上在知名企业高质量实习"},{"id":"CASUAL*2","name":"2段以上实习，但都是打打酱油"},{"id":"SERIOUS*1","name":"1段踏实学习了真东西的实习"},{"id":"CASUAL*1","name":"1段走马观花，糊弄了事的实习"},{"id":"NEVER","name":"从来没实习过"}]
             */

            private String title;
            private String id;
            private String name;
            private ArrayList<IdNameInfo> options;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }
        }

        public static class ActivityResearchEntity {
            /**
             * title : 科研活动
             * id :
             * name :
             * options : [{"id":"SERIOUS*2","name":"2段以上相关科研，并成功抱上牛腿"},{"id":"CASUAL*2","name":"2段水水的科研"},{"id":"SERIOUS*2","name":"1段科研，踏踏实实做出了成果"},{"id":"CASUAL*2","name":"1段科研或课上大作业，糊弄了事"},{"id":"NEVER","name":"我的大学是用来打游戏的"}]
             */

            private String title;
            private String id;
            private String name;
            private ArrayList<IdNameInfo> options;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }
        }

        public static class ActivityCommunityEntity {
            /**
             * title : 课外活动
             * id :
             * name :
             * options : [{"id":"LEADER","name":"我在本科期间在学生会、社团、志愿协会中参与并担任领导"},{"id":"PARTICIPANT","name":"我对学生会、社团、志愿活动等兴趣浓厚，积极参与"},{"id":"NEVER","name":"我对课外活动并没有兴趣，也没有相关经历"}]
             */

            private String title;
            private String id;
            private String name;
            private ArrayList<IdNameInfo> options;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }
        }

        public static class ActivityExchangeEntity {
            /**
             * title : 国际交流
             * id :
             * name :
             * options : [{"id":"ATTENDED","name":"我就读于英语母语的海外名校"},{"id":"REGULAR_EXCHANGED","name":"我在海外名校进行交换并修得学分，有海外教授为我出具推荐信"},{"id":"SUMMER_EXCHANGED_WELLKNOWN","name":"我在海外名校完成暑期研究"},{"id":"SUMMER_EXCHANGED","name":"我参加了海外院校的暑期课程并修得学分"},{"id":"NEVER","name":"我并没有海外学术经历"}]
             */

            private String title;
            private String id;
            private String name;
            private ArrayList<IdNameInfo> options;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getTitle() {
                return title;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }
        }

        public static class ActivitySocialEntity {

            private String id;
            private String name;
            private boolean multiple;
            private ArrayList<IdNameInfo> options;

            public boolean isMultiple() {
                return multiple;
            }

            public void setMultiple(boolean multiple) {
                this.multiple = multiple;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOptions(ArrayList<IdNameInfo> options) {
                this.options = options;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public ArrayList<IdNameInfo> getOptions() {
                return options;
            }
        }
    }
}

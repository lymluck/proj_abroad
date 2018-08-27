package com.smartstudy.commonlib.entity;

import java.util.List;

/**
 * Created by louis on 17/7/17.
 */

public class CourseIntroInfo {

    /**
     * productId : 2248
     * name : 哈佛学姐留学全攻略
     * coverUrl : http://upload.beikaodi.com/upload/ufile/fe/ba/febaf376a89599f915355345ae4f84441d4ca.png
     * introduction : 这是一个测试。。。
     * provider : 智课选校帝研发团队
     * targetUser : 先面向测试人员咯。
     * teachers : [{"name":"李静超","title":"智课资深留学申请导师","avatarUrl":"https://bkd-media.smartstudy.com/upload/ufile/85/0d/850d74f805af0c9587da2139b5fd2414f395.png","introduction":"毕业于美国波士顿大学，教育学硕士学位。曾任某大型出国咨询公司研究生部文书组负责人，有丰富的文书写作课程研发和授课经验，尤其擅长指导高端商学院 Essay 写作，以及教育，传媒，东亚，心理，法律，政治，人力等相关专业文书写作和申请。"}]
     * createTime : 2017-07-13T09:27:50.551Z
     * playCount : 259
     * rate:4
     * abroadServiceImageUrl : https://bkd-media.smartstudy.com/upload/ufile/0a/44/0a440e8ebcb5aac557b841b11c94397248879.png
     */

    private String productId;
    private String name;
    private String coverUrl;
    private String introduction;
    private String provider;
    private String targetUser;
    private String createTime;
    private String playCount;
    private String abroadServiceImageUrl;
    private String rate;
    private List<TeachersEntity> teachers;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public void setAbroadServiceImageUrl(String abroadServiceImageUrl) {
        this.abroadServiceImageUrl = abroadServiceImageUrl;
    }

    public void setTeachers(List<TeachersEntity> teachers) {
        this.teachers = teachers;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getProvider() {
        return provider;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getPlayCount() {
        return playCount;
    }

    public String getAbroadServiceImageUrl() {
        return abroadServiceImageUrl;
    }

    public List<TeachersEntity> getTeachers() {
        return teachers;
    }

    public static class TeachersEntity {
        /**
         * name : 李静超
         * title : 智课资深留学申请导师
         * avatarUrl : https://bkd-media.smartstudy.com/upload/ufile/85/0d/850d74f805af0c9587da2139b5fd2414f395.png
         * introduction : 毕业于美国波士顿大学，教育学硕士学位。曾任某大型出国咨询公司研究生部文书组负责人，有丰富的文书写作课程研发和授课经验，尤其擅长指导高端商学院 Essay 写作，以及教育，传媒，东亚，心理，法律，政治，人力等相关专业文书写作和申请。
         */

        private String name;
        private String title;
        private String avatarUrl;
        private String introduction;

        public void setName(String name) {
            this.name = name;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public String getIntroduction() {
            return introduction;
        }
    }
}

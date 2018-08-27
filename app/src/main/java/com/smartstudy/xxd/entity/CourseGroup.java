package com.smartstudy.xxd.entity;


import java.util.List;

/**
 * Created by yqy on 2017/11/14.
 */

public class CourseGroup {
    /**
     * productId : 2175
     * name : 选校帝留学课程之签证篇
     * coverUrl : http://upload.beikaodi.com/upload/ufile/fe/ba/febaf376a89599f915355345ae4f84441d4ca.png
     * provider : 智课选校帝
     * createTime : 2017-07-12T02:33:51.050Z
     * playCount : 233
     * rate : 4
     */
    private String group;

    private List<Course> list;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }

    public class Course {
        private String productId;
        private String name;
        private String coverUrl;
        private String provider;
        private String createTime;
        private String playCount;
        private String rate;
        private String group;
        private String sectionCount;

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }

        public void setRate(String rate) {
            this.rate = rate;
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

        public String getProvider() {
            return provider;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getPlayCount() {
            return playCount;
        }

        public String getRate() {
            return rate;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getSectionCount() {
            return sectionCount;
        }

        public void setSectionCount(String sectionCount) {
            this.sectionCount = sectionCount;
        }

    }

}

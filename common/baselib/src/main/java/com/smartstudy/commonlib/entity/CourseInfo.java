package com.smartstudy.commonlib.entity;

/**
 * Created by louis on 17/7/13.
 */

public class CourseInfo {

    /**
     * productId : 2175
     * name : 选校帝留学课程之签证篇
     * coverUrl : http://upload.beikaodi.com/upload/ufile/fe/ba/febaf376a89599f915355345ae4f84441d4ca.png
     * provider : 智课选校帝
     * createTime : 2017-07-12T02:33:51.050Z
     * playCount : 233
     * rate : 4
     */

    private String productId;
    private String name;
    private String coverUrl;
    private String provider;
    private String createTime;
    private String playCount;
    private String rate;

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

    @Override
    public String toString() {
        return "CourseInfo{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", provider='" + provider + '\'' +
                ", createTime='" + createTime + '\'' +
                ", playCount='" + playCount + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}

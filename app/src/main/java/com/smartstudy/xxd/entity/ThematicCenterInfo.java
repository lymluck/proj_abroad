package com.smartstudy.xxd.entity;

/**
 * Created by yqy on 2017/10/10.
 * GET http://api.beikaodi.com/subject/list
 */

public class ThematicCenterInfo {
    private String id;
    private String name;
    private String imageUrl;
    private String createTime;
    private String createTimeText;
    private String subjectUrl;
    private String visitCount;

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeText() {
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }

    public String getSubjectUrl() {
        return subjectUrl;
    }

    public void setSubjectUrl(String subjectUrl) {
        this.subjectUrl = subjectUrl;
    }
}
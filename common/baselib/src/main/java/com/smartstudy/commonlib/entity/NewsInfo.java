package com.smartstudy.commonlib.entity;

/**
 * Created by louis on 17/5/18.
 */

public class NewsInfo {
    private String id;
    private String coverUrl;
    private String title;
    private String visitCount;
    private String tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "NewsInfo{" +
                "id='" + id + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", title='" + title + '\'' +
                ", visitCount='" + visitCount + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}

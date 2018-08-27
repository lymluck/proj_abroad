package com.smartstudy.xxd.entity;

import android.content.Context;

import com.smartstudy.commonlib.utils.HttpUrlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by louis on 17/4/12.
 */

public class CollectionInfo {
    private int id;
    private String name;
    private String collectType;
    private Date collectTime;
    private String collectTypeName;
    private String displayText;
    private String url;
    private String displayImage;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollectTime() {
        return dateFormat.format(collectTime);
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public String getCollectType() {
        return collectType;
    }

    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

    public String getCollectTypeName(Context context) {
        if ("news".equals(collectType)) {
            collectTypeName = "留学资讯";
            url = String.format(HttpUrlUtils.getWebUrl(
                    HttpUrlUtils.URL_NEWS_DETAIL), id);
        } else if ("question".equals(collectType)) {
            collectTypeName = "留学问答";
            url = String.format(HttpUrlUtils.getWebUrl(
                    HttpUrlUtils.URL_QUESTION_DETAIL), id);
        } else if ("school-introduction".equals(collectType)) {
            collectTypeName = "学校简介";
            url = String.format(HttpUrlUtils.getWebUrl(
                    HttpUrlUtils.URL_SCHOOL_INTRO), id);
        } else if ("school-undergraduate-application".equals(collectType)) {
            collectTypeName = "本科申请";
            url = String.format(HttpUrlUtils.getWebUrl(
                    HttpUrlUtils.URL_SCHOOL_UNDERGRADUATE), id);
        } else if ("school-graduate-application".equals(collectType)) {
            collectTypeName = "研究生申请";
            url = String.format(HttpUrlUtils.getWebUrl(
                    HttpUrlUtils.URL_SCHOOL_GRADUATE), id);
        } else if ("major-lib-major".equals(collectType)) {
            collectTypeName = "专业详情";
            url = String.format(HttpUrlUtils.getWebUrl(
                    HttpUrlUtils.URL_SPE_INTRO), id);
        } else if ("major-lib-program".equals(collectType)) {
            collectTypeName = "推荐院校";
        }
        return collectTypeName;
    }

    public void setCollectTypeName(String collectTypeName) {
        this.collectTypeName = collectTypeName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    @Override
    public String toString() {
        return "CollectionInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", collectType='" + collectType + '\'' +
                ", collectTime=" + collectTime +
                ", collectTypeName='" + collectTypeName + '\'' +
                ", displayText='" + displayText + '\'' +
                ", url='" + url + '\'' +
                ", displayImage='" + displayImage + '\'' +
                ", dateFormat=" + dateFormat +
                '}';
    }
}

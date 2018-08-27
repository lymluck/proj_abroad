package com.smartstudy.commonlib.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by louis on 17/7/6.
 */

public class DeviceMsgInfo {
    private String id;
    private String type;
    private String text;
    private String imageUrl;
    private String linkUrl;
    private Date pushTime;
    private String data;
    private boolean read;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPushTime() {
        return dateFormat.format(pushTime);
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public String toString() {
        return "DeviceMsgInfo{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", pushTime='" + pushTime + '\'' +
                ", read=" + read +
                '}';
    }
}

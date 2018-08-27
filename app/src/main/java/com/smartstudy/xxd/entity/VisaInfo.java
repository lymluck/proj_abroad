package com.smartstudy.xxd.entity;

/**
 * Created by louis on 17/8/18.
 */

public class VisaInfo {

    private String type;
    private String title;
    private String backgroundImageUrl;
    private String productId;
    private String coverUrl;
    private String visitCount;
    private boolean recommended;
    private String webviewUrl;

    public boolean isRecommended() {
        return recommended;
    }

    public String getWebviewUrl() {
        return webviewUrl;
    }

    public void setWebviewUrl(String webviewUrl) {
        this.webviewUrl = webviewUrl;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public boolean getRecommended() {
        return recommended;
    }
}

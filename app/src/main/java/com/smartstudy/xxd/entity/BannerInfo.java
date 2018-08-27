package com.smartstudy.xxd.entity;

/**
 * Created by louis on 17/5/16.
 */

public class BannerInfo {

    /**
     * id : 112
     * platformId : PLATFORM_MOBILE
     * pageKey : index
     * typeKey : banner
     * order : 1
     * imageUrl : http://upload.beikaodi.com/business/image/2f/b2/2fb26918defcf9106c6fb5f2247e157e.jpg
     * adUrl : https://sojump.com/jq/11823302.aspx
     * name : 选校帝用户属性调查问卷
     */

    private String id;
    private String platformId;
    private String pageKey;
    private String typeKey;
    private String order;
    private String imageUrl;
    private String adUrl;
    private String name;

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getPageKey() {
        return pageKey;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}

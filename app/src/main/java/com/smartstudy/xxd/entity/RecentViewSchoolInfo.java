package com.smartstudy.xxd.entity;

/**
 * @author yqy
 * @date on 2018/6/13
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class RecentViewSchoolInfo {
    private String id;
    private String chineseName;
    private String englishName;
    private String logo;
    private String viewTime;
    private String viewTimeText;
    private String groupName;
    private boolean isHeadVisible;

    public boolean isHeadVisible() {
        return isHeadVisible;
    }

    public void setHeadVisible(boolean headVisible) {
        isHeadVisible = headVisible;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getViewTime() {
        return viewTime;
    }

    public void setViewTime(String viewTime) {
        this.viewTime = viewTime;
    }

    public String getViewTimeText() {
        return viewTimeText;
    }

    public void setViewTimeText(String viewTimeText) {
        this.viewTimeText = viewTimeText;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}

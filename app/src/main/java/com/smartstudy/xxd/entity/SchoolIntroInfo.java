package com.smartstudy.xxd.entity;

/**
 * Created by louis on 17/9/13.
 */

public class SchoolIntroInfo {
    private String id;
    private String chineseName;
    private String englishName;
    private String logo;
    private String localRank;
    private String worldRank;
    private String coverPicture;
    private String cityPath;

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

    public String getLocalRank() {
        return localRank;
    }

    public void setLocalRank(String localRank) {
        this.localRank = localRank;
    }

    public String getWorldRank() {
        return worldRank;
    }

    public void setWorldRank(String worldRank) {
        this.worldRank = worldRank;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getCityPath() {
        return cityPath;
    }

    public void setCityPath(String cityPath) {
        this.cityPath = cityPath;
    }
}

package com.smartstudy.commonlib.entity;

/**
 * @author yqy
 * @date on 2018/4/8
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HighSchoolInfo {

    private String id;
    private String chineseName;
    private String englishName;
    private String countryName;
    private String provinceName;
    private String cityName;
    private String boarderTypeName;
    private String sexualTypeName;
    private String schoolTypeName;
    private String seniorFacultyRatio;
    private String rank;

    public String getSeniorFacultyRatio() {
        return seniorFacultyRatio;
    }

    public void setSeniorFacultyRatio(String seniorFacultyRatio) {
        this.seniorFacultyRatio = seniorFacultyRatio;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBoarderTypeName() {
        return boarderTypeName;
    }

    public void setBoarderTypeName(String boarderTypeName) {
        this.boarderTypeName = boarderTypeName;
    }

    public String getSexualTypeName() {
        return sexualTypeName;
    }

    public void setSexualTypeName(String sexualTypeName) {
        this.sexualTypeName = sexualTypeName;
    }

    public String getSchoolTypeName() {
        return schoolTypeName;
    }

    public void setSchoolTypeName(String schoolTypeName) {
        this.schoolTypeName = schoolTypeName;
    }
}

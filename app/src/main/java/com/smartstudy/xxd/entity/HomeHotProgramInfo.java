package com.smartstudy.xxd.entity;

/**
 * @author louis
 * @date on 2018/7/11
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class HomeHotProgramInfo {

    /**
     * id : 7
     * chineseName : 信息系统
     * englishName : Information Systems
     * visitCount : 2
     */

    private String id;
    private String categoryId;
    private String categoryRank;
    private String chineseName;
    private String englishName;
    private String schoolName;
    private int visitCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryRank() {
        return categoryRank;
    }

    public void setCategoryRank(String categoryRank) {
        this.categoryRank = categoryRank;
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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
            .append(id).append('\"');
        sb.append(",\"categoryId\":\"")
            .append(categoryId).append('\"');
        sb.append(",\"categoryRank\":\"")
            .append(categoryRank).append('\"');
        sb.append(",\"chineseName\":\"")
            .append(chineseName).append('\"');
        sb.append(",\"englishName\":\"")
            .append(englishName).append('\"');
        sb.append(",\"schoolName\":\"")
            .append(schoolName).append('\"');
        sb.append(",\"visitCount\":")
            .append(visitCount);
        sb.append('}');
        return sb.toString();
    }
}

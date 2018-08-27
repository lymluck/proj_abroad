package com.smartstudy.xxd.entity;

/**
 * @author yqy
 * @date on 2018/4/9
 * @describe 美高排行详细信息
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HighSchoolRankDetailInfo {

    private String chineseName;
    private String highschoolId;
    private String id;
    private String rank;

    public String getHighschoolId() {
        return highschoolId;
    }

    public void setHighschoolId(String highschoolId) {
        this.highschoolId = highschoolId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

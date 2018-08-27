package com.smartstudy.xxd.entity;

/**
 * @author yqy
 * @date on 2018/6/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ChoiceSchoolListInfo {
    private String avatar;
    private String name;
    private int selectSchoolCount;
    private String id;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSelectSchoolCount() {
        return selectSchoolCount;
    }

    public void setSelectSchoolCount(int checkinCount) {
        this.selectSchoolCount = checkinCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

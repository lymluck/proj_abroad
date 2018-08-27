package com.smartstudy.xxd.entity;

/**
 * @author yqy
 * @date on 2018/5/28
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class CurrentExamSchedule {
    private String id;
    private String exam;
    private String date;
    private String selectCount;
    private String countdown;
    private boolean checkedInToday;

    public boolean isCheckedInToday() {
        return checkedInToday;
    }

    public void setCheckedInToday(boolean checkedInToday) {
        this.checkedInToday = checkedInToday;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(String selectCount) {
        this.selectCount = selectCount;
    }

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }
}

package com.smartstudy.xxd.entity;

/**
 * @author yqy
 * @date on 2018/5/23
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class MyRemarkPlanInfo {
    private String countdown;
    private String date;
    private String exam;
    private String id;
    private boolean over;
    private String selectCount;
    private String weekday;

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public String getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(String selectCount) {
        this.selectCount = selectCount;
    }
}

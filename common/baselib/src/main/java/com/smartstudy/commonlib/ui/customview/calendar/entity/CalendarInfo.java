package com.smartstudy.commonlib.ui.customview.calendar.entity;

/**
 * Created by Administrator on 2016/7/30.
 */
public class CalendarInfo {

    /**
     * id
     */
    public int id;
    /**
     * 年
     */
    public int year;
    /**
     * 月
     */
    public int month;
    /**
     * 日
     */
    public int day;
    /**
     * 描述词
     */
    public String des;
    /**
     * 是否为休、班。。1为休，2为班，默认为普通日期
     */
    public int rest;

    public String selectCount;
    public boolean select;

    /**
     * 构造函数
     *
     * @param year  事务年份
     * @param month 事务月份
     * @param day   事务日期号
     * @param des   事务描述
     */
    public CalendarInfo(int year, int month, int day, String des) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.des = des;
    }

    /**
     * 构造函数
     *
     * @param year  事务年份
     * @param month 事务月份
     * @param day   事务日期号
     * @param des   事务描述
     * @param rest  是否为休、班。。1为休，2为班，默认为普通日期
     */
    public CalendarInfo(int year, int month, int day, String des, int rest) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.des = des;
        this.rest = rest;
    }

    /**
     * 构造函数
     *
     * @param id
     * @param year        事务年份
     * @param month       事务月份
     * @param day         事务日期号
     * @param des         事务描述
     * @param selectCount 选择人数
     */
    public CalendarInfo(int id, int year, int month, int day, String des, String selectCount) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.des = des;
        this.selectCount = selectCount;
    }

    /**
     * 构造函数
     *
     * @param id
     * @param year   事务年份
     * @param month  事务月份
     * @param day    事务日期号
     * @param des    事务描述
     * @param select 选中状态
     */
    public CalendarInfo(int id, int year, int month, int day, String des, String selectCount, boolean select) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.des = des;
        this.selectCount = selectCount;
        this.select = select;
    }

}

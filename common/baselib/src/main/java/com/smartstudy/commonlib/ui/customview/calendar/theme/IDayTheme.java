package com.smartstudy.commonlib.ui.customview.calendar.theme;

/**
 * Created by Administrator on 2016/7/30.
 */
public interface IDayTheme {
    /**
     * 选中日期的背景色
     *
     * @return 16进制颜色值 hex color
     */
    public int colorSelectBG(String des);

    /**
     * 选中日期的颜色
     *
     * @return 16进制颜色值 hex color
     */
    public int colorSelectDay();


    /**
     * 事务装饰颜色
     *
     * @return 16进制颜色值 hex color
     */
    public int colorDecor(String des);

    /**
     * day颜色
     *
     * @return
     */
    public int colorDay();

    /**
     * 可以选择day颜色
     *
     * @return
     */
    public int colorDisableDay();

    /**
     * 假日颜色
     *
     * @return 16进制颜色值 hex color
     */
    public int colorRest();

    /**
     * 班颜色
     *
     * @return 16进制颜色值 hex color
     */
    public int colorWork();

    /**
     * 描述文字颜色
     *
     * @return 16进制颜色值 hex color
     */
    public int colorDesc(String des);

    /**
     * 描述文字选中颜色
     *
     * @return 16进制颜色值 hex color
     */
    public int colorSelectDesc();

    /**
     * 日期大小
     *
     * @return
     */
    public int sizeDay();

    /**
     * 描述文字大小
     *
     * @return
     */
    public int sizeDesc();

    /**
     * 装饰器大小
     *
     * @return
     */
    public int sizeDecor();

    /**
     * 日期高度
     *
     * @return
     */
    public int dateHeight();

}

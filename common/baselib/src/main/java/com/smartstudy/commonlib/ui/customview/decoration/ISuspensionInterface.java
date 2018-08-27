package com.smartstudy.commonlib.ui.customview.decoration;

/**
 * 分类悬停的接口
 * Created by louis on 17/4/21.
 */

public interface ISuspensionInterface {
    //是否需要显示悬停title
    boolean isShowSuspension();

    //悬停的title
    String getSuspensionTag();

    //悬停的图片id
    int drawableId();
}

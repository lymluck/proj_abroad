package com.smartstudy.commonlib.ui.customview.treeview;


import com.smartstudy.commonlib.ui.adapter.base.BaseItem;

import java.util.List;

/**
 * @author jlanglang  2016/12/23 17:04
 * @版本 2.0
 * @Change
 */
interface TreeParent {
    /**
     * 是否允许展开
     *
     * @return
     */
    boolean isCanChangeExpand();

    /**
     * 展开后的回调
     */
    void onExpand(boolean isCollapseOthers);

    /**
     * 折叠后的回调
     */
    void onCollapse();

    /**
     * 获取子集
     *
     * @return
     */
    List<? extends BaseItem> getChilds();

}

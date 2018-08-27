package com.smartstudy.commonlib.ui.customView.treeView;

import android.support.annotation.Nullable;

import com.smartstudy.commonlib.ui.adapter.base.BaseItem;
import com.smartstudy.commonlib.ui.adapter.base.TreeRecyclerViewType;

import java.util.List;

/**
 * Created by baozi on 2016/12/22.
 * //拥有子集
 * //子集可以是parent,也可以是child
 */

public abstract class TreeItemGroup<D> extends TreeItem<D>
        implements TreeParent {
    /**
     * 持有的子item
     */
    private List<? extends BaseItem> childs;
    /**
     * 是否展开
     */
    private boolean isExpand;
    /**
     * 能否展开
     */
    protected boolean isCanChangeExpand = true;

    private boolean isCollapseOthers;

    public TreeItemGroup() {

    }

    public boolean isExpand() {
        return isExpand;
    }

    /**
     * 设置为传入
     *
     * @param expand 传入true则展开,传入false则折叠
     */
    public void setExpand(boolean expand) {
        if (!isCanChangeExpand()) {
            return;
        }
        isExpand = expand;
    }

    public boolean isCollapseOthers() {
        return this.isCollapseOthers;
    }

    public void setCollapseOthers(boolean isCollapseOthers) {
        this.isCollapseOthers = isCollapseOthers;
    }

    /**
     * 刷新Item的展开状态
     */
    public void notifyExpand() {
        if (isExpand()) {
            onExpand(isCollapseOthers);
        } else {
            onCollapse();
        }
    }

    @Override
    public void setData(D data) {
        super.setData(data);
        childs = initChildsList(data);
    }

    public void setChilds(List<? extends BaseItem> childs) {
        this.childs = childs;
    }

    /**
     * 展开
     */
    @Override
    public void onExpand(boolean isCollapseOthers) {
        if (getItemManager() != null) {
            int itemPosition = getItemManager().getItemPosition(this);
            if (isCollapseOthers) {
                getItemManager().collapseOthers(this);
            } else {
                getItemManager().addItems(itemPosition + 1, getAllChilds());
                getItemManager().notifyDataChanged();
            }
        }
        isExpand = true;
    }

    /**
     * 折叠
     */
    @Override
    public void onCollapse() {
        if (getItemManager() != null) {
            int itemPosition = getItemManager().getItemPosition(this);
            getItemManager().removeItems(getExpandChilds());
            getItemManager().notifyItemRangeRemoved(itemPosition + 1, getExpandChilds().size());
        }
        isExpand = false;
    }

    @Override
    public boolean isCanChangeExpand() {
        return isCanChangeExpand;
    }

    /**
     * 获得自己的childs.
     *
     * @return
     */
    @Override
    @Nullable
    public List<? extends BaseItem> getChilds() {
        return childs;
    }

    /**
     * 获得所有childs,包括子item的childs
     *
     * @return
     */
    @Nullable
    public List<? extends BaseItem> getExpandChilds() {
        if (getChilds() == null) {
            return null;
        }
        //TODO 待解决通过Adapter的Type来获取对应数据的问题
        return ItemHelper.getChildItemsWithType(this, TreeRecyclerViewType.SHOW_EXPAND);
    }

    /**
     * 获得所有childs,包括下下....级item的childs
     *
     * @return
     */
    @Nullable
    public List<? extends BaseItem> getAllChilds() {
        if (getChilds() == null) {
            return null;
        }
        return ItemHelper.getChildItemsWithType(this, TreeRecyclerViewType.SHOW_ALL);
    }

    public int getChildCount() {
        return childs == null ? 0 : childs.size();
    }

    public <T extends BaseItem> T getChildsAt(int index) {
        if (index < 0 || index > getChildCount()) {
            return null;
        }
        return (T) childs.get(index);
    }

    /**
     * 初始化子集
     *
     * @param data
     * @return
     */
    protected abstract List<? extends BaseItem> initChildsList(D data);

    /**
     * 是否消费child的click事件
     *
     * @param child 具体click的item
     * @return 返回true代表消费此次事件，child不会走onclick()，返回false说明不消费此次事件，child依然会走onclick()
     */
    public boolean onInterceptClick(TreeItem child) {
        return false;
    }
}

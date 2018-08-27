package com.smartstudy.commonlib.ui.adapter.base;

import android.support.v7.widget.RecyclerView;

import com.smartstudy.commonlib.ui.customView.treeView.TreeItemGroup;

import java.util.List;

public abstract class ItemManager<T extends BaseItem> {

    private BaseRecyclerAdapter<T> mAdapter;

    private int realPos;

    public ItemManager(BaseRecyclerAdapter<T> adapter) {
        mAdapter = adapter;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(BaseRecyclerAdapter<T> adapter) {
        mAdapter = adapter;
    }

    //增
    public abstract void addItem(T item);

    public abstract void addItem(int position, T item);

    public abstract void addItems(List<T> items);

    public abstract void addItems(int position, List<T> items);

    //删
    public abstract void removeItem(T item);

    public abstract void removeItem(int position);

    public abstract void removeItems(List<T> items);


    //改
    public abstract void replaceItem(int position, T item);

    public abstract void replaceAllItem(List<T> items);

    //查
    public abstract T getItem(int position);

    public abstract int getItemPosition(T item);

    //刷新
    public void notifyDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    public void collapseOthers(TreeItemGroup group) {
        if (group.isExpand()) {
            group.setExpand(false);
        }
        int count = getAdapter().getItemCount();
        //先关闭其它
        for (int i = 0; i < count; i++) {
            if (mAdapter.getData(i) instanceof TreeItemGroup) {
                TreeItemGroup treeItemGroup = (TreeItemGroup) mAdapter.getItemManager().getItem(i);
                if (treeItemGroup.getParentItem() == null) {
                    if (treeItemGroup.isExpand()) {
                        treeItemGroup.setExpand(false);
                        List<BaseItem> items = treeItemGroup.getAllChilds();
                        setChildExpand(items, false);
                        removeItems((List<T>) items);
                    }
                }
            }
        }
        //展开当前
        List<BaseItem> childs = group.getAllChilds();
        group.setExpand(true);
        setChildExpand(childs, true);
        addItems(getItemPosition((T) group) + 1, (List<T>) childs);
        realPos = getItemPosition((T) group);
    }

    public void setChildExpand(List<BaseItem> list, boolean isExpand) {
        for (BaseItem item : list) {
            if (item instanceof TreeItemGroup) {
                ((TreeItemGroup) item).setExpand(isExpand);
            }
        }
    }

    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        mAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    public int getRealPos() {
        return realPos;
    }
}
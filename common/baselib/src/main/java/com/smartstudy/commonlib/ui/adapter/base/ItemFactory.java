package com.smartstudy.commonlib.ui.adapter.base;


import com.smartstudy.commonlib.entity.BaseItemData;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItem;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItemGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baozi on 2017/4/29.
 */

public class ItemFactory {
    /**
     * 确定item的class类型,并且添加到了itemConfig,用该方法创建List<BaseItem>
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends BaseItem> List<T> createItemList(List<? extends BaseItemData> list) {
        if (null == list) {
            return null;
        }
        int size = list.size();
        ArrayList<T> treeItemList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            BaseItemData itemData = list.get(i);
            try {
                Class<? extends BaseItem> itemClass = ItemConfig.getViewHolderType(itemData.getViewItemType());
                if (itemClass != null) {
                    T treeItem = (T) itemClass.newInstance();
                    treeItem.setData(itemData);
                    treeItemList.add(treeItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return treeItemList;
    }

    /**
     * 不确定item的class类型,用该方法创建List<BaseItem>
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends BaseItem<D>, D> List<T> createItemList(List<D> list, Class<T> iClass) {
        if (null == list) {
            return null;
        }
        int size = list.size();
        ArrayList<T> treeItemList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            D o = list.get(i);
            try {
                if (iClass != null) {
                    T treeItem = iClass.newInstance();
                    treeItem.setData(o);
                    treeItemList.add(treeItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return treeItemList;
    }


    public static List<TreeItem> createTreeItemList(List<? extends BaseItemData> list, TreeItemGroup treeParentItem) {
        if (null == list) {
            return null;
        }
        int size = list.size();
        ArrayList<TreeItem> treeItemList = new ArrayList<>();
        BaseItemData itemData;
        for (int i = 0; i < size; i++) {
            itemData = list.get(i);
            try {
                Class<? extends BaseItem> itemClass = ItemConfig.getViewHolderType(itemData.getViewItemType());
                if (itemClass != null) {
                    TreeItem treeItem = (TreeItem) itemClass.newInstance();
                    treeItem.setData(itemData);
                    treeItem.setParentItem(treeParentItem);
                    treeItemList.add(treeItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemData = null;
        }
        list = null;
        treeParentItem = null;
        return treeItemList;
    }

    public static <D> List<TreeItem> createTreeItemList(List<D> list, Class<? extends TreeItem> iClass, TreeItemGroup treeParentItem) {
        if (null == list) {
            return null;
        }
        int size = list.size();
        ArrayList<TreeItem> treeItemList = new ArrayList<>();
        D itemData;
        for (int i = 0; i < size; i++) {
            itemData = list.get(i);
            try {
                if (iClass != null) {
                    TreeItem<D> treeItem = iClass.newInstance();
                    treeItem.setData(itemData);
                    treeItem.setParentItem(treeParentItem);
                    treeItemList.add(treeItem);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemData = null;
        }
        list = null;
        treeParentItem = null;
        return treeItemList;
    }
}

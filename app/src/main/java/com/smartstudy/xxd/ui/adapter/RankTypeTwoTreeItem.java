package com.smartstudy.xxd.ui.adapter;

import com.smartstudy.commonlib.entity.RankTypeInfo;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItem;
import com.smartstudy.xxd.R;

/**
 */
public class RankTypeTwoTreeItem extends TreeItem<RankTypeInfo.RankingsEntity> {

    @Override
    protected int initLayoutId() {
        return R.layout.item_ranktype_two;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_item_name, data.getYear() + data.getType() + data.getTitle());
    }
}

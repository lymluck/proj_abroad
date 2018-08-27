package com.smartstudy.xxd.ui.adapter;

import com.smartstudy.commonlib.entity.ProgramInfo;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItem;
import com.smartstudy.xxd.R;

/**
 */
public class ProgramTwoTreeItem extends TreeItem<ProgramInfo.ProgramsInfo> {

    @Override
    protected int initLayoutId() {
        return R.layout.item_special_two;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_item_name, data.getName());
    }
}

package com.smartstudy.xxd.ui.adapter;

import com.smartstudy.commonlib.entity.MajorInfo;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.xxd.R;

/**
 */
public class SpeDataTwoTreeItem extends TreeItem<MajorInfo.MajorsInfo.Majors> {

    @Override
    protected int initLayoutId() {
        return R.layout.item_special_two;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_item_name, data.getName());
    }
}

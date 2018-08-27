package com.smartstudy.xxd.ui.adapter;

import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItem;
import com.smartstudy.xxd.R;

/**
 */
public class VisaQaTwoTreeItem extends TreeItem<String> {

    @Override
    protected int initLayoutId() {
        return R.layout.item_visaqa_two;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_answer, data);
    }
}

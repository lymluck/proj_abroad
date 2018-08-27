package com.smartstudy.xxd.ui.adapter;

import android.widget.ImageView;

import com.smartstudy.commonlib.ui.adapter.base.BaseItem;
import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItemGroup;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.GpaDesInfo;

import java.util.List;

/**
 * Created by yqy on 2017/10/24.
 */

public class GpaDesOneTreeItemParent extends TreeItemGroup<GpaDesInfo> {
    @Override
    protected int initLayoutId() {
        return R.layout.item_gpa_one;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_name, data.getName());
        ImageView iv = viewHolder.getView(R.id.iv_more);
        if (isExpand()) {
            iv.setImageResource(R.drawable.ic_arrow_down);
        } else {
            iv.setImageResource(R.drawable.ic_arrow_right);
        }
    }

    @Override
    protected List<? extends BaseItem> initChildsList(GpaDesInfo data) {
        return ItemFactory.createTreeItemList(data.getGpaDesInfoEntities(), GpaDesTwoTreeItem.class, this);
    }


    @Override
    public void onExpand(boolean isCollapseOthers) {
        isCollapseOthers = true;
        super.onExpand(isCollapseOthers);
    }
}

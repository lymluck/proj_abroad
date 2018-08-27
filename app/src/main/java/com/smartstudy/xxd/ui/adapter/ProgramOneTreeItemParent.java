package com.smartstudy.xxd.ui.adapter;

import android.widget.ImageView;

import com.smartstudy.commonlib.entity.ProgramInfo;
import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItem;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItemGroup;
import com.smartstudy.xxd.R;

import java.util.List;

/**
 * Created by baozi on 2016/12/8.
 */
public class ProgramOneTreeItemParent extends TreeItemGroup<ProgramInfo> {

    @Override
    public List<? extends TreeItem> initChildsList(ProgramInfo data) {
        return ItemFactory.createTreeItemList(data.getPrograms(), ProgramTwoTreeItem.class, this);
    }

    @Override
    public int initLayoutId() {
        return R.layout.item_special_one;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder) {
        holder.setText(R.id.tv_type_name, data.getName());
        ImageView iv = holder.getView(R.id.iv_more);
        if (isExpand()) {
            iv.setImageResource(R.drawable.ic_arrow_down);
        } else {
            iv.setImageResource(R.drawable.ic_arrow_right);
        }
    }

    @Override
    public void onExpand(boolean isCollapseOthers) {
        isCollapseOthers = true;
        super.onExpand(isCollapseOthers);
    }

}

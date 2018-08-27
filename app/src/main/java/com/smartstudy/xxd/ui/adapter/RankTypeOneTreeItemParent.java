package com.smartstudy.xxd.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.smartstudy.commonlib.entity.RankTypeInfo;
import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItem;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItemGroup;
import com.smartstudy.xxd.R;

import java.util.List;

/**
 * Created by baozi on 2016/12/8.
 */
public class RankTypeOneTreeItemParent extends TreeItemGroup<RankTypeInfo> {

    @Override
    public List<? extends TreeItem> initChildsList(RankTypeInfo data) {
        return ItemFactory.createTreeItemList(data.getRankings(), RankTypeTwoTreeItem.class, this);
    }

    @Override
    public int initLayoutId() {
        return R.layout.item_ranktype_one;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder) {
        View view_line = holder.getView(R.id.view_line);
        if (getItemManager().getItemPosition(this) != 0 && getData().isTop()) {
            view_line.setVisibility(View.VISIBLE);
        } else {
            view_line.setVisibility(View.GONE);
        }
        holder.setCircleImageUrl(R.id.iv_type, data.getGroupIcon(), true);
        holder.setText(R.id.tv_type_name, data.getGroupName());
        ImageView iv = holder.getView(R.id.iv_more);
        if (isExpand()) {
            iv.setImageResource(R.drawable.ic_arrow_down_gray);
        } else {
            iv.setImageResource(R.drawable.ic_home_arrow_rg);
        }
    }

    @Override
    public void onExpand(boolean isCollapseOthers) {
        isCollapseOthers = true;
        super.onExpand(isCollapseOthers);
    }
}

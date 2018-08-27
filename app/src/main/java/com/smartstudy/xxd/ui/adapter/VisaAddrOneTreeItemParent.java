package com.smartstudy.xxd.ui.adapter;

import android.widget.ImageView;

import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItem;
import com.smartstudy.commonlib.ui.customView.treeView.TreeItemGroup;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.VisaAddrInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baozi on 2016/12/8.
 */
public class VisaAddrOneTreeItemParent extends TreeItemGroup<VisaAddrInfo> {

    @Override
    public List<? extends TreeItem> initChildsList(VisaAddrInfo data) {
        List<VisaAddrInfo> Addrs = new ArrayList<>();
        Addrs.add(data);
        return ItemFactory.createTreeItemList(Addrs, VisaAddrTwoTreeItem.class, this);
    }

    @Override
    public int initLayoutId() {
        return R.layout.item_visa_one;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder) {
        holder.setText(R.id.tv_type_name, data.getName());
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

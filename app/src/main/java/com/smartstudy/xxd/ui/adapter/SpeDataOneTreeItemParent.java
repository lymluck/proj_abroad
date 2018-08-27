package com.smartstudy.xxd.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.smartstudy.commonlib.entity.MajorInfo;
import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItemGroup;
import com.smartstudy.xxd.R;

import java.util.List;

/**
 * Created by baozi on 2016/12/8.
 */
public class SpeDataOneTreeItemParent extends TreeItemGroup<MajorInfo> {

    private View view;

    @Override
    public List<? extends TreeItem> initChildsList(MajorInfo data) {
        return ItemFactory.createTreeItemList(data.getMinorCategories().get(0).getMajors(), SpeDataTwoTreeItem.class, this);
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

    @Override
    public void onCollapse() {
        super.onCollapse();
        if (view != null) {
            view.setVisibility(View.INVISIBLE);
        }
    }
}

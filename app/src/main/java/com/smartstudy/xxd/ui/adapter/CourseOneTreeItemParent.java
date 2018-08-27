package com.smartstudy.xxd.ui.adapter;

import android.widget.ImageView;

import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItemGroup;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CourseContentInfo;

import java.util.List;

public class CourseOneTreeItemParent extends TreeItemGroup<CourseContentInfo> {


    @Override
    public List<? extends TreeItem> initChildsList(CourseContentInfo data) {
        return ItemFactory.createTreeItemList(data.getChapters(), CourseTwoTreeItemParent.class, this);
    }

    @Override
    public int initLayoutId() {
        return R.layout.item_course_one;
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

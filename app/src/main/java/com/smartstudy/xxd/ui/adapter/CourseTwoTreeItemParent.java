package com.smartstudy.xxd.ui.adapter;

import com.smartstudy.commonlib.ui.adapter.base.ItemFactory;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItem;
import com.smartstudy.commonlib.ui.customview.treeview.TreeItemGroup;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.CourseContentInfo;

import java.util.List;

/**
 * Created by baozi on 2016/12/8.
 */
public class CourseTwoTreeItemParent extends TreeItemGroup<CourseContentInfo.ChaptersEntity> {

    @Override
    public List<? extends TreeItem> initChildsList(CourseContentInfo.ChaptersEntity data) {
        return ItemFactory.createTreeItemList(data.getSections(), Course3TreeItem.class, this);
    }

    @Override
    public int initLayoutId() {
        return R.layout.item_course_two;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder) {
        holder.setText(R.id.tv_type_name, data.getName());
    }
}

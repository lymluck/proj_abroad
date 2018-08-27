package com.smartstudy.xxd.ui.adapter;

import android.content.Context;

import com.smartstudy.commonlib.entity.IdNameInfo;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.xxd.R;

import java.util.List;

/**
 * Created by louis on 2017/10/19.
 */

public class HighSchoolValueItemsAdapter extends CommonAdapter<IdNameInfo> {

    public HighSchoolValueItemsAdapter(Context context, int layoutId, List<IdNameInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, IdNameInfo item, int position) {
        holder.setText(R.id.tv_name, item.getName() + ":");
        holder.setText(R.id.tv_value, item.getValue());
    }
}

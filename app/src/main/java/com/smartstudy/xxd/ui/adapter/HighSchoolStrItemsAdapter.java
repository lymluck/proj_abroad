package com.smartstudy.xxd.ui.adapter;

import android.content.Context;

import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.xxd.R;

import java.util.List;

/**
 * Created by louis on 2017/10/19.
 */

public class HighSchoolStrItemsAdapter extends CommonAdapter<String> {

    public HighSchoolStrItemsAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String str, int position) {
        holder.setText(R.id.tv_name, str);
    }
}

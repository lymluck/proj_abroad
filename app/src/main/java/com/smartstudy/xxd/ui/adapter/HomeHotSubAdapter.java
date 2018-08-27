package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.HomeHotInfo;

import java.util.List;

/**
 * Created by louis on 2017/10/19.
 */

public class HomeHotSubAdapter extends CommonAdapter<HomeHotInfo> {

    public HomeHotSubAdapter(Context context, int layoutId, List<HomeHotInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, HomeHotInfo homeHotInfo, int position) {
        if (position == 0) {
            holder.getView(R.id.first_view).setVisibility(View.VISIBLE);
        } else if (position == mDatas.size() - 1) {
            holder.getView(R.id.last_view).setVisibility(View.VISIBLE);
        }
        DisplayImageUtils.formatImgUrl(mContext, homeHotInfo.getImageUrl(), (ImageView) holder.getView(R.id.iv_topic_cover));
        holder.setText(R.id.tv_see_num, String.format(mContext.getString(R.string.visa_see), homeHotInfo.getVisitCount()));
    }
}

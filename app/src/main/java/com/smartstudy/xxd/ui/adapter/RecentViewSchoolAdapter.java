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
 * @author luoyongming
 * @date on 2018/4/12
 * @describe 看过的院校适配器
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class RecentViewSchoolAdapter extends CommonAdapter<HomeHotInfo> {

    public RecentViewSchoolAdapter(Context context, int layoutId, List<HomeHotInfo> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, HomeHotInfo homeHotInfo, int position) {
        if (position == 0) {
            holder.getView(R.id.first_view).setVisibility(View.VISIBLE);
            holder.getView(R.id.last_view).setVisibility(View.GONE);
        } else if (position == mDatas.size() - 1) {
            holder.getView(R.id.last_view).setVisibility(View.VISIBLE);
            holder.getView(R.id.first_view).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.first_view).setVisibility(View.GONE);
            holder.getView(R.id.last_view).setVisibility(View.GONE);
        }
        DisplayImageUtils.formatImgUrl(mContext, homeHotInfo.getCoverPicture(), (ImageView) holder.getView(R.id.iv_topic_cover));
        holder.setText(R.id.tv_chineseName, homeHotInfo.getChineseName());
        holder.setText(R.id.tv_englishName, homeHotInfo.getEnglishName());
    }
}

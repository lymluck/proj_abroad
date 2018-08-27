package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.smartstudy.commonlib.ui.activity.BrowserPictureActivity;
import com.smartstudy.commonlib.ui.adapter.base.CommonAdapter;
import com.smartstudy.commonlib.ui.adapter.base.ViewHolder;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.xxd.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2017/10/19.
 */

public class HighSchoolPicAdapter extends CommonAdapter<String> {

    private Context mContext;

    public HighSchoolPicAdapter(Context context, int layoutId, List<String> datas) {
        super(context, layoutId, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final String url, int position) {
        if (position == 0) {
            holder.getView(R.id.first_view).setVisibility(View.VISIBLE);
        } else if (position == mDatas.size() - 1) {
            holder.getView(R.id.last_view).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.first_view).setVisibility(View.GONE);
            holder.getView(R.id.last_view).setVisibility(View.GONE);
        }
        final ImageView iv_pic = holder.getView(R.id.iv_pic);
        DisplayImageUtils.formatImgUrl(mContext, url, iv_pic);
        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = mDatas.indexOf(url);
                mContext.startActivity(new Intent(mContext, BrowserPictureActivity.class)
                    .putExtra("index", index)
                    .putStringArrayListExtra("pathList", (ArrayList<String>) mDatas));
            }
        });
    }
}

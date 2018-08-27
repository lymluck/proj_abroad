package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smartstudy.commonlib.BaseApplication;
import com.smartstudy.commonlib.ui.adapter.LoopPagerAdapter;
import com.smartstudy.commonlib.ui.customView.rollviewpager.RollPagerView;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.xxd.entity.BannerInfo;
import com.smartstudy.xxd.ui.activity.ShowWebViewActivity;

import java.util.List;

/**
 * Created by louis on 17/5/16.
 */

public class XxdBannerAdapter extends LoopPagerAdapter {
    private List<BannerInfo> mDatas;
    private Context context;

    public XxdBannerAdapter(Context context, RollPagerView viewPager, List<BannerInfo> mDatas) {
        super(viewPager);
        this.context = context;
        this.mDatas = mDatas;
        context = null;
        mDatas = null;
    }

    @Override
    public View getView(ViewGroup container, final int position) {
        LinearLayout llyt_view = new LinearLayout(container.getContext());
        llyt_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView view = new ImageView(container.getContext());
        llyt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMoreDetails = new Intent(context, ShowWebViewActivity.class);
                toMoreDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                String url = mDatas.get(position).getAdUrl();
                if (url.contains("?")) {
                    url += "&app-type=android";
                } else {
                    url += "?app-type=android";
                }
                toMoreDetails.putExtra("web_url", url);
                toMoreDetails.putExtra("title", mDatas.get(position).getName());
                toMoreDetails.putExtra("url_action", "get");
                context.startActivity(toMoreDetails);
            }
        });
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        DisplayImageUtils.formatImgUrl(BaseApplication.appContext, mDatas.get(position).getImageUrl(), view);
        llyt_view.addView(view);
        view = null;
        return llyt_view;
    }

    @Override
    public int getRealCount() {
        return mDatas != null && mDatas.size() > 0 ? mDatas.size() : 0;
    }

    @Override
    public void destroy() {
        if (context != null) {
            context = null;
        }
        if (mDatas != null) {
            mDatas.clear();
            mDatas = null;
        }
        super.destroy();
    }
}

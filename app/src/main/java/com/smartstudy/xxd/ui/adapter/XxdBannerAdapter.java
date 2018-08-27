package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smartstudy.commonlib.app.BaseApplication;
import com.smartstudy.commonlib.ui.adapter.LoopPagerAdapter;
import com.smartstudy.commonlib.ui.customview.rollviewpager.RollPagerView;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.StatisticUtils;
import com.smartstudy.router.Router;
import com.smartstudy.xxd.entity.BannerInfo;

import java.util.List;

/**
 * Created by louis on 17/5/16.
 */

public class XxdBannerAdapter extends LoopPagerAdapter {
    private List<BannerInfo> mDatas;
    private Context mContext;

    public XxdBannerAdapter(Context context, RollPagerView viewPager, List<BannerInfo> mDatas) {
        super(viewPager);
        this.mContext = context;
        this.mDatas = mDatas;
        context = null;
        mDatas = null;
    }

    @Override
    public View getView(final ViewGroup container, final int position) {
        LinearLayout llyt_view = new LinearLayout(container.getContext());
        llyt_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView view = new ImageView(container.getContext());
        llyt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticUtils.actionEvent(container.getContext(), "8_A_banner_btn");
                String url = mDatas.get(position).getAdUrl();
                if (url.contains("?")) {
                    url += "&app-type=android&app_id=" + BaseApplication.appContext.getPackageName();
                } else {
                    url += "?app-type=android&app_id=" + BaseApplication.appContext.getPackageName();
                }
                Router.build("showWebView")
                        .with("web_url", url.replaceAll(" ", ""))
                        .with("title", mDatas.get(position).getName())
                        .with("url_action", "get")
                        .go(mContext);
            }
        });
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        DisplayImageUtils.formatImgUrl(BaseApplication.appContext, mDatas.get(position).getImageUrl(), view);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
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
        if (mDatas != null) {
            mDatas.clear();
            mDatas = null;
        }
        super.destroy();
    }
}

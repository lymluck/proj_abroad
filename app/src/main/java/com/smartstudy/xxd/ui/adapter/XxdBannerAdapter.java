package com.smartstudy.xxd.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smartstudy.commonlib.ui.adapter.LoopPagerAdapter;
import com.smartstudy.commonlib.ui.customview.rollviewpager.RollPagerView;
import com.smartstudy.commonlib.utils.AppUtils;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.router.Router;
import com.smartstudy.sdk.api.UApp;
import com.smartstudy.xxd.entity.BannerInfo;

import java.util.List;

import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_ACTION;
import static com.smartstudy.xxd.utils.AppContants.WEBVIEW_URL;

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
        final BannerInfo info = mDatas.get(position);
        LinearLayout llyt_view = new LinearLayout(container.getContext());
        llyt_view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView view = new ImageView(container.getContext());
        llyt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UApp.actionEvent(container.getContext(), "8_A_banner_btn");
                String url = info.getAdUrl();
                if (url.contains("?")) {
                    url += "&app-type=android&appName=" + mContext.getPackageName() + "&appVersion=" + AppUtils.getVersionName();
                } else {
                    url += "?app-type=android&appName=" + mContext.getPackageName() + "&appVersion=" + AppUtils.getVersionName();
                }
                Router.build("showWebView")
                    .with(WEBVIEW_URL, url.replaceAll(" ", ""))
                    .with("title", info.getName())
                    .with(WEBVIEW_ACTION, "get")
                    .go(mContext);
            }
        });
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        DisplayImageUtils.formatImgUrl(mContext, info.getImageUrl(), view);
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

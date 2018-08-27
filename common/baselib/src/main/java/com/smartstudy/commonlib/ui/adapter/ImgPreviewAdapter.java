package com.smartstudy.commonlib.ui.adapter;

import android.app.Activity;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.handler.WeakHandler;
import com.smartstudy.commonlib.ui.customview.transferimage.style.view.photoview.PhotoView;
import com.smartstudy.commonlib.utils.DisplayImageUtils;
import com.smartstudy.commonlib.utils.ParameterUtils;

import java.util.List;

/**
 * @author louis
 * @date on 2018/3/21
 * @describe 图片预览
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */

public class ImgPreviewAdapter extends PagerAdapter {
    private Activity mContext;
    private List<String> mDatas;
    private ViewPager mVp;
    private WeakHandler mHandler;

    public ImgPreviewAdapter(Activity context, ViewPager vp, WeakHandler handler, List<String> list) {
        this.mContext = context;
        this.mVp = vp;
        this.mHandler = handler;
        this.mDatas = list;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    /**
     * 点击某张图片预览时，系统自动调用此方法加载这张图片左右视图（如果有的话）
     */
    @Override
    public View instantiateItem(ViewGroup container, int position) {
        final PhotoView photoView = new PhotoView(mContext);
        photoView.enable();
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        photoView.setTag(R.id.glide_uri, position);
        final String path = mDatas.get(position);
        DisplayImageUtils.displayImageDrawable(mContext, path, new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                photoView.setImageDrawable(resource);
            }
        });
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.finish();
            }
        });
        photoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!TextUtils.isEmpty(path)) {
                    Message msg = Message.obtain();
                    msg.what = ParameterUtils.EMPTY_WHAT;
                    msg.obj = path;
                    mHandler.sendMessage(msg);
                    return true;
                }
                return false;
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getItemPosition(Object object) {
        View view = (View) object;
        int currentPage = mVp.getCurrentItem();
        if (currentPage == (Integer) view.getTag(R.id.glide_uri)) {
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

package com.smartstudy.commonlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.config.CustomShapeTransformation;
import com.smartstudy.commonlib.base.config.GlideCircleTransform;
import com.smartstudy.commonlib.base.config.GlideRoundTransform;
import com.smartstudy.commonlib.base.config.glideProgressSupport.ProgressTarget;

import java.io.File;


/**
 * 展示图片封装类
 * Created by louis on 2017/3/4.
 */
public class DisplayImageUtils {
    public static void displayGif(Context context, String url, ImageView view) {
        Glide.with(context).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
    }

    public static void displayGif(Context context, int resId, ImageView view) {
        Glide.with(context).load(resId).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
    }

    public static void displayImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.RESULT).placeholder(R.drawable.ic_img_default)
                .error(R.drawable.ic_img_default).centerCrop().dontAnimate().into(view);
    }

    public static void displayBubbleImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url).transform(new CustomShapeTransformation(context, R.drawable.bg_msg_img_left))
                .placeholder(R.drawable.bg_msg_img_left).error(R.drawable.bg_msg_img_left)
                .into(view);
    }

    public static void displayImageNoHolder(Context context, String url, ImageView view) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop().dontAnimate().into(view);
    }

    public static void displayImage(Context context, String url, ProgressTarget<String, GlideDrawable> progressTarget) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .into(progressTarget);
    }

    public static void displayLocationImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.location_default).centerCrop().dontAnimate().into(view);
    }

    public static void displayImage(Context context, String url, SimpleTarget<Bitmap> simpleTarget) {
        Glide.with(context).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.ic_img_default).centerCrop().dontAnimate().into(simpleTarget);
    }

    public static void displayImage(Context context, String url, ImageView view, RequestListener<String, GlideDrawable> listener) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.RESULT).listener(listener).into(view);
    }

    public static void downloadImageFile(Context context, String url, SimpleTarget<File> simpleTarget) {
        Glide.with(context).load(url).downloadOnly(simpleTarget);
    }

    public static void downloadImageFile(Context context, String url, int width, int height) {
        Glide.with(context).load(url).downloadOnly(width, height);
    }

    public static void displayImageFile(Context context, Bitmap btp, SimpleTarget<File> simpleTarget) {
        Glide.with(context).load(BitmapUtils.compressBitmap2Byte(btp)).downloadOnly(simpleTarget);
    }

    public static void displayImageRes(Context context, int id, ImageView view) {
        Glide.with(context).load(id).error(R.drawable.ic_img_default).centerCrop().into(view);
    }

    public static void displayImageRes(Context context, Drawable drawable, ImageView view) {
        Glide.with(context).load(drawable).error(R.drawable.ic_img_default).centerCrop().into(view);
    }

    public static void displayImageRes(Context context, Bitmap bitmap, ImageView view) {
        Glide.with(context).load(BitmapUtils.compressBitmap2Byte(bitmap)).centerCrop().error(R.drawable.ic_img_default).into(view);
    }

    public static void displayCircleImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).placeholder(R.drawable.ic_circleimg_default)
                .error(R.drawable.ic_circleimg_default).transform(new GlideCircleTransform(context))
                .dontAnimate().into(view);
    }

    public static void displayRoundImage(Context context, String url, ImageView view, int radius_dp) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.RESULT)
                .centerCrop().transform(new GlideRoundTransform(context, radius_dp))
                .dontAnimate().into(view);
    }

    public static void displayRoundImageNoHolder(Context context, String url, ImageView view, int radius_dp) {
        Glide.with(context).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideRoundTransform(context, radius_dp))
                .dontAnimate().into(view);
    }

    public static void displayCircleImageWithborder(Context context, String url, ImageView view,
                                                    int border_width, int border_color) {
        Glide.with(context).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).placeholder(R.drawable.ic_circleimg_default)
                .error(R.drawable.ic_circleimg_default).transform(new GlideCircleTransform(context, border_width, border_color))
                .dontAnimate().into(view);
    }

    public static void displayPersonImageWithborder(Context context, String url, ImageView view,
                                                    int border_width, int border_color) {
        Glide.with(context).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).placeholder(R.drawable.ic_person_default)
                .error(R.drawable.ic_person_default).transform(new GlideCircleTransform(context, border_width, border_color))
                .dontAnimate().into(view);
    }

    public static void displayPersonImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.RESULT).placeholder(R.drawable.ic_person_default)
                .error(R.drawable.ic_person_default).transform(new GlideCircleTransform(context))
                .dontAnimate().into(view);
    }

    public static void displayPersonImage(Context context, String url, ImageView view, RequestListener<String, GlideDrawable> listener) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.RESULT).listener(listener).placeholder(R.drawable.ic_person_default)
                .error(R.drawable.ic_person_default).transform(new GlideCircleTransform(context))
                .dontAnimate().into(view);
    }

    public static void displayPersonRes(Context context, int id, ImageView view) {
        Glide.with(context).load(id).error(R.drawable.ic_person_default).transform(new GlideCircleTransform(context)).dontAnimate().into(view);
    }

    public static void displayPersonRes(Context context, File file, ImageView view) {
        Glide.with(context).load(file).error(R.drawable.ic_person_default).transform(new GlideCircleTransform(context)).dontAnimate().into(view);
    }

    public static void displayPersonRes(Context context, Bitmap bmp, ImageView view) {
        Glide.with(context).load(BitmapUtils.compressBitmap2Byte(bmp)).error(R.drawable.ic_person_default).transform(new GlideCircleTransform(context))
                .dontAnimate().into(view);
    }

    public static String formatPersonImgUrl(final Context context, final String url, final ImageView iv) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_pad,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayPersonImage(context, url + params, iv);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatCircleImgUrl(final Context context, final String url, final ImageView iv) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_pad,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayCircleImage(context, url + params, iv);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatImgUrl(final Context context, final String url, final ImageView iv) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayImage(context, url + params, iv);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatImgUrl(final Context context, final String url, final ImageView iv, final RequestListener<String, GlideDrawable> listener) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayImage(context, url + params, iv, listener);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatImgUrlNoHolder(final Context context, final String url, final ImageView iv) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayImageNoHolder(context, url + params, iv);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatImgDownload(final Context context, final String url, final ImageView iv) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.downloadImageFile(context, url + params, width, height);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatRoundImgUrl(final Context context, final String url, final ImageView iv, final int radius) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayRoundImage(context, url + params, iv, radius);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatRoundImgUrlNoHolder(final Context context, final String url, final ImageView iv, final int radius) {
        String new_url = url;
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String params = "?x-oss-process=image/resize,m_lfit,w_" + width + ",h_" + height;
                    Log.d("url", url + params);
                    DisplayImageUtils.displayRoundImageNoHolder(context, url + params, iv, radius);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
        return new_url;
    }

    public static String formatImgUrl(String url, final String params) {
        String new_url = url;
        if (new_url != null && !"".equals(new_url)) {
            if (new_url.contains("!")) {
                new_url = new_url.split("!")[0] + params;
            } else {
                new_url = new_url + params;
            }
        }
        return new_url;
    }

    public static void pauseRequest(Context context) {
        Glide.with(context).pauseRequests();
    }

    public static void resumeRequest(Context context) {
        Glide.with(context).resumeRequests();
    }
}

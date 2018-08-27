package com.smartstudy.commonlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.base.config.CustomShapeTransformation;

import java.io.File;


/**
 * 展示图片封装类
 * Created by louis on 2017/3/4.
 */
public class DisplayImageUtils {

    public static void displayGif(Context context, String url, ImageView view) {
        Glide.with(context).asGif().load(url).into(view);
    }

    public static void displayGif(Context context, int resId, ImageView view) {
        Glide.with(context).asGif().load(resId).into(view);
    }

    public static void displayImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url)
            .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.ic_img_default)
                .error(R.drawable.ic_img_default).dontAnimate()).into(view);
    }

    public static void displayBubbleImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url)
            .apply(RequestOptions.bitmapTransform(new CustomShapeTransformation(context, R.drawable.bg_msg_img_left))
                .placeholder(R.drawable.bg_msg_img_left).error(R.drawable.bg_msg_img_left)).into(view);
    }

    public static void displayImageNoHolder(Context context, String url, ImageView view) {
        Glide.with(context).load(url)
            .apply(RequestOptions.centerCropTransform().dontAnimate()).into(view);
    }

    public static void displayLocationImage(Context context, String url, ImageView view) {
        Glide.with(context).load(url)
            .apply(RequestOptions.centerCropTransform()
                .error(R.drawable.location_default).dontAnimate()).into(view);
    }

    public static void displayImage(Context context, String url, SimpleTarget<Bitmap> simpleTarget) {
        Glide.with(context).asBitmap().load(url)
            .apply(RequestOptions.centerCropTransform()
                .error(R.drawable.ic_img_default).dontAnimate()).into(simpleTarget);
    }

    public static void displayImageDrawable(Context context, String url, SimpleTarget<Drawable> simpleTarget) {
        Glide.with(context).load(url)
            .apply(RequestOptions.centerCropTransform().placeholder(R.drawable.ic_img_default).dontAnimate()).into(simpleTarget);
    }

    public static void displayImage(Context context, String url, ImageView view, RequestListener<Drawable> listener) {
        Glide.with(context).load(url).listener(listener).into(view);
    }

    public static void downloadImageFile(Context context, String url, SimpleTarget<File> simpleTarget) {
        Glide.with(context).downloadOnly().load(url).into(simpleTarget);
    }

    public static void displayImageFile(Context context, Bitmap btp, SimpleTarget<File> simpleTarget) {
        Glide.with(context).downloadOnly().load(BitmapUtils.compressBitmap2Byte(btp)).into(simpleTarget);
    }

    public static void displayImageRes(Context context, int id, ImageView view) {
        Glide.with(context).load(id)
            .apply(RequestOptions.centerCropTransform().error(R.drawable.ic_img_default))
            .into(view);
    }

    public static void displayImageRes(Context context, Drawable drawable, ImageView view) {
        Glide.with(context).load(drawable)
            .apply(RequestOptions.centerCropTransform().error(R.drawable.ic_img_default))
            .into(view);
    }

    public static void displayImageRes(Context context, Bitmap bitmap, ImageView view) {
        Glide.with(context).load(BitmapUtils.compressBitmap2Byte(bitmap))
            .apply(RequestOptions.centerCropTransform().error(R.drawable.ic_img_default))
            .into(view);
    }

    public static void displayCircleImage(Context context, String url, ImageView view) {
        if (context != null) {
            Glide.with(context).load(url)
                .apply(RequestOptions.circleCropTransform()
                    .placeholder(R.drawable.ic_circleimg_default)
                    .error(R.drawable.ic_circleimg_default)
                    .dontAnimate()).into(view);
        }
    }

    public static void displayRoundImage(Context context, String url, ImageView view, int radius_dp) {
        Glide.with(context).load(url)
            .apply(RequestOptions.centerCropTransform().dontAnimate()
                .transform(new RoundedCorners(radius_dp)).placeholder(R.drawable.ic_img_default)).into(view);
    }

    public static void displayRoundImageNoHolder(Context context, String url, ImageView view, int radius_dp) {
        Glide.with(context).load(url)
            .apply(RequestOptions.centerCropTransform().dontAnimate()
                .transform(new RoundedCorners(radius_dp))).into(view);
    }

    public static void displayPersonImage(Context context, String url, ImageView view) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context).load(url)
                .apply(RequestOptions.circleCropTransform().error(R.drawable.ic_person_default).dontAnimate()
                    .placeholder(R.drawable.ic_person_default)).into(view);
        }
    }

    public static void displayPersonImage(Context context, String url, SimpleTarget<Drawable> target) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(context).load(url)
                .apply(RequestOptions.circleCropTransform().error(R.drawable.ic_person_default).dontAnimate()
                    .placeholder(R.drawable.ic_person_default)).into(target);
        }
    }

    public static void displayPersonRes(Context context, int id, ImageView view) {
        Glide.with(context).load(id)
            .apply(RequestOptions.circleCropTransform().error(R.drawable.ic_person_default).dontAnimate())
            .into(view);
    }

    public static void displayPersonRes(Context context, File file, ImageView view) {
        Glide.with(context).load(file)
            .apply(RequestOptions.circleCropTransform().error(R.drawable.ic_person_default).dontAnimate())
            .into(view);
    }

    public static void displayPersonRes(Context context, Bitmap bmp, ImageView view) {
        Glide.with(context).load(BitmapUtils.compressBitmap2Byte(bmp))
            .apply(RequestOptions.circleCropTransform().error(R.drawable.ic_person_default).dontAnimate())
            .into(view);
    }

    public static void formatPersonImgUrl(final Context context, final String url, final ImageView iv) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String newUrl = url;
                    if (width != 0 || height != 0) {
                        if (!TextUtils.isEmpty(newUrl)) {
                            newUrl = String.format(ParameterUtils.CIRCLE_IMG_URL_WH, url, width, height);
                        }
                    }
                    LogUtils.d("imgUrl===" + newUrl);
                    if (context instanceof AppCompatActivity
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((AppCompatActivity) context).isDestroyed()) {
                            DisplayImageUtils.displayPersonImage(context, newUrl, iv);
                        }
                    } else {
                        if (context != null) {
                            DisplayImageUtils.displayPersonImage(context, newUrl, iv);
                        }
                    }
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
    }

    public static void formatCircleImgUrl(final Context context, final String url, final ImageView iv) {
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String newUrl = url;
                    if (width != 0 || height != 0) {
                        if (!TextUtils.isEmpty(newUrl)) {
                            newUrl = String.format(ParameterUtils.CIRCLE_IMG_URL_WH, url, width, height);
                        }
                    }
                    LogUtils.d("imgUrl===" + newUrl);
                    if (context instanceof AppCompatActivity
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((AppCompatActivity) context).isDestroyed()) {
                            DisplayImageUtils.displayCircleImage(context, newUrl, iv);
                        }
                    } else {
                        if (context != null) {
                            DisplayImageUtils.displayCircleImage(context, newUrl, iv);
                        }
                    }
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
    }

    public static void formatImgUrl(final Context context, final String url, final ImageView iv) {
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String newUrl = url;
                    if (width != 0 || height != 0) {
                        if (!TextUtils.isEmpty(newUrl)) {
                            newUrl = String.format(ParameterUtils.IMG_URL_WH, url, width, height);
                        }
                    }
                    LogUtils.d("imgUrl===" + newUrl);
                    if (context instanceof AppCompatActivity
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((AppCompatActivity) context).isDestroyed()) {
                            DisplayImageUtils.displayImage(context, newUrl, iv);
                        }
                    } else {
                        if (context != null) {
                            DisplayImageUtils.displayImage(context, newUrl, iv);
                        }
                    }
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
    }

    public static void formatImgUrl(final Context context, final String url, final ImageView iv, final RequestListener<Drawable> listener) {
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String newUrl = url;
                    if (width != 0 || height != 0) {
                        if (!TextUtils.isEmpty(newUrl)) {
                            newUrl = String.format(ParameterUtils.IMG_URL_WH, url, width, height);
                        }
                    }
                    LogUtils.d("imgUrl===" + newUrl);
                    DisplayImageUtils.displayImage(context, newUrl, iv, listener);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
    }

    public static void formatImgUrlNoHolder(final Context context, final String url, final ImageView iv) {
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String newUrl = url;
                    if (width != 0 || height != 0) {
                        if (!TextUtils.isEmpty(newUrl)) {
                            newUrl = String.format(ParameterUtils.IMG_URL_WH, url, width, height);
                        }
                    }
                    LogUtils.d("imgUrl===" + newUrl);
                    if (context instanceof AppCompatActivity
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((AppCompatActivity) context).isDestroyed()) {
                            DisplayImageUtils.displayImageNoHolder(context, newUrl, iv);
                        }
                    } else {
                        if (context != null) {
                            DisplayImageUtils.displayImageNoHolder(context, newUrl, iv);
                        }
                    }
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
    }

    public static void formatRoundImgUrl(final Context context, final String url, final ImageView iv, final int radius) {
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String newUrl = url;
                    if (width != 0 || height != 0) {
                        if (!TextUtils.isEmpty(newUrl)) {
                            newUrl = String.format(ParameterUtils.IMG_URL_WH, url, width, height);
                        }
                    }
                    LogUtils.d("imgUrl===" + newUrl);
                    if (context instanceof AppCompatActivity
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((AppCompatActivity) context).isDestroyed()) {
                            DisplayImageUtils.displayRoundImage(context, newUrl, iv, radius);
                        }
                    } else {
                        if (context != null) {
                            DisplayImageUtils.displayRoundImage(context, newUrl, iv, radius);
                        }
                    }
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
    }

    public static void formatRoundImgUrlNoHolder(final Context context, final String url, final ImageView iv, final int radius) {
        iv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            boolean hasMeasured = false;

            @Override
            public boolean onPreDraw() {
                if (!hasMeasured) {
                    int width = iv.getMeasuredWidth();
                    int height = iv.getMeasuredHeight();
                    String newUrl = url;
                    if (width != 0 || height != 0) {
                        if (!TextUtils.isEmpty(newUrl)) {
                            newUrl = String.format(ParameterUtils.IMG_URL_WH, url, width, height);
                        }
                    }
                    LogUtils.d("imgUrl===" + newUrl);
                    DisplayImageUtils.displayRoundImageNoHolder(context, newUrl, iv, radius);
                    iv.getViewTreeObserver().removeOnPreDrawListener(this);
                    hasMeasured = true;
                }
                return true;
            }
        });
    }

    public static String formatImgUrl(String url, final String params) {
        String new_url = url;
        if (!TextUtils.isEmpty(new_url)) {
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

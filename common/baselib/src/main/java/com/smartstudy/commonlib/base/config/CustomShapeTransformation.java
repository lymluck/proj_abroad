package com.smartstudy.commonlib.base.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.smartstudy.commonlib.utils.ScreenUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;


public class CustomShapeTransformation extends BitmapTransformation {

    // 画笔
    private Paint mPaint;
    private Context mContext;
    // 形状的drawable资源
    private int mShapeRes;
    private static final String ID = "com.bumptech.glide.transformations.FillSpace";

    public CustomShapeTransformation(Context context, int shapeRes) {
        super();
        mContext = context;
        mShapeRes = shapeRes;
        // 实例化Paint对象，并设置Xfermode为SRC_IN
        mPaint = new Paint();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    @Override
    // 复写该方法，完成图片的转换
    public Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        // 获取到形状资源的Drawable对象
        Drawable shape = ContextCompat.getDrawable(mContext, mShapeRes);
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        if (height != 0) {
            double scale = (width * 1.00) / height;
            if (width >= height) {
                width = getBitmapWidth();
                height = (int) (width / scale);
            } else {
                height = getBitmapHeight();
                width = (int) (height * scale);
            }
        } else {
            width = 100;
            height = 100;
        }
        // 居中裁剪图片，调用Glide库中TransformationUtils类的centerCrop()方法完成裁剪，保证图片居中且填满
        final Bitmap toReuse = pool.get(width, height, toTransform.getConfig() != null
            ? toTransform.getConfig() : Bitmap.Config.ARGB_8888);
        Bitmap transformed = TransformationUtils.centerCrop(pool, toTransform, width, height);
        if (toReuse != null && toReuse != transformed) {
            toReuse.recycle();
        }

        // 根据算出的宽高新建Bitmap对象并设置到画布上
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // 设置形状的大小与图片的大小一致
        shape.setBounds(0, 0, width, height);
        // 将图片画到画布上
        shape.draw(canvas);
        // 将裁剪后的图片画得画布上
        canvas.drawBitmap(transformed, 0, 0, mPaint);

        return bitmap;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof CustomShapeTransformation;
    }


    @Override
    public int hashCode() {
        // 用于缓存的唯一标识符
        return ID.hashCode();
    }

    public int getBitmapWidth() {
        return ScreenUtils.getScreenWidth() / 3;
    }

    public int getBitmapHeight() {
        return ScreenUtils.getScreenHeight() / 4;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        try {
            messageDigest.update(ID.getBytes(STRING_CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
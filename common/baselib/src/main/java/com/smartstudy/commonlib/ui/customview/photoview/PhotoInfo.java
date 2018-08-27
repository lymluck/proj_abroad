package com.smartstudy.commonlib.ui.customview.photoview;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class PhotoInfo implements Parcelable {

    // 内部图片在整个手机界面的位置
    RectF mRect = new RectF();

    // 控件在窗口的位置
    RectF mImgRect = new RectF();

    RectF mWidgetRect = new RectF();

    RectF mBaseRect = new RectF();

    PointF mScreenCenter = new PointF();

    float mScale;

    float mDegrees;

    ImageView.ScaleType mScaleType;

    public PhotoInfo(RectF rect, RectF img, RectF widget, RectF base, PointF screenCenter, float scale, float degrees, ImageView.ScaleType scaleType) {
        mRect.set(rect);
        mImgRect.set(img);
        mWidgetRect.set(widget);
        mScale = scale;
        mScaleType = scaleType;
        mDegrees = degrees;
        mBaseRect.set(base);
        mScreenCenter.set(screenCenter);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mRect, 0);
        dest.writeParcelable(this.mImgRect, 0);
        dest.writeParcelable(this.mWidgetRect, 0);
        dest.writeParcelable(this.mBaseRect, 0);
        dest.writeParcelable(this.mScreenCenter, 0);
        dest.writeFloat(this.mScale);
        dest.writeFloat(this.mDegrees);
        dest.writeInt(this.mScaleType == null ? -1 : this.mScaleType.ordinal());
    }

    protected PhotoInfo(Parcel in) {
        this.mRect = in.readParcelable(RectF.class.getClassLoader());
        this.mImgRect = in.readParcelable(RectF.class.getClassLoader());
        this.mWidgetRect = in.readParcelable(RectF.class.getClassLoader());
        this.mBaseRect = in.readParcelable(RectF.class.getClassLoader());
        this.mScreenCenter = in.readParcelable(PointF.class.getClassLoader());
        this.mScale = in.readFloat();
        this.mDegrees = in.readFloat();
        int tmpMScaleType = in.readInt();
        this.mScaleType = tmpMScaleType == -1 ? null : ImageView.ScaleType.values()[tmpMScaleType];
    }

    public static final Parcelable.Creator<PhotoInfo> CREATOR = new Parcelable.Creator<PhotoInfo>() {
        @Override
        public PhotoInfo createFromParcel(Parcel source) {
            return new PhotoInfo(source);
        }

        @Override
        public PhotoInfo[] newArray(int size) {
            return new PhotoInfo[size];
        }
    };
}

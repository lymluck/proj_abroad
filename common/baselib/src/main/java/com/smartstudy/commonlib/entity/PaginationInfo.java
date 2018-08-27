package com.smartstudy.commonlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louis on 2017/3/2.
 */

public class PaginationInfo implements Parcelable {

    /**
     * count : 12947
     * page : 1
     * pageSize : 20
     */

    private int count;
    private int page;
    private int pageSize;

    public void setCount(int count) {
        this.count = count;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.count);
        dest.writeInt(this.page);
        dest.writeInt(this.pageSize);
    }

    public PaginationInfo() {
    }

    protected PaginationInfo(Parcel in) {
        this.count = in.readInt();
        this.page = in.readInt();
        this.pageSize = in.readInt();
    }

    public static final Parcelable.Creator<PaginationInfo> CREATOR = new Parcelable.Creator<PaginationInfo>() {
        public PaginationInfo createFromParcel(Parcel source) {
            return new PaginationInfo(source);
        }

        public PaginationInfo[] newArray(int size) {
            return new PaginationInfo[size];
        }
    };

    @Override
    public String toString() {
        return "PaginationInfo{" +
                "count=" + count +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}

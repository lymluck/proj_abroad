package com.smartstudy.commonlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louis on 2017/3/7.
 */

public class SpecialRankInfo implements Parcelable {

    /**
     * id : 98
     * scale : world
     * year : 2016
     * name : QS教育学排名
     * abbr : 教育学排名
     */

    private String id;
    private String scale;
    private int year;
    private String name;
    private String abbr;


    public void setScale(String scale) {
        this.scale = scale;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScale() {
        return scale;
    }

    public int getYear() {
        return year;
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbr;
    }

    @Override
    public String toString() {
        return "SpecialRankInfo{" +
                "id=" + id +
                ", scale='" + scale + '\'' +
                ", year=" + year +
                ", name='" + name + '\'' +
                ", abbr='" + abbr + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.scale);
        dest.writeInt(this.year);
        dest.writeString(this.name);
        dest.writeString(this.abbr);
    }

    public SpecialRankInfo() {
    }

    protected SpecialRankInfo(Parcel in) {
        this.id = in.readString();
        this.scale = in.readString();
        this.year = in.readInt();
        this.name = in.readString();
        this.abbr = in.readString();
    }

    public static final Parcelable.Creator<SpecialRankInfo> CREATOR = new Parcelable.Creator<SpecialRankInfo>() {
        public SpecialRankInfo createFromParcel(Parcel source) {
            return new SpecialRankInfo(source);
        }

        public SpecialRankInfo[] newArray(int size) {
            return new SpecialRankInfo[size];
        }
    };
}

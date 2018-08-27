package com.smartstudy.xxd.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 17/6/29.
 */

public class HotRankInfo implements Parcelable {

    /**
     * id : 607
     * type : USNEWS
     * year : 2017
     * title : 世界大学排名
     */

    private String id;
    private String schoolType;
    private String type;
    private String year;
    private String title;
    private String backgroundImage;
    private boolean isMajorRank;
    private boolean isWorldRank;
    private ArrayList<HotRankInfo> rankings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public boolean isMajorRank() {
        return isMajorRank;
    }

    public void setMajorRank(boolean majorRank) {
        isMajorRank = majorRank;
    }

    public boolean isWorldRank() {
        return isWorldRank;
    }

    public void setWorldRank(boolean worldRank) {
        isWorldRank = worldRank;
    }

    public ArrayList<HotRankInfo> getRankings() {
        return rankings;
    }

    public void setRankings(ArrayList<HotRankInfo> rankings) {
        this.rankings = rankings;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.schoolType);
        dest.writeString(this.type);
        dest.writeString(this.year);
        dest.writeString(this.title);
        dest.writeString(this.backgroundImage);
        dest.writeByte(isMajorRank ? (byte) 1 : (byte) 0);
        dest.writeByte(isWorldRank ? (byte) 1 : (byte) 0);
        dest.writeList(this.rankings);
    }

    public HotRankInfo() {
    }

    protected HotRankInfo(Parcel in) {
        this.id = in.readString();
        this.schoolType = in.readString();
        this.type = in.readString();
        this.year = in.readString();
        this.title = in.readString();
        this.backgroundImage = in.readString();
        this.isMajorRank = in.readByte() != 0;
        this.isWorldRank = in.readByte() != 0;
        this.rankings = new ArrayList<HotRankInfo>();
        in.readList(this.rankings, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<HotRankInfo> CREATOR = new Parcelable.Creator<HotRankInfo>() {
        @Override
        public HotRankInfo createFromParcel(Parcel source) {
            return new HotRankInfo(source);
        }

        @Override
        public HotRankInfo[] newArray(int size) {
            return new HotRankInfo[size];
        }
    };

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
            .append(id).append('\"');
        sb.append(",\"schoolType\":\"")
            .append(schoolType).append('\"');
        sb.append(",\"type\":\"")
            .append(type).append('\"');
        sb.append(",\"year\":\"")
            .append(year).append('\"');
        sb.append(",\"title\":\"")
            .append(title).append('\"');
        sb.append(",\"backgroundImage\":\"")
            .append(backgroundImage).append('\"');
        sb.append(",\"isMajorRank\":")
            .append(isMajorRank);
        sb.append(",\"isWorldRank\":")
            .append(isWorldRank);
        sb.append(",\"rankings\":")
            .append(rankings);
        sb.append('}');
        return sb.toString();
    }
}

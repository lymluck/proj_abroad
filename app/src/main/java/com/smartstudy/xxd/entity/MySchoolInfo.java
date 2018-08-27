package com.smartstudy.xxd.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louis on 17/3/17.
 */

public class MySchoolInfo implements Parcelable {
    /**
     * id : 2394
     * matchTypeId : MS_MATCH_TYPE_BOTTOM
     * sourceId : MS_SOURCE_AUTO_MATCH
     * prob : 0.720545618077606
     * createTime : 2017-03-17T08:31:11.238Z
     * degreeId : DEGREE_1
     * school :
     * degreeName : 本科
     */

    private int id;
    private String matchTypeId;
    private String sourceId;
    private double prob;
    private String createTime;
    private String degreeId;
    private String school;
    private String degreeName;

    public void setId(int id) {
        this.id = id;
    }

    public void setMatchTypeId(String matchTypeId) {
        this.matchTypeId = matchTypeId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setDegreeId(String degreeId) {
        this.degreeId = degreeId;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public int getId() {
        return id;
    }

    public String getMatchTypeId() {
        return matchTypeId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public double getProb() {
        return prob;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getDegreeId() {
        return degreeId;
    }

    public String getSchool() {
        return school;
    }

    public String getDegreeName() {
        return degreeName;
    }

    @Override
    public String toString() {
        return "MySchoolInfo{" +
                "id=" + id +
                ", matchTypeId='" + matchTypeId + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", prob=" + prob +
                ", createTime='" + createTime + '\'' +
                ", degreeId='" + degreeId + '\'' +
                ", school='" + school + '\'' +
                ", degreeName='" + degreeName + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.matchTypeId);
        dest.writeString(this.sourceId);
        dest.writeDouble(this.prob);
        dest.writeString(this.createTime);
        dest.writeString(this.degreeId);
        dest.writeString(this.school);
        dest.writeString(this.degreeName);
    }

    public MySchoolInfo() {
    }

    protected MySchoolInfo(Parcel in) {
        this.id = in.readInt();
        this.matchTypeId = in.readString();
        this.sourceId = in.readString();
        this.prob = in.readDouble();
        this.createTime = in.readString();
        this.degreeId = in.readString();
        this.school = in.readString();
        this.degreeName = in.readString();
    }

    public static final Parcelable.Creator<MySchoolInfo> CREATOR = new Parcelable.Creator<MySchoolInfo>() {
        @Override
        public MySchoolInfo createFromParcel(Parcel source) {
            return new MySchoolInfo(source);
        }

        @Override
        public MySchoolInfo[] newArray(int size) {
            return new MySchoolInfo[size];
        }
    };
}

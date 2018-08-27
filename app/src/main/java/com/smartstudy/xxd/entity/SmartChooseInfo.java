package com.smartstudy.xxd.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louis on 17/4/20.
 */

public class SmartChooseInfo implements Parcelable {
    private String schoolId;
    private String countryId;
    private String projId;
    private String gradeId;
    private int toeflScore;
    private double ieltsScore;
    private int greScore;
    private int gmatScore;
    private String rankTop;
    private int schoolScore;
    private int satScore;
    private int actScore;

    public int getToeflScore() {
        return toeflScore;
    }

    public void setToeflScore(int toeflScore) {
        this.toeflScore = toeflScore;
    }

    public double getIeltsScore() {
        return ieltsScore;
    }

    public void setIeltsScore(double ieltsScore) {
        this.ieltsScore = ieltsScore;
    }

    public int getActScore() {
        return actScore;
    }

    public void setActScore(int actScore) {
        this.actScore = actScore;
    }

    public int getGreScore() {
        return greScore;
    }

    public void setGreScore(int greScore) {
        this.greScore = greScore;
    }

    public int getGmatScore() {
        return gmatScore;
    }

    public void setGmatScore(int gmatScore) {
        this.gmatScore = gmatScore;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getRankTop() {
        return rankTop;
    }

    public void setRankTop(String rankTop) {
        this.rankTop = rankTop;
    }

    public int getSchoolScore() {
        return schoolScore;
    }

    public void setSchoolScore(int schoolScore) {
        this.schoolScore = schoolScore;
    }

    public int getSatScore() {
        return satScore;
    }

    public void setSatScore(int satScore) {
        this.satScore = satScore;
    }


    public SmartChooseInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.schoolId);
        dest.writeString(this.countryId);
        dest.writeString(this.projId);
        dest.writeString(this.gradeId);
        dest.writeInt(this.toeflScore);
        dest.writeDouble(this.ieltsScore);
        dest.writeInt(this.greScore);
        dest.writeInt(this.gmatScore);
        dest.writeString(this.rankTop);
        dest.writeInt(this.schoolScore);
        dest.writeInt(this.satScore);
        dest.writeInt(this.actScore);
    }

    protected SmartChooseInfo(Parcel in) {
        this.schoolId = in.readString();
        this.countryId = in.readString();
        this.projId = in.readString();
        this.gradeId = in.readString();
        this.toeflScore = in.readInt();
        this.ieltsScore = in.readDouble();
        this.greScore = in.readInt();
        this.gmatScore = in.readInt();
        this.rankTop = in.readString();
        this.schoolScore = in.readInt();
        this.satScore = in.readInt();
        this.actScore = in.readInt();
    }

    public static final Creator<SmartChooseInfo> CREATOR = new Creator<SmartChooseInfo>() {
        @Override
        public SmartChooseInfo createFromParcel(Parcel source) {
            return new SmartChooseInfo(source);
        }

        @Override
        public SmartChooseInfo[] newArray(int size) {
            return new SmartChooseInfo[size];
        }
    };
}

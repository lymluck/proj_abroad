package com.smartstudy.commonlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louis on 2017/3/7.
 */

public class SchooolRankInfo implements Parcelable {

    /**
     * id : 341501
     * rankCategoryId : 607
     * schoolId : 4
     * rank : 1
     * chineseName : 哈佛大学
     * englishName : Harvard University
     * school_id : 4
     * establishmentYear : 1636
     * logo : http://upload.beikaodi.com/school/logo/RO/ZR/ZxbbjdxQtk6XTQeUqHP10d24y0bc.jpg
     * countryName : 美国
     * provinceName : 马萨诸塞
     * cityName : 波士顿
     * typeName : 私立
     * TIE_ADMINSSION_RATE : 6%
     * feeTotal : $67,401
     * scoreToefl : 100
     * scoreIelts : 6.5
     * scoreSat : 2110
     * gpa : 3.8
     */

    private int id;
    private int rankCategoryId;
    private String schoolId;
    private String rank;
    private String chineseName;
    private String englishName;
    private String school_id;
    private int establishmentYear;
    private String logo;
    private String countryName;
    private String provinceName;
    private String cityName;
    private String typeName;
    private String TIE_ADMINSSION_RATE;
    private String feeTotal;
    private String scoreToefl;
    private String scoreIelts;
    private String scoreSat;
    private String gpa;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRankCategoryId() {
        return rankCategoryId;
    }

    public void setRankCategoryId(int rankCategoryId) {
        this.rankCategoryId = rankCategoryId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public int getEstablishmentYear() {
        return establishmentYear;
    }

    public void setEstablishmentYear(int establishmentYear) {
        this.establishmentYear = establishmentYear;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTIE_ADMINSSION_RATE() {
        return TIE_ADMINSSION_RATE;
    }

    public void setTIE_ADMINSSION_RATE(String TIE_ADMINSSION_RATE) {
        this.TIE_ADMINSSION_RATE = TIE_ADMINSSION_RATE;
    }

    public String getFeeTotal() {
        return feeTotal;
    }

    public void setFeeTotal(String feeTotal) {
        this.feeTotal = feeTotal;
    }

    public String getScoreToefl() {
        return scoreToefl;
    }

    public void setScoreToefl(String scoreToefl) {
        this.scoreToefl = scoreToefl;
    }

    public String getScoreIelts() {
        return scoreIelts;
    }

    public void setScoreIelts(String scoreIelts) {
        this.scoreIelts = scoreIelts;
    }

    public String getScoreSat() {
        return scoreSat;
    }

    public void setScoreSat(String scoreSat) {
        this.scoreSat = scoreSat;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return "SchooolRankInfo{" +
                "id=" + id +
                ", rankCategoryId=" + rankCategoryId +
                ", schoolId='" + schoolId + '\'' +
                ", rank='" + rank + '\'' +
                ", chineseName='" + chineseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", school_id='" + school_id + '\'' +
                ", establishmentYear=" + establishmentYear +
                ", logo='" + logo + '\'' +
                ", countryName='" + countryName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", cityName='" + cityName + '\'' +
                ", typeName='" + typeName + '\'' +
                ", TIE_ADMINSSION_RATE='" + TIE_ADMINSSION_RATE + '\'' +
                ", feeTotal='" + feeTotal + '\'' +
                ", scoreToefl='" + scoreToefl + '\'' +
                ", scoreIelts='" + scoreIelts + '\'' +
                ", scoreSat='" + scoreSat + '\'' +
                ", gpa='" + gpa + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.rankCategoryId);
        dest.writeString(this.schoolId);
        dest.writeString(this.rank);
        dest.writeString(this.chineseName);
        dest.writeString(this.englishName);
        dest.writeString(this.school_id);
        dest.writeInt(this.establishmentYear);
        dest.writeString(this.logo);
        dest.writeString(this.countryName);
        dest.writeString(this.provinceName);
        dest.writeString(this.cityName);
        dest.writeString(this.typeName);
        dest.writeString(this.TIE_ADMINSSION_RATE);
        dest.writeString(this.feeTotal);
        dest.writeString(this.scoreToefl);
        dest.writeString(this.scoreIelts);
        dest.writeString(this.scoreSat);
        dest.writeString(this.gpa);
    }

    public SchooolRankInfo() {
    }

    protected SchooolRankInfo(Parcel in) {
        this.id = in.readInt();
        this.rankCategoryId = in.readInt();
        this.schoolId = in.readString();
        this.rank = in.readString();
        this.chineseName = in.readString();
        this.englishName = in.readString();
        this.school_id = in.readString();
        this.establishmentYear = in.readInt();
        this.logo = in.readString();
        this.countryName = in.readString();
        this.provinceName = in.readString();
        this.cityName = in.readString();
        this.typeName = in.readString();
        this.TIE_ADMINSSION_RATE = in.readString();
        this.feeTotal = in.readString();
        this.scoreToefl = in.readString();
        this.scoreIelts = in.readString();
        this.scoreSat = in.readString();
        this.gpa = in.readString();
    }

    public static final Creator<SchooolRankInfo> CREATOR = new Creator<SchooolRankInfo>() {
        public SchooolRankInfo createFromParcel(Parcel source) {
            return new SchooolRankInfo(source);
        }

        public SchooolRankInfo[] newArray(int size) {
            return new SchooolRankInfo[size];
        }
    };
}

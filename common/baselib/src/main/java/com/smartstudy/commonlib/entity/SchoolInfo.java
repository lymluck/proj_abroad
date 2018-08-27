package com.smartstudy.commonlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louis on 2017/3/1.
 */

public class SchoolInfo implements Parcelable {

    /**
     * id : 1
     * chineseName : 牛津大学
     * englishName : University of Oxford
     * logo : http://upload.beikaodi.com/school/logo/f5/d0/f5d09d97ac4bab2f225a8e32f2617199.jpg
     * coverPicture : http://upload.beikaodi.com/school/photo/8b/af/e9a8efade4413a821b6131075ccf.jpg
     * countryId : COUNTRY_225
     * establishmentYear : 1096
     * worldRank : 6
     * localRank : 1
     * order : 5
     * feeTotal : 32,082美元
     * countryName : 英国
     * provinceName : 英格兰
     * cityName : 牛津
     * typeName : 公立
     * TIE_ADMINSSION_RATE : 18%
     * scoreToefl : 110
     * scoreIelts : 7.0
     * scoreSat :
     * gpa : 3.5
     */

    private int id;
    private String chineseName;
    private String englishName;
    private String logo;
    private String coverPicture;
    private String countryId;
    private String establishmentYear;
    private String worldRank;
    private String localRank;
    private String order;
    private String feeTotal;
    private String countryName;
    private String provinceName;
    private String cityName;
    private String typeName;
    private String TIE_ADMINSSION_RATE;
    private String scoreToefl;
    private String scoreIelts;
    private String scoreSat;
    private String gpa;
    private String majorChineseName;
    private String majorEnglishName;
    private boolean selected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getEstablishmentYear() {
        return establishmentYear;
    }

    public void setEstablishmentYear(String establishmentYear) {
        this.establishmentYear = establishmentYear;
    }

    public String getWorldRank() {
        return worldRank;
    }

    public void setWorldRank(String worldRank) {
        this.worldRank = worldRank;
    }

    public String getLocalRank() {
        return localRank;
    }

    public void setLocalRank(String localRank) {
        this.localRank = localRank;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFeeTotal() {
        return feeTotal;
    }

    public void setFeeTotal(String feeTotal) {
        this.feeTotal = feeTotal;
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

    public String getMajorChineseName() {
        return majorChineseName;
    }

    public void setMajorChineseName(String majorChineseName) {
        this.majorChineseName = majorChineseName;
    }

    public String getMajorEnglishName() {
        return majorEnglishName;
    }

    public void setMajorEnglishName(String majorEnglishName) {
        this.majorEnglishName = majorEnglishName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.chineseName);
        dest.writeString(this.englishName);
        dest.writeString(this.logo);
        dest.writeString(this.coverPicture);
        dest.writeString(this.countryId);
        dest.writeString(this.establishmentYear);
        dest.writeString(this.worldRank);
        dest.writeString(this.localRank);
        dest.writeString(this.order);
        dest.writeString(this.feeTotal);
        dest.writeString(this.countryName);
        dest.writeString(this.provinceName);
        dest.writeString(this.cityName);
        dest.writeString(this.typeName);
        dest.writeString(this.TIE_ADMINSSION_RATE);
        dest.writeString(this.scoreToefl);
        dest.writeString(this.scoreIelts);
        dest.writeString(this.scoreSat);
        dest.writeString(this.gpa);
        dest.writeString(this.majorChineseName);
        dest.writeString(this.majorEnglishName);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
    }

    public SchoolInfo() {
    }

    protected SchoolInfo(Parcel in) {
        this.id = in.readInt();
        this.chineseName = in.readString();
        this.englishName = in.readString();
        this.logo = in.readString();
        this.coverPicture = in.readString();
        this.countryId = in.readString();
        this.establishmentYear = in.readString();
        this.worldRank = in.readString();
        this.localRank = in.readString();
        this.order = in.readString();
        this.feeTotal = in.readString();
        this.countryName = in.readString();
        this.provinceName = in.readString();
        this.cityName = in.readString();
        this.typeName = in.readString();
        this.TIE_ADMINSSION_RATE = in.readString();
        this.scoreToefl = in.readString();
        this.scoreIelts = in.readString();
        this.scoreSat = in.readString();
        this.gpa = in.readString();
        this.majorChineseName = in.readString();
        this.majorEnglishName = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<SchoolInfo> CREATOR = new Creator<SchoolInfo>() {
        @Override
        public SchoolInfo createFromParcel(Parcel source) {
            return new SchoolInfo(source);
        }

        @Override
        public SchoolInfo[] newArray(int size) {
            return new SchoolInfo[size];
        }
    };
}

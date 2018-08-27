package com.smartstudy.commonlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by louis on 17/3/16.
 */

public class MyInfo implements Parcelable {

    /**
     * id : 1525
     * name : 林康
     * avatar : https://upload.beikaodi.com/customer/avatar/d4/f4/d4f4f11a62246f2814292168de78a12b.png
     * genderId : GENDER_MALE
     * contact : 15651768870
     * email : linkang@innobuddy.com
     * weixin : abc
     * provinceId : PROVINCE_11
     * cityId : CITY_134
     * address : null
     * currentSchool : null
     * currentGradeId : null
     * currentMajor : null
     * currentRankId : null
     * targetDegreeId : null
     * targetCountryId : null
     * targetMajor : null
     * targetRangeId : null
     * scoreToefl : null
     * scoreIelts : null
     * scoreSat : null
     * scoreGre : null
     * scoreGmat : null
     * selfIntroduction : null
     * graduateTime
     * admissionTime
     * genderName : 男
     * provinceName : 浙江
     * cityName : 杭州
     * currentGradeName : null
     * currentRankName : null
     * targetCountryName : null
     * targetDegreeName : null
     * targetRangeName : null
     */

    private int id;
    private String name;
    private String avatar;
    private String genderId;
    private String contact;
    private String email;
    private String weixin;
    private String provinceId;
    private String cityId;
    private String address;
    private String currentSchool;
    private String currentGradeId;
    private String currentMajor;
    private String currentRankId;
    private String targetDegreeId;
    private String targetCountryId;
    private String targetMajor;
    private String targetRangeId;
    private String scoreToefl;
    private String scoreIelts;
    private String scoreSat;
    private String scoreGre;
    private String scoreGmat;
    private String selfIntroduction;
    private Date graduateTime;
    private Date admissionTime;
    private String genderName;
    private String provinceName;
    private String cityName;
    private String currentGradeName;
    private String currentRankName;
    private String targetCountryName;
    private String targetDegreeName;
    private String targetRangeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGenderId() {
        return genderId;
    }

    public void setGenderId(String genderId) {
        this.genderId = genderId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrentSchool() {
        return currentSchool;
    }

    public void setCurrentSchool(String currentSchool) {
        this.currentSchool = currentSchool;
    }

    public String getCurrentGradeId() {
        return currentGradeId;
    }

    public void setCurrentGradeId(String currentGradeId) {
        this.currentGradeId = currentGradeId;
    }

    public String getCurrentMajor() {
        return currentMajor;
    }

    public void setCurrentMajor(String currentMajor) {
        this.currentMajor = currentMajor;
    }

    public String getCurrentRankId() {
        return currentRankId;
    }

    public void setCurrentRankId(String currentRankId) {
        this.currentRankId = currentRankId;
    }

    public String getTargetDegreeId() {
        return targetDegreeId;
    }

    public void setTargetDegreeId(String targetDegreeId) {
        this.targetDegreeId = targetDegreeId;
    }

    public String getTargetCountryId() {
        return targetCountryId;
    }

    public void setTargetCountryId(String targetCountryId) {
        this.targetCountryId = targetCountryId;
    }

    public String getTargetMajor() {
        return targetMajor;
    }

    public void setTargetMajor(String targetMajor) {
        this.targetMajor = targetMajor;
    }

    public String getTargetRangeId() {
        return targetRangeId;
    }

    public void setTargetRangeId(String targetRangeId) {
        this.targetRangeId = targetRangeId;
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

    public String getScoreGre() {
        return scoreGre;
    }

    public void setScoreGre(String scoreGre) {
        this.scoreGre = scoreGre;
    }

    public String getScoreGmat() {
        return scoreGmat;
    }

    public void setScoreGmat(String scoreGmat) {
        this.scoreGmat = scoreGmat;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public Date getGraduateTime() {
        return graduateTime;
    }

    public void setGraduateTime(Date graduateTime) {
        this.graduateTime = graduateTime;
    }

    public Date getAdmissionTime() {
        return admissionTime;
    }

    public void setAdmissionTime(Date admissionTime) {
        this.admissionTime = admissionTime;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
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

    public String getCurrentGradeName() {
        return currentGradeName;
    }

    public void setCurrentGradeName(String currentGradeName) {
        this.currentGradeName = currentGradeName;
    }

    public String getCurrentRankName() {
        return currentRankName;
    }

    public void setCurrentRankName(String currentRankName) {
        this.currentRankName = currentRankName;
    }

    public String getTargetCountryName() {
        return targetCountryName;
    }

    public void setTargetCountryName(String targetCountryName) {
        this.targetCountryName = targetCountryName;
    }

    public String getTargetDegreeName() {
        return targetDegreeName;
    }

    public void setTargetDegreeName(String targetDegreeName) {
        this.targetDegreeName = targetDegreeName;
    }

    public String getTargetRangeName() {
        return targetRangeName;
    }

    public void setTargetRangeName(String targetRangeName) {
        this.targetRangeName = targetRangeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeString(this.genderId);
        dest.writeString(this.contact);
        dest.writeString(this.email);
        dest.writeString(this.weixin);
        dest.writeString(this.provinceId);
        dest.writeString(this.cityId);
        dest.writeString(this.address);
        dest.writeString(this.currentSchool);
        dest.writeString(this.currentGradeId);
        dest.writeString(this.currentMajor);
        dest.writeString(this.currentRankId);
        dest.writeString(this.targetDegreeId);
        dest.writeString(this.targetCountryId);
        dest.writeString(this.targetMajor);
        dest.writeString(this.targetRangeId);
        dest.writeString(this.scoreToefl);
        dest.writeString(this.scoreIelts);
        dest.writeString(this.scoreSat);
        dest.writeString(this.scoreGre);
        dest.writeString(this.scoreGmat);
        dest.writeString(this.selfIntroduction);
        dest.writeLong(graduateTime != null ? graduateTime.getTime() : -1);
        dest.writeLong(admissionTime != null ? admissionTime.getTime() : -1);
        dest.writeString(this.genderName);
        dest.writeString(this.provinceName);
        dest.writeString(this.cityName);
        dest.writeString(this.currentGradeName);
        dest.writeString(this.currentRankName);
        dest.writeString(this.targetCountryName);
        dest.writeString(this.targetDegreeName);
        dest.writeString(this.targetRangeName);
    }

    public MyInfo() {
    }

    protected MyInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.avatar = in.readString();
        this.genderId = in.readString();
        this.contact = in.readString();
        this.email = in.readString();
        this.weixin = in.readString();
        this.provinceId = in.readString();
        this.cityId = in.readString();
        this.address = in.readString();
        this.currentSchool = in.readString();
        this.currentGradeId = in.readString();
        this.currentMajor = in.readString();
        this.currentRankId = in.readString();
        this.targetDegreeId = in.readString();
        this.targetCountryId = in.readString();
        this.targetMajor = in.readString();
        this.targetRangeId = in.readString();
        this.scoreToefl = in.readString();
        this.scoreIelts = in.readString();
        this.scoreSat = in.readString();
        this.scoreGre = in.readString();
        this.scoreGmat = in.readString();
        this.selfIntroduction = in.readString();
        long tmpGraduateTime = in.readLong();
        this.graduateTime = tmpGraduateTime == -1 ? null : new Date(tmpGraduateTime);
        long tmpAdmissionTime = in.readLong();
        this.admissionTime = tmpAdmissionTime == -1 ? null : new Date(tmpAdmissionTime);
        this.genderName = in.readString();
        this.provinceName = in.readString();
        this.cityName = in.readString();
        this.currentGradeName = in.readString();
        this.currentRankName = in.readString();
        this.targetCountryName = in.readString();
        this.targetDegreeName = in.readString();
        this.targetRangeName = in.readString();
    }

    public static final Parcelable.Creator<MyInfo> CREATOR = new Parcelable.Creator<MyInfo>() {
        public MyInfo createFromParcel(Parcel source) {
            return new MyInfo(source);
        }

        public MyInfo[] newArray(int size) {
            return new MyInfo[size];
        }
    };
}

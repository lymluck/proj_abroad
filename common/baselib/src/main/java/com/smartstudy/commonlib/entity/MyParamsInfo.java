package com.smartstudy.commonlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.Date;

/**
 * Created by louis on 17/3/15.
 */

public class MyParamsInfo implements Parcelable {
    private String name;
    private File avatar;
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
    private Date graduateTime;
    private Date admissionTime;
    private String targetDegreeId;
    private String targetCountryId;
    private String targetMajor;
    private String targetRangeId;
    private int scoreToefl;
    private int scoreIelts;
    private int scoreSat;
    private int scoreGre;
    private int scoreGmat;
    private String selfIntroduction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
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

    public int getScoreToefl() {
        return scoreToefl;
    }

    public void setScoreToefl(int scoreToefl) {
        this.scoreToefl = scoreToefl;
    }

    public int getScoreIelts() {
        return scoreIelts;
    }

    public void setScoreIelts(int scoreIelts) {
        this.scoreIelts = scoreIelts;
    }

    public int getScoreSat() {
        return scoreSat;
    }

    public void setScoreSat(int scoreSat) {
        this.scoreSat = scoreSat;
    }

    public int getScoreGre() {
        return scoreGre;
    }

    public void setScoreGre(int scoreGre) {
        this.scoreGre = scoreGre;
    }

    public int getScoreGmat() {
        return scoreGmat;
    }

    public void setScoreGmat(int scoreGmat) {
        this.scoreGmat = scoreGmat;
    }

    public String getSelfIntroduction() {
        return selfIntroduction;
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

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    @Override
    public String toString() {
        return "MyParamsInfo{" +
                "name='" + name + '\'' +
                ", avatar=" + avatar +
                ", genderId='" + genderId + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", weixin='" + weixin + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", address='" + address + '\'' +
                ", currentSchool='" + currentSchool + '\'' +
                ", currentGradeId='" + currentGradeId + '\'' +
                ", currentMajor='" + currentMajor + '\'' +
                ", currentRankId='" + currentRankId + '\'' +
                ", graduateTime=" + graduateTime +
                ", admissionTime=" + admissionTime +
                ", targetDegreeId='" + targetDegreeId + '\'' +
                ", targetCountryId='" + targetCountryId + '\'' +
                ", targetMajor='" + targetMajor + '\'' +
                ", targetRangeId='" + targetRangeId + '\'' +
                ", scoreToefl=" + scoreToefl +
                ", scoreIelts=" + scoreIelts +
                ", scoreSat=" + scoreSat +
                ", scoreGre=" + scoreGre +
                ", scoreGmat=" + scoreGmat +
                ", selfIntroduction='" + selfIntroduction + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeSerializable(this.avatar);
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
        dest.writeLong(graduateTime != null ? graduateTime.getTime() : -1);
        dest.writeLong(admissionTime != null ? admissionTime.getTime() : -1);
        dest.writeString(this.targetDegreeId);
        dest.writeString(this.targetCountryId);
        dest.writeString(this.targetMajor);
        dest.writeString(this.targetRangeId);
        dest.writeInt(this.scoreToefl);
        dest.writeInt(this.scoreIelts);
        dest.writeInt(this.scoreSat);
        dest.writeInt(this.scoreGre);
        dest.writeInt(this.scoreGmat);
        dest.writeString(this.selfIntroduction);
    }

    public MyParamsInfo() {
    }

    protected MyParamsInfo(Parcel in) {
        this.name = in.readString();
        this.avatar = (File) in.readSerializable();
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
        long tmpGraduateTime = in.readLong();
        this.graduateTime = tmpGraduateTime == -1 ? null : new Date(tmpGraduateTime);
        long tmpAdmissionTime = in.readLong();
        this.admissionTime = tmpAdmissionTime == -1 ? null : new Date(tmpAdmissionTime);
        this.targetDegreeId = in.readString();
        this.targetCountryId = in.readString();
        this.targetMajor = in.readString();
        this.targetRangeId = in.readString();
        this.scoreToefl = in.readInt();
        this.scoreIelts = in.readInt();
        this.scoreSat = in.readInt();
        this.scoreGre = in.readInt();
        this.scoreGmat = in.readInt();
        this.selfIntroduction = in.readString();
    }

    public static final Creator<MyParamsInfo> CREATOR = new Creator<MyParamsInfo>() {
        public MyParamsInfo createFromParcel(Parcel source) {
            return new MyParamsInfo(source);
        }

        public MyParamsInfo[] newArray(int size) {
            return new MyParamsInfo[size];
        }
    };
}

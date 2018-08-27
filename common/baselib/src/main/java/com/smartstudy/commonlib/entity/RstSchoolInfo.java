package com.smartstudy.commonlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louis on 17/4/21.
 */

public class RstSchoolInfo implements Parcelable {

    /**
     * chineseName : 加州大学圣地亚哥分校
     * coverPicture : https://bkd-media.smartstudy.com/school/photo/9e/41/85a10a926a3960af72564abda10e.JPG
     * englishName : University of California, San Diego
     * id : 961
     * localRank : 15
     * logo : https://bkd-media.smartstudy.com/school/logo/5e/4c/5e4c4a333609f1273bf26b5a460e9ed6.jpg
     * scoreIelts : 7
     * scoreToefl : 83
     * worldRank : 15
     */

    private String chineseName;
    private String coverPicture;
    private String englishName;
    private String id;
    private String localRank;
    private String logo;
    private String scoreIelts;
    private String scoreToefl;
    private String worldRank;

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalRank() {
        return localRank;
    }

    public void setLocalRank(String localRank) {
        this.localRank = localRank;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getScoreIelts() {
        return scoreIelts;
    }

    public void setScoreIelts(String scoreIelts) {
        this.scoreIelts = scoreIelts;
    }

    public String getScoreToefl() {
        return scoreToefl;
    }

    public void setScoreToefl(String scoreToefl) {
        this.scoreToefl = scoreToefl;
    }

    public String getWorldRank() {
        return worldRank;
    }

    public void setWorldRank(String worldRank) {
        this.worldRank = worldRank;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.chineseName);
        dest.writeString(this.coverPicture);
        dest.writeString(this.englishName);
        dest.writeString(this.id);
        dest.writeString(this.localRank);
        dest.writeString(this.logo);
        dest.writeString(this.scoreIelts);
        dest.writeString(this.scoreToefl);
        dest.writeString(this.worldRank);
    }

    public RstSchoolInfo() {
    }

    protected RstSchoolInfo(Parcel in) {
        this.chineseName = in.readString();
        this.coverPicture = in.readString();
        this.englishName = in.readString();
        this.id = in.readString();
        this.localRank = in.readString();
        this.logo = in.readString();
        this.scoreIelts = in.readString();
        this.scoreToefl = in.readString();
        this.worldRank = in.readString();
    }

    public static final Parcelable.Creator<RstSchoolInfo> CREATOR = new Parcelable.Creator<RstSchoolInfo>() {
        public RstSchoolInfo createFromParcel(Parcel source) {
            return new RstSchoolInfo(source);
        }

        public RstSchoolInfo[] newArray(int size) {
            return new RstSchoolInfo[size];
        }
    };
}

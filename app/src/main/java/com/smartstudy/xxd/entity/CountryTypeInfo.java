package com.smartstudy.xxd.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by louis on 17/5/18.
 */

public class CountryTypeInfo implements Parcelable {


    /**
     * tagId : [23758]
     * countryId : COUNTRY_226
     * schoolType : 美国大学
     * countryName : 美国
     */

    private String countryId;
    private String countryNumId;
    private String schoolType;
    private String countryName;
    private String visaName;
    private List<String> tagId;
    private boolean isSelected;

    public String getCountryNumId() {
        return countryNumId;
    }

    public void setCountryNumId(String countryNumId) {
        this.countryNumId = countryNumId;
    }

    public String getVisaName() {
        return visaName;
    }

    public void setVisaName(String visaName) {
        this.visaName = visaName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(String schoolType) {
        this.schoolType = schoolType;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<String> getTagId() {
        return tagId;
    }

    public void setTagId(List<String> tagId) {
        this.tagId = tagId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.countryId);
        dest.writeString(this.countryNumId);
        dest.writeString(this.schoolType);
        dest.writeString(this.countryName);
        dest.writeString(this.visaName);
        dest.writeStringList(this.tagId);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
    }

    public CountryTypeInfo() {
    }

    protected CountryTypeInfo(Parcel in) {
        this.countryId = in.readString();
        this.countryNumId = in.readString();
        this.schoolType = in.readString();
        this.countryName = in.readString();
        this.visaName = in.readString();
        this.tagId = in.createStringArrayList();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<CountryTypeInfo> CREATOR = new Creator<CountryTypeInfo>() {
        @Override
        public CountryTypeInfo createFromParcel(Parcel source) {
            return new CountryTypeInfo(source);
        }

        @Override
        public CountryTypeInfo[] newArray(int size) {
            return new CountryTypeInfo[size];
        }
    };
}

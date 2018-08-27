package com.smartstudy.commonlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.smartstudy.commonlib.R;
import com.smartstudy.commonlib.ui.customView.decoration.ISuspensionInterface;
import com.smartstudy.commonlib.utils.ParameterUtils;

/**
 * Created by louis on 17/4/21.
 */

public class SmartSchoolRstInfo implements Parcelable, ISuspensionInterface {

    /**
     * avgIelts : 7.375
     * avgSat : 1530
     * avgToefl : 104.583333333333
     * cdf : 0.6846822847981191
     * cdfSat : 0.6846822847981191
     * count : 14
     * prob : 0.5477458278384953
     * school :
     * schoolId : 1356
     * stddevIelts : 0.25
     * stddevToefl : 6.62582184725149
     */

    private String avgIelts;
    private String avgSat;
    private String avgToefl;
    private String cdf;
    private String cdfSat;
    private String count;
    private float prob;
    private boolean selected;
    private RstSchoolInfo school;
    private String schoolId;
    private String stddevIelts;
    private String stddevToefl;
    private String match_type;
    private boolean isTop; //是否是最上面的
    private int drawableId;

    public String getAvgIelts() {
        return avgIelts;
    }

    public void setAvgIelts(String avgIelts) {
        this.avgIelts = avgIelts;
    }

    public String getAvgSat() {
        return avgSat;
    }

    public void setAvgSat(String avgSat) {
        this.avgSat = avgSat;
    }

    public String getAvgToefl() {
        return avgToefl;
    }

    public void setAvgToefl(String avgToefl) {
        this.avgToefl = avgToefl;
    }

    public String getCdf() {
        return cdf;
    }

    public void setCdf(String cdf) {
        this.cdf = cdf;
    }

    public String getCdfSat() {
        return cdfSat;
    }

    public void setCdfSat(String cdfSat) {
        this.cdfSat = cdfSat;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public float getProb() {
        return prob;
    }

    public void setProb(float prob) {
        this.prob = prob;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public RstSchoolInfo getSchool() {
        return school;
    }

    public void setSchool(RstSchoolInfo school) {
        this.school = school;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getStddevIelts() {
        return stddevIelts;
    }

    public void setStddevIelts(String stddevIelts) {
        this.stddevIelts = stddevIelts;
    }

    public String getStddevToefl() {
        return stddevToefl;
    }

    public void setStddevToefl(String stddevToefl) {
        this.stddevToefl = stddevToefl;
    }

    public String getMatch_type() {
        return match_type;
    }

    public void setMatch_type(String match_type) {
        this.match_type = match_type;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }

    @Override
    public String getSuspensionTag() {
        return match_type;
    }

    @Override
    public int drawableId() {
        if (ParameterUtils.MATCH_TOP.equals(match_type)) {
            drawableId = R.drawable.ic_top;
        } else if (ParameterUtils.MATCH_MID.equals(match_type)) {
            drawableId = R.drawable.ic_mid;
        } else if (ParameterUtils.MATCH_BOT.equals(match_type)) {
            drawableId = R.drawable.ic_bot;
        }
        return drawableId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.avgIelts);
        dest.writeString(this.avgSat);
        dest.writeString(this.avgToefl);
        dest.writeString(this.cdf);
        dest.writeString(this.cdfSat);
        dest.writeString(this.count);
        dest.writeFloat(this.prob);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.school, 0);
        dest.writeString(this.schoolId);
        dest.writeString(this.stddevIelts);
        dest.writeString(this.stddevToefl);
        dest.writeString(this.match_type);
        dest.writeByte(isTop ? (byte) 1 : (byte) 0);
    }

    public SmartSchoolRstInfo() {
    }

    protected SmartSchoolRstInfo(Parcel in) {
        this.avgIelts = in.readString();
        this.avgSat = in.readString();
        this.avgToefl = in.readString();
        this.cdf = in.readString();
        this.cdfSat = in.readString();
        this.count = in.readString();
        this.prob = in.readFloat();
        this.selected = in.readByte() != 0;
        this.school = in.readParcelable(RstSchoolInfo.class.getClassLoader());
        this.schoolId = in.readString();
        this.stddevIelts = in.readString();
        this.stddevToefl = in.readString();
        this.match_type = in.readString();
        this.isTop = in.readByte() != 0;
    }

    public static final Creator<SmartSchoolRstInfo> CREATOR = new Creator<SmartSchoolRstInfo>() {
        public SmartSchoolRstInfo createFromParcel(Parcel source) {
            return new SmartSchoolRstInfo(source);
        }

        public SmartSchoolRstInfo[] newArray(int size) {
            return new SmartSchoolRstInfo[size];
        }
    };
}

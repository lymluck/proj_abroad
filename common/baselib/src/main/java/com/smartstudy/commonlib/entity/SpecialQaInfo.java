package com.smartstudy.commonlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class SpecialQaInfo implements Parcelable {
    private String id;
    private String type;
    private String text;
    private double score;
    private boolean is_selected;
    private int selectedId;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean is_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.text);
        dest.writeDouble(this.score);
        dest.writeByte(is_selected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.selectedId);
    }

    public SpecialQaInfo() {
    }

    protected SpecialQaInfo(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.text = in.readString();
        this.score = in.readDouble();
        this.is_selected = in.readByte() != 0;
        this.selectedId = in.readInt();
    }

    public static final Creator<SpecialQaInfo> CREATOR = new Creator<SpecialQaInfo>() {
        public SpecialQaInfo createFromParcel(Parcel source) {
            return new SpecialQaInfo(source);
        }

        public SpecialQaInfo[] newArray(int size) {
            return new SpecialQaInfo[size];
        }
    };

    @Override
    public String toString() {
        return "SpecialQaInfo{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", score=" + score +
                ", is_selected=" + is_selected +
                ", selectedId=" + selectedId +
                '}';
    }
}

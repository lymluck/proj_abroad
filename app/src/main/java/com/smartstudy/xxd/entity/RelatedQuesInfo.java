package com.smartstudy.xxd.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by louis on 17/4/11.
 */

public class RelatedQuesInfo implements Parcelable {
    private int id;
    private String question;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "RelatedQuesInfo{" +
                "id=" + id +
                ", question='" + question + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.question);
    }

    public RelatedQuesInfo() {
    }

    protected RelatedQuesInfo(Parcel in) {
        this.id = in.readInt();
        this.question = in.readString();
    }

    public static final Parcelable.Creator<RelatedQuesInfo> CREATOR = new Parcelable.Creator<RelatedQuesInfo>() {
        @Override
        public RelatedQuesInfo createFromParcel(Parcel source) {
            return new RelatedQuesInfo(source);
        }

        @Override
        public RelatedQuesInfo[] newArray(int size) {
            return new RelatedQuesInfo[size];
        }
    };
}

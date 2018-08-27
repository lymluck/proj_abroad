package com.smartstudy.commonlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class IdNameInfo implements Parcelable {
    private String id;
    private String name;
    private String value;
    private String group;
    private String prefix;
    private String hint;
    private boolean is_selected;
    private boolean isTop;

    public boolean is_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    @Override
    public String toString() {

        return "{" +
                "\"id\":" + "\"" + id + "\"" +
                ", \"name\":" + "\"" + name + "\"" +
                ",\"value\":" + "\"" + value + "\"" +
                ", \"is_selected\":" + is_selected +
                "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.value);
        dest.writeString(this.group);
        dest.writeString(this.prefix);
        dest.writeString(this.hint);
        dest.writeByte(is_selected ? (byte) 1 : (byte) 0);
        dest.writeByte(isTop ? (byte) 1 : (byte) 0);
    }

    public IdNameInfo() {
    }

    protected IdNameInfo(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.value = in.readString();
        this.group = in.readString();
        this.prefix = in.readString();
        this.hint = in.readString();
        this.is_selected = in.readByte() != 0;
        this.isTop = in.readByte() != 0;
    }

    public static final Creator<IdNameInfo> CREATOR = new Creator<IdNameInfo>() {
        public IdNameInfo createFromParcel(Parcel source) {
            return new IdNameInfo(source);
        }

        public IdNameInfo[] newArray(int size) {
            return new IdNameInfo[size];
        }
    };
}

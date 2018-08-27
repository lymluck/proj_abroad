package com.smartstudy.xxd.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author louis
 * @date on 2018/6/28
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class ActivityInfo implements Parcelable {

    /**
     * id : 90
     * name : 北极斯瓦尔巴群岛科考项目
     * major : 理科
     * quality : 3星
     * review : 1.性价比低，不做特别推荐；
     * 2.内容专业度不错；
     * 3.喜欢、有钱，学术方向匹配的小孩可以考虑。
     * host : 知世
     * price : 118000元
     * place : 北极.斯瓦尔巴群岛
     * registrationDeadline : null
     * activityTime : 7月27日-8月7日
     * participantsLimit : 12
     * introduction : 学生可拜访先进的北极科考基地、探索斯匹次卑尔根最精华的西北部地区、参访极地圈里的“哈佛”——斯瓦尔巴大学、由专业探险队员进行课程讲解及分享、寻找自然栖息地的北极熊和其他野生动物，全程定时观测实验、记录及采样，进行生态气候学、海洋生态及环境科学等多学科交叉学习。
     * mentors : null
     * expectedOutcome : 知名气候变化专家推荐信+专业科研论文撰写与发表（有机会在国家级期刊发表）
     * note : null
     * createTime : 2018-06-28T08:17:00.097Z
     * editorIds : [1525]
     * deleted : false
     * tel:
     */

    private int id;
    private String name;
    private String major;
    private String quality;
    private String review;
    private String host;
    private String price;
    private String place;
    private String registrationDeadline;
    private String activityTime;
    private String participantsLimit;
    private String introduction;
    private String mentors;
    private String expectedOutcome;
    private String note;
    private String createTime;
    private boolean deleted;
    private String tel;
    private List<Integer> editorIds;

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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(String registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(String activityTime) {
        this.activityTime = activityTime;
    }

    public String getParticipantsLimit() {
        return participantsLimit;
    }

    public void setParticipantsLimit(String participantsLimit) {
        this.participantsLimit = participantsLimit;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMentors() {
        return mentors;
    }

    public void setMentors(String mentors) {
        this.mentors = mentors;
    }

    public String getExpectedOutcome() {
        return expectedOutcome;
    }

    public void setExpectedOutcome(String expectedOutcome) {
        this.expectedOutcome = expectedOutcome;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<Integer> getEditorIds() {
        return editorIds;
    }

    public void setEditorIds(List<Integer> editorIds) {
        this.editorIds = editorIds;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.major);
        dest.writeString(this.quality);
        dest.writeString(this.review);
        dest.writeString(this.host);
        dest.writeString(this.price);
        dest.writeString(this.place);
        dest.writeString(this.registrationDeadline);
        dest.writeString(this.activityTime);
        dest.writeString(this.participantsLimit);
        dest.writeString(this.introduction);
        dest.writeString(this.mentors);
        dest.writeString(this.expectedOutcome);
        dest.writeString(this.note);
        dest.writeString(this.createTime);
        dest.writeByte(deleted ? (byte) 1 : (byte) 0);
        dest.writeList(this.editorIds);
    }

    public ActivityInfo() {
    }

    protected ActivityInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.major = in.readString();
        this.quality = in.readString();
        this.review = in.readString();
        this.host = in.readString();
        this.price = in.readString();
        this.place = in.readString();
        this.registrationDeadline = in.readString();
        this.activityTime = in.readString();
        this.participantsLimit = in.readString();
        this.introduction = in.readString();
        this.mentors = in.readString();
        this.expectedOutcome = in.readString();
        this.note = in.readString();
        this.createTime = in.readString();
        this.deleted = in.readByte() != 0;
        this.editorIds = new ArrayList<Integer>();
        in.readList(this.editorIds, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<ActivityInfo> CREATOR = new Parcelable.Creator<ActivityInfo>() {
        @Override
        public ActivityInfo createFromParcel(Parcel source) {
            return new ActivityInfo(source);
        }

        @Override
        public ActivityInfo[] newArray(int size) {
            return new ActivityInfo[size];
        }
    };

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
            .append(id);
        sb.append(",\"name\":\"")
            .append(name).append('\"');
        sb.append(",\"major\":\"")
            .append(major).append('\"');
        sb.append(",\"quality\":\"")
            .append(quality).append('\"');
        sb.append(",\"review\":\"")
            .append(review).append('\"');
        sb.append(",\"host\":\"")
            .append(host).append('\"');
        sb.append(",\"price\":\"")
            .append(price).append('\"');
        sb.append(",\"place\":\"")
            .append(place).append('\"');
        sb.append(",\"registrationDeadline\":\"")
            .append(registrationDeadline).append('\"');
        sb.append(",\"activityTime\":\"")
            .append(activityTime).append('\"');
        sb.append(",\"participantsLimit\":\"")
            .append(participantsLimit).append('\"');
        sb.append(",\"introduction\":\"")
            .append(introduction).append('\"');
        sb.append(",\"mentors\":\"")
            .append(mentors).append('\"');
        sb.append(",\"expectedOutcome\":\"")
            .append(expectedOutcome).append('\"');
        sb.append(",\"note\":\"")
            .append(note).append('\"');
        sb.append(",\"createTime\":\"")
            .append(createTime).append('\"');
        sb.append(",\"deleted\":")
            .append(deleted);
        sb.append(",\"tel\":\"")
            .append(tel).append('\"');
        sb.append(",\"editorIds\":")
            .append(editorIds);
        sb.append('}');
        return sb.toString();
    }
}

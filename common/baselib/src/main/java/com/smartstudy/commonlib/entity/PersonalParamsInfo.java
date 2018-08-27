package com.smartstudy.commonlib.entity;

import java.io.File;

/**
 * Created by louis on 17/8/1.
 */

public class PersonalParamsInfo {
    private String name;
    private File avatar;
    private boolean avatarAlreadyCut;
    private String genderId;
    private String contact;
    private String email;
    private String customerRoleId;
    private String admissionTime;
    private String targetCountry;
    private String targetDegree;
    private String targetSchoolRank;
    private String targetMajorDirection;
    private String currentSchool;
    private String score;
    private String scoreLanguage;
    private String scoreStandard;
    private String activityInternship;
    private String activityResearch;
    private String activityCommunity;
    private String activitySocial;
    private String activityExchange;

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

    public boolean isAvatarAlreadyCut() {
        return avatarAlreadyCut;
    }

    public void setAvatarAlreadyCut(boolean avatarAlreadyCut) {
        this.avatarAlreadyCut = avatarAlreadyCut;
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

    public String getCustomerRoleId() {
        return customerRoleId;
    }

    public void setCustomerRoleId(String customerRoleId) {
        this.customerRoleId = customerRoleId;
    }

    public String getAdmissionTime() {
        return admissionTime;
    }

    public void setAdmissionTime(String admissionTime) {
        this.admissionTime = admissionTime;
    }

    public String getTargetCountry() {
        return targetCountry;
    }

    public void setTargetCountry(String targetCountry) {
        this.targetCountry = targetCountry;
    }

    public String getTargetDegree() {
        return targetDegree;
    }

    public void setTargetDegree(String targetDegree) {
        this.targetDegree = targetDegree;
    }

    public String getTargetSchoolRank() {
        return targetSchoolRank;
    }

    public void setTargetSchoolRank(String targetSchoolRank) {
        this.targetSchoolRank = targetSchoolRank;
    }

    public String getTargetMajorDirection() {
        return targetMajorDirection;
    }

    public void setTargetMajorDirection(String targetMajorDirection) {
        this.targetMajorDirection = targetMajorDirection;
    }

    public String getCurrentSchool() {
        return currentSchool;
    }

    public void setCurrentSchool(String currentSchool) {
        this.currentSchool = currentSchool;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getScoreLanguage() {
        return scoreLanguage;
    }

    public void setScoreLanguage(String scoreLanguage) {
        this.scoreLanguage = scoreLanguage;
    }

    public String getScoreStandard() {
        return scoreStandard;
    }

    public void setScoreStandard(String scoreStandard) {
        this.scoreStandard = scoreStandard;
    }

    public String getActivityInternship() {
        return activityInternship;
    }

    public void setActivityInternship(String activityInternship) {
        this.activityInternship = activityInternship;
    }

    public String getActivityResearch() {
        return activityResearch;
    }

    public void setActivityResearch(String activityResearch) {
        this.activityResearch = activityResearch;
    }

    public String getActivityCommunity() {
        return activityCommunity;
    }

    public void setActivityCommunity(String activityCommunity) {
        this.activityCommunity = activityCommunity;
    }

    public String getActivitySocial() {
        return activitySocial;
    }

    public void setActivitySocial(String activitySocial) {
        this.activitySocial = activitySocial;
    }

    public String getActivityExchange() {
        return activityExchange;
    }

    public void setActivityExchange(String activityExchange) {
        this.activityExchange = activityExchange;
    }

    @Override
    public String toString() {
        return "PersonalParamsInfo{" +
                "name='" + name + '\'' +
                ", avatar=" + avatar +
                ", avatarAlreadyCut=" + avatarAlreadyCut +
                ", genderId='" + genderId + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", customerRoleId='" + customerRoleId + '\'' +
                ", admissionTime='" + admissionTime + '\'' +
                ", targetCountry='" + targetCountry + '\'' +
                ", targetDegree='" + targetDegree + '\'' +
                ", targetSchoolRank='" + targetSchoolRank + '\'' +
                ", targetMajorDirection='" + targetMajorDirection + '\'' +
                ", currentSchool='" + currentSchool + '\'' +
                ", score='" + score + '\'' +
                ", scoreLanguage='" + scoreLanguage + '\'' +
                ", scoreStandard='" + scoreStandard + '\'' +
                ", activityInternship='" + activityInternship + '\'' +
                ", activityResearch='" + activityResearch + '\'' +
                ", activityCommunity='" + activityCommunity + '\'' +
                ", activitySocial='" + activitySocial + '\'' +
                ", activityExchange='" + activityExchange + '\'' +
                '}';
    }
}

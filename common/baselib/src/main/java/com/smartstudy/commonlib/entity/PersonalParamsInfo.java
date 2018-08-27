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
    private String budget;

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

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"name\":\"")
            .append(name).append('\"');
        sb.append(",\"avatar\":")
            .append(avatar);
        sb.append(",\"avatarAlreadyCut\":")
            .append(avatarAlreadyCut);
        sb.append(",\"genderId\":\"")
            .append(genderId).append('\"');
        sb.append(",\"contact\":\"")
            .append(contact).append('\"');
        sb.append(",\"email\":\"")
            .append(email).append('\"');
        sb.append(",\"customerRoleId\":\"")
            .append(customerRoleId).append('\"');
        sb.append(",\"admissionTime\":\"")
            .append(admissionTime).append('\"');
        sb.append(",\"targetCountry\":\"")
            .append(targetCountry).append('\"');
        sb.append(",\"targetDegree\":\"")
            .append(targetDegree).append('\"');
        sb.append(",\"targetSchoolRank\":\"")
            .append(targetSchoolRank).append('\"');
        sb.append(",\"targetMajorDirection\":\"")
            .append(targetMajorDirection).append('\"');
        sb.append(",\"currentSchool\":\"")
            .append(currentSchool).append('\"');
        sb.append(",\"score\":\"")
            .append(score).append('\"');
        sb.append(",\"scoreLanguage\":\"")
            .append(scoreLanguage).append('\"');
        sb.append(",\"scoreStandard\":\"")
            .append(scoreStandard).append('\"');
        sb.append(",\"activityInternship\":\"")
            .append(activityInternship).append('\"');
        sb.append(",\"activityResearch\":\"")
            .append(activityResearch).append('\"');
        sb.append(",\"activityCommunity\":\"")
            .append(activityCommunity).append('\"');
        sb.append(",\"activitySocial\":\"")
            .append(activitySocial).append('\"');
        sb.append(",\"activityExchange\":\"")
            .append(activityExchange).append('\"');
        sb.append(",\"budget\":\"")
            .append(budget).append('\"');
        sb.append('}');
        return sb.toString();
    }
}

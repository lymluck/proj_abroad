package com.smartstudy.commonlib.entity;

/**
 * Created by louis on 17/4/10.
 */

public class QuestionInfo {
    private int id;
    private int userId;
    private String content;
    private boolean isClassic;
    private int answerCount;
    private String visitCount;
    private String createTime;
    private boolean answered;
    private String likedCount;
    private String collectedCount;
    private int subQuestionCount;
    private Asker asker;
    private String createTimeText;
    private FirstAnswerer firstAnswerer;
    private String schoolName;
    private String schoolLogo;
    private boolean hasUnreadAnswers;
    private String targetCountryName;
    private String targetDegreeName;

    public boolean isHasUnreadAnswers() {
        return hasUnreadAnswers;
    }

    public void setHasUnreadAnswers(boolean hasUnreadAnswers) {
        this.hasUnreadAnswers = hasUnreadAnswers;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolLogo() {
        return schoolLogo;
    }

    public void setSchoolLogo(String schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    public FirstAnswerer getFirstAnswerer() {
        return firstAnswerer;
    }

    public void setFirstAnswerer(FirstAnswerer firstAnswerer) {
        this.firstAnswerer = firstAnswerer;
    }

    public String getCreateTimeText() {
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }

    public static class Asker {
        private String id;
        private String name;
        private String avatar;

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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

    }

    public static class FirstAnswerer {
        private String id;
        private String name;
        private String imUserId;
        private String avatar;
        private String title;
        private String yearsOfWorking;
        private String staus;
        private String organizationSmallLogo;

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

        public String getImUserId() {
            return imUserId;
        }

        public void setImUserId(String imUserId) {
            this.imUserId = imUserId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getYearsOfWorking() {
            return yearsOfWorking;
        }

        public void setYearsOfWorking(String yearsOfWorking) {
            this.yearsOfWorking = yearsOfWorking;
        }

        public String getStaus() {
            return staus;
        }

        public void setStaus(String staus) {
            this.staus = staus;
        }

        public String getOrganizationSmallLogo() {
            return organizationSmallLogo;
        }

        public void setOrganizationSmallLogo(String organizationSmallLogo) {
            this.organizationSmallLogo = organizationSmallLogo;
        }
    }

    public Asker getAsker() {
        return asker;
    }

    public void setAsker(Asker asker) {
        this.asker = asker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isClassic() {
        return isClassic;
    }

    public void setClassic(boolean classic) {
        isClassic = classic;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public String getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(String likedCount) {
        this.likedCount = likedCount;
    }

    public String getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(String collectedCount) {
        this.collectedCount = collectedCount;
    }

    public int getSubQuestionCount() {
        return subQuestionCount;
    }

    public void setSubQuestionCount(int subQuestionCount) {
        this.subQuestionCount = subQuestionCount;
    }

    public String getTargetCountryName() {
        return targetCountryName;
    }

    public void setTargetCountryName(String targetCountryName) {
        this.targetCountryName = targetCountryName;
    }

    public String getTargetDegreeName() {
        return targetDegreeName;
    }

    public void setTargetDegreeName(String targetDegreeName) {
        this.targetDegreeName = targetDegreeName;
    }
}

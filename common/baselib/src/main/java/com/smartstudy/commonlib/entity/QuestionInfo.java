package com.smartstudy.commonlib.entity;

/**
 * Created by louis on 17/4/10.
 */

public class QuestionInfo {

    /**
     * id : 1235
     * score :
     * question : 【墨尔本大学】当前为国内双非大学二本大一在读 刚开始准备雅思成绩 申请墨尔本本科
     * answer :
     * askTime : 2017/02/16 14:35:50
     * answerTime :
     * ip : 106.226.31.122
     * isClassic :
     * assignTime :
     * tags :
     * rate :
     * askedTeacherIds :
     * openCount : 0
     * parentId :
     * schoolId : 24
     * asker :
     * answerer :
     * assignee :
     * visitCount :
     * country :
     * grade :
     * answered : false
     * tagsData :
     */

    private int id;
    private String score;
    private String question;
    private String answer;
    private String askTime;
    private String answerTime;
    private String ip;
    private String isClassic;
    private String assignTime;
    private String tags;
    private String rate;
    private String askedTeacherIds;
    private String openCount;
    private String parentId;
    private int schoolId;
    private Asker asker;
    private Answerer answerer;
    private String assignee;
    private String country;
    private String grade;
    private boolean answered;
    private String tagsData;
    private String visitCount;

    public static class Asker {
        private String id;
        private String name;
        private String avatar;
        private String avatarPlaceholder;

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

        public String getAvatarPlaceholder() {
            return avatarPlaceholder;
        }

        public void setAvatarPlaceholder(String avatarPlaceholder) {
            this.avatarPlaceholder = avatarPlaceholder;
        }
    }

    public static class Answerer {
        private String id;
        private String name;
        private String avatarUrl;
        private String title;

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

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public Asker getAsker() {
        return asker;
    }

    public void setAsker(Asker asker) {
        this.asker = asker;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setAskTime(String askTime) {
        this.askTime = askTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setIsClassic(String isClassic) {
        this.isClassic = isClassic;
    }

    public void setAssignTime(String assignTime) {
        this.assignTime = assignTime;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setAskedTeacherIds(String askedTeacherIds) {
        this.askedTeacherIds = askedTeacherIds;
    }

    public void setOpenCount(String openCount) {
        this.openCount = openCount;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }


    public void setAnswerer(Answerer answerer) {
        this.answerer = answerer;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public void setTagsData(String tagsData) {
        this.tagsData = tagsData;
    }

    public int getId() {
        return id;
    }

    public String getScore() {
        return score;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getAskTime() {
        return askTime;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public String getIp() {
        return ip;
    }

    public String getIsClassic() {
        return isClassic;
    }

    public String getAssignTime() {
        return assignTime;
    }

    public String getTags() {
        return tags;
    }

    public String getRate() {
        return rate;
    }

    public String getAskedTeacherIds() {
        return askedTeacherIds;
    }

    public String getOpenCount() {
        return openCount;
    }

    public String getParentId() {
        return parentId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public Answerer getAnswerer() {
        return answerer;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getCountry() {
        return country;
    }

    public String getGrade() {
        return grade;
    }

    public boolean getAnswered() {
        return answered;
    }

    public String getTagsData() {
        return tagsData;
    }

    public String getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(String visitCount) {
        this.visitCount = visitCount;
    }
}

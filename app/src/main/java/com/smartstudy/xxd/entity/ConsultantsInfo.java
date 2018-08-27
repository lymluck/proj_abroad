package com.smartstudy.xxd.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/3/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class ConsultantsInfo implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private String id;
    private String name;
    private String avatar;
    private String title;
    private String school;
    private boolean schoolCertified;
    private String yearsOfWorking;
    private String location;
    private String imUserId;
    private int status;
    private String video;
    private String likesCount;
    private Organization organization;
    private AnsweredQuestions answeredQuestions;

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public AnsweredQuestions getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(AnsweredQuestions answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public boolean isSchoolCertified() {
        return schoolCertified;
    }

    public void setSchoolCertified(boolean schoolCertified) {
        this.schoolCertified = schoolCertified;
    }

    public String getYearsOfWorking() {
        return yearsOfWorking;
    }

    public void setYearsOfWorking(String yearsOfWorking) {
        this.yearsOfWorking = yearsOfWorking;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImUserId() {
        return imUserId;
    }

    public void setImUserId(String imUserId) {
        this.imUserId = imUserId;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public class Organization implements Serializable {
        private static final long serialVersionUID = -7060210544600424481L;
        private String id;
        private String name;
        private String subtitle;
        private String logo;
        private String smallLogo;
        private String introduction;


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

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getSmallLogo() {
            return smallLogo;
        }

        public void setSmallLogo(String smallLogo) {
            this.smallLogo = smallLogo;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

    }


    public class AnsweredQuestions implements Serializable {
        private static final long serialVersionUID = -7060210544600424481L;
        private List<AnswerData> data;

        private Pagination pagination;

        public List<AnswerData> getData() {
            return data;
        }

        public void setData(List<AnswerData> data) {
            this.data = data;
        }

        public Pagination getPagination() {
            return pagination;
        }

        public void setPagination(Pagination pagination) {
            this.pagination = pagination;
        }

        public class Pagination implements Serializable {
            private String count;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }
        }

        public class AnswerData implements Serializable {
            private static final long serialVersionUID = -7060210544600424481L;
            private String id;
            private String userId;
            private String content;
            private String schoolId;
            private String isClassic;
            private String answerCount;
            private String visitCount;
            private String createTime;
            private String createTimeText;
            private String targetCountryName;
            private String targetDegreeName;
            private Asker asker;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getSchoolId() {
                return schoolId;
            }

            public void setSchoolId(String schoolId) {
                this.schoolId = schoolId;
            }

            public String getIsClassic() {
                return isClassic;
            }

            public void setIsClassic(String isClassic) {
                this.isClassic = isClassic;
            }

            public String getAnswerCount() {
                return answerCount;
            }

            public void setAnswerCount(String answerCount) {
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

            public String getCreateTimeText() {
                return createTimeText;
            }

            public void setCreateTimeText(String createTimeText) {
                this.createTimeText = createTimeText;
            }

            public Asker getAsker() {
                return asker;
            }

            public void setAsker(Asker asker) {
                this.asker = asker;
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
    }

}


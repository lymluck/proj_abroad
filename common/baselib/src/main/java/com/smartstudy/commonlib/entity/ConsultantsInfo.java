package com.smartstudy.commonlib.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yqy on 2017/12/21.
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
    private Organization organization;
    private AnsweredQuestions answeredQuestions;

    public AnsweredQuestions getAnsweredQuestions() {
        return answeredQuestions;
    }

    public void setAnsweredQuestions(AnsweredQuestions answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
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

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"id\":\"")
                    .append(id).append('\"');
            sb.append(",\"name\":\"")
                    .append(name).append('\"');
            sb.append(",\"subtitle\":\"")
                    .append(subtitle).append('\"');
            sb.append(",\"logo\":\"")
                    .append(logo).append('\"');
            sb.append(",\"smallLogo\":\"")
                    .append(smallLogo).append('\"');
            sb.append(",\"introduction\":\"")
                    .append(introduction).append('\"');
            sb.append('}');
            return sb.toString();
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

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"data\":\"")
                    .append(data).append('\"');
            sb.append(",\"pagination\":\"")
                    .append(pagination).append('\"');
            sb.append('}');
            return sb.toString();
        }

        public class Pagination implements Serializable {
            private String count;

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }


            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("{");
                sb.append("\"count\":\"")
                        .append(count).append('\"');
                sb.append('}');
                return sb.toString();
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


            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("{");
                sb.append("\"id\":\"")
                        .append(id).append('\"');
                sb.append("\"userId\":\"")
                        .append(userId).append('\"');
                sb.append("\"content\":\"")
                        .append(content).append('\"');
                sb.append("\"schoolId\":\"")
                        .append(schoolId).append('\"');

                sb.append("\"isClassic\":\"")
                        .append(isClassic).append('\"');
                sb.append("\"answerCount\":\"")
                        .append(answerCount).append('\"');

                sb.append("\"visitCount\":\"")
                        .append(visitCount).append('\"');
                sb.append("\"createTime\":\"")
                        .append(createTime).append('\"');

                sb.append("\"createTimeText\":\"")
                        .append(createTimeText).append('\"');
                sb.append("\"asker\":\"")
                        .append(asker).append('\"');

                sb.append('}');
                return sb.toString();
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append(",\"name\":\"")
                .append(name).append('\"');
        sb.append(",\"avatar\":\"")
                .append(avatar).append('\"');
        sb.append(",\"title\":\"")
                .append(title).append('\"');
        sb.append(",\"school\":\"")
                .append(school).append('\"');
        sb.append(",\"schoolCertified\":\"")
                .append(schoolCertified).append('\"');
        sb.append(",\"yearsOfWorking\":\"")
                .append(yearsOfWorking).append('\"');
        sb.append(",\"location\":\"")
                .append(location).append('\"');
        sb.append(",\"imUserId\":\"")
                .append(imUserId).append('\"');
        sb.append(",\"status\":\"")
                .append(status).append('\"');
        sb.append(",\"organization\":")
                .append(organization);
        sb.append(",\"answeredQuestions\":")
                .append(answeredQuestions);
        sb.append('}');
        return sb.toString();
    }

}

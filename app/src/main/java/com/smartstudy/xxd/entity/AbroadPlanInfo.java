package com.smartstudy.xxd.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 2017/12/7.
 */

public class AbroadPlanInfo {

    /**
     * gradeId : GRA_JHS_3
     * grade : 初三
     * keywords : ["留学测评和分析","全程申请规划方案","保持GPA完成学业","托福学习","活动拓展"]
     * stages : [{"name":"初三上学期","items":["根据留学测评，MBTI性格测试、简历信息表和深入沟通活动成长背景等，全面配合留学专家来帮你完成全程申请规划方案，早规划为未来争取更多机会","GPA是衡量申请人学习能力的直观标准。在美国大学评估申请者的背景条件中，其中一个指标就是申请者的GPA，它不仅可以反映一个学生在大学期间的学习成绩，从某种程度上来讲，也能体现学生的学习能力和整体素质。务必努力学习确保优秀的GPA","提早开始托福考试基础学习和准备。阅读英文原版适合的小说及国家地理等报刊杂志，英文写作和口语表达需要长期积累和练习，真正夯实英语基础和听说读写能力","在平常紧张的学习生活之余，利用业余时间多参加一些校文体和学术活动，拓展自己的兴趣专长和申请活动背景"],"isCurrentStage":false,"time":"2016/09-2016/11"},{"name":"初三上期末","items":["准备期末考试"],"isCurrentStage":false,"time":"2016/12-2016/12"},{"name":"初三寒假","items":["了解匹配自己性格的职业和专业细化方向，并且在留学专家指导下进一步细化清晰大学专业方向，了解相关申请专业的申请要求和背景","继续拓展校外支教、志愿或其他感兴趣的竞赛及文体活动，学会有针对性的参加活动","早期托福强化阶段，备战寒假后托福首考寒假后托福首考，让学生了解考试真实流程以及自己的基础水平以便后续针对学习提升"],"isCurrentStage":false,"time":"2017/01-2017/02"},{"name":"初三下学期","items":["参加3月初TOEFL首考 目标85-90+，考前多模考并且考试后总结经验和错题，以便出具后续的提升和学习规划","认真学习在校课程，确保良好的GPA和会考成绩，而且很多学生面临中考，希望多抽时间准备为进入优秀适合的高中打下基础","继续丰富拓展在校活动和兴趣爱好","不间断托福学习，进一步提升听说读写能力和考试技巧"],"isCurrentStage":false,"time":"2017/03-2017/05"},{"name":"初三下期末","items":["期末考试、中考"],"isCurrentStage":false,"time":"2017/06-2017/06"},{"name":"初三暑假","items":["根据托福首战之后的分数和各类题型掌握情况，利用暑假进行个性化集中强化辅导，如增加TPO练习的强度，进行高效的能力训练，8月底考试突破100分","探索自己的学术等兴趣丰富申请背景：申请参加相关竞赛、社会实践、暑期活动等--这些活动的整体方向尽量和之后要申请的专业方向大体一致，但是美国大学喜欢多样化的学生，所以不必要每个活动都目的性那么强","了解美国文化背景，尤其是美国历史和发展等方面知识，对未来美国大学申请和未来SAT学习都十分有帮助"],"isCurrentStage":false,"time":"2017/07-2017/08"}]
     * isCurrentGrade : false
     */

    private String gradeId;
    private String grade;
    private boolean isCurrentGrade;
    private List<String> keywords;
    private ArrayList<StagesEntity> stages;

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setIsCurrentGrade(boolean isCurrentGrade) {
        this.isCurrentGrade = isCurrentGrade;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setStages(ArrayList<StagesEntity> stages) {
        this.stages = stages;
    }

    public String getGradeId() {
        return gradeId;
    }

    public String getGrade() {
        return grade;
    }

    public boolean getIsCurrentGrade() {
        return isCurrentGrade;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public ArrayList<StagesEntity> getStages() {
        return stages;
    }

    public static class StagesEntity implements Parcelable {
        /**
         * name : 初三上学期
         * items : ["根据留学测评，MBTI性格测试、简历信息表和深入沟通活动成长背景等，全面配合留学专家来帮你完成全程申请规划方案，早规划为未来争取更多机会","GPA是衡量申请人学习能力的直观标准。在美国大学评估申请者的背景条件中，其中一个指标就是申请者的GPA，它不仅可以反映一个学生在大学期间的学习成绩，从某种程度上来讲，也能体现学生的学习能力和整体素质。务必努力学习确保优秀的GPA","提早开始托福考试基础学习和准备。阅读英文原版适合的小说及国家地理等报刊杂志，英文写作和口语表达需要长期积累和练习，真正夯实英语基础和听说读写能力","在平常紧张的学习生活之余，利用业余时间多参加一些校文体和学术活动，拓展自己的兴趣专长和申请活动背景"]
         * isCurrentStage : false
         * time : 2016/09-2016/11
         */

        private String name;
        private boolean isCurrentStage;
        private String time;
        private List<String> items;
        private int currentIndex;

        public void setName(String name) {
            this.name = name;
        }

        public void setIsCurrentStage(boolean isCurrentStage) {
            this.isCurrentStage = isCurrentStage;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }

        public String getName() {
            return name;
        }

        public boolean getIsCurrentStage() {
            return isCurrentStage;
        }

        public String getTime() {
            return time;
        }

        public List<String> getItems() {
            return items;
        }

        public int getCurrentIndex() {
            return currentIndex;
        }

        public void setCurrentIndex(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeByte(isCurrentStage ? (byte) 1 : (byte) 0);
            dest.writeString(this.time);
            dest.writeStringList(this.items);
            dest.writeInt(this.currentIndex);
        }

        public StagesEntity() {
        }

        protected StagesEntity(Parcel in) {
            this.name = in.readString();
            this.currentIndex=in.readInt();
            this.isCurrentStage = in.readByte() != 0;
            this.time = in.readString();
            this.items = in.createStringArrayList();
        }

        public static final Creator<StagesEntity> CREATOR = new Creator<StagesEntity>() {
            @Override
            public StagesEntity createFromParcel(Parcel source) {
                return new StagesEntity(source);
            }

            @Override
            public StagesEntity[] newArray(int size) {
                return new StagesEntity[size];
            }
        };
    }

}

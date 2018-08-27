package com.smartstudy.xxd.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by louis on 17/7/17.
 */

public class CourseContentInfo {

    /**
     * id : 1240
     * name : 李沛钊留学规划课之精华篇
     * productId : 2248
     * chapters : [{"id":"PAN_CID1499737238203","name":"1. 哈佛大学校友面试官-我们最喜欢这样的学生","sections":[{"id":101357,"name":"1 哈佛大学校友面试官-我们最喜欢这样的学生-李沛钊","duration":853000,"isTrial":true,"lastPlayTime":0,"maxPlayTime":0,"status":0,"progress":0}],"duration":853000,"progress":0,"status":0}]
     */

    private String id;
    private String name;
    private String productId;
    private List<ChaptersEntity> chapters;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setChapters(List<ChaptersEntity> chapters) {
        this.chapters = chapters;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProductId() {
        return productId;
    }

    public List<ChaptersEntity> getChapters() {
        return chapters;
    }

    public static class ChaptersEntity {
        /**
         * id : PAN_CID1499737238203
         * name : 1. 哈佛大学校友面试官-我们最喜欢这样的学生
         * sections : [{"id":101357,"name":"1 哈佛大学校友面试官-我们最喜欢这样的学生-李沛钊","duration":853000,"isTrial":true,"lastPlayTime":0,"maxPlayTime":0,"status":0,"progress":0}]
         * duration : 853000
         * progress : 0
         * status : 0
         */

        private String id;
        private String name;
        private String duration;
        private String progress;
        private String status;
        private List<SectionsEntity> sections;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setSections(List<SectionsEntity> sections) {
            this.sections = sections;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDuration() {
            return duration;
        }

        public String getProgress() {
            return progress;
        }

        public String getStatus() {
            return status;
        }

        public List<SectionsEntity> getSections() {
            return sections;
        }

        public static class SectionsEntity {
            /**
             * id : 101357
             * name : 1 哈佛大学校友面试官-我们最喜欢这样的学生-李沛钊
             * duration : 853000
             * isTrial : true
             * lastPlayTime : 0
             * maxPlayTime : 0
             * status : 0
             * progress : 0
             */

            private String id;
            private String name;
            private long duration;
            private boolean isTrial;
            private long lastPlayTime;
            private long maxPlayTime;
            private String status;
            private int progress;
            private SimpleDateFormat minuteFormat = new SimpleDateFormat("mm:ss");
            private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setDuration(long duration) {
                this.duration = duration;
            }

            public void setIsTrial(boolean isTrial) {
                this.isTrial = isTrial;
            }

            public void setLastPlayTime(long lastPlayTime) {
                this.lastPlayTime = lastPlayTime;
            }

            public void setMaxPlayTime(long maxPlayTime) {
                this.maxPlayTime = maxPlayTime;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setProgress(int progress) {
                this.progress = progress;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getDuration() {
                Date dt = new Date(duration);
                return duration > 60 * 60 * 1000 ? hourFormat.format(dt) : minuteFormat.format(dt);
            }

            public long getLongDuration() {
                return duration;
            }

            public boolean getIsTrial() {
                return isTrial;
            }

            public long getLastPlayTime() {
                return lastPlayTime;
            }

            public long getMaxPlayTime() {
                return maxPlayTime;
            }

            public String getStatus() {
                return status;
            }

            public int getProgress() {
                return progress;
            }
        }
    }
}

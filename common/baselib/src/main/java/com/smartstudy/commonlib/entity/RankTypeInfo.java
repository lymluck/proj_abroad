package com.smartstudy.commonlib.entity;

import java.util.List;

/**
 * Created by louis on 17/6/29.
 */

public class RankTypeInfo {


    /**
     * groupId : 6
     * groupName : 德国排名
     * groupIcon : http://upload.beikaodi.com/logo/rank_category/de.png
     * rankings : [{"id":825,"title":"德国大学排名","type":"USNEWS","year":2017}]
     */

    private String groupId;
    private String groupName;
    private String groupIcon;
    private boolean isTop;
    private List<RankingsEntity> rankings;

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public void setRankings(List<RankingsEntity> rankings) {
        this.rankings = rankings;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public List<RankingsEntity> getRankings() {
        return rankings;
    }

    public static class RankingsEntity {
        /**
         * id : 825
         * title : 德国大学排名
         * type : USNEWS
         * year : 2017
         */

        private String id;
        private String title;
        private String type;
        private String year;

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public String getYear() {
            return year;
        }
    }
}

package com.smartstudy.xxd.entity;

import java.util.List;

/**
 * @author louis
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class ColumnInfo {


    /**
     * id : 2
     * title : 1234
     * content : <p>12fasdf</p><div><br></div>
     * tagIds : [23805,23753]
     * enabled : true
     * deleted : false
     * editors : [45625,1525]
     * createTime : 2018-07-13T09:27:11.855Z
     * updateTime : 2018-07-17T07:15:55.115Z
     * publishTime : 2018-07-17T07:15:55.118Z
     * coverUrl : http://upload.beikaodi.com/upload/ufile/3b/2c/3b2c8e7be686b1d101bb3e506ddaca49695a8.png
     * visitCount : 2647
     * authorId : 45625
     * commentsCount : 0
     * author : {"id":45625,"name":"zhangxiaowei","avatar":null,"columnsCount":2,"visitCount":5344,"followCount":0}
     * tags : [{"id":23805,"name":"其他国家","data":null,"type":"country","flags":null},{"id":23753,"name":"申请offer","data":null,"type":"stage","flags":null}]
     * likedCount : 0
     * collectedCount : 0
     * liked : false
     * collected : false
     * collectState : {"columnNews":false}
     */

    private int id;
    private String title;
    private String content;
    private boolean enabled;
    private boolean deleted;
    private String createTime;
    private String updateTime;
    private String publishTime;
    private String coverUrl;
    private int visitCount;
    private int authorId;
    private int commentsCount;
    private AuthorInfo author;
    private int likedCount;
    private int collectedCount;
    private boolean liked;
    private boolean collected;
    private CollectStateInfo collectState;
    private List<Integer> tagIds;
    private List<Integer> editors;
    private List<TagsInfo> tags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public AuthorInfo getAuthor() {
        return author;
    }

    public void setAuthor(AuthorInfo author) {
        this.author = author;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }

    public int getCollectedCount() {
        return collectedCount;
    }

    public void setCollectedCount(int collectedCount) {
        this.collectedCount = collectedCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public CollectStateInfo getCollectState() {
        return collectState;
    }

    public void setCollectState(CollectStateInfo collectState) {
        this.collectState = collectState;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public List<Integer> getEditors() {
        return editors;
    }

    public void setEditors(List<Integer> editors) {
        this.editors = editors;
    }

    public List<TagsInfo> getTags() {
        return tags;
    }

    public void setTags(List<TagsInfo> tags) {
        this.tags = tags;
    }

    public static class AuthorInfo {
        /**
         * id : 45625
         * name : zhangxiaowei
         * avatar : null
         * columnsCount : 2
         * visitCount : 5344
         * followCount : 0
         */

        private int id;
        private String name;
        private String avatar;
        private int columnsCount;
        private int visitCount;
        private int followCount;

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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getColumnsCount() {
            return columnsCount;
        }

        public void setColumnsCount(int columnsCount) {
            this.columnsCount = columnsCount;
        }

        public int getVisitCount() {
            return visitCount;
        }

        public void setVisitCount(int visitCount) {
            this.visitCount = visitCount;
        }

        public int getFollowCount() {
            return followCount;
        }

        public void setFollowCount(int followCount) {
            this.followCount = followCount;
        }
    }

    public static class CollectStateInfo {
        /**
         * columnNews : false
         */

        private boolean columnNews;

        public boolean isColumnNews() {
            return columnNews;
        }

        public void setColumnNews(boolean columnNews) {
            this.columnNews = columnNews;
        }
    }

    public static class TagsInfo {
        /**
         * id : 23805
         * name : 其他国家
         * data : null
         * type : country
         * flags : null
         */

        private int id;
        private String name;
        private Object data;
        private String type;
        private Object flags;

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

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getFlags() {
            return flags;
        }

        public void setFlags(Object flags) {
            this.flags = flags;
        }
    }
}

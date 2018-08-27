package com.smartstudy.xxd.entity;

import java.util.List;

/**
 * @author louis
 * @date on 2018/7/17
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class ColumnListItemInfo {

    /**
     * id : 1
     * title : 测试一下
     * tagIds : [23769,23753]
     * enabled : true
     * deleted : false
     * editors : [{"id":1525,"name":"小星星"},{"id":45625,"name":"zhangxiaowei"}]
     * createTime : 2018/06/27 20:08:19
     * updateTime : 2018/07/17 15:16:11
     * publishTime : 2018/07/17 15:16:11
     * coverUrl : http://upload.beikaodi.com/image/upload/28/8f/288f8dec9377e7935fde2b0067c5928f48e5d.png
     * visitCount : 2697
     * authorId : 45625
     * commentsCount : 0
     * author : {"id":45625,"name":"zhangxiaowei","avatar":null}
     * preview : 相较于英、美、澳三国平均每年学费40万左右而言，新加坡显得“物美价廉”。至于“物美价廉”到什么程度呢?下面我们详细说说。fsdmf.,asdmf.a,sdmf/as,dmf/.qmwe.,fmafs/.,dmfas,dfn,dmfna,smdnf,amsndf,ansd.,fmnas.,dmfna....
     */

    private int id;
    private String title;
    private boolean enabled;
    private boolean deleted;
    private String createTime;
    private String updateTime;
    private String publishTime;
    private String coverUrl;
    private int visitCount;
    private int likesCount;
    private int authorId;
    private int commentsCount;
    private AuthorInfo author;
    private String preview;
    private List<Integer> tagIds;
    private List<EditorsInfo> editors;

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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
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

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public List<EditorsInfo> getEditors() {
        return editors;
    }

    public void setEditors(List<EditorsInfo> editors) {
        this.editors = editors;
    }

    public static class AuthorInfo {
        /**
         * id : 45625
         * name : zhangxiaowei
         * avatar : null
         */

        private int id;
        private String name;
        private String avatar;

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
    }

    public static class EditorsInfo {
        /**
         * id : 1525
         * name : 小星星
         */

        private int id;
        private String name;

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
    }
}

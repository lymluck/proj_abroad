package com.smartstudy.xxd.entity;

/**
 * @author louis
 * @date on 2018/7/19
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class ColumnCommentInfo {


    /**
     * id : 2
     * userId : 37885
     * columnId : 3
     * parentId : 1
     * content : 哪里好了😂
     * createTime : 2018-07-19T02:22:24.015Z
     * user : {"id":37885,"name":"非常","avatar":"http://upload.beikaodi.com/customer/avatar/52/ee/52ee0bbff1deb4c57490a70eed84097a4b58e.png"}
     * previousComment : {"id":1,"userId":37884,"columnId":3,"parentId":null,"content":"好！说得好！","createTime":"2018-07-19T02:20:45.079Z","user":{"id":37884,"name":"明明","avatar":"http://upload.beikaodi.com/customer/avatar/50/68/506803b6e30343ed49263997d37c5ad14f35c.png"}}
     */

    private int id;
    private int userId;
    private int columnId;
    private int parentId;
    private String createTimetext;
    private String content;
    private String createTime;
    private UserInfo user;
    private PreviousCommentInfo previousComment;

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

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public String getCreateTimetext() {
        return createTimetext;
    }

    public void setCreateTimetext(String createTimetext) {
        this.createTimetext = createTimetext;
    }

    public PreviousCommentInfo getPreviousComment() {
        return previousComment;
    }

    public void setPreviousComment(PreviousCommentInfo previousComment) {
        this.previousComment = previousComment;
    }

    public static class UserInfo {
        /**
         * id : 37885
         * name : 非常
         * avatar : http://upload.beikaodi.com/customer/avatar/52/ee/52ee0bbff1deb4c57490a70eed84097a4b58e.png
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

    public static class PreviousCommentInfo {
        /**
         * id : 1
         * userId : 37884
         * columnId : 3
         * parentId : null
         * content : 好！说得好！
         * createTime : 2018-07-19T02:20:45.079Z
         * user : {"id":37884,"name":"明明","avatar":"http://upload.beikaodi.com/customer/avatar/50/68/506803b6e30343ed49263997d37c5ad14f35c.png"}
         */

        private int id;
        private int userId;
        private int columnId;
        private String parentId;
        private String content;
        private String createTime;
        private UserInfoX user;

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

        public int getColumnId() {
            return columnId;
        }

        public void setColumnId(int columnId) {
            this.columnId = columnId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public UserInfoX getUser() {
            return user;
        }

        public void setUser(UserInfoX user) {
            this.user = user;
        }

        public static class UserInfoX {
            /**
             * id : 37884
             * name : 明明
             * avatar : http://upload.beikaodi.com/customer/avatar/50/68/506803b6e30343ed49263997d37c5ad14f35c.png
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
    }
}

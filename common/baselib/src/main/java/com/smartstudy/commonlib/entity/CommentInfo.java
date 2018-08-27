package com.smartstudy.commonlib.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by louis on 17/7/18.
 */

public class CommentInfo {

    /**
     * id : 53
     * productId : 2248
     * userId : 37885
     * rate : 3
     * comment : 冯德川家康
     * createTime : 2017-07-18T11:50:27.360Z
     * user : {"id":37885,"name":"非常棒看","avatar":"http://upload.beikaodi.com/customer/avatar/95/bb/95bb28d2b00adff3bcf137a251ef63252b910.png"}
     */

    private String id;
    private String productId;
    private String userId;
    private String rate;
    private String comment;
    private Date createTime;
    private UserEntity user;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }

    public String getCreateTime() {
        return dateFormat.format(createTime);
    }

    public UserEntity getUser() {
        return user;
    }

    public static class UserEntity {
        /**
         * id : 37885
         * name : 非常棒看
         * avatar : http://upload.beikaodi.com/customer/avatar/95/bb/95bb28d2b00adff3bcf137a251ef63252b910.png
         */

        private String id;
        private String name;
        private String avatar;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}

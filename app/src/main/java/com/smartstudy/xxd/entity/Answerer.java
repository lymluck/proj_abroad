package com.smartstudy.xxd.entity;

import android.net.Uri;
import android.text.TextUtils;

import com.smartstudy.commonlib.entity.Asker;

import java.util.List;

/**
 * Created by yqy on 2017/12/4.
 */

public class Answerer {
    private String id;

    private int commenterId;

    private String commenterRole;

    private String commentType;

    private String atCommentId;

    private String createTime;

    private String content;

    private Uri voiceUrl;

    private String questionId;

    private Commenter commenter;

    private int likedCount;

    private int collectedCount;

    private String createTimeText;

    private List<Comments> comments;

    private boolean isAnswerAgain = false;

    private String voiceDuration;

    public String getVoiceDuration() {
        if (!TextUtils.isEmpty(voiceDuration)) {
            if (voiceDuration.contains(".")) {
                String[] time = voiceDuration.split("\\.");
                if (time.length > 0) {
                    int second = Integer.parseInt(time[0]);
                    if (0 < second && second < 60) {
                        return second + "''";
                    } else if (second == 60) {
                        return 1 + "'";
                    } else if (60 < second && second < 120) {
                        return 1 + "'" + (second - 60) + "''";
                    } else if (second == 120) {
                        return 2 + "'" + "00''";
                    } else if (120 < second && second < 180) {
                        return 2 + "'" + (second - 120) + "''";
                    } else {
                        return 3 + "'" + "00''";
                    }
                }

            }
        }
        return 1 + "'00''";
    }

    public void setVoiceDuration(String voiceDuration) {
        this.voiceDuration = voiceDuration;
    }

    public String getId() {
        return id;
    }


    public boolean isAnswerAgain() {
        return isAnswerAgain;
    }

    public void setAnswerAgain(boolean answerAgain) {
        isAnswerAgain = answerAgain;
    }

    public String getCreateTimeText() {
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(int commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommenterRole() {
        return commenterRole;
    }

    public void setCommenterRole(String commenterRole) {
        this.commenterRole = commenterRole;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getAtCommentId() {
        return atCommentId;
    }

    public void setAtCommentId(String atCommentId) {
        this.atCommentId = atCommentId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Uri getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(Uri voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Commenter getCommenter() {
        return commenter;
    }

    public void setCommenter(Commenter commenter) {
        this.commenter = commenter;
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

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public static class Comments {
        private String id;
        private String commenterId;
        private String commenterRole;
        private String commentType;
        private String atCommentId;
        private String createTime;
        private String content;
        private Uri voiceUrl;
        private String questionId;
        private int likedCount;
        private int collectedCount;
        private Commenter commenter;
        private String createTimeText;
        private boolean isAnswerAgain;
        private String voiceDuration;

        public String getVoiceDuration() {
            if (!TextUtils.isEmpty(voiceDuration)) {
                if (voiceDuration.contains(".")) {
                    String[] time = voiceDuration.split("\\.");
                    if (time.length > 0) {
                        int second = Integer.parseInt(time[0]);
                        if (0 < second && second < 60) {
                            return second + "''";
                        } else if (second == 60) {
                            return 1 + "'";
                        } else if (60 < second && second < 120) {
                            return 1 + "'" + (second - 60) + "''";
                        } else if (second == 120) {
                            return 2 + "'" + "00''";
                        } else if (120 < second && second < 180) {
                            return 2 + "'" + (second - 120) + "''";
                        } else {
                            return 3 + "'" + "00''";
                        }
                    }

                }
            }
            return 1 + "'00''";
        }

        public void setVoiceDuration(String voiceDuration) {
            this.voiceDuration = voiceDuration;
        }

        public boolean isAnswerAgain() {
            return isAnswerAgain;
        }

        public void setAnswerAgain(boolean answerAgain) {
            isAnswerAgain = answerAgain;
        }

        public String getCreateTimeText() {
            return createTimeText;
        }

        public void setCreateTimeText(String createTimeText) {
            this.createTimeText = createTimeText;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCommenterId() {
            return commenterId;
        }

        public void setCommenterId(String commenterId) {
            this.commenterId = commenterId;
        }

        public String getCommenterRole() {
            return commenterRole;
        }

        public void setCommenterRole(String commenterRole) {
            this.commenterRole = commenterRole;
        }

        public String getCommentType() {
            return commentType;
        }

        public void setCommentType(String commentType) {
            this.commentType = commentType;
        }

        public String getAtCommentId() {
            return atCommentId;
        }

        public void setAtCommentId(String atCommentId) {
            this.atCommentId = atCommentId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Uri getVoiceUrl() {
            return voiceUrl;
        }

        public void setVoiceUrl(Uri voiceUrl) {
            this.voiceUrl = voiceUrl;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
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

        public Commenter getCommenter() {
            return commenter;
        }

        public void setCommenter(Commenter commenter) {
            this.commenter = commenter;
        }
    }

    public static class Commenter extends Asker {
        private String imUserId;

        public String getImUserId() {
            return imUserId;
        }

        public void setImUserId(String imUserId) {
            this.imUserId = imUserId;
        }
    }
}

package com.smartstudy.xxd.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author yqy
 * @date on 2018/6/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email yeqingyu@innobuddy.com
 */
public class OtherStudentChoiceDetailInfo implements Serializable {
    private boolean liked;
    private int likesCount;
    private List<SelectedSchools> selectedSchools;
    private String selectedSchoolsCount;
    private User user;

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public List<SelectedSchools> getSelectedSchools() {
        return selectedSchools;
    }

    public void setSelectedSchools(List<SelectedSchools> selectedSchools) {
        this.selectedSchools = selectedSchools;
    }

    public String getSelectedSchoolsCount() {
        return selectedSchoolsCount;
    }

    public void setSelectedSchoolsCount(String selectedSchoolsCount) {
        this.selectedSchoolsCount = selectedSchoolsCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class User implements Serializable {
        private String admissionYear;
        private String avatar;
        private String id;
        private String name;
        private String targetCountryId;
        private String targetCountryName;
        private String targetDegreeName;

        public String getTargetDegreeName() {
            return targetDegreeName;
        }

        public void setTargetDegreeName(String targetDegreeName) {
            this.targetDegreeName = targetDegreeName;
        }

        public String getAdmissionYear() {
            return admissionYear;
        }

        public void setAdmissionYear(String admissionYear) {
            this.admissionYear = admissionYear;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public String getTargetCountryId() {
            return targetCountryId;
        }

        public void setTargetCountryId(String targetCountryId) {
            this.targetCountryId = targetCountryId;
        }

        public String getTargetCountryName() {
            return targetCountryName;
        }

        public void setTargetCountryName(String targetCountryName) {
            this.targetCountryName = targetCountryName;
        }
    }

    public class SelectedSchools implements Serializable {
        private String matchTypeId;
        private String chineseName;
        private String id;
        private String worldRank;

        public String getMatchTypeId() {
            return matchTypeId;
        }

        public void setMatchTypeId(String matchTypeId) {
            this.matchTypeId = matchTypeId;
        }

        public String getChineseName() {
            return chineseName;
        }

        public void setChineseName(String chineseName) {
            this.chineseName = chineseName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWorldRank() {
            return worldRank;
        }

        public void setWorldRank(String worldRank) {
            this.worldRank = worldRank;
        }
    }
}

package com.smartstudy.xxd.entity;

import java.util.List;

/**
 * @author louis
 * @date on 2018/6/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class SchoolChooseInfo {


    /**
     * matchTypes : [{"name":"冲刺院校","matchTypeId":"MS_MATCH_TYPE_TOP","usersCount":64,"usersIncludeMe":false},{"name":"核心院校","matchTypeId":"MS_MATCH_TYPE_MIDDLE","usersCount":32,"usersIncludeMe":false},{"name":"保底院校","matchTypeId":"MS_MATCH_TYPE_BOTTOM","usersCount":20,"usersIncludeMe":false}]
     * years : [{"year":"2018年","count":42},{"year":"2019年","count":16},{"year":"2020年","count":11},{"year":"其他","count":46}]
     * watchers : [{"id":37897,"name":"4尺改哈爱就去P民哦了金融街热6S店蓝色的休xjjxjdjsj7","avatar":"http://upload.beikaodi.com/customer/avatar/67/02/6702ad80ccd8bcaf9caa41b647c705044bf1d.png","selectSchoolCount":118},{"id":38387,"name":"178****0111","avatar":"http://upload.beikaodi.com/user/avatar/default/9e/76/9e768cf121f1d25049cbf382cfc4908f4b13.jpg","selectSchoolCount":37},{"id":37888,"name":"，，。？！TTT","avatar":"http://upload.beikaodi.com/customer/avatar/33/f0/33f0f143f248c5e8b7dbf85816e805433070f.png","selectSchoolCount":29},{"id":17554,"name":"《&====》","avatar":"http://upload.beikaodi.com/customer/avatar/f7/7c/f77cc1710fefb8c5db7d97342cafbcc2.png","selectSchoolCount":25},{"id":37952,"name":"152****4444个","avatar":"http://upload.beikaodi.com/customer/avatar/1a/d1/1ad1dcd43f696fc5aaa949714f877084bd09.png","selectSchoolCount":19}]
     * topSchools : [{"selectedCount":22,"id":2,"logo":"http://upload.beikaodi.com/school/logo/oP/Br/4i6aKI67XRPeX13DgxtLFtubPMfy.jpg","chineseName":"麻省理工学院","englishName":"Massachusetts Institute of Technology"},{"selectedCount":20,"id":6,"logo":"http://upload.beikaodi.com/school/logo/2r/Il/Ms5ehJNl88i09L2OUwxRniVj9Em1.jpg","chineseName":"斯坦福大学","englishName":"Stanford University"},{"selectedCount":17,"id":5,"logo":"http://upload.beikaodi.com/school/logo/ty/Ac/Le2Lrzzm8wJ333wrUS7M7Cf986ao.png","chineseName":"加州大学伯克利分校","englishName":"University of California, Berkeley"},{"selectedCount":17,"id":500,"logo":"http://upload.beikaodi.com/school/logo/7d/74/7d7412ed6efb41d55fc81c899d5abde9.png","chineseName":"加州大学洛杉矶分校","englishName":"University of California, Los Angeles"},{"selectedCount":14,"id":22,"logo":"http://upload.beikaodi.com/school/logo/d1/fd/d1fd91b1ad4cd6952dc8cea759059837.jpg","chineseName":"哥伦比亚大学","englishName":"Columbia University"}]
     */

    private List<MatchTypesEntity> matchTypes;
    private List<YearsEntity> years;
    private List<WatchersEntity> watchers;
    private List<TopSchoolsEntity> topSchools;

    public void setMatchTypes(List<MatchTypesEntity> matchTypes) {
        this.matchTypes = matchTypes;
    }

    public void setYears(List<YearsEntity> years) {
        this.years = years;
    }

    public void setWatchers(List<WatchersEntity> watchers) {
        this.watchers = watchers;
    }

    public void setTopSchools(List<TopSchoolsEntity> topSchools) {
        this.topSchools = topSchools;
    }

    public List<MatchTypesEntity> getMatchTypes() {
        return matchTypes;
    }

    public List<YearsEntity> getYears() {
        return years;
    }

    public List<WatchersEntity> getWatchers() {
        return watchers;
    }

    public List<TopSchoolsEntity> getTopSchools() {
        return topSchools;
    }

    public static class MatchTypesEntity {
        /**
         * name : 冲刺院校
         * matchTypeId : MS_MATCH_TYPE_TOP
         * usersCount : 64
         * usersIncludeMe : false
         */

        private String name;
        private String matchTypeId;
        private int usersCount;
        private boolean usersIncludeMe;

        public void setName(String name) {
            this.name = name;
        }

        public void setMatchTypeId(String matchTypeId) {
            this.matchTypeId = matchTypeId;
        }

        public void setUsersCount(int usersCount) {
            this.usersCount = usersCount;
        }

        public void setUsersIncludeMe(boolean usersIncludeMe) {
            this.usersIncludeMe = usersIncludeMe;
        }

        public String getName() {
            return name;
        }

        public String getMatchTypeId() {
            return matchTypeId;
        }

        public int getUsersCount() {
            return usersCount;
        }

        public boolean getUsersIncludeMe() {
            return usersIncludeMe;
        }
    }

    public static class YearsEntity {
        /**
         * year : 2018年
         * count : 42
         */

        private String year;
        private int count;

        public void setYear(String year) {
            this.year = year;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getYear() {
            return year;
        }

        public int getCount() {
            return count;
        }
    }

    public static class WatchersEntity {
        /**
         * id : 37897
         * name : 4尺改哈爱就去P民哦了金融街热6S店蓝色的休xjjxjdjsj7
         * avatar : http://upload.beikaodi.com/customer/avatar/67/02/6702ad80ccd8bcaf9caa41b647c705044bf1d.png
         * selectSchoolCount : 118
         */

        private int id;
        private String name;
        private String avatar;
        private int selectSchoolCount;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setSelectSchoolCount(int selectSchoolCount) {
            this.selectSchoolCount = selectSchoolCount;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getAvatar() {
            return avatar;
        }

        public int getSelectSchoolCount() {
            return selectSchoolCount;
        }
    }

    public static class TopSchoolsEntity {
        /**
         * selectedCount : 22
         * id : 2
         * logo : http://upload.beikaodi.com/school/logo/oP/Br/4i6aKI67XRPeX13DgxtLFtubPMfy.jpg
         * chineseName : 麻省理工学院
         * englishName : Massachusetts Institute of Technology
         */

        private int selectedCount;
        private int id;
        private String logo;
        private String chineseName;
        private String englishName;

        public void setSelectedCount(int selectedCount) {
            this.selectedCount = selectedCount;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public void setChineseName(String chineseName) {
            this.chineseName = chineseName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public int getSelectedCount() {
            return selectedCount;
        }

        public int getId() {
            return id;
        }

        public String getLogo() {
            return logo;
        }

        public String getChineseName() {
            return chineseName;
        }

        public String getEnglishName() {
            return englishName;
        }
    }
}

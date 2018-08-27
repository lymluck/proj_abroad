package com.smartstudy.commonlib.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 17/6/20.
 */

public class MajorInfo extends BaseItemData {
    private String id;
    private String name;
    private ArrayList<MajorsInfo> minorCategories;

    public static class MajorsInfo extends BaseItemData {
        private String id;
        private String name;
        private List<Majors> majors;

        public static class Majors extends BaseItemData {
            private String id;
            private String name;
            private String majorCategoryId;
            private String minorCategoryId;
            private String majorCategoryName;
            private String minorCategoryName;

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

            public String getMajorCategoryId() {
                return majorCategoryId;
            }

            public void setMajorCategoryId(String majorCategoryId) {
                this.majorCategoryId = majorCategoryId;
            }

            public String getMinorCategoryId() {
                return minorCategoryId;
            }

            public void setMinorCategoryId(String minorCategoryId) {
                this.minorCategoryId = minorCategoryId;
            }

            public String getMajorCategoryName() {
                return majorCategoryName;
            }

            public void setMajorCategoryName(String majorCategoryName) {
                this.majorCategoryName = majorCategoryName;
            }

            public String getMinorCategoryName() {
                return minorCategoryName;
            }

            public void setMinorCategoryName(String minorCategoryName) {
                this.minorCategoryName = minorCategoryName;
            }
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

        public List<Majors> getMajors() {
            return majors;
        }

        public void setMajors(List<Majors> majors) {
            this.majors = majors;
        }
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

    public ArrayList<MajorsInfo> getMinorCategories() {
        return minorCategories;
    }

    public void setMinorCategories(ArrayList<MajorsInfo> minorCategories) {
        this.minorCategories = minorCategories;
    }
}

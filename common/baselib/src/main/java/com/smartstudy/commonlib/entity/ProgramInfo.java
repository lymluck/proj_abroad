package com.smartstudy.commonlib.entity;

import java.util.List;

/**
 * Created by louis on 17/6/20.
 */

public class ProgramInfo extends BaseItemData {
    private String id;
    private String name;
    private List<ProgramsInfo> programs;

    public static class ProgramsInfo extends BaseItemData {
        private String id;
        private String name;
        private String categoryId;
        private String categoryName;

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

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
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

    public List<ProgramsInfo> getPrograms() {
        return programs;
    }

    public void setPrograms(List<ProgramsInfo> programs) {
        this.programs = programs;
    }
}

package com.smartstudy.xxd.entity;

import java.util.List;

/**
 * @author louis
 * @date on 2018/5/24
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class ExamDateInfo {

    /**
     * date : 2018-05-05
     * items : [{"id":1,"exam":"SAT","selectCount":0},{"id":2,"exam":"雅思","selectCount":0}]
     */

    private String date;
    private List<ItemsEntity> items;

    public void setDate(String date) {
        this.date = date;
    }

    public void setItems(List<ItemsEntity> items) {
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public List<ItemsEntity> getItems() {
        return items;
    }

    public static class ItemsEntity {
        /**
         * id : 1
         * exam : SAT
         * selectCount : 0
         * selected
         */

        private int id;
        private String exam;
        private String selectCount;
        private boolean selected;

        public void setId(int id) {
            this.id = id;
        }

        public void setExam(String exam) {
            this.exam = exam;
        }

        public void setSelectCount(String selectCount) {
            this.selectCount = selectCount;
        }

        public int getId() {
            return id;
        }

        public String getExam() {
            return exam;
        }

        public String getSelectCount() {
            return selectCount;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            sb.append("\"id\":")
                .append(id);
            sb.append(",\"exam\":\"")
                .append(exam).append('\"');
            sb.append(",\"selectCount\":\"")
                .append(selectCount).append('\"');
            sb.append(",\"selected\":")
                .append(selected);
            sb.append('}');
            return sb.toString();
        }
    }
}

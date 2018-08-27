package com.smartstudy.xxd.entity;

/**
 * Created by louis on 17/8/1.
 */

public class PersonalInitOptsInfo {

    /**
     * id : STUDENT
     * name : 我是学生
     * next : step1
     */

    private String id;
    private String name;
    private String next;
    private boolean finish;
    private boolean selected;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNext() {
        return next;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

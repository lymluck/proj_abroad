package com.smartstudy.commonlib.entity;

public class ImageFloderInfo {
    /**
     * 图片的文件夹路径
     */
    private String dir;

    /**
     * 第一张图片的路径
     */
    private String firstImagePath;

    /**
     * 文件夹的名称
     */
    private String name;

    /**
     * 图片的数量
     */
    private int count;

    private boolean isSelected = false;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
        if (dir == null) {
            this.name = "所有图片";
        } else {
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf + 1);
        }
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}

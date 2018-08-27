package com.smartstudy.xxd.entity;

/**
 * Created by louis on 2017/3/8.
 */

public class VersionInfo {
    private boolean needUpdate;
    private String latestVersion;
    private boolean forceUpdate;
    private String description;
    private String packageUrl;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getPackageUrl() {
        return packageUrl;
    }

    public void setPackageUrl(String packageUrl) {
        this.packageUrl = packageUrl;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "needUpdate=" + needUpdate +
                ", latestVersion='" + latestVersion + '\'' +
                ", forceUpdate=" + forceUpdate +
                ", description='" + description + '\'' +
                ", packageUrl='" + packageUrl + '\'' +
                '}';
    }
}

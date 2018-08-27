package com.smartstudy.commonlib.entity;

/**
 * @author louis
 * @date on 2018/7/24
 * @describe 渠道、pid对应信息
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class ChannelInfo {
    private String id;
    private String name;
    private String medium;
    private String term;
    private String content;
    private String campaign;

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

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
            .append(id).append('\"');
        sb.append(",\"name\":\"")
            .append(name).append('\"');
        sb.append(",\"medium\":\"")
            .append(medium).append('\"');
        sb.append(",\"term\":\"")
            .append(term).append('\"');
        sb.append(",\"content\":\"")
            .append(content).append('\"');
        sb.append(",\"campaign\":\"")
            .append(campaign).append('\"');
        sb.append('}');
        return sb.toString();
    }
}

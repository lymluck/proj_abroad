package com.smartstudy.xxd.entity;

/**
 * Created by louis on 17/8/21.
 */

public class VisaAddrInfo {

    /**
     * id : 1
     * countryId : COUNTRY_226
     * name : 美国驻华大使馆
     * postcode : 100600
     * phone : 010-5679-4700
     * website : http://chinese.usembassy-china.org.cn/index.html
     * address : 北京安家楼路55号、旧馆地址：北京建国门外秀水东街2号
     * districts : 北京市、天津市、新疆自治区、青海省、甘肃省、陕西省、山西省、内蒙古自治区、宁夏自治区、河北省、河南省、山东省、湖北省、湖南省、江西省
     */

    private String id;
    private String countryId;
    private String name;
    private String postcode;
    private String phone;
    private String website;
    private String address;
    private String districts;

    public void setId(String id) {
        this.id = id;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDistricts(String districts) {
        this.districts = districts;
    }

    public String getId() {
        return id;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getName() {
        return name;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsite() {
        return website;
    }

    public String getAddress() {
        return address;
    }

    public String getDistricts() {
        return districts;
    }
}

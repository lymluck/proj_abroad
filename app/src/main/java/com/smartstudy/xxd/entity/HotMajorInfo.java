package com.smartstudy.xxd.entity;

import java.util.List;

/**
 * @author louis
 * @date on 2018/7/4
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class HotMajorInfo {

    /**
     * id : HOT
     * name : 热门专业
     * majors : [{"id":1,"name":"航空航天工程"},{"id":14,"name":"软件工程"},{"id":9,"name":"计算机工程"},{"id":2,"name":"材料科学与工程"},{"id":26,"name":"酒店管理"},{"id":3,"name":"环境工程"},{"id":25,"name":"运营管理"},{"id":43,"name":"计算机科学"},{"id":189,"name":"农业环境科学"}]
     */

    private String id;
    private String name;
    private List<MajorsInfo> majors;

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

    public List<MajorsInfo> getMajors() {
        return majors;
    }

    public void setMajors(List<MajorsInfo> majors) {
        this.majors = majors;
    }

    public static class MajorsInfo {
        /**
         * id : 1
         * name : 航空航天工程
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

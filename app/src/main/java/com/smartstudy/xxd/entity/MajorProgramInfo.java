package com.smartstudy.xxd.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author louis
 * @date on 2018/7/12
 * @describe TODO
 * @org xxd.smartstudy.com
 * @email luoyongming@innobuddy.com
 */
public class MajorProgramInfo implements Parcelable {

    /**
     * id : COUNTRY_226
     * name : 美国
     * directions : [{"id":"BUSINESS","name":"商科","categories":[{"id":7,"chineseName":"信息系统","englishName":"Information Systems","visitCount":1},{"id":2,"chineseName":"金融工程","englishName":"Financial Engineering","visitCount":0},{"id":3,"chineseName":"经济学","englishName":"Economics","visitCount":0},{"id":4,"chineseName":"商业分析","englishName":"Business Analytics","visitCount":0},{"id":5,"chineseName":"市场营销","englishName":"Marketing","visitCount":0},{"id":10,"chineseName":"人力资源管理","englishName":"Human Resource Management","visitCount":0},{"id":6,"chineseName":"会计学","englishName":"Accounting","visitCount":0},{"id":8,"chineseName":"供应链管理","englishName":"Supply Chain Management","visitCount":0},{"id":1,"chineseName":"金融学","englishName":"Finance","visitCount":0},{"id":9,"chineseName":"管理学","englishName":"Management","visitCount":0}]},{"id":"ENGINEERING","name":"工科","categories":[{"id":58,"chineseName":"化学工程","englishName":"Chemical Engineering","visitCount":0},{"id":62,"chineseName":"能源","englishName":"Energy","visitCount":0},{"id":63,"chineseName":"工程管理","englishName":"Engineering Management","visitCount":0},{"id":54,"chineseName":"计算机科学","englishName":"Computer Science","visitCount":0},{"id":55,"chineseName":"电气与计算机工程","englishName":"Electrical and Computer Engineering","visitCount":0},{"id":60,"chineseName":"材料","englishName":"Materials","visitCount":0},{"id":59,"chineseName":"生物工程","englishName":"Biomedical Engineering","visitCount":0},{"id":56,"chineseName":"机械工程","englishName":"Mechanical Engineering","visitCount":0},{"id":61,"chineseName":"工业工程","englishName":"Industrial Engineering","visitCount":0},{"id":57,"chineseName":"土木与环境工程","englishName":"Civil and Environmental Engineering","visitCount":0}]},{"id":"SCIENCE","name":"理科","categories":[{"id":66,"chineseName":"生物","englishName":"Biology","visitCount":0},{"id":65,"chineseName":"统计学","englishName":"Statistics","visitCount":0},{"id":64,"chineseName":"数据科学","englishName":"Data Science","visitCount":0}]},{"id":"SOCIETY","name":"社科","categories":[{"id":71,"chineseName":"社会工作","englishName":"Social work","visitCount":0},{"id":69,"chineseName":"教育学","englishName":"Education","visitCount":0},{"id":72,"chineseName":"公共政策与事务","englishName":"Public Policy and Affairs","visitCount":0},{"id":68,"chineseName":"语言学","englishName":"Linguistics","visitCount":0},{"id":74,"chineseName":"社会学","englishName":"Sociology","visitCount":0},{"id":67,"chineseName":"传媒","englishName":"Communication","visitCount":0},{"id":70,"chineseName":"建筑学","englishName":"Architecture","visitCount":0},{"id":73,"chineseName":"法学","englishName":"Law","visitCount":0}]}]
     */

    private String id;
    private String name;
    private ArrayList<DirectionsInfo> directions;

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

    public ArrayList<DirectionsInfo> getDirections() {
        return directions;
    }

    public void setDirections(ArrayList<DirectionsInfo> directions) {
        this.directions = directions;
    }

    public static class DirectionsInfo implements Parcelable {
        /**
         * id : BUSINESS
         * name : 商科
         * categories : [{"id":7,"chineseName":"信息系统","englishName":"Information Systems","visitCount":1},{"id":2,"chineseName":"金融工程","englishName":"Financial Engineering","visitCount":0},{"id":3,"chineseName":"经济学","englishName":"Economics","visitCount":0},{"id":4,"chineseName":"商业分析","englishName":"Business Analytics","visitCount":0},{"id":5,"chineseName":"市场营销","englishName":"Marketing","visitCount":0},{"id":10,"chineseName":"人力资源管理","englishName":"Human Resource Management","visitCount":0},{"id":6,"chineseName":"会计学","englishName":"Accounting","visitCount":0},{"id":8,"chineseName":"供应链管理","englishName":"Supply Chain Management","visitCount":0},{"id":1,"chineseName":"金融学","englishName":"Finance","visitCount":0},{"id":9,"chineseName":"管理学","englishName":"Management","visitCount":0}]
         */

        private String id;
        private String name;
        private List<CategoriesInfo> categories;

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

        public List<CategoriesInfo> getCategories() {
            return categories;
        }

        public void setCategories(List<CategoriesInfo> categories) {
            this.categories = categories;
        }

        public static class CategoriesInfo implements Parcelable {
            /**
             * id : 7
             * chineseName : 信息系统
             * englishName : Information Systems
             * visitCount : 1
             */

            private int id;
            private String chineseName;
            private String englishName;
            private int visitCount;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getChineseName() {
                return chineseName;
            }

            public void setChineseName(String chineseName) {
                this.chineseName = chineseName;
            }

            public String getEnglishName() {
                return englishName;
            }

            public void setEnglishName(String englishName) {
                this.englishName = englishName;
            }

            public int getVisitCount() {
                return visitCount;
            }

            public void setVisitCount(int visitCount) {
                this.visitCount = visitCount;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.chineseName);
                dest.writeString(this.englishName);
                dest.writeInt(this.visitCount);
            }

            public CategoriesInfo() {
            }

            protected CategoriesInfo(Parcel in) {
                this.id = in.readInt();
                this.chineseName = in.readString();
                this.englishName = in.readString();
                this.visitCount = in.readInt();
            }

            public static final Creator<CategoriesInfo> CREATOR = new Creator<CategoriesInfo>() {
                @Override
                public CategoriesInfo createFromParcel(Parcel source) {
                    return new CategoriesInfo(source);
                }

                @Override
                public CategoriesInfo[] newArray(int size) {
                    return new CategoriesInfo[size];
                }
            };
        }


        public DirectionsInfo() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeTypedList(categories);
        }

        protected DirectionsInfo(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.categories = in.createTypedArrayList(CategoriesInfo.CREATOR);
        }

        public static final Creator<DirectionsInfo> CREATOR = new Creator<DirectionsInfo>() {
            @Override
            public DirectionsInfo createFromParcel(Parcel source) {
                return new DirectionsInfo(source);
            }

            @Override
            public DirectionsInfo[] newArray(int size) {
                return new DirectionsInfo[size];
            }
        };
    }


    public MajorProgramInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(directions);
    }

    protected MajorProgramInfo(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.directions = in.createTypedArrayList(DirectionsInfo.CREATOR);
    }

    public static final Parcelable.Creator<MajorProgramInfo> CREATOR = new Parcelable.Creator<MajorProgramInfo>() {
        @Override
        public MajorProgramInfo createFromParcel(Parcel source) {
            return new MajorProgramInfo(source);
        }

        @Override
        public MajorProgramInfo[] newArray(int size) {
            return new MajorProgramInfo[size];
        }
    };
}

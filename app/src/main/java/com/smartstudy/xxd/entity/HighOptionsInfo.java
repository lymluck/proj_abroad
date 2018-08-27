package com.smartstudy.xxd.entity;

import com.smartstudy.commonlib.entity.IdNameInfo;

import java.util.List;

/**
 * @author yqy
 * @date on 2018/4/8
 * @describe 高校筛选方式
 * @org xxd.smarttudy.com
 * @email yeqingyu@innobuddy.com
 */
public class HighOptionsInfo {

    private Ranks ranks;
    private FeeRange feeRange;
    private SexualTypes sexualTypes;
    private BoarderTypes boarderTypes;

    public Ranks getRanks() {
        return ranks;
    }

    public void setRanks(Ranks ranks) {
        this.ranks = ranks;
    }

    public FeeRange getFeeRange() {
        return feeRange;
    }

    public void setFeeRange(FeeRange feeRange) {
        this.feeRange = feeRange;
    }

    public SexualTypes getSexualTypes() {
        return sexualTypes;
    }

    public void setSexualTypes(SexualTypes sexualTypes) {
        this.sexualTypes = sexualTypes;
    }

    public BoarderTypes getBoarderTypes() {
        return boarderTypes;
    }

    public void setBoarderTypes(BoarderTypes boarderTypes) {
        this.boarderTypes = boarderTypes;
    }

    public class Ranks {

        private String title;
        private String key;
        private List<IdNameInfo> options;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<IdNameInfo> getOptions() {
            return options;
        }

        public void setOptions(List<IdNameInfo> options) {
            this.options = options;
        }
    }


    public class FeeRange {
        private String title;
        private String key;
        private List<IdNameInfo> options;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<IdNameInfo> getOptions() {
            return options;
        }

        public void setOptions(List<IdNameInfo> options) {
            this.options = options;
        }
    }


    public class SexualTypes {
        private String title;
        private String key;
        private List<IdNameInfo> options;

        public List<IdNameInfo> getOptions() {
            return options;
        }

        public void setOptions(List<IdNameInfo> options) {
            this.options = options;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }


    public class BoarderTypes {
        private String title;
        private String key;
        private List<IdNameInfo> options;

        public class BoarderTypesOptions extends OptionsInfo {

            public BoarderTypesOptions(String id, String name, String targetDegreeId) {
                super(id, name, targetDegreeId);
            }
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<IdNameInfo> getOptions() {
            return options;
        }

        public void setOptions(List<IdNameInfo> options) {
            this.options = options;
        }
    }
}

package com.smartstudy.xxd.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yqy on 2017/10/24.
 */

public class GpaDesInfo {
    private String name;

    private List<GpaDesInfo.GpaDesInfoEntity> gpaDesInfoEntities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GpaDesInfoEntity> getGpaDesInfoEntities() {
        return gpaDesInfoEntities;
    }

    public void setGpaDesInfoEntities(List<GpaDesInfoEntity> gpaDesInfoEntities) {
        this.gpaDesInfoEntities = gpaDesInfoEntities;
    }

    public class GpaDesInfoEntity {
        private String name;
        private String formula;
        private String scoreTableImageUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFormula() {
            return formula;
        }

        public void setFormula(String formula) {
            this.formula = formula;
        }

        public String getScoreTableImageUrl() {
            return scoreTableImageUrl;
        }

        public void setScoreTableImageUrl(String scoreTableImageUrl) {
            this.scoreTableImageUrl = scoreTableImageUrl;
        }
    }


    public static List<GpaDesInfo> creatData(List<GpaCalculationDetail> gpaCalculationDetails) {
        List<GpaDesInfo> gpaDesInfos = new ArrayList<GpaDesInfo>();
        int len = gpaCalculationDetails.size();
        GpaDesInfo gpaDesInfo;
        for (int i = 0; i < len; i++) {
            gpaDesInfo = new GpaDesInfo();
            gpaDesInfo.setName(gpaCalculationDetails.get(i).getName());
            List<GpaDesInfoEntity> gpaDesInfoEntities = new ArrayList<GpaDesInfoEntity>();
            GpaDesInfoEntity gpaDesInfoEntity = gpaDesInfo.new GpaDesInfoEntity();
            gpaDesInfoEntity.setName(gpaCalculationDetails.get(i).getName());
            gpaDesInfoEntity.setFormula(gpaCalculationDetails.get(i).getFormula());
            gpaDesInfoEntity.setScoreTableImageUrl(gpaCalculationDetails.get(i).getScoreTableImageUrl());
            gpaDesInfoEntities.add(gpaDesInfoEntity);
            gpaDesInfo.setGpaDesInfoEntities(gpaDesInfoEntities);
            gpaDesInfos.add(gpaDesInfo);
            gpaDesInfo = null;
        }
        return gpaDesInfos;
    }
}

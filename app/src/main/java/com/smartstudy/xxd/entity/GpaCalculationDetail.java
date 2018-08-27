package com.smartstudy.xxd.entity;

/**
 * Created by yqy on 2017/10/24.
 */

public class GpaCalculationDetail {
    private String name;
    private String gpaResult;
    private String gpaMax;
    private String formula;
    private String scoreTableImageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGpaResult() {
        return gpaResult;
    }

    public void setGpaResult(String gpaResult) {
        this.gpaResult = gpaResult;
    }

    public String getGpaMax() {
        return gpaMax;
    }

    public void setGpaMax(String gpaMax) {
        this.gpaMax = gpaMax;
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

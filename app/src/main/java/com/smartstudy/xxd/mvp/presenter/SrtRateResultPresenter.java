package com.smartstudy.xxd.mvp.presenter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smartstudy.commonlib.base.callback.BaseCallback;
import com.smartstudy.xxd.R;
import com.smartstudy.xxd.entity.SmartChooseInfo;
import com.smartstudy.xxd.mvp.contract.SrtRateResultContract;
import com.smartstudy.xxd.mvp.model.SrtTestRateModel;

import java.math.BigDecimal;

/**
 * Created by louis on 2017/3/2.
 */

public class SrtRateResultPresenter implements SrtRateResultContract.Presenter {

    private SrtRateResultContract.View view;
    private Context mContext;

    public SrtRateResultPresenter(SrtRateResultContract.View view, Context context) {
        this.view = view;
        this.mContext = context;
        this.view.setPresenter(this);
    }

    @Override
    public void getTestResult(String id, final SmartChooseInfo info, final String chineseName) {
        SrtTestRateModel.getResult(id, new BaseCallback<String>() {
            @Override
            public void onErr(String errCode, String msg) {
                view.showTip(errCode, msg);
            }

            @Override
            public void onSuccess(String result) {
                JSONObject data = JSON.parseObject(result);
                //展示结果信息
                double rate = data.getDouble("rate");
                showResult(rate);
                //展示概率
                String avgRate = data.getString("averageRate");
                if (avgRate != null) {
                    if (avgRate.contains("%")) {
                        avgRate = avgRate.substring(0, avgRate.length() - 1);
                    }
                    if (!TextUtils.isEmpty(avgRate)) {
                        view.showRate(Double.parseDouble(avgRate), rate);
                    } else {
                        view.showRate(-1, rate);
                    }
                } else {
                    view.showRate(-1, rate);
                }
                int id = 0;
                if (info.getToeflScore() > 0) {
                    //展示托福评语
                    id++;
                    showSugMsg(id, "托福", data.getIntValue("toeflWinRate"), info.getToeflScore(), data.getDoubleValue("qualifiedToefl"), "toefl");
                } else if (info.getIeltsScore() > 0) {
                    //展示雅思评语
                    id++;
                    showSugMsg(id, "雅思", data.getIntValue("ieltsWinRate"), info.getIeltsScore(), data.getDoubleValue("qualifiedIelts"), "ielts");
                }

                //展示标准化考试成绩评语
                if (info.getGreScore() > 0) {
                    id++;
                    showSugMsg(id, "GRE", data.getIntValue("greWinRate"), info.getGreScore(), data.getDoubleValue("qualifiedGre"), "gre");
                } else if (info.getGmatScore() > 0) {
                    id++;
                    showSugMsg(id, "GMAT", data.getIntValue("gmatWinRate"), info.getGmatScore(), data.getDoubleValue("qualifiedGmat"), "gmat");
                } else if (info.getSatScore() > 0) {
                    id++;
                    showSugMsg(id, "SAT", data.getIntValue("satWinRate"), info.getSatScore(), data.getDoubleValue("qualifiedSat"), "sat");
                } else if (info.getActScore() > 0) {
                    id++;
                    showSugMsg(id, "ACT", data.getIntValue("actWinRate"), info.getActScore(), data.getDoubleValue("qualifiedAct"), "act");
                }
            }
        });
    }

    //展示结果信息
    private void showResult(double rate) {
        String msg = "胜券在握";
        int color = R.color.rate_test_rlt1;
        int rating = 5;
        if (rate < 60) {
            msg = "比较容易";
            color = R.color.rate_test_rlt2;
            rating = 4;
        }
        if (rate < 40) {
            msg = "可以尝试";
            color = R.color.rate_test_rlt3;
            rating = 3;
        }
        if (rate < 20) {
            msg = "有些难度";
            color = R.color.rate_test_rlt4;
            rating = 2;
        }
        if (rate < 10) {
            msg = "比较困难";
            color = R.color.rate_test_rlt5;
            rating = 1;
        }
        view.showResultMsg(msg, color, rating);
    }

    private void showSugMsg(int id, String Name, int winRate, double userScore, double qualifiedScore, String type) {
        Spanned userScoreTxt = Html.fromHtml(String.format(mContext.getString(R.string.your_score), id, Name)
                + "<font color=#FF8A00>" + userScore + "</font>");
        String win_rate = String.format(mContext.getString(R.string.score_win_rate),
                "<font color=#078CF1>" + winRate + "%" + "</font>");
        String Msg = "";
        if (qualifiedScore > 0) {
            if (userScore < qualifiedScore) {
                Double diff = qualifiedScore - userScore;
                BigDecimal bdl = new BigDecimal(diff);
                Msg = String.format(mContext.getString(R.string.score_but_diff), Name, "<font color=#078CF1>" + bdl.setScale(1, BigDecimal.ROUND_HALF_UP) + "分</font>");
                if (winRate > 0) {
                    Msg += win_rate;
                }
                Msg += String.format(mContext.getString(R.string.score_but_sug),
                        Name, Name, qualifiedScore + "");
                showScoreNoSug(userScoreTxt, Html.fromHtml(Msg), type, true);
            } else {
                Double diff = userScore - qualifiedScore;
                if (diff > 0) {
                    BigDecimal bdl = new BigDecimal(diff);
                    Msg = String.format(mContext.getString(R.string.score_win_sug), Name, "<font color=#078CF1>"
                            + bdl.setScale(1, BigDecimal.ROUND_HALF_UP) + "分</font>");
                } else if (diff == 0) {
                    Msg = String.format(mContext.getString(R.string.score_just_right), Name);
                }
                if (winRate > 0) {
                    Msg += win_rate;
                }
                if (diff > 0) {
                    Msg += String.format(mContext.getString(R.string.score_win_very_good), Name);
                    showScoreNoSug(userScoreTxt, Html.fromHtml(Msg), type, false);
                } else if (diff == 0) {
                    Msg += String.format(mContext.getString(R.string.score_win_just_good), Name);
                    showScoreNoSug(userScoreTxt, Html.fromHtml(Msg), type, true);
                }
            }
        } else {
            Msg += win_rate + mContext.getString(R.string.score_help);
            showScoreNoSug(userScoreTxt, Html.fromHtml(Msg), type, true);
        }
    }

    private void showScoreNoSug(Spanned userScoreTxt, Spanned sugMsg, String type, boolean showWebSite) {
        if ("toefl".equals(type) || "ielts".equals(type)) {
            view.showEgSug(userScoreTxt, sugMsg, type, showWebSite);
        } else if ("sat".equals(type) || "act".equals(type) || "gre".equals(type) || "gmat".equals(type)) {
            view.showTestSug(userScoreTxt, sugMsg, type, showWebSite);
        }
    }

    @Override
    public void onDetachView() {
        view = null;
    }

}

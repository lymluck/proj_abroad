package com.smartstudy.xxd.entity;

/**
 * Created by louis on 17/8/21.
 */

public class VisaQaInfo {

    /**
     * question : Q3：美国签证必须面谈吗？
     * answer : A：首次申请必须面谈，续签可以无需面谈。
     */

    private String question;
    private String answer;

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}

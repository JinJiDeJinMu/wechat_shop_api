package com.chundengtai.base.entity;

import java.io.Serializable;

/**
 * 实体
 * 表名 nideshop_goods_issue
 */
public class GoodsIssueEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //问题
    private String question;
    //回答
    private String answer;
    //商户ID
    private Integer merchant_id;

    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置：问题
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * 获取：问题
     */
    public String getQuestion() {
        return question;
    }

    /**
     * 设置：回答
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * 获取：回答
     */
    public String getAnswer() {
        return answer;
    }

    public Integer getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(Integer merchant_id) {
        this.merchant_id = merchant_id;
    }


}

package com.gad.domin;

import java.math.BigDecimal;

/**
 * @author zhongchao
 * @title PlayResult.java
 * @Date 2017-12-03
 * @since v1.0.0
 */
public class PlayResult {

    /**
     * 购买计划
     */
    private String playType;

    /**
     * 跟入轮数
     */
    private int playRoundNum;

    /**
     * 亏损的轮数
     */
    private int failedRoundNum;

    /**
     *
     */
    private int filedNum;


    /**
     * 盈利的次数
     */
    private int winNum;

    /**
     * 所有 下单 次数
     */
    private int playNum;

    /**
     * 所有下单次数
     */
    private int allRoundNum = 0;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 盈利金额
     */
    private BigDecimal winMoney;

    /**
     * 总投入金额  每次投入金额累计
     */
    private BigDecimal payMoney;

    /**
     * 改变计划次数
     */
    private int changePlanNum;

    /**
     * 是否需要改变计划
     */
    private boolean changePlan;

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public int getPlayRoundNum() {
        return playRoundNum;
    }

    public void setPlayRoundNum(int playRoundNum) {
        this.playRoundNum = playRoundNum;
    }

    public int getFailedRoundNum() {
        return failedRoundNum;
    }

    public void setFailedRoundNum(int failedRoundNum) {
        this.failedRoundNum = failedRoundNum;
    }

    public int getFiledNum() {
        return filedNum;
    }

    public void setFiledNum(int filedNum) {
        this.filedNum = filedNum;
    }

    public int getWinNum() {
        return winNum;
    }

    public void setWinNum(int winNum) {
        this.winNum = winNum;
    }

    public int getPlayNum() {
        return playNum;
    }

    public void setPlayNum(int playNum) {
        this.playNum = playNum;
    }

    public int getAllRoundNum() {
        return allRoundNum;
    }

    public void setAllRoundNum(int allRoundNum) {
        this.allRoundNum = allRoundNum;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getWinMoney() {
        return winMoney;
    }

    public void setWinMoney(BigDecimal winMoney) {
        this.winMoney = winMoney;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public int getChangePlanNum() {
        return changePlanNum;
    }

    public void setChangePlanNum(int changePlanNum) {
        this.changePlanNum = changePlanNum;
    }

    public boolean isChangePlan() {
        return changePlan;
    }

    public void setChangePlan(boolean changePlan) {
        this.changePlan = changePlan;
    }
}



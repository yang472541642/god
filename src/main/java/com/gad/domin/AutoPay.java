package com.gad.domin;

import com.gad.common.MoneyTypeEnum;
import com.gad.domin.dto.PlanConfig;

import java.math.BigDecimal;

/**
 * @author zhongchao
 * @title AutoPay.java
 * @Date 2017-11-30
 * @since v1.0.0
 */
public class AutoPay {

    /**
     * 期数
     */
    private String nper;
    /**
     * 上一次买入期数
     */
    private String lastPer;

    /**
     * 购买号码
     */
    private String numbers;

    /**
     * 次数
     */
    private int count = 0;

    /**
     * 跟入轮数
     */
    private int playRoundNum;

    /**
     * 是否盈利
     */
    private boolean isWin;

    /**
     * 购买计划
     */
    private String plan;

    /**
     * 单位
     */
    private MoneyTypeEnum moneyTypeEnum;

    /**
     * 购买计划
     */
    private PlayTypeEnum playTypeEnum;

    /**
     * 购买计划
     */
    private PlanConfig planConfig;

    /**
     * 中奖位数
     */
    private Integer digit;

    /**
     * 投入金额
     */
    private BigDecimal payMoney;

    /**
     * 预计收入金额
     */
    private BigDecimal winMoney;

    /**
     * 赔率
     */
    private Integer playOdds;
    
    /**
     * 计划编码
     */
    private String planCode;
    
    /**
     * 计划名称
     */
    private String planName;
    
    public String getPlanName() {
        return planName;
    }
    
    public void setPlanName(String planName) {
        this.planName = planName;
    }
    
    public String getLastPer() {
        return lastPer;
    }
    
    public void setLastPer(String lastPer) {
        this.lastPer = lastPer;
    }
    
    public String getNper() {
        return nper;
    }

    public void setNper(String nper) {
        this.nper = nper;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPlayRoundNum() {
        return playRoundNum;
    }

    public void setPlayRoundNum(int playRoundNum) {
        this.playRoundNum = playRoundNum;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public MoneyTypeEnum getMoneyTypeEnum() {
        return moneyTypeEnum;
    }

    public void setMoneyTypeEnum(MoneyTypeEnum moneyTypeEnum) {
        this.moneyTypeEnum = moneyTypeEnum;
    }

    public PlayTypeEnum getPlayTypeEnum() {
        return playTypeEnum;
    }

    public void setPlayTypeEnum(PlayTypeEnum playTypeEnum) {
        this.playTypeEnum = playTypeEnum;
    }

    public PlanConfig getPlanConfig() {
        return planConfig;
    }

    public void setPlanConfig(PlanConfig planConfig) {
        this.planConfig = planConfig;
    }

    public Integer getDigit() {
        return digit;
    }

    public void setDigit(Integer digit) {
        this.digit = digit;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public BigDecimal getWinMoney() {
        return winMoney;
    }

    public void setWinMoney(BigDecimal winMoney) {
        this.winMoney = winMoney;
    }

    public Integer getPlayOdds() {
        return playOdds;
    }

    public void setPlayOdds(Integer playOdds) {
        this.playOdds = playOdds;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getPlanCode() {
        return planCode;
    }
}

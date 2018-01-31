/*
 * @(#) PlanDetailDTO.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.domin.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
public class PlanDetailDTO implements Serializable {

    /**
     * 热度
     */
    private Float profit;


    private Boolean curBool;

    /**
     * 方案编码
     */
    private String jhfaCode;

    /**
     * 方案名称
     */
    private String jhfaName;

    /**
     * 错误
     */
    private Integer currentError;

    /**
     * 正确
     */
    private Integer currentCorrect;

    /**
     * 胜算
     */
    private Float winRate;

    /**
     * 错误次数
     */
    private Integer errorCount;

    /**
     * 当前规划号码
     */
    private String firstPlanResult;


    private Integer maxCorrent;

    /**
     * 当前中奖情况
     */
    private List<FvDTO> fvList;

    /**
     * 上一期是否中奖
     */
    private Boolean lastIsSuccess;

    /**
     * 上一期开奖号码
     */
    private String lastKjNum;

    public Boolean getLastIsSuccess() {
        return lastIsSuccess;
    }

    public void setLastIsSuccess(Boolean lastIsSuccess) {
        this.lastIsSuccess = lastIsSuccess;
    }

    public Float getProfit() {
        return profit;
    }

    public void setProfit(Float profit) {
        this.profit = profit;
    }

    public Boolean getCurBool() {
        return curBool;
    }

    public void setCurBool(Boolean curBool) {
        this.curBool = curBool;
    }

    public String getJhfaCode() {
        return jhfaCode;
    }

    public void setJhfaCode(String jhfaCode) {
        this.jhfaCode = jhfaCode;
    }

    public Integer getCurrentError() {
        return currentError;
    }

    public void setCurrentError(Integer currentError) {
        this.currentError = currentError;
    }

    public Integer getCurrentCorrect() {
        return currentCorrect;
    }

    public void setCurrentCorrect(Integer currentCorrect) {
        this.currentCorrect = currentCorrect;
    }

    public Float getWinRate() {
        return winRate;
    }

    public void setWinRate(Float winRate) {
        this.winRate = winRate;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getJhfaName() {
        return jhfaName;
    }

    public void setJhfaName(String jhfaName) {
        this.jhfaName = jhfaName;
    }

    public Integer getMaxCorrent() {
        return maxCorrent;
    }

    public void setMaxCorrent(Integer maxCorrent) {
        this.maxCorrent = maxCorrent;
    }

    public String getFirstPlanResult() {
        return firstPlanResult;
    }

    public void setFirstPlanResult(String firstPlanResult) {
        this.firstPlanResult = firstPlanResult;
    }

    public void setFvList(List<FvDTO> fvList) {
        this.fvList = fvList;
    }

    public List<FvDTO> getFvList() {
        return fvList;
    }

    public void setLastKjNum(String lastKjNum) {
        this.lastKjNum = lastKjNum;
    }

    public String getLastKjNum() {
        return lastKjNum;
    }
}

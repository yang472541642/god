/*
 * @(#) HistoryPlanDtileDTO.java ,2017年11月30日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.domin.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/30
 */
public class HistoryPlanDetailDTO implements Serializable {

    /**
     * 历史开奖列表
     */
    private List<CaiPiaoPeriodInfo> caipiaoList;

    /**
     * 好多期  063-065
     */
    private String item;

    /**
     * 计划名称
     */
    private String planName;

    /**
     * 计划编码
     */
    private String planNum;

    /**
     * 中奖号码
     */
    private String resultNumber;

    /**
     * 是否中奖
     */
    private Boolean status;

    /**
     * 位置  个位 十位 。。。
     */
    private String wei;

    public List<CaiPiaoPeriodInfo> getCaipiaoList() {
        return caipiaoList;
    }

    public void setCaipiaoList(List<CaiPiaoPeriodInfo> caipiaoList) {
        this.caipiaoList = caipiaoList;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanNum() {
        return planNum;
    }

    public void setPlanNum(String planNum) {
        this.planNum = planNum;
    }

    public String getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(String resultNumber) {
        this.resultNumber = resultNumber;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getWei() {
        return wei;
    }

    public void setWei(String wei) {
        this.wei = wei;
    }
}

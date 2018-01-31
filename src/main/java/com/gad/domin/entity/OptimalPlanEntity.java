/*
 * @(#) OptimalPlanEntity.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.domin.entity;

//import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
//@Entity
public class OptimalPlanEntity implements Serializable {

    /**
     * 是否为最新计划
     */
    private boolean newPlanFlag;

    /**
     * 当前期数
     */
    private String periods;

    /**
     * 追加期数
     */
    private Integer keepTimes;

    /**
     * 计划码
     */
    private String code;

    public boolean getNewPlanFlag() {
        return newPlanFlag;
    }

    public boolean isNewPlanFlag() {
        return newPlanFlag;
    }

    public void setNewPlanFlag(boolean newPlanFlag) {
        this.newPlanFlag = newPlanFlag;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public Integer getKeepTimes() {
        return keepTimes;
    }

    public void setKeepTimes(Integer keepTimes) {
        this.keepTimes = keepTimes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

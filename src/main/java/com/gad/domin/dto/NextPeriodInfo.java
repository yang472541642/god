/*
 * @(#) NextPeriodInfo.java ,2017年11月30日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.domin.dto;

import java.io.Serializable;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/30
 */
public class NextPeriodInfo implements Serializable {

    /**
     * 下一场次的编号  20171130-038
     */
    private String nextPeriodStr;

    /**
     * 剩余小时数
     */
    private Integer hour;

    /**
     * 剩余分钟数
     */
    private Integer minute;

    /**
     * 剩余秒数
     */
    private Integer second;

    public String getNextPeriodStr() {
        return nextPeriodStr;
    }

    public void setNextPeriodStr(String nextPeriodStr) {
        this.nextPeriodStr = nextPeriodStr;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }
}

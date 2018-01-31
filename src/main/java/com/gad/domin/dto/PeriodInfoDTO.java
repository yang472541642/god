/*
 * @(#) PeriodInfoDTO.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.domin.dto;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
public class PeriodInfoDTO {

    /**
     * 剩余开奖分钟数
     */
    private Integer minute;

    /**
     * 下一期STR   [YYYYMMDD-001]
     */
    private String nextPeriodStr;

    /**
     * 日期
     */
    private String day;

    /**
     * 期数
     */
    private String item;

    /**
     * 剩余开奖小时数
     */
    private Integer hour;

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getNextPeriodStr() {
        return nextPeriodStr;
    }

    public void setNextPeriodStr(String nextPeriodStr) {
        String str[] = nextPeriodStr.split("-");
        this.day = str[0];
        this.item = str[1];
        this.nextPeriodStr = nextPeriodStr;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "PeriodInfoDTO{" +
                "minute=" + minute +
                ", nextPeriodStr='" + nextPeriodStr + '\'' +
                ", hour=" + hour +
                '}';
    }


}

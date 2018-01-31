/*
 * @(#) CaiPiaoPeriodInfo.java ,2017年11月30日
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
public class CaiPiaoPeriodInfo implements Serializable {

    //期数
    private String period;

    //开奖结果
    private String result;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

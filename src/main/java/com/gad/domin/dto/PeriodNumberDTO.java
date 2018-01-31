package com.gad.domin.dto;

import java.io.Serializable;

/**
 * Created by jlcao on 2017/12/1.
 */
public class PeriodNumberDTO implements Serializable {

    /**
     * 开奖总数
     */
    private Integer kaiCount;

    /**
     * 当前开奖号码    高位 到低位     0万  1千  2百  3十 4个,
     */
    private Integer[] currentNumberArr;

    /**
     * 当前开奖期数
     */
    private String currentPeriod;

    /**
     * 剩余未开数量
     */
    private Integer weiKaiCount;

    public Integer getKaiCount() {
        return kaiCount;
    }

    public void setKaiCount(Integer kaiCount) {
        this.kaiCount = kaiCount;
    }

    public Integer[] getCurrentNumberArr() {
        return currentNumberArr;
    }

    public void setCurrentNumberArr(Integer[] currentNumberArr) {
        this.currentNumberArr = currentNumberArr;
    }

    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public Integer getWeiKaiCount() {
        return weiKaiCount;
    }

    public void setWeiKaiCount(Integer weiKaiCount) {
        this.weiKaiCount = weiKaiCount;
    }
}

/*
 * @(#) PlanConfig.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.domin.dto;

import com.gad.common.ByTypeEnum;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
public class PlanConfig implements Serializable {

    /**
     * 查询类型
     */
    private ByTypeEnum type = ByTypeEnum.DW;

    /**
     * 定位胆位置 （1 个  2 十 3百  4 千  5万）
     * 直选位置 （1 前二 2 后二 3前三 4中三 5后三）
     */
    private Integer  offset = 1;

    /**
     * 号码个数 ( 1 - 9)
     */
    private Integer number = 5;

    /**
     * 几期 (1 - 15)
     */
    private Integer keepPeriosd = 3;

    public Integer getOffset() {
        return offset;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getKeepPeriosd() {
        return keepPeriosd;
    }

    public void setKeepPeriosd(Integer keepPeriosd) {
        this.keepPeriosd = keepPeriosd;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public ByTypeEnum getType() {
        return type;
    }

    public void setType(ByTypeEnum type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanConfig that = (PlanConfig) o;
        return type == that.type &&
                Objects.equals(offset, that.offset) &&
                Objects.equals(number, that.number) &&
                Objects.equals(keepPeriosd, that.keepPeriosd);
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, offset, number, keepPeriosd);
    }
}

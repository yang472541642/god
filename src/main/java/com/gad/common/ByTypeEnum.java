/*
 * @(#) ByTypeEnum.java ,2017年11月30日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.common;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/30
 */
public enum ByTypeEnum {

    //定位
    DW(1, "dw"),

    //胆码
    BDW(2,"bdw"),

    //直选
    ZHX(3, "zhx"),

    //组选
    ZUX(4,"zux"),

    //四星复式
    SIS(5, "sis"),

    //五星复式
    WUX(6,"wux")
    ;

    private final int id;
    private final String code;

    ByTypeEnum(int id, String code) {
        this.id = id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}

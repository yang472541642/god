/*
 * @(#) FvDTO.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.domin.dto;

import java.io.Serializable;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
public class FvDTO implements Serializable {
    /**
     * 期数
     */
    private String item;

    /**
     * 返回结果 (当有返回结果时表示 未命中)
     */
    private String result;



    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

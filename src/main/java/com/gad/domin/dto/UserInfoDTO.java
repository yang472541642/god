/*
 * @(#) UserInfoDTO.java ,2017年11月13日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.domin.dto;

import java.io.Serializable;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/13
 */
public class UserInfoDTO implements Serializable {
    //"uid":"42588","name":"jlcaold","rebate":"0.025","money":"38.7000","type":"agent","lastipaddreess":"106.91.49.83","nowtime":"1510202118","qq":0,"safepwd":1,"urlrebate":"","status":"0","safequestion":0,"token":"a32063970jto2vadl010tvq9l1","host":"http:\/\/106.15.91.183:8080\/chess\/index.php\/Home\/index\/setCookieName\/value\/15105303544258827494\/token\/151053035442588\/uid\/42588","sysTime":1510530355,"safeLevel":"\u9ad8"
    //

    private String uid;

    private String name;

    private String rebate;

    private String money;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRebate() {
        return rebate;
    }

    public void setRebate(String rebate) {
        this.rebate = rebate;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}

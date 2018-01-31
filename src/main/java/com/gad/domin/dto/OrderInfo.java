/*
 * @(#) OrderInfo.java ,2017年11月13日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.domin.dto;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/13
 */
public class OrderInfo {
    //{"id":"117111p1csc","uid":"42588","ticketnum":"20171113024","tickettype":"重庆时时彩","ticketstar":"定位胆","ticketkind":"直猜万位","note":"01234","multiple":"1","pattern":"角","count":"5","time":"1510528269","status":"0","cost":1,

    /**
     * 订单ID  117111p1csc
     */
    private String id;

    /**
     * 用户ID 42588
     */
    private String uid;

    /**
     * 选购期数  20171113024
     */
    private String ticketnum;

    /**
     * 彩票名称 重庆时时彩
     */
    private String tickettype;

    /**
     * 购买item  定位胆
     */
    private String ticketstar;

    /**
     * 购买item 直猜万位
     */
    private String ticketkind;

    /**
     * 购买的号码  01234
     */
    private String note;

    /**
     * 购买的倍数
     */
    private String multiple;

    /**
     * 购买的单位   元  角 分
     */
    private String pattern;

    /**
     * 号码个数
     */
    private String count;

    /**
     * 购买时间
     */
    private Long time;


    /**
     * 状态  0 (购买成功，可撤单)      3(已撤单)
     */
    private String status;

    /**
     * 花费金额
     */
    private String cost;

    // "odds":"0","winnum":null,"winmoney":"0.0000","notetype":"1","isdelete":"0"}

    private String odds;

    /**
     * 获奖的号码
     */
    private String winnum;

    /**
     * 盈利金额
     */
    private String winmoney;

    /**
     * 备注类型
     */
    private String notetype;

    /**
     * 是否删除
     */
    private String isdelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTicketnum() {
        return ticketnum;
    }

    public void setTicketnum(String ticketnum) {
        this.ticketnum = ticketnum;
    }

    public String getTickettype() {
        return tickettype;
    }

    public void setTickettype(String tickettype) {
        this.tickettype = tickettype;
    }

    public String getTicketstar() {
        return ticketstar;
    }

    public void setTicketstar(String ticketstar) {
        this.ticketstar = ticketstar;
    }

    public String getTicketkind() {
        return ticketkind;
    }

    public void setTicketkind(String ticketkind) {
        this.ticketkind = ticketkind;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getOdds() {
        return odds;
    }

    public void setOdds(String odds) {
        this.odds = odds;
    }

    public String getWinnum() {
        return winnum;
    }

    public void setWinnum(String winnum) {
        this.winnum = winnum;
    }

    public String getWinmoney() {
        return winmoney;
    }

    public void setWinmoney(String winmoney) {
        this.winmoney = winmoney;
    }

    public String getNotetype() {
        return notetype;
    }

    public void setNotetype(String notetype) {
        this.notetype = notetype;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }
}

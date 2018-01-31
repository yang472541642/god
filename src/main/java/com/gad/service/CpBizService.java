/*
 * @(#) CpBizService.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.service;

import com.gad.domin.dto.CpBuyResult;
import com.gad.domin.dto.OrderInfo;
import com.gad.domin.dto.PlanConfig;
import com.gad.domin.dto.UserInfoDTO;
import com.gad.domin.dto.WithdrawResult;
import com.gad.domin.entity.OptimalPlanEntity;

import java.util.List;

/**
 * @author caojianlong
 * @title CpBizService
 * @date 2017/11/10
 * @since v1.0.0
 */
public interface CpBizService {

    public static final String LOGIN_PER = "";

    public static final String LOGIN_URL = "http://www.htdbill.com/a7/index.php/Home/user/user_login?username=jlcaold&password=cjl584520&captcha=7549";
    //{"errno":0,"msg":"login","data":{"uid":"42588","name":"jlcaold","rebate":"0.025","money":"38.7000","type":"agent","lastipaddreess":"106.91.49.83","nowtime":"1510202118","qq":0,"safepwd":1,"urlrebate":"","status":"0","safequestion":0,"token":"a32063970jto2vadl010tvq9l1","host":"http:\/\/106.15.91.183:8080\/chess\/index.php\/Home\/index\/setCookieName\/value\/15105303544258827494\/token\/151053035442588\/uid\/42588","sysTime":1510530355,"safeLevel":"\u9ad8"}}

    public static final String INSERT = "http://www.htdbill.com/a7/index.php/Home/trace/Insert";
    //{"errno":0,"msg":"追号成功","data":false}

    public static final String GET_ORDERLIST = "http://www.htdbill.com/a7/index.php/Home/Search/noteTodaySearchByUid?uid=42588&game=cqssc";
    //{"errno":0,"msg":null,"data":[{"id":"117111p1csc","uid":"42588","ticketnum":"20171113024","tickettype":"重庆时时彩","ticketstar":"定位胆","ticketkind":"直猜万位","note":"01234","multiple":"1","pattern":"角","count":"5","time":"1510528269","status":"0","cost":1,"odds":"0","winnum":null,"winmoney":"0.0000","notetype":"1","isdelete":"0"},{"id":"117111p1cs3","uid":"42588","ticketnum":"20171113025","tickettype":"重庆时时彩","ticketstar":"定位胆","ticketkind":"直猜万位","note":"01234","multiple":"2","pattern":"角","count":"5","time":"1510528269","status":"0","cost":2,"odds":"0","winnum":null,"winmoney":"0.0000","notetype":"1","isdelete":"0"},{"id":"117111p1cs5","uid":"42588","ticketnum":"20171113026","tickettype":"重庆时时彩","ticketstar":"定位胆","ticketkind":"直猜万位","note":"01234","multiple":"4","pattern":"角","count":"5","time":"1510528269","status":"0","cost":4,"odds":"0","winnum":null,"winmoney":"0.0000","notetype":"1","isdelete":"0"}]}

    public static final String CANCEL = "http://www.htdbill.com/a7/index.php/Home/Note/cancelNote?noteId=117111p1cs5&uid=42588";
    // {"errno":0,"msg":"117111p1cs5ce撤单成功","data":null}

    public static final String GET_BALANCE = "http://www.htdbill.com/a7/index.php/Home/user/getUserBalance?uid=42588";
    // {"errno":0,"msg":null,"data":"45.7000"}

    /**
     * 购买
     * @param optimalPlan
     * @param config
     * @return
     */
    CpBuyResult buy(UserInfoDTO userInfoDTO, OptimalPlanEntity optimalPlan, PlanConfig config);

    /**
     * 登录
     * @param userName
     * @param password
     */
    UserInfoDTO login(String userName, String password);

    /**
     * 撤单
     * @param id
     * @return
     */
    WithdrawResult withdraw(UserInfoDTO userInfoDTO,String id);

    /**
     * 获取订单列表
     * @param userInfoDTO
     * @return
     */
    List<OrderInfo> getOrderList(UserInfoDTO userInfoDTO);


    /**
     * 获取余额
     * @param userInfoDTO
     * @return
     */
    Double getBalance(UserInfoDTO userInfoDTO);

}

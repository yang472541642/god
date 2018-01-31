/*
 * @(#) CpBizServiceImpl.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gad.common.Constant;
import com.gad.common.HttpHelper;
import com.gad.common.MoneyTypeEnum;
import com.gad.common.PlayConfigUtil;
import com.gad.domin.AutoPay;
import com.gad.domin.PlayTypeEnum;
import com.gad.domin.dto.CpBuyResult;
import com.gad.domin.dto.OrderInfo;
import com.gad.domin.dto.PlanConfig;
import com.gad.domin.dto.UserInfoDTO;
import com.gad.domin.dto.WithdrawResult;
import com.gad.domin.entity.OptimalPlanEntity;
import com.gad.handler.PlanNoticeHandler;
import com.gad.handler.PlayHandler;
import com.gad.handler.PlayHandlerThreadPool;
import com.gad.service.CpBizService;
import com.gad.service.CpxzsBizService;
import com.gad.service.NoticeService;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
@Service
public class CpBizServiceImpl implements CpBizService {
    private static final Logger logger = LoggerFactory.getLogger(CpBizServiceImpl.class);

    private static final String LANG_OPTION = "-l";

    private static final String EOL = System.getProperty("line.separator");

    /**
     * 一次购买的
     */
    @Value("${play-type}")
    private String playType;

    /**
     * 购入单位
     */
    @Value("${unit}")
    private String unit;

    /**
     * 跟入轮数
     */
    @Value("${play-round-num}")
    private String playRoundNum;

    /**
     * 每次购买的号码数量
     */
    @Value("${buy-num}")
    private String buyNum;

    /**
     * 计划
     */
    @Value("${plan-round-num}")
    private String planRoundNum;

    /**
     * 赔率
     */
    @Value("${play-odds}")
    private String playOdds;


    @Resource
    private CpxzsBizService bizService;


    @Resource
    private PlayHandlerThreadPool playHandlerThreadPool;
    
    @Resource
    private NoticeService noticeService;
    private String cqsscCokie;


    @Override
    public CpBuyResult buy(UserInfoDTO userInfoDTO, OptimalPlanEntity optimalPlan, PlanConfig config) {
        return null;
    }


    @Override
    public UserInfoDTO login(String userName, String password) {
        if (StringUtils.isEmpty(playType)) {
            throw new IllegalArgumentException("配置错误 请配置购买计划");
        }
        String[] playTypeList = playType.split(",");
        String[] playOddsList = playOdds.split(",");
        String[] playRoundNumList = playRoundNum.split(",");
        try {
            //this.validateAccount(userName, password);
        } catch (Exception e) {
            logger.error("登录失败", e);
        }
        try {
            /*for (int i = 1; i <= playTypeList.length; i++) {
                Integer playType = Integer.valueOf(playTypeList[i - 1]);
                Integer playRoundNum = Integer.valueOf(playRoundNumList[i - 1]);
                PlayTypeEnum playTypeEnum = PlayTypeEnum.getByKey(playType);
                PlanConfig planConfig = PlayConfigUtil.genPlanConfig(playTypeEnum, Integer.valueOf(buyNum), Integer.valueOf(planRoundNum));
                // 购买配置
                AutoPay autoPay = new AutoPay();
                autoPay.setPlayOdds(Integer.valueOf(playOddsList[i - 1]));
                autoPay.setPlanConfig(planConfig);
                autoPay.setMoneyTypeEnum(MoneyTypeEnum.getByMultiple(unit));
                autoPay.setPlayTypeEnum(playTypeEnum);
                autoPay.setPlayRoundNum(playRoundNum);
                playHandlerThreadPool.putDealFailedTask(new PlayHandler(autoPay, bizService, this.getCqsscCokie()));
            }*/
            Thread thread = new Thread(new PlanNoticeHandler(bizService, noticeService, cqsscCokie, playHandlerThreadPool));
            thread.start();
        } catch (Exception e) {
            logger.error("购买失败", e);
        }
        return null;
    }

    @Override
    public WithdrawResult withdraw(UserInfoDTO userInfoDTO, String id) {
        return null;
    }

    @Override
    public List<OrderInfo> getOrderList(UserInfoDTO userInfoDTO) {
        return null;
    }

    @Override
    public Double getBalance(UserInfoDTO userInfoDTO) {
        return null;
    }

    /***
     * 登陆
     * @param userName
     * @param pwd
     * @throws IOException
     */
    private void validateAccount(String userName, String pwd) throws IOException {

        // cookie store default
        HttpHelper httpHelper = HttpHelper.create().get(Constant.host).execute();
        String firset = httpHelper.getResponseContent();
        Header[] headers = httpHelper.getResponseHeaders();
        String cp_yj_us = "";
        String cp_yj_ucc = "";
        String cp_yj_cce = "";

        for (Header header : headers) {
            String name = header.getName();
            String value = header.getValue();
            if (name.equals("Set-Cookie") && value.startsWith("cp_yj_us=")) {
                cp_yj_us = value.substring(value.indexOf("cp_yj_us=") + "cp_yj_us=".length(), value.indexOf(";"));
            }
            if (name.equals("Set-Cookie") && value.startsWith("cp_yj_ucc=")) {
                cp_yj_ucc = value.substring(value.indexOf("cp_yj_ucc=") + "cp_yj_ucc=".length(), value.indexOf(";"));
            }
            if (name.equals("Set-Cookie") && value.startsWith("cp_yj_cce=")) {
                cp_yj_cce = value.substring(value.indexOf("cp_yj_cce=") + "cp_yj_cce=".length(), value.indexOf(";"));
            }
        }
        String cookie = "cp_yj_cce=" + cp_yj_cce + "; " +
                "cp_yj_ucc=" + cp_yj_ucc + "; " +
                "cp_yj_us=" + cp_yj_us;
        this.setCqsscCokie(cookie);
        HttpHelper post = HttpHelper.create()
                .post(Constant.loginUrl)
                .addHeader("Cookie", cookie)
                .addParam("loginName", userName)
                .addParam("password", pwd)
                .addParam("autoLogin", "1")
                .addParam("charCaptcha", "")
                .addParam("geetsCheck", "")
                .execute();

        String responseContent = post.getResponseContent();
        JSONObject validateObj = JSON.parseObject(responseContent);
        if (validateObj != null && Boolean.TRUE.equals(validateObj.get("success"))) {
            Header[] cqsscCokie = post.getResponseHeaders();
            StringBuilder ck = new StringBuilder();
            for (Header header : cqsscCokie) {
                if (header.getName().equals("Set-Cookie")) {
                    String value = header.getValue();
                    ck.append(value.substring(0, value.indexOf(";") + 1));
                }
            }
            this.setCqsscCokie(this.getCqsscCokie() + ";" + ck.toString());
//            if (Constant.cookiePath != null) {
//                return;
//            }
//            CookieUtil.serializeObject(httpRequestDao.getHttpClientContext().getCookieStore(), Constant.cookiePath);
        } else {
            throw new RuntimeException("登录失败");
        }

    }

    private String getCqsscCokie() {
        return cqsscCokie;
    }

    private void setCqsscCokie(String cqsscCokie) {
        this.cqsscCokie = cqsscCokie;
    }
}

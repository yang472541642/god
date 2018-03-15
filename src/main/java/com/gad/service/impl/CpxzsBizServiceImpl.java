/*
 * @(#) CpxzsBizServiceImpl.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gad.common.Constant;
import com.gad.common.HttpHelper;
import com.gad.domin.dto.*;
import com.gad.domin.entity.OptimalPlanEntity;
import com.gad.handler.GetPlanResultHandler;
import com.gad.service.CpxzsBizService;
import com.gad.service.XzsCookieGetService;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/10
 */
@Service
public class CpxzsBizServiceImpl implements CpxzsBizService {

    private Boolean isLogin = Boolean.FALSE;

    private String userName = "YuYuYuYu";

    private String password = "cqssc123456";


    private String nowPlanCode = "ssc040";

    private String nowPeriod= "";

    private Map<PlanConfig, List<PlanDetailDTO>> nowPlanMap = new ConcurrentHashMap<>();

    private Boolean newPeriodFlage = Boolean.TRUE;

    @Autowired
    private XzsCookieGetService cookieGet;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostConstruct
    public void init(){
        if (!isLogin) {
            try {
                this.login("YuYuYuYu", "cqssc123456");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public RemotePlanDTO getPlanList(PlanConfig config) throws IOException {
        String content = getPvList(config, null);
        RemotePlanDTO planDTO = JSONObject.parseObject(content, RemotePlanDTO.class);
        return planDTO;
    }

    @Override
    public RemotePlanDTO getPlanList(PlanConfig config,String nowPlanCode){
        String content = null;
        try {
            content = getPvList(config,nowPlanCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RemotePlanDTO planDTO = JSONObject.parseObject(content, RemotePlanDTO.class);
        return planDTO;
    }

    @Override
    public PeriodNumberDTO getCurrentNumberInfo() {
        String content = null;
        try {
            content = HttpHelper.create().get(Constant.XZS_KJXX)
                    .addHeader("Referer", "http://www.cpxzs.com/")
                    .addHeader("Cookie", cookieGet.getCookie())
                    .execute()
                    .getResponseContent();
        } catch (IOException e) {
            logger.error("获取中心信息失败！");
        }
        PeriodNumberDTO periodInfo = JSONObject.parseObject(content, PeriodNumberDTO.class);
        return periodInfo;
    }
    
    @Override
    public List<String> getHistoryNumber(String date, List<String> list) {
        String content = null;
        try {
            content = HttpHelper.create().get("http://www.cpxzs.com/historyNumber/"+date+".html")
                    .addHeader("Referer", "http://www.cpxzs.com/historyNumber/"+date+".html")
                    .addHeader("Cookie", cookieGet.getCookie())
                    .execute()
                    .getResponseContent();
        } catch (IOException e) {
            logger.error("获取中心信息失败！");
            return new ArrayList<>();
        }
        Document document = Jsoup.parse(content);
        Elements elements1 = document.select("table").select("tr");
        for(int i = 1;i<elements1.size();i++){
            Elements tds = elements1.get(i).select("td");
            String text = tds.get(1).text();
            if(!StringUtils.isEmpty(text)) {
                list.add(text);
            }
        }
        for(int i = 1;i<elements1.size();i++){
            Elements tds = elements1.get(i).select("td");
            String text = tds.get(6).text();
            if(!StringUtils.isEmpty(text)) {
                list.add(text);
            }
        }
    
        for(int i = 1;i<elements1.size();i++){
            Elements tds = elements1.get(i).select("td");
            String text = tds.get(11).text();
            if(!StringUtils.isEmpty(text)) {
                list.add(text);
            }
        }
        return list;
    }
    
    
    private void login(String userName, String password) throws Exception {
        cookieGet.chk();

        Thread.sleep(1000);
        HttpHelper helper = HttpHelper.create();
        HttpResponse response = helper.post(Constant.XZS_LOGIN_URL)
                .addHeader("Origin", "http://www.cpxzs.com")
                .addHeader("Referer", "http://www.cpxzs.com/")
                .addHeader("Cookie", cookieGet.getCookie())
                .addParam("loginName", userName)
                .addParam("password", password)
                .addParam("autoLogin", "1")
                .execute()
                .getResponse();

        Thread.sleep(1000);
        Assert.assertTrue("小助手登录返回码不对 [" + response.getStatusLine().getStatusCode() + "]", response.getStatusLine().getStatusCode() == 200);
        Header[] headers = response.getHeaders("Set-Cookie");
        Assert.assertTrue("小助手登录返回 header 长度不对", headers.length > 0);
        cookieGet.appendCookie(headers);
        System.out.println(helper.getResponseContent());
        JSONObject json = (JSONObject) JSONObject.parse(helper.getResponseContent());
        if (json.getBoolean("success")) {
            logger.info("小助手登录成功！");
        }
    }





    /**
     * 查询下一期计划
     * @return
     * @throws IOException
     */
    @Override
    public NextPeriodInfo getNextPeriod() throws IOException {
        String content = HttpHelper.create().get(Constant.XZS_NEXT_PERIOD)
                .addHeader("Referer", "http://www.cpxzs.com/jhfa/excellentPlan")
                .addHeader("Cookie", cookieGet.getCookie())
                .execute()
                .getResponseContent();
        NextPeriodInfo periodInfo = JSONObject.parseObject(content, NextPeriodInfo.class);
        return periodInfo;
    }

    @Override
    public PlanDetailDTO getPlanCode(PlanConfig config, String nowPlanCode) throws IOException {
        logger.info("获取计划 {} ，{} ，{} ", nowPlanCode, config.getType().getCode(), config.getOffset());
        return doGetPlanDetail(config, nowPlanCode);
    }

    private PlanDetailDTO doGetPlanDetail(PlanConfig config, String nowPlanCode) {
        RemotePlanDTO planList = this.getPlanList(config, nowPlanCode);
        PlanDetailDTO detailDTO = null;
        for (PlanDetailDTO dto : planList.getExpressionName()) {
            if (dto.getJhfaCode().equals(nowPlanCode)) {
                detailDTO = dto;
            }
        }

        detailDTO.setFirstPlanResult(planList.getFirstPlanResult());
        detailDTO.setFvList(planList.getFvList());
        return detailDTO;
    }

    /**
     *
     * @param config 计划配置
     * @param nowPlanCode  当前计划编码
     * @return
     * @throws IOException
     */
    public String getPvList(PlanConfig config, String nowPlanCode) throws IOException {
        String url = String.format(Constant.XZS_PLAN_URL, config.getType().getCode(), config.getOffset(), config.getNumber(), config.getKeepPeriosd(), nowPlanCode, System.currentTimeMillis() + "");
        String content = HttpHelper.create().get(url)
                .addHeader("Referer", "http://www.cpxzs.com/jhfa/excellentPlan")
                .addHeader("Cookie", cookieGet.getCookie())
                .execute()
                .getResponseContent();
        return content;
    }



    @Override
    public List<PlanDetailDTO> getAllPlanCode(PlanConfig config) throws IOException, InterruptedException {
        NextPeriodInfo nextPeriod = getNextPeriod();

        List<PlanDetailDTO> nowPlanlist = null;

        if (nowPeriod.equals(nextPeriod.getNextPeriodStr()) && nowPlanMap.containsKey(config)) {
            return nowPlanMap.get(config);
        }

        nowPeriod = nextPeriod.getNextPeriodStr();

        RemotePlanDTO planList = this.getPlanList(config);
        nowPlanlist = planList.getExpressionName();
        List<Thread> threadList = new ArrayList<>();

        for (PlanDetailDTO detailDTO : nowPlanlist) {
            Thread t = new Thread(new GetPlanResultHandler(detailDTO, this, config, nextPeriod));
            t.start();
            threadList.add(t);
        }

        Thread.sleep(3000);
        for (Thread thread : threadList) {
            thread.join();
        }

        nowPlanMap.put(config, nowPlanlist);

        return nowPlanlist;
    }

    @Override
    public List<PlanDetailDTO> getOptimalPlan(PlanConfig config, Integer top) {
        return null;
    }


    public static void main(String[] args) throws Exception {

        CpxzsBizServiceImpl service = new CpxzsBizServiceImpl();

        service.login("YuYuYuYu", "cqssc123456");
        //Thread.sleep(1000);
        System.out.println();
        System.out.println();
        PlanConfig config = new PlanConfig();
        config.setKeepPeriosd(3);
        config.setNumber(5);
        config.setOffset(1);

        //RemotePlanDTO planList = service.getPlanList(config);

        List<PlanDetailDTO> list = service.getAllPlanCode(config);

        System.out.println("===============" + list);

        System.out.println("===============" + service.getNextPeriod());

        //login("YuYuYuYu", "cqssc123456");
        /*Thread.sleep(1000);
        System.out.println();
        System.out.println();
        String str = getPvList(5, 5, 3, "ssc022");
        System.out.println("===============" + str);
        RemotePlanDTO planDTO = JSONObject.parseObject(str, RemotePlanDTO.class);
        System.out.println();
        System.out.println("==============="+getNextPeriod());*/
    }


}

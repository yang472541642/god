/*
 * @(#) CpxzsBizService.java ,2017年11月10日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.service;

import com.gad.domin.dto.*;
import com.gad.domin.entity.OptimalPlanEntity;

import java.io.IOException;
import java.util.List;

/**
 * @author caojianlong
 * @title CpxzsBizService
 * @date 2017/11/10
 * @since v1.0.0
 */
public interface CpxzsBizService {

    /**
     * 下一场开场信息
     * @return
     * @throws IOException
     */
    public NextPeriodInfo getNextPeriod() throws IOException;


    /**
     * 获取制定计划
     * @param config  配置
     * @param nowPlanCode  计划编码
     * @return
     * @throws IOException
     */
    PlanDetailDTO getPlanCode(PlanConfig config, String nowPlanCode) throws IOException;

    /**
     * 拉取所有计划列表
     * @param config
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public List<PlanDetailDTO> getAllPlanCode(PlanConfig config) throws IOException, InterruptedException;

    /**
     * 拉取最优计划
     *
     * @param config  配置
     * @param top  前几
     * @return
     */
    List<PlanDetailDTO> getOptimalPlan(PlanConfig config, Integer top);


    /**
     * 拉取指定计划
     * @param config
     * @param nowPlanCode
     * @return
     */
    public RemotePlanDTO getPlanList(PlanConfig config, String nowPlanCode);

    /**
     * 获取最近一场次的开奖信息
     * @return
     */
    public PeriodNumberDTO getCurrentNumberInfo();
    
    public List<String> getHistoryNumber(String date, List<String> list);

}

/*
 * @(#) GetPlanResultHandler.java ,2017年11月29日
 *
 * Copyright 2017 zbj.com, Inc. ALL rights reserved.
 * ZHUBAJIE PROPRIETARY/CONFIDENTIAL. Use is subject to lincese trems.
 */
package com.gad.handler;

import com.gad.domin.dto.FvDTO;
import com.gad.domin.dto.HistoryPlanDetailDTO;
import com.gad.domin.dto.NextPeriodInfo;
import com.gad.domin.dto.PlanConfig;
import com.gad.domin.dto.PlanDetailDTO;
import com.gad.domin.dto.RemotePlanDTO;
import com.gad.service.CpxzsBizService;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author caojianlong(caojianlong@zbj.com)
 * @date 2017/11/29
 */
public class GetPlanResultHandler implements Runnable {

    private final CpxzsBizService service;
    private final PlanDetailDTO detailDTO;
    private final PlanConfig config;
    private final NextPeriodInfo nextPeriod;


    public GetPlanResultHandler(PlanDetailDTO detailDTO, CpxzsBizService service, PlanConfig config, NextPeriodInfo nextPeriod) {
        this.detailDTO = detailDTO;
        this.service = service;
        this.config = config;
        this.nextPeriod = nextPeriod;
    }



    @Override
    public void run() {
        RemotePlanDTO tmp = service.getPlanList(config, detailDTO.getJhfaCode());

        String nowPeriod = getNowPeriod(nextPeriod.getNextPeriodStr().split("-")[1]);

        List<FvDTO> fvList = tmp.getFvList();

        Boolean isTurn = Boolean.TRUE;

        for (FvDTO fv : fvList) {
            if (!StringUtils.isEmpty(fv.getResult()) && fv.getItem().equals(nowPeriod)) {
                detailDTO.setLastKjNum(fv.getResult());
                isTurn = Boolean.FALSE;
            }
        }

        if (isTurn) {
            List<HistoryPlanDetailDTO> his = tmp.getResultList();
            for (HistoryPlanDetailDTO dto : his) {
                String [] spilted = dto.getItem().split("-");
                Integer begin = Integer.parseInt(spilted[0]);
                Integer end = Integer.parseInt(spilted[1]);
                Integer now = Integer.parseInt(nowPeriod);

                if (now == 120 && end <= 2 || now >= begin && now <= end) {
                    detailDTO.setLastKjNum(dto.getCaipiaoList().get(0).getResult());
                    isTurn = dto.getStatus();
                    break;
                }

            }
        }

        detailDTO.setLastIsSuccess(isTurn);
        detailDTO.setFirstPlanResult(tmp.getFirstPlanResult());

        detailDTO.setFvList(tmp.getFvList());
    }

    private String getNowPeriod(String s) {
        Integer next = Integer.parseInt(s);

        Integer now = 1;
        if (next == 1) {
            now = 120;
        } else {
            now = next - 1;
        }


        String nowStr = now.toString();

        if (now < 10) {
            nowStr = "00" + nowStr;
        }
        if (now < 100) {
            nowStr = "0" + nowStr;
        }

        return nowStr;
    }
}

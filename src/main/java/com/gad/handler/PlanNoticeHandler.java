package com.gad.handler;

import com.gad.common.MoneyTypeEnum;
import com.gad.domin.AutoPay;
import com.gad.domin.PlayTypeEnum;
import com.gad.domin.dto.HistoryPlanDetailDTO;
import com.gad.domin.dto.MessageDTO;
import com.gad.domin.dto.NextPeriodInfo;
import com.gad.domin.dto.PeriodNumberDTO;
import com.gad.domin.dto.PlanConfig;
import com.gad.domin.dto.PlanDetailDTO;
import com.gad.domin.dto.RemotePlanDTO;
import com.gad.service.CpBizService;
import com.gad.service.CpxzsBizService;
import com.gad.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 计划关键节点提醒
 *
 * @author ycp
 * @date 2017/12/13
 */
public class PlanNoticeHandler implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(PlanNoticeHandler.class);
    
    private CpxzsBizService cpxzsBizService;
    
    private NoticeService noticeService;
    
    private String sscCookie;
    
    private PlayHandlerThreadPool playHandlerThreadPool;
    
    private static final int MIN_PLAN_SIZE = 3;
    
    private volatile AtomicInteger totalActiveThreadCount;
    
    private int totalPushedPlanCount = 0;
    
    private static final int MAX_ACTIVE_THREAD_SIZE = 4;
    
    private static final int MAX_HANDLED_THREAD_SIZE = 10;
    
    
    
    
    public PlanNoticeHandler(CpxzsBizService cpxzsBizService, NoticeService noticeService,
                             String sscCookie, PlayHandlerThreadPool playHandlerThreadPool) {
        this.cpxzsBizService = cpxzsBizService;
        this.noticeService = noticeService;
        this.sscCookie = sscCookie;
        this.playHandlerThreadPool = playHandlerThreadPool;
        this.totalActiveThreadCount = new AtomicInteger(0);
    }
    @Override
    public void run() {
        listenAll();
    }
    
    
    private void listenAll(){
        while (true) {
            Calendar c = Calendar.getInstance();
            //如果是白天的10点到-晚上10点，计划每十分钟一次；晚上十点到凌晨两点，五分钟一次
            boolean isNeedCheck = ((c.get(Calendar.HOUR_OF_DAY) < 22 && c.get(Calendar.HOUR_OF_DAY) >= 10) && (c.get(Calendar.MINUTE) % 10 == 2))
                    || ((c.get(Calendar.HOUR_OF_DAY) < 2 || c.get(Calendar.HOUR_OF_DAY) >= 22) && (c.get(Calendar.MINUTE) % 5 == 2));
            if(!isNeedCheck){
                try {
                    logger.info("开始睡眠20s");
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                
                logger.info("HOUR={},MINUT={}", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                //选取连续出现5次的数据
    
                List<String> list = new ArrayList<>();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                cpxzsBizService.getHistoryNumber(df.format(new Date()), list);
                if(list.size() < 6) {
                    continue;
                }
                List<String> list0 = list.subList(list.size() - 6, list.size());
                
                List<String> list1 = list0.subList(0, 1);
                List<String> list2 = list0.subList(1, list0.size());
                checkAndSendEmail(list1, list2, 5);
    
    
                if(list.size() < 7) {
                    continue;
                }
                List<String> list3 = list.subList(list.size() - 7, list.size());
    
                List<String> list4 = list3.subList(0, 1);
                List<String> list5 = list3.subList(1, list3.size() - 1);
                List<String> list6 = list3.subList(list3.size() - 1, list3.size());
                checkIsCanCheckInAndSendEmail(list4, list5, list6, 5);
                
                
                
                
                
                
                /*try {
                    //获取所有的计划
                    logger.info("开始计划计划");
                    PlanConfig planConfig = new PlanConfig();
                    
                    AutoPay autoPay = new AutoPay();
                    autoPay.setPlayOdds(1);
                    autoPay.setPlanConfig(planConfig);
                    autoPay.setMoneyTypeEnum(MoneyTypeEnum.YUAN);
                    autoPay.setPlayTypeEnum(PlayTypeEnum.POSITION_ONE);
                    String nextBuyNum = getNextBuyNum();
                    playHandlerThreadPool.putDealFailedTask(new AutoBuyHandler(autoPay, cpxzsBizService,
                            planConfig, sscCookie, noticeService, totalActiveThreadCount, nextBuyNum));
                    addTotalActiveThreadCount();
                } catch (Exception e) {
                    logger.error("验证计划是否有可能发生异常，异常信息：{}", e.getMessage());
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setSubject("系统异常");
                    messageDTO.setContent("验证计划是否有可能发生异常，异常信息：" + e.getMessage());
                    noticeService.notice(messageDTO);
                }*/
    
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void checkAndSendEmail(List<String> list1, List<String> list2, int num) {
        try {
            NextPeriodInfo nextPeriodInfo = cpxzsBizService.getNextPeriod();
            String nextStr = "";
            if(nextPeriodInfo != null) {
                nextStr = nextPeriodInfo.getNextPeriodStr();
            }
            for(int weizhi = 0; weizhi<5; weizhi++) {
                if(checkIsTheSame(list1, list2, weizhi, 1)) {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setSubject(nextStr + "连出预警" + num + ","+ weizhi + "-异常大");
                    messageDTO.setContent("位置：" + weizhi + "类型：大");
                    noticeService.notice(messageDTO);
                }
                if(checkIsTheSame(list1, list2, weizhi, 2)) {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setSubject(nextStr + "连出预警" + num + ","+ weizhi + "-异常小");
                    messageDTO.setContent("位置：" + weizhi + "类型：小");
                    noticeService.notice(messageDTO);
                }
                if(checkIsTheSame(list1, list2, weizhi, 3)) {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setSubject(nextStr + "连出预警" + num + ","+ weizhi + "-异常单");
                    messageDTO.setContent("位置：" + weizhi + "类型：单");
                    noticeService.notice(messageDTO);
                }
                if(checkIsTheSame(list1, list2, weizhi, 4)) {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setSubject(nextStr + "连出预警" + num + ","+ weizhi + "-异常双");
                    messageDTO.setContent("位置：" + weizhi + "类型：双");
                    noticeService.notice(messageDTO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    private void checkIsCanCheckInAndSendEmail(List<String> list1, List<String> list2, List<String> list3,  int num) {
        try {
            NextPeriodInfo nextPeriodInfo = cpxzsBizService.getNextPeriod();
            String nextStr = "";
            if(nextPeriodInfo != null) {
                nextStr = nextPeriodInfo.getNextPeriodStr();
            }
            for(int weizhi = 0; weizhi<5; weizhi++) {
                if(checkIsCanCheckIn(list1, list2, list3, weizhi, 1)) {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setSubject(nextStr + "关键点预警" + num + ","+ weizhi + "-关键点出现");
                    messageDTO.setContent("位置：" + weizhi + "类型：大");
                    noticeService.notice(messageDTO);
                }
                if(checkIsCanCheckIn(list1, list2, list3, weizhi, 2)) {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setSubject(nextStr + "关键点预警" + num + ","+ weizhi + "-关键点出现");
                    messageDTO.setContent("位置：" + weizhi + "类型：小");
                    noticeService.notice(messageDTO);
                }
                if(checkIsCanCheckIn(list1, list2, list3, weizhi, 3)) {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setSubject(nextStr + "关键点预警" + num + ","+ weizhi + "-关键点出现");
                    messageDTO.setContent("位置：" + weizhi + "类型：单");
                    noticeService.notice(messageDTO);
                }
                if(checkIsCanCheckIn(list1, list2, list3, weizhi, 4)) {
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setSubject(nextStr + "关键点预警" + num + ","+ weizhi + "-关键点出现");
                    messageDTO.setContent("位置：" + weizhi + "类型：双");
                    noticeService.notice(messageDTO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    private Boolean checkIsCanCheckIn(List<String> list1, List<String> list2, List<String> list3, int weizhi, int type) {
        if(type == 1) {
            //大
            if(Integer.valueOf(String.valueOf(list1.get(0).charAt(weizhi))) > 4) {
                return false;
            }
            if(Integer.valueOf(String.valueOf(list3.get(0).charAt(weizhi))) > 4) {
                return false;
            }
            for(String s : list2) {
                if(Integer.valueOf(String.valueOf(s.charAt(weizhi))) < 5) {
                    return false;
                }
            }
            return true;
        }
        if(type == 2) {
            //小
            if(Integer.valueOf(String.valueOf(list1.get(0).charAt(weizhi))) < 5) {
                return false;
            }
            if(Integer.valueOf(String.valueOf(list3.get(0).charAt(weizhi))) < 5) {
                return false;
            }
            for(String s : list2) {
                if(Integer.valueOf(String.valueOf(s.charAt(weizhi))) > 4) {
                    return false;
                }
            }
            return true;
        }
        if(type == 3) {
            //单
            if(Integer.valueOf(String.valueOf(list1.get(0).charAt(weizhi))) % 2 == 1) {
                return false;
            }
            if(Integer.valueOf(String.valueOf(list3.get(0).charAt(weizhi))) % 2 == 1) {
                return false;
            }
            for(String s : list2) {
                if(Integer.valueOf(String.valueOf(s.charAt(weizhi))) % 2 == 0) {
                    return false;
                }
            }
            return true;
        }
        if(type == 4) {
            //双
            if(Integer.valueOf(String.valueOf(list1.get(0).charAt(weizhi))) % 2 == 0) {
                return false;
            }
            if(Integer.valueOf(String.valueOf(list3.get(0).charAt(weizhi))) % 2 == 0) {
                return false;
            }
            for(String s : list2) {
                if(Integer.valueOf(String.valueOf(s.charAt(weizhi))) % 2 == 1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private Boolean checkIsTheSame(List<String> list1, List<String> list2, int weizhi, int type) {
        if(type == 1) {
            //大
            if(Integer.valueOf(String.valueOf(list1.get(0).charAt(weizhi))) > 4) {
                return false;
            }
            for(String s : list2) {
                if(Integer.valueOf(String.valueOf(s.charAt(weizhi))) < 5) {
                    return false;
                }
            }
            return true;
        }
        if(type == 2) {
            //小
            if(Integer.valueOf(String.valueOf(list1.get(0).charAt(weizhi))) < 5) {
                return false;
            }
            for(String s : list2) {
                if(Integer.valueOf(String.valueOf(s.charAt(weizhi))) > 4) {
                    return false;
                }
            }
            return true;
        }
        if(type == 3) {
            //单
            if(Integer.valueOf(String.valueOf(list1.get(0).charAt(weizhi))) % 2 == 1) {
                return false;
            }
            for(String s : list2) {
                if(Integer.valueOf(String.valueOf(s.charAt(weizhi))) % 2 == 0) {
                    return false;
                }
            }
            return true;
        }
        if(type == 4) {
            //双
            if(Integer.valueOf(String.valueOf(list1.get(0).charAt(weizhi))) % 2 == 0) {
                return false;
            }
            for(String s : list2) {
                if(Integer.valueOf(String.valueOf(s.charAt(weizhi))) % 2 == 1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private String getNextBuyNum() {
        PeriodNumberDTO periodNumberDTO = cpxzsBizService.getCurrentNumberInfo();
        Map<Integer, String> map1 = new HashMap<>();
        map1.put(0, "036");
        map1.put(1, "147");
        map1.put(2, "258");
        map1.put(3, "369");
        map1.put(4, "047");
        map1.put(5, "158");
        map1.put(6, "269");
        map1.put(7, "037");
        map1.put(8, "148");
        map1.put(9, "259");
        
        return map1.get(periodNumberDTO.getCurrentNumberArr()[4]);
    }
    
    
    /**
     * 监听时时彩计划出现挂
     */
    private void listenFailed(){
        while (true) {
            Calendar c = Calendar.getInstance();
            //如果是白天的10点到-晚上10点，计划每十分钟一次；晚上十点到凌晨两点，五分钟一次
            boolean isNeedCheck = ((c.get(Calendar.HOUR_OF_DAY) < 22 && c.get(Calendar.HOUR_OF_DAY) >= 10) && (c.get(Calendar.MINUTE) % 10 == 2))
                    || ((c.get(Calendar.HOUR_OF_DAY) < 2 || c.get(Calendar.HOUR_OF_DAY) >= 22) && (c.get(Calendar.MINUTE) % 5 == 2));
            if(!isNeedCheck){
                try {
                    logger.info("开始睡眠20s");
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("已经完成的计划数："+totalPushedPlanCount);
                logger.info("活跃当中的计划数："+totalActiveThreadCount);
                if(totalPushedPlanCount >= MAX_HANDLED_THREAD_SIZE) {
                    logger.info("已经达到最大线程数，可以休息了，再见");
                    break;
                }
                if(totalActiveThreadCount.intValue() >= MAX_ACTIVE_THREAD_SIZE) {
                    logger.info("当前已经达到最大活跃线程数，等待下次机会");
                    continue;
                }
                logger.info("HOUR={},MINUT={}", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
                try {
                    //获取所有的计划
                    logger.info("开始计划计划");
                    PlanConfig planConfig = new PlanConfig();
                    StringBuilder jhName = new StringBuilder();
                    List<PlanDetailDTO> planDetailDTOS = cpxzsBizService.getAllPlanCode(planConfig);
                    planDetailDTOS = planDetailDTOS.subList(0, 20);
                    for(PlanDetailDTO planDetailDTO : planDetailDTOS) {
                        RemotePlanDTO remotePlanDTO = cpxzsBizService.getPlanList(planConfig, planDetailDTO.getJhfaCode());
                        if(checkPlanIsNeedNotice(remotePlanDTO)) {
                            AutoPay autoPay = new AutoPay();
                            autoPay.setPlayOdds(2);
                            autoPay.setPlanConfig(planConfig);
                            autoPay.setPlanCode(planDetailDTO.getJhfaCode());
                            autoPay.setPlanName(planDetailDTO.getJhfaName());
                            autoPay.setMoneyTypeEnum(MoneyTypeEnum.YUAN);
                            autoPay.setPlayTypeEnum(PlayTypeEnum.POSITION_ONE);
                            if(totalPushedPlanCount >= MAX_HANDLED_THREAD_SIZE) {
                                logger.info("已经达到最大线程数，可以休息了，再见");
                                break;
                            }
                            if(totalActiveThreadCount.intValue() >= MAX_ACTIVE_THREAD_SIZE) {
                                logger.info("当前已经达到最大活跃线程数，等待下次机会");
                                break;
                            }
                            playHandlerThreadPool.putDealFailedTask(new AutoBuyHandler(autoPay, cpxzsBizService,
                                    planConfig, sscCookie, noticeService, totalActiveThreadCount, null));
                            addTotalActiveThreadCount();
                        }
                    }
                } catch (Exception e) {
                    logger.error("验证计划是否有可能发生异常，异常信息：{}", e.getMessage());
                    MessageDTO messageDTO = new MessageDTO();
                    messageDTO.setSubject("系统异常");
                    messageDTO.setContent("验证计划是否有可能发生异常，异常信息：" + e.getMessage());
                    noticeService.notice(messageDTO);
                }
            }
        }
    }
    
    
    /**
     * 递增count
     */
    private void addTotalActiveThreadCount(){
        synchronized (totalActiveThreadCount) {
            totalActiveThreadCount.addAndGet(1);
        }
        totalPushedPlanCount++;
    }
    
    /**
     * 验证当前计划是否有机会
     * @param remotePlanDTO
     * @return
     */
    private boolean checkPlanIsNeedNotice(RemotePlanDTO remotePlanDTO) {
        if(remotePlanDTO == null) {
            return false;
        }
        if(CollectionUtils.isEmpty(remotePlanDTO.getResultList()) || remotePlanDTO.getResultList().size() < MIN_PLAN_SIZE) {
            return false;
        }
        List<HistoryPlanDetailDTO> historyPlanDetailDTOS = remotePlanDTO.getResultList();
        /*boolean isValid = historyPlanDetailDTOS.get(0).getStatus()
                && historyPlanDetailDTOS.get(0).getCaipiaoList().size() == 1
                && !historyPlanDetailDTOS.get(1).getStatus()
                && historyPlanDetailDTOS.get(2).getStatus()
                && remotePlanDTO.getFvList().get(2).getResult() == null;*/
        boolean isValid = !historyPlanDetailDTOS.get(0).getStatus()
                && historyPlanDetailDTOS.get(1).getStatus()
                && remotePlanDTO.getFvList().get(2).getResult() == null;
        return isValid;
    }
}

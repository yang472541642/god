package com.gad.handler;

import com.alibaba.fastjson.JSON;
import com.gad.common.HttpHelper;
import com.gad.domin.AutoPay;
import com.gad.domin.dto.MessageDTO;
import com.gad.domin.dto.PeriodNumberDTO;
import com.gad.domin.dto.PlanConfig;
import com.gad.domin.dto.RemotePlanDTO;
import com.gad.service.CpxzsBizService;
import com.gad.service.NoticeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import static com.gad.common.Constant.ssc_host;

/**
 * 自动投注处理器
 * @author ycp
 * @date 2017/12/14
 */
public class AutoBuyHandler implements Runnable{
    
    private static final Logger logger = LoggerFactory.getLogger(AutoBuyHandler.class);
    
    private AutoPay autoPay;
    
    private CpxzsBizService bizService;
    
    private PlanConfig planConfig;
    
    private String sscCookie;
    
    private NoticeService noticeService;
    
    private static final int MAX_FOLLOW_COUNT = 3;
    
    private AtomicInteger totalActiveThreadCount;
    
    private String nextBuyNum;
    
    
    public AutoBuyHandler(AutoPay autoPay, CpxzsBizService cpxzsBizService,
                          PlanConfig planConfig, String sscCookie,
                          NoticeService noticeService, AtomicInteger totalActiveThreadCount, String nextBuyNum) {
        this.autoPay = autoPay;
        this.bizService =  cpxzsBizService;
        this.planConfig = planConfig;
        this.sscCookie = sscCookie;
        this.noticeService = noticeService;
        this.totalActiveThreadCount = totalActiveThreadCount;
        this.nextBuyNum = nextBuyNum;
    }
    
    @Override
    public void run() {
        while (!this.autoPay.isWin() && this.autoPay.getCount() < MAX_FOLLOW_COUNT) {
            Calendar c = Calendar.getInstance();
            //如果是白天的10点到-晚上10点，计划每十分钟一次；晚上十点到凌晨两点，五分钟一次
            boolean isNeedCheck = this.autoPay.getCount() == 0 || ((c.get(Calendar.HOUR_OF_DAY) < 22 && c.get(Calendar.HOUR_OF_DAY)>= 10) && (c.get(Calendar.MINUTE) % 10 == 2))
                    || ((c.get(Calendar.HOUR_OF_DAY) < 2 || c.get(Calendar.HOUR_OF_DAY)>= 22) && (c.get(Calendar.MINUTE) % 5 == 2));
            if(isNeedCheck) {
                String currentPer = getNextPer();
                if(autoPay.getLastPer() != null && autoPay.getLastPer().equals(currentPer)) {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                autoPay.setLastPer(currentPer);
                logger.info("开始计算购买：{},num:{}", JSON.toJSONString(autoPay), nextBuyNum);
                RemotePlanDTO remotePlanDTO = bizService.getPlanList(planConfig, this.autoPay.getPlanCode());
                remotePlanDTO.setExpressionName(null);
                
                logger.info("计划详情：{}", JSON.toJSONString(remotePlanDTO));
                //判断当前是否应该继续购买,并获取计划号码
                String numbers = checkIsNeedBuyAndReturnNumber(remotePlanDTO);
                logger.info("拿到的计划号码：{}", numbers);
                if(numbers == null) {
                    //验证是否本次跟单失败并发送邮件通知
                    //checkIsFailedAndNotice(remotePlanDTO);
                    //减少活跃线程数
                    //reduceTotalActiveCount();
                    //重置autopay
                    this.autoPay.setWin(true);
                    this.autoPay.setCount(MAX_FOLLOW_COUNT);
                    continue;
                }
                autoPay.setNumbers(numbers);
                //执行购入
                realBuy();
    
                try {
                    logger.info("购买完成，进入睡眠");
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 验证是否本次跟单失败并发送邮件通知
     * @param remotePlanDTO
     */
    private void checkIsFailedAndNotice(RemotePlanDTO remotePlanDTO) {
        if(autoPay.getCount() >= 6 && !remotePlanDTO.getResultList().get(0).getStatus()) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setSubject("失败告警");
            messageDTO.setContent(autoPay.getPlanCode());
            noticeService.notice(messageDTO);
        }
    }
    
    /**
     * 减少活跃的线程数
     */
    private void reduceTotalActiveCount() {
        synchronized (totalActiveThreadCount) {
            totalActiveThreadCount.addAndGet(-1);
        }
    }
    
    /**
     * 获取当前期号
     * @return
     */
    private String getNextPer() {
        // 获取期数号码
        HttpHelper httpHelper = null;
        try {
            httpHelper = HttpHelper.create()
                    .get(ssc_host)
                    .addHeader("Cookie", sscCookie).execute();
            Document parse = Jsoup.parse(httpHelper.getResponseContent());
            Element nextStrEle = parse.getElementById("nextPeriodStr");
            if (nextStrEle == null) {
                throw new RuntimeException("获取期数失败");
            }
            String nextStr = nextStrEle.text().trim();
            return nextStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private String checkIsNeedBuyAndReturnNumber(RemotePlanDTO remotePlanDTO) {
        
        /*if(this.autoPay.getCount() == 0) {
            return getOtherNum(remotePlanDTO.getFirstPlanResult());
        }
        if(this.autoPay.getCount() == 1 && remotePlanDTO.getResultList().get(0).getStatus() && remotePlanDTO.getFvList().get(2).getResult() == null) {
            return getOtherNum(remotePlanDTO.getFirstPlanResult());
        }
        if(this.autoPay.getCount() == 2 && remotePlanDTO.getResultList().get(0).getStatus() && remotePlanDTO.getFvList().get(2).getResult() == null) {
            return getOtherNum(remotePlanDTO.getFirstPlanResult());
        }
        if(this.autoPay.getCount() == 3 && remotePlanDTO.getResultList().get(0).getStatus() && remotePlanDTO.getFvList().get(2).getResult() == null) {
            return remotePlanDTO.getFirstPlanResult();
        }
        if(this.autoPay.getCount() == 4 && remotePlanDTO.getResultList().get(0).getStatus() && remotePlanDTO.getFvList().get(2).getResult() != null) {
            return remotePlanDTO.getFirstPlanResult();
        }
        if(this.autoPay.getCount() == 5 && remotePlanDTO.getResultList().get(0).getStatus() && remotePlanDTO.getFvList().get(1).getResult() != null && remotePlanDTO.getFvList().get(2).getResult() != null) {
            return remotePlanDTO.getFirstPlanResult();
        }*/
        if(this.autoPay.getCount() == 0) {
            return nextBuyNum;
        }
        PeriodNumberDTO periodNumberDTO = bizService.getCurrentNumberInfo();
        if(nextBuyNum.indexOf(String.valueOf(periodNumberDTO.getCurrentNumberArr()[4])) != -1) {
            return null;
        } else {
            return nextBuyNum;
        }
    }
    
    /**
     * 获取五码的反码
     * @param numbers
     * @return
     */
    private String getOtherNum(String numbers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 9; i++) {
            if(numbers.indexOf(String.valueOf(i)) == -1) {
                stringBuilder.append(i);
            }
        }
        return stringBuilder.toString();
    }
    
    private void realBuy() {
        int count = autoPay.getCount();
        autoPay.setCount(count + 1);
        String result = null;
        try {
            logger.info("开始进入pay");
            result = new PayUtils(autoPay, sscCookie).autoPay();
            logger.info("购买结果：{}", result);
        } catch (IOException e) {
            logger.info("执行购买发生异常：{}", e.getMessage());
        }
        logger.info("第 {} 次购买,购买期数:{} 购买号码:{},购买方案:{},购买计划:{} 购买结果:{},", autoPay.getCount(), autoPay.getNper(), autoPay.getNumbers(), autoPay.getPlayTypeEnum().getValue(), autoPay.getPlan(), result);
    }
    
    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 9; i++) {
            if("35678".indexOf(String.valueOf(i)) == -1) {
                stringBuilder.append(i);
            }
        }
        System.out.println(stringBuilder.toString());
    }
    
}

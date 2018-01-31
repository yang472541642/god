package com.gad.handler;

import com.alibaba.fastjson.JSON;
import com.gad.common.Constant;
import com.gad.domin.AutoPay;
import com.gad.domin.PlayResult;
import com.gad.domin.PlayTypeEnum;
import com.gad.domin.dto.NextPeriodInfo;
import com.gad.domin.dto.PeriodNumberDTO;
import com.gad.domin.dto.PlanConfig;
import com.gad.domin.dto.PlanDetailDTO;
import com.gad.service.CpxzsBizService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author zhongchao
 * @title PlayHandler.java
 * @Date 2017-12-03
 * @since v1.0.0
 */
public class PlayHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PlayHandler.class);

    private AutoPay autoPay;

    private CpxzsBizService bizService;

    private PlanConfig planConfig;

    private String sscCokie;

    private PlayResult playResult;

    public PlayHandler(AutoPay autoPay, CpxzsBizService bizService, String sscCokie) {
        this.autoPay = autoPay;
        this.bizService = bizService;
        this.planConfig = autoPay.getPlanConfig();
        this.sscCokie = sscCokie;
        this.init();
    }

    private void init() {
        playResult = new PlayResult();
        playResult.setPlayType(autoPay.getPlayTypeEnum().getValue());
        playResult.setChangePlan(false);
        playResult.setWinMoney(new BigDecimal(0));
        playResult.setWinNum(0);
        playResult.setFailedRoundNum(0);
        playResult.setPayMoney(new BigDecimal(0));
        AbstractPlayCenter.putCache(UUID.randomUUID().toString(), playResult);
        logger.info("购买计划:{} ,小助手号码获取计划方案:{}", autoPay.getPlayTypeEnum().getValue(), JSON.toJSONString(planConfig));
    }

    @Override
    public void run() {
        String num = "";
        while (true) {
            try {
                // 获取当前期数和当前期数
                NextPeriodInfo nextPeriod = bizService.getNextPeriod();
                Integer minute = nextPeriod.getMinute();
                //最后两分钟  中奖结果已出 不会出现重复购买的情况~
                if (minute > 2 && minute * 60 + nextPeriod.getSecond() >= 120) {
                    int i = (minute * 60 * 1000 + (nextPeriod.getSecond() * 1000) - (2 * 60 * 1000));
                    logger.info("时间还早 暂时不投注 休眠中....:{}s", i);
                    Thread.sleep(i);
                    continue;
                }
                // 最后6s 时间 太短了防止错误购买
                if (minute == 0 && nextPeriod.getSecond() < 6) {
                    logger.info("本期即将结束 等待下一期... ");
                    Thread.sleep(10000);
                    continue;

                }
                if (num.equals(nextPeriod.getNextPeriodStr())) {
                    // 购买完成后 休眠两分钟
                    Thread.sleep(2 * 60 * 1000);
                    continue;
                }
                PlanDetailDTO plan = null;

                if (StringUtils.isEmpty(autoPay.getPlan())) {
                    List<PlanDetailDTO> tmp = bizService.getAllPlanCode(planConfig);
                    plan = getPlan(tmp);
                    autoPay.setPlan(plan.getJhfaName());
                    autoPay.setPlanCode(plan.getJhfaCode());
                } else {
                    plan = bizService.getPlanCode(planConfig, autoPay.getPlanCode());
                }
                //PlanDetailDTO plan = getPlan(allPlanCode);
                if (playResult.isChangePlan()) {
                    playResult.setChangePlanNum(playResult.getPlayNum() + 1);
                    logger.info("改变计划 重新选择:{}", JSON.toJSONString(plan));
                }
                buy(plan);

                num = nextPeriod.getNextPeriodStr();


            } catch (Exception e) {
                logger.error("投注失败!", e);
            }

        }

    }

    /**
     * 获取 计划
     *
     * @param allPlanCode
     * @return
     */
    private PlanDetailDTO getPlan(List<PlanDetailDTO> allPlanCode) {
        PlanDetailDTO plan = new PlanDetailDTO();
        // 如果需要改变计划
        if (autoPay.getPlan() == null || playResult.isChangePlan()) {
            plan = allPlanCode.get(0);
            autoPay.setPlan(plan.getJhfaName());

        } else {
            for (PlanDetailDTO detailDTO : allPlanCode) {
                if (autoPay.getPlan().equals(detailDTO.getJhfaName())) {
                    plan = detailDTO;
                }
            }
        }
        return plan;
    }

    private void buy(PlanDetailDTO detailDTO) throws IOException {
        isWin();
        // 第一次购买
        int count = autoPay.getCount();
        int playRoundNum = autoPay.getPlayRoundNum();
        if (count == 0 || count == playRoundNum) {
            autoPay.setCount(0);
            autoPay.setNumbers(detailDTO.getFirstPlanResult());
            realBuy();
        } else if (count == playRoundNum - 1) {
            // 连续3把未中 最后一把 购买后将 count 置为0
            realBuy();
        } else {
            autoPay.setNumbers(detailDTO.getFirstPlanResult());
            realBuy();
        }
        playResult.setPayMoney(playResult.getPayMoney().add(autoPay.getPayMoney()));

    }

    /**
     * 判断是否
     */
    private void isWin() {
        Integer[] nums = getOpenNum();
        String numbers = autoPay.getNumbers();
        if (numbers == null) {
            return;
        }
        String[] split = numbers.split(Constant.NUM_SPLIT);
        boolean win = true;
        for (int i = 0; i < nums.length; i++) {
            Integer num = nums[i];
            String buyNums = split[i];
            if (!buyNums.contains(num.toString())) {
                win = false;
            }
        }
        if (win) {
            logger.info("*** 恭喜盈利 购买期数:{} 购买号码:{},购买计划:{}***", autoPay.getNper(), numbers, autoPay.getPlan());
            autoPay.setWin(true);
            playResult.setWinNum(playResult.getWinNum() + 1);
            playResult.setWinMoney(playResult.getWinMoney().add(autoPay.getWinMoney()));
            //如果中奖 则 将购买次数置为0
            playResult.setAllRoundNum(playResult.getAllRoundNum() + 1);
            autoPay.setCount(0);
        } else {
            playResult.setFiledNum(playResult.getFiledNum() + 1);
            // 如果达到了最后一轮  还没盈利 则说明该轮失败！
            if (autoPay.getPlayRoundNum() == autoPay.getCount()) {
                playResult.setFailedRoundNum(playResult.getFailedRoundNum() + 1);
                playResult.setAllRoundNum(playResult.getAllRoundNum() + 1);
            }
        }
    }

    /**
     * 获取开盘号码
     *
     * @return
     */
    private Integer[] getOpenNum() {
        PeriodNumberDTO currentNumberInfo = bizService.getCurrentNumberInfo();
        logger.info("开奖结果:{}", JSON.toJSONString(currentNumberInfo));
        Integer[] currentNumberArr = currentNumberInfo.getCurrentNumberArr();
        PlayTypeEnum playTypeEnum = autoPay.getPlayTypeEnum();
        Integer num = -1;
        List<Integer> openNums = Lists.newArrayList();
        switch (playTypeEnum) {
            case POSITION_ONE:
                openNums.add(currentNumberArr[4]);
                break;
            case POSITION_TEN:
                openNums.add(currentNumberArr[3]);
                break;
            case POSITION_HUNDRED:
                openNums.add(currentNumberArr[2]);
                break;
            case POSITION_THOUSAND:
                openNums.add(currentNumberArr[1]);
                break;
            case POSITION_WAN:
                openNums.add(currentNumberArr[0]);
                break;
            case POSITION_DIRECT_FIRST_TOW:
                openNums.add(currentNumberArr[0]);
                openNums.add(currentNumberArr[1]);
                break;
            case POSITION_DIRECT_FIRST_THREE:
                openNums.add(currentNumberArr[0]);
                openNums.add(currentNumberArr[1]);
                openNums.add(currentNumberArr[2]);
                break;

            case POSITION_DIRECT_FIRST_FOUR:
                openNums.add(currentNumberArr[0]);
                openNums.add(currentNumberArr[1]);
                openNums.add(currentNumberArr[2]);
                openNums.add(currentNumberArr[3]);
                break;
            case POSITION_DIRECT_LAST_TOW:
                openNums.add(currentNumberArr[3]);
                openNums.add(currentNumberArr[4]);
                break;
            case POSITION_DIRECT_LAST_THREE:
                openNums.add(currentNumberArr[2]);
                openNums.add(currentNumberArr[3]);
                openNums.add(currentNumberArr[4]);
                break;
            case POSITION_DIRECT_LAST_FOUR:
                openNums.add(currentNumberArr[1]);
                openNums.add(currentNumberArr[2]);
                openNums.add(currentNumberArr[3]);
                openNums.add(currentNumberArr[4]);
                break;
            case POSITION_DIRECT_FIVE:
                Collections.addAll(openNums, currentNumberArr);
            default:
                //暂时不支持其他是否中奖的选项
                break;

        }
        return openNums.toArray(new Integer[]{});
    }

    private void realBuy() throws IOException {
        int count = autoPay.getCount();
        autoPay.setCount(count + 1);
        String result = new PayUtils(autoPay, sscCokie).autoPay();
        logger.info("第 {} 次购买,购买期数:{} 购买号码:{},购买方案:{},购买计划:{} 购买结果:{},", autoPay.getCount(), autoPay.getNper(), autoPay.getNumbers(), autoPay.getPlayTypeEnum().getValue(), autoPay.getPlan(), result);
    }
}

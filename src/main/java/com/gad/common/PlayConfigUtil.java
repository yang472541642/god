package com.gad.common;

import com.gad.domin.PlayTypeEnum;
import com.gad.domin.dto.PlanConfig;

/**
 * @author zhongchao
 * @title PlayConfigUtil.java
 * @Date 2017-12-03
 * @since v1.0.0
 */
public class PlayConfigUtil {

    /**
     * 生成购买计划 从小助手获取
     *
     * @param playTypeEnum
     */
    public static PlanConfig genPlanConfig(PlayTypeEnum playTypeEnum, Integer buyNum, Integer planRoundNum) {
        PlanConfig planConfig = new PlanConfig();
        planConfig.setNumber(buyNum);
        planConfig.setKeepPeriosd(planRoundNum);
        switch (playTypeEnum) {
            case POSITION_ONE:
                planConfig.setType(ByTypeEnum.DW);
                planConfig.setOffset(playTypeEnum.getKey());
                break;
            case POSITION_TEN:
                planConfig.setType(ByTypeEnum.DW);
                planConfig.setOffset(playTypeEnum.getKey());
                break;
            case POSITION_HUNDRED:
                planConfig.setType(ByTypeEnum.DW);
                planConfig.setOffset(playTypeEnum.getKey());
                break;
            case POSITION_THOUSAND:
                planConfig.setType(ByTypeEnum.DW);
                planConfig.setOffset(playTypeEnum.getKey());
                break;
            case POSITION_WAN:
                planConfig.setType(ByTypeEnum.DW);
                planConfig.setOffset(playTypeEnum.getKey());
                break;
//                    （1 前二 2 后二 3 前三 4 中三 5 后三）
            case POSITION_DIRECT_FIRST_TOW:
                planConfig.setType(ByTypeEnum.ZHX);
                planConfig.setOffset(1);
                break;
            case POSITION_DIRECT_FIRST_THREE:
                planConfig.setType(ByTypeEnum.ZHX);
                planConfig.setOffset(3);
                break;
            case POSITION_DIRECT_FIRST_FOUR:
                planConfig.setType(ByTypeEnum.ZHX);
                //todo 前4
                break;
            case POSITION_DIRECT_LAST_TOW:
                planConfig.setType(ByTypeEnum.ZHX);
                planConfig.setOffset(2);
                break;
            case POSITION_DIRECT_LAST_THREE:
                planConfig.setType(ByTypeEnum.ZHX);
                planConfig.setOffset(5);
                break;
            case POSITION_DIRECT_LAST_FOUR:
                planConfig.setType(ByTypeEnum.ZHX);
                break;
            default:
                throw new IllegalArgumentException("暂时不支持的玩法儿~");
        }
        return planConfig;
    }
}

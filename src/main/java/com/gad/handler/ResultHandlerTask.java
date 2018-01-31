package com.gad.handler;

import com.alibaba.fastjson.JSON;
import com.gad.domin.PlayResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TimerTask;

/**
 * @author zhongchao
 * @title ResultHandlerTask.java
 * @Date 2017-12-06
 * @since v1.0.0
 */
public class ResultHandlerTask extends TimerTask {
    public static final Logger logger = LoggerFactory.getLogger(ResultHandlerTask.class);

    @Override
    public void run() {
        logger.info("timer start");
        Map<String, PlayResult> all = AbstractPlayCenter.getAllAutoPalay();
        for (String key : all.keySet()) {
            logger.info("自动投注状态:{}", JSON.toJSONString(all.get(key)));


        }
    }
}

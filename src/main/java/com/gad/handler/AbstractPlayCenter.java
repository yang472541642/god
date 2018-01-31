package com.gad.handler;

import com.gad.domin.PlayResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhongchao
 * @title AbstractPlayCenter.java
 * @Date 2017-12-03
 * @since v1.0.0
 */
public abstract class AbstractPlayCenter {

    /**
     * 将每个线程运行结果放入缓存池 达到在外部控制线程运行的目的
     */
    private static Map<String, PlayResult> map = new ConcurrentHashMap<>();

    private static Map<String, Boolean> rsMap = new ConcurrentHashMap<>();

    public static void putCache(String key, PlayResult playResult) {
        map.put(key, playResult);
        rsMap.put(key, false);
    }

    public static Map<String, PlayResult> getAllAutoPalay() {
        return map;
    }

    public static void changePlan(String key) {
        rsMap.put(key, true);
    }

    /**
     * 处理
     */
    abstract void deal();
}

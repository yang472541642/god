package com.gad.handler;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhongchao
 * @title PlayHandlerThreadPool.java
 * @Date 2017-12-03
 * @since v1.0.0
 */
public class PlayHandlerThreadPool {

    private ThreadPoolExecutor executorService;

    public PlayHandlerThreadPool(int nThreads, String name) {
        executorService = new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), getThreadFactory(name));
    }

    public void putDealFailedTask(Runnable runnable) {
        executorService.submit(runnable);
    }

    public <T> void putDealFailedTask(Callable<T> callable) {
        executorService.submit(callable);
    }


    public int getThreadCount() {
        return ((ThreadPoolExecutor) executorService).getActiveCount();
    }

    public int getNThread() {
        return executorService.getCorePoolSize();
    }


    private ThreadFactory getThreadFactory(String name) {
        return new MyThreadFactory(name);
    }

}


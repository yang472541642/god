package com.gad.config;

import com.gad.handler.PlayHandlerThreadPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongchao
 * @title ThreadConfig.java
 * @Date 2017-12-03
 * @since v1.0.0
 */
@Configuration
public class ThreadConfig {


    @Bean
    public PlayHandlerThreadPool dealPartsExecutorPool() {
        return new PlayHandlerThreadPool(10, "out-play");
    }
}

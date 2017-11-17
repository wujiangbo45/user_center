package com.navinfo.opentsp.user.common.util.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-15
 * @modify
 * @copyright Navi Tsp
 */
@Configuration
public class CommonConfig {

    @Value("${opentsp.listener.executor.size:3}")
    private Integer poolSize;

    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(poolSize);
    }

}

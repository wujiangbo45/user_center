package com.navinfo.opentsp.user.service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@Configuration
public class HttpConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}

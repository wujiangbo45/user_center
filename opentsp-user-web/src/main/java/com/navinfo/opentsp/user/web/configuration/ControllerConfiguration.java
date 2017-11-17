package com.navinfo.opentsp.user.web.configuration;

import com.navinfo.opentsp.user.web.proxy.HttpProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ControllerConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpProxy httpProxy() {
        return new HttpProxy();
    }

}
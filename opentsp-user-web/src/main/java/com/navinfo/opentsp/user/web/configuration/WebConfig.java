package com.navinfo.opentsp.user.web.configuration;

import com.navinfo.opentsp.user.web.interceptor.OpentspInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Autowired(required = false)
    private List<OpentspInterceptor> interceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        for (OpentspInterceptor opentspInterceptor : interceptors) {
            logger.info("loading interceptor : {}", opentspInterceptor.getClass());
            registry.addInterceptor(opentspInterceptor).addPathPatterns(opentspInterceptor.urlMapping());
        }
    }

//    /**
//     * register filters
//     * @param filters
//     * @return
//     */
//    @Bean
//    @Autowired(required = false)
//    public List<FilterRegistrationBean> registeFilters(List<OpenTspFilter> filters) throws OgnlException {
//        List<FilterRegistrationBean> filterRegistrationBeans = new LinkedList<>();
//
//        if (filters != null) {
//            for (OpenTspFilter filter : filters) {
//                logger.info("loading filter : {}", filter.getClass());
//                FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(filter);
//                String[] urlMapping = filter.urlPattern();
//                // add url pattern
//                if(urlMapping != null && urlMapping.length > 0) {
//                    filterRegistrationBean.addUrlPatterns(urlMapping);
//                } else {
//                    filterRegistrationBean.addUrlPatterns("/*");
//                }
//
//                //add sort
//                if(filter.order() != null) {
//                    filterRegistrationBean.setOrder(filter.order());
//                }
//
//                filterRegistrationBeans.add(filterRegistrationBean);
//            }
//        }
//
//        return filterRegistrationBeans;
//    }
}

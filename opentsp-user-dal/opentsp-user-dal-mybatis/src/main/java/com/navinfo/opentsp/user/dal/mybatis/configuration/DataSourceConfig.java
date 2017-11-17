package com.navinfo.opentsp.user.dal.mybatis.configuration;

import com.navinfo.opentsp.user.dal.profiles.Mybatis;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 *
 * init a database connection pool here.
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-09-28
 * @modify
 * @copyright Navi Tsp
 */
@Configuration
@Mybatis
@EnableConfigurationProperties({DataSourceConfig.PoolPropertyConfig.class})
public class DataSourceConfig {

    @Autowired
    private PoolPropertyConfig config;

    @Bean
    public DataSource dataSource(){

        return new org.apache.tomcat.jdbc.pool.DataSource(config);
    }

//    @Autowired
//    @Bean
//    public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource){
//        return new DataSourceTransactionManager(dataSource);
//    }

    @Autowired
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * @see   org.apache.tomcat.jdbc.pool.PoolProperties
     *
     *  reference: https://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html#Common_Attributes
     */
    @ConfigurationProperties("opentsp.datasource")
    final static class PoolPropertyConfig extends PoolProperties{

    }

}

package com.navinfo.opentsp.user.dal.mybatis.configuration;

import com.navinfo.opentsp.user.dal.profiles.Mybatis;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-28
 * @modify
 * @copyright Navi Tsp
 */
@Configuration
@Mybatis
@EnableTransactionManagement
@Import({DataSourceConfig.class})
public class MybatisConfig {
    private static final Logger logger = LoggerFactory.getLogger(MybatisConfig.class);

    /**
     * xml 配置文件的位置
     */
    @Value("${opentsp.mybatis.mapperfile.location}")
    private String mapperFolder;

    /**
     * mapper 接口的包名
     */
    @Value("${opentsp.mybatis.mapper.package}")
    private String mapperPackage;

    @Bean
    public MybatisResource mybatisResource() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + mapperFolder + "/**/*.xml";
        Resource[] resources = resourcePatternResolver.getResources(pattern);
        return new MybatisResource(resources);
    }

    @Autowired
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource, MybatisResource resource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(resource.getMapperResources());
        return sqlSessionFactoryBean;
    }

//    @Autowired
//    @Bean
//    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }

//    @Bean
//    @Autowired
//    public List<MapperFactoryBean> mapperFactoryBeans(SqlSessionFactoryBean sqlSessionFactoryBean, MybatisResource mybatisResource)
//            throws Exception {
//        List<MapperFactoryBean> mapperFactoryBeans = new LinkedList<>();
//        SAXReader reader = new SAXReader();
//        for(Resource resource : mybatisResource.getMapperResources()) {
//            InputStream is = null;
//            try {
//                is = resource.getInputStream();
//                Document document = reader.read(is);
//                String mapperClass = document.getRootElement().attributeValue("namespace");
//                logger.info("load mapper class : {}", mapperClass);
//                MapperFactoryBean mapperFactoryBean = new MapperFactoryBean();
//                mapperFactoryBean.setMapperInterface(Class.forName(mapperClass));
//                mapperFactoryBean.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
//                mapperFactoryBeans.add(mapperFactoryBean);
//            } finally {
//                if(is != null)
//                    is.close();
//            }
//        }
//
//        return mapperFactoryBeans;
//    }

//    @Bean
//    @Autowired
//    public List<MapperFactoryBean> mapperFactoryBeans(SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
//                + (StringUtils.isEmpty(mapperPackage) ? "" : ClassUtils.convertClassNameToResourcePath(mapperPackage))
//                + "/**/*.class";
//        List<MapperFactoryBean> mapperFactoryBeans = new LinkedList<>();
//        Resource[] resources = resourcePatternResolver.getResources(pattern);
//        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
//        TypeFilter typeFilter = new AssignableTypeFilter(MybatisMapper.class);
//        for (Resource resource : resources) {
//            if(resource.isReadable()) {
//                MetadataReader reader = readerFactory.getMetadataReader(resource);
//                String className = reader.getClassMetadata().getClassName();
//                if(typeFilter.match(reader, readerFactory) && !MybatisMapper.class.getName().equals(className)) {
//                    logger.info("loading mapper class : {}", className);
//                    MapperFactoryBean mapperFactoryBean = new MapperFactoryBean();
//                    mapperFactoryBean.setMapperInterface(Class.forName(className));
//                    mapperFactoryBean.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
//                    mapperFactoryBeans.add(mapperFactoryBean);
//                }
//            }
//        }
//
//        return mapperFactoryBeans;
//    }

}

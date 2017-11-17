package com.navinfo.opentsp.user.common.util.classscan;

import com.navinfo.opentsp.user.common.util.event.ClassScanEvent;
import com.navinfo.opentsp.user.common.util.event.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-30
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class ClassScanner implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ClassScanner.class);

    @Value("${opentsp.class.scan.packages:}")
    private String scanPackages;
    @Autowired
    private EventPublisher eventPublisher;

    private void scan(String scanPackage){
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + ClassUtils.convertClassNameToResourcePath(scanPackage) + "/**/*.class";

        try {
            /**
             *
             * 扫描包 然后加载class
             *
             */
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    logger.info("scanned class {}", className);
                    Class clazz = Class.forName(className);
                    this.eventPublisher.publishEvent(new ClassScanEvent(clazz));
                }
            }
        } catch (ClassNotFoundException e ) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(StringUtils.isEmpty(scanPackages)) {
            return;
        }
        logger.info("begin to scan packages : {}", scanPackages);
        for(String s : scanPackages.split(",")) {
            logger.info("scan package : {} ...", s);
            this.scan(s);
        }

        logger.info("scan packages success ! {}", scanPackages);
    }
}

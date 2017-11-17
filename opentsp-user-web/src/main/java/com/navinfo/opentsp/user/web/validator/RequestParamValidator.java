package com.navinfo.opentsp.user.web.validator;

import com.navinfo.opentsp.user.common.util.event.ClassScanEvent;
import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.service.validator.ParamValidatable;
import com.navinfo.opentsp.user.service.valimpl.ValidatorImplmentor;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * 请求参数校验
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class RequestParamValidator extends AbstractOpentspListener<ClassScanEvent> implements OpentspValidator, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RequestParamValidator.class);
    private static final Map<Class, Map<String, List<Class>>> validateMeta = new HashMap<>();
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Map<Class, ValidatorImplmentor> impls;

    @Autowired
    public RequestParamValidator(List<ValidatorImplmentor> implmentors){
        Map<Class, ValidatorImplmentor> map = new HashMap<>();
        for(ValidatorImplmentor implmentor : implmentors) {
            map.put(implmentor.supportValidation(), implmentor);
            logger.info("loading validation implmentor : {}", implmentor.getClass());
        }

        impls = Collections.unmodifiableMap(map);
    }

    /**
     * 校验请求参数
     * @param args  controller的方法参数
     * @return
     */
    @Override
    public boolean validate(ParamValidatable... args) {//
        for(ParamValidatable arg : args) {//逐个校验
            logger.info("begin to validation arg ! arg : {}", arg);
            if(!validateSingle(arg)) {
                logger.info("validation arg failed ! arg : {}", arg);
                return false;
            }
            logger.info("validation arg success ! arg : {}", arg);
        }

        return true;
    }

    /**
     * 校验单个参数
     * @param arg
     * @return
     */
    private boolean validateSingle(ParamValidatable arg){
        Map<String, List<Class>> meta = null;

        /**
         * read lock won't block other read operations
         */
        lock.readLock().lock();
        try {
            meta = validateMeta.get(arg.getClass());
        } finally {
            lock.readLock().unlock();
        }

        /**
         * if meta is empty, loading class meta
         */
        if(meta == null) {
            this.parseClass(arg.getClass());
            lock.readLock().lock();
            try {
                meta = validateMeta.get(arg.getClass());
            } finally {
                lock.readLock().unlock();
            }
        }

        if(meta != null && meta.size() > 0) {
            try {
                for (Map.Entry<String, List<Class>> entry : meta.entrySet()) {
                    Object value = Ognl.getValue(entry.getKey(), arg);
                    if (!validateField(value, entry.getValue()))
                        return false;
                }
            } catch (OgnlException e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }

        return true;
    }

    /**
     * 校验参数是否符合
     * @param fieldValue
     * @param validation
     * @return
     */
    private boolean validateField(Object fieldValue, List<Class> validation){
        for (Class clazz : validation) {
            ValidatorImplmentor implmentor = impls.get(clazz);
            logger.info("begin validation ! field value : {}, validator implmentor : {}", fieldValue, implmentor.getClass());
            if(implmentor != null && !implmentor.doValidate(fieldValue)) {
                logger.info("validation field error ! field value : {}, validator implmentor : {}", fieldValue, implmentor.getClass());
                return false;
            }
        }

        logger.info("validation field success !");
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * 解析class有哪些约束
     * @param clazz
     */
    private void parseClass(Class clazz){
        logger.info("parsing request param class : {}", clazz.getName());
        /**
         * block other read or write operations
         */
        lock.writeLock().lock();
        try {
            Map<String, List<Class>> meta = validateMeta.get(clazz);
            if(meta == null) {
                meta = new HashMap<>();
                validateMeta.put(clazz, meta);
            }

            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields) {
                Annotation[] annotations = field.getAnnotations();
                if(annotations == null || annotations.length == 0)
                    continue;

                List<Class> validations = meta.get(field.getName());
                if(validations == null) {
                    validations = new LinkedList<>();
                    meta.put(field.getName(), validations);
                }
                for(Annotation annotation : annotations) {
                    validations.add(annotation.annotationType());
                }
            }
        } finally {
            /**
             * unlock write lock
             */
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void onEvent(ClassScanEvent event) {
        Class clazz = event.getData();
        if(clazz != null && ParamValidatable.class.isAssignableFrom(clazz))
            this.parseClass(clazz);
    }
}

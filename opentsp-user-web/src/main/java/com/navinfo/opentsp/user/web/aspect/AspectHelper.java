package com.navinfo.opentsp.user.web.aspect;

import com.navinfo.opentsp.user.common.util.event.ClassScanEvent;
import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-30
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class AspectHelper  extends AbstractOpentspListener<ClassScanEvent> implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(AspectHelper.class);

    private static final Map<String, Class> returnTypes = new HashMap<>();
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    public Class getReturnType(String methodFullName) {
        lock.readLock().lock();
        try {
            return returnTypes.get(methodFullName);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Class getReturnType(ProceedingJoinPoint pjp) {
        Class clazz = this.getReturnType(pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        if(clazz == null) {
            Class[] classes = new Class[pjp.getArgs().length];
            for(int i = 0; i < classes.length; i++) {
                classes[i] = pjp.getArgs()[i].getClass();
            }
            clazz = this.parseReturnType(pjp.getSignature().getDeclaringType(), pjp.getSignature().getName());
        }
        return clazz;
    }

    public Class parseReturnType(Class clazz, String methodName) {
        Method method = null;
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
        for(Method m : methods) {
            if(methodName.equals(m.getName())) {
                method = m;
                break;
            }
        }

        if(method != null) {
            Class cls = method.getReturnType();
            lock.writeLock().lock();
            try {
                returnTypes.put(clazz.getName() + "." + methodName, cls);
            } finally {
                lock.writeLock().unlock();
            }

            return cls;
        }

        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public boolean async() {
        return false;
    }

    private void parseClass(Class clazz) {
        RestController annotation = AnnotationUtils.findAnnotation(clazz, RestController.class);
        if(annotation == null)
            return;

        Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
        for(Method method : methods) {
            if(method.getDeclaringClass() == Object.class)
                continue;

            logger.info("parse method return type : {}.{}", clazz, method);
            lock.writeLock().lock();
            try {
                returnTypes.put(clazz.getName() + "." + method.getName(), method.getReturnType());
            } finally {
                lock.writeLock().unlock();
            }

        }
    }

    @Override
    public void onEvent(ClassScanEvent event) {
        this.parseClass(event.getData());
    }
}

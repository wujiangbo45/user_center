package com.navinfo.opentsp.user.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *<p>
 * aspect of all controllers to check request param
 *</p>
 * <p>
 * 切面，拦截所有的controller， 检查请求参数。  这里没有使用@Valid 和 @NotNull 这类spring-hibernate校验，
 * 因为spring的校验返回值 并不符合要求
 *</p>
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
@Aspect
@Component
public class BackendAspect {

    @Autowired
    private ControllerAspect aspect;

    @Around("execution(* com.navinfo.opentsp.user.web.backend..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        return aspect.doAround(pjp);
    }
}

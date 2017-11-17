package com.navinfo.opentsp.user.web.aspect;

import com.navinfo.opentsp.user.service.exception.ExceptionHandlerDispatcher;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.result.ReturnResult;
import com.navinfo.opentsp.user.web.interceptor.HttpHolder;
import com.navinfo.opentsp.user.web.validator.ValidatorChain;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class ControllerAspect {
    private Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

    @Autowired
    private ValidatorChain chain;
    @Autowired
    private ExceptionHandlerDispatcher dispatcher;
    @Autowired
    private AspectHelper aspectHelper;

    @Around("execution(* com.navinfo.opentsp.user.web.controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        try {
            if(!chain.validate(pjp.getArgs())) {
                logger.debug("validate param error ! ");
                HttpHolder.getResponse().setStatus(HttpStatus.BAD_REQUEST.value());
                return returnValue(pjp, ResultCode.BAD_REQUEST);
            }
        } catch (Exception e) {
            return this.handlerException(pjp, e);
        }

        try {
            Object obj = pjp.proceed();
            if(obj != null && (obj instanceof CommonResult)) {
                int resultcode = ((CommonResult) obj).getHttpCode();
                if (resultcode == 0)
                    resultcode = 200;

                int httpcode = HttpHolder.getResponse().getStatus();
                if(resultcode != httpcode && httpcode == 200)
                    HttpHolder.getResponse().setStatus(resultcode);
            }
            return obj;
        } catch (Exception e) {
            return this.handlerException(pjp, e);
        }
    }

    private Object handlerException(ProceedingJoinPoint pjp, Exception e ) throws Exception {
        ReturnResult returnResult = this.dispatcher.handlerException(e);
        if(returnResult != null)
            HttpHolder.getResponse().setStatus(returnResult.httpCode());
        Object obj = this.returnValue(pjp, returnResult);
        if (obj == null)
            throw e;

        return obj;
    }

    private Object returnValue(ProceedingJoinPoint pjp, ReturnResult returnResult) {
        try {
            // if controller returns CommonResult
            if(CommonResult.class.isAssignableFrom(aspectHelper.getReturnType(pjp))) {
                Class<CommonResult> clazz = aspectHelper.getReturnType(pjp);
                CommonResult instance = clazz.newInstance();
                instance.fillResult(returnResult);
                return instance;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}

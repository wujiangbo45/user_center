package com.navinfo.opentsp.user.web.validator;

import com.navinfo.opentsp.user.service.validator.ParamValidatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class ValidatorChain {
    private static final Logger logger = LoggerFactory.getLogger(ValidatorChain.class);

    @Autowired
    private List<OpentspValidator> validators;

    public boolean validate(Object[] args){
        if(args.length > 0) {
            List<ParamValidatable> paramValidatables = new LinkedList<>();
            for(Object arg : args) {
                if(arg instanceof ParamValidatable)
                    paramValidatables.add((ParamValidatable) arg);
            }

            ParamValidatable[] paramValidatablesArray = paramValidatables.toArray(new ParamValidatable[0]);
            paramValidatables = null;
            for(OpentspValidator validator : validators) {
                logger.info("begin to validation !  validator : {}, args : {}", validator.getClass(), paramValidatablesArray);
                if(!validator.validate(paramValidatablesArray)) {
                    logger.info("validate args failed !  validator : {}, args : {}", validator.getClass(), paramValidatablesArray);
                    return false;
                }
                logger.info("validation success ! ");
            }
        }

        return true;
    }

}

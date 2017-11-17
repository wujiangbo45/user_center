package com.navinfo.opentsp.user.web.validator;

import com.navinfo.opentsp.user.service.param.HttpHeaderParamable;
import com.navinfo.opentsp.user.service.validator.ParamValidatable;
import com.navinfo.opentsp.user.web.interceptor.HttpHolder;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class HttpHeaderParamValidator implements OpentspValidator {
    private static final Logger logger = LoggerFactory.getLogger(HttpHeaderParamValidator.class);

    @Override
    public boolean validate(ParamValidatable... args) {
        for(ParamValidatable paramValidatable : args) {
            if(paramValidatable instanceof HttpHeaderParamable)
                this.changAttr((HttpHeaderParamable) paramValidatable);
        }
        return true;
    }

    public void changAttr(HttpHeaderParamable headerParamable){
        logger.info("change attr for : {}", headerParamable);
        String[][] mapping = headerParamable.fieldHeaderMapping();
        if(mapping == null) {
            return;
        }

        try {
            for(int i = 0; i < mapping.length; i++) {
                String[] map = mapping[i];
                String header = HttpHolder.getRequest().getHeader(map[1]);
                if(StringUtils.isEmpty(header))
                    continue;
                logger.info("set header value [{}] to field [{}]", header, map[0]);
                Ognl.setValue(map[0], headerParamable, header);
            }
        } catch (OgnlException e) {
            logger.error(e.getMessage(), e);
        }

    }
}

package com.navinfo.opentsp.user.web.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-30
 * @modify
 * @copyright Navi Tsp
 */
@RestController
@RequestMapping("/proxy")
public class ProxiedController {
    private static final Logger logger = LoggerFactory.getLogger(ProxiedController.class);

    @Autowired
    private HttpProxy httpProxy;

    @RequestMapping("/**")
    public void proxy(HttpServletRequest request, HttpServletResponse response) {
//        final URI uri = new URI("http", null, instance.getHost(), instance.getPort(), request.getRequestURI(), null, null);
//        logger.debug("send request to {}", uri.toString());
//        final HttpProxyContext proxyContext = new HttpProxyContext(request, response, uri);
//        try {
//            httpProxy.service(proxyContext);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//        return stub;
    }

}

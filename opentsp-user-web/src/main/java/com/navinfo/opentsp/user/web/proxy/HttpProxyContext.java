package com.navinfo.opentsp.user.web.proxy;

import org.apache.http.HttpHost;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Class which contains some data for proxied request
 */
final class HttpProxyContext {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final URI target;
    private final HttpHost targetHost;

    public HttpProxyContext(HttpServletRequest request, HttpServletResponse response, URI target) {
        this.request = request;
        Assert.notNull(request, "request is null");
        this.response = response;
        Assert.notNull(response, "response is null");
        this.target = target;
        Assert.notNull(target, "target is null");
        this.targetHost = new HttpHost(target.getHost(), target.getPort(), target.getScheme());
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public URI getTarget() {
        return target;
    }

    public HttpHost getTargetHost() {
        return this.targetHost;
    }

    public String getTargetPath() {
        return this.target.getPath();
    }
}

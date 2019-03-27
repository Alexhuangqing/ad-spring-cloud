package com.imooc.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @Author Alex
 * @Desc <p>
 *
 *    RequestContext:请求网关的上下文
 * </p>
 * @Date 2019/2/24 10:00
 */
@Component
@Slf4j
public class PreRequestFilter  extends ZuulFilter {
    /**
     * String ERROR_TYPE = "error";
     * String POST_TYPE = "post";
     * String PRE_TYPE = "pre";
     * String ROUTE_TYPE = "route";
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext cxt = RequestContext.getCurrentContext();
        cxt.set("startTime",System.currentTimeMillis());
        return null;
    }
}

package com.imooc.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Alex
 * @Desc <p>打印日志信息</p>
 * @Date 2019/2/24 10:00
 */
@Slf4j
@Component
public class AccessLogFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER-1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext cxt = RequestContext.getCurrentContext();
        HttpServletRequest request = cxt.getRequest();
        String requestURI = request.getRequestURI();
        long startTime = (Long)cxt.get("startTime");
        Long  duration = System.currentTimeMillis() - startTime;
        log.info("requestURI={} | duration={}",requestURI,duration);
        return null;
    }
}

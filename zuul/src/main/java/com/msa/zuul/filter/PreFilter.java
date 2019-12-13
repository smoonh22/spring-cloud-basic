package com.msa.zuul.filter;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@Component
public class PreFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PreFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

//    해당 필터를 타게 할 건지 아닌지
    @Override
    public boolean shouldFilter() {
        return true;
    }


//  실제 필터 로직
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = new HttpServletRequestWrapper(ctx.getRequest());

        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        String requestData = null;

        if (request.getContentLength() > 0) {
            try {
                requestData = CharStreams.toString(request.getReader());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestData == null) {
            return null;
        }

        log.info(String.format("%s request payload %s", request.getMethod(), requestData));
        return null;


    }
}

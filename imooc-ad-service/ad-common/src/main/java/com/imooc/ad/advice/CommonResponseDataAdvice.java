package com.imooc.ad.advice;

import com.imooc.ad.annotation.IgnoreResponseAdvice;
import com.imooc.ad.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/2/24 16:39
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * @param methodParameter 可以通过反射获取 方法 与实体类
     * @param aClass
     * @return true 针对条件响应做拦截
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        //类级别 忽略 处理
        if (methodParameter.getDeclaringClass()
                .isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        //方法级别 忽略 处理
        if (methodParameter.getMethod()
                .isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        return true;
    }


    /**
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        CommonResponse<Object> commonResponse = new  CommonResponse(0,"");
        if(o==null){
            return commonResponse;
        }

        if(o instanceof CommonResponse ){
            return  (CommonResponse)o;
        }
        commonResponse.setData(o);

        return commonResponse;
    }
}

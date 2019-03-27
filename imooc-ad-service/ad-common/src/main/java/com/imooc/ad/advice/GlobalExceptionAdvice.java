package com.imooc.ad.advice;

import com.imooc.ad.exception.AdException;
import com.imooc.ad.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/2/24 17:31
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(AdException.class)
    public CommonResponse<Object> handlerAdException(HttpServletRequest request,
                                                     AdException e) {
        CommonResponse<Object> commonResponse = new CommonResponse<Object>(-1,"business error");

        commonResponse.setData(e.getMessage());

        return commonResponse;
    }
}

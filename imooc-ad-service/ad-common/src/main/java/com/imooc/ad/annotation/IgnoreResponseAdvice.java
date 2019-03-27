package com.imooc.ad.annotation;



import java.lang.annotation.*;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/2/24 17:10
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {
}

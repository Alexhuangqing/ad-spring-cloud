package com.imooc.ad.index;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Alex
 * @Desc
 * <p>
 *
 * 通过ApplicationContextAware 与 PriorityOrdered
 * 初始化一个异步容器
 *
 * </p>
 * @Date 2019/3/30 15:41
 */

@Component
@Slf4j
public class DataTable implements ApplicationContextAware, PriorityOrdered {
    private static ApplicationContext applicationContext;

    //参数key异构容器
    private  final static  Map<Class,Object> dataMap
            = new ConcurrentHashMap<>();



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;
    }
    //当前容器bean的初始化优先级最高
    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    /**
     * 获取统一管理的bean实例
     * @param aClass
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T of(Class<T> aClass){
        Object instance = dataMap.get(aClass);
        if(instance != null){
           return  (T)instance;
        }
         instance = bean(aClass);
        dataMap.put(aClass,instance);
        return (T)instance;
    }



    //通过两种方式获取容器中初始化bean
    private static <T> T  bean(Class<T> aClass){
        return applicationContext.getBean(aClass);
    }

    @SuppressWarnings("unchecked")
    private static <T> T  beanName(String beanName){
        return (T)applicationContext.getBean(beanName);
    }


}

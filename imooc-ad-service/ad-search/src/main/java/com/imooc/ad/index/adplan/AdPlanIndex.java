package com.imooc.ad.index.adplan;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     实现索引的增查改删操作
 * </p>
 * @Date 2019/3/23 11:53
 */
@Slf4j
@Component
public class AdPlanIndex implements IndexAware<Long,AdPlanObject> {


    private static Map<Long,AdPlanObject>  adPlanMap;

    static {
        adPlanMap = new ConcurrentHashMap<>();
    }


    @Override
    public void add(Long key, AdPlanObject value) {
        log.info("add before adPlanMap:{}",adPlanMap);
        adPlanMap.put(key,value);
        log.info("add after adPlanMap:{}",adPlanMap);
    }

    @Override
    public AdPlanObject get(Long key) {
        return adPlanMap.get(key);
    }

    @Override
    public void update(Long key, AdPlanObject value) {
        log.info("update before adPlanMap:{}",adPlanMap);
        AdPlanObject oldObject = adPlanMap.get(key);
        if (oldObject!=null) {
            oldObject.update(value);
        } else {
            adPlanMap.put(key, value);
        }

        log.info("update after adPlanMap:{}",adPlanMap);
    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        log.info("delete before adPlanMap:{}",adPlanMap);
        adPlanMap.remove(key);
        log.info("delete after adPlanMap:{}",adPlanMap);
    }
}

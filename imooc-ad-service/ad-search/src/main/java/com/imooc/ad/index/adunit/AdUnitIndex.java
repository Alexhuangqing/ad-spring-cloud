package com.imooc.ad.index.adunit;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/3/23 14:55
 */
@Component
@Slf4j
public class AdUnitIndex  implements IndexAware<Long,AdUnitObject> {

    private static Map<Long,AdUnitObject> adUnitObjectMap;
    static {
        adUnitObjectMap = new ConcurrentHashMap<>();
    }


    @Override
    public void add(Long key, AdUnitObject value) {
        log.info("add before adUnitObjectMap:{}",adUnitObjectMap);
        adUnitObjectMap.put(key,value);
        log.info("add after adUnitObjectMap:{}",adUnitObjectMap);
    }

    @Override
    public AdUnitObject get(Long key) {
        return adUnitObjectMap.get(key);
    }

    @Override
    public void update(Long key, AdUnitObject value) {
        log.info("update before adUnitObjectMap:{}", adUnitObjectMap);
        AdUnitObject adUnitObject = adUnitObjectMap.get(key);

        if (adUnitObject!=null) {
            adUnitObject.update(value);
        } else {
            adUnitObjectMap.put(key, value);
        }
        log.info("update after adUnitObjectMap:{}", adUnitObjectMap);
    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        adUnitObjectMap.remove(key);
    }
}

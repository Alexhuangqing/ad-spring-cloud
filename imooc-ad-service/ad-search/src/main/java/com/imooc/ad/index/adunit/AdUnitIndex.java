package com.imooc.ad.index.adunit;

import com.imooc.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
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

    /**
     * 筛选出对应“流量位置”的广告位
     * @param positionType
     * @return
     */
    public Set<Long> match(int positionType){
        Set<Long>  unitIds = new HashSet<>();
        adUnitObjectMap.forEach((k,v)->{
//            没有直接用 v.getPositionType().equals(positionType);
            if(AdUnitObject.isSlotTypeOk(positionType,v.getPositionType())){
                unitIds.add(k);
            }
        });
        return  unitIds;
    }

    /**
     * 根据对应unitIds映射出对应的AdUnitObject
     * @param unitIds
     * @return
     */
    public List<AdUnitObject>  fetch(Set<Long> unitIds){
        if(CollectionUtils.isEmpty(unitIds)){
            return Collections.emptyList();
        }
        List<AdUnitObject> result = new ArrayList<>();
        unitIds.forEach(
                unitId -> {
                    AdUnitObject adUnitObject = adUnitObjectMap.get(unitId);
                    if (adUnitObject != null) {
                        result.add(adUnitObject);
                    }
                }
        );
        return  result;
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

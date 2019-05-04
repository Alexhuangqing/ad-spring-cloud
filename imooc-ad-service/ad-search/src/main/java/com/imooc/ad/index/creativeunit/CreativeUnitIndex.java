package com.imooc.ad.index.creativeunit;

import com.imooc.ad.index.IndexAware;
import com.imooc.ad.index.adunit.AdUnitObject;
import com.imooc.ad.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Author Alex
 * @Desc
 * <p>
 *   维护创意与广告单元的多对多关系
 * </p>
 * @Date 2019/3/27 22:02
 */
@Slf4j
@Component
public class CreativeUnitIndex implements IndexAware<String,CreativeUnitObject> {


    private static  Map<String,CreativeUnitObject>  objectMap;
    private static  Map<Long, Set<Long>>  creativeUnitMap;
    private static   Map<Long, Set<Long>>  unitCreativeMap;

    static {
        // adId-unitId
        objectMap = new ConcurrentHashMap<>();
        // 反向索引  adId    unitId(set)
        creativeUnitMap = new ConcurrentHashMap<>();
        // 正向索引  unitId    adId(set)
        unitCreativeMap = new ConcurrentHashMap<>();

    }


    /**
     * 维护3个map容器
     * @param key  adId-unitId
     * @param value
     */
    @Override
    public void add(String key, CreativeUnitObject value) {
        log.info("add before:{}",objectMap);
        objectMap.put(key,value);

        Set<Long> unitIds = CommonUtil.getOrCreate(
                value.getAdId(),
                creativeUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIds.add(value.getUnitId());

        Set<Long> adIds = CommonUtil.getOrCreate(
                value.getUnitId(),
                unitCreativeMap,
                ConcurrentSkipListSet::new
        );
        adIds.add(value.getAdId());
        log.info("add after:{}",objectMap);
    }

    @Override
    public CreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void update(String key, CreativeUnitObject value) {
        log.error(" unSupport update for CreativeUnitIndex");
    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        log.info("delete before:{}",objectMap);
        objectMap.remove(key);

        Set<Long> units = creativeUnitMap.get(value.getAdId());
        if(CollectionUtils.isNotEmpty(units)){
            units.remove(value.getUnitId());
        }

        Set<Long> adIds = unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isNotEmpty(adIds)){
            adIds.remove(value.getAdId());
        }


        log.info("delete after:{}",objectMap);

    }

    public List<Long> selectAds(List<AdUnitObject> adUnitObjects) {
        if(CollectionUtils.isEmpty(adUnitObjects)){
            return Collections.emptyList();
        }
        List<Long>  adIds = new ArrayList<>();
        adUnitObjects.forEach(unitObject->{
            Set<Long> ids = unitCreativeMap.get(unitObject.getUnitId());
            if(CollectionUtils.isNotEmpty(ids)){
                adIds.addAll(ids);
            }
        });
        return adIds;
    }
}

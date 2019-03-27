package com.imooc.ad.index.keyWord;

import com.imooc.ad.index.IndexAware;
import com.imooc.ad.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @Author Alex
 * @Desc
 * <p>
 * 构建多对多的双向索引关系
 * 1.正向索引
 * 2.反向索引
 * </p>
 * @Date 2019/3/23 15:29
 */
@Component
@Slf4j
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {
    //   正向索引
   private static Map<Long,Set<String>> unitKeywordMap;
    //   反向索引
   private static Map<String,Set<Long>> keywordUnitMap;

    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    @Override
    public void add(String keyword, Set<Long> value) {

        log.info("UnitKeywordIndex add before unitKeywordMap:{}",unitKeywordMap);
        //维护反向关系
        keywordUnitMap.put(keyword,value);
        //维护正向关系
        value.forEach(v->{
            Set<String> orCreate = CommonUtil.getOrCreate(v, unitKeywordMap, ConcurrentSkipListSet::new);
            orCreate.add(keyword);
        });
        log.info("UnitKeywordIndex add after unitKeywordMap:{}",unitKeywordMap);
    }

    @Override
    public Set<Long> get(String key) {
        if(StringUtils.isEmpty(key)||null==keywordUnitMap.get(key)){
           return Collections.emptySet();
        }
        return keywordUnitMap.get(key);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("unSupport update for UnitKeywordIndex");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitKeywordIndex delete before unitKeywordMap:{}",unitKeywordMap);
        //维护反向关系  部分删除
        Set<Long> units = CommonUtil.getOrCreate(key, keywordUnitMap, ConcurrentSkipListSet::new);
        units.removeAll(value);
        //维护正向关系
        value.forEach(v->{
            Set<String> keyWords = CommonUtil.getOrCreate(
                    v, unitKeywordMap,
                    ConcurrentSkipListSet::new);
            keyWords.remove(key);
        });
        log.info("UnitKeywordIndex delete after unitKeywordMap:{}",unitKeywordMap);
    }

    /**
     * 筛选符合条件（条件是and的关系）的资源
     * @param unitId
     * @param value
     * @return
     */
    public boolean match(Long unitId, Set<String> value){


        if (unitKeywordMap.containsKey(unitId)
            && CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))) {
            Set<String> keywords = unitKeywordMap.get(unitId);
            return CollectionUtils.isSubCollection(keywords, value);
        }

        return false;
    }
}



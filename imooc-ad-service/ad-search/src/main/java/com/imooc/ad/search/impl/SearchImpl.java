package com.imooc.ad.search.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.index.CommonStatus;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.index.adunit.AdUnitObject;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.creativeunit.CreativeUnitIndex;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interest.UnitItIndex;
import com.imooc.ad.index.keyWord.UnitKeywordIndex;
import com.imooc.ad.search.ISearch;
import com.imooc.ad.search.vo.SearchRequest;
import com.imooc.ad.search.vo.SearchResponse;
import com.imooc.ad.search.vo.feature.DistrictFeature;
import com.imooc.ad.search.vo.feature.FeatureRelation;
import com.imooc.ad.search.vo.feature.ItFeature;
import com.imooc.ad.search.vo.feature.KeywordFeature;
import com.imooc.ad.search.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/5/2 22:33
 */
@Component
@Slf4j
public class SearchImpl implements ISearch {
    @Override
    public SearchResponse fetchAds(SearchRequest request) {

        //构造响应对象
        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>>
                adSlot2Ads = response.getAdSlot2Ads();

        //请求的广告位
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();
        //请求的广告位特征的三大特征
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        ItFeature itFeature = request.getFeatureInfo().getItFeature();
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        FeatureRelation featureRelation = request.getFeatureInfo().getRelation();

        //通过广告位的特征类型筛选
        for (AdSlot adSlot:adSlots){
            Set<Long> targetUnits;
            //通过广告位的流量类型去删选
            Set<Long> adUnits = DataTable.of(
                    AdUnitIndex.class
            ).match(adSlot.getPositionType());

            //通过三大广告特征及and/or关系删选
            if(featureRelation==FeatureRelation.AND){
                filterKeywordFeature(adUnits,keywordFeature);
                filterDistrictFeature(adUnits,districtFeature);
                filterItFeature(adUnits,itFeature);
                targetUnits = adUnits;
            }else {
                targetUnits = getORRelationUnitIds(adUnits,keywordFeature,districtFeature,itFeature);
            }
            //过滤状态
            List<AdUnitObject> adUnitObjects = DataTable.of(AdUnitIndex.class).fetch(targetUnits);
            filterAdUnitAndPlanStatus(adUnitObjects, CommonStatus.VALID);
            //过滤创意
            List<Long> adIds = DataTable.of(
                    CreativeUnitIndex.class
                    ).selectAds(adUnitObjects);

            List<CreativeObject> creativeObjects = DataTable.of(CreativeIndex.class)
                    .fetch(adIds);
            // 通过 AdSlot 实现对 CreativeObject 的过滤
            filterCreativeByAdSlot(
                    creativeObjects,
                    adSlot.getWidth(),
                    adSlot.getHeight(),
                    adSlot.getType()
            );
            adSlot2Ads.put(adSlot.getAdSlotCode(),buildCreativeResponse(creativeObjects));
        }
        log.info("fetchAds: {}->{}", JSON.toJSONString(request),JSON.toJSONString(response));
        return response;
    }

    /**
     * 自定义：一个广告栏。只返回一个广告创意
     * @param creativeObjects
     * @return
     */
    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creativeObjects) {
        if(CollectionUtils.isEmpty(creativeObjects)){
            return Collections.emptyList();
        }


        CreativeObject creativeObject = creativeObjects.get(
                Math.abs(ThreadLocalRandom.current().nextInt())
                % creativeObjects.size());
        return Collections.singletonList(SearchResponse.convert(creativeObject));
    }

    /**
     * 广告栏尺寸的适配
     * @param creativeObjects
     * @param width  广告栏宽度
     * @param height 广告栏高度
     * @param type   广告栏兼容类型
     */
    private void filterCreativeByAdSlot(List<CreativeObject> creativeObjects,
                                        Integer width,
                                        Integer height,
                                        List<Integer> type) {
        if(CollectionUtils.isEmpty(creativeObjects)){
          return;
        }

        CollectionUtils.filter(creativeObjects,
                creative->creative.getWidth().equals(width)
                &&creative.getHeight().equals(height)
                &&type.contains(creative.getType())
                );
    }

    /**
     * 筛选出指定状态的广告单元
     * @param adUnitObjects
     * @param validStatus
     */
    private void filterAdUnitAndPlanStatus(List<AdUnitObject> adUnitObjects,
                                           CommonStatus validStatus) {
        if(CollectionUtils.isEmpty(adUnitObjects)){
            return;
        }
        CollectionUtils.filter(adUnitObjects,
                adUnitObject ->
                 validStatus.getStatus()
                         .equals(adUnitObject.getUnitStatus())
                 && validStatus.getStatus()
                         .equals(adUnitObject.getAdPlanObject().getPlanStatus())
        );
    }

    /**
     * 或者的关系 筛选出全部的adUnits
     * @param adUnits
     * @param keywordFeature
     * @param districtFeature
     * @param itFeature
     */
    private Set<Long> getORRelationUnitIds(Set<Long> adUnits,
                                      KeywordFeature keywordFeature,
                                      DistrictFeature districtFeature,
                                      ItFeature itFeature) {
        if(CollectionUtils.isEmpty(adUnits)){
            return Collections.emptySet();
        }
        Set<Long> itAdUnits =new HashSet<>(adUnits);
        Set<Long> districtAdUnits =new HashSet<>(adUnits);
        Set<Long> keywordAdUnits =new HashSet<>(adUnits);

        filterItFeature(itAdUnits,itFeature);
        filterDistrictFeature(districtAdUnits,districtFeature);
        filterKeywordFeature(keywordAdUnits,keywordFeature);

        Collection<Long> union = CollectionUtils.union(
                itAdUnits,
                CollectionUtils.union(districtAdUnits, keywordAdUnits)
        );
        return new HashSet<>(union);
    }

    /**
     * 筛选出适配itFeature的units集合
     * @param adUnits
     * @param itFeature
     */
    private void filterItFeature(Set<Long> adUnits, ItFeature itFeature) {
        if(CollectionUtils.isEmpty(adUnits)){
            return ;
        }
        if(CollectionUtils.isNotEmpty(itFeature.getIts())){
            CollectionUtils.filter(adUnits, adUnit ->
                    DataTable.of(UnitItIndex.class)
                            .match(adUnit,itFeature.getIts())
            );
        }
    }

    /**
     * 筛选出适配districtFeature的units集合
     * @param adUnits
     * @param districtFeature
     */
    private void filterDistrictFeature(Set<Long> adUnits, DistrictFeature districtFeature) {
        if(CollectionUtils.isEmpty(adUnits)){
            return ;
        }
        if(CollectionUtils.isNotEmpty(districtFeature.getDistricts())){
            CollectionUtils.filter(adUnits, adUnit ->
                    DataTable.of(UnitDistrictIndex.class)
                            .match(adUnit, districtFeature.getDistricts())
            );
        }
    }

    /**
     * 筛选出适配keywordFeature的units集合
     * @param adUnits
     * @param keywordFeature
     */
    private void filterKeywordFeature(Collection<Long> adUnits, KeywordFeature keywordFeature) {
        if(CollectionUtils.isEmpty(adUnits)){
            return ;
        }
        if(CollectionUtils.isNotEmpty(keywordFeature.getKeywords())){
            CollectionUtils.filter(adUnits,adUnit->
                DataTable.of(UnitKeywordIndex.class).match(adUnit,keywordFeature.getKeywords())
            );
        }
    }
}

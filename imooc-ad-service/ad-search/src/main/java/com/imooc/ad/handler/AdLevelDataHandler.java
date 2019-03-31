package com.imooc.ad.handler;

import com.imooc.ad.dump.table.*;
import com.imooc.ad.index.DataTable;
import com.imooc.ad.index.IndexAware;
import com.imooc.ad.index.adplan.AdPlanIndex;
import com.imooc.ad.index.adplan.AdPlanObject;
import com.imooc.ad.index.adunit.AdUnitIndex;
import com.imooc.ad.index.adunit.AdUnitObject;
import com.imooc.ad.index.creative.CreativeIndex;
import com.imooc.ad.index.creative.CreativeObject;
import com.imooc.ad.index.creativeunit.CreativeUnitIndex;
import com.imooc.ad.index.creativeunit.CreativeUnitObject;
import com.imooc.ad.index.district.UnitDistrictIndex;
import com.imooc.ad.index.interest.UnitItIndex;
import com.imooc.ad.index.interest.UnitItObject;
import com.imooc.ad.index.keyWord.UnitKeywordIndex;
import com.imooc.ad.index.keyWord.UnitKeywordObject;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;

/**
 * @Author Alex
 * @Desc
 * <p>
 *    通过获取的表的结构化数据
 *    转成
 *    内存的结构化数据
 * </p>
 * @Date 2019/3/30 16:52
 */

@Slf4j
public class AdLevelDataHandler {



     public static void handleLevel2(AdPlanTable adPlanTable,OpType opType){

         AdPlanObject adPlanObject = new AdPlanObject(
                 adPlanTable.getId(),
                 adPlanTable.getUserId(),
                 adPlanTable.getPlanStatus(),
                 adPlanTable.getStartDate(),
                 adPlanTable.getEndDate()
         );
         handleBinlogEvent(DataTable.of(AdPlanIndex.class)
                 ,adPlanObject.getPlanId()
                 ,adPlanObject
                 ,opType);

     }


     public static void handleLevel2(AdCreativeTable  adCreativeTable, OpType opType){

         CreativeObject creativeObject = new CreativeObject(
                 adCreativeTable.getAdId(),
                 adCreativeTable.getName(),
                 adCreativeTable.getType(),
                 adCreativeTable.getMaterialType(),
                 adCreativeTable.getHeight(),
                 adCreativeTable.getWidth(),
                 adCreativeTable.getAuditStatus(),
                 adCreativeTable.getAdUrl()
         );
         handleBinlogEvent(DataTable.of(CreativeIndex.class)
                 ,creativeObject.getAdId()
                 ,creativeObject
                 ,opType);

     }

     public static void handleLevel3(AdUnitTable adUnitTable, OpType opType){

         AdPlanObject adPlanObject = DataTable.of(
                 AdPlanIndex.class)
                 .get(adUnitTable.getPlanId());
         if(adPlanObject == null){
             log.error("handleLevel3  planId:{} ",adUnitTable.getPlanId());
             return;
         }

         AdUnitObject adUnitObject = new AdUnitObject(
                 adUnitTable.getUnitId(),
                 adUnitTable.getUnitStatus(),
                 adUnitTable.getPositionType(),
                 adUnitTable.getPlanId(),
                 adPlanObject
         );


         handleBinlogEvent(
                 DataTable.of( AdUnitIndex.class),
                 adUnitObject.getUnitId(),
                 adUnitObject,
                 opType
         );


     }

    public static void handleLevel4(AdUnitKeywordTable adUnitKeywordTable, OpType opType){

         if(OpType.UPDATE.equals(opType)){
             log.error("UnitKeywordIndex unSupport handle update");
             return;
         }

         Long unitId = adUnitKeywordTable.getUnitId();
        AdUnitObject adUnitObject = DataTable.of(
                AdUnitIndex.class
                ).get(unitId);
         if(adUnitObject == null){
             log.error("AdUnitIndex not found unitId:{}",unitId);
             return;
         }


        UnitKeywordObject unitKeywordObject = new UnitKeywordObject(
                adUnitKeywordTable.getUnitId(),
                adUnitKeywordTable.getKeyword()
        );

        Set<Long> value = Collections.singleton(unitId);
        handleBinlogEvent(
                DataTable.of(UnitKeywordIndex.class),
                unitKeywordObject.getKeyword(),
                value,
                opType);

    }


    public static void handleLevel4(AdUnitItTable adUnitItTable, OpType opType){

        if(OpType.UPDATE.equals(opType)){
            log.error("UnitItIndex unSupport handle update");
            return;
        }

        Long unitId = adUnitItTable.getUnitId();
        AdUnitObject adUnitObject = DataTable.of(
                                    AdUnitIndex.class
                                    ).get(unitId);
        if(adUnitObject == null){
            log.error("AdUnitIndex not found unitId:{}",unitId);
            return;
        }
        //一对多 一的一方维护key
        UnitItObject unitItObject = new UnitItObject(
                unitId,
                adUnitItTable.getItTag()

        );
        //final 修饰字段
        Set<Long> value = Collections.singleton(unitId);

        handleBinlogEvent(
                DataTable.of(UnitItIndex.class),
                adUnitItTable.getItTag(),
                value,
                opType
        );


    }

    public static void handleLevel4(AdUnitDistrictTable adUnitDistrictTable, OpType opType){
        if(OpType.UPDATE.equals(opType)){
            log.error("UnitDistrictIndex unSupport handle update");
            return;
        }

        Long unitId = adUnitDistrictTable.getUnitId();
        AdUnitObject adUnitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitId);
        if(adUnitObject == null){
            log.error("AdUnitIndex not found unitId:{}",unitId);
            return;
        }

        String key = CommonUtil.stringConcat(
                adUnitDistrictTable.getProvince(),
                adUnitDistrictTable.getCity());
        Set<Long> value = Collections.singleton(unitId);
        //根据事件操作索引
        handleBinlogEvent(
                DataTable.of(UnitDistrictIndex.class),
                key,
                value,
                opType
        );

    }


    public static void handleLevel4(AdCreativeUnitTable adCreativeUnitTable, OpType opType) {
        if (OpType.UPDATE.equals(opType)) {
            log.error("CreativeUnitIndex unSupport handle update");
            return;
        }

        Long unitId = adCreativeUnitTable.getUnitId();
        AdUnitObject adUnitObject = DataTable.of(
                AdUnitIndex.class
        ).get(unitId);
        if (adUnitObject == null) {
            log.error("AdUnitIndex not found unitId:{}", unitId);
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                adCreativeUnitTable.getAdId(),
                adCreativeUnitTable.getUnitId()
        );

        String key = CommonUtil.stringConcat(
                creativeUnitObject.getAdId().toString(),
                creativeUnitObject.getUnitId().toString()
                );

        handleBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                key,
                creativeUnitObject,
                opType
        );

    }

    /**
     * 内存的索引维护
     * @param index
     * @param key
     * @param value
     * @param opType
     * @param <K>
     * @param <V>
     */
    public static <K,V> void handleBinlogEvent(
            IndexAware<K,V> index,
            K key,
            V value,
            OpType opType){
        switch (opType) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key, value);
                break;
            default:
                break;
        }




    }



}

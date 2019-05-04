package com.imooc.ad.index.adunit;

import com.imooc.ad.index.adplan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/3/23 14:54
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdUnitObject {
    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;

    private AdPlanObject adPlanObject;


    public void update(AdUnitObject newObject){
        if(null == newObject){
           log.error("update is failed  newObject is null");
           return;
        }
        if(null!=newObject.getUnitId()){
           this.unitId =  newObject.getUnitId();
        }
        if(null!=newObject.getUnitStatus()){
           this.unitStatus =  newObject.getUnitStatus();
        }
        if(null!=newObject.getPositionType()){
           this.positionType =  newObject.getPositionType();
        }
        if(null!=newObject.getPlanId()){
           this.planId =  newObject.getPlanId();
        }
        if(null!=newObject.getAdPlanObject()){
           this.adPlanObject =  newObject.getAdPlanObject();
        }
    }
    /*提供工具方法*/
    private static boolean isKaiPing(int positionType){
        return (positionType&AdUnitConstant.POSITION_TYPE.KAIPING)>0;
    }
    private static boolean isTiePian(int positionType){
        return (positionType&AdUnitConstant.POSITION_TYPE.TIEPIAN)>0;
    }
    private static boolean isTiePianMiddle(int positionType){
        return (positionType&AdUnitConstant.POSITION_TYPE.TIEPIAN_MIDDLE)>0;
    }
    private static boolean isTiePianPause(int positionType){
        return (positionType&AdUnitConstant.POSITION_TYPE.TIEPIAN_PAUSE)>0;
    }
    private static boolean isTiePianPost(int positionType){
        return (positionType&AdUnitConstant.POSITION_TYPE.TIEPIAN_POST)>0;
    }
    /*提供工具方法来匹配slotType，用于在遍历Object时筛选使用*/

    /**
     *
     * @param slotType  要适配的流量类型
     * @param positionType 当前正在遍历的流量类型
     * @return
     */
    public static  boolean isSlotTypeOk(Integer slotType ,Integer positionType ){
        switch(slotType){
            case AdUnitConstant.POSITION_TYPE.KAIPING:
                return isKaiPing(positionType);
            case AdUnitConstant.POSITION_TYPE.TIEPIAN:
                return isTiePian(positionType);
            case AdUnitConstant.POSITION_TYPE.TIEPIAN_MIDDLE:
                return isTiePianMiddle(positionType);
            case AdUnitConstant.POSITION_TYPE.TIEPIAN_PAUSE:
                return isTiePianPost(positionType);
            default:
                return false;
        }

    }


}

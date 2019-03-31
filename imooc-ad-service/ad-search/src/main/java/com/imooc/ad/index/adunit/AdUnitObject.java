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
}

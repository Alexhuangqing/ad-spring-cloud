package com.imooc.ad.index.adplan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Author Alex
 * @Desc
 * <p>
 *
 *    (1)广告推荐计划的模型领域
 *    (2)内存持有的检索对象
 *
 * </p>
 * @Date 2019/3/23 11:42
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class AdPlanObject {

    private Long planId;
    private Long userId;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;


    /**
     * 基于内存中缓存数据的更新
     * @param newAdPlanObject
     */
    public void update(AdPlanObject newAdPlanObject){
        if(null==newAdPlanObject){
            log.info("update failed newAdPlanObject is null ");
            return;
        }
        if(newAdPlanObject.planId != null){
            this.planId= newAdPlanObject.getPlanId();
        }
        if(newAdPlanObject.userId != null){
            this.userId= newAdPlanObject.getUserId();
        }
        if(newAdPlanObject.planStatus != null){
            this.planStatus= newAdPlanObject.getPlanStatus();
        }
        if(newAdPlanObject.startDate != null){
            this.startDate= newAdPlanObject.getStartDate();
        }
        if(newAdPlanObject.endDate != null){
            this.endDate= newAdPlanObject.getEndDate();
        }



    }
}

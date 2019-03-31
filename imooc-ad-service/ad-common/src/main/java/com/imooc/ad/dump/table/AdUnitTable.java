package com.imooc.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Qinyi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitTable {
    /*推广单元主键id*/
    private Long unitId;
    /*推广单元状态*/
    private Integer unitStatus;
    /*广告位类型(开屏, 贴片, 中贴, 暂停帖, 后贴)*/
    private Integer positionType;
    /*关联父级的广告计划id*/
    private Long planId;
}

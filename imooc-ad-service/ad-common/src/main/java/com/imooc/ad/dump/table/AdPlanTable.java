package com.imooc.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by Qinyi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanTable {
    /*自增主键*/
    private Long id;
    /*用户id*/
    private Long userId;
    /*推广计划状态*/
    private Integer planStatus;
    /*推广计划开始时间*/
    private Date startDate;
    /*推广计划结束时间*/
    private Date endDate;
}

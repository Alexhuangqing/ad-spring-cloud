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
public class AdCreativeTable {
    /*自增主键*/
    private Long adId;
    /*创意名称*/
    private String name;
    /*物料类型(图片, 视频)*/
    private Integer type;
    /*物料子类型(图片: bmp, jpg 等等)*/
    private Integer materialType;
    /*高度*/
    private Integer height;
    /*宽度*/
    private Integer width;
    /*审核状态*/
    private Integer auditStatus;
    /*物料地址*/
    private String adUrl;
}

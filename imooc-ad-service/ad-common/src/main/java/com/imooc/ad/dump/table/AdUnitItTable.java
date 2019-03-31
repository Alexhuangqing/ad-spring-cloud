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
public class AdUnitItTable {
    /*推广单元的id*/
    private Long unitId;
    /*关联的用户标签*/
    private String itTag;
}

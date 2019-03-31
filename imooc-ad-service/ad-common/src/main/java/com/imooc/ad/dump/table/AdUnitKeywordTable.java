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
public class AdUnitKeywordTable {
    /*推广单元的id*/
    private Long unitId;
    /*推广单元的关键词*/
    private String keyword;
}

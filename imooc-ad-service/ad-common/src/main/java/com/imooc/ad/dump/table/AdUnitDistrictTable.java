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
public class AdUnitDistrictTable {
    /*推广单元id*/
    private Long unitId;
    /*该推广单元的省份*/
    private String province;
    /*推广单元所在的城市*/
    private String city;
}

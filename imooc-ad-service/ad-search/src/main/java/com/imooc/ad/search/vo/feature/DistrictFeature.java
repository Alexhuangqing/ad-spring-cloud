package com.imooc.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/3/23 19:03
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictFeature {

    /**
     * 广告单元与地域是1对多的关系
     * 1个广告单元要匹配多地域
     */
    private List<ProvinceAndCity> districts;
    @Data
    public static class ProvinceAndCity{
        private String province;
        private String city;
    }
}

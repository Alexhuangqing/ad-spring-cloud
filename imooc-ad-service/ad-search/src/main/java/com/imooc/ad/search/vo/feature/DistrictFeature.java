package com.imooc.ad.search.vo.feature;

import lombok.Data;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/3/23 19:03
 */
public class DistrictFeature {

    @Data
    public static class ProvinceAndCity{
        private String province;
        private String city;
    }
}

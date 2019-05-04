package com.imooc.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Qinyi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItFeature {

    /**
     * 广告单元与兴趣是1对多的关系
     * 1个广告单元要匹配多个兴趣
     */
    private List<String> its;
}

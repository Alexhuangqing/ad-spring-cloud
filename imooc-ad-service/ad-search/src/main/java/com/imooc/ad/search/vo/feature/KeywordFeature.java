package com.imooc.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordFeature {
    /**
     *广告单元与关键字是1对多的关系；
     * 一个广告单元要匹配多个关键字
     */
    private List<String> keywords;
}

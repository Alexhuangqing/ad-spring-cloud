package com.imooc.ad.mysql.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     template.json 解析的隐射类
 * </p>
 * @Date 2019/3/31 21:26
 */
@Data
public class Template {

    private String database;
    private List<JsonTable> tableList;
}

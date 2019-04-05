package com.imooc.ad.mysql.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     解析为Template操作对象
 * </p>
 * @Date 2019/3/31 21:43
 */
@Data
public class ParseTemplate {
    /*该模板操作的数据库*/
    private String database;
    /*该模板操作的表操作集合*/
    private Map<String, List<ParseTable>> tableMap = new HashMap<>();





}

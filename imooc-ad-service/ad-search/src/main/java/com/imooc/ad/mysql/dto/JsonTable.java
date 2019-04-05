package com.imooc.ad.mysql.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     template.json 解析的表隐射类
 * </p>
 * @Date 2019/3/31 21:35
 */
@Data
public class JsonTable {

    private String tableName;
    private Integer level;
    private List<Column> insert;
    private List<Column> update;
    private List<Column> delete;
    @Data
    public static class Column{
        private String column ;
    }
}

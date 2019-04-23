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
    /*表名*/
    private String tableName;
    /*当前表所处的级别，从2开始  2 ，3 ， 4*/
    private Integer level;
    /*操作类型所在的操作列*/
    private List<Column> insert;
    /*更新类型所在的更新列*/
    private List<Column> update;
    /*删除类型的删除列*/
    private List<Column> delete;

    /*定义列类型的名称*/
    @Data
    public static class Column{
        private String column ;
    }
}

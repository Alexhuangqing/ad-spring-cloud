package com.imooc.ad.mysql.dto;

import com.imooc.ad.mysql.constant.OpType;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Alex
 * @Desc
 * <p>
 *      解析为Table的操作对象
 * </p>
 * @Date 2019/3/31 21:44
 */
@Data
public class ParseTable {
    /*表操作的名称*/
    private String tableName;
    /*表操作的层级*/
    private String level;
    /**
     *     ADD,
     *     UPDATE,
     *     DELETE,
     *     OTHER;
     * ====》该表定义的枚举类型所对应的操作列
     */
    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();

    /**
     *
     *   mysql-binlog-connector-java监听日志
     *   建立索引与实际列名的映射
     *      Write---------------
     *      WriteRowsEventData{
     *      tableId=85,
     *      includedColumns={0, 1, 2},
     *      rows=[
     *      [10, 10, 宝马]
     *      ]
     *      }
     *      Update--------------
     *      UpdateRowsEventData{tableId=85, includedColumnsBeforeUpdate={0, 1, 2},
     *      includedColumns={0, 1, 2}, rows=[
     *      {before=[10, 10, 宝马], after=[10, 11, 宝马]}
     *      ]}
     *      Delete--------------
     *      DeleteRowsEventData{tableId=85, includedColumns={0, 1, 2}, rows=[
     *      [11, 10, 奔驰]
     *      ]}
     *
     *
     *       TableMapEventData{tableId=102, database='imooc_ad_data', table='ad_unit_keyword', columnTypes=3, 3, 15, columnMetadata=0, 0, 90, columnNullability={}}
     *       UpdateRowsEventData{tableId=102, includedColumnsBeforeUpdate={0, 1, 2}, includedColumns={0, 1, 2}, rows=[
     *          {before=[1, 1, 李四], after=[1, 1, 王五]}
     *        ]}
     *      mysql-binlog-connector-java 开源软件索引与表中实际列名之间的映射
     *
     */
    private Map<Integer,String> indexColumnMap =  new HashMap<>();


}

package com.imooc.ad.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     定义数据结构；包装监听的事件
 * </p>
 * @Date 2019/4/7 10:46
 */
@Data
public class BinlogRowData {
    /*表操作*/
    private ParseTable parseTable;
    /*binlog的事件类型*/
    private EventType eventType;
    private List<Map<String,String>>  before;
    /*更新后的值 列名与字段值的映射*/
    private List<Map<String,String>>  after;
}

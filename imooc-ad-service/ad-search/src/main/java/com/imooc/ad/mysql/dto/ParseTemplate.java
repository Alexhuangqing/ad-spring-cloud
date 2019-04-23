package com.imooc.ad.mysql.dto;

import com.imooc.ad.mysql.constant.OpType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     解析为Template操作对象
 * </p>
 * @Date 2019/3/31 21:43
 */
@Data
@Slf4j
public class ParseTemplate {
    /*该模板操作的数据库*/
    private String database;
    /*该模板操作的表操作集合*/
    private Map<String, ParseTable> tableMap = new HashMap<>();

    /**
     * 提供对外的能力，解析模板，返回一个自上而下的操作对象
     * @param template 自定义的配置文件
     * @return
     */
    public static ParseTemplate parse(Template template){

        if(template == null){
          throw new RuntimeException("parse template error");
        }

        ParseTemplate parseTemplate = new ParseTemplate();
        parseTemplate.setDatabase(template.getDatabase());
        Map<String, ParseTable> tableMap = parseTemplate.getTableMap();

        for(JsonTable jsonTable : template.getTableList()){
            ParseTable parseTable = new ParseTable();
            String tableName = jsonTable.getTableName();
            parseTable.setTableName(tableName);
            parseTable.setLevel(jsonTable.getLevel().toString());
            Map<OpType, List<String>> opTypeFieldSetMap = parseTable.getOpTypeFieldSetMap();
            List<JsonTable.Column> insert = jsonTable.getInsert();
            List<JsonTable.Column> update = jsonTable.getUpdate();
            List<JsonTable.Column> delete = jsonTable.getDelete();

            if(CollectionUtils.isNotEmpty(insert)){
                for (JsonTable.Column column : insert
                ) {
                    getOrCreate(OpType.ADD,
                            opTypeFieldSetMap,
                            ArrayList::new)
                            .add(column.getColumn());
                }
            }
            if(CollectionUtils.isNotEmpty(update)){
                for (JsonTable.Column column : update
                ) {
                    getOrCreate(OpType.UPDATE,
                            opTypeFieldSetMap,
                            ArrayList::new)
                            .add(column.getColumn());
                }
            }
            if(CollectionUtils.isNotEmpty(delete)){
                for (JsonTable.Column column : delete
                ) {
                    getOrCreate(OpType.DELETE,
                            opTypeFieldSetMap,
                            ArrayList::new)
                            .add(column.getColumn());
                }
            }
            tableMap.put(tableName,parseTable);

        }

        return parseTemplate;
    }


    /**
     * 生成并返回对应的值
     */
    private static  <K,V> V getOrCreate(K key, Map<K,V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key,k->factory.get());
    }


}

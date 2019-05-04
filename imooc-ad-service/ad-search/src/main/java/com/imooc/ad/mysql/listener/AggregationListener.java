package com.imooc.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.imooc.ad.mysql.TemplateHolder;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.ParseTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     1.接受监听事件
 *     2.通过事件key，寻址观察者
 * </p>
 * @Date 2019/4/7 10:51
 */
@Component
@Slf4j
public class AggregationListener implements BinaryLogClient.EventListener {
    private String dbName;
    private String tbName;

    private Map<String, IListener> listenerMap = new HashMap<>();

    private final TemplateHolder templateHolder;

    @Autowired
    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }


    //该监听事件会不会被注册的实例消费？
    @Override
    public void onEvent(Event event) {
        //表中更新数据，会产生两条事件
        //想想是监听binlog数据，只取消费一份binlog，因此不会有并发的问题，类似队列的消费
        //       1.元数据事件，用来初始化事件的tbName与dbName
        //       2.真正的监听事件，用来得到监听的数据

        EventType eventType = event.getHeader().getEventType();

        if(eventType!=null&&eventType.equals(EventType.TABLE_MAP)){
                //  得到对应的事件数据
            TableMapEventData mapEventData = event.getData();
            this.dbName = mapEventData.getDatabase();
            this.tbName = mapEventData.getTable();
                // 事件1结束
            return;
        }

        //排除不需要的事件
        if (!eventType.equals(EventType.EXT_UPDATE_ROWS) &&
            !eventType.equals(EventType.EXT_DELETE_ROWS) &&
            !eventType.equals(EventType.EXT_WRITE_ROWS)
        ) {
            return;
        }
        //监听事件进入 校验参数 取出注册OK的监听器
        if(StringUtils.isEmpty(dbName)||StringUtils.isEmpty(tbName)){
            log.info("skip:{},dbName or tbName empty",eventType);
            return;
        }
        IListener iListener = listenerMap.get(genKey(dbName, tbName));
        if(iListener==null){
            log.info("skip:{},iListener is null",eventType);
            return;
        }

        try {
            BinlogRowData binlogRowData = buildRowData(event.getData());
            binlogRowData.setEventType(eventType);
            iListener.onEvent(binlogRowData);
        } catch (Exception e) {
            log.error("event:{}",event,e);
        } finally {
            this.dbName="";
            this.tbName="";
        }

    }

    /**
     * 封转EventData数据
     * @param data
     * @return
     */
    private BinlogRowData buildRowData(EventData data) {
        BinlogRowData binlogRowData = new BinlogRowData();
        ParseTable table = templateHolder.getTable(tbName);
        Map<Integer, String> indexColumnMap = table.getIndexColumnMap();
        binlogRowData.setParseTable(templateHolder.getTable(tbName));
        List<Map<String,String>> mapList = Lists.newArrayList();
        //每一行数据进行过滤与筛选
        for(Serializable[] ser:getAfterList(data)){
            for (int i = 0; i < ser.length; i++) {
                String columnName = indexColumnMap.get(i);
                if(StringUtils.isNotEmpty(columnName)){
                    Map<String, String> map = Maps.newHashMap();
                    map.put(columnName, ser[i].toString());
                    mapList.add(map);
               }
            }
        }
        binlogRowData.setAfter(mapList);
        return binlogRowData;
    }


    private  List<Serializable[]> getAfterList(EventData data) {
        if(data instanceof DeleteRowsEventData){
            return ((DeleteRowsEventData) data).getRows();
        }

        if(data instanceof WriteRowsEventData){
            return ((WriteRowsEventData) data).getRows();
        }

        if(data  instanceof  UpdateRowsEventData){
            return ((UpdateRowsEventData) data).getRows()
                    .stream().map(Map.Entry::getValue)//value 是更新后的数据
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    /**
     * 为观察者提供注册功能
     * @param _dbName
     * @param _tbName
     * @param iListener  观察者
     */
    public void register(String _dbName, String _tbName, IListener iListener) {
        log.info("register key {}:{} ",_dbName,_tbName);
        listenerMap.put(genKey(_dbName,_tbName),iListener);
    }


    /**
     * 生成注册key
     * @param _dbName
     * @param _tbName
     * @return
     */
    private String genKey(String _dbName,String _tbName){
        return String.format("%s:%s",_dbName,_tbName);
    }





}

package com.imooc.ad.mysql.listener;

import com.imooc.ad.mysql.constant.Constant;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.BinlogRowData;
import com.imooc.ad.mysql.dto.MySqlRowData;
import com.imooc.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Alex
 * @Desc <p>
 * 观察者实现
 * 1.注册到被观察者
 * 2.根据被观察者事件触发自己的操作
 * </p>
 * @Date 2019/4/7 11:01
 */
@Component
@Slf4j
public class IncrementListenerImpl implements IListener {

    @Autowired
    private AggregationListener aggregationListener;

    @Resource(name = "kafkaSender")
    private ISender sender;

    /**
     * 1.bean装载完成后，将自己注册到被观察者
     * 2.多个key，注册一个value，该bean主要用于处理事件，没有并发危险
     */
    @PostConstruct
    @Override
    public void register() {
        Constant.table2Db.forEach(
                (k, v) -> aggregationListener.register(v, k, this)
        );

    }

    /**
     * 接受被观察者投送的监听事件
     *
     * @param binlogRowData 封装事件
     */
    @Override
    public void onEvent(BinlogRowData binlogRowData) {

        MySqlRowData mySqlRowData = new MySqlRowData();
        mySqlRowData.setLevel(binlogRowData.getParseTable().getLevel());
        mySqlRowData.setOpType(OpType.of(binlogRowData.getEventType()));
        mySqlRowData.setTableName(binlogRowData.getParseTable().getTableName());
        List<Map<String, String>> fieldValueMap = mySqlRowData.getFieldValueMap();
        for (Map<String, String> map : binlogRowData.getAfter()) {
            Map<String, String> fieldMap = new HashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String columnName = entry.getKey();
                String columnValue = entry.getKey();
                fieldMap.put(columnName, columnValue);
            }
            //将after中map集合转成一个大Map
            fieldValueMap.add(fieldMap);
        }
        //将数据投递到消息系统
        sender.send(mySqlRowData);
    }
}

package com.imooc.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Alex
 * @Desc
 * <p>
 *
 *  测试监听binlog的开源组件
 *
 * </p>
 * @Date 2019/3/25 22:22
 */
@Slf4j
public class BinlogServiceTest {


    public static void main(String[] args) throws Exception{
        BinaryLogClient client = new BinaryLogClient(
                "127.0.0.1",
                3306,
                "root",
                "123456"
        );
        client.registerEventListener(event->{
            EventHeader header = event.getHeader();
            log.info("header:{}",header);
            EventData data = event.getData();
            if (data instanceof UpdateRowsEventData) {
                log.info("update info:{}", data);
            } else if (data instanceof WriteRowsEventData) {
                log.info("insert info:{}", data);
            } else if (data instanceof DeleteRowsEventData) {
                log.info("delete info:{}", data);
            }
        });
        client.connect();
    }
}

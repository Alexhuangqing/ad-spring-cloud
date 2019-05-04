package com.imooc.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.mysql.dto.MySqlRowData;
import com.imooc.ad.sender.ISender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/5/2 12:03
 */
@Component("kafkaSender")
@Slf4j
public class KafkaSender implements ISender {
    @Value("${adconf.kafka.topic}")
    private String kafkaTopic;
    @Autowired
    private KafkaTemplate kafkaTemplate;


    @Override
    @SuppressWarnings("unchecked")
    public void send(MySqlRowData rowData) {
        kafkaTemplate.send(kafkaTopic, JSON.toJSONString(rowData));
    }

    /**
     *
     * 监听topic
     * 测试kafka投递的消息
     * @param consumerRecord
     */
    @KafkaListener(topics="ad-search-mysql-data",groupId = "ad-search")
    public void processMysqlRowData(ConsumerRecord<?,?> consumerRecord){
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if(optional.isPresent()){
            Object object = optional.get();
            MySqlRowData mySqlRowData = JSON.parseObject(object.toString(), MySqlRowData.class);
            log.info("processMysqlRowData mySqlRowData:{}",mySqlRowData);
        }
    }
}

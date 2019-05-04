package com.imooc.ad.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.imooc.ad.mysql.listener.AggregationListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     监听mysql的客户端实例BinlogClient
 * </p>
 * @Date 2019/5/1 16:54
 */

@Component
@Slf4j
public class BinlogClient {

    private BinaryLogClient client;

    @Autowired
    BinlogConfig binlogConfig;

    @Autowired
    AggregationListener aggregationListener;

    /**
     * 该客户端进行数据库连接
     */
    public void connect(){
        client = new BinaryLogClient(
                binlogConfig.getHost(),
                binlogConfig.getPort(),
                binlogConfig.getUsername(),
                binlogConfig.getPassword()
        );
        if (StringUtils.isNotEmpty(binlogConfig.getBinlogName())
            && StringUtils.isNotEmpty(binlogConfig.getPassword())) {
            client.setBinlogFilename(binlogConfig.getBinlogName());
            client.setBinlogPosition(binlogConfig.getPosition());
        }
        try {
            log.info("mysql connect start...");
            client.connect();
            log.info("mysql connect down...");
        } catch (IOException e) {
            log.error("BinaryLogClient connect error", e);
        }

    }

    /**
     * 该客户端从数据库断开连接
     */
    public void  disconnect(){
        try {
            client.disconnect();
        } catch (IOException e) {
            log.error("BinaryLogClient disconnect error", e);
        }
    }

}

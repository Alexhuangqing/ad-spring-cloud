package com.imooc.ad.mysql.runner;

import com.imooc.ad.mysql.BinlogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author Alex
 * @Desc
 * <p>常用来自动执行某些功能</p>
 * @Date 2019/5/1 17:12
 */
@Component
public class BinlogRunner implements CommandLineRunner {

    @Autowired
    BinlogClient binlogClient;

    /**
     * 新开一个监听的线程
     * @param args
     * @throws Exception
     */
    @Override
    @Order(1)
    public void run(String... args) throws Exception {

        new Thread(()->{
            binlogClient.connect();
        }).start();


    }
}

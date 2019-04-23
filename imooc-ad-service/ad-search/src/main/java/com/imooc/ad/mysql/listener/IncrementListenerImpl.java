package com.imooc.ad.mysql.listener;

import com.imooc.ad.mysql.constant.Constant;
import com.imooc.ad.mysql.dto.BinlogRowData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     观察者实现
 *     1.注册到被观察者
 *     2.根据被观察者事件触发自己的操作
 * </p>
 * @Date 2019/4/7 11:01
 */
@Component
@Slf4j
public class IncrementListenerImpl implements IListener {

    @Autowired
    AggregationListener aggregationListener;

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
     * @param binlogRowData 封装事件
     */
    @Override
    public void onEvent(BinlogRowData binlogRowData) {

    }
}

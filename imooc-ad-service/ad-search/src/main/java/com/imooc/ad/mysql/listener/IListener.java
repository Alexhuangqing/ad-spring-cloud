package com.imooc.ad.mysql.listener;

import com.imooc.ad.mysql.dto.BinlogRowData;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     观察者模型之观察者
 * </p>
 * @Date 2019/4/7 10:52
 */
public interface IListener {

    void register();

    void onEvent(BinlogRowData binlogRowData);
}

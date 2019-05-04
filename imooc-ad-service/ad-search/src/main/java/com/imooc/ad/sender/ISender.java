package com.imooc.ad.sender;

import com.imooc.ad.mysql.dto.MySqlRowData;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     定义投递数据的接口
 * </p>
 * @Date 2019/5/2 8:03
 */
public interface ISender {

    void send(MySqlRowData rowData);
}

package com.imooc.ad.mysql.constant;

import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     枚举类型，对应3种操作指令
 * </p>
 * @Date 2019/3/30 16:55
 */
public enum OpType {

    ADD,
    UPDATE,
    DELETE,
    OTHER;

   public static  OpType of (EventType eventType){
        switch (eventType){
            case WRITE_ROWS:
                return ADD;
            case UPDATE_ROWS:
                return UPDATE;
            case DELETE_ROWS:
                return DELETE;
            default:
                return OTHER;
        }

    }



}

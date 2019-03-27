package com.imooc.ad.util;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/3/23 15:39
 */
public class CommonUtil {


    /**
     * 创建map中默认元素
     * @param key
     * @param map
     * @param factory
     * @param <K>
     * @param <V>
     * @return
     */
    public   static <K,V>  V  getOrCreate(K key, Map<K,V> map, Supplier<V> factory){
        return map.computeIfAbsent(key,k -> factory.get());
    }


    /**
     * 构建字符串
     * @param args
     * @return
     */
    public static String stringConcat(String... args) {

        StringBuilder result = new StringBuilder();
        for (String arg : args) {
            result.append(arg);
            result.append("-");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

}

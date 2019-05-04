package com.imooc.ad.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @Author Alex
 * @Desc <p></p>
 * @Date 2019/3/23 15:39
 */
@Slf4j
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


    // Tue Jan 01 08:00:00 CST 2019

    /**
     * 格式化插件监听的时mysql中的时间消息
     * {tableId=74,
     * includedColumns={0, 1, 2, 3, 4, 5, 6, 7},
     * rows=
     * [[1, 1, qw, 1, Tue Jan 01 08:00:00 CST 2019, Tue Jan 01 08:00:00 CST 2019, Thu Jan 01 08:00:00 CST 1970, Thu Jan 01 08:00:00 CST 1970]]}
     * @param dateString
     * @return
     */
    public static Date parseStringDate(String dateString) {

        try {

            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy",
                    Locale.US
            );
            return DateUtils.addHours(
                    dateFormat.parse(dateString),
                    -8
            );

        } catch (ParseException ex) {
            log.error("parseStringDate error: {}", dateString);
            return null;
        }
    }

}

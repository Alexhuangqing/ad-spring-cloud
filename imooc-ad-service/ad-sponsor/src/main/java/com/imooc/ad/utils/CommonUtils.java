package com.imooc.ad.utils;

import com.imooc.ad.exception.AdException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

/**
 * Created by Qinyi.
 */
public class CommonUtils {

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"
    };

    public static String md5(String value) {

        return DigestUtils.md5Hex(value).toUpperCase();
    }

    public static Date parseStringDate(String dateString)
            throws AdException {

        try {
            return DateUtils.parseDate(
                    dateString, parsePatterns
            );
        } catch (Exception ex) {
            throw new AdException(ex.getMessage());
        }
    }

    public static void main(String[] args) throws AdException {
        Date date1 = parseStringDate("2018-09-01");
        Date date2= parseStringDate("2018/11/01");
        Date date3= parseStringDate("2018.09.01");
        System.out.println(date1);
        System.out.println(date2);
        System.out.println(date3);
        Date date4= parseStringDate("2018/11/01 12:01:01");
        System.out.println(date4);
    }
}

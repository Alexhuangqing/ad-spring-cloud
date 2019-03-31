package com.imooc.ad.index;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.dump.DConstant;
import com.imooc.ad.dump.table.*;
import com.imooc.ad.handler.AdLevelDataHandler;
import com.imooc.ad.mysql.constant.OpType;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     1.读取的文件加载索引
 *     2.文件数据被抽象成行集合
 *     3.加载bean的时候的初始化操作
 * </p>
 * @Date 2019/3/31 15:13
 */

@Component
@DependsOn("dataTable")
@Slf4j
public class IndexFileLoader {

    /**
     * 初始化bean完成后，开始加载并解析本地文件
     */
    @PostConstruct
    public void init(){
        //读取 文件，批量添加 AdPlanTable 记录
        List<String> adPlans = loadDumpData(String.format(
                "%s%s",
                DConstant.DATA_ROOT_DIR,
                DConstant.AD_PLAN));
        adPlans.forEach(adPlan->
            AdLevelDataHandler.handleLevel2(
                    JSON.parseObject(adPlan, AdPlanTable.class),
                    OpType.ADD
            )
        );
        //读取 文件，批量添加 ad_creative 记录
        List<String>  adCreatives = loadDumpData(String.format(
                "%s%s",
                DConstant.DATA_ROOT_DIR,
                DConstant.AD_CREATIVE));
        adCreatives.forEach(adCreative->
            AdLevelDataHandler.handleLevel2(
                    JSON.parseObject(adCreative, AdCreativeTable.class),
                    OpType.ADD
            )
        );

        //读取 文件，批量添加 ad_unit 记录
        List<String>  adUnits = loadDumpData(String.format(
                "%s%s",
                DConstant.DATA_ROOT_DIR,
                DConstant.AD_UNIT));
        adUnits.forEach(adUnit->
            AdLevelDataHandler.handleLevel3(
                    JSON.parseObject(adUnit, AdUnitTable.class),
                    OpType.ADD
            )
        );

        //读取 文件，批量添加 ad_creative_unit 记录
        List<String>  adCreativeUnits = loadDumpData(String.format(
                "%s%s",
                DConstant.DATA_ROOT_DIR,
                DConstant.AD_CREATIVE_UNIT));
        adCreativeUnits.forEach(adCreativeUnit->
            AdLevelDataHandler.handleLevel4(
                    JSON.parseObject(adCreativeUnit, AdCreativeUnitTable.class),
                    OpType.ADD
            )
        );
        //读取 文件，批量添加 ad_unit_it 记录
        List<String>  unitIts = loadDumpData(String.format(
                "%s%s",
                DConstant.DATA_ROOT_DIR,
                DConstant.AD_UNIT_IT));
        unitIts.forEach(unitIt->
            AdLevelDataHandler.handleLevel4(
                    JSON.parseObject(unitIt, AdUnitItTable.class),
                    OpType.ADD
            )
        );
        //读取 文件，批量添加 ad_unit_district 记录
        List<String>  unitDistricts = loadDumpData(String.format(
                "%s%s",
                DConstant.DATA_ROOT_DIR,
                DConstant.AD_UNIT_DISTRICT));
        unitDistricts.forEach(unitDistrict->
            AdLevelDataHandler.handleLevel4(
                    JSON.parseObject(unitDistrict, AdUnitDistrictTable.class),
                    OpType.ADD
            )
        );

        //读取 文件，批量添加 ad_unit_keyword 记录
        List<String>  unitKeywords = loadDumpData(String.format(
                "%s%s",
                DConstant.DATA_ROOT_DIR,
                DConstant.AD_UNIT_KEYWORD));
        unitKeywords.forEach(unitKeyword->
            AdLevelDataHandler.handleLevel4(
                    JSON.parseObject(unitKeyword, AdUnitKeywordTable.class),
                    OpType.ADD
            )
        );





    }



    /**
     * java8 语法真是好用
     * @param fileName 本地文件路径
     * @return
     */
    public static List<String> loadDumpData(String  fileName) {

        try {
            @Cleanup BufferedReader bufferedReader =  Files.newBufferedReader(Paths.get(fileName));
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            log.error("loadDumpData fileName",fileName,e);
            throw  new RuntimeException(e.getMessage());
        }


    }

}

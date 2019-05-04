package com.imooc.ad.sender.index;

import com.google.common.collect.Lists;
import com.imooc.ad.dump.table.*;
import com.imooc.ad.handler.AdLevelDataHandler;
import com.imooc.ad.index.DataLevel;
import com.imooc.ad.mysql.constant.Constant;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.MySqlRowData;
import com.imooc.ad.sender.ISender;
import com.imooc.ad.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Alex
 * @Desc <p>
 * 利用增量索引投递数据
 * </p>
 * @Date 2019/5/2 8:04
 */
@Slf4j
@Component("indexSender")
public class IndexSender implements ISender {
    @Override
    public void send(MySqlRowData rowData) {
        String level = rowData.getLevel();
        if (DataLevel.LEVEL2.getLevel().equals(level)) {
            level2RowData(rowData);
        } else if (DataLevel.LEVEL3.getLevel().equals(level)) {
            level3RowData(rowData);
        } else if (DataLevel.LEVEL4.getLevel().equals(level)) {
            level4RowData(rowData);
        } else {
            log.error("ignore mySqlRowData:{}", rowData);
        }

    }


    /**
     * 投递第2level的数据
     *
     * @param rowData
     */
    private void level2RowData(MySqlRowData rowData) {
        //mysql中 data的参数
        String tableName = rowData.getTableName();
        List<Map<String, String>> fieldValueMap = rowData.getFieldValueMap();
        OpType opType = rowData.getOpType();
        //根据不同的表的类型得出不同的到处索引的方法
        if (Constant.AD_PLAN_TABLE_INFO.TABLE_NAME.equals(tableName)) {
            List<AdPlanTable> adPlanTables = Lists.newArrayList();
            for (Map<String, String> fieldAndValue : fieldValueMap) {
                AdPlanTable adPlanTable = new AdPlanTable();
                fieldAndValue.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            adPlanTable.setId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            adPlanTable.setUserId(Long.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            adPlanTable.setPlanStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            adPlanTable.setStartDate(CommonUtil.parseStringDate(v));
                            break;
                    }
                });
                adPlanTables.add(adPlanTable);
            }
            adPlanTables.forEach(adPlanTable
                    -> AdLevelDataHandler.handleLevel2(adPlanTable, opType));
        } else if (Constant.AD_CREATIVE_TABLE_INFO.TABLE_NAME.equals(tableName)) {
            List<AdCreativeTable> creativeTables = Lists.newArrayList();
            for (Map<String, String> fieldAndValue : fieldValueMap) {
                AdCreativeTable creativeTable = new AdCreativeTable();
                fieldAndValue.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            creativeTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                            creativeTable.setType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                            creativeTable.setMaterialType(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_HEIGHT:
                            creativeTable.setHeight(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_WIDTH:
                            creativeTable.setWidth(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            creativeTable.setAuditStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                            creativeTable.setAdUrl(v);
                            break;
                    }
                });
                creativeTables.add(creativeTable);
            }
            creativeTables.forEach(creativeTable
                    -> AdLevelDataHandler.handleLevel2(creativeTable, opType));

        }
    }

    private void level3RowData(MySqlRowData rowData) {

        if (rowData.getTableName().equals(
                Constant.AD_UNIT_TABLE_INFO.TABLE_NAME)) {

            List<AdUnitTable> unitTables = new ArrayList<>();

            for (Map<String, String> fieldValueMap :
                    rowData.getFieldValueMap()) {

                AdUnitTable unitTable = new AdUnitTable();

                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            unitTable.setUnitId(Long.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            unitTable.setUnitStatus(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            unitTable.setPositionType(Integer.valueOf(v));
                            break;
                        case Constant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            unitTable.setPlanId(Long.valueOf(v));
                            break;
                    }
                });

                unitTables.add(unitTable);
            }

            unitTables.forEach(u ->
                    AdLevelDataHandler.handleLevel3(u, rowData.getOpType()));
        } else if (rowData.getTableName().equals(
                Constant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME
        )) {
            List<AdCreativeUnitTable> creativeUnitTables = new ArrayList<>();

            for (Map<String, String> fieldValueMap :
                    rowData.getFieldValueMap()) {

                AdCreativeUnitTable creativeUnitTable = new AdCreativeUnitTable();

                fieldValueMap.forEach((k, v) -> {
                    switch (k) {
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            creativeUnitTable.setAdId(Long.valueOf(v));
                            break;
                        case Constant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            creativeUnitTable.setUnitId(Long.valueOf(v));
                            break;
                    }
                });

                creativeUnitTables.add(creativeUnitTable);
            }

            creativeUnitTables.forEach(
                    u -> AdLevelDataHandler.handleLevel3(u, rowData.getOpType())
            );
        }
    }

    private void level4RowData(MySqlRowData rowData) {

        switch (rowData.getTableName()) {

            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME:
                List<AdUnitDistrictTable> districtTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap :
                        rowData.getFieldValueMap()) {

                    AdUnitDistrictTable districtTable = new AdUnitDistrictTable();

                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                                districtTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_PROVINCE:
                                districtTable.setProvince(v);
                                break;
                            case Constant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                                districtTable.setCity(v);
                                break;
                        }
                    });

                    districtTables.add(districtTable);
                }

                districtTables.forEach(
                        d -> AdLevelDataHandler.handleLevel4(d, rowData.getOpType())
                );
                break;
            case Constant.AD_UNIT_IT_TABLE_INFO.TABLE_NAME:
                List<AdUnitItTable> itTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap :
                        rowData.getFieldValueMap()) {

                    AdUnitItTable itTable = new AdUnitItTable();

                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_UNIT_ID:
                                itTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_IT_TABLE_INFO.COLUMN_IT_TAG:
                                itTable.setItTag(v);
                                break;
                        }
                    });
                    itTables.add(itTable);
                }
                itTables.forEach(
                        i -> AdLevelDataHandler.handleLevel4(i, rowData.getOpType())
                );
                break;
            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME:

                List<AdUnitKeywordTable> keywordTables = new ArrayList<>();

                for (Map<String, String> fieldValueMap :
                        rowData.getFieldValueMap()) {
                    AdUnitKeywordTable keywordTable = new AdUnitKeywordTable();

                    fieldValueMap.forEach((k, v) -> {
                        switch (k) {
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                                keywordTable.setUnitId(Long.valueOf(v));
                                break;
                            case Constant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                                keywordTable.setKeyword(v);
                                break;
                        }
                    });
                    keywordTables.add(keywordTable);
                }

                keywordTables.forEach(
                        k -> AdLevelDataHandler.handleLevel4(k, rowData.getOpType())
                );
                break;
        }
    }
}

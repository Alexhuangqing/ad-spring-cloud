package com.imooc.ad.mysql;

import com.alibaba.fastjson.JSON;
import com.imooc.ad.mysql.constant.OpType;
import com.imooc.ad.mysql.dto.ParseTable;
import com.imooc.ad.mysql.dto.ParseTemplate;
import com.imooc.ad.mysql.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @Author Alex
 * @Desc
 * <p>
 *     1.初始化模板文件的入口
 *     2.提供对外查询模本配置的能力
 * </p>
 * @Date 2019/4/7 11:41
 */
@Component
@Slf4j
public class TemplateHolder {
    private ParseTemplate parseTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String TEMPLATE_SQL = "select TABLE_SCHEMA ,TABLE_NAME, COLUMN_NAME,ORDINAL_POSITION" +
                                        "  from information_schema.columns " +
                                        "  where TABLE_SCHEMA = ? and TABLE_NAME =?";
    
    @PostConstruct
    public void init(){
            loadJson("template.json");
    }

    //提供查询配置数据的能力
    public ParseTable getTable(String tableName) {
        return parseTemplate.getTableMap().get(tableName);
    }



    private void loadJson(String resourcePath) {
        //1.获得类加载器，将资源文件以流的方式输出
        //2.用JSON将输入流转成模板数据对象
        //3.查询元数据，将表中列名与开源插件中输出序号对应起来
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = contextClassLoader.getResourceAsStream(resourcePath);

        try {
            Template template = JSON.parseObject(is, Charset.defaultCharset(), Template.class);

            this.parseTemplate = ParseTemplate.parse(template);
        } catch (IOException e) {
            log.error(" loadJson error",e);
        }

        loadMetadata();

    }

    //加载表的元数据
    private void loadMetadata() {
       //利用库名+表名查询存储元数据的库表想要的结果集
        //查询后的列与操作列是包含关系
        for (Map.Entry<String, ParseTable> entry:parseTemplate.getTableMap().entrySet()
             ) {
            ParseTable parseTable = entry.getValue();
            Map<OpType, List<String>> opTypeFieldSetMap = parseTable.getOpTypeFieldSetMap();
            Map<Integer, String> indexColumnMap = parseTable.getIndexColumnMap();
            List<String> insetColumns = opTypeFieldSetMap.get(OpType.ADD);
            List<String> updateColumns = opTypeFieldSetMap.get(OpType.UPDATE);
            List<String> deleteColumns = opTypeFieldSetMap.get(OpType.DELETE);

            // RowMapper<T> rowMapper 会将结果集封装成一个对象
            // ResultSet rs是表示一行结果集，int i 表示当前结果集的位置rowNum
             /*  @FunctionalInterface
                public interface RowMapper<T> {
                    @Nullable
                    T mapRow(ResultSet var1, int var2) throws SQLException;
                }
            */
            jdbcTemplate.query  (TEMPLATE_SQL,
                    new Object[]{parseTemplate.getDatabase(),
                            parseTable.getTableName()
                    }, (rs, i) -> {
                        log.info("index:{} , rs:{}",i,rs);
                        String column = rs.getString("COLUMN_NAME");
                        int pos = rs.getInt("ORDINAL_POSITION");//pos是从1开始
                        if (
                                (CollectionUtils.isNotEmpty(insetColumns) && insetColumns.contains(column)) ||
                                (CollectionUtils.isNotEmpty(updateColumns) && updateColumns.contains(column)) ||
                                (CollectionUtils.isNotEmpty(deleteColumns) && deleteColumns.contains(column))
                        ) {
                            indexColumnMap.put(pos - 1, column);
                        }
                        return null;
                    });
        }
    }
}

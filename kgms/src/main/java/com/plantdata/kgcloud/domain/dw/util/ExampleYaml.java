package com.plantdata.kgcloud.domain.dw.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plantdata.kgcloud.domain.dataset.constant.FieldType;
import com.plantdata.kgcloud.sdk.rsp.CustomColumnRsp;
import com.plantdata.kgcloud.sdk.rsp.CustomTableRsp;
import com.plantdata.kgcloud.sdk.rsp.DWTableRsp;
import com.plantdata.kgcloud.sdk.req.DataSetSchema;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @program: kg-cloud-kgms
 * @description:
 * @author: czj
 * @create: 2020-04-01 17:35
 **/
public class ExampleYaml {

    private final static String JSON_START = "{";
    private final static String ARRAY_START = "[";
    private final static String ARRAY_STRING_START = "[\"";
    private final static String LONG = "bigint";
    private final static String INT = "int";
    private final static String VARCHAR = "varchar";
    private final static String CHAR = "char";
    private final static String TEXT = "text";
    private final static String LONGTEXT = "longtext";
    private final static String DATETIME = "datetime";
    private final static String SHORT = "tinyint";
    private final static String DATE = "date";
    private final static String TIMESTAMP = "timestamp";
    private final static String DOUBLE = "double";

    private static Map<Integer, String> attDataTypeMap = new HashMap<>();

    static {

        attDataTypeMap.put(0, "int");
        attDataTypeMap.put(1, "string");
        attDataTypeMap.put(2, "int");
        attDataTypeMap.put(3, "date");
        attDataTypeMap.put(8, "double");
        attDataTypeMap.put(9, "float");
        attDataTypeMap.put(10, "text");
        attDataTypeMap.put(13, "date");
    }


    public static byte[] create(List<DWTableRsp> tableRspList) {

        StringBuilder yaml = new StringBuilder();

        yaml.append("/*根据当前数据表以及字段生成的打标文件；\n" +
                "tables：当前数仓所拥有的表及表中文名；\n" +
                "relation：关系表有此字段，格式“-概念名>关系名>概念名”；\n" +
                "columns：数据表中的字段\n" +
                "columns.tag：该字段表示数值属性，格式“模式名.属性名”；\n" +
                "columns.type：该字段的属性值类型，包括“string”、“float”、“int”；\n" +
                "columns.explain：字段描述；\n" +
                "*/\n");

        yaml.append("tables:").append("\r\n");

        addTables(yaml, tableRspList);

        for (DWTableRsp tableRsp : tableRspList) {
            addTableColumns(yaml, tableRsp);
        }

        return yaml.toString().getBytes();
    }

    public static List<CustomTableRsp> createCustom(List<DWTableRsp> tableRspList) {

        List<CustomTableRsp> tableRsps = new ArrayList<>(tableRspList.size());

        for (DWTableRsp tableRsp : tableRspList) {
            tableRsps.add(addTableColumns(tableRsp));
        }

        return tableRsps;
    }

    private static CustomTableRsp addTableColumns(DWTableRsp tableRsp) {

        List<CustomColumnRsp> customColumnRsps = new ArrayList<>();

        List<DataSetSchema> fields = tableRsp.getSchema();
        if (fields != null && !fields.isEmpty()) {
            for (DataSetSchema field : fields) {
                String type = getGraphType(field.getType());
                customColumnRsps.add(CustomColumnRsp.builder()
                        .name(field.getField())
                        .type(type)
                        .comment(field.getDesc())
                        .build()
                );
            }
        }

        return CustomTableRsp.builder().tableName(tableRsp.getTableName()).columns(customColumnRsps).build();
    }

    private static void addTableColumns(StringBuilder yaml, DWTableRsp tableRsp) {
        yaml.append(tableRsp.getTableName()).append(":").append("\r\n");
        yaml.append(" relation:").append("\r\n");
        yaml.append(" columns:").append("\r\n");

        List<DataSetSchema> fields = tableRsp.getSchema();
        if (fields != null && !fields.isEmpty()) {
            for (DataSetSchema field : fields) {
                String type = getGraphType(field.getType());
                yaml.append("    - ").append(field.getField()).append(": { tag:   , type: ").append(type).append(" , explain: ").append(field.getDesc()).append("}").append("\r\n");
            }
        }

    }

    private static String getGraphType(Integer type) {

        if (attDataTypeMap.containsKey(type)) {
            return attDataTypeMap.get(type);
        } else {
            return "string";
        }
    }

    private static void addTables(StringBuilder yaml, List<DWTableRsp> tableRspList) {
        for (DWTableRsp tableRsp : tableRspList) {
            yaml.append(" - ").append(tableRsp.getTableName()).append(": ").append(tableRsp.getTitle()).append("\r\n");
        }
    }


    public static FieldType readMysqlType(String string) {
        FieldType type;
        if (string.startsWith(LONG)) {
            type = FieldType.LONG;
        } else if (string.startsWith(INT)) {
            type = FieldType.INTEGER;
        } else if (string.startsWith(VARCHAR)) {
            try {
                Integer size = Integer.parseInt(string.substring(string.indexOf("(") + 1, string.lastIndexOf(")")));
                if (size > 50) {
                    type = FieldType.TEXT;
                } else {
                    type = FieldType.STRING;
                }
            } catch (Exception e) {
                type = FieldType.STRING;
            }
        } else if (string.startsWith(CHAR)) {
            try {
                Integer size = Integer.parseInt(string.substring(string.indexOf("(") + 1, string.lastIndexOf(")")));
                if (size > 50) {
                    type = FieldType.TEXT;
                } else {
                    type = FieldType.STRING;
                }
            } catch (Exception e) {
                type = FieldType.STRING;
            }
        } else if (string.startsWith(TEXT)) {
            type = FieldType.TEXT;
        } else if (string.startsWith(LONGTEXT)) {
            type = FieldType.TEXT;
        } else if (string.startsWith(DATETIME)) {
            type = FieldType.DATETIME;
        } else if (string.startsWith(SHORT)) {
            type = FieldType.INTEGER;
        } else if (string.startsWith(DATE)) {
            type = FieldType.DATE;
        } else if (string.startsWith(TIMESTAMP)) {
            type = FieldType.DATETIME;
        } else if(string.startsWith(DOUBLE)){
            type = FieldType.DOUBLE;
        }else {
            type = FieldType.STRING;
        }
        return type;
    }
}

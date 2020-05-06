package com.plantdata.kgcloud.domain.dw.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.dw.rsp.*;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.rsp.*;
import com.plantdata.kgcloud.sdk.rsp.ModelRangeRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaserYaml2SchemaUtil {

    private static Map<String,String> attSetMap = new HashMap<>();
    private static Map<String,Integer> attDataTypeMap = new HashMap<>();
    public static List<Integer> attrTypeList = Lists.newArrayList();
    public static String ENUM_CONCEPT = "$concept";

    static {
        attSetMap.put("name", "name");
        attSetMap.put("meaningTag", "meaningTag");
        attSetMap.put("img", "img");
        attSetMap.put("desc", "abs");
        attSetMap.put("synonyms", "synonyms");

        attDataTypeMap.put("int",1);
        attDataTypeMap.put("float",2);
        attDataTypeMap.put("double",2);
        attDataTypeMap.put("datetime",4);
        attDataTypeMap.put("date",41);
        attDataTypeMap.put("time",42);
        attDataTypeMap.put("string",5);
        attDataTypeMap.put("map",8);
        attDataTypeMap.put("link",9);
        attDataTypeMap.put("text",10);

        attrTypeList.add(1);
        attrTypeList.add(2);
        attrTypeList.add(4);
        attrTypeList.add(41);
        attrTypeList.add(42);
        attrTypeList.add(5);
        attrTypeList.add(8);
        attrTypeList.add(9);
        attrTypeList.add(10);

    }

    public static void main(String[] args) {

        //yaml转json
        Yaml yaml = new Yaml();
        String document = "[{\"tableName\":\"t_trans_log\",\"typeEnum\":null,\"columns\":[{\"name\":\"id\",\"tag\":null,\"type\":\"int\",\"comment\":\"\"},{\"name\":\"seat\",\"tag\":\"席位.name\",\"type\":\"text\",\"comment\":\"席位\"},{\"name\":\"buy_num\",\"tag\":\"成交记录.买成交量\",\"type\":\"int\",\"comment\":\"\"},{\"name\":\"sale_num\",\"tag\":\"成交记录.卖成交量\",\"type\":\"int\",\"comment\":\"\"},{\"name\":\"trust_buy_num\",\"tag\":\"成交记录.委托买次数\",\"type\":\"int\",\"comment\":\"\"},{\"name\":\"trust_sale_num\",\"tag\":\"成交记录.委托卖次数\",\"type\":\"int\",\"comment\":\"\"},{\"name\":\"deal_time\",\"tag\":\"成交记录.成交时间\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"deal_price\",\"tag\":\"成交记录.成交单价\",\"type\":\"string\",\"comment\":\"\"},{\"name\":\"create_time\",\"tag\":null,\"type\":\"text\",\"comment\":\"\"},{\"name\":\"name\",\"tag\":\"成交记录.name\",\"type\":\"text\",\"comment\":\"\"}],\"relationRsps\":[{\"name\":\"产生\",\"domain\":\"席位\",\"range\":[\"成交记录\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]}]},{\"tableName\":\"t_system\",\"typeEnum\":null,\"columns\":[{\"name\":\"system\",\"tag\":\"系统.name\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"MAC\",\"tag\":\"系统.MAC\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"type\",\"tag\":\"系统.类型\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"counter\",\"tag\":null,\"type\":\"text\",\"comment\":\"\"},{\"name\":\"status\",\"tag\":\"系统.状态\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"createdate\",\"tag\":null,\"type\":\"date\",\"comment\":\"\"}],\"relationRsps\":null},{\"tableName\":\"t_relation\",\"typeEnum\":null,\"columns\":[{\"name\":\"system\",\"tag\":\"系统.name\",\"type\":\"text\",\"comment\":\"系统\"},{\"name\":\"user\",\"tag\":\"会员.name\",\"type\":\"text\",\"comment\":\"用户\"},{\"name\":\"dc\",\"tag\":\"数据中心.name\",\"type\":\"text\",\"comment\":\"数据中心\"},{\"name\":\"idc\",\"tag\":\"机房.name\",\"type\":\"text\",\"comment\":\"机房\"},{\"name\":\"rack\",\"tag\":\"机柜.name\",\"type\":\"text\",\"comment\":\"机架\"},{\"name\":\"camera_stand\",\"tag\":\"机位.name\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"equipment\",\"tag\":\"设备.name\",\"type\":\"text\",\"comment\":\"设备\"},{\"name\":\"IP\",\"tag\":\"IP.name\",\"type\":\"text\",\"comment\":\"ip\"},{\"name\":\"seat\",\"tag\":\"席位.name\",\"type\":\"text\",\"comment\":\"席位\"}],\"relationRsps\":[{\"name\":\"使用\",\"domain\":\"会员\",\"range\":[\"系统\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"系统\",\"range\":[\"数据中心\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"系统\",\"range\":[\"机房\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"系统\",\"range\":[\"机柜\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"系统\",\"range\":[\"机位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"所属设备\",\"domain\":\"系统\",\"range\":[\"设备\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"系统\",\"range\":[\"IP\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"席位\",\"domain\":\"系统\",\"range\":[\"席位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"会员\",\"range\":[\"数据中心\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"会员\",\"range\":[\"机房\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"会员\",\"range\":[\"机柜\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"会员\",\"range\":[\"机位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"托管\",\"domain\":\"设备\",\"range\":[\"会员\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"会员\",\"range\":[\"IP\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"拥有\",\"domain\":\"会员\",\"range\":[\"席位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"所属数据中心\",\"domain\":\"机房\",\"range\":[\"数据中心\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"数据中心\",\"range\":[\"机柜\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"数据中心\",\"range\":[\"机位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"数据中心\",\"range\":[\"设备\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"数据中心\",\"range\":[\"IP\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"数据中心\",\"range\":[\"席位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"所属机房\",\"domain\":\"机柜\",\"range\":[\"机房\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"机房\",\"range\":[\"机位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"机房\",\"range\":[\"设备\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"机房\",\"range\":[\"IP\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"机房\",\"range\":[\"席位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"机柜\",\"range\":[\"机位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"所属机柜\",\"domain\":\"设备\",\"range\":[\"机柜\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"机柜\",\"range\":[\"IP\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"机柜\",\"range\":[\"席位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"机位\",\"range\":[\"设备\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"机位\",\"range\":[\"IP\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"机位\",\"range\":[\"席位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"设备\",\"range\":[\"IP\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"设备\",\"range\":[\"席位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"IP\",\"range\":[\"席位\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]}]},{\"tableName\":\"t_rack_a\",\"typeEnum\":null,\"columns\":[{\"name\":\"rack\",\"tag\":\"机柜.name\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"dc\",\"tag\":\"数据中心.name\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"idc\",\"tag\":\"机房.name\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"rack_capacity\",\"tag\":\"机柜.机柜容量\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"row\",\"tag\":null,\"type\":\"text\",\"comment\":\"\"},{\"name\":\"column\",\"tag\":null,\"type\":\"text\",\"comment\":\"\"},{\"name\":\"enabled\",\"tag\":\"机柜.启用状态\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"on_position\",\"tag\":\"机柜.通电状态\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"power_type\",\"tag\":\"机柜.电源类型\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"hz\",\"tag\":\"机柜.电流类型\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"two_circuit_feed\",\"tag\":\"机柜.是否双路供电\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"lease_type\",\"tag\":\"机柜.租赁类型\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"kw\",\"tag\":\"机柜.最大额定功率\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"create_date\",\"tag\":null,\"type\":\"date\",\"comment\":\"\"},{\"name\":\"update_date\",\"tag\":null,\"type\":\"date\",\"comment\":\"\"},{\"name\":\"desc\",\"tag\":null,\"type\":\"text\",\"comment\":\"\"}],\"relationRsps\":[{\"name\":\"所属数据中心\",\"domain\":\"机柜\",\"range\":[\"数据中心\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"所属机房\",\"domain\":\"机柜\",\"range\":[\"机房\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"数据中心\",\"range\":[\"机房\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]}]},{\"tableName\":\"t_idc\",\"typeEnum\":null,\"columns\":[{\"name\":\"idc\",\"tag\":\"机房.name·\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"dc\",\"tag\":\"数据中心.name\",\"type\":\"text\",\"comment\":\"\"}],\"relationRsps\":[{\"name\":\"所属数据中心\",\"domain\":\"机房\",\"range\":[\"数据中心\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]}]},{\"tableName\":\"t_equipment\",\"typeEnum\":null,\"columns\":[{\"name\":\"ID\",\"tag\":null,\"type\":\"text\",\"comment\":\"\"},{\"name\":\"equipment\",\"tag\":\"设备.name\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"model\",\"tag\":\"设备.型号\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"type\",\"tag\":null,\"type\":\"text\",\"comment\":\"\"},{\"name\":\"image_url\",\"tag\":\"设备.图片\",\"type\":\"text\",\"comment\":\"\"},{\"name\":\"interspace\",\"tag\":\"设备.空间\",\"type\":\"int\",\"comment\":\"\"},{\"name\":\"power\",\"tag\":\"设备.功率\",\"type\":\"int\",\"comment\":\"\"},{\"name\":\"createDate\",\"tag\":\"设备.创建时间\",\"type\":\"text\",\"comment\":\"\"}],\"relationRsps\":null},{\"tableName\":\"t_dc\",\"typeEnum\":null,\"columns\":[{\"name\":\"dc\",\"tag\":\"数据中心.name\",\"type\":\"text\",\"comment\":\"数据中心\"},{\"name\":\"locality\",\"tag\":\"地区.name\",\"type\":\"text\",\"comment\":\"地点\"},{\"name\":\"type\",\"tag\":\"数据中心.类型\",\"type\":\"text\",\"comment\":\"类型\"},{\"name\":\"network_access\",\"tag\":\"数据中心.网络接入\",\"type\":\"text\",\"comment\":\"网络地址\"},{\"name\":\"ddress\",\"tag\":\"地址.name\",\"type\":\"text\",\"comment\":\"\"}],\"relationRsps\":[{\"name\":\"所在地区\",\"domain\":\"数据中心\",\"range\":[\"地区\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"地址\",\"domain\":\"数据中心\",\"range\":[\"地址\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]},{\"name\":\"\",\"domain\":\"地区\",\"range\":[\"地址\"],\"startTime\":\"\",\"endTime\":\"\",\"relationAttrs\":[{\"name\":\"\",\"tag\":\"\",\"type\":\"\",\"comment\":\"\"}]}]}]";
//        System.out.println(JacksonUtils.writeValueAsString(a));
        JSONObject jsonObject = new JSONObject();
        System.out.println(JSON.toJSONString(parserYaml2TagJson(jsonObject,null)));
    }

    public static List<PreBuilderConceptRsp> parserYaml2Schema(JSONObject json,List<DWTableRsp> tableRsps){

        if(json == null || json.isEmpty() || !json.containsKey("tables")){
            return new ArrayList<>();
        }

        Map<String,List<String>> tableFields = tableRsps.stream().collect(Collectors.toMap(DWTableRsp::getTableName,DWTableRsp::getFields));

        JSONArray tables = json.getJSONArray("tables");

        Map<String, PreBuilderConceptRsp> conceptRspMap = new HashMap<>();

        for(int i=0; i< tables.size(); i++){
            JSONObject tab = tables.getJSONObject(i);

            Set<String> key = tab.keySet();
            if(key.isEmpty()){
                continue;
            }

            String tableName = key.iterator().next();

            JSONObject tabJOSNObj = json.getJSONObject(tableName);

            if(tabJOSNObj == null){
                continue;
            }

            JSONArray columns = tabJOSNObj.getJSONArray("columns");
            JSONArray relations = tabJOSNObj.getJSONArray("relation");

            List<YamlColumn> columnList = convertColumn(columns,tableFields.get(tableName));
            List<YamlRelation> relationList = convertRelation(relations);

            Map<String,List<YamlColumn>> relationColumn = new HashMap<>();

            for(YamlColumn column : columnList){

                if(column.getIsRelationAttr()){
                    List<YamlColumn> relationAttrList = relationColumn.containsKey(column.getConceptOrRelationName()) ? relationColumn.get(column.getConceptOrRelationName()) : new ArrayList<>();
                    relationAttrList.add(column);
                    relationColumn.put(column.getConceptOrRelationName(),relationAttrList);
                }else{
                    PreBuilderConceptRsp concept = null;
                    if(conceptRspMap.containsKey(column.getConceptOrRelationName())){
                        concept = conceptRspMap.get(column.getConceptOrRelationName());

                        if(!concept.getTables().contains(tableName)){
                            concept.getTables().add(tableName);
                        }
                    }else{
                        concept = new PreBuilderConceptRsp();
                        concept.setName(column.getConceptOrRelationName());

                        List<String> t = new ArrayList<>();
                        t.add(tableName);
                        concept.setTables(t);

                        conceptRspMap.put(column.getConceptOrRelationName(),concept);
                    }

                    if(attSetMap.containsKey(column.getAttrName())){
                        //默认的属性不需要加属性定义
                        continue;
                    }
                    List<PreBuilderAttrRsp> attrs = concept.getAttrs() == null ? new ArrayList<>() : concept.getAttrs();
                    PreBuilderAttrRsp attrRsp = PreBuilderAttrRsp.builder().name(column.getAttrName()).attrType(0).dataType(attDataTypeMap.get(column.getType())).build();
                    attrRsp.setTables(Lists.newArrayList(tableName));

                    attrs.add(attrRsp);
                    concept.setAttrs(attrs);
                }
            }

            for(YamlRelation relation : relationList){

                String domain = relation.getDomain();
                String range = relation.getRange();

                if(!conceptRspMap.containsKey(domain)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_ATTR_DOMAIN_NOT_EXIST_ERROR);
                }

                if(!conceptRspMap.containsKey(range)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_ATTR_RANGE_NOT_EXIST_ERROR);
                }

                List<ModelRangeRsp> rangeRsps = Lists.newArrayList(ModelRangeRsp.builder().rangeName(range).build());


                PreBuilderConceptRsp conceptRsp = conceptRspMap.get(domain);
                PreBuilderAttrRsp attrRsp = PreBuilderAttrRsp.builder()
                        .attrType(1)
                        .name(relation.getRelationName())
                        .range(rangeRsps)
                        .tables(Lists.newArrayList(tableName))
                        .build();

                if(relationColumn.containsKey(relation.getRelationName())){

                    List<YamlColumn> relationAtt = relationColumn.get(relation.getRelationName());

                    List<PreBuilderRelationAttrRsp> attrs = new ArrayList<>();

                    for(YamlColumn column : relationAtt){

                        attrs.add(PreBuilderRelationAttrRsp.builder()
                                .dataType(attDataTypeMap.get(column.getType()))
                                .name(column.getAttrName())
                                .tables(Lists.newArrayList(tableName))
                                .build());
                    }
                    attrRsp.setRelationAttrs(attrs);

                }
                List<PreBuilderAttrRsp> attrs = conceptRsp.getAttrs() == null ? new ArrayList<>() : conceptRsp.getAttrs();
                attrs.add(attrRsp);
                conceptRsp.setAttrs(attrs);
            }
        }

        List<PreBuilderConceptRsp> rsList = new ArrayList();

        rsList.addAll(conceptRspMap.values());

        distinc(rsList);

        return rsList;
    }

    public static void distinc(List<PreBuilderConceptRsp> rsList) {

        if(rsList == null || rsList.isEmpty()){
            return ;
        }

        for(PreBuilderConceptRsp concept : rsList){

            List<PreBuilderAttrRsp> attrs = concept.getAttrs();
            if(attrs != null && !attrs.isEmpty()){

                Map<String,PreBuilderAttrRsp> existAttrList  = new HashMap<>();
                Iterator<PreBuilderAttrRsp> it = attrs.iterator();


                while (it.hasNext()){

                    PreBuilderAttrRsp attr = it.next();
                    if(existAttrList.containsKey(attr.getName())){


                        PreBuilderAttrRsp relation = existAttrList.get(attr.getName());
                        //引用的表合并
                        relation.getTables().addAll(attr.getTables());

                        //关系 合并边属性
                        if(attr.getAttrType().equals(1)){
                            List<PreBuilderRelationAttrRsp> relationAttrRsps = attr.getRelationAttrs();

                            if(relationAttrRsps != null && !relationAttrRsps.isEmpty()){

                                if(relation.getRelationAttrs() == null){
                                    relation.setRelationAttrs(new ArrayList<>());
                                }

                                if(relation.getRelationAttrs().isEmpty()){
                                    relation.getRelationAttrs().addAll(relationAttrRsps);
                                }else{

                                    Map<String,PreBuilderRelationAttrRsp> existRelaAttrs = relation.getRelationAttrs().stream().collect(Collectors.toMap(PreBuilderRelationAttrRsp::getName, Function.identity()));
//                                    relation.getRelationAttrs().forEach(relaAttr -> existRelaAttrs.add(relaAttr.getName()));

                                    for(PreBuilderRelationAttrRsp relationAttr : relationAttrRsps){
                                        if(!existRelaAttrs.containsKey(relationAttr.getName())){
                                            relation.getRelationAttrs().add(relationAttr);
                                            existRelaAttrs.put(relationAttr.getName(),relationAttr);
                                        }else{
                                            if(relationAttr.getTables() == null || relationAttr.getTables().isEmpty()){
                                                continue;
                                            }

                                            PreBuilderRelationAttrRsp relationAttrRsp = existRelaAttrs.get(relationAttr.getName());
                                            if(relationAttrRsp.getTables() == null){
                                                relationAttrRsp.setTables(new ArrayList<>());
                                            }

                                            for(String tableName : relationAttr.getTables()){
                                                if(!relationAttr.getTables().contains(tableName)){
                                                    relationAttr.getTables().add(tableName);
                                                }
                                            }

                                        }
                                    }
                                }


                            }

                        }

                        it.remove();
                    }else{
                        existAttrList.put(attr.getName(),attr);
                    }

                }
            }

        }

    }


    private static List<YamlRelation> convertRelation(JSONArray relations) {

        if(relations == null ||relations.isEmpty()){
            return new ArrayList<>();
        }

        List<YamlRelation> relation = new ArrayList<>();

        for(int i = 0; i< relations.size(); i++){
            String rela = relations.getString(i);

            String[] relationValue = rela.split(">");

            if(relationValue.length != 3){
                throw BizException.of(KgmsErrorCodeEnum.YAML_RELATION_PARSER_ERROR);
            }


            relation.add(YamlRelation.builder().domain(relationValue[0].trim()).range(relationValue[2].trim()).relationName(relationValue[1].trim()).build());
        }

        return relation;
    }


    public static List<YamlColumn> convertColumn(JSONArray columns,List<String> fields){

        if(columns == null ||columns.isEmpty()){
            throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_IS_EMTRY_ERROR);
        }

        List<YamlColumn> columnList = new ArrayList<>();
        for(int i=0; i<columns.size(); i++){

            JSONObject column = columns.getJSONObject(i);
            for (String key : column.keySet()) {

                if(fields == null || fields.isEmpty()){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMS_NOT_EXIST_IN_TABLE);
                }

                if(!fields.contains(key)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMS_NOT_EXIST_IN_TABLE);
                }

                JSONObject columnValue = column.getJSONObject(key);
                String tag = columnValue.getString("tag");

                if(StringUtils.isEmpty(tag)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TAG_NOT_EXIST);
                }

                String[] tags = tag.split("\\.");

                if(tags.length != 2){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TAG_PARSER_ERROR);
                }

                String type = columnValue.getString("type");

                if(StringUtils.isEmpty(type)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TYPE_NOT_EXIST);
                }

                if(!attDataTypeMap.containsKey(type)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TYPE_PARSER_ERROR);
                }


                YamlColumn col = YamlColumn.builder().attrName(tags[1]).type(type).columnName(key).conceptOrRelationName(tags[0]).build();
                if (tags[0].startsWith("<")) {
                    col.setIsRelationAttr(true);
                    col.setConceptOrRelationName(tags[0].substring(1, tags[0].length() - 1));
                } else {
                    col.setIsRelationAttr(false);
                }

                columnList.add(col);
                break;
            }

        }

        if(columnList.isEmpty()){
            throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_IS_EMTRY_ERROR);
        }

        Map<String,Boolean> columnNameMap = new HashMap<>();

        for(YamlColumn yamlColumn : columnList){

            if(yamlColumn.getIsRelationAttr()){
                continue;
            }

            if(!columnNameMap.containsKey(yamlColumn.getConceptOrRelationName())){
                columnNameMap.put(yamlColumn.getConceptOrRelationName(),false);
            }

            if("名称".equals(yamlColumn.getAttrName())){
                columnNameMap.put(yamlColumn.getConceptOrRelationName(),true);
            }

        }

        if(columnNameMap.isEmpty()){
            throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_NOT_EXIST_CONCEPT);
        }

        columnNameMap.forEach((k,v) -> {
            if(!v){
                throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_NOT_EXIST_CONCEPT_NAME);
            }
        });

        return columnList;
    }

    public static List<ModelSchemaConfigRsp> parserYaml2TagJson(JSONObject json, List<DWTableRsp> tableRsps) {
        if(json == null || json.isEmpty()){
            throw BizException.of(KgmsErrorCodeEnum.YAML_FILE_EMTRY_ERROR);
        }

        if(!json.containsKey("tables")){
            throw BizException.of(KgmsErrorCodeEnum.YAML_TABLES_NOT_EXIST_ERROR);
        }

        Map<String,List<String>> tableFields = tableRsps.stream().collect(Collectors.toMap(DWTableRsp::getTableName,DWTableRsp::getFields));

        JSONArray tables = json.getJSONArray("tables");

        if(tables.isEmpty()){
            throw BizException.of(KgmsErrorCodeEnum.YAML_TABLES_IS_EMTRY_ERROR);
        }

        List<ModelSchemaConfigRsp> rsList = new ArrayList<>(tables.size());

        for(int i=0; i< tables.size(); i++){
            JSONObject tab = tables.getJSONObject(i);

            Set<String> key = tab.keySet();
            if(key.isEmpty()){
                continue;
            }

            String tableName = key.iterator().next();

            JSONObject tabJOSNObj = json.getJSONObject(tableName);

            if(tabJOSNObj == null){
                throw BizException.of(KgmsErrorCodeEnum.YAML_TABLES_CONFIG_IS_EMTRY_ERROR);
            }

            JSONArray columns = tabJOSNObj.getJSONArray("columns");
            JSONArray relations = tabJOSNObj.getJSONArray("relation");

            List<YamlColumn> columnList = convertColumn(columns,tableFields.get(tableName));
            List<YamlRelation> relationList = convertRelation(relations);

            Set<String> entity = new HashSet<>();
            Set<ModelAttrBeanRsp> attrBeans = new HashSet<>();
            Set<ModelRelationBeanRsp> relationBeans = new HashSet<>();

            Map<String,List<YamlColumn>> relationColumn = new HashMap<>();

            for(YamlColumn column : columnList){

                if(column.getIsRelationAttr()){
                    List<YamlColumn> relationAttrList = relationColumn.containsKey(column.getConceptOrRelationName()) ? relationColumn.get(column.getConceptOrRelationName()) : new ArrayList<>();
                    relationAttrList.add(column);
                    relationColumn.put(column.getConceptOrRelationName(),relationAttrList);
                }else{
                    entity.add(column.getConceptOrRelationName());

                    if(attSetMap.containsKey(column.getAttrName())){
                        //默认的属性不需要加属性定义
                        continue;
                    }
                    ModelAttrBeanRsp attrBean = new ModelAttrBeanRsp();
                    attrBean.setDomain(column.getConceptOrRelationName());
                    attrBean.setDataType(attDataTypeMap.get(column.getType()));
                    attrBean.setName(column.getAttrName());
                    attrBeans.add(attrBean);
                }
            }

            for(YamlRelation relation : relationList){

                String domain = relation.getDomain();
                String range = relation.getRange();

                if(!entity.contains(domain)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_ATTR_DOMAIN_NOT_EXIST_ERROR);
                }

                if(!entity.contains(range)){
                    throw BizException.of(KgmsErrorCodeEnum.YAML_ATTR_RANGE_NOT_EXIST_ERROR);
                }


                ModelRelationBeanRsp relationBean = new ModelRelationBeanRsp();
                relationBean.setName(relation.getRelationName());
                relationBean.setDomain(domain);
                relationBean.setRange(Sets.newHashSet(range));

                if(relationColumn.containsKey(relation.getRelationName())){

                    List<YamlColumn> relationAtt = relationColumn.get(relation.getRelationName());

                    Set<ModelRelationAttrBeanRsp> attrs = new HashSet<>();

                    for(YamlColumn column : relationAtt){

                        ModelRelationAttrBeanRsp relationAttr = new ModelRelationAttrBeanRsp();
                        relationAttr.setName(column.getAttrName());
                        relationAttr.setDataType(attDataTypeMap.get(column.getType()));

                        attrs.add(relationAttr);
                    }
                    relationBean.setAttrs(attrs);

                }
                relationBeans.add(relationBean);
            }

            ModelSchemaConfigRsp modelSchemaConfigRsp = new ModelSchemaConfigRsp();
            modelSchemaConfigRsp.setEntity(entity);
            modelSchemaConfigRsp.setAttr(attrBeans);
            modelSchemaConfigRsp.setRelation(relationBeans);
            modelSchemaConfigRsp.setTableName(tableName);
            rsList.add(modelSchemaConfigRsp);
        }

        return rsList;
    }

    public static List<ModelSchemaConfigRsp> parserLabel2TagJson(List<CustomTableRsp> tableLabels, List<DWTableRsp> tableRsps) {

        Map<String,List<String>> tableFields = tableRsps.stream().collect(Collectors.toMap(DWTableRsp::getTableName,DWTableRsp::getFields));

        List<ModelSchemaConfigRsp> rsList = new ArrayList<>(tableRsps.size());

        for(CustomTableRsp label : tableLabels){

            List<YamlColumn> columnList = convertLabelColumn(label.getColumns(),tableFields.get(label.getTableName()));

            Set<String> entity = new HashSet<>();
            Set<ModelAttrBeanRsp> attrBeans = new HashSet<>();
            Set<ModelRelationBeanRsp> relationBeans = new HashSet<>();
            Map<String,Set<String>> enumMap = new HashMap<>();

            for(YamlColumn column : columnList){
                entity.add(column.getConceptOrRelationName());

                if(attSetMap.containsKey(column.getAttrName())){
                    //默认的属性不需要加属性定义
                    continue;
                }

                if(ENUM_CONCEPT.equals(column.getAttrName())){
                    if(label.getTypeEnum() == null ||!label.getTypeEnum().containsKey(column.getConceptOrRelationName()+"."+ENUM_CONCEPT) || label.getTypeEnum().get(column.getConceptOrRelationName()+"."+ENUM_CONCEPT).isEmpty()){
                        throw BizException.of(KgmsErrorCodeEnum.LABEL_ENUM_NOT_EMTRY);
                    }

                    if(enumMap.containsKey(column.getConceptOrRelationName())){
                        enumMap.get(column.getConceptOrRelationName()).addAll(label.getTypeEnum().get(column.getConceptOrRelationName()+"."+ENUM_CONCEPT));
                    }else{
                        enumMap.put(column.getConceptOrRelationName(),new HashSet<>(label.getTypeEnum().get(column.getConceptOrRelationName()+"."+ENUM_CONCEPT)));
                    }
                    continue;
                }

                ModelAttrBeanRsp attrBean = new ModelAttrBeanRsp();
                attrBean.setDomain(column.getConceptOrRelationName());
                attrBean.setDataType(attDataTypeMap.get(column.getType()));
                attrBean.setName(column.getAttrName());
                attrBeans.add(attrBean);
            }

            if(label.getRelationRsps() != null && !label.getRelationRsps().isEmpty()){

                for(CustomRelationRsp relation : label.getRelationRsps()){

                    if(relation.getName() == null || relation.getName().trim().isEmpty()){
                        continue;
                    }
                    String domain = relation.getDomain();
                    List<String> range = relation.getRange();

                    if(!entity.contains(domain)){
                        continue;
//                        throw BizException.of(KgmsErrorCodeEnum.YAML_ATTR_DOMAIN_NOT_EXIST_ERROR);
                    }

                    if(!entity.containsAll(range)){
                        continue;
//                        throw BizException.of(KgmsErrorCodeEnum.YAML_ATTR_RANGE_NOT_EXIST_ERROR);
                    }


                    ModelRelationBeanRsp relationBean = new ModelRelationBeanRsp();
                    relationBean.setName(relation.getName());
                    relationBean.setDomain(domain);
                    relationBean.setRange(Sets.newHashSet(range));

                    if(relation.getRelationAttrs() != null && !relation.getRelationAttrs().isEmpty()){

                        Set<ModelRelationAttrBeanRsp> attrs = new HashSet<>();

                        for(CustomColumnRsp column : relation.getRelationAttrs()){

                            ModelRelationAttrBeanRsp relationAttr = new ModelRelationAttrBeanRsp();
                            relationAttr.setName(column.getTag());
                            relationAttr.setDataType(attDataTypeMap.get(column.getType()));

                            attrs.add(relationAttr);
                        }
                        relationBean.setAttrs(attrs);

                    }
                    relationBeans.add(relationBean);
                }
            }

            //处理枚举值
            if(!enumMap.isEmpty()){

                //实体
                Set<String> ents = new HashSet<>();
                for(Iterator<String> it = entity.iterator(); it.hasNext();){
                    String ent = it.next();
                    if(enumMap.containsKey(ent)){
                        it.remove();
                        ents.addAll(enumMap.get(ent));
                    }
                }
                entity.addAll(ents);

                //数值属性
                Set<ModelAttrBeanRsp> attrs = new HashSet<>();
                for(Iterator<ModelAttrBeanRsp> it = attrBeans.iterator(); it.hasNext();){
                    ModelAttrBeanRsp attr = it.next();
                    if(enumMap.containsKey(attr.getDomain())){
                        it.remove();
                        Set<String> enumValues = enumMap.get(attr.getDomain());
                        for(String s : enumValues){
                            ModelAttrBeanRsp attrBean = new ModelAttrBeanRsp();
                            attrBean.setDomain(s);
                            attrBean.setDataType(attr.getDataType());
                            attrBean.setName(attr.getName());
                            attrs.add(attrBean);
                        }
                    }

                }
                attrBeans.addAll(attrs);


                //关系
                Set<ModelRelationBeanRsp> relations = new HashSet<>();
                for(Iterator<ModelRelationBeanRsp> it = relationBeans.iterator(); it.hasNext();){
                    ModelRelationBeanRsp relation = it.next();

                    Set<String> newRanges= new HashSet<>();
                    for(String range :relation.getRange()){
                        if(enumMap.containsKey(range)){
                            newRanges.addAll(enumMap.get(range));
                        }else{
                            newRanges.add(range);
                        }
                    }

                    if(enumMap.containsKey(relation.getDomain())){
                        it.remove();
                        Set<String> enumValues = enumMap.get(relation.getDomain());
                        for(String s : enumValues){
                            ModelRelationBeanRsp relationBeanRsp = new ModelRelationBeanRsp();
                            relationBeanRsp.setDomain(s);
                            relationBeanRsp.setAttrs(relation.getAttrs());
                            relationBeanRsp.setName(relation.getName());
                            relationBeanRsp.setRange(newRanges);
                            relations.add(relationBeanRsp);
                        }
                    }else{
                        relation.setRange(newRanges);
                    }


                }
                relationBeans.addAll(relations);
            }



            ModelSchemaConfigRsp modelSchemaConfigRsp = new ModelSchemaConfigRsp();
            modelSchemaConfigRsp.setEntity(entity);
            modelSchemaConfigRsp.setAttr(attrBeans);
            modelSchemaConfigRsp.setRelation(relationBeans);
            modelSchemaConfigRsp.setTableName(label.getTableName());
            rsList.add(modelSchemaConfigRsp);
        }

        return rsList;

    }


    private static List<YamlColumn> convertLabelColumn(List<CustomColumnRsp> columns, List<String> fields) {

        if(columns == null ||columns.isEmpty()){
            return Lists.newArrayList();
        }

        List<YamlColumn> columnList = new ArrayList<>();
        for(CustomColumnRsp column : columns){

            if(column.getTag() == null || StringUtils.isEmpty(column.getTag().trim())){
                continue;
            }

            if(fields == null || fields.isEmpty()){
                continue;
//                throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMS_NOT_EXIST_IN_TABLE);
            }

            if(!fields.contains(column.getName())){
                continue;
//                throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMS_NOT_EXIST_IN_TABLE);
            }


            if(StringUtils.isEmpty(column.getTag())){
                continue;
//                throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TAG_NOT_EXIST);
            }

            String[] tags = column.getTag().split("\\.");

            if(tags.length != 2){
                continue;
//                throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TAG_PARSER_ERROR);
            }

            String type = column.getType();

            if(StringUtils.isEmpty(type)){
                continue;
//                throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TYPE_NOT_EXIST);
            }

            if(!attDataTypeMap.containsKey(type)){
                continue;
//                throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_TYPE_PARSER_ERROR);
            }


            YamlColumn col = YamlColumn.builder().attrName(tags[1]).type(type).columnName(column.getName()).conceptOrRelationName(tags[0]).build();
            col.setIsRelationAttr(false);
            columnList.add(col);
        }

        if(columnList.isEmpty()){
            return columnList;
        }

        Map<String,Boolean> columnNameMap = new HashMap<>();

        for(YamlColumn yamlColumn : columnList){

            if(!columnNameMap.containsKey(yamlColumn.getConceptOrRelationName())){
                columnNameMap.put(yamlColumn.getConceptOrRelationName(),false);
            }

            if("name".equals(yamlColumn.getAttrName())){
                columnNameMap.put(yamlColumn.getConceptOrRelationName(),true);
            }

        }

        if(columnNameMap.isEmpty()){
            return columnList;
        }

        columnNameMap.forEach((k,v) -> {
            if(!v){
                throw BizException.of(KgmsErrorCodeEnum.YAML_COLUMN_NOT_EXIST_CONCEPT_NAME);
            }
        });

        return columnList;
    }
}

package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.AttributeStatisticsBean;
import ai.plantdata.kg.api.pub.req.ConceptStatisticsBean;
import ai.plantdata.kg.api.pub.req.RelationExtraInfoStatisticBean;
import ai.plantdata.kg.api.pub.req.RelationStatisticsBean;
import ai.plantdata.kg.common.bean.AttributeDefinition;
import ai.plantdata.kg.common.bean.ExtraInfo;
import com.plantdata.kgcloud.constant.AppErrorCodeEnum;
import com.plantdata.kgcloud.constant.AttributeValueType;
import com.plantdata.kgcloud.constant.StatisticResultTypeEnum;
import com.plantdata.kgcloud.domain.app.dto.StatisticDTO;
import com.plantdata.kgcloud.domain.app.util.PageUtils;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.constant.AttributeDataTypeEnum;
import com.plantdata.kgcloud.sdk.constant.DataSetStatisticEnum;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeAttrStatisticByAttrValueReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EdgeStatisticByConceptIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByAttrIdReq;
import com.plantdata.kgcloud.sdk.req.app.statistic.EntityStatisticGroupByConceptReq;
import com.plantdata.kgcloud.sdk.rsp.app.statistic.StatDataRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/11 9:59
 */
public class GraphStatisticConverter {


    public static AttributeStatisticsBean attrReqToAttributeStatisticsBean(int reSize, EntityStatisticGroupByAttrIdReq attrIdReq) {
        Integer appendId = attrIdReq.getEntityIds() == null || attrIdReq.getEntityIds().isEmpty() ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE;
        AttributeStatisticsBean statisticsBean = new AttributeStatisticsBean();
        statisticsBean.setEntityIds(JacksonUtils.writeValueAsString(attrIdReq.getEntityIds()));
        statisticsBean.setPos(NumberUtils.INTEGER_ZERO);
        statisticsBean.setSize(reSize);
        statisticsBean.setDirection(attrIdReq.getDirection());
        statisticsBean.setAppendId(appendId);
        statisticsBean.setAttributeId(attrIdReq.getAttrId());
        statisticsBean.setAllowValues(JacksonUtils.writeValueAsString(attrIdReq.getDirection()));
        return statisticsBean;
    }

    public static ConceptStatisticsBean entityReqConceptStatisticsBean(EntityStatisticGroupByConceptReq statisticReq) {
        Integer appendId = statisticReq.getEntityIds() == null || statisticReq.getEntityIds().isEmpty() ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE;
        ConceptStatisticsBean statisticsBean = new ConceptStatisticsBean();
        statisticsBean.setAllowTypes(JacksonUtils.writeValueAsString(statisticReq.getAllowTypes()));
        statisticsBean.setAppendId(appendId);
        statisticsBean.setDirection(statisticReq.getDirection());
        statisticsBean.setEntityIds(JacksonUtils.writeValueAsString(statisticReq.getEntityIds()));
        statisticsBean.setPos(NumberUtils.INTEGER_ZERO);
        statisticsBean.setSize(defaultStatisticSize(statisticReq.getSize()));
        return statisticsBean;
    }

    public static RelationStatisticsBean conceptIdReqConceptStatisticsBean(EdgeStatisticByConceptIdReq conceptIdReq) {
        RelationStatisticsBean statisticsBean = new RelationStatisticsBean();
        Integer appendId = CollectionUtils.isEmpty(conceptIdReq.getTripleIds()) ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE;
        statisticsBean.setAllowAtts(JacksonUtils.writeValueAsString(conceptIdReq.getAllowAtts()));
        statisticsBean.setConceptId(conceptIdReq.getConceptId());
        statisticsBean.setAppendId(appendId);
        statisticsBean.setStartTime(conceptIdReq.getFromTime());
        statisticsBean.setEndTime(conceptIdReq.getToTime());
        statisticsBean.setPos(NumberUtils.INTEGER_ZERO);
        statisticsBean.setSize(defaultStatisticSize(conceptIdReq.getSize()));
        return statisticsBean;
    }

    public static RelationExtraInfoStatisticBean edgeAttrReqToRelationExtraInfoStatisticBean(EdgeAttrStatisticByAttrValueReq attrValueReq) {
        RelationExtraInfoStatisticBean statisticBean = new RelationExtraInfoStatisticBean();
        Integer appendId = CollectionUtils.isEmpty(attrValueReq.getTripleIds()) ? NumberUtils.INTEGER_ZERO : NumberUtils.INTEGER_ONE;
        statisticBean.setAllowValues(JacksonUtils.writeValueAsString(attrValueReq.getAllowValues()));
        statisticBean.setAttributeId(attrValueReq.getAttrId());
        statisticBean.setAppendId(appendId);
        statisticBean.setSeqNo(attrValueReq.getSeqNo());
        statisticBean.setTripleIds(JacksonUtils.writeValueAsString(attrValueReq.getTripleIds()));
        statisticBean.setPos(NumberUtils.INTEGER_ZERO);
        statisticBean.setSize(defaultStatisticSize(attrValueReq.getSize()));
        statisticBean.setDirection(attrValueReq.getSort());
        return statisticBean;
    }

    public static Object statisticByType(List<StatisticDTO> dataMapList, int returnType, StatisticResultTypeEnum resultType) {
        if (returnType == DataSetStatisticEnum.KV.getValue()) {
            return dataMapList.stream().map(s -> {
                Map<String, Object> map = new HashMap<>();
                String value = StatisticResultTypeEnum.NAME.equals(resultType) ? s.getName() : s.getValue();
                map.put("name", value);
                map.put("value", s.getTotal());
                Object relation = s.getRelation();
                Object entity = s.getEntity();
                if (relation != null) {
                    map.put("ids", relation);
                }
                if (relation != null) {
                    map.put("ids", entity);
                }
                return map;
            }).collect(Collectors.toList());
        }
        StatDataRsp statDataRsp = new StatDataRsp();
        if (CollectionUtils.isEmpty(dataMapList)) {
            return statDataRsp;
        }
        List<String> xData = new ArrayList<>();
        List<Long> sData = new ArrayList<>();

        dataMapList.forEach(data -> {
            String value = StatisticResultTypeEnum.NAME.equals(resultType) ? data.getName() : data.getValue();
            xData.add(value);
            sData.add(data.getTotal());
        });

        statDataRsp.addData2X(xData);
        statDataRsp.addData2Series(sData);
        return statDataRsp;
    }

    public static Integer reBuildResultSize(Integer size, Integer valueType, AttributeDataTypeEnum dataType) {
        if (AttributeValueType.isNumeric(valueType) && AttributeDataTypeEnum.DATE_SET.contains(dataType)) {
            return Integer.MAX_VALUE;
        }
        return size == null ? 10 : size;
    }

    private static int defaultStatisticSize(Integer size) {
        if (size.equals(NumberUtils.INTEGER_MINUS_ONE)) {
            return Integer.MAX_VALUE - NumberUtils.INTEGER_ONE;
        }
        return PageUtils.DEFAULT_SIZE;
    }

    public static AttributeDataTypeEnum edgeAttrDataType(int seqNo, AttributeDefinition attrDef) {
        if (seqNo == -1 || seqNo == -2) {
            return AttributeDataTypeEnum.DATETIME;
        }
        Optional<ExtraInfo> firstOpt = attrDef.getExtraInfo().stream().filter(a -> a.getSeqNo() == seqNo).findFirst();
        if (!firstOpt.isPresent()) {
            throw BizException.of(AppErrorCodeEnum.EDGE_ATTR_DEF_NULL);
        }
        return AttributeDataTypeEnum.parseById(firstOpt.get().getDataType());
    }
}

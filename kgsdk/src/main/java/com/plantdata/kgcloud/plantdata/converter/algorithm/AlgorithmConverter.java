package com.plantdata.kgcloud.plantdata.converter.algorithm;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.rule.BusinessGraphBean;
import com.plantdata.kgcloud.plantdata.rsp.app.EntityBeanAnalysisa;
import com.plantdata.kgcloud.plantdata.rsp.app.GraphAnalysisaBean;
import com.plantdata.kgcloud.sdk.req.app.algorithm.BusinessGraphRsp;
import com.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import lombok.NonNull;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/31 10:55
 */
public class AlgorithmConverter extends BasicConverter {

    public static BusinessGraphRsp businessGraphBeanToRsp(@NonNull BusinessGraphBean graphBean) {
        return copy(graphBean, BusinessGraphRsp.class);
    }

    public static BusinessGraphBean businessGraphRspToBean(@NonNull BusinessGraphRsp graphRsp) {
        return copy(graphRsp, BusinessGraphBean.class);
    }

    public static GraphAnalysisaBean complexGraphVisualRspToGraphAnalysisaBean(@NonNull ComplexGraphVisualRsp visualRsp) {
        GraphAnalysisaBean bean = new GraphAnalysisaBean();
        bean.setEntityList(toListNoNull(visualRsp.getEntityList(), AlgorithmConverter::coordinatesEntityRspToEntityBeanAnalysisa));
        return bean;
    }

    private static EntityBeanAnalysisa coordinatesEntityRspToEntityBeanAnalysisa(@NonNull ComplexGraphVisualRsp.CoordinatesEntityRsp entityRsp) {
        EntityBeanAnalysisa analysis = new EntityBeanAnalysisa();
        analysis.setName(entityRsp.getName());
        analysis.setCluster(entityRsp.getCluster());
        analysis.setClassId(entityRsp.getClassId() == null ? NumberUtils.INTEGER_ZERO : entityRsp.getClassId());
        analysis.setClassIdList(Lists.newArrayList(entityRsp.getClassId()));
        analysis.setConceptId(entityRsp.getConceptId());
        analysis.setConceptIdList(entityRsp.getConceptIdList());
        analysis.setCreationTime(entityRsp.getCreationTime());
        analysis.setDistance(entityRsp.getDistance());
        analysis.setMeaningTag(entityRsp.getMeaningTag());
        return analysis;
    }
}

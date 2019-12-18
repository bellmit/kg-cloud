package com.plantdata.kgcloud.domain.app.converter;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.constant.MetaDataInfo;
import com.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import com.plantdata.kgcloud.sdk.rsp.app.MetaDataInterface;
import com.plantdata.kgcloud.sdk.rsp.app.explore.StyleRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.TagRsp;
import com.plantdata.kgcloud.util.DateUtils;
import com.plantdata.kgcloud.util.JacksonUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/2 11:27
 */
public class MetaConverter {

    private static final String LABEL_STYLE = "labelStyle";
    private static final String NODE_STYLE = "nodeStyle";

    public static void fillMetaWithNoNull(Map<String, Object> metaData, MetaDataInterface metaDataImpl) {
        if (metaData.containsKey(MetaDataInfo.SCORE.getFieldName())) {
            Object o = metaData.get(MetaDataInfo.SCORE.getFieldName());
            metaDataImpl.setScore((Double) o);
        }
        if (metaData.containsKey(MetaDataInfo.RELIABILITY.getFieldName())) {
            Object o = metaData.get(MetaDataInfo.RELIABILITY.getFieldName());
            metaDataImpl.setReliability((Double) o);
        }
        if (metaData.containsKey(MetaDataInfo.BATCH_NO.getFieldName())) {
            Object o = metaData.get(MetaDataInfo.BATCH_NO.getFieldName());
            metaDataImpl.setBatch(o.toString());
        }
        if (metaData.containsKey(MetaDataInfo.FROM_TIME.getFieldName())) {
            Object o = metaData.get(MetaDataInfo.FROM_TIME.getFieldName());
            metaDataImpl.setStartTime(DateUtils.parseDatetime(o.toString()));
        }
        if (metaData.containsKey(MetaDataInfo.TO_TIME.getFieldName())) {
            Object o = metaData.get(MetaDataInfo.TO_TIME.getFieldName());
            metaDataImpl.setEndTime(DateUtils.parseDatetime(o.toString()));
        }
        if (metaData.containsKey(MetaDataInfo.ENTITY_LINK.getFieldName())) {
            metaDataImpl.setEntityLinks((List<EntityLinkVO>) metaData.get(MetaDataInfo.ENTITY_LINK.getFieldName()));
        }
        if (metaData.containsKey(MetaDataInfo.OPEN_GIS.getFieldName())) {
            metaDataImpl.setOpenGis((Boolean) metaData.get(MetaDataInfo.OPEN_GIS.getFieldName()));
        }
        if (metaData.containsKey(MetaDataInfo.GIS_COORDINATE.getFieldName())) {
            List<Double> list = (List) metaData.get(MetaDataInfo.GIS_COORDINATE.getFieldName());
            metaDataImpl.setLat(list.get(0));
            metaDataImpl.setLng(list.get(1));
        }
        if (metaData.containsKey(MetaDataInfo.GIS_ADDRESS.getFieldName())) {
            metaDataImpl.setAddress((String) metaData.get(MetaDataInfo.GIS_ADDRESS.getFieldName()));
        }
        if (metaData.containsKey(MetaDataInfo.TAG.getFieldName())) {
            List<Map<String, Object>> tagMaps = (List<Map<String, Object>>) metaData.get(MetaDataInfo.TAG.getFieldName());
            List<TagRsp> tagList = Lists.newArrayListWithCapacity(tagMaps.size());
            for (Map<String, Object> map : tagMaps) {
                try {
                    tagList.add(JacksonUtils.getInstance().readValue(JacksonUtils.writeValueAsString(map), TagRsp.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            metaDataImpl.setTags(tagList);
        }
        if (metaData.containsKey(MetaDataInfo.ADDITIONAL.getFieldName())) {
            Map<String, Object> objectMap = (Map<String, Object>) metaData.get(MetaDataInfo.ADDITIONAL.getFieldName());
            metaDataImpl.setAdditional(objectMap);
            if (objectMap.containsKey(LABEL_STYLE)) {
                metaDataImpl.setLabelStyle((Map<String, Object>) objectMap.get(LABEL_STYLE));
            }
            if (objectMap.containsKey(NODE_STYLE)) {
                metaDataImpl.setNodeStyle((Map<String, Object>) objectMap.get(NODE_STYLE));
            }

        }
    }
}

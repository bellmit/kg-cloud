package com.plantdata.kgcloud.domain.app.converter;

import ai.plantdata.kg.api.pub.req.EntityAttributesObjectFrom;
import ai.plantdata.kg.api.pub.resp.EntityVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 16:55
 */
public class KnowledgeRecommendConverter {

    public static EntityAttributesObjectFrom reqToFrom(KnowledgeRecommendReq req) {
        EntityAttributesObjectFrom from = new EntityAttributesObjectFrom();
        from.setEntityId(req.getEntityId());
        from.setAttributeIds(req.getAllowAttrs());
        if (!CollectionUtils.isEmpty(req.getAllowAttrs())) {
            Map<Integer, Integer> directionMap = Maps.newHashMapWithExpectedSize(req.getAllowAttrs().size());
            Map<Integer, Integer> sizeMap = Maps.newHashMapWithExpectedSize(req.getAllowAttrs().size());
            for (int i = 0; i < req.getAllowAttrs().size(); i++) {
                directionMap.put(req.getAllowAttrs().get(i), req.getDirection());
                sizeMap.put(req.getAllowAttrs().get(i), req.getSize());
            }
            from.setDirections(directionMap);
            from.setSizes(sizeMap);

        }
        return from;
    }

    public static List<ObjectAttributeRsp> voToRsp(@NonNull Map<Integer, Set<Long>> data, List<EntityVO> entityList) {
        List<ObjectAttributeRsp> attributeRspList = Lists.newArrayListWithCapacity(data.size());

        Map<Long, EntityVO> resultIndexMap = CollectionUtils.isEmpty(entityList) ? Collections.emptyMap()
                : entityList.stream().collect(Collectors.toMap(EntityVO::getId, Function.identity()));

        for (Map.Entry<Integer, Set<Long>> entry : data.entrySet()) {
            List<PromptEntityRsp> rspList = Lists.newArrayListWithCapacity(entry.getValue().size());
            for (Long entityId : entry.getValue()) {
                EntityVO entityVO = resultIndexMap.get(entityId);
                if (entityVO == null) {
                    continue;
                }
                rspList.add(new PromptEntityRsp(entityVO.getId(), entityVO.getName(), entityVO.getMeaningTag(), entityVO.getConceptId(), EntityTypeEnum.ENTITY));
            }
            attributeRspList.add(new ObjectAttributeRsp(entry.getKey(), rspList));
        }

        return attributeRspList;
    }
}

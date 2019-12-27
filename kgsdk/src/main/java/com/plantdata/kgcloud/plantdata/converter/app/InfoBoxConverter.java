package com.plantdata.kgcloud.plantdata.converter.app;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.plantdata.kgcloud.plantdata.bean.EntityLink;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.app.InfoBoxParameter;
import com.plantdata.kgcloud.plantdata.req.app.InfoBoxParameterMore;
import com.plantdata.kgcloud.plantdata.req.common.DataLinks;
import com.plantdata.kgcloud.plantdata.req.common.ExtraKVBean;
import com.plantdata.kgcloud.plantdata.req.common.KVBean;
import com.plantdata.kgcloud.plantdata.req.common.Links;
import com.plantdata.kgcloud.plantdata.req.common.Tag;
import com.plantdata.kgcloud.plantdata.req.entity.EntityBean;
import com.plantdata.kgcloud.plantdata.req.entity.EntityProfileBean;
import com.plantdata.kgcloud.plantdata.req.explore.common.EntityLinksBean;
import com.plantdata.kgcloud.sdk.constant.EntityTypeEnum;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.main.DataLinkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.EntityLinksRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxConceptRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.LinksRsp;
import lombok.NonNull;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/16 17:03
 */
public class InfoBoxConverter extends BasicConverter {

    public static InfoBoxReq infoBoxParameterToInfoBoxReq(InfoBoxParameter infoBoxParam) {
        InfoBoxReq infoBoxReq = new InfoBoxReq();
        infoBoxReq.setAllowAttrs(infoBoxParam.getAllowAtts());
        infoBoxReq.setId(infoBoxParam.getId());
        infoBoxReq.setRelationAttrs(infoBoxParam.getIsRelationAtts());
        infoBoxReq.setAllowAttrsKey(infoBoxParam.getAllowAttsKey());
        return infoBoxReq;
    }

    public static BatchInfoBoxReq infoBoxParameterMoreToBatchInfoBoxReq(@NonNull InfoBoxParameterMore parameterMore) {
        BatchInfoBoxReq infoBoxReq = new BatchInfoBoxReq();
        infoBoxReq.setAllowAttrs(parameterMore.getAllowAtts());
        infoBoxReq.setAllowAttrsKey(parameterMore.getAllowAttsKey());
        infoBoxReq.setRelationAttrs(parameterMore.getIsRelationAtts());
        infoBoxReq.setIds(parameterMore.getIds());
        return infoBoxReq;
    }

    public static EntityProfileBean infoBoxRspToEntityProfileBean(InfoBoxRsp infoBoxRsp) {
        EntityProfileBean entityProfileBean = new EntityProfileBean();
        entityProfileBean.setSelf(entityLinksRspToEntityLinksBean(infoBoxRsp.getSelf()));
        entityProfileBean.setAtts(Sets.newHashSet(listToRsp(infoBoxRsp.getAttrs(), InfoBoxConverter::infoBoxAttrRspToKVBean)));
        entityProfileBean.setReAtts(Sets.newHashSet(listToRsp(infoBoxRsp.getReAttrs(), InfoBoxConverter::infoBoxAttrRspToKVBean)));
        entityProfileBean.setPars(listToRsp(infoBoxRsp.getParents(), InfoBoxConverter::infoBoxConceptRspToEntityBean));
        entityProfileBean.setSons(listToRsp(infoBoxRsp.getSons(), InfoBoxConverter::infoBoxConceptRspToEntityBean));
        return entityProfileBean;
    }


    private static EntityBean infoBoxConceptRspToEntityBean(@NonNull InfoBoxConceptRsp conceptRsp) {
        EntityBean entityBean = new EntityBean();
        entityBean.setId(conceptRsp.getId());
        entityBean.setName(conceptRsp.getName());
        entityBean.setClassId(conceptRsp.getId());
        entityBean.setClassIdList(Lists.newArrayList(conceptRsp.getId()));
        entityBean.setConceptIdList(Lists.newArrayList(conceptRsp.getId()));
        entityBean.setConceptId(conceptRsp.getId());
        entityBean.setImg(conceptRsp.getImageUrl());
        entityBean.setMeaningTag(conceptRsp.getMeaningTag());
        entityBean.setType(EntityTypeEnum.CONCEPT.getValue());
        return entityBean;
    }

    private static KVBean<String, List<EntityBean>> infoBoxAttrRspToKVBean(@NonNull InfoBoxRsp.InfoBoxAttrRsp attrRsp) {
        List<EntityBean> entityBeans = listToRsp(attrRsp.getEntityList(), PromptConverter::promptEntityRspToEntityBean);
        return new KVBean<>(attrRsp.getAttrDefName(), entityBeans, attrRsp.getAttrDefId());
    }

    private static EntityLinksBean entityLinksRspToEntityLinksBean(EntityLinksRsp entityLinksRsp) {
        EntityLinksBean oldBean = new EntityLinksBean();
        oldBean.setClassId(entityLinksRsp.getConceptId());
        oldBean.setId(entityLinksRsp.getId());
        consumerIfNoNull(entityLinksRsp.getImg(), a -> oldBean.setImg(a.getHref()));
        oldBean.setMeaningTag(entityLinksRsp.getMeaningTag());
        oldBean.setName(entityLinksRsp.getName());
        consumerIfNoNull(entityLinksRsp.getType(), oldBean::setType);
        oldBean.setTags(listToRsp(entityLinksRsp.getTags(), a -> copy(a, Tag.class)));
        oldBean.setDataLinks(listToRsp(entityLinksRsp.getDataLinks(), InfoBoxConverter::dataLinkRspToDataLinks));
        List<EntityLink> entityLinks = listToRsp(entityLinksRsp.getEntityLinks(), a -> copy(a, EntityLink.class));
        consumerIfNoNull(entityLinks, a -> oldBean.setEntityLinks(Sets.newHashSet(a)));
        oldBean.setExtra(listToRsp(entityLinksRsp.getExtraList(), a -> InfoBoxConverter.extraRspToExtraKVBean(a, entityLinksRsp.getConceptId())));
        return oldBean;
    }

    private static ExtraKVBean extraRspToExtraKVBean(EntityLinksRsp.ExtraRsp extraRsp, Long domain) {
        ExtraKVBean extraKVBean = new ExtraKVBean();
        extraKVBean.setAttDefid(extraRsp.getAttrId());
        extraKVBean.setDomain(domain);
        extraKVBean.setType(extraRsp.getDataType());
        extraKVBean.setK(extraRsp.getName());
        extraKVBean.setV(extraRsp.getValue().toString());
        return extraKVBean;
    }

    private static DataLinks dataLinkRspToDataLinks(DataLinkRsp dataLink) {
        DataLinks dataLinks = new DataLinks();
        consumerIfNoNull(dataLink.getDataSetId(), a -> dataLinks.setDataSetId(a.intValue()));
        dataLinks.setDataSetTitle(dataLink.getDataSetTitle());
        dataLinks.setLinks(listToRsp(dataLink.getLinks(), InfoBoxConverter::linkRspToLinks));
        return dataLinks;
    }

    private static Links linkRspToLinks(LinksRsp linksRsp) {
        Links links = new Links();
        links.setDataId(linksRsp.getDataId());
        links.setDataTitle(linksRsp.getDataTitle());
        links.setScore(linksRsp.getScore());
        links.setSource(links.getSource());
        return links;
    }
}

package com.plantdata.kgcloud.plantdata.converter.semantic;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.plantdata.kgcloud.plantdata.bean.rule.RuleBean;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.req.reason.InferenceParameter;
import com.plantdata.kgcloud.sdk.req.GraphConfReasonReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.ReasoningReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfReasonRsp;
import com.plantdata.kgcloud.util.JacksonUtils;
import com.plantdata.kgcloud.util.JsonUtils;
import lombok.NonNull;

import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/26 14:32
 */
public class ReasonConverter extends BasicConverter {

    public static ReasoningReq inferenceParameterToReasoningReq(@NonNull InferenceParameter param) {
        Map<String, Object> ruleConfig = Maps.newHashMap();
        ruleConfig.put("pathRuleList", JSON.parse(param.getPathRuleList()));
        ruleConfig.put("rangeList", param.getRangeList());
        ruleConfig.put("domain", param.getDomain());
        ruleConfig.put("type", param.getDomain());
        consumerIfNoNull(param.getAttrId(), a -> ruleConfig.put("attrId", param.getAttrId()));
        consumerIfNoNull(param.getName(), a -> ruleConfig.put("name", a));
        ReasoningReq reasoningReq = new ReasoningReq();
        reasoningReq.setIds(JsonUtils.jsonToList(param.getIds(), Long.class));
        reasoningReq.setPos(param.getPageNo());
        reasoningReq.setRuleConfig(JacksonUtils.writeValueAsString(Lists.newArrayList(ruleConfig)));
        reasoningReq.setSize(param.getPageSize());
        return reasoningReq;
    }

    public static RuleBean graphConfReasonRspToRuleBean(@NonNull GraphConfReasonRsp reasonRsp) {
        RuleBean ruleBean = new RuleBean();
        ruleBean.setCreateTime(reasonRsp.getCreateAt());
        ruleBean.setKgName(reasonRsp.getKgName());
        ruleBean.setRuleConfig(JacksonUtils.writeValueAsString(reasonRsp.getRuleConfig()));
        ruleBean.setRuleId(reasonRsp.getId());
        ruleBean.setRuleName(reasonRsp.getRuleName());
        ruleBean.setUpdateTime(reasonRsp.getUpdateAt());
        return ruleBean;
    }

    public static GraphConfReasonReq ruleBeanToGraphConfReasonReq(@NonNull RuleBean ruleBean) {
        GraphConfReasonReq confReasonReq = new GraphConfReasonReq();
        consumerIfNoNull(ruleBean.getRuleConfig(), a -> confReasonReq.setRuleConfig(JsonUtils.stringToMap(a)));
        confReasonReq.setRuleName(ruleBean.getRuleName());
        return confReasonReq;
    }
}

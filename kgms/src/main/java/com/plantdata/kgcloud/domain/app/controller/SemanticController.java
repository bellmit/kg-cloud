package com.plantdata.kgcloud.domain.app.controller;

import ai.plantdata.kg.api.semantic.QuestionAnswersApi;
import ai.plantdata.kg.api.semantic.ReasoningApi;
import ai.plantdata.kg.api.semantic.req.QueryReq;
import ai.plantdata.kg.api.semantic.req.ReasoningReq;
import ai.plantdata.kg.api.semantic.rsp.AnswerDataRsp;
import ai.plantdata.kg.api.semantic.rsp.ReasoningResultRsp;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.app.converter.RestCopyConverter;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.GraphReasoningResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.QaAnswerDataRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/6 11:26
 */
public class SemanticController {

    @Autowired
    private ReasoningApi reasoningApi;
    @Autowired
    private QuestionAnswersApi questionAnswersApi;

    @ApiOperation("意图图谱生成")
    @GetMapping("qa/init/{kgName}")
    public ApiReturn kbQaiInit(@ApiParam("图谱名称") @PathVariable String kgName) {
        questionAnswersApi.create(kgName);
        return ApiReturn.success();
    }

    @ApiOperation("知识图谱问答")
    @GetMapping("qa/{kgName}")
    public ApiReturn<QaAnswerDataRsp> qaKbQa(@ApiParam("图谱名称") @PathVariable String kgName,
                                             @RequestBody QueryReq queryReq) {
        RestResp<AnswerDataRsp> query = questionAnswersApi.query(kgName, queryReq);
        return ApiReturn.success(RestCopyConverter.copyRestRespResult(query, new QaAnswerDataRsp()));
    }

    @ApiOperation("隐含关系推理")
    @PostMapping("reasoning/execute/{kgName}")
    public ApiReturn<GraphReasoningResultRsp> reasoning(@ApiParam(value = "图谱名称") @PathVariable("kgName") String kgName,
                                                        @RequestBody ReasoningReq reasoningReq) {
        RestResp<ReasoningResultRsp> reasoning = reasoningApi.reasoning(kgName, reasoningReq);
        return ApiReturn.success(RestCopyConverter.copyRestRespResult(reasoning, new GraphReasoningResultRsp()));
    }
}

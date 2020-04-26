package com.plantdata.kgcloud.domain.nlp;

import com.google.common.collect.Lists;
import com.hiekn.basicnlptools.hanlp.HanLPService;
import com.hiekn.pddocument.bean.element.PdEntity;
import com.hiekn.pddocument.bean.element.PdKeyword;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.common.module.NaturalLanguageProcessingInterface;
import com.plantdata.kgcloud.sdk.NlpClient;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.req.app.sematic.DistanceListReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.DistanceEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.SegmentEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.IntentDataBeanRsp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.plantdata.kgcloud.plantdata.converter.nlp.NlpConverter2;
import com.hiekn.pddocument.bean.PdDocument;

import java.util.List;
import java.util.Map;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/4 17:07
 */
@RestController
@RequestMapping("v3/nlp")
public class NlpController implements NaturalLanguageProcessingInterface {

    @Autowired
    public NlpClient nlpClient;
    @Autowired
    public SemanticClient semanticClient;

    private HanLPService hanLPService = new HanLPService();

    @ApiOperation(value = "文本语义标注", notes = "文本语义标注，以知识图谱的实体，对输入文本进行标注。")
    @PostMapping("annotation/{kgName}")
    public ApiReturn<PdDocument> tagging(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                   @RequestBody EntityLinkingReq linkingFrom) {
        return ApiReturn.success(NlpConverter2.annotationToPdDocument(nlpClient.tagging(kgName, linkingFrom).getData()));
    }

    @ApiOperation(value = "图谱实体识别", notes = "实体识别，以知识图谱的实体，对输入文本进行命名实体识别。")
    @PostMapping("ner/graph/{kgName}")
    public ApiReturn<PdDocument> nerGraph(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                      @RequestBody SegmentReq segmentReq) {
        return nlpClient.nerGraph(kgName, segmentReq);
    }

    @ApiOperation("命名实体识别")
    @PostMapping("ner")
    public ApiReturn<List<NerResultRsp>> namedEntityRecognition(@RequestBody NerReq nerReq) {
        return nlpClient.namedEntityRecognition(nerReq);
    }

    @ApiOperation(value = "中文命名实体识别", notes = "中文命名实体识别，用于识别中文人名")
    @PostMapping("ner/chinese")
    public ApiReturn<PdDocument> ner(@ApiParam(required = true) @RequestBody String input) {
        return ApiReturn.success(NlpConverter2.nerToPdDocument(hanLPService.ner(input),input));
    }

    @ApiOperation(value = "图谱分词", notes = "图谱分词，以知识图谱的实体，对输入文本进行分词。")
    @GetMapping("segment/graph/{kgName}")
    public ApiReturn<PdDocument> graphSegment(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                         SegmentReq segmentReq) {
        return ApiReturn.success(NlpConverter2.graphSegmentToPdDocument(nlpClient.graphSegment(kgName, segmentReq).getData(),segmentReq.getKw()));
    }

    @ApiOperation(value = "语义关联", notes = "语义关联接口。在给定的知识图谱中对输入的文本内容进行实体识别和消歧，" +
            "并基于schema进行文本意图识别，返回识别结果及相应权重。")
    @PostMapping("association")
    public ApiReturn<PdDocument> intent(
            @ApiParam(value = "图谱名称") @RequestParam("kgName") String kgName,
            @ApiParam(value = "自然语言输入") @RequestParam("query") String query,
            @RequestParam(value = "size", defaultValue = "5") int size) {
        return ApiReturn.success(NlpConverter2.intentDataBeanRspToPdDocument(semanticClient.intent(kgName, query, size).getData()));
    }

    @ApiOperation(value = "繁体转换", notes = "繁体转换，将输入的文本转换为繁体中文")
    @PostMapping("traditional/chinese")
    public ApiReturn<PdDocument> toTraditionalChinese(@ApiParam(required = true) @RequestBody String input) {
        return ApiReturn.success(NlpConverter2.stringToPdDocument(hanLPService.toTraditionalChinese(input)));
    }

    @ApiOperation(value = "简体转换", notes = "简体转换，将输入的文本转换为简体中文")
    @PostMapping("simplified/chinese")
    public ApiReturn<PdDocument> toSimplifiedChinese(@ApiParam(required = true) @RequestBody String input) {
        return ApiReturn.success(NlpConverter2.stringToPdDocument(hanLPService.toSimplifiedChinese(input)));
    }

    @ApiOperation(value = "拼音转换", notes = "将输入文本转换为拼音")
    @PostMapping("phonetic")
    public ApiReturn<PdDocument> phonetic(@ApiParam(required = true) @RequestBody String input) {
        return ApiReturn.success(NlpConverter2.phoneticToPdDocument(hanLPService.toPinyin(input)));
    }

    @ApiOperation(value = "中文分词", notes = "中文分词")
    @PostMapping("segment/chinese")
    public ApiReturn<PdDocument> seg(@RequestParam @ApiParam(required = true) String input) {

        return ApiReturn.success(NlpConverter2.segmentToPdDocument(hanLPService.seg(input)));
    }

    @ApiOperation(value = "自动摘要", notes = "自动摘要")
    @PostMapping("summarize")
    public ApiReturn<PdDocument> summarize(@ApiParam(required = true) @RequestBody String input,
                                             @ApiParam(required = true, value = " 句子个数") @RequestParam("size") Integer size) {
        return ApiReturn.success(NlpConverter2.segmentToPdDocument(hanLPService.summarize(input, size),input));
    }

    @ApiOperation(value = "词性标注", notes = "词性标注")
    @PostMapping("pos")
    public ApiReturn<PdDocument> pos(@ApiParam(required = true) @RequestBody String input) {
        return ApiReturn.success(NlpConverter2.posToPdDocument(hanLPService.pos(input)));
    }


    @ApiOperation(value = "短语提取", notes = "短语提取")
    @PostMapping("extract/phrase")
    public ApiReturn<PdDocument> extractPhrase(@ApiParam(required = true) @RequestBody String input,
                                                 @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return ApiReturn.success(NlpConverter2.segmentToPdDocument(hanLPService.extractPhrase(input, size),input));
    }

    @ApiOperation(value = "新词发现", notes = "新词发现")
    @PostMapping("extract/newWord")
    public ApiReturn<PdDocument> extractNewWord(@ApiParam(required = true) @RequestBody String input,
                                                  @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return ApiReturn.success(NlpConverter2.segmentToPdDocument(hanLPService.extractNewWord(input, size),input));
    }

    @ApiOperation(value = "关键词提取", notes = "关键词提取")
    @PostMapping("extract/keyword")
    public ApiReturn<PdDocument> extractKeyword(@ApiParam(required = true) @RequestBody String input,
                                               @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return ApiReturn.success(NlpConverter2.keywordToPdDocument(hanLPService.extractKeyword(input, size),input));
    }

    @ApiOperation("两个实体间语义距离查询")
    @PostMapping("{kgName}/distance/score")
    public ApiReturn<Double> semanticDistanceScore(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                   @RequestParam("entityIdOne") Long entityIdOne, @RequestParam("entityIdTwo") Long entityIdTwo) {
        return semanticClient.semanticDistanceScore(kgName, entityIdOne, entityIdTwo);
    }

}

package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.hiekn.basicnlptools.hanlp.HanLPService;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.nlp.NlpConverter;
import com.plantdata.kgcloud.plantdata.req.entity.SegmentEntityBean;
import com.plantdata.kgcloud.plantdata.req.nlp.AnnotationParameter;
import com.plantdata.kgcloud.plantdata.req.nlp.NerParameter;
import com.plantdata.kgcloud.plantdata.req.nlp.QaIntentParameter;
import com.plantdata.kgcloud.plantdata.req.nlp.RecognitionParameter;
import com.plantdata.kgcloud.plantdata.req.nlp.SegmentParametet;
import com.plantdata.kgcloud.sdk.NlpClient;
import com.plantdata.kgcloud.sdk.SemanticClient;
import com.plantdata.kgcloud.sdk.req.app.nlp.EntityLinkingReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.NerReq;
import com.plantdata.kgcloud.sdk.req.app.nlp.SegmentReq;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.GraphSegmentRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.NerResultRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.SegmentEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.nlp.TaggingItemRsp;
import com.plantdata.kgcloud.sdk.rsp.app.semantic.IntentDataBean;
import com.plantdata.kgcloud.util.JacksonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Administrator
 */
@Api(tags = "nlp-sdk")
@RestController("nlpController-v2")
@RequestMapping("sdk/nlp")
public class NlpController implements SdkOldApiInterface {

    @Autowired
    public NlpClient nlpClient;
    @Autowired
    public SemanticClient semanticClient;
    private HanLPService hanLPService = new HanLPService();

    /**
     * 命名实体识别
     *
     * @return 词列表
     */
    @ApiOperation("中文命名实体识别")
    @PostMapping("ner")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "input", dataType = "string", paramType = "form", value = "input"),
            @ApiImplicitParam(name = "config", required = true, dataType = "string", paramType = "form", value = "config"),
    })
    public RestResp<List<NerResultRsp>> ner(@Valid @ApiIgnore NerParameter nerParameter) {
        NerReq nerReq = JacksonUtils.readValue(JacksonUtils.writeValueAsString(nerParameter), NerReq.class);
        Optional<List<NerResultRsp>> nerResultRspList = BasicConverter.apiReturnData(nlpClient.namedEntityRecognition(nerReq));
        return new RestResp<>(nerResultRspList.orElse(Collections.emptyList()));
    }

    @ApiOperation("图谱实体识别")
    @PostMapping("ner/graph")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", dataType = "string", paramType = "query", value = "kgName"),
            @ApiImplicitParam(name = "kw", required = true, dataType = "string", paramType = "form", value = "kw"),
            @ApiImplicitParam(name = "useConcept", required = true, dataType = "boolean", paramType = "form", value = "是否使用概念作为词典"),
            @ApiImplicitParam(name = "useEntity", required = true, dataType = "boolean", paramType = "form", value = "是否使用实体作为词典"),
            @ApiImplicitParam(name = "useAttr", required = true, dataType = "boolean", paramType = "form", value = "是否使用属性作为词典"),
    })
    public RestResp<List<SegmentEntityBean>> recognition(@Valid @ApiIgnore RecognitionParameter param) {
        Function<SegmentReq, ApiReturn<List<SegmentEntityRsp>>> returnFunction = a -> nlpClient.nerGraph(param.getKgName(), a);
        List<SegmentEntityBean> entityBeans = returnFunction
                .compose(NlpConverter::recognitionParameterToSegmentReq)
                .andThen(a -> BasicConverter.convert(a, b -> BasicConverter.listToRsp(b, NlpConverter::segmentEntityRspToSegmentEntityBean)))
                .apply(param);
        return new RestResp<>(entityBeans);
    }

    @ApiOperation("图谱分词")
    @PostMapping("segment/graph")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", dataType = "string", paramType = "query", value = "kgName"),
            @ApiImplicitParam(name = "kw", required = true, dataType = "string", paramType = "form", value = "kw"),
            @ApiImplicitParam(name = "useConcept", defaultValue = "true", dataType = "boolean", paramType = "form", value = "是否使用概念作为词典"),
            @ApiImplicitParam(name = "useEntity", defaultValue = "true", dataType = "boolean", paramType = "form", value = "是否使用实体作为词典"),
            @ApiImplicitParam(name = "useAttr", defaultValue = "true", dataType = "boolean", paramType = "form", value = "是否使用属性作为词典"),
    })
    public RestResp<List<GraphSegmentRsp>> segment(@Valid @ApiIgnore SegmentParametet param) {
        Function<SegmentReq, ApiReturn<List<GraphSegmentRsp>>> returnFunction = a -> nlpClient.graphSegment(param.getKgName(), a);
        Optional<List<GraphSegmentRsp>> entityBeans = returnFunction
                .compose(NlpConverter::segmentParametetToSegmentReq)
                .andThen(BasicConverter::apiReturnData)
                .apply(param);
        return new RestResp<>(entityBeans.orElse(Collections.emptyList()));
    }

    @ApiOperation("语义标注")
    @PostMapping("annotation")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", dataType = "string", paramType = "query", value = "kgName"),
            @ApiImplicitParam(name = "text", dataType = "string", paramType = "form", value = "待标注文本"),
            @ApiImplicitParam(name = "conceptIds", dataType = "string", paramType = "form", value = "标注范围，格式为json数组格式的概念列表"),
    })
    public RestResp<List<TaggingItemRsp>> annotation(@Valid @ApiIgnore AnnotationParameter param) {
        Function<EntityLinkingReq, ApiReturn<List<TaggingItemRsp>>> returnFunction = a -> nlpClient.tagging(param.getKgName(), a);
        Optional<List<TaggingItemRsp>> opt = returnFunction
                .compose(NlpConverter::annotationParameterToEntityLinkingReq)
                .andThen(BasicConverter::apiReturnData)
                .apply(param);
        return new RestResp<>(opt.orElse(Collections.emptyList()));
    }

    @ApiOperation("语义关联")
    @PostMapping("/semantic/association")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "kgName"),
            @ApiImplicitParam(name = "query", required = true, dataType = "string", paramType = "form", value = "待识别语句"),
            @ApiImplicitParam(name = "size", defaultValue = "5", dataType = "int", paramType = "query", value = "返回结果数量"),
    })
    public RestResp<IntentDataBean> qaIntent(@Valid @ApiIgnore QaIntentParameter param) {
        Optional<IntentDataBean> intentDataBean = BasicConverter.apiReturnData(semanticClient.intent(param.getKgName(), param.getQuery(), param.getSize()));
        return new RestResp<>(intentDataBean.orElse(new IntentDataBean()));
    }

    /**
     * 分词
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("中文分词")
    @PostMapping("segment/chinese")
    public RestResp<List<String>> seg(@RequestParam @ApiParam(required = true) String input) {
        List<String> segList = hanLPService.seg(input);
        return new RestResp<>(segList);
    }

    /**
     * 词性标注
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("词性标注")
    @PostMapping("pos")
    public RestResp<List<String>> pos(@ApiParam(required = true) @RequestParam("input") String input) {
        List<String> segList = hanLPService.pos(input);
        return new RestResp<>(segList);
    }

    /**
     * 命名实体识别
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("中文命名实体识别")
    @PostMapping("ner/chinere")
    public RestResp<Map<String, List<String>>> ner(@ApiParam(required = true) @RequestParam("input") String input) {
        Map<String, List<String>> nerList = hanLPService.ner(input);
        return new RestResp<>(nerList);
    }

    /**
     * 转换为简体中文
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("转换为简体中文")
    @PostMapping("simplified/chinese")
    public RestResp<String> toSimplifiedChinese(@ApiParam(required = true) @RequestParam("input") String input) {
        return new RestResp<>(hanLPService.toSimplifiedChinese(input));
    }

    /**
     * 转换为简体中文
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("转换为繁体中文")
    @PostMapping("traditional/chinese")
    public RestResp<String> toTraditionalChinese(@ApiParam(required = true) @RequestParam("input") String input) {
        return new RestResp<>(hanLPService.toTraditionalChinese(input));
    }

    /**
     * 转换为拼音
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("转换为拼音")
    @PostMapping("phoneticize")
    public RestResp<List<String>> phoneticize(@ApiParam(required = true) @RequestParam("input") String input) {
        return new RestResp<>(hanLPService.toPinyin(input));
    }

    /**
     * 转换为拼音
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("关键词提取")
    @PostMapping("extract/keyword")
    public RestResp<List<String>> extractKeyword(@ApiParam(required = true) @RequestParam("input") String input,
                                                 @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return new RestResp<>(hanLPService.extractKeyword(input, size));
    }


    /**
     * 新词发现
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("新词发现")
    @PostMapping("extract/newword")
    public RestResp<List<String>> extractNewWord(@ApiParam(required = true) @RequestParam("input") String input,
                                                 @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return new RestResp<>(hanLPService.extractNewWord(input, size));
    }

    /**
     * 短语提取
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("短语提取")
    @PostMapping("extract/phrase")
    public RestResp<List<String>> extractPhrase(@ApiParam(required = true) @RequestParam("input") String input,
                                                @ApiParam(required = true, value = "个数") @RequestParam("size") Integer size) {
        return new RestResp<>(hanLPService.extractPhrase(input, size));
    }

    /**
     * 自动摘要
     *
     * @param input 输入文本
     * @return 词列表
     */
    @ApiOperation("自动摘要")
    @PostMapping("summarize")
    public RestResp<List<String>> summarize(@ApiParam(required = true) @RequestParam("input") String input,
                                            @ApiParam(required = true, value = " 句子个数") @RequestParam("size") Integer size) {
        return new RestResp<>(hanLPService.summarize(input, size));
    }
}

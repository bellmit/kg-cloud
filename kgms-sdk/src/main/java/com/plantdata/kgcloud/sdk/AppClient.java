package com.plantdata.kgcloud.sdk;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.sdk.req.StatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.TableStatisticByDimensionalReq;
import com.plantdata.kgcloud.sdk.req.app.DataSetStatisticRsp;
import com.plantdata.kgcloud.sdk.req.app.EdgeAttrPromptReq;
import com.plantdata.kgcloud.sdk.req.app.ExploreByKgQlReq;
import com.plantdata.kgcloud.sdk.req.app.GisGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.GisLocusReq;
import com.plantdata.kgcloud.sdk.req.app.GraphInitRsp;
import com.plantdata.kgcloud.sdk.req.app.KnowledgeRecommendReq;
import com.plantdata.kgcloud.sdk.req.app.ObjectAttributeRsp;
import com.plantdata.kgcloud.sdk.req.app.PromptReq;
import com.plantdata.kgcloud.sdk.req.app.SeniorPromptReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonReasoningExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.CommonTimingExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.PathTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReasoningAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationReqAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.explore.RelationTimingAnalysisReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.BatchInfoBoxReq;
import com.plantdata.kgcloud.sdk.req.app.infobox.InfoBoxReq;
import com.plantdata.kgcloud.sdk.rsp.app.ComplexGraphVisualRsp;
import com.plantdata.kgcloud.sdk.rsp.app.EdgeAttributeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.PageRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisReasonRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.PathTimingAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationReasoningAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.analysis.RelationTimingAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.CommonBasicGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisGraphExploreRsp;
import com.plantdata.kgcloud.sdk.rsp.app.explore.GisLocusAnalysisRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.ApkRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.BasicConceptTreeRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.InfoBoxRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.PromptEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SchemaRsp;
import com.plantdata.kgcloud.sdk.rsp.app.main.SeniorPromptRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.BasicInfoVO;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/21 14:03
 */
@FeignClient(value = "kgms", path = "app", contextId = "app")
public interface AppClient {

    /**
     * 获取当前图实体类型及属性类型的schema
     *
     * @param kgName kgName
     * @return List
     */
    @GetMapping("schema/{kgName}")
    ApiReturn<SchemaRsp> querySchema(@PathVariable("kgName") String kgName);


    /**
     * 批量读取知识卡片
     *
     * @param kgName          kgName
     * @param batchInfoBoxReq batchReq
     * @return .
     */
    @PostMapping("infoBox/list/{kgName}")
    ApiReturn<List<InfoBoxRsp>> listInfoBox(@PathVariable("kgName") String kgName,
                                            @RequestBody BatchInfoBoxReq batchInfoBoxReq);

    /**
     * 读取知识卡片
     *
     * @param kgName     kgName
     * @param infoBoxReq req
     * @return .
     */
    @PostMapping("infoBox/{kgName}")
    ApiReturn<InfoBoxRsp> infoBox(@PathVariable("kgName") String kgName,
                                  @RequestBody InfoBoxReq infoBoxReq);

    /**
     * 复杂算法分析 可视化
     *
     * @param kgName kgName
     * @param azkId  脚本执行返回的azkId
     * @param type   算法类型
     * @param size   显示数量
     * @return obj
     */
    @GetMapping("complex/graph/visual/{kgName}")
    ApiReturn<ComplexGraphVisualRsp> complexGraphVisual(@ApiParam("图谱名称") @PathVariable("kgName") String kgName,
                                                        @RequestParam("azkId") Long azkId,
                                                        @RequestParam(value = "type", defaultValue = "louvain") String type,
                                                        @RequestParam(value = "size", defaultValue = "100") int size);

    /**
     * 知识推荐
     *
     * @param kgName         kgName
     * @param recommendParam body
     * @return List
     */
    @PostMapping("knowledgeRecommend/{kgName}")
    ApiReturn<List<ObjectAttributeRsp>> knowledgeRecommend(@PathVariable("kgName") String kgName, @RequestBody KnowledgeRecommendReq recommendParam);

    /**
     * 获取模型可视化数据
     *
     * @param conceptId 概念id
     * @param kgName    kgName
     * @param display   true 显示属性(包含对象属性和数值属性)
     * @return object
     */
    @GetMapping("visualModels/{kgName}/{conceptId}")
    ApiReturn<BasicConceptTreeRsp> visualModels(@PathVariable("kgName") String kgName, @PathVariable("conceptId") long conceptId, @RequestParam("display") boolean display);

    /**
     * 获取概念树
     *
     * @param kgName     kgName
     * @param conceptId  概念id
     * @param conceptKey 概念唯一key
     * @return List
     */
    @GetMapping("concept/{kgName}")
    ApiReturn<List<BasicInfoVO>> conceptTree(@PathVariable("kgName") String kgName,
                                             @RequestParam(value = "conceptId", required = false) Long conceptId,
                                             @RequestParam(value = "conceptKey", required = false) String conceptKey);

    /**
     * 获取所有图谱名称
     *
     * @param page 页码
     * @param size 数量
     * @return ApkRsp
     */
    @GetMapping("kgName/all")
    ApiReturn<PageRsp<ApkRsp>> getKgName(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                         @RequestParam(value = "size", required = false, defaultValue = "10") Integer size);

    /**
     * 实体下拉提示
     *
     * @param kgName    kgName
     * @param promptReq req
     * @return 。。。
     */
    @PostMapping("prompt/{kgName}")
    ApiReturn<List<PromptEntityRsp>> prompt(@PathVariable("kgName") String kgName,
                                            @RequestBody PromptReq promptReq);

    /**
     * 高级搜索
     *
     * @param kgName          kgName
     * @param seniorPromptReq s
     * @return ...
     */
    @GetMapping("prompt/senior/{kgName}")
    ApiReturn<List<SeniorPromptRsp>> seniorPrompt(@PathVariable("kgName") String kgName,
                                                  @RequestBody SeniorPromptReq seniorPromptReq);

    /**
     * 边属性搜索
     *
     * @param kgName            kgName
     * @param edgeAttrPromptReq 边属性搜索参数
     * @return list
     */
    @PostMapping("attributes/{kgName}")
    ApiReturn<List<EdgeAttributeRsp>> attrPrompt(@PathVariable("kgName") String kgName,
                                                 @RequestBody EdgeAttrPromptReq edgeAttrPromptReq);

    /**
     * 初始化图探索焦点
     *
     * @param kgName 图谱名称
     * @param type   图类型
     * @return 。。
     */
    @PostMapping("graphExplore/init/{kgName}")
    ApiReturn<GraphInitRsp> initGraphExploration(@PathVariable("kgName") String kgName,
                                                 @RequestParam("type") String type);

    /**
     * kgQl查询
     *
     * @param kgName  kgName
     * @param kgQlReq kgQl
     * @return ..。
     */
    @PostMapping("graphExplore/byKgQl/{kgName}")
    ApiReturn<CommonBasicGraphExploreRsp> exploreByKgQl(@PathVariable("kgName") String kgName,
                                                        @RequestBody ExploreByKgQlReq kgQlReq);

    /**
     * 普通图探索
     *
     * @param kgName       kgName
     * @param exploreParam param
     * @return 。。。
     */
    @PostMapping("graphExplore/common/{kgName}")
    ApiReturn<CommonBasicGraphExploreRsp> commonGraphExploration(@PathVariable("kgName") String kgName,
                                                                 @RequestBody CommonExploreReq exploreParam);

    /**
     * 时序图探索
     *
     * @param kgName       kgName
     * @param exploreParam param
     * @return 。。。
     */
    @PostMapping("graphExplore/timing/{kgName}")
    ApiReturn<CommonBasicGraphExploreRsp> timingGraphExploration(@PathVariable("kgName") String kgName,
                                                                 @RequestBody CommonTimingExploreReq exploreParam);

    /**
     * 推理图探索
     *
     * @param kgName       kgName
     * @param exploreParam param
     * @return CommonBasicGraphExploreRsp
     */
    @PostMapping("reasoning/{kgName}")
    ApiReturn<CommonBasicGraphExploreRsp> reasoningGraphExploration(@PathVariable("kgName") String kgName, @RequestBody CommonReasoningExploreReq exploreParam);

    /**
     * gis图探索
     *
     * @param kgName       图谱名称
     * @param exploreParam 个i是、图探索参数body
     * @return 。。。
     */
    @PostMapping("graphExplore/gis/{kgName}")
    ApiReturn<GisGraphExploreRsp> gisGraphExploration(@PathVariable("kgName") String kgName,
                                                      @RequestBody GisGraphExploreReq exploreParam);

    /**
     * gis轨迹分析
     *
     * @param kgName        kgName
     * @param locusGisParam param
     * @return 。。。
     */
    @PostMapping("graphExplore/gisLocus/{kgName}")
    ApiReturn<GisLocusAnalysisRsp> graphLocusGis(@PathVariable("kgName") String kgName,
                                                 @RequestBody GisLocusReq locusGisParam);

    /**
     * 路径发现
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return 。。。
     */
    @PostMapping("graphExplore/path/{kgName}")
    ApiReturn<PathAnalysisRsp> path(@PathVariable("kgName") String kgName,
                                    @RequestBody PathAnalysisReq analysisReq);

    /**
     * 最短路径发现
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return 。。。
     */
    @PostMapping("graphExplore/path/shortest/{kgName}")
    ApiReturn<PathAnalysisRsp> shortestPath(@PathVariable("kgName") String kgName,
                                            @RequestBody PathAnalysisReq analysisReq);

    /**
     * 路径分析推理
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return 。。。
     */
    @PostMapping("graphExplore/path/reasoning/{kgName}")
    ApiReturn<PathAnalysisReasonRsp> pathRuleReason(@PathVariable("kgName") String kgName,
                                                    @RequestBody PathReasoningAnalysisReq analysisReq);

    /**
     * 时序路径分析
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return ...
     */
    @PostMapping("graphExplore/path/timing/{kgName}")
    ApiReturn<PathTimingAnalysisRsp> pathTimingAnalysis(@PathVariable("kgName") String kgName,
                                                        @RequestBody PathTimingAnalysisReq analysisReq);

    /**
     * 关联分析
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return ...
     */
    @PostMapping("graphExplore/relation/{kgName}")
    ApiReturn<RelationAnalysisRsp> relationAnalysis(@PathVariable("kgName") String kgName,
                                                    @RequestBody RelationReqAnalysisReq analysisReq);

    /**
     * 直接关联分析
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return ...
     */
    @PostMapping("graphExplore/relation/direct/{kgName}")
    ApiReturn<RelationAnalysisRsp> relationDirect(@PathVariable("kgName") String kgName,
                                                  @RequestBody RelationReqAnalysisReq analysisReq);

    /**
     * 时序关联分析
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return ...
     */
    @PostMapping("graphExplore/relation/timing/{kgName}")
    ApiReturn<RelationTimingAnalysisRsp> relationTimingAnalysis(@PathVariable("kgName") String kgName,
                                                                @RequestBody RelationTimingAnalysisReq analysisReq);

    /**
     * 关联推理分析
     *
     * @param kgName      kgName
     * @param analysisReq req
     * @return ...
     */
    @PostMapping("graphExplore/relation/reasoning/{kgName}")
    ApiReturn<RelationReasoningAnalysisRsp> relationReasoningAnalysis(@PathVariable("kgName") String kgName,
                                                                      @RequestBody RelationReasoningAnalysisReq analysisReq);

    /**
     * 图片导出
     *
     * @param fileName 文件名称
     * @param data     数据
     * @return 。
     * @throws IOException 转换异常
     */
    @PostMapping("png/export")
    ApiReturn exportPng(@RequestParam("name") String fileName, @RequestParam("data") String data) throws IOException;

    /**
     * 统计数据二维(仅支持搜索数据集)
     *
     * @param dataName       图谱名称
     * @param twoDimensional 2维
     * @return 。
     */
    @PostMapping("dataset/statistic/2d/{dataName}")
    ApiReturn<DataSetStatisticRsp> statistic2d(
            @PathVariable("dataName") String dataName,
            @RequestBody StatisticByDimensionalReq twoDimensional);

    /**
     * 统计数据三维(仅支持搜索数据集)
     *
     * @param dataName       数据集名称
     * @param dimensionalReq ..
     * @return .
     */
    @PostMapping("dataset/statistic/3d/{dataName}")
    ApiReturn<DataSetStatisticRsp> statistic3d(@PathVariable("dataName") String dataName,
                                               @RequestBody StatisticByDimensionalReq dimensionalReq);

    /**
     * 统计数据二维/按表统计
     *
     * @param twoDimensional dataName
     * @return .
     */
    @PostMapping("dataset/statistic/2dByTable")
    ApiReturn<DataSetStatisticRsp> statistic2dByTable(@RequestBody TableStatisticByDimensionalReq twoDimensional);

    /**
     * 统计数据三维/按表统计
     *
     * @param thirdDimensional dataName
     * @return .
     */
    @PostMapping("dataset/statistic/3dByTable")
    ApiReturn<DataSetStatisticRsp> statistic3dByTable(@Valid @RequestBody TableStatisticByDimensionalReq thirdDimensional);
}

package com.plantdata.kgcloud.domain.edit.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicInfoListBodyReq;
import com.plantdata.kgcloud.domain.edit.req.basic.BasicInfoListReq;
import com.plantdata.kgcloud.domain.edit.req.entity.BatchPrivateRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.BatchRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeleteEdgeObjectReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeletePrivateDataReq;
import com.plantdata.kgcloud.domain.edit.req.entity.DeleteRelationReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EdgeNumericAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EdgeObjectAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityDeleteReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityMetaDeleteReq;
import com.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import com.plantdata.kgcloud.sdk.rsp.edit.EntityModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityTagSearchReq;
import com.plantdata.kgcloud.domain.edit.req.entity.EntityTimeModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.GisInfoModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.NumericalAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.ObjectAttrValueReq;
import com.plantdata.kgcloud.domain.edit.req.entity.PrivateAttrDataReq;
import com.plantdata.kgcloud.domain.edit.req.entity.SsrModifyReq;
import com.plantdata.kgcloud.domain.edit.req.entity.UpdateRelationMetaReq;
import com.plantdata.kgcloud.domain.edit.rsp.BasicInfoRsp;
import com.plantdata.kgcloud.domain.edit.service.BasicInfoService;
import com.plantdata.kgcloud.domain.edit.service.EntityService;
import com.plantdata.kgcloud.domain.edit.vo.EntityTagVO;
import com.plantdata.kgcloud.sdk.req.app.BatchEntityAttrDeleteReq;
import com.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import com.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import com.plantdata.kgcloud.sdk.req.edit.BasicInfoModifyReq;
import com.plantdata.kgcloud.sdk.rsp.EntityLinkVO;
import com.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import com.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import com.plantdata.kgcloud.sdk.rsp.edit.EntityModifyReq;
import com.plantdata.kgcloud.util.ConvertUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: LinHo
 * @Date: 2019/11/20 10:11
 * @Description:
 */
@Api(tags = "实体编辑")
@RestController
@RequestMapping("/edit/entity")
public class EntityController {

    @Autowired
    private EntityService entityService;

    @Autowired
    private BasicInfoService basicInfoService;

    @ApiOperation("实体编辑-添加概念")
    @PostMapping("/{kgName}/{conceptId}/{entityId}/add")
    public ApiReturn addMultipleConcept(@PathVariable("kgName") String kgName,
                                        @PathVariable("conceptId") Long conceptId,
                                        @PathVariable("entityId") Long entityId) {
        entityService.addMultipleConcept(kgName, conceptId, entityId);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-删除概念")
    @PostMapping("/{kgName}/{conceptId}/{entityId}/delete")
    public ApiReturn deleteMultipleConcept(@PathVariable("kgName") String kgName,
                                           @PathVariable("conceptId") Long conceptId,
                                           @PathVariable("entityId") Long entityId) {
        entityService.deleteMultipleConcept(kgName, conceptId, entityId);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-修改实体名称,消歧")
    @PostMapping("/{kgName}/update")
    public ApiReturn updateEntity(@PathVariable("kgName") String kgName,
                                  @Valid @RequestBody EntityModifyReq entityModifyReq) {
        basicInfoService.updateBasicInfo(kgName, ConvertUtils.convert(BasicInfoModifyReq.class).apply(entityModifyReq));
        return ApiReturn.success();
    }

    @ApiOperation("实体详情-批量查询-根据实体Ids")
    @PostMapping("/{kgName}/entities")
    public ApiReturn<List<BasicInfoRsp>> batchEntityDetails(@PathVariable("kgName") String kgName, @RequestBody List<Long> ids) {
        return ApiReturn.success(basicInfoService.listByIds(kgName, ids));
    }

    @ApiOperation("实体编辑-批量删除实体")
    @PostMapping("/{kgName}/batch/delete")
    public ApiReturn<List<DeleteResult>> batchDeleteEntities(@PathVariable("kgName") String kgName,
                                                             @RequestBody List<Long> ids) {
        return ApiReturn.success(entityService.deleteByIds(kgName, ids));
    }

    @ApiOperation("实体编辑-根据概念id删除实体")
    @PostMapping("/{kgName}/concept/delete")
    public ApiReturn<Long> deleteByConceptId(@PathVariable("kgName") String kgName,
                                             @Valid @RequestBody EntityDeleteReq entityDeleteReq) {
        return ApiReturn.success(entityService.deleteByConceptId(kgName, entityDeleteReq));
    }

    @ApiOperation("实体编辑-根据来源 ,批次号删除实体")
    @PostMapping("/{kgName}/delete/meta")
    public ApiReturn deleteByMeta(@PathVariable("kgName") String kgName,
                                  @Valid @RequestBody EntityMetaDeleteReq entityMetaDeleteReq) {
        entityService.deleteByMeta(kgName, entityMetaDeleteReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体详情-实体列表")
    @PostMapping("/{kgName}/list")
    public ApiReturn<Page<BasicInfoRsp>> listEntities(@PathVariable("kgName") String kgName,
                                                      BasicInfoListReq basicInfoListReq,
                                                      @RequestBody BasicInfoListBodyReq bodyReq) {
        return ApiReturn.success(entityService.listEntities(kgName, basicInfoListReq, bodyReq));
    }

    @ApiOperation("实体编辑-更新权重,来源,可信度")
    @PostMapping("/{kgName}/{entityId}/ssr")
    public ApiReturn updateScoreSourceReliability(@PathVariable("kgName") String kgName,
                                                  @PathVariable("entityId") Long entityId,
                                                  @Valid @RequestBody SsrModifyReq ssrModifyReq) {

        entityService.updateScoreSourceReliability(kgName, entityId, ssrModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-更新实体开始截止时间")
    @PostMapping("/{kgName}/{entityId}/time")
    public ApiReturn updateEntityTime(@PathVariable("kgName") String kgName,
                                      @PathVariable("entityId") Long entityId,
                                      @Valid @RequestBody EntityTimeModifyReq entityTimeModifyReq) {
        entityService.updateEntityTime(kgName, entityId, entityTimeModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-更新实体gis详情")
    @PostMapping("/{kgName}/{entityId}/gis")
    public ApiReturn updateGisInfo(@PathVariable("kgName") String kgName,
                                   @PathVariable("entityId") Long entityId,
                                   @Valid @RequestBody GisInfoModifyReq gisInfoModifyReq) {
        entityService.updateGisInfo(kgName, entityId, gisInfoModifyReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-标签-添加")
    @PostMapping("/{kgName}/{entityId}/tag/add")
    public ApiReturn addEntityTag(@PathVariable("kgName") String kgName,
                                  @PathVariable("entityId") Long entityId,
                                  @Valid @RequestBody List<EntityTagVO> vos) {
        entityService.addEntityTag(kgName, entityId, vos);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-标签-修改")
    @PostMapping("/{kgName}/{entityId}/tag/update")
    public ApiReturn updateEntityTag(@PathVariable("kgName") String kgName,
                                     @PathVariable("entityId") Long entityId,
                                     @Valid @RequestBody List<EntityTagVO> vos) {
        entityService.updateEntityTag(kgName, entityId, vos);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-标签-删除")
    @PostMapping("/{kgName}/{entityId}/tag/delete")
    public ApiReturn deleteEntityTag(@PathVariable("kgName") String kgName,
                                     @PathVariable("entityId") Long entityId,
                                     @Valid @RequestBody List<String> tagNames) {
        entityService.deleteEntityTag(kgName, entityId, tagNames);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-添加实体关联")
    @PostMapping("/{kgName}/{entityId}/link/add")
    public ApiReturn addEntityLink(@PathVariable("kgName") String kgName,
                                   @PathVariable("entityId") Long entityId,
                                   @Valid @RequestBody List<EntityLinkVO> vos) {
        entityService.addEntityLink(kgName, entityId, vos);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-删除实体关联")
    @PostMapping("/{kgName}/{entityId}/link/delete")
    public ApiReturn deleteEntityLink(@PathVariable("kgName") String kgName,
                                      @PathVariable("entityId") Long entityId,
                                      @Valid @RequestBody List<EntityLinkVO> vos) {
        entityService.deleteEntityLink(kgName, entityId, vos);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-属性-数值属性值更新")
    @PostMapping("/{kgName}/number/update")
    public ApiReturn upsertNumericalAttrValue(@PathVariable("kgName") String kgName,
                                              @Valid @RequestBody NumericalAttrValueReq numericalAttrValueReq) {
        entityService.upsertNumericalAttrValue(kgName, numericalAttrValueReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-属性-添加对象属性值")
    @PostMapping("/{kgName}/relation")
    public ApiReturn addObjectAttrValue(@PathVariable("kgName") String kgName,
                                        @Valid @RequestBody ObjectAttrValueReq objectAttrValueReq) {
        entityService.addObjectAttrValue(kgName, objectAttrValueReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-属性-修改关系的metadata和时间")
    @PostMapping("/{kgName}/relation/update/meta")
    public ApiReturn updateRelationMeta(@PathVariable("kgName") String kgName,
                                        @Valid @RequestBody UpdateRelationMetaReq updateRelationMetaReq) {
        entityService.updateRelationMeta(kgName, updateRelationMetaReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-属性-删除对象属性值")
    @PostMapping("/{kgName}/relation/delete")
    public ApiReturn deleteObjAttrValue(@PathVariable("kgName") String kgName,
                                        @Valid @RequestBody DeleteRelationReq deleteRelationReq) {
        entityService.deleteObjAttrValue(kgName, deleteRelationReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-属性-添加私有数值或对象属性值")
    @PostMapping("/{kgName}/private/data")
    public ApiReturn addPrivateData(@PathVariable("kgName") String kgName,
                                    @Valid @RequestBody PrivateAttrDataReq privateAttrDataReq) {
        entityService.addPrivateData(kgName, privateAttrDataReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-属性-批量删除私有数值或对象属性值")
    @PostMapping("/{kgName}/batch/private/data/delete")
    public ApiReturn deletePrivateData(@PathVariable("kgName") String kgName,
                                       @Valid @RequestBody DeletePrivateDataReq deletePrivateDataReq) {
        entityService.deletePrivateData(kgName, deletePrivateDataReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-属性-添加或更新边数值属性值")
    @PostMapping("/{kgName}/edge/number")
    public ApiReturn addEdgeNumericAttrValue(@PathVariable("kgName") String kgName,
                                             @Valid @RequestBody EdgeNumericAttrValueReq edgeNumericAttrValueReq) {
        entityService.addEdgeNumericAttrValue(kgName, edgeNumericAttrValueReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-属性-添加边对象属性值")
    @PostMapping("/{kgName}/edge/object")
    public ApiReturn addEdgeObjectAttrValue(@PathVariable("kgName") String kgName,
                                            @Valid @RequestBody EdgeObjectAttrValueReq edgeObjectAttrValueReq) {
        entityService.addEdgeObjectAttrValue(kgName, edgeObjectAttrValueReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-属性-删除边对象属性值")
    @PostMapping("/{kgName}/edge/object/delete")
    public ApiReturn deleteEdgeObjectAttrValue(@PathVariable("kgName") String kgName,
                                               @Valid @RequestBody DeleteEdgeObjectReq deleteEdgeObjectReq) {
        entityService.deleteEdgeObjectAttrValue(kgName, deleteEdgeObjectReq);
        return ApiReturn.success();
    }

    @ApiOperation("实体编辑-属性-批量添加对象属性值")
    @PostMapping("/{kgName}/batch/object/add")
    public ApiReturn<List<String>> batchAddRelation(@PathVariable("kgName") String kgName,
                                                    @Valid @RequestBody BatchRelationReq batchRelationReq) {
        return ApiReturn.success(entityService.batchAddRelation(kgName, batchRelationReq));
    }

    @ApiOperation("实体编辑-属性-批量添加私有对象属性值")
    @PostMapping("/{kgName}/batch/object/add/private")
    public ApiReturn<List<String>> batchAddPrivateRelation(@PathVariable("kgName") String kgName,
                                                           @Valid @RequestBody BatchPrivateRelationReq batchPrivateRelationReq) {
        return ApiReturn.success(entityService.batchAddPrivateRelation(kgName, batchPrivateRelationReq));
    }

    @ApiOperation("标签列表-实体标签搜索")
    @GetMapping("/{kgName}/entity/tag/prompt")
    public ApiReturn<List<String>> tagSearch(@PathVariable("kgName") String kgName, EntityTagSearchReq entityTagSearchReq) {
        return ApiReturn.success(entityService.tagSearch(kgName, entityTagSearchReq));
    }


    @ApiOperation("实体详情-实体查询")
    @GetMapping("/{kgName}/list/search")
    public ApiReturn<List<OpenEntityRsp>> queryEntityList(@PathVariable("kgName") String kgName,
                                                          EntityQueryReq entityQueryReq) {
        return ApiReturn.success(entityService.queryEntityList(kgName, entityQueryReq));
    }


    @ApiOperation("实体编辑-批量新增或更新实体")
    @PostMapping("/{kgName}")
    public ApiReturn<OpenBatchResult<OpenBatchSaveEntityRsp>> saveOrUpdate(@PathVariable("kgName") String kgName, @ApiParam(
            "是否只是更新，默认不是") boolean add, @RequestBody List<OpenBatchSaveEntityRsp> batchEntity) {
        return ApiReturn.success(entityService.saveOrUpdate(kgName, add, batchEntity));
    }

    @ApiOperation("实体编辑-属性-批量实体数值属性删除")
    @DeleteMapping("/attr/{kgName}")
    public ApiReturn batchDeleteEntityAttr(
            @PathVariable("kgName") String kgName,
            @RequestBody BatchEntityAttrDeleteReq deleteReq) {
        entityService.batchDeleteEntityAttr(kgName, deleteReq);
        return ApiReturn.success();
    }


}

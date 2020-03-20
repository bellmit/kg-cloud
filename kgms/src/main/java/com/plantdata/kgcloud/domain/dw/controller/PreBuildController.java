package com.plantdata.kgcloud.domain.dw.controller;


import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderMatchAttrRsp;
import com.plantdata.kgcloud.domain.dw.rsp.PreBuilderSearchRsp;
import com.plantdata.kgcloud.domain.dw.service.PreBuilderService;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.sdk.req.PreBuilderGraphMapReq;
import com.plantdata.kgcloud.sdk.req.PreBuilderMatchAttrReq;
import com.plantdata.kgcloud.sdk.req.PreBuilderSearchReq;
import com.plantdata.kgcloud.sdk.req.SchemaQuoteReq;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "预构建模式")
@RestController
@RequestMapping("/builder")
public class PreBuildController {

    @Autowired
    private PreBuilderService preBuilderService;

    @ApiOperation("预构建模式-查找所有")
    @PostMapping("/all")
    public ApiReturn<Page<PreBuilderSearchRsp>> findModel(@RequestBody PreBuilderSearchReq preBuilderSearchReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.findModel(userId,preBuilderSearchReq));
    }

    @ApiOperation("预构建模式-获取数据库模式")
    @PostMapping("/{databaseId}/detail")
    public ApiReturn<PreBuilderSearchRsp> databaseDetail(@PathVariable("databaseId") Long databaseId) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.databaseDetail(userId,databaseId));
    }

    @ApiOperation("预构建模式-匹配属性")
    @PostMapping("/match/attr")
    public ApiReturn<List<PreBuilderMatchAttrRsp>> matchAttr(@RequestBody PreBuilderMatchAttrReq preBuilderMatchAttrReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.matchAttr(userId,preBuilderMatchAttrReq));
    }

    @ApiOperation("预构建模式-引入模式配置保存")
    @PostMapping("/save/graph/map")
    public ApiReturn saveGraphMap(@RequestBody PreBuilderGraphMapReq preBuilderGraphMapReq) {
        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.saveGraphMap(userId,preBuilderGraphMapReq));
    }

    @ApiOperation("预构建模式-查询图谱的映射配置")
    @GetMapping("/get/{kgName}/map")
    public ApiReturn<List<SchemaQuoteReq>> getGraphMap(@PathVariable("kgName") String kgName) {

        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.getGraphMap(userId,kgName));
    }

    @ApiOperation("预构建模式-查询分类")
    @GetMapping("/get/types")
    public ApiReturn<List<String>> getTypes() {

        String userId = SessionHolder.getUserId();
        return ApiReturn.success(preBuilderService.getTypes(userId));
    }

}

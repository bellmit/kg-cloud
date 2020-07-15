package ai.plantdata.kgcloud.domain.data.obtain.controller;

import ai.plantdata.cloud.bean.ApiReturn;
import ai.plantdata.kgcloud.domain.common.module.GraphDataObtainInterface;
import ai.plantdata.kgcloud.sdk.EditClient;
import ai.plantdata.kgcloud.sdk.req.app.EntityQueryReq;
import ai.plantdata.kgcloud.sdk.req.app.OpenEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.OpenBatchResult;
import ai.plantdata.kgcloud.sdk.rsp.app.OpenBatchSaveEntityRsp;
import ai.plantdata.kgcloud.sdk.rsp.edit.DeleteResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/7 10:49
 */
@RestController
@RequestMapping("v3/kgdata/entity")
public class EntityController implements GraphDataObtainInterface {

    @Autowired
    private EditClient editClient;

    @ApiOperation(value = "批量实体查询", notes = "按数值属性筛选实例")
    @PostMapping("{kgName}/list")
    public ApiReturn<List<OpenEntityRsp>> queryEntityList(@PathVariable("kgName") String kgName,
                                                          @RequestBody EntityQueryReq entityQueryReq) {
        return editClient.queryEntityList(kgName, entityQueryReq);
    }

    @ApiOperation(value = "批量实体新增及更新", notes = "新增实体节点及其数值属性，或更新实体的数值属性")
    @PostMapping("{kgName}")
    public ApiReturn<OpenBatchResult<OpenBatchSaveEntityRsp>> batchAdd(@PathVariable("kgName") String kgName,
                                                                       @ApiParam(value = "true修改", required = true) @RequestParam boolean add,
                                                                       @RequestBody List<OpenBatchSaveEntityRsp> batchEntity) {
        return editClient.saveOrUpdate(kgName, add, batchEntity);
    }

    @ApiOperation(value = "批量实体删除", notes = "删除实体。")
    @PostMapping("{kgName}/batch/delete")
    public ApiReturn<List<DeleteResult>> batchDeleteEntities(@PathVariable("kgName") String kgName,
                                                             @RequestBody List<Long> ids) {
        return editClient.batchDeleteEntities(kgName, ids);
    }
}

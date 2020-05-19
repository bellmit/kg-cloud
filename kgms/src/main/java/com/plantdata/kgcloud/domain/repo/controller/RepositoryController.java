package com.plantdata.kgcloud.domain.repo.controller;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.repo.constatn.StringConstants;
import com.plantdata.kgcloud.domain.repo.model.req.RepositoryReq;
import com.plantdata.kgcloud.domain.repo.model.rsp.RepositoryRsp;
import com.plantdata.kgcloud.domain.repo.service.RepositoryService;
import com.plantdata.kgcloud.security.SessionHolder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cjw
 * @date 2020/5/18  9:57
 */
@RestController
@RequestMapping("repository")
public class RepositoryController {

    @Autowired
    private RepositoryService repositoryService;

    @ApiOperation("组件列表")
    @GetMapping
    public ApiReturn<List<RepositoryRsp>> repositoryRsp() {
        return ApiReturn.success(repositoryService.list(SessionHolder.getUserId()));
    }

    @ApiOperation("组件新增")
    @PostMapping
    public  ApiReturn<Integer> add(@RequestBody  RepositoryReq repositoryReq){

    }

    @ApiOperation("组件启用")
    @PostMapping("{id}/start")
    public ApiReturn<String> startRepository(@PathVariable(name = "id") Integer id) {
        repositoryService.updateStatus(id, true);
        return ApiReturn.success(StringConstants.SUCCESS);
    }

    @ApiOperation("组件停用")
    @PostMapping("{id}/stop")
    public ApiReturn<String> stopRepository(@PathVariable(name = "id") Integer id) {
        repositoryService.updateStatus(id, false);
        return ApiReturn.success(StringConstants.SUCCESS);
    }

    @ApiOperation("使用记录")
    @GetMapping("{id}/log")
    public ApiReturn<String> useLog(@PathVariable(name = "id") Integer id) {
        repositoryService.useLog(id, SessionHolder.getUserId());
        return ApiReturn.success(StringConstants.SUCCESS);
    }

}

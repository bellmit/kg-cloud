package com.plantdata.kgcloud.plantdata.controller;

import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.bean.BasePage;
import com.plantdata.kgcloud.plantdata.bean.rule.GraphmRuleMapBean;
import com.plantdata.kgcloud.plantdata.converter.common.BasicConverter;
import com.plantdata.kgcloud.plantdata.converter.rule.GraphRuleConverter;
import com.plantdata.kgcloud.plantdata.req.rule.GraphRuleAdd;
import com.plantdata.kgcloud.plantdata.req.rule.GraphRuleDelect;
import com.plantdata.kgcloud.plantdata.req.rule.GraphRuleListByPageParameter;
import com.plantdata.kgcloud.plantdata.req.rule.GraphRuleUpdate;
import com.plantdata.kgcloud.sdk.KgmsClient;
import com.plantdata.kgcloud.sdk.req.GraphConfKgqlReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfKgqlRsp;
import com.plantdata.kgcloud.sdk.rsp.app.RestData;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.function.Function;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/25 10:08
 */
@RestController("graphRuleController-v2")
@RequestMapping("sdk/graph/rule")
public class GraphRuleController implements SdkOldApiInterface {

    @Autowired
    private KgmsClient kgmsClient;

    @GetMapping("get/list")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "ruleType", defaultValue = "0", dataType = "int", paramType = "query", value = "规则类型 1gis规则 0或不传是图探索规则"),
            @ApiImplicitParam(name = "pageNo", defaultValue = "1", dataType = "int", paramType = "query", value = "分页页码最小值为1"),
            @ApiImplicitParam(name = "pageSize", defaultValue = "10", dataType = "int", paramType = "query", value = "分页每页最小为1"),
    })
    public RestResp<RestData<GraphmRuleMapBean>> listByPage(@Valid @ApiIgnore GraphRuleListByPageParameter param) {
        ApiReturn<BasePage<GraphConfKgqlRsp>> apiReturn = kgmsClient.selectKgql(param.getKgName(), param.getRuleType(), param.getPageNo(), param.getPageSize());
        RestData<GraphmRuleMapBean> restData = BasicConverter.convert(apiReturn,
                a -> BasicConverter.basePageToRestData(a, GraphRuleConverter::graphConfKgQlRspToGraphRuleMapBean));
        return new RestResp<>(restData);
    }

    @ApiOperation("详情")
    @GetMapping("get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", defaultValue = "1", dataType = "int", paramType = "query", value = "规则id"),
    })
    public RestResp<GraphmRuleMapBean> get(@RequestParam(value = "kgName", required = false) String kgName, @RequestParam("id") Integer id) {
        Function<Integer, ApiReturn<GraphConfKgqlRsp>> rspFunction = a -> kgmsClient.detailKgql(Long.valueOf(a));
        GraphmRuleMapBean mapBean = rspFunction
                .andThen(a -> BasicConverter.convert(a, GraphRuleConverter::graphConfKgQlRspToGraphRuleMapBean))
                .apply(id);
        return new RestResp<>(mapBean);
    }


    @PostMapping("add")
    @ApiOperation("新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "bean", required = true, dataType = "string", paramType = "form", value = "保存的数据"),
    })
    public RestResp<GraphmRuleMapBean> add(@Valid @ApiIgnore GraphRuleAdd graphRuleAdd) {
        Function<GraphConfKgqlReq, ApiReturn<GraphConfKgqlRsp>> returnFunction = a -> kgmsClient.saveKgql(graphRuleAdd.getKgName(), a);
        GraphmRuleMapBean ruleMapBean = returnFunction
                .compose(GraphRuleConverter::graphRuleAddToGraphConfKgqlReq)
                .andThen(a -> BasicConverter.convert(a, GraphRuleConverter::graphConfKgQlRspToGraphRuleMapBean))
                .apply(graphRuleAdd);
        return new RestResp<>(ruleMapBean);
    }


    @PostMapping("delete")
    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
    })
    public RestResp delete(@Valid @ApiIgnore GraphRuleDelect graphRuleDelect) {
        kgmsClient.deleteKgql(Long.valueOf(graphRuleDelect.getId()));
        return new RestResp<>();
    }

    @PostMapping("update")
    @ApiOperation("修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "kgName", required = true, dataType = "string", paramType = "query", value = "图谱名称"),
            @ApiImplicitParam(name = "id", required = true, dataType = "int", paramType = "form", value = "id"),
            @ApiImplicitParam(name = "data", required = true, dataType = "string", paramType = "form", value = "需要修改的数据"),
    })
    public RestResp update(@Valid @ApiIgnore GraphRuleUpdate update) {
        Function<GraphConfKgqlReq, ApiReturn<GraphConfKgqlRsp>> returnFunction = a -> kgmsClient.updateKgql(Long.valueOf(update.getId()), a);
        returnFunction.compose(GraphRuleConverter::graphRuleMapBeanToGraphConfKgqlReq)
                .apply(update.getBean());
        return new RestResp<>();
    }
}

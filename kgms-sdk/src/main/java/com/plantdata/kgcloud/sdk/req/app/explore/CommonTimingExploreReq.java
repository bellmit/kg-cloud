package com.plantdata.kgcloud.sdk.req.app.explore;

import com.google.common.collect.Sets;
import com.plantdata.kgcloud.sdk.req.app.explore.common.BasicGraphExploreReq;
import com.plantdata.kgcloud.sdk.req.app.TimeFilterExploreReq;
import com.plantdata.kgcloud.sdk.req.app.explore.common.CommonFiltersReq;
import com.plantdata.kgcloud.sdk.req.app.function.GraphCommonReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.GraphTimingReqInterface;
import com.plantdata.kgcloud.sdk.req.app.function.SecondaryScreeningInterface;
import com.plantdata.kgcloud.sdk.rsp.app.explore.BasicGraphExploreRsp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;


/**
 * @author cjw
 * @version 1.0
 * @date 2019/11/1 11:02
 */
@ApiModel("时序图探索")
@Getter
@Setter
public class CommonTimingExploreReq extends BasicGraphExploreReq implements GraphCommonReqInterface, GraphTimingReqInterface, SecondaryScreeningInterface {
    @NotNull
    @ApiModelProperty("普通参数")
    private CommonFiltersReq common;
    @ApiModelProperty("时间参数过滤")
    private TimeFilterExploreReq timeFilters;

    @Override
    public CommonFiltersReq fetchCommon() {
        return common;
    }

    @Override
    public TimeFilterExploreReq fetchTimeFilter() {
        return timeFilters;
    }

    @Override
    public Set<Long> getNeedSaveEntityIds() {
        return common.getId() == null ? Collections.emptySet() : Sets.newHashSet(common.getId());
    }

    @Override
    public BasicGraphExploreRsp getGraphReq() {
        return super.getGraphReq();
    }
}

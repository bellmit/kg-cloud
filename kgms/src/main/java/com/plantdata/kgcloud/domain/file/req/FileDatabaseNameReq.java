package com.plantdata.kgcloud.domain.file.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lp
 * @date 2020/5/20 14:46
 */
@Data
@ApiModel("数据库改名参数")
public class FileDatabaseNameReq {

    @ApiModelProperty("数据库id")
    private Long databaseId;

    @ApiModelProperty("名称")
    private String name;

}

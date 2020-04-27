package com.plantdata.kgcloud.domain.edit.req.file;

import com.plantdata.kgcloud.bean.BaseReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author EYE
 */
@Data
@ApiModel("实体文件关联查询")
public class EntityFileRelationQueryReq extends BaseReq {

    @ApiModelProperty(value = "文件名")
    private String name;

}

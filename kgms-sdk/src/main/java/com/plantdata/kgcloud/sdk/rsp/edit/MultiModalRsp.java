package com.plantdata.kgcloud.sdk.rsp.edit;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: LinHo
 * @Date: 2020/3/23 10:33
 * @Description:
 */
@Setter
@Getter
public class MultiModalRsp {

    private String id;

    @ApiModelProperty("实体id")
    @JsonAlias("entity_id")
    private Long entityId;

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("数据连接")
    @JsonAlias("data_href")
    private String dataHref;

    @ApiModelProperty("缩略图路径")
    @JsonAlias("thumb_path")
    private String thumbPath;

    @ApiModelProperty("文件类型")
    private String type;

}

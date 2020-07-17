package ai.plantdata.kgcloud.sdk.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lp
 * @date 2020/5/21 17:04
 */
@Data
@ApiModel("图谱索引查询")
public class GraphConfIndexStatusRsp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "kgName")
    private String kgName;

    @ApiModelProperty(value = "索引状态(0:关闭，1:开启)")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "更新时间")
    private Date updateAt;

}

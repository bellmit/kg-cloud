package com.plantdata.kgcloud.plantdata.req.app;

import com.plantdata.kgcloud.sdk.validator.ChooseCheck;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AssociationParameter {
    @NotBlank
    private String kgName;
    @NotNull
    private Long entityId;
    @ChooseCheck(value = "[0,1,2]")
    private Integer direction = 1;
    @NotNull
    private List<Integer> allowAtts;
    private List<String> allowAttsKey;
    private Integer pageSize = 10;

}

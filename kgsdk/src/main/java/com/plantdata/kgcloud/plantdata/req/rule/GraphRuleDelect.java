package com.plantdata.kgcloud.plantdata.req.rule;

import lombok.*;

/**
 * @author Administrator
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GraphRuleDelect {
    private String kgName;
    private Integer id;
}

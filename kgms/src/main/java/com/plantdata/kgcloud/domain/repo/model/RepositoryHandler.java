package com.plantdata.kgcloud.domain.repo.model;

import com.plantdata.kgcloud.domain.repo.enums.HandleType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author cjw
 * @date 2020/5/15  11:15
 */
@Getter
@Setter
public class RepositoryHandler {
    private int id;
    private HandleType handleType;
    private String requestServerName;

}

package com.plantdata.kgcloud.sdk.rsp.app.semantic;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * @author Administrator
 */
@Getter
@Setter
public class IntentDataBean {
    private int count;
    private List<ElementSeqBean> array;

}

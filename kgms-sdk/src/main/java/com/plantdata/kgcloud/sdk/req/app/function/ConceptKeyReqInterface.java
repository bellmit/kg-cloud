package com.plantdata.kgcloud.sdk.req.app.function;

import java.util.List;

/**
 * @author cjw
 * @version 1.0
 * @date 2019/12/17 17:27
 */
public interface ConceptKeyReqInterface {


    default List<Long> getAllowConcepts() {
        return null;
    }

    default void setAllowConcepts(List<Long> disAllowConcepts) {

    }

    default List<String> getAllowConceptsKey() {
        return null;
    }
}

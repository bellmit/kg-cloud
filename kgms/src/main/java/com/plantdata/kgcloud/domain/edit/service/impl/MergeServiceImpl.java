package com.plantdata.kgcloud.domain.edit.service.impl;

import ai.plantdata.kg.api.edit.MergeApi;
import ai.plantdata.kg.api.edit.merge.EntityMergeSourceVO;
import ai.plantdata.kg.api.edit.merge.MergeEntity4Edit;
import ai.plantdata.kg.api.edit.merge.MergeEntityDetail;
import ai.plantdata.kg.api.edit.merge.MergeFinalEntityFrom;
import ai.plantdata.kg.api.edit.merge.WaitMergeVO;
import cn.hiboot.mcn.core.model.result.RestResp;
import com.plantdata.kgcloud.bean.BaseReq;
import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.edit.converter.RestRespConverter;
import com.plantdata.kgcloud.domain.edit.service.MergeService;
import com.plantdata.kgcloud.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: LinHo
 * @Date: 2019/11/28 15:03
 * @Description:
 */
@Service
public class MergeServiceImpl implements MergeService {

    @Autowired
    private MergeApi mergeApi;


    @Override
    public Set<String> allSource(String kgName) {
        return RestRespConverter.convert(mergeApi.allSource(kgName)).orElse(Collections.emptySet());
    }

    @Override
    public List<EntityMergeSourceVO> getSourceSort(String kgName) {
        return RestRespConverter.convert(mergeApi.getSourceSort(kgName))
                .orElse(Collections.emptyList());
    }

    @Override
    public void saveSourceSort(String kgName, Map<Integer, String> sourceList) {
        RestRespConverter.convertVoid(mergeApi.saveSourceSort(kgName, sourceList));
    }

    @Override
    public void mergeByObjIds(String kgName, Integer mode, List<String> objIds) {
        RestRespConverter.convertVoid(mergeApi.mergeByObjIds(kgName, mode, objIds));
    }

    @Override
    public void doMergeEntity(String kgName, String objId, MergeFinalEntityFrom save) {
        RestRespConverter.convertVoid(mergeApi.doMergeEntity(kgName, objId, save));
    }


    @Override
    public String createMergeEntity(String kgName, Set<Long> ids) {
        return RestRespConverter.convert(mergeApi.createMergeEntity(kgName, new ArrayList<>(ids)))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ATTRIBUTE_DEFINITION_NOT_EXISTS));
    }

    @Override
    public void insertMergeEntity(String kgName, String objId, List<Long> ids) {
        RestRespConverter.convertVoid(mergeApi.insertMergeEntity(kgName, objId, ids));
    }

    @Override
    public void deleteMergeEntity(String kgName, String objId, Collection<Long> ids) {
        RestRespConverter.convertVoid(mergeApi.deleteMergeEntity(kgName, objId, new ArrayList<>(ids)));
    }

    @Override
    public Page<WaitMergeVO> waitList(String kgName, BaseReq req) {
        RestResp<List<WaitMergeVO>> listRestResp = mergeApi.waitList(kgName, req.getOffset(), req.getLimit());
        List<WaitMergeVO> list = RestRespConverter.convert(listRestResp).orElse(Collections.emptyList());
        Integer integer = RestRespConverter.convertCount(listRestResp).orElse(0);
        PageRequest pageable = PageRequest.of(req.getPage() - 1, req.getSize());
        return new PageImpl<>(list, pageable, integer);
    }

    @Override
    public void deleteWaitList(String kgName, List<String> ids) {
        RestRespConverter.convertVoid(mergeApi.deleteWaitList(kgName, ids));
    }

    @Override
    public List<MergeEntityDetail> showEntityList(String kgName, String objId) {
        RestResp<List<MergeEntityDetail>> listRestResp = mergeApi.showEntityList(kgName, objId);
        return RestRespConverter.convert(listRestResp).orElse(Collections.emptyList());
    }

    @Override
    public MergeEntity4Edit showDifferent(String kgName, String objId, Integer mode) {
        return RestRespConverter.convert(mergeApi.showDifferent(kgName, objId, mode))
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.ATTRIBUTE_DEFINITION_NOT_EXISTS));
    }
}

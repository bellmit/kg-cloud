package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfStatistical;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfStatisticalRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfStatisticalService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.sdk.req.GraphConfStatisticalReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfStatisticalRsp;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Created by plantdata-1007 on 2019/12/3.
 *
 */
@Service
public class GraphConfStatisticalServiceImpl implements GraphConfStatisticalService {
    @Autowired
    private GraphConfStatisticalRepository graphConfStatisticalRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfStatisticalRsp createStatistical(String kgName, GraphConfStatisticalReq req) {
        GraphConfStatistical targe = new GraphConfStatistical();
        BeanUtils.copyProperties(req, targe);
        targe.setId(kgKeyGenerator.getNextId());
        targe.setKgName(kgName);
        GraphConfStatistical result = graphConfStatisticalRepository.save(targe);
        return ConvertUtils.convert(GraphConfStatisticalRsp.class).apply(result);
    }

    @Override
    public List<GraphConfStatisticalRsp> saveAll(String kgName ,List<GraphConfStatisticalReq> listReq) {
        List<GraphConfStatistical> list = new ArrayList<>();
        for (GraphConfStatisticalReq req : listReq){
            GraphConfStatistical targe = new GraphConfStatistical();
            BeanUtils.copyProperties(req, targe);
            targe.setKgName(kgName);
            list.add(targe);
        }
        List<GraphConfStatistical> list1 = graphConfStatisticalRepository.saveAll(list);
        return list1.stream().map(ConvertUtils.convert(GraphConfStatisticalRsp.class)).collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GraphConfStatisticalRsp updateStatistical(Long id, GraphConfStatisticalReq req) {
        GraphConfStatistical graphConfStatistical = graphConfStatisticalRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_STATISTICAL_NOT_EXISTS));
        BeanUtils.copyProperties(req, graphConfStatistical);
        GraphConfStatistical result = graphConfStatisticalRepository.save(graphConfStatistical);
        return ConvertUtils.convert(GraphConfStatisticalRsp.class).apply(result);
    }

    @Override
    public List<GraphConfStatisticalRsp> updateAll(String kgName, List<GraphConfStatisticalReq> reqs) {
        List<GraphConfStatistical> list = graphConfStatisticalRepository.findByKgName(kgName);
        for (GraphConfStatisticalReq req : reqs){
            GraphConfStatistical targe = new GraphConfStatistical();
            BeanUtils.copyProperties(req, targe);
            list.add(targe);
        }
        List<GraphConfStatistical> list1 = graphConfStatisticalRepository.saveAll(list);
        return list1.stream().map(ConvertUtils.convert(GraphConfStatisticalRsp.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStatistical(Long id) {
        GraphConfStatistical graphConfStatistical = graphConfStatisticalRepository.findById(id)
                .orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.CONF_STATISTICAL_NOT_EXISTS));
        graphConfStatisticalRepository.delete(graphConfStatistical);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInBatch( List<Long> ids) {
        List<GraphConfStatistical> allById = graphConfStatisticalRepository.findAllById(ids);
        graphConfStatisticalRepository.deleteInBatch(allById);
    }

    @Override
    public List<GraphConfStatisticalRsp> findByKgName(String kgName) {
        List<GraphConfStatistical> all = graphConfStatisticalRepository.findByKgName(kgName);
        return all.stream().map(ConvertUtils.convert(GraphConfStatisticalRsp.class)).collect(Collectors.toList());
    }

    @Override
    public List<GraphConfStatisticalRsp> findAll() {
        List<GraphConfStatistical> all = graphConfStatisticalRepository.findAll();
        return all.stream().map(ConvertUtils.convert(GraphConfStatisticalRsp.class)).collect(Collectors.toList());
    }
}

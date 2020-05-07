package com.plantdata.kgcloud.domain.graph.config.service.impl;

import com.plantdata.kgcloud.domain.graph.config.entity.GraphConfQa;
import com.plantdata.kgcloud.domain.graph.config.repository.GraphConfQaRepository;
import com.plantdata.kgcloud.domain.graph.config.service.GraphConfQaService;
import com.plantdata.kgcloud.sdk.req.GraphConfQaReq;
import com.plantdata.kgcloud.sdk.rsp.GraphConfQaRsp;
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
 * @author jiangdeming
 * @date 2019/12/2
 */
@Service
public class GraphConfQaServiceImpl implements GraphConfQaService {
    @Autowired
    private GraphConfQaRepository graphConfQaRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<GraphConfQaRsp> saveQa(String kgName, List<GraphConfQaReq> reqs) {
        graphConfQaRepository.deleteByKgName(kgName);
        List<GraphConfQa> list = new ArrayList<>();
        for (GraphConfQaReq req : reqs) {
            GraphConfQa targe = new GraphConfQa();
            BeanUtils.copyProperties(req, targe);
            targe.setId(kgKeyGenerator.getNextId());
            String s = targe.getQuestion();
            int count = 0;
            int index = s.indexOf("$entity");
            while (index > -1) {
                count++;
                s = s.substring(index + 1);
                index = s.indexOf("$entity");
            }
            targe.setCount(count);
            targe.setKgName(kgName);
            list.add(targe);
        }
        List<GraphConfQa> result = graphConfQaRepository.saveAll(list);
        return result.stream().map(ConvertUtils.convert(GraphConfQaRsp.class)).collect(Collectors.toList());

    }


    @Override
    public List<GraphConfQaRsp> findByKgName(String kgName) {
        List<GraphConfQa> all = graphConfQaRepository.findAll();
        List<GraphConfQa> newList = new ArrayList<>();
        if(all != null){
            for(GraphConfQa qa: all){
                if(qa.getKgName().equals(kgName)){
                    newList.add(qa);
                }
            }
        }
        return newList.stream().map(ConvertUtils.convert(GraphConfQaRsp.class)).collect(Collectors.toList());
    }
}

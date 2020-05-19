package com.plantdata.kgcloud.domain.repo.factory;

import com.google.common.collect.Lists;
import com.plantdata.kgcloud.domain.app.converter.BasicConverter;
import com.plantdata.kgcloud.domain.graph.manage.service.GraphService;
import com.plantdata.kgcloud.domain.repo.checker.ConsulServiceChecker;
import com.plantdata.kgcloud.domain.repo.checker.FileServiceChecker;
import com.plantdata.kgcloud.domain.repo.checker.MongoServiceChecker;
import com.plantdata.kgcloud.domain.repo.checker.ServiceChecker;
import com.plantdata.kgcloud.domain.repo.checker.service.ConsulService;
import com.plantdata.kgcloud.domain.repo.checker.service.FileService;
import com.plantdata.kgcloud.domain.repo.checker.service.MongoService;
import com.plantdata.kgcloud.domain.repo.enums.RepoCheckType;
import com.plantdata.kgcloud.domain.repo.model.RepoCheckConfig;
import com.plantdata.kgcloud.domain.repo.model.RepositoryRoot;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.SpringContextUtils;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjw
 * @date 2020/5/15  11:55
 */
public class ServiceCheckerFactory {

    private static ServiceChecker match(RepoCheckType checkType, List<RepoCheckConfig> checkConfigs) {
        switch (checkType) {
            case CONSUL:
                return new ConsulServiceChecker(SpringContextUtils.getBean(DiscoveryClient.class), checkConfigs);
            case FILE:
                return new FileServiceChecker(checkConfigs);
            case MONGO:
                return new MongoServiceChecker(null, null);
            default:
                throw new BizException("暂不支持;敬请期待");
        }
    }


    public static ServiceChecker match(RepoCheckType checkType,RepoCheckConfig checkConfig) {
        switch (checkType) {
            case CONSUL:
                return new ConsulServiceChecker(SpringContextUtils.getBean(DiscoveryClient.class), Lists.newArrayList(checkConfig));
            case FILE:
                return new FileServiceChecker(Lists.newArrayList(checkConfig));
            case MONGO:
                return new MongoServiceChecker(null, null);
            default:
                throw new BizException("暂不支持;敬请期待");
        }
    }
    public static List<ServiceChecker> factory(List<RepoCheckConfig> allConfigs) {
        Map<RepoCheckType, List<RepoCheckConfig>> checkTypeListMap = allConfigs.stream()
                .collect(Collectors.groupingBy(RepoCheckConfig::getCheckType));
        return Arrays.stream(RepoCheckType.values())
                .filter(checkTypeListMap::containsKey)
                .map(a -> match(a, checkTypeListMap.get(a)))
                .collect(Collectors.toList());
    }

}

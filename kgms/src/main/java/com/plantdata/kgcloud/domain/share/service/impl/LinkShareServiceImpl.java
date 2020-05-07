package com.plantdata.kgcloud.domain.share.service.impl;

import com.plantdata.kgcloud.bean.ApiReturn;
import com.plantdata.kgcloud.domain.share.entity.LinkShare;
import com.plantdata.kgcloud.domain.share.repository.LinkShareRepository;
import com.plantdata.kgcloud.domain.share.rsp.LinkShareRsp;
import com.plantdata.kgcloud.domain.share.rsp.ShareRsp;
import com.plantdata.kgcloud.domain.share.service.LinkShareService;
import com.plantdata.kgcloud.sdk.UserClient;
import com.plantdata.kgcloud.sdk.req.SelfSharedRsp;
import com.plantdata.kgcloud.sdk.rsp.LinkShareSpaRsp;
import com.plantdata.kgcloud.sdk.rsp.UserLimitRsp;
import com.plantdata.kgcloud.security.JwtClient;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jdm on 2019/12/7 14:28.
 */
@Service
public class LinkShareServiceImpl implements LinkShareService {


    @Autowired
    private UserClient userClient;

    @Autowired
    private LinkShareRepository linkShareRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;
    @Autowired
    private JwtClient jwtClient;

    @Override
    public LinkShareSpaRsp shareStatus(String userId, String kgName, String spaId) {
        ApiReturn<UserLimitRsp> detail = userClient.getCurrentUserLimitDetail();
        UserLimitRsp data = detail.getData();
        LinkShareSpaRsp linkShareRsp = new LinkShareSpaRsp();
        linkShareRsp.setKgName(kgName);
        linkShareRsp.setSpaId(spaId);
        if (data.getShareable()) {
            Optional<LinkShare> linkShare = linkShareRepository.findByKgNameAndSpaId(kgName, spaId);
            if (linkShare.isPresent()) {
                LinkShare linkShare1 = linkShare.get();
                Boolean shared = linkShare1.getShared();
                linkShareRsp.setShareable(shared);
            } else {
                linkShareRsp.setShareable(false);
            }
        } else {
            linkShareRsp.setShareable(false);
        }
        return linkShareRsp;
    }

    @Override
    public LinkShareRsp shareStatus(String userId, String kgName) {
        LinkShareRsp linkShareRsp = linkShareRsp();
        List<LinkShare> all = linkShareRepository.findByUserIdAndKgName(userId, kgName);
        List<ShareRsp> collect = all.stream().map(ConvertUtils.convert(ShareRsp.class)).collect(Collectors.toList());
        linkShareRsp.setShareList(collect);
        return linkShareRsp;
    }

    @Override
    public LinkShareRsp liteShareStatus(String userId) {
        LinkShareRsp linkShareRsp = linkShareRsp();
        LinkShare linkShare = new LinkShare();
        linkShare.setUserId(userId);
        linkShare.setSpaId("graph");
        List<LinkShare> all = linkShareRepository.findAll(Example.of(linkShare));
        List<ShareRsp> collect = all.stream().map(ConvertUtils.convert(ShareRsp.class)).collect(Collectors.toList());
        linkShareRsp.setShareList(collect);
        return linkShareRsp;
    }

    private LinkShareRsp linkShareRsp() {
        ApiReturn<UserLimitRsp> detail = userClient.getCurrentUserLimitDetail();
        UserLimitRsp data = detail.getData();
        LinkShareRsp linkShareRsp = new LinkShareRsp();
        if (data != null && data.getShareable() != null && data.getShareable()) {
            linkShareRsp.setHasRole(1);
        } else {
            linkShareRsp.setHasRole(0);
        }
        return linkShareRsp;
    }

    private LinkShare getOne(String kgName, String spaId) {
        Optional<LinkShare> bean = linkShareRepository.findByKgNameAndSpaId(kgName, spaId);
        return bean.orElseGet(() -> {
            LinkShare share = new LinkShare();
            share.setId(kgKeyGenerator.getNextId());
            share.setKgName(kgName);
            share.setSpaId(spaId);
            return share;
        });
    }

    @Override
    public ShareRsp shareLink(String userId, String kgName, String spaId) {
        LinkShare linkShare = getOne(kgName, spaId);
        linkShare.setShared(true);
        linkShare.setUserId(userId);
        LinkShare save = linkShareRepository.save(linkShare);
        return ConvertUtils.convert(ShareRsp.class).apply(save);
    }

    @Override
    public ShareRsp shareCancel(String userId, String kgName, String spaId) {
        LinkShare linkShare = getOne(kgName, spaId);
        linkShare.setShared(false);
        linkShare.setUserId(userId);
        LinkShare save = linkShareRepository.save(linkShare);
        return ConvertUtils.convert(ShareRsp.class).apply(save);
    }

    @Override
    public SelfSharedRsp shareSpaStatus(String userId, String kgName, String spaId, String token) {
        SelfSharedRsp selfSharedRsp = new SelfSharedRsp();
        if (!StringUtils.hasText(token)) {
            selfSharedRsp.setLogin(false);
        }
        selfSharedRsp.setSelf(true);
        UserLimitRsp data = userClient.getCurrentUserLimitDetail().getData();
        if(data !=null){
            selfSharedRsp.setSharePermission(data.getShareable());
        }else {
            selfSharedRsp.setSharePermission(false);
        }
        LinkShare linkShare = getOne(kgName, spaId);
        Boolean shared = linkShare.getShared();
        if (shared != null) {
            selfSharedRsp.setShareable(shared);
        } else {
            selfSharedRsp.setShareable(false);
        }
        return selfSharedRsp;
    }
}

package com.plantdata.kgcloud.domain.file.service.impl;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.plantdata.kgcloud.constant.FileConstants;
import com.plantdata.kgcloud.domain.edit.converter.DocumentConverter;
import com.plantdata.kgcloud.domain.edit.service.EntityFileRelationService;
import com.plantdata.kgcloud.domain.file.entity.FileData;
import com.plantdata.kgcloud.domain.file.req.FileDataBatchReq;
import com.plantdata.kgcloud.domain.file.req.FileDataQueryReq;
import com.plantdata.kgcloud.domain.file.req.FileDataReq;
import com.plantdata.kgcloud.domain.file.req.FileDataUpdateReq;
import com.plantdata.kgcloud.domain.file.rsq.FileDataRsp;
import com.plantdata.kgcloud.domain.file.service.FileDataService;
import com.plantdata.kgcloud.security.SessionHolder;
import com.plantdata.kgcloud.template.FastdfsTemplate;
import com.plantdata.kgcloud.util.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lp
 * @date 2020/5/20 17:08
 */
@Service
public class FileDataServiceImpl implements FileDataService {

    @Autowired
    private DocumentConverter documentConverter;
    @Autowired
    private MongoClient mongoClient;
    @Autowired
    private FastdfsTemplate fastdfsTemplate;
    @Autowired
    private EntityFileRelationService entityFileRelationService;

    private MongoCollection<Document> getFileCollection() {
        return mongoClient.getDatabase(FileConstants.DW_PREFIX + SessionHolder.getUserId()).getCollection(FileConstants.FILE);
    }

    private final Function<FileData, FileDataRsp> fileData2rsp = (s) -> {
        FileDataRsp dataRsp = new FileDataRsp();
        BeanUtils.copyProperties(s, dataRsp);
        return dataRsp;
    };

    @Override
    public Page<FileDataRsp> getFileData(String userId, Long databaseId, Long tableId, FileDataQueryReq req) {
        Integer size = req.getSize();
        Integer page = (req.getPage() - 1) * size;
        List<Bson> bsons = new ArrayList<>(3);
        if (StringUtils.isNotBlank(req.getName())) {
            bsons.add(Filters.regex("name", Pattern.compile("^.*" + req.getName() + ".*$")));
        }
        bsons.add(Filters.eq("databaseId", databaseId));
        bsons.add(Filters.eq("tableId", tableId));

        FindIterable<Document> findIterable = getFileCollection().find(Filters.and(bsons)).skip(page).limit(size + 1).sort(new Document("createTime", -1));
        List<FileData> fileDatas = documentConverter.toBeans(findIterable, FileData.class);
        List<FileDataRsp> fileDataRsps = fileDatas.stream().map(fileData2rsp).collect(Collectors.toList());
        int count = fileDataRsps.size();
        if (count > size) {
            fileDataRsps.remove(size.intValue());
            count += page;
        }

        return new PageImpl<>(fileDataRsps, PageRequest.of(req.getPage() - 1, size), count);
    }

    @Override
    public FileData fileAdd(FileDataReq req) {
        byte[] bytes = fastdfsTemplate.downloadFile(req.getPath());

        FileData fileData = ConvertUtils.convert(FileData.class).apply(req);
        fileData.setFileSize((long) bytes.length);
        if (req.getFileName() != null && req.getFileName().contains(".")) {
            fileData.setType(req.getFileName().substring(req.getFileName().lastIndexOf(".") + 1));
        }
        fileData.setTitle(req.getFileName());
        fileData.setCreateTime(new Date());

        Document document = documentConverter.toDocument(fileData);
        getFileCollection().insertOne(document);

        return documentConverter.toBean(document, FileData.class);
    }

    @Override
    public void fileAddBatch(FileDataBatchReq req, MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return;
        }

        for (MultipartFile file : files) {
            FileData fileData = ConvertUtils.convert(FileData.class).apply(req);
            fileData.setFileSize(file.getSize());
            if (file.getOriginalFilename().contains(".")) {
                fileData.setType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
                fileData.setName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
            } else {
                fileData.setName(file.getOriginalFilename());
            }
            fileData.setTitle(file.getOriginalFilename());
            fileData.setPath(fastdfsTemplate.uploadFile(file).getFullPath());
            fileData.setCreateTime(new Date());
            Document document = documentConverter.toDocument(fileData);
            getFileCollection().insertOne(document);
        }
    }

    @Override
    public void fileUpdate(FileDataUpdateReq req) {
        Document document = getFileCollection().find(documentConverter.buildObjectId(req.getId())).first();
        if (document == null) {
            return;
        }
        FileData fileData = documentConverter.toBean(document, FileData.class);
        String id = fileData.getId();
        fileData.setId(null);
        fileData.setName(req.getName());
        fileData.setTitle(req.getName() + "." + fileData.getType());
        fileData.setKeyword(req.getKeyword());
        fileData.setDescription(req.getDescription());
        fileData.setOwner(req.getOwner());
        Document newDocument = documentConverter.toDocument(fileData);
        getFileCollection().updateOne(documentConverter.buildObjectId(id), new Document("$set", newDocument));
    }

    @Override
    public void fileDelete(String id) {
        getFileCollection().deleteOne(documentConverter.buildObjectId(id));
        // 删除实体文件关联
        entityFileRelationService.deleteRelationByFileId(id);
    }

    @Override
    public void fileDeleteBatch(List<String> ids) {
        List<ObjectId> collect = ids.stream().map(ObjectId::new).collect(Collectors.toList());
        getFileCollection().deleteMany(Filters.in("_id", collect));
        entityFileRelationService.deleteRelationByFileIds(ids);
    }
}

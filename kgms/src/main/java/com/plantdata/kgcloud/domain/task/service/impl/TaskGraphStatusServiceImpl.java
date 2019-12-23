package com.plantdata.kgcloud.domain.task.service.impl;

import com.plantdata.kgcloud.constant.KgmsErrorCodeEnum;
import com.plantdata.kgcloud.constant.TaskStatus;
import com.plantdata.kgcloud.domain.task.entity.TaskGraphStatus;
import com.plantdata.kgcloud.domain.task.repository.TaskGraphStatusRepository;
import com.plantdata.kgcloud.domain.task.req.TaskGraphStatusReq;
import com.plantdata.kgcloud.domain.task.rsp.TaskGraphStatusRsp;
import com.plantdata.kgcloud.domain.task.service.TaskGraphStatusService;
import com.plantdata.kgcloud.exception.BizException;
import com.plantdata.kgcloud.util.ConvertUtils;
import com.plantdata.kgcloud.util.KgKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author: LinHo
 * @Date: 2019/12/16 19:33
 * @Description:
 */
@Service
public class TaskGraphStatusServiceImpl implements TaskGraphStatusService {

    @Autowired
    private TaskGraphStatusRepository taskGraphStatusRepository;

    @Autowired
    private KgKeyGenerator kgKeyGenerator;


    @Override
    public TaskGraphStatus create(TaskGraphStatusReq taskGraphStatusReq) {
        TaskGraphStatus taskGraphStatus = ConvertUtils.convert(TaskGraphStatus.class).apply(taskGraphStatusReq);
        taskGraphStatus.setId(kgKeyGenerator.getNextId());
        taskGraphStatus = taskGraphStatusRepository.save(taskGraphStatus);
        System.out.println(taskGraphStatus);
        return taskGraphStatus;
    }


    @Override
    public TaskGraphStatusRsp getDetailsByKgName(String kgName) {
        TaskGraphStatus taskGraphStatus =
                TaskGraphStatus.builder().kgName(kgName).build();
        List<TaskGraphStatus> taskGraphStatusList = taskGraphStatusRepository.findAll(Example.of(taskGraphStatus),
                new Sort(Sort.Direction.DESC,"createAt"));
        if (CollectionUtils.isEmpty(taskGraphStatusList)) {
            throw BizException.of(KgmsErrorCodeEnum.TASK_STATUS_NOT_EXISTS);
        }
        return ConvertUtils.convert(TaskGraphStatusRsp.class).apply(taskGraphStatusList.get(0));
    }

    @Override
    public Boolean checkTask(String kgName) {
        TaskGraphStatus taskGraphStatus =
                TaskGraphStatus.builder().kgName(kgName).status(TaskStatus.PROCESSING.getStatus()).build();
        List<TaskGraphStatus> taskGraphStatusList = taskGraphStatusRepository.findAll(Example.of(taskGraphStatus),
                new Sort(Sort.Direction.DESC,"createAt"));
        if (CollectionUtils.isEmpty(taskGraphStatusList)) {
            return true;
        }else {
            throw BizException.of(KgmsErrorCodeEnum.SYNC_TASK_ONLY_ONE);
        }
    }

    @Override
    public void updateTaskStatus(Long id, String taskStatus) {
        Optional<TaskGraphStatus> optional = taskGraphStatusRepository.findById(id);
        TaskGraphStatus taskGraphStatus =
                optional.orElseThrow(() -> BizException.of(KgmsErrorCodeEnum.TASK_STATUS_NOT_EXISTS));
        taskGraphStatus.setStatus(taskStatus);
        taskGraphStatusRepository.save(taskGraphStatus);
    }
}

package com.plantdata.kgcloud.constant;

import lombok.Getter;

/**
 * @description:
 * @author: Bovin
 * @create: 2019-11-06 18:22
 **/
@Getter
public enum KgmsErrorCodeEnum implements ErrorCode {
    /**
     * errorCode 统一为6位数字
     * 前 两 位: 服务标识，默认以服务端口后两位 固定为12
     * 中间两位: 模块标识，
     * 后 两 位: 业务错误标识
     */
    DATASET_NOT_EXISTS(120101, "数据集不存在"),
    FOLDER_NOT_EXISTS(120102, "文件夹不存在"),
    FOLDER_DISABLE_DELETE(120103, "默认文件夹不允许删除"),
    DATASET_TYPE_NONSUPPORT(120104, "不支持的数据集类型"),
    DATASET_CONNECT_ERROR(120105, "数据连接失败"),
    DATASET_KEY_EXISTS(120106, "唯一标识已存在"),
    DATASET_EXPORT_FAIL(120107, "数据集导出失败"),
    DATASET_IMPORT_FAIL(120108, "数据集导出失败"),
    QUERYSETTING_NOT_EXISTS(120110, "规则配置不存在"),

    KTR_SAVE_FAIL(120113, "kettle文件生成失败"),

    ANNOTATION_NOT_EXISTS(120109, "标引不存在"),
    DATASET_ES_REQUEST_ERROR(120110, "es请求失败"),
    DATASET_ES_KEY_EXISTS(120111, "es唯一标识已存在"),
    TASK_STATUS_NOT_EXISTS(120112, "任务状态记录不存在"),

    MODEL_NOT_EXISTS(120201, "模型不存在"),
    /**
     * app模块
     */
    CONF_ALGORITHM_NOT_EXISTS(120601, "算法配置不存在"),
    CONF_KGQL_NOT_EXISTS(120602, "图谱业务不存在"),
    CONF_KGQLQUERYSETTING_ERROR(120614, "图谱业务规则错误"),
    CONF_QA_NOT_EXISTS(120603, "图谱问答不存在"),
    CONF_REASONING_NOT_EXISTS(120604, "图谱统计不存在"),
    CONF_STATISTICAL_NOT_EXISTS(120606, "图谱统计不存在"),
    ES_CONFIG_NOT_FOUND(120605, "未找到es配置"),
    GRAPH_TYPE_ERROR(120606, "图谱类型错误"),
    MY_DATA_NULL_ES(120607, "数据集不为搜索数据集"),
    DATE_PARSE_ERROR(120608, "数据时间格式转换失败"),
    EDGE_ATTR_DEF_NULL(120609, "边属性定义不存在"),
    TAG_HAVE_EXISTED(120610, "实体标签已存在"),
    ENTITY_TEMPLATE_NEED_CONCEPT_ID(120611, "实体模板下载需要概念id"),
    SPECIFIC_TEMPLATE_NEED_ATTR_ID(120612, "特定关系模板下载需要属性id"),
    YOURSELF_NOT_AS_PARENT(120613, "自身不能作为父概念"),
    SYNC_TASK_ONLY_ONE(120614, "一个图谱只能有一个异步任务"),
    SAME_ATTRIBUTE_ONLY_EXIST_ONE(120615, "同一个属性只能在属性分组里面存在一次"),
    TIME_FORM_MORE_THAN_TO(120616, "开始时间不能大于截止时间"),

    DICTIONARY_NOT_EXISTS(120301, "词典不存在"),

    WORD_NOT_EXISTS(120302, "词条不存在"),

    GRAPH_NOT_EXISTS(120401, "图谱不存在"),

    GRAPH_CREATE_FAIL(120402, "图谱创建失败"),

    GRAPH_OUT_LIMIT(120403, "超过图谱创建限制,请联系管理员"),

    REMOTE_SERVICE_ERROR(120501, "远程服务错误"),

    BASIC_INFO_NOT_EXISTS(120502, "概念或实体不存在"),

    ATTRIBUTE_DEFINITION_NOT_EXISTS(120503, "属性定义不存在"),

    DATA_CONVERSION_ERROR(120504, "数据转换错误"),

    FILE_IMPORT_ERROR(120505, "文件导入错误"),

    ATTR_GROUP_NOT_EXISTS(120506, "属性分组不存在"),

    ATTR_TEMPLATE_ERROR(120507, "属性模板配置不正确"),

    ATTR_TEMPLATE_NOT_EXISTS(120508, "属性模板不存在"),
    ;

    private final int errorCode;

    private final String message;

    KgmsErrorCodeEnum(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

}

/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.generator.parser.impl;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.apidoc.enums.*;
import com.acooly.openapi.apidoc.generator.parser.dto.ApiDataSize;
import com.acooly.openapi.apidoc.generator.parser.dto.ApiDocItemContext;
import com.acooly.openapi.apidoc.generator.parser.ApiDocParser;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.apidoc.utils.ApiDataTypeUtils;
import com.acooly.openapi.apidoc.utils.ApiDocPrivateUtils;
import com.acooly.openapi.apidoc.utils.ApiDocs;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.annotation.OpenApiFieldCondition;
import com.acooly.openapi.framework.common.annotation.OpenApiService;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.acooly.openapi.framework.common.message.ApiNotify;
import com.acooly.openapi.framework.common.message.ApiRequest;
import com.acooly.openapi.framework.common.message.ApiResponse;
import com.acooly.openapi.framework.domain.ApiMetaService;
import com.acooly.openapi.framework.service.ApiMetaServiceService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class ApiDocParserImpl implements ApiDocParser {

    private static final Logger logger = LoggerFactory.getLogger(ApiDocParserImpl.class);

    @Autowired
    private ApiMetaServiceService apiMetaServiceService;


    @Override
    public List<ApiDocService> parse() {
        List<ApiMetaService> metas = doQueryMetas();
        Set<ApiDocService> docs = Sets.newHashSet();
        for (ApiMetaService meta : metas) {
            docs.add(doParseService(meta));
        }
        return Lists.newArrayList(docs);
    }


    protected List<ApiMetaService> doQueryMetas() {
        List<ApiMetaService> apiMetaServices = apiMetaServiceService.getAll();
        return apiMetaServices;
    }


    /**
     * 防止多个扫描路径相互包含去掉重复的服务
     *
     * @param apiServiceDocs
     * @param apiServiceDoc
     * @return
     */
    public static boolean removeRepeat(List<ApiDocService> apiServiceDocs, ApiDocService apiServiceDoc) {
        boolean result = true;
        for (ApiDocService doc : apiServiceDocs) {
            if (StringUtils.equals(apiServiceDoc.getServiceNo(), doc.getServiceNo())) {
                result = false;
                break;
            }
        }
        return result;
    }


    /**
     * 解析文档
     *
     * @return
     */
    protected ApiDocService doParseService(ApiMetaService meta) {
        try {
            ApiDocService apiDocService = doFillApiDocService(meta);
            //获取服务对应的实体
            Class<?> requestClass = Class.forName(meta.getRequestClass());
            Class<?> responseClass = Strings.isNotBlank(meta.getResponseClass()) ? Class.forName(meta.getResponseClass()) : null;
            Class<?> notifyClass = Strings.isNotBlank(meta.getNotifyClass()) ? Class.forName(meta.getNotifyClass()) : null;

            List<ApiDocMessage> mds = Lists.newArrayList();
            mds.add(doParseMessage(apiDocService, requestClass));

            ResponseType responseType = apiDocService.getServiceType();
            if (responseClass != null) {
                if (responseType == ResponseType.SYN || responseType == ResponseType.ASNY) {
                    mds.add(doParseMessage(apiDocService, responseClass));
                }
            }

            if (notifyClass != null) {
                if (responseType == ResponseType.REDIRECT || responseType == ResponseType.ASNY) {
                    mds.add(doParseMessage(apiDocService, notifyClass));
                }
            }

            apiDocService.setApiDocMessages(mds);
            return apiDocService;
        } catch (Exception e) {
            logger.warn("parse service fail: {}", meta.getServiceName(), e);
        }
        return null;
    }

    private ApiDocService doFillApiDocService(ApiMetaService meta) {
        ApiDocService apiDocService = new ApiDocService();
        apiDocService.setName(meta.getServiceName());
        apiDocService.setVersion(meta.getVersion());
        apiDocService.setServiceNo(ApiDocs.getServiceNo(apiDocService.getName(), apiDocService.getVersion()));
        apiDocService.setBusiType(meta.getBusiType());
        apiDocService.setNote(meta.getNote());
        apiDocService.setTitle(meta.getServiceDesc());
        apiDocService.setServiceType(meta.getResponseType());
        apiDocService.setOwner(meta.getOwner());
        return apiDocService;
    }


    protected ApiDocMessage doParseMessage(ApiDocService apiDocService, final Class<?> clazz) {
        ApiDocMessage messageDoc = new ApiDocMessage();
        messageDoc.setServiceNo(apiDocService.getServiceNo());
        messageDoc.setMessageType(doParseMessageType(clazz));
        messageDoc.setMessageNo(messageDoc.getServiceNo() + "_" + messageDoc.getMessageType().code());
        messageDoc.setApiDocItems(doParseMessageItem(clazz, messageDoc.getMessageNo(), null));
        return messageDoc;
    }

    protected List<ApiDocItem> doParseMessageItem(final Class<?> clazz, String messageNo, String parentNo) {
        List<ApiDocItem> apiItems = Lists.newArrayList();
        Class<?> cc = clazz;
        while (cc != null && cc != ApiRequest.class && cc != ApiResponse.class && cc != ApiNotify.class && cc != Object.class){
            Field[] fields = cc.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                //OpenApiField标识的才做解析
                if (!field.isAnnotationPresent(OpenApiField.class)) {
                    log.warn("报文:{}，属性:{} 为标记OpenApiField，忽略解析", cc.getName(), field.getName());
                    continue;
                }
                ApiDocItem item = null;
                item = doParseItem(field);
                item.setItemNo(ApiDocs.genItemNo(messageNo, parentNo, item.getName()));
                Class<?> subItemType = null;
                if (ApiDataTypeUtils.isCollection(field)) {
                    // 集合或数组
                    Class<?> genericClass = ApiDocPrivateUtils.getParameterGenericType(clazz, field);
                    if (genericClass != null && !ApiDataTypeUtils.isSimpleType(genericClass)) {
                        subItemType = genericClass;
                    }
                } else if (ApiDataTypeUtils.isObject(field)) {
                    // 对象
                    subItemType = field.getType();
                }

                if (subItemType != null) {
                    item.setChildren(doParseMessageItem(subItemType, messageNo, item.getItemNo()));
                }
                item.setMessageNo(messageNo);
                item.setParentNo(parentNo);
                apiItems.add(item);
            }
            cc = cc.getSuperclass();
        }

        return apiItems;
    }

    /**
     * 解析属性
     *
     * @param field
     * @return
     */
    private ApiDocItem doParseItem(Field field) {
        OpenApiField openApiField = field.getAnnotation(OpenApiField.class);
        // 字段中文名
        String title = Strings.isBlankDefault(openApiField.desc(), field.getName());
        // 字段说明
        String descn = doParseApiDocItemDescn(field);
        // 是否加密
        ApiEncryptstatusEnum apiEncryptstatus = openApiField.security() ? ApiEncryptstatusEnum.yes : ApiEncryptstatusEnum.no;

        // 数据类型
        ApiDataTypeEnum dataType = ApiDataTypeUtils.getApiDataType(field);

        // 报文demo
        String demo = openApiField.demo();

        // 填写状态（必须，可选，条件可选）
        FieldStatus fieldStatus = doParseApiDocItemStatus(field);

        // 数据长度
        ApiDataSize apiDataSize = ApiDataTypeUtils.getApiDataSize(field);

        ApiDocItem apiDocItem = new ApiDocItem(field.getName(), title, descn, apiDataSize.getMin(), apiDataSize.getMax(),
                dataType, demo, fieldStatus, apiEncryptstatus);
        return apiDocItem;
    }

    /**
     * 解析字段说明
     *
     * @param field
     * @return
     */
    private String doParseApiDocItemDescn(Field field) {
        OpenApiField openApiField = field.getAnnotation(OpenApiField.class);
        // 字段中文名
        String title = Strings.isBlankDefault(openApiField.desc(), field.getName());
        String constraint = openApiField.constraint();
        if (Strings.isBlank(constraint)) {
            constraint = title;
        }
        ApiDocItemContext apiDocItemMemo = new ApiDocItemContext();
        apiDocItemMemo.setContent(constraint);
        if (field.getType().isEnum()) {
            Object[] objects = field.getType().getEnumConstants();
            Messageable messageable = null;
            for (Object object : objects) {
                messageable = (Messageable) object;
                apiDocItemMemo.put(messageable.code(), messageable.message());
            }
        }
        return apiDocItemMemo.toJson();
    }


    /**
     * 判断字段状态（是否为空，是否是条件可选）
     *
     * @param field
     * @return
     */
    private FieldStatus doParseApiDocItemStatus(Field field) {
        FieldStatus status = FieldStatus.O;
        if (!(field.getAnnotation(NotNull.class) == null && field.getAnnotation(NotEmpty.class) == null && field
                .getAnnotation(NotBlank.class) == null)) {
            status = FieldStatus.M;
        }
        //条件可选
        OpenApiFieldCondition openApiFieldCondition = field.getAnnotation(OpenApiFieldCondition.class);
        if (openApiFieldCondition != null) {
            status = FieldStatus.C;
        }
        return status;
    }


    private MessageTypeEnum doParseMessageType(Class<?> clazz) {
        MessageTypeEnum messageType = null;
        if (ApiRequest.class.isAssignableFrom(clazz)) {
            messageType = MessageTypeEnum.Request;
        } else if (ApiNotify.class.isAssignableFrom(clazz)) {
            messageType = MessageTypeEnum.Notify;
        } else if (ApiResponse.class.isAssignableFrom(clazz)) {
            messageType = MessageTypeEnum.Response;
        } else {
            throw new IllegalArgumentException("Class " + clazz.getName() + "not a supported " + "messageDoc type.");
        }
        return messageType;
    }

    private ApiDocTypeEnum getApiDocType(OpenApiService openApiService) {
        String code = openApiService.busiType().name();
        ApiDocTypeEnum apiDocTypeEnum = ApiDocTypeEnum.find(code);
        if (apiDocTypeEnum == null) {
            apiDocTypeEnum = ApiDocTypeEnum.Trade;
        }
        return apiDocTypeEnum;
    }

}

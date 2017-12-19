/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.parser.impl;

import com.acooly.core.utils.Strings;
import com.acooly.core.utils.enums.Messageable;
import com.acooly.openapi.apidoc.enums.*;
import com.acooly.openapi.apidoc.parser.ApiDocParser;
import com.acooly.openapi.apidoc.parser.dto.ApiDataSize;
import com.acooly.openapi.apidoc.parser.dto.ApiDocItemContext;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

@Slf4j
@Service("apiDocumentParser")
public class ApiDocParserImpl implements ApiDocParser {

    private static final Logger logger = LoggerFactory.getLogger(ApiDocParserImpl.class);

    @Autowired
    private ApiMetaServiceService apiMetaServiceService;


    @Override
    public List<ApiDocService> parse() {
        List<ApiMetaService> metas = doQueryMetas();
        List<ApiDocService> docs = Lists.newArrayList();
        for (ApiMetaService meta : metas) {
            ApiDocService doc = doParseService(meta);
            if (removeRepeat(docs, doc)) {
                docs.add(doc);
            }
        }
        return docs;
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
            ApiDocService apiDocService = doFullApiDocService(meta);
            //获取服务对应的实体
            Class<?> requestClass = Class.forName(meta.getRequestClass());
            Class<?> responseClass = Class.forName(meta.getResponseClass());
            Class<?> notifyClass = Class.forName(meta.getNotifyClass());

            List<ApiDocMessage> mds = Lists.newArrayList();
            mds.add(doParseMessage(requestClass));
            if (apiDocService.getServiceType() == ResponseType.SYN) {

            }

//            if (!ResponseType.REDIRECT.equals(openApiService.responseType())) {
//                mds.add(doParseMessage(responseClass));
//            } else {
//                //增加跳转类型messageDoc
//                ApiDocMessage redirectApiMessageDoc = new ApiDocMessage();
//                redirectApiMessageDoc.setApiItems(parseMessage(redirect.getClass(), null));
////                redirectApiMessageDoc.setMessageType(MessageType.Redirect);
//                mds.add(redirectApiMessageDoc);
//            }
//            if (openApiService.responseType() == ResponseType.ASNY
//                    || openApiService.responseType() == ResponseType.REDIRECT) {
//                if (!notifyClass.isAssignableFrom(ApiNotify.class)) {
//                    mds.add(doParseMessage(notifyClass));
//                }
//            }
            apiDocService.setApiDocMessages(mds);
//            doParseServiceDepend(meta, doc);
//            logger.info("Parse service success:{}", meta);
            return apiDocService;
        } catch (Exception e) {
            logger.warn("parse service fail: {}", meta.getServiceName(), e);
        }
        return null;
    }

    private ApiDocService doFullApiDocService(ApiMetaService meta) {

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


    protected ApiDocTypeEnum getApiDocType(OpenApiService openApiService) {
        String code = openApiService.busiType().name();
        ApiDocTypeEnum apiDocTypeEnum = ApiDocTypeEnum.find(code);
        if (apiDocTypeEnum == null) {
            apiDocTypeEnum = ApiDocTypeEnum.Trade;
        }
        return apiDocTypeEnum;
    }


    private boolean isSubClass(Class subClass, Class clazz) {
        if (subClass == null) {
            return false;
        }
        return clazz.isAssignableFrom(subClass);
    }

    protected ApiDocMessage doParseMessage(final Class<?> clazz) {
        ApiDocMessage messageDoc = new ApiDocMessage();
        messageDoc.setApiDocItems(doParseMessageItem(clazz, null));
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
//        messageDoc.setMessageType(messageType);
        return messageDoc;
    }

    protected List<ApiDocItem> doParseMessageItem(final Class<?> clazz, String igornFieldNames) {
        List<ApiDocItem> apiItems = Lists.newArrayList();
        Class<?> cc = clazz;
        do {
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
                // 忽略的属性
                if (StringUtils.isNotBlank(igornFieldNames) && igornFieldNames.contains(field.getName())) {
                    apiItems.add(item);
                    continue;
                }
            }
            cc = cc.getSuperclass();
        }
        while (cc != null && cc != ApiRequest.class && cc != ApiResponse.class && cc != ApiNotify.class && cc != Object.class);
        return apiItems;
    }

//    private String jsonItem(ApiDocItem itemDoc) {
//        StringBuilder jsonBuilder = new StringBuilder();
//        String json = "";
//
//        if (ApiDataTypeEnum.O.equals(itemDoc.getDataType())) {
//            if (!Collections3.isEmpty(itemDoc.getChildren())) {
//                for (ApiDocItem item : itemDoc.getChildren()) {
//                    String childJson = jsonItem(item);
//                    jsonBuilder.append(childJson);
//                    if (StringUtils.isNotBlank(childJson)) {
//                        jsonBuilder.append(",");
//                    }
//                }
//                json = jsonBuilder.toString();
//                json = "{" + json.substring(0, json.length() - 1) + "}";
//            }
//            json = "\"" + itemDoc.getName() + "\"" + ":" + json;
//        } else if (ApiDataTypeEnum.A.equals(itemDoc.getDataType())) {
//            if (!Collections3.isEmpty(itemDoc.getChildren())) {
//                for (ApiDocItem item : itemDoc.getChildren()) {
//                    String childJson = jsonItem(item);
//                    jsonBuilder.append(childJson);
//                    if (StringUtils.isNotBlank(childJson)) {
//                        jsonBuilder.append(",");
//                    }
//                }
//                json = jsonBuilder.toString();
//                json = "[{" + json.substring(0, json.length() - 1) + "}]";
//            }
//            json = "\"" + itemDoc.getName() + "\"" + ":" + json;
//        } else {
//            json += "\"" + itemDoc.getName() + "\":\"" + itemDoc.getDemo() + "\"";
//        }
//
//        return json;
//    }

    private String checkRecursionField(Class<?> clazz) {
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            if (clazz.equals(f.getType()) || clazz.equals(ApiDocPrivateUtils.getParameterGenericType(clazz, f))) {
                return f.getName();
            }
        }
        return null;
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
        String constraint = openApiField.constraint();
        if (StringUtils.isBlank(constraint)) {
            constraint = openApiField.desc();
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


        return new ApiDocItem(field.getName(), title, constraint, apiDataSize.getMin(), apiDataSize.getMax(),
                dataType, demo, fieldStatus, apiEncryptstatus);
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


}

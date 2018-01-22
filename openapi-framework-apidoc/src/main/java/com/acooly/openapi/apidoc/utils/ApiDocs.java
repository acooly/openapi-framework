/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-12-11 00:07 创建
 */
package com.acooly.openapi.apidoc.utils;

import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author zhangpu 2017-12-11 00:07
 */
@Slf4j
public class ApiDocs {

    public static final String API_DOC_COMMON_SPLIT_CHAR = "_";

    public static String getServiceNo(String serviceName, String serviceVersion) {
        return serviceName + API_DOC_COMMON_SPLIT_CHAR + serviceVersion;
    }

    public static String genItemNo(String messageNo, String parentNo, String name) {
        return DigestUtils.md5Hex(messageNo + Strings.trimToEmpty(parentNo) + name);
    }


    public static boolean equelsApiDocService(ApiDocService entity1, ApiDocService entity2) {
        return Strings.equals(ApiDocs.signApiDocService(entity1), ApiDocs.signApiDocService(entity2));
    }

    public static boolean equelsApiDocMessage(ApiDocMessage entity1, ApiDocMessage entity2) {
        return Strings.equals(ApiDocs.signApiDocMessage(entity1), ApiDocs.signApiDocMessage(entity2));
    }

    public static boolean equelsApiDocItem(ApiDocItem entity1, ApiDocItem entity2) {
        return Strings.equals(ApiDocs.signApiDocItem(entity1), ApiDocs.signApiDocItem(entity2));
    }

    public static String signApiDocService(ApiDocService entity) {
        StringBuilder waitToSign = new StringBuilder();
        waitToSign.append(Strings.trimToEmpty(entity.getServiceNo()))
                .append(Strings.trimToEmpty(entity.getTitle()))
                .append(Strings.trimToEmpty(entity.getNote()))
                .append(Strings.trimToEmpty(entity.getServiceType() == null ? null : entity.getServiceType().name()))
                .append(Strings.trimToEmpty(entity.getBusiType() == null ? null : entity.getBusiType().name()));
        return DigestUtils.md5Hex(waitToSign.toString().getBytes());
    }

    public static String signApiDocMessage(ApiDocMessage entity) {
        StringBuilder waitToSign = new StringBuilder();
        waitToSign.append(Strings.trimToEmpty(entity.getServiceNo()))
                .append(Strings.trimToEmpty(entity.getMessageType() == null ? null : entity.getMessageType().name()));
        return DigestUtils.md5Hex(waitToSign.toString().getBytes());
    }


    public static String signApiDocItem(ApiDocItem apiDocItem) {

        StringBuilder waitToSign = new StringBuilder();
        waitToSign.append(Strings.trimToEmpty(apiDocItem.getParentNo()))
                .append(Strings.trimToEmpty(apiDocItem.getItemNo()))
                .append(Strings.trimToEmpty(apiDocItem.getName()))
                .append(Strings.trimToEmpty(apiDocItem.getTitle()))
                .append(Strings.trimToEmpty(apiDocItem.getDescn()))
                .append(String.valueOf(apiDocItem.getMin()))
                .append(String.valueOf(apiDocItem.getMax()))
                .append(apiDocItem.getDataType().code())
                .append(Strings.trimToEmpty(apiDocItem.getDemo()))
                .append(apiDocItem.getStatus().code())
                .append(apiDocItem.getEncryptstatus().code());
        return DigestUtils.md5Hex(waitToSign.toString().getBytes());
    }


}

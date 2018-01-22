/*
 * www.acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved
 */

/*
 * 修订记录:
 * zhangpu@acooly.cn 2017-08-18 12:02 创建
 */
package com.acooly.openapi.apidoc.builder;

import com.acooly.core.utils.Ids;
import com.acooly.core.utils.Strings;
import com.acooly.openapi.apidoc.enums.ApiDataTypeEnum;
import com.acooly.openapi.apidoc.enums.MessageTypeEnum;
import com.acooly.openapi.apidoc.persist.entity.ApiDocItem;
import com.acooly.openapi.apidoc.persist.entity.ApiDocMessage;
import com.acooly.openapi.apidoc.persist.entity.ApiDocService;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.enums.ResponseType;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OpenAPI ApiDoc 示例报文构建实现
 *
 * @author zhangpu
 * @date 2018-01-22 12:02
 */
@Slf4j
@Component
public class ApiDocMessageBuilderImpl implements ApiDocMessageBuilder {

    private static final String key = "123456789";
    private static final String PARTNER_ID = "20141229020000062199";

    @Override
    public List<ApiDocMessageContext> build(ApiDocService apiDocService, String signType) {
        return doBuild(apiDocService, signType);
    }


    public List<ApiDocMessageContext> doBuild(ApiDocService apiDocService, String signType) {
        String requestNo = Ids.oid();
        Map<MessageTypeEnum, Map<String, Object>> messages = Maps.newLinkedHashMap();

        List<ApiDocMessageContext> contexts = Lists.newArrayList();

        List<ApiDocMessage> apiDocMessages = apiDocService.getApiDocMessages();
        MessageTypeEnum messageType = null;
        String messageBody = null;
        for (ApiDocMessage apiDocMessage : apiDocMessages) {
            messageType = apiDocMessage.getMessageType();
            if (messageType == MessageTypeEnum.Redirect) {
                continue;
            }

            Map<String, Object> data = Maps.newLinkedHashMap();
            data.putAll(buildCommonMessage(apiDocService, apiDocMessage, requestNo));
            data.putAll(doBuildMessage(apiDocMessage.getApiDocItems()));
            messageBody = JSON.toJSONString(data,true);
            String sign = DigestUtils.md5Hex(messageBody + key);

            contexts.add(new ApiDocMessageContext(apiDocService.getServiceNo(),
                    messageType,getMessageHeader(messageType,sign,Strings.length(messageBody)),messageBody));

        }
        return contexts;
    }

    protected Map<String, String> buildCommonMessage(ApiDocService apiDocService, ApiDocMessage apiDocMessage, String requestNo) {
        Map<String, String> header = Maps.newHashMap();
        header.put(ApiConstants.PARTNER_ID, PARTNER_ID);
        header.put(ApiConstants.REQUEST_NO, requestNo);
        header.put(ApiConstants.SERVICE, apiDocService.getName());
        header.put(ApiConstants.VERSION, "1.0");
        header.put(ApiConstants.CONTEXT, "会话参数，请求传入，响应和通知报文透传回。");
        MessageTypeEnum messageType = apiDocMessage.getMessageType();
        if (messageType == MessageTypeEnum.Request) {
            if (apiDocService.getServiceType() == ResponseType.REDIRECT) {
                header.put(ApiConstants.RETURN_URL, "https://www.xxx.com/retrueUrl");
                header.put(ApiConstants.NOTIFY_URL, "https://www.xxx.com/notifyUrl");
            }
            if (apiDocService.getServiceType() == ResponseType.ASNY) {
                header.put(ApiConstants.NOTIFY_URL, "https://www.xxx.com/notifyUrl");
            }
        } else if (messageType == MessageTypeEnum.Response) {
            if (apiDocService.getServiceType() == ResponseType.ASNY) {
                header.put(ApiConstants.CODE, "PROCESSING");
                header.put(ApiConstants.MESSAGE, "正在处理中");
            } else {
                header.put(ApiConstants.CODE, "SUCCESS");
                header.put(ApiConstants.MESSAGE, "处理成功");
            }
        } else {
            header.put(ApiConstants.CODE, "SUCCESS");
            header.put(ApiConstants.MESSAGE, "处理成功");
        }


        return header;
    }


    protected String getMessageHeader(MessageTypeEnum messageType, String sign, int contentLength) {

        StringBuilder sb = new StringBuilder();
        if (messageType == MessageTypeEnum.Response) {
            sb.append("HTTP/1.1 200 OK\n");
            sb.append("Date: " + new Date() + "\n");
            sb.append("Content-Type: application/json;charset=UTF-8\n");
            sb.append("Content-Length: " + contentLength + "\n");
            sb.append("Keep-Alive: timeout=15, max=100\n");
            sb.append(ApiConstants.SIGN_TYPE +  ": MD5\n");
            sb.append(ApiConstants.SIGN +  ":" + sign + "\n");
            sb.append("Connection: Keep-Alive\n   \n\n");
        } else if (messageType == MessageTypeEnum.Request) {
            sb.append("POST /gateway.do HTTP/1.1\n");
            sb.append("Content-Length: " + contentLength + "\n");
            sb.append("Content-Type: application/json;charset=UTF-8\n");
            sb.append("Host: api.xxx.com\n");
            sb.append(ApiConstants.SIGN_TYPE +  ": MD5\n");
            sb.append(ApiConstants.SIGN +  ":" + sign + "\n");
            sb.append("Connection: Keep-Alive\n   \n\n");
        } else {
            sb.append("POST /www.mechant.com/" + (messageType == MessageTypeEnum.Notify ? "notify" : "return") + ".html HTTP/1.1\n");
            sb.append("Content-Length: " + contentLength + "\n");
            sb.append("Content-Type: application/x-www-form-urlencoded; charset=UTF-8\n");
            sb.append("Host: www.mechant.com\n");
            sb.append(ApiConstants.SIGN_TYPE +  ": MD5\n");
            sb.append(ApiConstants.SIGN +  ":" + sign + "\n");
            sb.append("Connection: Keep-Alive\n   \n\n");
        }

        return sb.toString();
    }

    protected Map<String, Object> doBuildMessage(List<ApiDocItem> items) {
        Map<String, Object> map = Maps.newHashMap();
        for (ApiDocItem item : items) {
            map.put(item.getName(), doBuildItem(item));
        }
        return map;
    }

    protected Object doBuildItem(ApiDocItem apiDocItem) {

        // 如果有Demo，则直接返回Demo
        if (Strings.isNotBlank(apiDocItem.getDemo())) {
            return apiDocItem.getDemo();
        }

        Integer min = apiDocItem.getMin() == null ? 1 : apiDocItem.getMin();
        Integer max = apiDocItem.getMax() == null ? 10 : apiDocItem.getMax();
        int dataLength = RandomUtils.nextInt(min, max);

        ApiDataTypeEnum dataType = apiDocItem.getDataType();
        Object value = null;
        if (dataType == ApiDataTypeEnum.S || dataType == ApiDataTypeEnum.FS) {
            // 字符串
            value = RandomStringUtils.randomAscii(dataLength);
        } else if (dataType == ApiDataTypeEnum.M) {
            value = "100.00";
        } else if (dataType == ApiDataTypeEnum.N) {
            value = String.valueOf(RandomUtils.nextInt(1, 10000));
        } else if (dataType == ApiDataTypeEnum.O) {
            value = doBuildMessage(apiDocItem.getChildren());
        }else if (dataType == ApiDataTypeEnum.A) {
            value = Lists.newArrayList(doBuildMessage(apiDocItem.getChildren()));
        }

        return value;
    }

}

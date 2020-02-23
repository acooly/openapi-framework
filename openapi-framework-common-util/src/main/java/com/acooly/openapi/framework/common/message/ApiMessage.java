/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2019-09-16 09:31
 */
package com.acooly.openapi.framework.common.message;

import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.enums.ApiProtocol;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Api报文抽象基类Bean
 *
 * @author zhangpu
 * @date 2019-09-16
 */
public abstract class ApiMessage implements Serializable {

    @NotBlank
    @Size(min = 8, max = 64)
    @OpenApiField(desc = "请求流水号", constraint = "商户请求号，全局唯一。建议规则为：商户前缀+唯一标识",
            demo = "201601011212120001", ordinal = ApiConstants.ORDINAL_MIN)
    private String requestNo;

    @NotBlank
    @OpenApiField(desc = "Api服务名", constraint = "必填", demo = "Auth", ordinal = ApiConstants.ORDINAL_MIN + 1)
    private String service;

    @NotBlank
    @OpenApiField(desc = "服务版本", constraint = "必填", demo = "1.0", ordinal = ApiConstants.ORDINAL_MIN + 2)
    private String version = ApiConstants.VERSION_DEFAULT;


    @NotBlank
    @OpenApiField(desc = "商户ID", constraint = "必填", demo = "test", ordinal = ApiConstants.ORDINAL_MAX - 3)
    private String partnerId;

    @OpenApiField(desc = "报文协议", constraint = "定义报文体的协议，默认JSON", demo = "JSON", ordinal = ApiConstants.ORDINAL_MAX - 2)
    private ApiProtocol protocol = ApiProtocol.JSON;

    @Size(max = 128)
    @OpenApiField(desc = "会话参数", constraint = "调用端的API调用会话参数，请求参数任何合法值，在响应时会回传给调用端"
            , demo = "{\"clientUserId\":\"12312321\"}", ordinal = ApiConstants.ORDINAL_MAX - 1)
    private String context;

    @OpenApiField(desc = "扩展参数", constraint = "扩展参数", demo = "{\"xx\":\"yyy\"}", ordinal = ApiConstants.ORDINAL_MAX)
    private Map<String, String> ext = new HashMap();

    /**
     * 增加扩展参数
     */
    public void ext(String key, String value) {
        this.ext.put(key, value);
    }

    /**
     * 获取扩展参数
     */
    public String ext(String key) {
        return this.ext.get(key);
    }


    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public ApiProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(ApiProtocol protocol) {
        this.protocol = protocol;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Map<String, String> getExt() {
        return ext;
    }

    public void setExt(Map<String, String> ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ApiMessage{");
        sb.append("requestNo: '").append(requestNo).append('\'');
        sb.append(", service: '").append(service).append('\'');
        sb.append(", version: '").append(version).append('\'');
        sb.append(", partnerId: '").append(partnerId).append('\'');
        sb.append(", protocol: ").append(protocol);
        sb.append(", context: '").append(context).append('\'');
        sb.append(", ext: ").append(ext);
        sb.append('}');
        return sb.toString();
    }

    public void check() throws RuntimeException {

    }

}

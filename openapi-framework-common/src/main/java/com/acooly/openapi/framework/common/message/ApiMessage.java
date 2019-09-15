package com.acooly.openapi.framework.common.message;

import com.acooly.core.common.facade.InfoBase;
import com.acooly.openapi.framework.common.ApiConstants;
import com.acooly.openapi.framework.common.annotation.OpenApiField;
import com.acooly.openapi.framework.common.enums.ApiProtocol;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Map;

/**
 * 基础报文bean
 *
 * @author zhangpu
 */
@Getter
@Setter
public abstract class ApiMessage extends InfoBase {

    @NotEmpty
    @Size(min = 8, max = 64)
    @OpenApiField(desc = "请求流水号", constraint = "商户请求号，全局唯一。建议规则为：商户前缀+唯一标识",
            demo = "201601011212120001", ordinal = ApiConstants.ORDINAL_MIN)
    private String requestNo;

    @NotEmpty
    @OpenApiField(desc = "Api服务名", constraint = "必填", demo = "Auth", ordinal = ApiConstants.ORDINAL_MIN + 1)
    private String service;

    @NotEmpty
    @OpenApiField(desc = "服务版本", constraint = "必填", demo = "1.0", ordinal = ApiConstants.ORDINAL_MIN + 2)
    private String version = ApiConstants.VERSION_DEFAULT;


    @NotEmpty
    @OpenApiField(desc = "商户ID", constraint = "必填", demo = "test", ordinal = ApiConstants.ORDINAL_MAX - 3)
    private String partnerId;

    @OpenApiField(desc = "报文协议", constraint = "定义报文体的协议，默认JSON", demo = "JSON", ordinal = ApiConstants.ORDINAL_MAX - 2)
    private ApiProtocol protocol = ApiProtocol.JSON;

    @Size(max = 128)
    @OpenApiField(desc = "会话参数", constraint = "调用端的API调用会话参数，请求参数任何合法值，在响应时会回传给调用端"
            , demo = "{\"clientUserId\":\"12312321\"}", ordinal = ApiConstants.ORDINAL_MAX - 1)
    private String context;

    @OpenApiField(desc = "扩展参数", constraint = "扩展参数", demo = "{\"xx\":\"yyy\"}", ordinal = ApiConstants.ORDINAL_MAX)
    private Map<String, String> ext = Maps.newHashMap();

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
}

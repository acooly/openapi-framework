/**
 * openapi-framework
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2020-05-29 13:42
 */
package com.acooly.openapi.framework.core.filterchain;
/**
 * OpenApi filter 定义
 *
 * @author zhangpu
 * @date 2020-05-29 13:42
 */

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum OpenApiFilterEnum implements Messageable {

    InitContext("InitContext", "初始化会话"),
    IpWhitelistAuth("IpWhitelistAuth", "IP白名单认证"),
    RequestAuth("RequestAuth", "认证授权"),
    RequestMarshall("RequestMarshall", "请求解析"),
    ServiceExecute("ServiceExecute", "服务执行"),
    ExceptionHandler("ExceptionHandler", "异常处理"),
    ResponseMarshall("ResponseMarshall", "响应组装"),
    FinishHandle("FinishHandle", "回收处理");
    
    private final String code;
    private final String message;

    OpenApiFilterEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (OpenApiFilterEnum type : values()) {
            map.put(type.getCode(), type.getMessage());
        }
        return map;
    }

    /**
     * 通过枚举值码查找枚举值。
     *
     * @param code 查找枚举值的枚举值码。
     * @return 枚举值码对应的枚举值。
     * @throws IllegalArgumentException 如果 code 没有对应的 Status 。
     */
    public static OpenApiFilterEnum find(String code) {
        for (OpenApiFilterEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<OpenApiFilterEnum> getAll() {
        List<OpenApiFilterEnum> list = new ArrayList<OpenApiFilterEnum>();
        for (OpenApiFilterEnum status : values()) {
            list.add(status);
        }
        return list;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllCode() {
        List<String> list = new ArrayList<String>();
        for (OpenApiFilterEnum status : values()) {
            list.add(status.code());
        }
        return list;
    }

}

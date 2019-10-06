/*
 * acooly.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */
package com.acooly.openapi.framework.common.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 支持的签名类型
 *
 * @author zhangpu
 * @date 2014年8月3日
 */
public enum SignTypeEnum implements Messageable {
    MD5("MD5", "MD5"),
    SHA1HEX("SHA1HEX", "SHA1HEX"),
    SHA256HEX("SHA256HEX", "SHA256HEX"),
    HMACSHA1HEX("HMACSHA1HEX", "HMACSHA1HEX");
    private final String code;
    private final String message;

    private SignTypeEnum(String code, String message) {
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
        for (SignTypeEnum type : values()) {
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
    public static SignTypeEnum find(String code) {
        for (SignTypeEnum status : values()) {
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
    public static List<SignTypeEnum> getAll() {
        List<SignTypeEnum> list = new ArrayList<SignTypeEnum>();
        for (SignTypeEnum status : values()) {
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
        for (SignTypeEnum status : values()) {
            list.add(status.code());
        }
        return list;
    }

}

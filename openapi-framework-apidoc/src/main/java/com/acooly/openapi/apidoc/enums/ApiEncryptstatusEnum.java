/*
 * acooly.cn Inc.
 * Copyright (c) 2017 All Rights Reserved.
 * create by acooly
 * date:2017-12-05
 *
 */
package com.acooly.openapi.apidoc.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 报文字段 ApiDocItemEncryptstatusEnum 枚举定义
 *
 * @author acooly
 * Date: 2017-12-05 12:34:39
 */
public enum ApiEncryptstatusEnum implements Messageable {

    yes("yes", "需要"),

    no("no", "无需");

    private final String code;
    private final String message;

    private ApiEncryptstatusEnum(String code, String message) {
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
        for (ApiEncryptstatusEnum type : values()) {
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
    public static ApiEncryptstatusEnum find(String code) {
        for (ApiEncryptstatusEnum status : values()) {
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
    public static List<ApiEncryptstatusEnum> getAll() {
        List<ApiEncryptstatusEnum> list = new ArrayList<ApiEncryptstatusEnum>();
        for (ApiEncryptstatusEnum status : values()) {
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
        for (ApiEncryptstatusEnum status : values()) {
            list.add(status.code());
        }
        return list;
    }

}

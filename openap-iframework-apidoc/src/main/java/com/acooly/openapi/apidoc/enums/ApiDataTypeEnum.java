/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.enums;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * API item 数据类型
 *
 * @author zhangpu
 */
public enum ApiDataTypeEnum implements Messageable {

    /**
     * 字符型
     */
    S("S", "字符串"),

    FS("FS", "定长字符串"),

    /**
     * 数字型
     */
    N("N", "整数"),
    /**
     * 货币型
     */
    M("M", "金额"),
    /**
     * Decimal型
     */
    D("D", "小数"),

    /**
     * Boolean
     */
    B("B", "布尔"),

    /**
     * JSON串
     */
    O("O", "对象"),

    /**
     * JSON数组
     */
    A("A", "数组");


    private final String code;
    private final String message;

    private ApiDataTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (ApiDataTypeEnum type : values()) {
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
    public static ApiDataTypeEnum find(String code) {
        for (ApiDataTypeEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("ApiDataType not legal:" + code);
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<ApiDataTypeEnum> getAll() {
        List<ApiDataTypeEnum> list = new ArrayList<ApiDataTypeEnum>();
        for (ApiDataTypeEnum status : values()) {
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
        for (ApiDataTypeEnum status : values()) {
            list.add(status.code());
        }
        return list;
    }


}

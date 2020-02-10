/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by acooly
 * date:2016-07-16
 *
 */
package com.acooly.openapi.framework.common.enums;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 合作方管理 SecretType 枚举定义
 *
 * @author acooly Date: 2016-07-16 02:05:01
 */
public enum SecretType implements Messageable {

    /**
     * 摘要方式 MD5/SHA等
     */
    digest("digest", "摘要", Lists.newArrayList(SignType.MD5, SignType.SHA256HEX)),

    /**
     * 证书/RSA
     */
    cert("cert", "证书", Lists.newArrayList(SignType.RSA));

    private final String code;
    private final String message;
    private final List<SignType> signTypes;

    SecretType(String code, String message, List<SignType> signTypes) {
        this.code = code;
        this.message = message;
        this.signTypes = signTypes;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (SecretType type : values()) {
            map.put(type.getCode(), type.getMessage());
        }
        return map;
    }

    /**
     * 通过枚举值码查找枚举值。
     *
     * @param code 查找枚举值的枚举值码。
     * @return 枚举值码对应的枚举值。
     */
    public static SecretType find(String code) {
        for (SecretType status : values()) {
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
    public static List<SecretType> getAll() {
        List<SecretType> list = new ArrayList<SecretType>();
        for (SecretType status : values()) {
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
        for (SecretType status : values()) {
            list.add(status.code());
        }
        return list;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<SignType> getSignTypes() {
        return signTypes;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", this.code, this.message);
    }
}

/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.enums;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 数据项状态 可选，必选，条件
 * <p/>
 * Created by zhangpu on 2015/4/10.
 */
public enum FieldStatus implements Messageable {

    /**
     * 必选
     */
    M("M", "必须"),
    /**
     * 可选
     */
    O("O", "可选"),

    /**
     * 条件可选
     */
    C("C", "条件可选");

    private String key;
    private String message;

    FieldStatus(String key, String message) {
        this.key = key;
        this.message = message;
    }

    public String getKey() {
        return key;
    }

    public String getMessage() {
        return message;
    }





    public static Map<String, String> mapping() {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (FieldStatus type : values()) {
            map.put(type.key, type.message);
        }
        return map;
    }

    @Override
    public String code() {
        return this.key;
    }

    @Override
    public String message() {
        return this.message;
    }
}

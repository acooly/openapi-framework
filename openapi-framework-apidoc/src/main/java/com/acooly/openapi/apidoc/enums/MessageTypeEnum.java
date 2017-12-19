/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

package com.acooly.openapi.apidoc.enums;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public enum MessageTypeEnum {

    Request("Request", "请求报文"),

    Response("Response", "响应报文"),

    Return("Return", "通知报文"),

    Redirect("Redirect", "跳转通知"),

    Notify("Notify", "通知报文");

    /**
     * 枚举值
     */
    private final String code;

    /**
     * 枚举描述
     */
    private final String message;

    MessageTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return Returns the code.
     */
    public String code() {
        return code;
    }

    /**
     * @return Returns the message.
     */
    public String message() {
        return message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举
     *
     * @param code
     * @return DeductResultEnum
     */
    public static MessageTypeEnum getByCode(String code) {
        for (MessageTypeEnum _enum : values()) {
            if (_enum.getCode().equals(code)) {
                return _enum;
            }
        }
        return null;
    }

    /**
     * 获取全部枚举
     *
     * @return List<DeductResultEnum>
     */
    public static List<MessageTypeEnum> getAllEnum() {
        List<MessageTypeEnum> list = new ArrayList<MessageTypeEnum>();
        for (MessageTypeEnum _enum : values()) {
            list.add(_enum);
        }
        return list;
    }

    /**
     * 获取全部枚举值
     *
     * @return List<String>
     */
    public List<String> getAllEnumCode() {
        List<String> list = new ArrayList<String>();
        for (MessageTypeEnum _enum : values()) {
            list.add(_enum.code());
        }
        return list;
    }


    public static Map<String, String> mapping() {
        Map<String, String> map = Maps.newLinkedHashMap();
        for (MessageTypeEnum type : values()) {
            map.put(type.getCode(), type.getMessage());
        }
        return map;
    }

    @Override
    public String toString() {
        return code + ":" + message;
    }
}

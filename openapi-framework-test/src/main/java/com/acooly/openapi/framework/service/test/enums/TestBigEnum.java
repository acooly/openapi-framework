/*
 * acooly.com Inc.
 * Copyright (c) 2015 All Rights Reserved.
 */

/*
 * 修订记录：
 * faZheng 2016年2月22日 下午2:45:51 创建
 */

package com.acooly.openapi.framework.service.test.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试用大枚举对象
 * @author zhangpu
 */
public enum TestBigEnum implements Messageable {

    /**
     * 未认证
     */
    BUSINESS_STATUS0("BUSINESS_STATUS0", "BUSINESS_STATUS0"),
    BUSINESS_STATUS1("BUSINESS_STATUS1", "BUSINESS_STATUS1"),
    BUSINESS_STATUS2("BUSINESS_STATUS2", "BUSINESS_STATUS2"),
    BUSINESS_STATUS3("BUSINESS_STATUS3", "BUSINESS_STATUS3"),
    BUSINESS_STATUS4("BUSINESS_STATUS4", "BUSINESS_STATUS4"),
    BUSINESS_STATUS5("BUSINESS_STATUS5", "BUSINESS_STATUS5"),
    BUSINESS_STATUS6("BUSINESS_STATUS6", "BUSINESS_STATUS6"),
    BUSINESS_STATUS7("BUSINESS_STATUS7", "BUSINESS_STATUS7"),
    BUSINESS_STATUS8("BUSINESS_STATUS8", "BUSINESS_STATUS8"),
    BUSINESS_STATUS9("BUSINESS_STATUS9", "BUSINESS_STATUS9"),
    BUSINESS_STATUS10("BUSINESS_STATUS10", "BUSINESS_STATUS0"),
    BUSINESS_STATUS11("BUSINESS_STATUS11", "BUSINESS_STATUS1"),
    BUSINESS_STATUS12("BUSINESS_STATUS12", "BUSINESS_STATUS2"),
    BUSINESS_STATUS13("BUSINESS_STATUS13", "BUSINESS_STATUS3"),
    BUSINESS_STATUS14("BUSINESS_STATUS14", "BUSINESS_STATUS4"),
    BUSINESS_STATUS15("BUSINESS_STATUS15", "BUSINESS_STATUS5"),
    BUSINESS_STATUS16("BUSINESS_STATUS16", "BUSINESS_STATUS6"),
    BUSINESS_STATUS17("BUSINESS_STATUS17", "BUSINESS_STATUS7"),
    BUSINESS_STATUS18("BUSINESS_STATUS18", "BUSINESS_STATUS8"),
    BUSINESS_STATUS19("BUSINESS_STATUS19", "BUSINESS_STATUS9"),
    BUSINESS_STATUS20("BUSINESS_STATUS20", "BUSINESS_STATUS0"),
    BUSINESS_STATUS21("BUSINESS_STATUS21", "BUSINESS_STATUS1"),
    BUSINESS_STATUS22("BUSINESS_STATUS22", "BUSINESS_STATUS2"),
    BUSINESS_STATUS23("BUSINESS_STATUS23", "BUSINESS_STATUS3"),
    BUSINESS_STATUS24("BUSINESS_STATUS24", "BUSINESS_STATUS4"),
    BUSINESS_STATUS25("BUSINESS_STATUS25", "BUSINESS_STATUS5"),
    BUSINESS_STATUS26("BUSINESS_STATUS26", "BUSINESS_STATUS6"),
    BUSINESS_STATUS27("BUSINESS_STATUS27", "BUSINESS_STATUS7"),
    BUSINESS_STATUS28("BUSINESS_STATUS28", "BUSINESS_STATUS8"),
    BUSINESS_STATUS29("BUSINESS_STATUS29", "BUSINESS_STATUS9"),
    BUSINESS_STATUS30("BUSINESS_STATUS30", "BUSINESS_STATUS0"),
    BUSINESS_STATUS31("BUSINESS_STATUS31", "BUSINESS_STATUS1"),
    BUSINESS_STATUS32("BUSINESS_STATUS32", "BUSINESS_STATUS2"),
    BUSINESS_STATUS33("BUSINESS_STATUS33", "BUSINESS_STATUS3"),
    BUSINESS_STATUS34("BUSINESS_STATUS34", "BUSINESS_STATUS4"),
    BUSINESS_STATUS35("BUSINESS_STATUS35", "BUSINESS_STATUS5"),
    BUSINESS_STATUS36("BUSINESS_STATUS36", "BUSINESS_STATUS6"),
    BUSINESS_STATUS37("BUSINESS_STATUS37", "BUSINESS_STATUS7"),
    BUSINESS_STATUS38("BUSINESS_STATUS38", "BUSINESS_STATUS8"),
    BUSINESS_STATUS39("BUSINESS_STATUS39", "BUSINESS_STATUS9"),


    ;
    private final String code;
    private final String message;

    TestBigEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = new HashMap<String, String>();
        for (TestBigEnum type : values()) {
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
    public static TestBigEnum getByCode(String code) {
        for (TestBigEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("UserAuthEnum not legal:" + code);
    }

    /**
     * 获取全部枚举值。
     *
     * @return 全部枚举值。
     */
    public static List<TestBigEnum> getAllEnum() {
        List<TestBigEnum> list = new ArrayList<TestBigEnum>();
        for (TestBigEnum status : values()) {
            list.add(status);
        }
        return list;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllEnumCode() {
        List<String> list = new ArrayList<String>();
        for (TestBigEnum status : values()) {
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

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", this.code, this.message);
    }
}

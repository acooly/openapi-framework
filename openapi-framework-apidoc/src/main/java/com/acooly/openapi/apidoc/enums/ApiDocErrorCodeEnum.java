package com.acooly.openapi.apidoc.enums;
/**
 * @author liangsong
 * @date 2019-12-02 11:43
 */

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum ApiDocErrorCodeEnum implements Messageable {
    /**
     * 只能批量删除同层节点
     */
    SAME_LEVEL_BATCH_DELETE_CONFLICT("SAME_LEVEL_BATCH_DELETE_CONFLICT", "只能批量删除本层的节点数据"),

    EXIST_SUB_NODE_CONFLICT("EXIST_SUB_NODE_CONFLICT", "存在下级节点，不能直接删除"),
    ;
    private final String code;
    private final String message;

    ApiDocErrorCodeEnum(String code, String message) {
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
        for (ApiDocErrorCodeEnum type : values()) {
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
    public static ApiDocErrorCodeEnum find(String code) {
        for (ApiDocErrorCodeEnum status : values()) {
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
    public static List<ApiDocErrorCodeEnum> getAll() {
        List<ApiDocErrorCodeEnum> list = new ArrayList<ApiDocErrorCodeEnum>();
        for (ApiDocErrorCodeEnum status : values()) {
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
        for (ApiDocErrorCodeEnum status : values()) {
            list.add(status.code());
        }
        return list;
    }

}

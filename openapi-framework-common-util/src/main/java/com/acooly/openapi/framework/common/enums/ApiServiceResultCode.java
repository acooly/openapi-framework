package com.acooly.openapi.framework.common.enums;

import com.acooly.core.utils.enums.Messageable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenApi错误信息定义
 *
 * @author zhangpu
 */
public enum ApiServiceResultCode implements Messageable {

    /**
     * 成功
     */
    SUCCESS("SUCCESS", "成功"),
    /**
     * 处理中
     */
    PROCESSING("PROCESSING", "处理中"),
    /**
     * 失败
     */
    FAILURE("FAILURE", "执行失败"),


    INTERNAL_ERROR("INTERNAL_ERROR", "内部错误"),
    PARAMETER_ERROR("PARAMETER_ERROR", "参数错误"),
    PARAM_FORMAT_ERROR("PARAM_FORMAT_ERROR", "参数格式错误"),

    TEST_NOT_SUPPORT_IN_PRODUCTION("TEST_NOT_SUPPORT_IN_PRODUCTION","生产环境不能使用测试帐号"),
    ACCESS_KEY_STATE_ERROR("ACCESS_KEY_STATE_ERROR", "访问码状态非法"),
    ACCESS_KEY_NOT_EXIST("ACCESS_KEY_NOT_EXIST", "访问码非法"),
    UNAUTHENTICATED_ERROR("UNAUTHENTICATED_ERROR", "签名认证错误"),
    UNAUTH_PASSWORD_ERROR("UNAUTH_PASSWORD_ERROR", "密码或账户错误"),
    UNAUTH_IP_ERROR("UNAUTH_IP_ERROR", "请求IP未通过白名单认证"),
    REQUEST_NO_NOT_UNIQUE("REQUEST_NO_NOT_UNIQUE", "请求号重复"),
    FIELD_NOT_UNIQUE("FIELD_NOT_UNIQUE", "对象字段重复"),
    TOO_MANY_REQUEST("TOO_MANY_REQUEST", "请求数太多"),
    MOCK_NOT_FOUND("MOCK_NOT_FOUND", "MOCK请求不匹配"),
    REQUEST_GID_NOT_EXSIT("REQUEST_GID_NOT_EXSIT", "gid不存在"),
    SERVICE_NOT_FOUND_ERROR("SERVICE_NOT_FOUND_ERROR", "服务不存在"),
    UNAUTHORIZED_ERROR("UNAUTHORIZED_ERROR", "未授权的服务"),
    REDIRECT_URL_NOT_EXIST("REDIRECT_URL_NOT_EXIST", "跳转服务需设置redirectUrl"),
    UNSUPPORTED_PROTOCOL("UNSUPPORTED_PROTOCOL", "不支持的报文协议类型"),
    OBJECT_NOT_EXIST("OBJECT_NOT_EXIST", "对象不存在"),

    CRYPTO_ERROR("CRYPTO_ERROR", "加解密错误"),

    SEND_REDIRECT_ERROR("SEND_REDIRECT_ERROR","跳转请求失败"),

    JSON_BODY_PARSING_FAILED("JSON_BODY_PARSING_FAILED", "JSON报文体解析失败"),

    /**
     * 合作伙伴id没有在openapi中注册
     */
    PARTNER_NOT_REGISTER("PARTNER_NOT_REGISTER", "商户没有注册"),
    /**
     * 合作伙伴id没有产品
     */
    PARTNER_NOT_PRODUCT("PARTNER_NOT_PRODUCT", "商户没有配置产品"),

    NOTIFY_ERROR("NOTIFY_ERROR", "异步通知失败");

    private final String code;
    private final String message;

    ApiServiceResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Map<String, String> mapping() {
        Map<String, String> map = new LinkedHashMap();
        for (ApiServiceResultCode type : values()) {
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
    public static ApiServiceResultCode findStatus(String code) {
        for (ApiServiceResultCode status : values()) {
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
    public static List<ApiServiceResultCode> getAllStatus() {
        List<ApiServiceResultCode> list = new ArrayList<ApiServiceResultCode>();
        for (ApiServiceResultCode status : values()) {
            list.add(status);
        }
        return list;
    }

    /**
     * 获取全部枚举值码。
     *
     * @return 全部枚举值码。
     */
    public static List<String> getAllStatusCode() {
        List<String> list = new ArrayList<String>();
        for (ApiServiceResultCode status : values()) {
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

package com.acooly.openapi.framework.common.enums;

import com.acooly.core.utils.enums.Messageable;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum ApiServiceResultCode implements Messageable {
	SUCCESS("EXECUTE_SUCCESS", "成功"),

	PROCESSING("EXECUTE_PROCESSING", "处理中"),

	INTERNAL_ERROR("INTERNAL_ERROR", "内部错误"),

	PARAMETER_ERROR("PARAMETER_ERROR", "参数错误"),

	UN_AUTHENTICATED_ERROR("UNAUTHENTICATED", "认证(签名)错误"),

	PARAM_FORMAT_ERROR("PARAM_FORMAT_ERROR", "参数格式错误"),

	SERVICE_NOT_FOUND_ERROR("SERVICE_NOT_FOUND_ERROR", "服务不存在"),

	UN_AUTHORIZED_ERROR("UNAUTHORIZED", "未授权的服务"),

	REQUEST_NO_NOT_UNIQUE("REQUEST_NO_NOT_UNIQUE", "请求号重复"),

	FIELD_NOT_UNIQUE("FIELD_NOT_UNIQUE", "对象字段重复"),

	REDIRECT_URL_NOT_EXIST("REDIRECT_URL_NOT_EXIST", "跳转服务需设置redirectUrl"),
	/**
	 * 合作伙伴id没有在openapi中注册
	 */
	PARTNER_NOT_REGISTER("PARTNER_NOT_REGISTER", "商户没有注册"),
	/**
	 * 合作伙伴id没有产品
	 */
	PARTNER_NOT_PRODUCT("PARTNER_NOT_PRODUCT", "商户没有配置产品"),

	UNSUPPORTED_SECHEME("UNSUPPORTED_SECHEME", "不支持的请求协议"),

	NOTIFY_ERROR("NOTIFY_ERROR", "异步通知失败"),

	REQUEST_GID_NOT_EXSIT("REQUEST_GID_NOT_EXSIT", "gid不存在"),
	APP_CLIENT_NOT_SUPPORT("APP_CLIENT_NOT_SUPPORT","不支持移动端访问"),
	;
	private final String code;
	private final String message;

	ApiServiceResultCode(String code, String message) {
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
		for (ApiServiceResultCode type : values()) {
			map.put(type.getCode(), type.getMessage());
		}
		return map;
	}

	/**
	 * 通过枚举值码查找枚举值。
	 * 
	 * @param code
	 *            查找枚举值的枚举值码。
	 * @return 枚举值码对应的枚举值。
	 * @throws IllegalArgumentException
	 *             如果 code 没有对应的 Status 。
	 */
	public static ApiServiceResultCode findStatus(String code) {
		for (ApiServiceResultCode status : values()) {
			if (status.getCode().equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("ApiServiceResultCode not legal:" + code);
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

	@Override
	public String toString() {
		return String.format("%s:%s", this.code, this.message);
	}

}

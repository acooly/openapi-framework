package com.acooly.openapi.framework.common;

import java.util.regex.Pattern;

/**
 * Api 公共协议字段常量
 *
 * @author zhangpu
 * @date 2014年5月16日
 */
public final class ApiConstants {
    public static final String PERFORMANCE_LOGGER = "com.acooly.framework.openapi_PERFORMANCE_LOGGER";

    /**
     * http头方式传入的参数名称，内部会转换为统一的协议key(去除前缀x-api-)
     */
    public static final String X_HEAD_PREFIX = "x-api-";
    public static final String X_API_PROTOCOL = "x-api-protocol";
    public static final String X_API_ACCESS_KEY = "x-api-accessKey";
    public static final String X_API_SIGN = "x-api-sign";
    public static final String X_API_SIGN_TYPE = "x-api-signType";

    /**
     * 框架内部统一的协议key
     */
    public static final String PROTOCOL = "protocol";
    public static final String ACCESS_KEY = "accessKey";
    public static final String SIGN = "sign";
    public static final String SIGN_TYPE = "signType";

    public static final String REQUEST_NO = "requestNo";
    public static final String PARTNER_ID = "partnerId";
    public static final String SERVICE = "service";
    public static final String VERSION = "version";
    public static final String NOTIFY_URL = "notifyUrl";
    public static final String RETURN_URL = "returnUrl";


    public static final String MERCH_ORDER_NO = "merchOrderNo";

    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String DETAIL = "detail";
    public static final String SUCCESS = "success";

    public static final String GID = "gid";
    public static final String CONTEXT = "context";

    public static final String VERSION_DEFAULT = "1.0";

    public static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String BODY = "body";

    public static final String NOTIFY_SUCCESS_CONTENT = "success";

    public static final String LOGIN_SERVICE_NAME = "login";
    public static final String WILDCARD_TOKEN = "*";
    public static final String TEST_ACCESS_KEY = "test";
    public static final String TEST_SECRET_KEY = "06f7aab08aa2431e6dae6a156fc9e0b4";

    public static final String ANONYMOUS_ACCESS_KEY = "anonymous";

    public static final String ANONYMOUS_SECRET_KEY = "anonymouanonymou";

    public static Pattern PERM_PATTERN = Pattern.compile("\\S+:\\S+");

    public static final String BUILDIN_APIDOC_CODE = "build-in";
    public static final String BUILDIN_APIDOC_NAME = "系统服务";


    public static final int ORDINAL_MAX = 100000;
    public static final int ORDINAL_MIN = -100000;

    private ApiConstants() {
        super();
    }
}

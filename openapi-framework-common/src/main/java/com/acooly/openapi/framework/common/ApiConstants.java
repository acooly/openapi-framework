package com.acooly.openapi.framework.common;

/**
 * Api 公共协议字段常量
 *
 * @author zhangpu
 * @date 2014年5月16日
 */
public final class ApiConstants {
  public static final String PERFORMANCE_LOGGER = "com.acooly.framework.openapi_PERFORMANCE_LOGGER";

  public static final String REQUEST_NO = "requestNo";
  public static final String PARTNER_ID = "partnerId";
  public static final String ORDER_NO = "orderNo";
  public static final String ACCESS_KEY = "x-api-accessKey";
  public static final String SIGN = "x-api-sign";
  public static final String SIGN_TYPE = "x-api-signType";
  public static final String SERVICE = "service";
  public static final String NOTIFY_URL = "notifyUrl";
  public static final String RETURN_URL = "returnUrl";
  public static final String RESULT_CODE = "resultCode";
  public static final String RESULT_MESSAGE = "resultMessage";
  public static final String RESULT_DETAIL = "resultDetail";
  public static final String SUCCESS = "success";
  public static final String PROTOCOL = "protocol";
  public static final String VERSION = "version";
  public static final String GID = "gid";
  public static final String CONTEXT = "context";
  public static final String MERCH_ORDER_NO = "merchOrderNo";
  public static final String SEND_MESSAGE_SERVICE_NAME = "sendMessageService";

  public static final String VERSION_DEFAULT = "1.0";

  public static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final String BODY = "body";

  public static final String NOTIFY_SUCCESS_CONTENT = "success";

  public static final String LOGIN_SERVICE_NAME = "login";
  public static final String WILDCARD_TOKEN = "*";
  public static final String ALL_WILDCARD_TOKEN = "*:*";
  public static final String TEST_ACCESS_KEY = "test";
  public static final String TEST_SECRET_KEY = "c9cef22553af973d4b04a012f9cb8ea8";


  private ApiConstants() {
    super();
  }
}

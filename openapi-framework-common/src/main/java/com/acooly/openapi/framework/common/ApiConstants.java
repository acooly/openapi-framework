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
  public static final String ORDER_NO = "orderNo";
  public static final String PARTNER_ID = "x-api-paternId";
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
  public static final String OID = "oid";
  public static final String MERCH_ORDER_NO = "merchOrderNo";
  public static final String SEND_MESSAGE_SERVICE_NAME = "sendMessageService";

  public static final String VERSION_DEFAULT = "1.0";

  public static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final String REDIRECT_DATA_KEY = "body";


  private ApiConstants() {
    super();
  }
}

/** create by zhangpu date:2015年9月11日 */
package com.acooly.openapi.framework.app.openapi.message;


import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.ApiResponse;

/**
 * APP崩溃上报 请求
 *
 * @author zhangpu
 * @date 2015年9月11日
 */
@OpenApiMessage(service = "appCrashReport", type = ApiMessageType.Response)
public class AppCrashReportResponse extends ApiResponse {}

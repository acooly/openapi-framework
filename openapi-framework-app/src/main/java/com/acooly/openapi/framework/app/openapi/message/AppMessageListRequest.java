/** create by zhangpu date:2016年2月13日 */
package com.acooly.openapi.framework.app.openapi.message;

import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.PageApiRequest;

/**
 * 推送消息列表 请求
 *
 * @author zhangpu
 * @date 2016年2月13日
 */
@OpenApiMessage(service = "appMessageList", type = ApiMessageType.Request)
public class AppMessageListRequest extends PageApiRequest {}

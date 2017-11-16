/** create by zhangpu date:2016年2月13日 */
package com.acooly.openapi.framework.app.openapi.message;

import com.acooly.openapi.framework.app.openapi.dto.AppMessageDto;
import com.acooly.openapi.framework.common.annotation.OpenApiMessage;
import com.acooly.openapi.framework.common.enums.ApiMessageType;
import com.acooly.openapi.framework.common.message.PageApiResponse;

/**
 * @author zhangpu
 * @date 2016年2月13日
 */
@OpenApiMessage(service = "appMessageList", type = ApiMessageType.Response)
public class AppMessageListResponse extends PageApiResponse<AppMessageDto> {}
